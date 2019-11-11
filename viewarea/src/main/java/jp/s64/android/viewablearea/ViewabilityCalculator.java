package jp.s64.android.viewablearea;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import jp.s64.android.viewability.core.dimension.DisplayDimension;
import jp.s64.android.viewability.core.gaps.ContentGaps;
import jp.s64.android.viewability.core.rect.ContentRect;
import jp.s64.android.viewability.core.rect.RealWidgetRect;
import jp.s64.android.viewability.core.rect.WidgetRectInContent;
import jp.s64.android.viewability.core.rect.WidgetRectInDisplay;
import jp.s64.android.viewability.core.rect.WidgetRectInWindow;
import jp.s64.android.viewability.core.viewability.ContentViewability;
import jp.s64.android.viewability.core.viewability.Viewability;

public class ViewabilityCalculator {

    @NonNull
    private final View view;

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
                view,
                new AppViewabilityCalculator(
                        appAreaCalculator
                ),
                new ViewAreaCalculator(view, appAreaCalculator)
        );
    }

    public ViewabilityCalculator(
            @NonNull View view,
            @NonNull AppViewabilityCalculator appViewabilityCalculator,
            @NonNull ViewAreaCalculator viewAreaCalculator
    ) {
        this.view = view;
        this.appCalc = appViewabilityCalculator;
        this.viewCalc = viewAreaCalculator;
    }

    // region ViewabilityCalculator#getRealViewRect

    @Nullable
    public RealWidgetRect getRealViewRect() {
        DisplayDimension displaySize = appCalc.areaCalculator.getDisplayDimension();
        WidgetRectInWindow widgetRectInWindow = viewCalc.getViewRectInWindow();

        if (displaySize == null || widgetRectInWindow == null) {
            return null;
        }

        return getRealViewRect(
                appCalc.areaCalculator.getContentSize(),
                displaySize,
                appCalc.areaCalculator.getContentGaps(),
                widgetRectInWindow
        );
    }

    @NonNull
    public RealWidgetRect getRealViewRect(
            @NonNull ContentRect contentInDisplay,
            @NonNull DisplayDimension displaySize,
            @NonNull ContentGaps contentGaps,
            @NonNull WidgetRectInWindow widgetRectInWindow
    ) {
        ContentViewability contentViewability
                = appCalc.getContentViewability(contentInDisplay, displaySize);
        WidgetRectInDisplay viewInDisplay
                = viewCalc.getViewRectInDisplay();
        WidgetRectInContent viewInContent
                = viewCalc.getViewRectInContent(contentGaps, widgetRectInWindow);

        return new RealWidgetRect(
                contentViewability,
                viewInDisplay,
                viewInContent
        );
    }

    // endregion

    // region ViewabilityCalculator#getViewability

    @Nullable
    public Viewability getViewability() {
        RealWidgetRect realWidgetRect = getRealViewRect();
        return realWidgetRect != null ? getViewability(realWidgetRect) : null;
    }

    @NonNull
    public Viewability getViewability(
            @NonNull RealWidgetRect realWidgetRect
    ) {
        if (realWidgetRect.getViewability() < 0) {
            return new Viewability(0);
        }
        return new Viewability(realWidgetRect.getViewability()); // TODO
    }

    // endregion

}
