package jp.s64.android.viewability.core.rect;

import android.support.annotation.NonNull;

import jp.s64.android.viewability.core.viewability.ContentViewability;

public class RealViewRect {

    @NonNull
    private final ContentViewability contentViewability;

    @NonNull
    private final ViewRect viewInDisplay;

    @NonNull
    private final ViewRect viewInContent;

    public RealViewRect(
            @NonNull ContentViewability contentViewability,
            @NonNull ViewRect viewInDisplay,
            @NonNull ViewRect viewInContent
    ) {
        this.contentViewability = contentViewability;
        this.viewInDisplay = viewInDisplay;
        this.viewInContent = viewInContent;
    }

    public int getViewableLeft() {
        int contentLeft = contentViewability.getViewableLeftInDisplay();
        int contentRight = contentViewability.getViewableRightInDisplay();

        if (viewInDisplay.left < contentLeft) {
            return contentLeft;
        } else if (viewInDisplay.left > contentRight) {
            return contentRight;
        }

        return viewInDisplay.left;
    }

    public int getViewableTop() {
        int contentTop = contentViewability.getViewableTopInDisplay();
        int contentBottom = contentViewability.getViewableBottomInDisplay();

        if (viewInDisplay.top < contentTop) {
            return contentTop;
        } else if (viewInDisplay.top > contentBottom) {
            return contentBottom;
        }

        return viewInDisplay.top;
    }

    public int getViewableRight() {
        int contentLeft = contentViewability.getViewableLeftInDisplay();
        int contentRight = contentViewability.getViewableRightInDisplay();

        if (viewInDisplay.right > contentRight) {
            return contentRight;
        } else if (viewInDisplay.right < contentLeft) {
            return contentLeft;
        }

        return viewInDisplay.right;
    }

    public int getViewableBottom() {
        int contentTop = contentViewability.getViewableTopInDisplay();
        int contentBottom = contentViewability.getViewableBottomInDisplay();

        if (viewInDisplay.bottom > contentBottom) {
            return contentBottom;
        } else if (viewInDisplay.bottom < contentTop) {
            return contentTop;
        }

        return viewInDisplay.bottom;
    }

    public int getViewableWidth() {
        return getViewableRight() - getViewableLeft();
    }

    public int getViewableHeight() {
        return getViewableBottom() - getViewableTop();
    }

    public float getViewability() {
        float actual = viewInDisplay.getWidthInPixels() * viewInDisplay.getHeightInPixels();
        float viewable = getViewableWidth() * getViewableHeight();

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
                .append(getViewability())
                .append(" (")
                .append("left: ")
                .append(getViewableLeft())
                .append(", ")
                .append("top: ")
                .append(getViewableTop())
                .append(", ")
                .append("right: ")
                .append(getViewableRight())
                .append(", ")
                .append("bottom: ")
                .append(getViewableBottom())
                .append(")")
                .toString();
    }

}
