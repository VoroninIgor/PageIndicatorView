package com.rd.pageindicatorview.data;

import com.rd.animation.type.AnimationType;
import com.rd.draw.data.Orientation;

public class CustomizationConverter {

    public static AnimationType getAnimationType(int position) {
        switch (position) {
            case 1:
                return AnimationType.COLOR;
            case 2:
                return AnimationType.SCALE;
            case 3:
                return AnimationType.SWAP;
            default:
                return AnimationType.NONE;
        }
    }

    public static Orientation getOrientation(int position) {
        switch (position) {
            case 1:
                return Orientation.VERTICAL;
            default:
                return Orientation.HORIZONTAL;
        }
    }
}
