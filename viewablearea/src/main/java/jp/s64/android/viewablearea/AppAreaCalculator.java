package jp.s64.android.viewablearea;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;

public class AppAreaCalculator {

    @NonNull
    final Activity activity;

    public AppAreaCalculator(@NonNull Activity activity) {
        this.activity = activity;
    }

    public AppAreaCalculator(@NonNull View view) {
        this(ViewUtils.requireActivity(view));
    }

    @NonNull
    private Window getWindow() {
        return activity.getWindow();
    }

    @NonNull
    View getDecorView() {
        return getWindow().getDecorView();
    }

    @Nullable
    private Display getCurrentDisplay() {
        return ViewCompat.getDisplay(getDecorView());
    }

    @Nullable
    public DisplaySize getDisplaySize() {
        final DisplayMetrics metrics = new DisplayMetrics();
        Display display = getCurrentDisplay();
        if (display == null) {
            return null;
        }

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealMetrics(metrics);
        } else {
            display.getMetrics(metrics);
        }

        return new DisplaySize(metrics.widthPixels, metrics.heightPixels);
    }

    @NonNull
    public WindowRect getWindowRect() {
        Rect decorWindowRect = new Rect();
        getDecorView().getWindowVisibleDisplayFrame(decorWindowRect);
        return new WindowRect(decorWindowRect);
    }

    @NonNull
    private ContentSize getContentSize() {
        int[] loc = new int[2];
        getContentView().getLocationOnScreen(loc);
        return new ContentSize(
                loc[0],
                loc[1],
                loc[0] + getContentView().getMeasuredWidth(),
                loc[1] + getContentView().getMeasuredHeight()
        );
    }

    @NonNull
    private View getContentView() {
        return getDecorView().findViewById(android.R.id.content);
    }

    @Nullable
    public SystemGaps getSystemGaps() {
        DisplaySize display = getDisplaySize();
        WindowRect window = getWindowRect();

        if (display == null) {
            return null;
        }

        return new SystemGaps(
                window.left,
                window.top,
                display.getWidthInPixels() - window.right,
                display.getHeightInPixels() - window.bottom
        );
    }

    @NonNull
    public ContentGaps getContentGaps() {
        ContentSize content = getContentSize();
        WindowRect window = getWindowRect();

        return new ContentGaps(
                content.left - window.left,
                content.top - window.top,
                window.right - content.right,
                window.bottom - content.bottom
        );
    }

    @Nullable
    public ContentSize getContentInDisplay() {
        DisplaySize display = getDisplaySize();
        SystemGaps systemGaps = getSystemGaps();
        ContentGaps contentGaps = getContentGaps();

        if (systemGaps == null || display == null) {
            return null;
        }

        return new ContentSize(
                systemGaps.getLeftInPixels() + contentGaps.getLeftInPixels(),
                systemGaps.getTopInPixels() + contentGaps.getTopInPixels(),
                display.getWidthInPixels() - systemGaps.getRightInPixels() - contentGaps.getRightInPixels(),
                display.getHeightInPixels() - systemGaps.getBottomInPixels() - contentGaps.getBottomInPixels()
        );
    }

    @NonNull
    public ContentSize getContentInWidow() {
        View content = getContentView();
        ContentGaps contentGaps = getContentGaps();

        return new ContentSize(
                contentGaps.getLeftInPixels(),
                contentGaps.getTopInPixels(),
                contentGaps.getLeftInPixels() + content.getMeasuredWidth(),
                contentGaps.getTopInPixels() + content.getMeasuredHeight()
        );
    }

}
