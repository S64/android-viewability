package jp.s64.android.viewablearea;

import android.graphics.Rect;
import android.support.annotation.NonNull;

public class ContentSize extends BaseRect {

    public ContentSize(
            @NonNull Rect rect
    ) {
        super(rect);
    }

    public ContentSize(
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
