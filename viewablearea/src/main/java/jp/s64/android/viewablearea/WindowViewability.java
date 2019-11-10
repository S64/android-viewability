package jp.s64.android.viewablearea;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class WindowViewability {

    @NonNull
    private final WindowRect actualWindow;

    @NonNull
    private final DisplaySize displaySize;

    public WindowViewability(
            @NonNull WindowRect actualWindow,
            @NonNull DisplaySize displaySize
    ) {
        this.actualWindow = actualWindow;
        this.displaySize = displaySize;
    }

    public int getViewableLeft() {
        return actualWindow.left < 0 ? 0 : actualWindow.left;
    }

    public int getViewableTop() {
        return actualWindow.top < 0 ? 0 : actualWindow.top;
    }

    public int getViewableRight() {
        int displayWidth = displaySize.getWidthInPixels();
        return actualWindow.right > displayWidth ? displayWidth : actualWindow.right;
    }

    public int getViewableBottom() {
        int displayHeight = displaySize.getHeightInPixels();
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
        int diff = actualWindow.right - displaySize.getWidthInPixels();
        if (diff <= 0) {
            return 0;
        }
        return diff;
    }

    public int getInvisibleWidthBottom() {
        int diff = actualWindow.bottom - displaySize.getHeightInPixels();
        if (diff <= 0) {
            return 0;
        }
        return diff;
    }

    public float getViewability() {
        int actual = actualWindow.getWidthInPixels() * actualWindow.getHeightInPixels();
        int viewable = getViewableWidth() * getViewableHeight();
        return viewable / actual;
    }

}
