package jp.s64.android.viewability.core.rect;

import android.graphics.Rect;
import android.support.annotation.NonNull;

public class WidgetRectInContent extends BaseRect {

    public WidgetRectInContent(
            @NonNull Rect rect
    ) {
        super(rect);
    }

    public WidgetRectInContent(
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
