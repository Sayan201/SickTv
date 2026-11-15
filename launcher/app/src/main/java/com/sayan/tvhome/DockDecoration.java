package com.sayan.tvhome;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Draws a frosted liquid-glass tray behind the dock row (row 1) and provides breathing room around it.
 */
public class DockDecoration extends RecyclerView.ItemDecoration {

    private final int dockSize;
    private final float density;
    private final float radius;
    private final Paint shadow = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint backplate = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint specular = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint rim = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF bounds = new RectF();

    DockDecoration(int dockSize, float density) {
        this.dockSize = dockSize;
        this.density = density;
        this.radius = 22f * density;

        shadow.setStyle(Paint.Style.FILL);

        rim.setStyle(Paint.Style.STROKE);
        rim.setStrokeWidth(1.0f * density);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(view);
        if (pos >= 0 && pos < dockSize) {
            outRect.top = (int) (6 * density);
            outRect.bottom = (int) (12 * density);
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        float minLeft = Float.MAX_VALUE;
        float maxRight = -Float.MAX_VALUE;
        float minTop = Float.MAX_VALUE;
        float maxBottom = -Float.MAX_VALUE;
        boolean found = false;

        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            int pos = parent.getChildAdapterPosition(child);
            if (pos >= 0 && pos < dockSize) {
                View card = child.findViewById(R.id.card);
                View target = card != null ? card : child;

                float left = child.getLeft() + target.getLeft() + target.getTranslationX();
                float right = child.getLeft() + target.getRight() + target.getTranslationX();
                float top = child.getTop() + target.getTop() + target.getTranslationY();
                float bottom = child.getTop() + target.getBottom() + target.getTranslationY();

                if (left < minLeft) minLeft = left;
                if (right > maxRight) maxRight = right;
                if (top < minTop) minTop = top;
                if (bottom > maxBottom) maxBottom = bottom;
                found = true;
            }
        }

        if (!found) return;

        // Insets around the dock cards to form the macOS/Zangetsu-like capsule dock
        float padH = 16f * density;
        float padV = 10f * density;
        bounds.set(minLeft - padH, minTop - padV, maxRight + padH, maxBottom + padV);
        float minAllowed = 6f * density;
        float maxAllowed = parent.getWidth() - 6f * density;
        if (bounds.left < minAllowed) bounds.left = minAllowed;
        if (bounds.right > maxAllowed) bounds.right = maxAllowed;
        float capsuleRadius = Math.min(bounds.height() / 2f, 26f * density);

        // 1. Soft multi-layered ambient drop shadow underneath (elevates and grounds the liquid glass tray)
        for (int step = 3; step >= 1; step--) {
            shadow.setColor(step == 1 ? 0x38000000 : (step == 2 ? 0x22000000 : 0x0E000000));
            float spread = step * 2.5f * density;
            float offsetY = (step * 2f + 1f) * density;
            c.drawRoundRect(bounds.left - spread * 0.5f, bounds.top + offsetY - spread * 0.2f,
                    bounds.right + spread * 0.5f, bounds.bottom + offsetY + spread,
                    capsuleRadius + spread * 0.5f, capsuleRadius + spread * 0.5f, shadow);
        }

        // 2. Translucent obsidian backplate for physical glass refraction & dark depth (Zangetsu smoked glass)
        backplate.setShader(new LinearGradient(0, bounds.top, 0, bounds.bottom,
                0x520A0E18, 0x3805070E, Shader.TileMode.CLAMP));
        c.drawRoundRect(bounds, capsuleRadius, capsuleRadius, backplate);

        // 3. Translucent liquid glass gradient fill (frosted white dispersion)
        fill.setShader(new LinearGradient(0, bounds.top, 0, bounds.bottom,
                0x28FFFFFF, 0x0EFFFFFF, Shader.TileMode.CLAMP));
        c.drawRoundRect(bounds, capsuleRadius, capsuleRadius, fill);

        // 4. Specular sheen across top 35% (Fresnel light reflection)
        specular.setShader(new LinearGradient(0, bounds.top, 0, bounds.top + bounds.height() * 0.35f,
                0x48FFFFFF, 0x00FFFFFF, Shader.TileMode.CLAMP));
        c.save();
        c.clipRect(bounds.left, bounds.top, bounds.right, bounds.top + bounds.height() * 0.35f);
        c.drawRoundRect(bounds, capsuleRadius, capsuleRadius, specular);
        c.restore();

        // 5. Crisp luminous glass rim stroke with vertical light falloff
        rim.setShader(new LinearGradient(0, bounds.top, 0, bounds.bottom,
                0x59FFFFFF, 0x24FFFFFF, Shader.TileMode.CLAMP));
        c.drawRoundRect(bounds, capsuleRadius, capsuleRadius, rim);
    }
}
