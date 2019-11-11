package jp.s64.android.viewability.viewarea;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import jp.s64.android.viewability.ViewUtils;
import jp.s64.android.viewability.core.gaps.ContentGaps;
import jp.s64.android.viewability.core.gaps.SystemGaps;
import jp.s64.android.viewability.core.rect.WidgetRectInContent;
import jp.s64.android.viewability.core.rect.WidgetRectInDisplay;
import jp.s64.android.viewability.core.rect.WidgetRectInWindow;
import jp.s64.android.viewability.apparea.AppAreaCalculator;

public class ViewAreaCalculator {

    @NonNull
    private final AppAreaCalculator area;

    @NonNull
    private final View view;

    public ViewAreaCalculator(
            @NonNull View view,
            @NonNull AppAreaCalculator areaCalc
    ) {
        this.view = view;
        this.area = areaCalc;
    }

    public ViewAreaCalculator(@NonNull View view) {
        this.view = view;
        this.area = new AppAreaCalculator(view);
    }

    @Nullable
    public WidgetRectInContent getViewRectInContent() {
        ContentGaps content = area.getContentGaps();
        WidgetRectInWindow widgetRect = getViewRectInWindow();

        return widgetRect != null ? getViewRectInContent(
                content,
                widgetRect
        ) : null;
    }

    @NonNull
    public WidgetRectInContent getViewRectInContent(
            @NonNull ContentGaps contentGaps,
            @NonNull WidgetRectInWindow widgetRectInWindow
    ) {
        int left = widgetRectInWindow.left - contentGaps.getLeftInPixels();
        int top = widgetRectInWindow.top - contentGaps.getTopInPixels();

        return new WidgetRectInContent(
                left,
                top,
                left + widgetRectInWindow.getWidthInPixels(),
                top + widgetRectInWindow.getHeightInPixels()
        );
    }

    @Nullable
    public WidgetRectInWindow getViewRectInWindow() {
        SystemGaps systemGaps = area.getSystemGaps();
        WidgetRectInDisplay widgetRectInDisplay = getViewRectInDisplay();
        return systemGaps != null ? getViewRectInWindow(systemGaps, widgetRectInDisplay) : null;
    }

    @NonNull
    public WidgetRectInWindow getViewRectInWindow(
            @NonNull SystemGaps systemGaps,
            @NonNull WidgetRectInDisplay widgetRectInDisplay
    ) {
        int left = widgetRectInDisplay.left - systemGaps.getLeftInPixels();
        int top = widgetRectInDisplay.top - systemGaps.getTopInPixels();

        return new WidgetRectInWindow(
                left,
                top,
                left + widgetRectInDisplay.getWidthInPixels(),
                top + widgetRectInDisplay.getHeightInPixels()
        );
    }

    @NonNull
    public WidgetRectInDisplay getViewRectInDisplay() {
        int[] loc = new int[2];
        view.getLocationOnScreen(loc);
        return new WidgetRectInDisplay(
                loc[0],
                loc[1],
                loc[0] + view.getMeasuredWidth(),
                loc[1] + view.getMeasuredHeight()
        );
    }

    @NonNull
    Activity requireActivity() {
        return ViewUtils.requireActivity(view);
    }

}
