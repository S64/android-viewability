package jp.s64.android.viewablearea;

import android.app.Activity;
import android.app.Application;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;

import java.io.Closeable;

public class AppAreaObserver implements Closeable {

    @NonNull
    private final Activity activity;

    @NonNull
    private final ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            AppAreaObserver.this.onDraw();
        }

    };

    @NonNull
    private final Application.ActivityLifecycleCallbacks lifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
            // no-op
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
            // no-op
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
            // no-op
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {
            // no-op
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
            // no-op
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
            // no-op
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
            AppAreaObserver.this.close();
        }

    };

    @Nullable
    private DisplaySize lastDisplaySize = null;

    @Nullable
    private WindowRect lastWindowRect = null;

    @Nullable
    private ContentSize lastContentSize = null;

    @NonNull
    private final IListener listener;

    public AppAreaObserver(
            @NonNull Activity activity,
            @NonNull IListener listener
    ) {
        this.activity = activity;
        this.listener = listener;
        activity.getWindow().getDecorView().getViewTreeObserver()
                .addOnGlobalLayoutListener(layoutListener);
        activity.getApplication()
                .registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    public AppAreaObserver(
            @NonNull View view,
            @NonNull IListener listener
    ) {
        this(
                ViewUtils.requireActivity(view),
                listener
        );
    }

    private Window getWindow() {
        return activity.getWindow();
    }

    @NonNull
    private View getDecorView() {
        return getWindow().getDecorView();
    }

    private Display getCurrentDisplay() {
        return ViewCompat.getDisplay(getDecorView());
    }

    private void onDraw() {
        DisplaySize newDisplaySize = getDisplaySize();
        WindowRect newWindowRect = getWindowRect();
        ContentSize newContentSize = getContentSize();

        try {
            if (!Utils.objectsEquals(lastDisplaySize, newDisplaySize)) {
                listener.onDisplaySizeChanged(
                        lastDisplaySize,
                        newDisplaySize
                );
            }
        } finally {
            lastDisplaySize = newDisplaySize;
        }

        try {
            if (!Utils.objectsEquals(lastWindowRect, newWindowRect)) {
                listener.onWindowRectChanged(
                        lastWindowRect,
                        newWindowRect
                );
            }
        } finally {
            lastWindowRect = newWindowRect;
        }

        try {
            if (!Utils.objectsEquals(lastContentSize, newContentSize)) {
                listener.onContentSizeChanged(
                        lastContentSize,
                        newContentSize
                );
            }
        } finally {
            lastContentSize = newContentSize;
        }
    }

    @NonNull
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
    public ContentSize getContentSize() {
        Rect rect = new Rect();
        getDecorView().findViewById(android.R.id.content)
                .getGlobalVisibleRect(rect);
        return new ContentSize(rect);
    }

    @Override
    public void close() {
        activity.getWindow().getDecorView().getViewTreeObserver()
                .removeOnGlobalLayoutListener(layoutListener);
        activity.getApplication()
                .unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    public interface IListener {

        void onDisplaySizeChanged(
                @Nullable DisplaySize oldDisplaySize,
                @NonNull DisplaySize newDisplaySize
        );

        void onWindowRectChanged(
                @Nullable WindowRect oldWindowRect,
                @NonNull WindowRect newWindowRect
        );

        void onContentSizeChanged(
                @Nullable ContentSize oldContentSize,
                @NonNull ContentSize newContentSize
        );

    }

}
