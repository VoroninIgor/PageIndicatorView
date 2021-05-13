package com.rd.utils;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rd.draw.data.Indicator;

public class CoordinatesUtils {

    @SuppressWarnings("UnnecessaryLocalVariable")
    public static int getCoordinate(@Nullable Indicator indicator, int position) {
        if (indicator == null) {
            return 0;
        }

        return getXCoordinate(indicator, position);
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    public static int getXCoordinate(@Nullable Indicator indicator, int position) {
        if (indicator == null) {
            return 0;
        }

        int coordinate = getHorizontalCoordinate(indicator, position);
        coordinate += indicator.getPaddingLeft();
        return coordinate;
    }

    public static int getYCoordinate(@Nullable Indicator indicator, int position) {
        if (indicator == null) {
            return 0;
        }

        int coordinate = getVerticalCoordinate(indicator);
        coordinate += indicator.getPaddingTop();
        return coordinate;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public static int getPosition(@Nullable Indicator indicator, float x, float y) {
        if (indicator == null) {
            return -1;
        }

        return getFitPosition(indicator, x, y);
    }

    private static int getFitPosition(@NonNull Indicator indicator, float lengthCoordinate, float heightCoordinate) {
        int count = indicator.getCount();
        int radius = indicator.getRadius();

        int padding = indicator.getPadding();

        int height = indicator.getHeight();
        int length = 0;

        for (int i = 0; i < count; i++) {
            int indicatorPadding = i > 0 ? padding : padding / 2;
            int startValue = length;

            length += radius * 2 + indicatorPadding;
            int endValue = length;

            boolean fitLength = lengthCoordinate >= startValue && lengthCoordinate <= endValue;
            boolean fitHeight = heightCoordinate >= 0 && heightCoordinate <= height;

            if (fitLength && fitHeight) {
                return i;
            }
        }

        return -1;
    }

    private static int getHorizontalCoordinate(@NonNull Indicator indicator, int position) {
        int count = indicator.getCount();
        int radius = indicator.getRadius();
        int padding = indicator.getPadding();

        int coordinate = 0;
        for (int i = 0; i < count; i++) {
            coordinate += radius;

            if (position == i) {
                return coordinate;
            }

            coordinate += radius + padding;
        }

        return coordinate;
    }

    private static int getVerticalCoordinate(@NonNull Indicator indicator) {
        return indicator.getRadius();
    }

    public static Pair<Integer, Float> getProgress(@NonNull Indicator indicator, int position, float positionOffset) {
        int count = indicator.getCount();
        int selectedPosition = indicator.getSelectedPosition();

        if (position < 0) {
            position = 0;

        } else if (position > count - 1) {
            position = count - 1;
        }

        boolean isRightOverScrolled = position > selectedPosition;
        boolean isLeftOverScrolled;


        isLeftOverScrolled = position + 1 < selectedPosition;


        if (isRightOverScrolled || isLeftOverScrolled) {
            selectedPosition = position;
            indicator.setSelectedPosition(selectedPosition);
        }

        boolean slideToRightSide = selectedPosition == position && positionOffset != 0;
        int selectingPosition;
        float selectingProgress;

        if (slideToRightSide) {
            selectingPosition = position + 1;
            selectingProgress = positionOffset;

        } else {
            selectingPosition = position;
            selectingProgress = 1 - positionOffset;
        }

        if (selectingProgress > 1) {
            selectingProgress = 1;

        } else if (selectingProgress < 0) {
            selectingProgress = 0;
        }

        return new Pair<>(selectingPosition, selectingProgress);
    }
}
