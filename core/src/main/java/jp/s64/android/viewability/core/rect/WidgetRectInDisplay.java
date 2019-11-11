package jp.s64.android.viewability.core.rect;

import android.graphics.Rect;
import android.support.annotation.NonNull;

public class WidgetRectInDisplay extends BaseRect {

    public WidgetRectInDisplay(
            @NonNull Rect rect
    ) {
        super(rect);
    }

    public WidgetRectInDisplay(
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
