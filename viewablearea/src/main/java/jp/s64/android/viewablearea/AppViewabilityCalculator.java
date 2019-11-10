package jp.s64.android.viewablearea;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.View;
import android.view.Window;

public class AppViewabilityCalculator {

    @NonNull
    private final AppAreaCalculator appCalc;

    public AppViewabilityCalculator(
            @NonNull View view
    ) {
        this(
                view,
                null
        );
    }

    public AppViewabilityCalculator(
            @NonNull View view,
            @Nullable AppAreaCalculator appCalculator
    ) {
        if (appCalculator != null) {
            this.appCalc = appCalculator;
        } else {
            this.appCalc = new AppAreaCalculator(view);
        }
    }

    @Nullable
    public WindowViewability getWindowViewability() {
        return getWindowViewability(appCalc.getWindow());
    }

    @Nullable
    public WindowViewability getWindowViewability(@NonNull Window window) {
        return getWindowViewability(
                appCalc.getDecorView(window)
        );
    }

    @Nullable
    public WindowViewability getWindowViewability(@NonNull View decorView) {
        Display display = appCalc.getCurrentDisplay(decorView);

        if (display == null){
            return null;
        }

        return getWindowViewability(
                display,
                appCalc.getWindowRect(decorView)
        );
    }

    @NonNull
    public WindowViewability getWindowViewability(@NonNull Display display, @NonNull WindowRect window) {
        return getWindowViewability(
                appCalc.getDisplaySize(display),
                window
        );
    }

    @NonNull
    public WindowViewability getWindowViewability(
            @NonNull DisplaySize displaySize,
            @NonNull WindowRect window
    ) {
        return new WindowViewability(window, displaySize);
    }


    public ContentViewability getContentViewability(
            @NonNull WindowViewability windowViewability,
            @NonNull ContentGaps contentGaps,
            @NonNull ContentSize contentInWindow
    ) {
        return new ContentViewability(windowViewability, contentGaps, contentInWindow);
    }

}
