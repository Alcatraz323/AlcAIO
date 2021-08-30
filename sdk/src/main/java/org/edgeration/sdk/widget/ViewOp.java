package org.edgeration.sdk.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

public class ViewOp {
    public static void setImageWithTint(ImageView imageView, int resId, int color) {
        imageView.setImageDrawable(getTintedDrawable(imageView.getContext(), resId, color));
    }

    public static void setImageWithTint(ImageButton imageButton, int resId, int color) {
        imageButton.setImageDrawable(getTintedDrawable(imageButton.getContext(), resId, color));
    }

    public static void reTintImage(ImageView imageView, int color) {
        imageView.post(new Runnable() {
            @Override
            public void run() {
                Drawable drawable = imageView.getDrawable();
                Drawable compat = DrawableCompat.wrap(drawable);
                if (compat != null) {
                    compat.setBounds(0, 0, compat.getMinimumWidth(), compat.getMinimumHeight());
                    DrawableCompat.setTint(compat, color);
                    imageView.setImageDrawable(compat);
                }
            }
        });
    }


    public static Drawable getTintedDrawable(Context context, int resId, int color) {
        Drawable up = ContextCompat.getDrawable(context, resId);
        Drawable drawableUp = DrawableCompat.wrap(up);
        drawableUp.setBounds(0, 0, drawableUp.getMinimumWidth(), drawableUp.getMinimumHeight());
        DrawableCompat.setTint(drawableUp, color);
        return drawableUp;
    }

    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
