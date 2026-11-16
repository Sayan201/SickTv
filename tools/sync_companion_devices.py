#!/usr/bin/env python3
"""Syncs all supported devices from examples/ into launcher/companion/devices/.

Generates launcher/companion/devices/devices/<id>.json for every folder in examples/,
and rebuilds launcher/companion/devices/index.json with the full list and SHA-256 hashes.
"""
import glob
import hashlib
import json
from pathlib import Path
import re
import sys

ROOT = Path(__file__).resolve().parents[1]
EXAMPLES = ROOT / "examples"
DEVICES_DIR = ROOT / "launcher/companion/devices/devices"
INDEX_PATH = ROOT / "launcher/companion/devices/index.json"
MATCH_DIR = ROOT / "launcher/companion/devices/match"

CLEAN_COMMIT = "ddc3b2cdce299a5f30aa2da2437edc40f3cf6f2e"
PACKAGE_RE = re.compile(r"^[A-Za-z_][A-Za-z0-9_]*(\.[A-Za-z0-9_]+)+$")


def get_sdk_from_os(os_str):
    if not os_str:
        return None
    s = os_str.lower()
    m = re.search(r"android\s*(?:tv)?\s*(\d+)", s)
    if m:
        v = int(m.group(1))
        mapping = {7: 24, 8: 26, 9: 28, 10: 29, 11: 30, 12: 31, 13: 33, 14: 34, 15: 35, 16: 36}
        return mapping.get(v, None)
    m = re.search(r"google\s*tv\s*(\d+)", s)
    if m:
        v = int(m.group(1))
        mapping = {11: 30, 12: 31, 14: 34, 15: 35}
        return mapping.get(v, 34 if v >= 14 else None)
    m = re.search(r"fire\s*os\s*(\d+)", s)
    if m:
        v = int(m.group(1))
        mapping = {5: 22, 6: 25, 7: 28, 8: 30}
        return mapping.get(v, None)
    return None


def parse_debloat(path):
    text = path.read_text(encoding="utf-8-sig", errors="replace")
    lines = text.splitlines()

    launcher = None
    build = None

    for line in lines[:25]:
        l_lower = line.lower()
        if "stock launcher:" in l_lower:
            parts = line.split(":", 1)[1].strip().split()
            if parts and PACKAGE_RE.match(parts[0]):
                launcher = parts[0]
        m_build = re.search(r"(?:build|firmware)\s+([A-Za-z0-9._-]+)", line, re.I)
        if m_build and not build:
            cand = m_build.group(1)
            if cand.lower() not in ["family", "the", "android", "version", "and", "is"]:
                build = cand

    groups = []
    current_title = "Debloat"
    current_pkgs = []

    ignore_starts = [
        "apply:", "undo:", "tested on", "same ", "everything is",
        "already disabled", "kept on", "the apex", "quirks", "note",
        "1.", "2.", "3.", "http",
    ]

    for line in lines:
        raw_line = line.strip()
        if not raw_line:
            continue

        if raw_line.startswith("#"):
            comment = raw_line.lstrip("#").strip()
            # check if this is a commented out package: "# com.foo.bar # note"
            c_body, _, c_note = comment.partition("#")
            c_words = c_body.split()
            if len(c_words) == 1 and PACKAGE_RE.match(c_words[0]):
                pkg_item = {"pkg": c_words[0], "optional": True}
                if c_note.strip():
                    pkg_item["note"] = c_note.strip()
                current_pkgs.append(pkg_item)
                continue

            if any(comment.lower().startswith(x) for x in ignore_starts):
                continue

            if len(comment) < 140 and not comment.endswith("."):
                if current_pkgs:
                    groups.append({"title": current_title, "packages": current_pkgs})
                    current_pkgs = []
                current_title = comment
            continue

        # Active package line
        body, _, note = raw_line.partition("#")
        pkg = body.strip()
        note = note.strip()
        if PACKAGE_RE.match(pkg):
            pkg_item = {"pkg": pkg}
            if note:
                pkg_item["note"] = note
            current_pkgs.append(pkg_item)
            if not launcher:
                if "launcherx" in pkg:
                    launcher = "com.google.android.apps.tv.launcherx"
                elif "tvlauncher" in pkg:
                    launcher = "com.google.android.tvlauncher"

    if current_pkgs:
        groups.append({"title": current_title, "packages": current_pkgs})

    return groups, launcher, build


