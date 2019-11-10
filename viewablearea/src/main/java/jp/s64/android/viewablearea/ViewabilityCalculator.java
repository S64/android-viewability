package jp.s64.android.viewablearea;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public class ViewabilityCalculator {

    @NonNull
    final AppViewabilityCalculator appCalc;

    @NonNull
    private final ViewAreaCalculator viewCalc;

    public ViewabilityCalculator(
            @NonNull View view
    ) {
        this(
                view,
                new AppAreaCalculator(view)
        );
    }

    public ViewabilityCalculator(
            @NonNull View view,
            @NonNull AppAreaCalculator appAreaCalculator
    ) {
        this(
                new AppViewabilityCalculator(
                        appAreaCalculator
                ),
                new ViewAreaCalculator(view, appAreaCalculator)
        );
    }

    public ViewabilityCalculator(
            @NonNull AppViewabilityCalculator appViewabilityCalculator,
            @NonNull ViewAreaCalculator viewAreaCalculator
    ) {
        this.appCalc = appViewabilityCalculator;
        this.viewCalc = viewAreaCalculator;
    }

    // region ViewabilityCalculator#getRealViewRect

    @Nullable
    public RealViewRect getRealViewRect() {
        DisplaySize displaySize = appCalc.areaCalculator.getDisplaySize();
        ViewRect viewRectInWindow = viewCalc.getViewRectInWindow();

        if (displaySize == null || viewRectInWindow == null) {
            return null;
        }

        return getRealViewRect(
                appCalc.areaCalculator.getContentSize(),
                displaySize,
                appCalc.areaCalculator.getContentGaps(),
                viewRectInWindow
        );
    }

    @NonNull
    public RealViewRect getRealViewRect(
            @NonNull ContentSize contentInDisplay,
            @NonNull DisplaySize displaySize,
            @NonNull ContentGaps contentGaps,
            @NonNull ViewRect viewRectInWindow
    ) {
        ContentViewability contentViewability
                = appCalc.getContentViewability(contentInDisplay, displaySize);
        ViewRect viewInDisplay
                = viewCalc.getViewRectInDisplay();
        ViewRect viewInContent
                = viewCalc.getViewRectInContent(contentGaps, viewRectInWindow);

        return new RealViewRect(
                contentViewability,
                viewInDisplay,
                viewInContent
        );
    }

    // endregion

}
