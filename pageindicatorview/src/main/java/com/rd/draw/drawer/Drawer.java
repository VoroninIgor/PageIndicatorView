package com.rd.draw.drawer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.rd.animation.data.Value;
import com.rd.draw.data.Indicator;
import com.rd.draw.drawer.type.BasicDrawer;
import com.rd.draw.drawer.type.ColorDrawer;
import com.rd.draw.drawer.type.SwapDrawer;

public class Drawer {

    private final BasicDrawer basicDrawer;
    private final ColorDrawer colorDrawer;
    private final SwapDrawer swapDrawer;
    private final Context context;
    private int position;
    private int coordinateX;
    private int coordinateY;

    public Drawer(Context context, @NonNull Indicator indicator) {
        this.context = context;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        basicDrawer = new BasicDrawer(context, paint, indicator);
        colorDrawer = new ColorDrawer(paint, indicator);
        swapDrawer = new SwapDrawer(context, paint, indicator);
    }

    public void setup(int position, int coordinateX, int coordinateY) {
        this.position = position;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public void drawBasic(@NonNull Canvas canvas) {
        if (colorDrawer != null) {
            basicDrawer.draw(context, canvas, position, coordinateX, coordinateY);
        }
    }

    public void drawSwap(@NonNull Canvas canvas, @NonNull Value value) {
        if (swapDrawer != null) {
            swapDrawer.draw(canvas, value, position, coordinateX, coordinateY);
        }
    }
}