def main():
    DEVICES_DIR.mkdir(parents=True, exist_ok=True)

    # Read existing index to preserve lists, logos, and any custom metadata
    existing_index = {}
    if INDEX_PATH.is_file():
        try:
            existing_index = json.loads(INDEX_PATH.read_text(encoding="utf-8"))
        except Exception as e:
            print(f"Warning: could not read {INDEX_PATH}: {e}", file=sys.stderr)

    existing_devices_meta = {}
    for d in existing_index.get("devices", []):
        existing_devices_meta[d["id"]] = d

    # Collect all example folders
    folders = sorted(p for p in EXAMPLES.iterdir() if p.is_dir())
    print(f"Found {len(folders)} device folders in examples/")

    new_device_entries = []

    for folder in folders:
        device_id = folder.name
        debloat_path = folder / "debloat.txt"
        tv_json_path = folder / "tv.json"

        if not debloat_path.is_file():
            print(f"Skipping {device_id}: no debloat.txt")
            continue

        tv_info = {}
        if tv_json_path.is_file():
            try:
                tv_info = json.loads(tv_json_path.read_text(encoding="utf-8"))
            except Exception:
                pass

        device_file_path = DEVICES_DIR / f"{device_id}.json"
        existing_data = None
        if device_file_path.is_file():
            try:
                existing_data = json.loads(device_file_path.read_text(encoding="utf-8"))
            except Exception:
                pass

        if existing_data:
            # Preserve existing device json, update commit if needed
            data = existing_data
            name = data.get("name") or tv_info.get("tv") or device_id
            sdk = data.get("tested", {}).get("sdk") if isinstance(data.get("tested"), dict) else None
        else:
            # Generate from scratch
            raw_tv = tv_info.get("tv", device_id)
            # Clean name: before parenthesis, e.g. "Acer AR32HDIGU2841AT 32\" HD"
            name = raw_tv.split("(")[0].strip().rstrip(",")
            if not name:
                name = raw_tv

            os_str = tv_info.get("os", "")
            sdk = get_sdk_from_os(os_str)
            groups, launcher, build = parse_debloat(debloat_path)

            # Fire TV launcher is null
            if "fire" in device_id.lower() or "fire os" in os_str.lower():
                launcher = None

            data = {
                "schema": 1,
                "id": device_id,
                "name": name,
                "tested": {
                    "os": os_str or None,
                    "sdk": sdk,
                    "build": build,
                },
                "launcher": launcher,
                "keep": [],
                "groups": groups,
                "source": {
                    "repo": "Gathin23/cleanyourtv",
                    "path": f"examples/{device_id}/debloat.txt",
                    "commit": CLEAN_COMMIT,
                },
            }
            device_file_path.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")

        # Compute SHA-256
        sha = hashlib.sha256(device_file_path.read_bytes()).hexdigest()

        # Check match rule if exists
        match_rule = None
        if device_id in existing_devices_meta and existing_devices_meta[device_id].get("match"):
            match_rule = existing_devices_meta[device_id]["match"]
        else:
            match_file = MATCH_DIR / f"{device_id}.json"
            if match_file.is_file():
                try:
                    match_rule = json.loads(match_file.read_text(encoding="utf-8"))
                except Exception:
                    pass

        new_device_entries.append({
            "id": device_id,
            "name": name,
            "match": match_rule,
            "sdk": sdk,
            "file": f"devices/{device_id}.json",
            "sha256": sha,
        })

    # Sort devices alphabetically by name (case insensitive)
    new_device_entries.sort(key=lambda d: d["name"].lower())

    index_data = {
        "schema": 1,
        "source": {
            "repo": "Gathin23/cleanyourtv",
            "commit": CLEAN_COMMIT,
        },
        "devices": new_device_entries,
        "lists": existing_index.get("lists", {}),
        "logos": existing_index.get("logos", {}),
    }

    INDEX_PATH.write_text(json.dumps(index_data, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
    print(f"Successfully generated {len(new_device_entries)} device files and updated {INDEX_PATH}")


if __name__ == "__main__":
    main()
