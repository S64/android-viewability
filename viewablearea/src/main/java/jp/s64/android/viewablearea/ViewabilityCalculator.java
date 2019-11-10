package jp.s64.android.viewablearea;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ObjectsCompat;
import android.support.v4.view.ViewGroupCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

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
    private final View view;

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
                view,
                new AppViewabilityCalculator(
                        appAreaCalculator
                ),
                new ViewAreaCalculator(view, appAreaCalculator),
                scanDepth
        );
    }

    public ViewabilityCalculator(
            @NonNull View view,
            @NonNull AppViewabilityCalculator appViewabilityCalculator,
            @NonNull ViewAreaCalculator viewAreaCalculator,
            @NonNull IScanDepth scanDepth
    ) {
        this.view = view;
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

        View root = appCalc.areaCalculator.getContentView();

        View pItr = view;
        for (int pIdx = -1;;pIdx--) {
            ViewParent parent = pItr.getParent();
            try {
                if (ObjectsCompat.equals(parent, root)) {
                    break;
                }
                digging(pItr, (ViewGroup) parent, pIdx);
            } finally {
                pItr = (View) parent;
            }
        }
    }

    private void digging(View src, ViewGroup startView, int startIdx) {
        for (int i = startView.indexOfChild(src)+1;i<startView.getChildCount();i++) {
            View itr = startView.getChildAt(i);

            ViewAreaCalculator calc = new ViewAreaCalculator(itr, appCalc.areaCalculator);

            if (!(itr instanceof ViewGroup) || ((ViewGroup)itr).getClipChildren()) {
                calc.getViewRectInDisplay();
            }
        }
    }

    // endregion

    public interface IScanDepth {

        boolean isScannable(int depth, @NonNull View target);

    }

}
