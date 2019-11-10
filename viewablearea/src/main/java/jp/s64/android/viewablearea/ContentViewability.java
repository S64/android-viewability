package jp.s64.android.viewablearea;

import android.support.annotation.NonNull;

public class ContentViewability {

    @NonNull
    private final ContentSize contentInDisplay;

    @NonNull
    private final DisplaySize displaySize;

    public ContentViewability(
            @NonNull ContentSize contentInDisplay,
            @NonNull DisplaySize displaySize
    ) {
        this.contentInDisplay = contentInDisplay;
        this.displaySize = displaySize;
    }

    public int getViewableLeftInDisplay() {
        return contentInDisplay.left < 0 ? 0 : contentInDisplay.left;
    }

    public int getViewableTopInDisplay() {
        return contentInDisplay.top < 0 ? 0 : contentInDisplay.top;
    }

    public int getViewableRightInDisplay() {
        int displayWidth = displaySize.getWidthInPixels();
        return contentInDisplay.right > displayWidth ? displayWidth : contentInDisplay.right;
    }

    public int getViewableBottomInDisplay() {
        int displayHeight = displaySize.getHeightInPixels();
        return contentInDisplay.bottom > displayHeight ? displayHeight : contentInDisplay.bottom;
    }

    public int getViewableWidthInDisplay() {
        return getViewableRightInDisplay() - getViewableLeftInDisplay();
    }

    public int getViewableHeightInDisplay() {
        return getViewableBottomInDisplay() - getViewableTopInDisplay();
    }

    public int getInvisibleWidthTopInDisplay() {
        if (contentInDisplay.top >= 0) {
            return 0;
        }
        return contentInDisplay.top * -1;
    }

    public int getInvisibleWidthLeftInDisplay() {
        if (contentInDisplay.left >= 0) {
            return 0;
        }
        return contentInDisplay.left * -1;
    }

    public int getInvisibleWidthRightInDisplay() {
        int diff = contentInDisplay.right - displaySize.getWidthInPixels();
        if (diff <= 0) {
            return 0;
        }
        return diff;
    }

    public int getInvisibleWidthBottomInDisplay() {
        int diff = contentInDisplay.bottom - displaySize.getHeightInPixels();
        if (diff <= 0) {
            return 0;
        }
        return diff;
    }

    public float getViewabilityInDisplay() {
        int actual = contentInDisplay.getWidthInPixels() * contentInDisplay.getHeightInPixels();
        int viewable = getViewableWidthInDisplay() * getViewableHeightInDisplay();
        return viewable / actual;
    }

}
