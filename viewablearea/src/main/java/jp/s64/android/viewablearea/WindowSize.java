package jp.s64.android.viewablearea;

import android.graphics.Rect;
import android.support.annotation.NonNull;

public class WindowSize implements Size {

    public final int left;
    public final int top;
    public final int right;
    public final int bottom;

    public WindowSize(
            @NonNull Rect rect
    ) {
        this(
                rect.left,
                rect.top,
                rect.right,
                rect.bottom
        );
    }

    public WindowSize(
            int leftInPixels,
            int topInPixels,
            int rightInPixels,
            int bottomInPixels
    ) {
        this.left = leftInPixels;
        this.top = topInPixels;
        this.right = rightInPixels;
        this.bottom = bottomInPixels;
    }

    @Override
    public int getWidthInPixels() {
        return right - left;
    }

    @Override
    public int getHeightInPixels() {
        return bottom - top;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WindowSize that = (WindowSize) o;

        if (left != that.left) return false;
        if (top != that.top) return false;
        if (right != that.right) return false;
        return bottom == that.bottom;
    }

    @Override
    public int hashCode() {
        int result = left;
        result = 31 * result + top;
        result = 31 * result + right;
        result = 31 * result + bottom;
        return result;
    }

}
