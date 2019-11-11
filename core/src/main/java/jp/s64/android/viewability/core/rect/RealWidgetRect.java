package jp.s64.android.viewability.core.rect;

import android.support.annotation.NonNull;

import jp.s64.android.viewability.core.viewability.ContentViewability;

public class RealWidgetRect {

    @NonNull
    private final ContentViewability contentViewability;

    @NonNull
    private final WidgetRectInDisplay widgetInDisplay;

    @NonNull
    private final WidgetRectInContent widgetInContent;

    public RealWidgetRect(
            @NonNull ContentViewability contentViewability,
            @NonNull WidgetRectInDisplay widgetInDisplay,
            @NonNull WidgetRectInContent widgetInContent
    ) {
        this.contentViewability = contentViewability;
        this.widgetInDisplay = widgetInDisplay;
        this.widgetInContent = widgetInContent;
    }

    public int getViewableLeft() {
        int contentLeft = contentViewability.getViewableLeftInDisplay();
        int contentRight = contentViewability.getViewableRightInDisplay();

        if (widgetInDisplay.left < contentLeft) {
            return contentLeft;
        } else if (widgetInDisplay.left > contentRight) {
            return contentRight;
        }

        return widgetInDisplay.left;
    }

    public int getViewableTop() {
        int contentTop = contentViewability.getViewableTopInDisplay();
        int contentBottom = contentViewability.getViewableBottomInDisplay();

        if (widgetInDisplay.top < contentTop) {
            return contentTop;
        } else if (widgetInDisplay.top > contentBottom) {
            return contentBottom;
        }

        return widgetInDisplay.top;
    }

    public int getViewableRight() {
        int contentLeft = contentViewability.getViewableLeftInDisplay();
        int contentRight = contentViewability.getViewableRightInDisplay();

        if (widgetInDisplay.right > contentRight) {
            return contentRight;
        } else if (widgetInDisplay.right < contentLeft) {
            return contentLeft;
        }

        return widgetInDisplay.right;
    }

    public int getViewableBottom() {
        int contentTop = contentViewability.getViewableTopInDisplay();
        int contentBottom = contentViewability.getViewableBottomInDisplay();

        if (widgetInDisplay.bottom > contentBottom) {
            return contentBottom;
        } else if (widgetInDisplay.bottom < contentTop) {
            return contentTop;
        }

        return widgetInDisplay.bottom;
    }

    public int getViewableWidth() {
        return getViewableRight() - getViewableLeft();
    }

    public int getViewableHeight() {
        return getViewableBottom() - getViewableTop();
    }

    public float getViewability() {
        float actual = widgetInDisplay.getWidthInPixels() * widgetInDisplay.getHeightInPixels();
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
