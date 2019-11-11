package jp.s64.android.viewablearea;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.View;
import android.view.Window;

import jp.s64.android.viewability.core.dimension.DisplayDimension;
import jp.s64.android.viewability.core.rect.ContentRect;
import jp.s64.android.viewability.core.rect.WindowRect;
import jp.s64.android.viewability.core.viewability.ContentViewability;
import jp.s64.android.viewability.core.viewability.WindowViewability;

public class AppViewabilityCalculator {

    @NonNull
    final AppAreaCalculator areaCalculator;

    public AppViewabilityCalculator(
            @NonNull AppAreaCalculator appAreaCalculator
    ) {
        this.areaCalculator = appAreaCalculator;
    }

    // region AppViewabilityCalculator#getWindowViewability

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
                areaCalculator.getDisplayDimension(display)
        );
    }

    @NonNull
    public WindowViewability getWindowViewability(
            @NonNull WindowRect windowRect,
            @NonNull DisplayDimension displaySize
    ) {
        return new WindowViewability(
                windowRect,
                displaySize
        );
    }

    // endregion

    // region AppViewabilityCalculator#getContentViewability

    @Nullable
    public ContentViewability getContentViewability() {
        ContentRect contentInDisplay = areaCalculator.getContentInDisplay();
        DisplayDimension displaySize = areaCalculator.getDisplayDimension();

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
            @NonNull ContentRect contentInDisplay,
            @NonNull DisplayDimension displaySize
    ) {
        return new ContentViewability(
                contentInDisplay,
                displaySize
        );
    }

    // endregion

}
