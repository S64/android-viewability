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
        int windowGapLeft = contentViewability.getInvisibleWidthLeftInDisplay();
        return windowGapLeft > viewInContent.left ? 0 : viewInDisplay.left;
    }

    public int getViewableTop() {
        int windowGapTop = contentViewability.getInvisibleWidthTopInDisplay();
        return windowGapTop > viewInContent.top ? 0 : viewInDisplay.top;
    }

    public int getViewableRight() {
        int windowRight = contentViewability.getViewableRightInDisplay();
        return viewInDisplay.right > windowRight ? windowRight : viewInDisplay.right;
    }

    public int getViewableBottom() {
        int windowBottom = contentViewability.getViewableBottomInDisplay();
        return viewInDisplay.bottom > windowBottom ? windowBottom : viewInDisplay.bottom;
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
        return viewable / actual;
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
