package jp.s64.android.viewablearea;

import android.graphics.Rect;
import android.support.annotation.NonNull;

public class WindowRect extends BaseRect {

    public WindowRect(
            @NonNull Rect rect
    ) {
        super(rect);
    }

    public WindowRect(
            int leftInPixels,
            int topInPixels,
            int rightInPixels,
            int bottomInPixels
    ) {
        super(
                leftInPixels,
                topInPixels,
                rightInPixels,
                bottomInPixels);
    }

}
