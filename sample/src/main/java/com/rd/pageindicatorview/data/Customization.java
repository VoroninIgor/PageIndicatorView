package com.rd.pageindicatorview.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.rd.animation.type.AnimationType;

public class Customization implements Parcelable {

    private AnimationType animationType = AnimationType.NONE;

    private boolean interactiveAnimation = false;
    private boolean autoVisibility = true;
    private boolean fadeOnIdle = false;

    public AnimationType getAnimationType() {
        return animationType;
    }

    public void setAnimationType(AnimationType animationType) {
        this.animationType = animationType;
    }

    public boolean isInteractiveAnimation() {
        return interactiveAnimation;
    }

    public void setInteractiveAnimation(boolean interactiveAnimation) {
        this.interactiveAnimation = interactiveAnimation;
    }

    public boolean isAutoVisibility() {
        return autoVisibility;
    }

    public void setAutoVisibility(boolean autoVisibility) {
        this.autoVisibility = autoVisibility;
    }

    public boolean isFadeOnIdle() {
        return fadeOnIdle;
    }

    public void setFadeOnIdle(boolean fadeOnIdle) {
        this.fadeOnIdle = fadeOnIdle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customization that = (Customization) o;

        if (interactiveAnimation != that.interactiveAnimation) return false;
        if (autoVisibility != that.autoVisibility) return false;
        if (animationType != that.animationType) return false;
        if (fadeOnIdle != that.fadeOnIdle) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = animationType != null ? animationType.hashCode() : 0;
        result = 31 * result + (interactiveAnimation ? 1 : 0);
        result = 31 * result + (autoVisibility ? 1 : 0);
        result = 31 * result + (fadeOnIdle ? 1 : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.animationType == null ? -1 : this.animationType.ordinal());
        dest.writeByte(this.interactiveAnimation ? (byte) 1 : (byte) 0);
        dest.writeByte(this.autoVisibility ? (byte) 1 : (byte) 0);
        dest.writeByte(this.fadeOnIdle ? (byte) 1 : (byte) 0);
    }

    public Customization() {
    }

    protected Customization(Parcel in) {
        int tmpAnimationType = in.readInt();
        this.animationType = tmpAnimationType == -1 ? null : AnimationType.values()[tmpAnimationType];
        this.interactiveAnimation = in.readByte() != 0;
        this.autoVisibility = in.readByte() != 0;
        this.fadeOnIdle = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Customization> CREATOR = new Parcelable.Creator<Customization>() {
        @Override
        public Customization createFromParcel(Parcel source) {
            return new Customization(source);
        }

        @Override
        public Customization[] newArray(int size) {
            return new Customization[size];
        }
    };
}
