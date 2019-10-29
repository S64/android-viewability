package jp.s64.android.viewablearea;

import android.graphics.Rect;
import android.support.annotation.NonNull;

abstract class BaseRect implements IRect {

    public final int left;
    public final int top;
    public final int right;
    public final int bottom;

    BaseRect(
            @NonNull Rect rect
    ) {
        this(
                rect.left,
                rect.top,
                rect.right,
                rect.bottom
        );
    }

    BaseRect(
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
    public int getLeftInPixels() {
        return left;
    }

    @Override
    public int getTopInPixels() {
        return top;
    }

    @Override
    public int getRightInPixels() {
        return right;
    }

    @Override
    public int getBottomInPixels() {
        return bottom;
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

        BaseRect baseRect = (BaseRect) o;

        if (left != baseRect.left) return false;
        if (top != baseRect.top) return false;
        if (right != baseRect.right) return false;
        return bottom == baseRect.bottom;
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
