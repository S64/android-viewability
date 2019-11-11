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
    Window getWindow() {
        return activity.getWindow();
    }

    @NonNull
    View getDecorView() {
        return getDecorView(getWindow());
    }

    @NonNull
    View getDecorView(@NonNull Window window) {
        return window.getDecorView();
    }

    @Nullable
    private Display getCurrentDisplay() {
        return getCurrentDisplay(getDecorView());
    }

    @Nullable
    Display getCurrentDisplay(@NonNull View decorView) {
        return ViewCompat.getDisplay(decorView);
    }

    @Nullable
    public DisplaySize getDisplaySize() {
        Display display = getCurrentDisplay();
        if (display == null) {
            return null;
        }
        return getDisplaySize(display);
    }

    @NonNull
    public DisplaySize getDisplaySize(@NonNull Display display) {
        final DisplayMetrics metrics = new DisplayMetrics();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealMetrics(metrics);
        } else {
            display.getMetrics(metrics);
        }

        return new DisplaySize(metrics.widthPixels, metrics.heightPixels);
    }

    @NonNull
    public WindowRect getWindowRect(@NonNull View decorView) {
        Rect decorWindowRect = new Rect();
        decorView.getWindowVisibleDisplayFrame(decorWindowRect);
        return new WindowRect(decorWindowRect);
    }

    @NonNull
    public WindowRect getWindowRect() {
        return getWindowRect(getDecorView());
    }

    @NonNull
    ContentSize getContentSize() {
        return getContentSize(getContentView());
    }

    @NonNull
    ContentSize getContentSize(@NonNull View contentView) {
        int[] loc = new int[2];
        contentView.getLocationOnScreen(loc);
        return new ContentSize(
                loc[0],
                loc[1],
                loc[0] + contentView.getMeasuredWidth(),
                loc[1] + contentView.getMeasuredHeight()
        );
    }

    @NonNull
    View getContentView(@NonNull View decorView) {
        return decorView.findViewById(android.R.id.content);
    }

    @NonNull
    View getContentView() {
        return getContentView(getDecorView());
    }

    @Nullable
    public SystemGaps getSystemGaps() {
        DisplaySize display = getDisplaySize();
        WindowRect window = getWindowRect();

        if (display == null) {
            return null;
        }

        return getSystemGaps(display, window);
    }

    @NonNull
    public SystemGaps getSystemGaps(@NonNull DisplaySize display, @NonNull WindowRect window) {
        return new SystemGaps(
                window.left,
                window.top,
                display.getWidthInPixels() - window.right,
                display.getHeightInPixels() - window.bottom
        );
    }

    @NonNull
    public ContentGaps getContentGaps() {
        return getContentGaps(getContentSize(), getWindowRect());
    }

    @NonNull
    public ContentGaps getContentGaps(@NonNull ContentSize content, @NonNull WindowRect window) {
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

        return getContentInDisplay(display, systemGaps, contentGaps);
    }

    @NonNull
    public ContentSize getContentInDisplay(
            @NonNull DisplaySize display,
            @NonNull SystemGaps systemGaps,
            @NonNull ContentGaps contentGaps
    ) {
        return new ContentSize(
                systemGaps.getLeftInPixels() + contentGaps.getLeftInPixels(),
                systemGaps.getTopInPixels() + contentGaps.getTopInPixels(),
                display.getWidthInPixels() - systemGaps.getRightInPixels() - contentGaps.getRightInPixels(),
                display.getHeightInPixels() - systemGaps.getBottomInPixels() - contentGaps.getBottomInPixels()
        );
    }

    @NonNull
    public ContentSize getContentInWindow() {
        return getContentInWindow(
                getContentView(),
                getContentGaps()
        );
    }

    @NonNull
    public ContentSize getContentInWindow(
            @NonNull View content,
            @NonNull ContentGaps contentGaps
    ) {
        return new ContentSize(
                contentGaps.getLeftInPixels(),
                contentGaps.getTopInPixels(),
                contentGaps.getLeftInPixels() + content.getMeasuredWidth(),
                contentGaps.getTopInPixels() + content.getMeasuredHeight()
        );
    }

}
