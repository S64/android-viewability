package jp.s64.android.viewablearea;

import android.graphics.Rect;
import android.support.annotation.NonNull;

public class ContentRect extends BaseRect {

    public ContentRect(
            @NonNull Rect rect
    ) {
        super(rect);
    }

    public ContentRect(
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
