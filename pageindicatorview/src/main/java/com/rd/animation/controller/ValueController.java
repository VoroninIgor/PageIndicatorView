package com.rd.animation.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.rd.animation.data.Value;
import com.rd.animation.type.*;

public class ValueController {

    private ColorAnimation colorAnimation;
    private SwapAnimation swapAnimation;

    private UpdateListener updateListener;

    public interface UpdateListener {
        void onValueUpdated(@Nullable Value value);
    }

    public ValueController(@Nullable UpdateListener listener) {
        updateListener = listener;
    }

    @NonNull
    public ColorAnimation color() {
        if (colorAnimation == null) {
            colorAnimation = new ColorAnimation(updateListener);
        }

        return colorAnimation;
    }

    @NonNull
    public SwapAnimation swap() {
        if (swapAnimation == null) {
            swapAnimation = new SwapAnimation(updateListener);
        }

        return swapAnimation;
    }
}
