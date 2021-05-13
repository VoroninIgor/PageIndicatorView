package com.rd.draw.drawer.type;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.rd.animation.type.AnimationType;
import com.rd.draw.data.Indicator;
import com.rd.pageindicatorview.R;

public class BasicDrawer extends BaseDrawer {

    public BasicDrawer(Context context, @NonNull Paint paint, @NonNull Indicator indicator) {
        super(paint, indicator);

    }

    public void draw(
            Context context,
            @NonNull Canvas canvas,
            int position,
            int coordinateX,
            int coordinateY) {

        int selectedPosition = indicator.getSelectedPosition();
        AnimationType animationType = indicator.getAnimationType();
        boolean isPositionSelected = position == selectedPosition;

        Drawable drawable = ContextCompat.getDrawable(
                context,
                isPositionSelected ? R.drawable.ic_selected : R.drawable.ic_unselected
        );

        if (drawable != null) {
            drawable.setBounds(
                    coordinateX,
                    coordinateY,
                    coordinateX + drawable.getIntrinsicWidth(),
                    coordinateY + drawable.getIntrinsicHeight()
            );
            drawable.draw(canvas);
        }

//        canvas.drawCircle(coordinateX, coordinateY, radius, paint);
    }
}
