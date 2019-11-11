package jp.s64.android.viewablearea;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

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
    public ViewRect getViewRectInContent() {
        ContentGaps content = area.getContentGaps();
        ViewRect viewRect = getViewRectInWindow();

        return viewRect != null ? getViewRectInContent(
                content,
                viewRect
        ) : null;
    }

    @NonNull
    public ViewRect getViewRectInContent(
            @NonNull ContentGaps contentGaps,
            @NonNull ViewRect viewRectInWindow
    ) {
        int left = viewRectInWindow.left - contentGaps.getLeftInPixels();
        int top = viewRectInWindow.top - contentGaps.getTopInPixels();

        return new ViewRect(
                left,
                top,
                left + viewRectInWindow.getWidthInPixels(),
                top + viewRectInWindow.getHeightInPixels()
        );
    }

    @Nullable
    public ViewRect getViewRectInWindow() {
        SystemGaps systemGaps = area.getSystemGaps();
        ViewRect viewRectInDisplay = getViewRectInDisplay();
        return systemGaps != null ? getViewRectInWindow(systemGaps, viewRectInDisplay) : null;
    }

    @NonNull
    public ViewRect getViewRectInWindow(
            @NonNull SystemGaps systemGaps,
            @NonNull ViewRect viewRectInDisplay
    ) {
        int left = viewRectInDisplay.left - systemGaps.getLeftInPixels();
        int top = viewRectInDisplay.top - systemGaps.getTopInPixels();

        return new ViewRect(
                left,
                top,
                left + viewRectInDisplay.getWidthInPixels(),
                top + viewRectInDisplay.getHeightInPixels()
        );
    }

    @NonNull
    public ViewRect getViewRectInDisplay() {
        int[] loc = new int[2];
        view.getLocationOnScreen(loc);
        return new ViewRect(
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
