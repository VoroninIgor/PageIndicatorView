package com.rd.draw.drawer.type;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.rd.animation.data.Value;
import com.rd.animation.data.type.SwapAnimationValue;
import com.rd.draw.data.Indicator;
import com.rd.pageindicatorview.R;

public class SwapDrawer extends BaseDrawer {

    private final Context context;

    public SwapDrawer(Context context, @NonNull Paint paint, @NonNull Indicator indicator) {
        super(paint, indicator);
        this.context = context;
    }

    public void draw(
            @NonNull Canvas canvas,
            @NonNull Value value,
            int position,
            int coordinateX,
            int coordinateY) {

        if (!(value instanceof SwapAnimationValue)) {
            return;
        }

        SwapAnimationValue v = (SwapAnimationValue) value;

        int selectedPosition = indicator.getSelectedPosition();
        int selectingPosition = indicator.getSelectingPosition();
        int lastSelectedPosition = indicator.getLastSelectedPosition();

        int coordinate = v.getCoordinate();

        if (indicator.isInteractiveAnimation()) {
            if (position == selectingPosition) {
                coordinate = v.getCoordinate();
            } else if (position == selectedPosition) {
                coordinate = v.getCoordinateReverse();
            }

        } else {
            if (position == lastSelectedPosition) {
                coordinate = v.getCoordinate();
            } else if (position == selectedPosition) {
                coordinate = v.getCoordinateReverse();
            }
        }

//        paint.setColor(color);
//        if (indicator.getOrientation() == Orientation.HORIZONTAL) {
//            canvas.drawCircle(coordinate, coordinateY, radius, paint);
//        } else {
//            canvas.drawCircle(coordinateX, coordinate, radius, paint);
//        }


        boolean isPositionSelected = position == lastSelectedPosition;

        Drawable drawable = ContextCompat.getDrawable(
                context,
                isPositionSelected ? R.drawable.ic_selected : R.drawable.ic_unselected
        );

        if (drawable != null) {
            drawable.setBounds(
                    coordinate,
                    coordinateY,
                    coordinate + drawable.getIntrinsicWidth(),
                    coordinateY + drawable.getIntrinsicHeight()
            );
            drawable.draw(canvas);
        }

    }
}
