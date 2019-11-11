package jp.s64.android.viewability.core.viewability;

import android.support.annotation.NonNull;

import jp.s64.android.viewability.core.dimension.DisplayDimension;
import jp.s64.android.viewability.core.rect.ContentRect;

public class ContentViewability {

    @NonNull
    private final ContentRect contentInDisplay;

    @NonNull
    private final DisplayDimension displaySize;

    public ContentViewability(
            @NonNull ContentRect contentInDisplay,
            @NonNull DisplayDimension displaySize
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
        float actual = contentInDisplay.getWidthInPixels() * contentInDisplay.getHeightInPixels();
        float viewable = getViewableWidthInDisplay() * getViewableHeightInDisplay();

        float rate = viewable / actual;
        if (rate < 0) {
            return 0;
        } else {
            return rate;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return new StringBuilder()
                .append(getViewabilityInDisplay())
                .append(" (")
                .append("left: ")
                .append(getViewableLeftInDisplay())
                .append(" [")
                .append(getInvisibleWidthLeftInDisplay())
                .append("],")
                .append("top: ")
                .append(getViewableTopInDisplay())
                .append(" [")
                .append(getInvisibleWidthTopInDisplay())
                .append("],")
                .append("right: ")
                .append(getViewableRightInDisplay())
                .append(" [")
                .append(getInvisibleWidthRightInDisplay())
                .append("],")
                .append("bottom: ")
                .append(getViewableBottomInDisplay())
                .append(" [")
                .append(getInvisibleWidthBottomInDisplay())
                .append("]")
                .append(")")
                .toString();
    }

}
