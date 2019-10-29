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

public class AppAreaDetector implements Closeable {

    @NonNull
    private final Activity activity;

    @NonNull
    private final ViewTreeObserver.OnDrawListener drawListener = new ViewTreeObserver.OnDrawListener() {

        @Override
        public void onDraw() {
            AppAreaDetector.this.onDraw();
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
            onDraw();
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
            // no-op
        }

    };

    @Nullable
    private DisplaySize lastDisplaySize = null;

    @Nullable
    private WindowSize lastWindowSize = null;

    @Nullable
    private ContentSize lastContentSize = null;

    @NonNull
    private final IListener listener;

    public AppAreaDetector(
            @NonNull Activity activity,
            @NonNull IListener listener
    ) {
        this.activity = activity;
        this.listener = listener;
        activity.getWindow().getDecorView().getViewTreeObserver()
                .addOnDrawListener(drawListener);
        activity.getApplication()
                .registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    public AppAreaDetector(
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
        WindowSize newWindowSize = getWindowSize();
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
            if (!Utils.objectsEquals(lastWindowSize, newWindowSize)) {
                listener.onWindowSizeChanged(
                        lastWindowSize,
                        newWindowSize
                );
            }
        } finally {
            lastWindowSize = newWindowSize;
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
    public WindowSize getWindowSize() {
        Rect decorWindowSize = new Rect();
        getDecorView().getWindowVisibleDisplayFrame(decorWindowSize);
        return new WindowSize(decorWindowSize);
    }

    @NonNull
    public ContentSize getContentSize() {
        View root = getDecorView();
        View contentView = root.findViewById(android.R.id.content);
        return new ContentSize(
                contentView.getMeasuredWidth(),
                contentView.getMeasuredHeight()
        );
    }

    @Override
    public void close() {
        activity.getWindow().getDecorView().getViewTreeObserver()
                .removeOnDrawListener(drawListener);
        activity.getApplication()
                .unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    public interface IListener {

        void onDisplaySizeChanged(
                @Nullable DisplaySize oldDisplaySize,
                @Nullable DisplaySize newDisplaySize
        );

        void onWindowSizeChanged(
                @Nullable WindowSize oldWindowSize,
                @NonNull WindowSize newWindowSize
        );

        void onContentSizeChanged(
                @Nullable ContentSize lastContentSize,
                @NonNull ContentSize newContentSize
        );
    }

}
