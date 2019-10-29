package jp.s64.android.viewablearea;

import android.graphics.Rect;
import android.support.annotation.NonNull;

public class WindowSize extends BaseRect {

    public WindowSize(
            @NonNull Rect rect
    ) {
        super(rect);
    }

    public WindowSize(
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
