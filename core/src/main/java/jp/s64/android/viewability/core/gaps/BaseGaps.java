package jp.s64.android.viewability.core.gaps;

import android.support.annotation.NonNull;
import android.support.v4.util.ObjectsCompat;

import jp.s64.android.viewability.core.IGaps;

abstract class BaseGaps implements IGaps {

    private final int leftInPixels;
    private final int topInPixels;
    private final int rightInPixels;
    private final int bottomInPixels;

    protected BaseGaps(
            int leftInPixels,
            int topInPixels,
            int rightInPixels,
            int bottomInPixels
    ) {
        this.leftInPixels = leftInPixels;
        this.topInPixels = topInPixels;
        this.rightInPixels = rightInPixels;
        this.bottomInPixels = bottomInPixels;
    }

    @Override
    public int getLeftInPixels() {
        return leftInPixels;
    }

    @Override
    public int getTopInPixels() {
        return topInPixels;
    }

    @Override
    public int getRightInPixels() {
        return rightInPixels;
    }

    @Override
    public int getBottomInPixels() {
        return bottomInPixels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseGaps baseGaps = (BaseGaps) o;
        return getLeftInPixels() == baseGaps.getLeftInPixels() &&
                getTopInPixels() == baseGaps.getTopInPixels() &&
                getRightInPixels() == baseGaps.getRightInPixels() &&
                getBottomInPixels() == baseGaps.getBottomInPixels();
    }

    @Override
    public int hashCode() {
        return ObjectsCompat.hash(getLeftInPixels(), getTopInPixels(), getRightInPixels(), getBottomInPixels());
    }

    @NonNull
    @Override
    public String toString() {
        return new StringBuilder()
                .append("left: ")
                .append(leftInPixels)
                .append(", ")
                .append("top: ")
                .append(topInPixels)
                .append(", ")
                .append("right: ")
                .append(rightInPixels)
                .append(", ")
                .append("bottom: ")
                .append(bottomInPixels)
                .toString();
    }

}
