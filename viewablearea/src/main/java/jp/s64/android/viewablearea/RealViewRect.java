package jp.s64.android.viewablearea;

import android.support.annotation.NonNull;

public class RealViewRect {

    @NonNull
    private final ContentViewability contentViewability;

    @NonNull
    private final ViewRect viewInDisplay;

    @NonNull
    private final ViewRect viewInContent;

    RealViewRect(
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
        return viewInDisplay.left < contentLeft ? contentLeft : viewInDisplay.left;
    }

    public int getViewableTop() {
        int contentTop = contentViewability.getViewableTopInDisplay();
        return viewInDisplay.top < contentTop ? contentTop : viewInDisplay.top;
    }

    public int getViewableRight() {
        int contentRight = contentViewability.getViewableRightInDisplay();
        return viewInDisplay.right > contentRight ? contentRight : viewInDisplay.right;
    }

    public int getViewableBottom() {
        int contentBottom = contentViewability.getViewableBottomInDisplay();
        return viewInDisplay.bottom > contentBottom ? contentBottom : viewInDisplay.bottom;
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
