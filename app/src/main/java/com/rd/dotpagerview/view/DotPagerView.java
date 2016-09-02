package com.rd.dotpagerview.view;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.*;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.rd.dotpagerview.view.animation.*;
import com.rd.dotpagerview.utils.DensityUtils;

public class DotPagerView extends View {

    private static final String DEFAULT_UNSELECTED_COLOR = "#33ffffff";
    private static final String DEFAULT_SELECTED_COLOR = "#ffffff";

    private static final int DEFAULT_RADIUS_DP = 8;
    private static final int DEFAULT_PADDING_DP = 16;

    private int radiusPx = DensityUtils.dpToPx(getContext(), DEFAULT_RADIUS_DP);
    private int paddingPx = DensityUtils.dpToPx(getContext(), DEFAULT_PADDING_DP);
    private int count;

    //Color
    private int unselectedColor = Color.parseColor(DEFAULT_UNSELECTED_COLOR);
    private int selectedColor = Color.parseColor(DEFAULT_SELECTED_COLOR);

    private int frameColor;
    private int frameColorReverse;

    //Scale
    private int frameRadiusPx;
    private int frameRadiusReversePx;

    //Slide
    private int frameLeftX;
    private int frameRightX;

    private int selectedPosition;
    private int selectingPosition;
    private int lastSelectedPosition;

    private boolean isInteractiveAnimation;

    private Paint paint;
    private AnimationType animationType = AnimationType.NONE;
    private ValueAnimation animation;

    public DotPagerView(Context context) {
        super(context);
        init();
    }

    public DotPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DotPagerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DotPagerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int dotDiameterPx = radiusPx * 2;
        int desiredHeight = dotDiameterPx;
        int desiredWidth = 0;

