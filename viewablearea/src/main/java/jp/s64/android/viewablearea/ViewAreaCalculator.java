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
            @NonNull AppAreaCalculator areaHelper
    ) {
        this.view = view;
        this.area = areaHelper;
    }

    @Nullable
    public ViewRect getViewRectInContent() {
        ContentGaps content = area.getContentGaps();
        ViewRect viewRect = getViewRectInWindow();

        if (viewRect == null) {
            return null;
        }

        int left = viewRect.left - content.getLeftInPixels();
        int top = viewRect.top - content.getTopInPixels();

        return new ViewRect(
                left,
                top,
                left + viewRect.getWidthInPixels(),
                top + viewRect.getHeightInPixels()
        );
    }

    @Nullable
    public ViewRect getViewRectInWindow() {
        SystemGaps system = area.getSystemGaps();
        ViewRect viewRect = getViewRectInDisplay();

        if (system == null) {
            return null;
        }

        int left = viewRect.left - system.getLeftInPixels();
        int top = viewRect.top - system.getTopInPixels();

        return new ViewRect(
                left,
                top,
                left + viewRect.getWidthInPixels(),
                top + viewRect.getHeightInPixels()
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
