package jp.s64.android.viewability.core.rect;

import android.graphics.Rect;
import android.support.annotation.NonNull;

public class WidgetRectInWindow extends BaseRect {

    public WidgetRectInWindow(
            @NonNull Rect rect
    ) {
        super(rect);
    }

    public WidgetRectInWindow(
            int leftInPixels,
            int topInPixels,
            int rightInPixels,
            int bottomInPixels
    ) {
        super(
                leftInPixels,
                topInPixels,
                rightInPixels,
                bottomInPixels
        );
    }

}
