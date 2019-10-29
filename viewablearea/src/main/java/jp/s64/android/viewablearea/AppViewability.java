package jp.s64.android.viewablearea;

import android.support.annotation.NonNull;

public class AppViewability {

    @NonNull
    private final ContentSize contentInDisplay;

    public AppViewability(
            @NonNull ContentSize contentInDisplay
    ) {
        this.contentInDisplay = contentInDisplay;
    }

    @NonNull
    public ContentSize getContentInDisplay() {
        return contentInDisplay;
    }

}
