package jp.s64.android.viewablearea;

import android.support.annotation.NonNull;

public class SystemGaps {

    private final int leftInPixels;
    private final int topInPixels;
    private final int rightInPixels;
    private final int bottomInPixels;

    public SystemGaps(
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

    public int getLeftInPixels() {
        return leftInPixels;
    }

    public int getTopInPixels() {
        return topInPixels;
    }

    public int getRightInPixels() {
        return rightInPixels;
    }

    public int getBottomInPixels() {
        return bottomInPixels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemGaps that = (SystemGaps) o;

        if (getLeftInPixels() != that.getLeftInPixels()) return false;
        if (getTopInPixels() != that.getTopInPixels()) return false;
        if (getRightInPixels() != that.getRightInPixels()) return false;
        return getBottomInPixels() == that.getBottomInPixels();
    }

    @Override
    public int hashCode() {
        int result = getLeftInPixels();
        result = 31 * result + getTopInPixels();
        result = 31 * result + getRightInPixels();
        result = 31 * result + getBottomInPixels();
        return result;
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
