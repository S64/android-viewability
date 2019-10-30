package jp.s64.android.viewablearea;

import android.support.annotation.NonNull;

public class Viewability {

    @NonNull
    private final ViewRect viewInDisplay;

    public Viewability(
            @NonNull ViewRect viewInDisplay
    ) {
        this.viewInDisplay = viewInDisplay;
    }

    @NonNull
    public ViewRect getViewInDisplay() {
        return viewInDisplay;
    }

}
