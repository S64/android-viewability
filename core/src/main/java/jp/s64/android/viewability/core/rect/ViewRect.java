package jp.s64.android.viewability.core.rect;

import android.graphics.Rect;
import android.support.annotation.NonNull;

public class ViewRect extends BaseRect {

    public ViewRect(
            @NonNull Rect rect
    ) {
        super(rect);
    }

    public ViewRect(
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
