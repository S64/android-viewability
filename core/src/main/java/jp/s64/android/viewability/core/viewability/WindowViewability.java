package jp.s64.android.viewability.core.viewability;

import android.support.annotation.NonNull;
import android.support.v4.util.ObjectsCompat;

import java.util.Objects;

import jp.s64.android.viewability.core.dimension.DisplayDimension;
import jp.s64.android.viewability.core.rect.WindowRect;

public class WindowViewability {

    @NonNull
    private final WindowRect actualWindow;

    @NonNull
    private final DisplayDimension displayDimension;

    public WindowViewability(
            @NonNull WindowRect actualWindow,
            @NonNull DisplayDimension displayDimension
    ) {
        this.actualWindow = actualWindow;
        this.displayDimension = displayDimension;
    }

    public int getViewableLeft() {
        return actualWindow.left < 0 ? 0 : actualWindow.left;
    }

    public int getViewableTop() {
        return actualWindow.top < 0 ? 0 : actualWindow.top;
    }

    public int getViewableRight() {
        int displayWidth = displayDimension.getWidthInPixels();
        return actualWindow.right > displayWidth ? displayWidth : actualWindow.right;
    }

    public int getViewableBottom() {
        int displayHeight = displayDimension.getHeightInPixels();
        return actualWindow.bottom > displayHeight ? displayHeight : actualWindow.bottom;
    }

    public int getViewableWidth() {
        return getViewableRight() - getViewableLeft();
    }

    public int getViewableHeight() {
        return getViewableBottom() - getViewableTop();
    }

    public int getInvisibleWidthTop() {
        if (actualWindow.top >= 0) {
            return 0;
        }
        return actualWindow.top * -1;
    }

    public int getInvisibleWidthLeft() {
        if (actualWindow.left >= 0) {
            return 0;
        }
        return actualWindow.left * -1;
    }

    public int getInvisibleWidthRight() {
        int diff = actualWindow.right - displayDimension.getWidthInPixels();
        if (diff <= 0) {
            return 0;
        }
        return diff;
    }

    public int getInvisibleWidthBottom() {
        int diff = actualWindow.bottom - displayDimension.getHeightInPixels();
        if (diff <= 0) {
            return 0;
        }
        return diff;
    }

    public float getViewability() {
        float actual = actualWindow.getWidthInPixels() * actualWindow.getHeightInPixels();
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
                .append(" [")
                .append(getInvisibleWidthLeft())
                .append("], ")
                .append("top: ")
                .append(getViewableTop())
                .append(" [")
                .append(getInvisibleWidthTop())
                .append("], ")
                .append("right: ")
                .append(getViewableRight())
                .append(" [")
                .append(getInvisibleWidthRight())
                .append("], ")
                .append("bottom: ")
                .append(getViewableBottom())
                .append(" [")
                .append(getInvisibleWidthBottom())
                .append("]")
                .append(")")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WindowViewability that = (WindowViewability) o;
        return actualWindow.equals(that.actualWindow) &&
                displayDimension.equals(that.displayDimension);
    }

    @Override
    public int hashCode() {
        return ObjectsCompat.hash(actualWindow, displayDimension);
    }

}