        if (count != 0) {
            desiredWidth = (dotDiameterPx * count) + (paddingPx * (count - 1));
        }

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawDotView(canvas);
    }

    public void setCount(int count) {
        this.count = count;
        invalidate();
    }

    public void setRadius(int radiusDp) {
        radiusPx = DensityUtils.dpToPx(getContext(), radiusDp);
        invalidate();
    }

    public void setPadding(int paddingDp) {
        paddingPx = DensityUtils.dpToPx(getContext(), paddingDp);
        invalidate();
    }

    public void setUnselectedColor(int color) {
        unselectedColor = color;
        invalidate();
    }

    public void setSelectedColor(int color) {
        selectedColor = color;
        invalidate();
    }

    public void setAnimationType(@Nullable AnimationType type) {
        if (type != null) {
            animationType = type;
        } else {
            animationType = AnimationType.NONE;
        }
    }

    public void setSelection(int position) {
        if (isInteractiveAnimation) {
            return;
        }

        lastSelectedPosition = selectedPosition;
        selectedPosition = position;

        switch (animationType) {
            case COLOR:
                startColorAnimation();
                break;

            case SCALE:
                startScaleAnimation();
                break;

            case COLOR_AND_SCALE:
                startColorAndScaleAnimation();
                break;

            case SLIDE:
                startSlideAnimation();
                break;
        }
    }

    public void setInteractiveAnimation(boolean isInteractive) {
        isInteractiveAnimation = isInteractive;
    }

    public void setProgress(int position, float offset) {
        if (!isInteractiveAnimation) {
            return;
        }

        Log.e("TEST", "POSITION: " + position + " OFFSET: " + offset);
        if (position == selectingPosition) {
            lastSelectedPosition = selectedPosition;
            selectedPosition = position;
        }

        boolean isRightSlide = selectedPosition == position;
        float progress;

        if (isRightSlide) {
            progress = offset;
            selectingPosition = position + 1;
        } else {
            progress = 1 - offset;
            selectingPosition = position;
        }

        switch (animationType) {
            case COLOR:
                animation.color().with(selectedColor, unselectedColor).progress(progress);
                break;

            case SCALE:
                animation.scale().with(radiusPx).progress(progress);
                break;
        }
    }

    public void setViewPager(@Nullable ViewPager viewPager) {
        if (viewPager == null) {
            return;
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setProgress(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void drawDotView(@NonNull Canvas canvas) {
        int y = getHeight() / 2;

        for (int i = 0; i < count; i++) {
            int x = getXCoordinate(i);
            drawDot(canvas, i, x, y);
        }
    }

    private void drawDot(@NonNull Canvas canvas, int position, int x, int y) {
        boolean isAnimatedItem = position == selectingPosition || position == selectedPosition || position == lastSelectedPosition;
        if (isAnimatedItem) {
            drawWithAnimationEffect(canvas, position, x, y);
        } else {
            drawWithNoEffect(canvas, x, y);
        }
    }

    private void drawWithAnimationEffect(@NonNull Canvas canvas, int position, int x, int y) {
        switch (animationType) {
            case COLOR:
                drawWithColorAnimation(canvas, position, x, y);
                break;

            case SCALE:
            case COLOR_AND_SCALE:
                drawWithScaleAnimation(canvas, position, x, y);
                break;

            case SLIDE:
                drawWithSlideAnimation(canvas, position, x, y);
                break;

            case NONE:
                drawWithNoEffect(canvas, x, y);
                break;
        }
    }

    private void drawWithColorAnimation(@NonNull Canvas canvas, int position, int x, int y) {
        int color = unselectedColor;

        if (isInteractiveAnimation) {
            if (position == selectingPosition) {
                color = frameColor;
            } else if (position == selectedPosition) {
                color = frameColorReverse;
            }

        } else {
            if (position == selectedPosition) {
                color = frameColor;
            } else if (position == lastSelectedPosition) {
                color = frameColorReverse;
            }
        }

        paint.setColor(color);
        canvas.drawCircle(x, y, radiusPx, paint);
    }

    private void drawWithScaleAnimation(@NonNull Canvas canvas, int position, int x, int y) {
        int color = selectedColor;
        int radius = radiusPx;

        if (isInteractiveAnimation) {
            if (position == selectingPosition) {
                radius = frameRadiusPx;
            } else if (position == selectedPosition) {
                radius = frameRadiusReversePx;
            }

        } else {
            if (position == selectedPosition) {
                radius = frameRadiusPx;
            } else if (position == lastSelectedPosition) {
                radius = frameRadiusReversePx;
            }
        }

        paint.setColor(color);
        canvas.drawCircle(x, y, radius, paint);
    }

    private void drawWithSlideAnimation(@NonNull Canvas canvas, int position, int x, int y) {
        int radius = radiusPx;
        RectF rect = null;

        if (position == selectedPosition) {
            int left = frameLeftX;
            int right = frameRightX;
            int top = y - radius;
            int bot = y + radius;

            rect = new RectF(left, top, right, bot);
        }

        paint.setColor(unselectedColor);
        canvas.drawCircle(x, y, radius, paint);

        if (rect != null) {
            paint.setColor(selectedColor);
            canvas.drawRoundRect(rect, radiusPx, radiusPx, paint);
        }
    }

    private void drawWithNoEffect(@NonNull Canvas canvas, int x, int y) {
        boolean isScaleAnimation = animationType == AnimationType.SCALE || animationType == AnimationType.COLOR_AND_SCALE;
        int radius = radiusPx;

        if (isScaleAnimation) {
            radius /= ScaleAnimation.SCALE_FACTOR;
        }

        paint.setColor(unselectedColor);
        canvas.drawCircle(x, y, radius, paint);
    }

    private void init() {
        initDefaultValues();
        initAnimation();
        initPaint();
    }

    private void initDefaultValues() {
        frameColor = selectedColor;
        frameColorReverse = unselectedColor;

        frameRadiusPx = radiusPx;
        frameRadiusReversePx = radiusPx;

        int xCoordinate = getXCoordinate(selectedPosition);
        if (xCoordinate - radiusPx >= 0) {
            frameLeftX = xCoordinate - radiusPx;
            frameRightX = xCoordinate + radiusPx;

        } else {
            frameLeftX = xCoordinate;
            frameRightX = xCoordinate + (radiusPx * 2);
        }
    }

    private void initAnimation() {
        animation = new ValueAnimation(new ValueAnimation.UpdateListener() {
            @Override
            public void onColorAnimationUpdated(int color, int colorReverse) {
                frameColor = color;
                frameColorReverse = colorReverse;
                invalidate();
            }

            @Override
            public void onScaleAnimationUpdated(int radiusPx, int radiusReverse) {
                frameRadiusPx = radiusPx;
                frameRadiusReversePx = radiusReverse;
                invalidate();
            }
        });
    }

    private void initPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    private void startColorAnimation() {
        animation.color().with(selectedColor, unselectedColor).start();
    }

    private void startScaleAnimation() {
        animation.scale().with(radiusPx).start();
    }

    private void startColorAndScaleAnimation() {
        ColorAndScaleAnimation.start(selectedColor, unselectedColor, radiusPx, new ColorAndScaleAnimation.Listener() {
            @Override
            public void onScaleAnimationUpdated(int color, int colorReverse, int radius, int radiusReverse) {
                frameColor = color;
                frameColorReverse = colorReverse;

                frameRadiusPx = radius;
                frameRadiusReversePx = radiusReverse;

                invalidate();
            }
        });
    }

    private void startSlideAnimation() {
        int fromX;
        int toX;

        int reverseFromX;
        int reverseToX;

        int xSelected = getXCoordinate(selectedPosition);
        int xLastSelected = getXCoordinate(lastSelectedPosition);
        boolean isRightSide = selectedPosition > lastSelectedPosition;

        if (selectedPosition > lastSelectedPosition) {
            fromX = xLastSelected + radiusPx;
            toX = xSelected + radiusPx;

            reverseFromX = xLastSelected - radiusPx;
            reverseToX = xSelected - radiusPx;

        } else if (selectedPosition < lastSelectedPosition) {
            fromX = xLastSelected - radiusPx;
            toX = xSelected - radiusPx;

            reverseFromX = xLastSelected + radiusPx;
            reverseToX = xSelected + radiusPx;

        } else {
            return;
        }

        SlideAnimation.end();
        SlideAnimation.start(fromX, toX, reverseFromX, reverseToX, isRightSide, new SlideAnimation.Listener() {
            @Override
            public void onSlideAnimationUpdated(int leftX, int rightX) {
                frameLeftX = leftX;
                frameRightX = rightX;
                invalidate();
            }
        });
    }

    private int getXCoordinate(int position) {
        int actualViewWidth = calculateActualViewWidth();
        int x = (getWidth() - actualViewWidth) / 2;

        for (int i = 0; i < count; i++) {
            x += radiusPx;
            if (position == i) {
                return x;
            }

            x += radiusPx + paddingPx;
        }

        return x;
    }

    private int calculateActualViewWidth() {
        int width = 0;
        int diameter = radiusPx * 2;

        for (int i = 0; i < count; i++) {
            width += diameter;

            if (i < count - 1) {
                width += paddingPx;
            }
        }

        return width;
    }

}