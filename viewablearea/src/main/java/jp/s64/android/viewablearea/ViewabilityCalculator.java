package jp.s64.android.viewablearea;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.View;
import android.view.Window;

public class ViewabilityCalculator {

    @NonNull
    private final AppAreaCalculator areaCalculator;

    public ViewabilityCalculator(
            @NonNull AppAreaCalculator appAreaCalculator
    ) {
        this.areaCalculator = appAreaCalculator;
    }

    // region ViewabilityCalculator#getWindowViewability

    @Nullable
    public WindowViewability getWindowViewability() {
        return getWindowViewability(areaCalculator.getWindow());
    }

    @Nullable
    public WindowViewability getWindowViewability(@NonNull Window window) {
        View decorView = areaCalculator.getDecorView(window);
        Display display = areaCalculator.getCurrentDisplay(decorView);

        if (display == null) {
            return null;
        }

        return getWindowViewability(
                decorView,
                display
        );
    }

    @NonNull
    public WindowViewability getWindowViewability(
            @NonNull View decorView,
            @NonNull Display display
    ) {
        return getWindowViewability(
                areaCalculator.getWindowRect(decorView),
                areaCalculator.getDisplaySize(display)
        );
    }

    @NonNull
    public WindowViewability getWindowViewability(
            @NonNull WindowRect windowRect,
            @NonNull DisplaySize displaySize
    ) {
        return new WindowViewability(
                windowRect,
                displaySize
        );
    }

    // endregion

    // region ViewabilityCalculator#getContentViewability

    @Nullable
    public ContentViewability getContentViewability() {
        ContentSize contentInDisplay = areaCalculator.getContentInDisplay();
        DisplaySize displaySize = areaCalculator.getDisplaySize();

        if (contentInDisplay == null || displaySize == null) {
            return null;
        }

        return getContentViewability(
                contentInDisplay,
                displaySize
        );
    }

    @NonNull
    public ContentViewability getContentViewability(
            @NonNull ContentSize contentInDisplay,
            @NonNull DisplaySize displaySize
    ) {
        return new ContentViewability(
                contentInDisplay,
                displaySize
        );
    }

    // endregion

}
