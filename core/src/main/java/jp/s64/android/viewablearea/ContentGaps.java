package jp.s64.android.viewablearea;

import android.support.annotation.NonNull;

public class ContentGaps {

    private final int leftInPixels;
    private final int topInPixels;
    private final int rightInPixels;
    private final int bottomInPixels;

    public ContentGaps(
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
