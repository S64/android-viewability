package jp.s64.android.viewablearea;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public class ViewabilityCalculator {

    @NonNull
    public static IScanDepth createScanDepth(final int depth) {
        return new IScanDepth() {

            @Override
            public boolean isScannable(int needle, @NonNull View target) {
                return needle <= depth;
            }

        };
    }

    public static IScanDepth SCAN_INFINITE = new IScanDepth() {
        @Override
        public boolean isScannable(int depth, @NonNull View target) {
            return true;
        }
    };

    @NonNull
    final AppViewabilityCalculator appCalc;

    @NonNull
    private final ViewAreaCalculator viewCalc;

    @NonNull
    private final IScanDepth scanDepth;

    public ViewabilityCalculator(
            @NonNull View view,
            @NonNull IScanDepth scanDepth
    ) {
        this(
                view,
                new AppAreaCalculator(view),
                scanDepth
        );
    }

    public ViewabilityCalculator(
            @NonNull View view,
            @NonNull AppAreaCalculator appAreaCalculator,
            @NonNull IScanDepth scanDepth
    ) {
        this(
                new AppViewabilityCalculator(
                        appAreaCalculator
                ),
                new ViewAreaCalculator(view, appAreaCalculator),
                scanDepth
        );
    }

    public ViewabilityCalculator(
            @NonNull AppViewabilityCalculator appViewabilityCalculator,
            @NonNull ViewAreaCalculator viewAreaCalculator,
            @NonNull IScanDepth scanDepth
    ) {
        this.appCalc = appViewabilityCalculator;
        this.viewCalc = viewAreaCalculator;
        this.scanDepth = scanDepth;
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

    // region ViewabilityCalculator#getViewability

    @Nullable
    public Viewability getViewability() {
        RealViewRect realViewRect = getRealViewRect();
        return realViewRect != null ? getViewability(realViewRect) : null;
    }

    @NonNull
    public Viewability getViewability(
            @NonNull RealViewRect realViewRect
    ) {
        if (realViewRect.getViewability() < 0) {
            return new Viewability(0);
        }
        return new Viewability(realViewRect.getViewability()); // TODO
    }

    // endregion

    public interface IScanDepth {

        boolean isScannable(int depth, @NonNull View target);

    }

}
