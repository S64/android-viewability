package jp.s64.android.viewablearea;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;

import java.io.Closeable;

public class AppAreaObserver implements Closeable {

    @NonNull
    private final AppAreaCalculator helper;

    @NonNull
    private final ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            AppAreaObserver.this.onLayout();
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
    private SystemGaps lastSystemGaps = null;

    @Nullable
    private ContentGaps lastContentGaps = null;

    @Nullable
    private ContentSize lastContentSizeInDisplay = null;

    @Nullable
    private ContentSize lastContentSizeInWindow = null;

    @NonNull
    private final IListener listener;

    public AppAreaObserver(
            @NonNull Activity activity,
            @NonNull IListener listener
    ) {
        this.helper = new AppAreaCalculator(activity);
        this.listener = listener;
        start();
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

    private void onLayout() {
        DisplaySize newDisplaySize = helper.getDisplaySize();
        WindowRect newWindowRect = helper.getWindowRect();
        SystemGaps newSystemGaps = helper.getSystemGaps();
        ContentGaps newContentGaps = helper.getContentGaps();
        ContentSize newContentSizeInDisplay = helper.getContentInDisplay();
        ContentSize newContentSizeInWindow = helper.getContentInWidow();

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
            if (!Utils.objectsEquals(lastSystemGaps, newSystemGaps)) {
                listener.onSystemGapsChanged(
                        lastSystemGaps,
                        newSystemGaps
                );
            }
        } finally {
            lastSystemGaps = newSystemGaps;
        }

        try {
            if (!Utils.objectsEquals(lastContentGaps, newContentGaps)) {
                listener.onContentGapsChanged(
                        lastContentGaps,
                        newContentGaps
                );
            }
        } finally {
            lastContentGaps = newContentGaps;
        }

        try {
            if (!Utils.objectsEquals(lastContentSizeInDisplay, newContentSizeInDisplay)) {
                listener.onContentSizeInDisplayChanged(
                        lastContentSizeInDisplay,
                        newContentSizeInDisplay
                );
            }
        } finally {
            lastContentSizeInDisplay = newContentSizeInDisplay;
        }

        try {
            if (!Utils.objectsEquals(lastContentSizeInWindow, newContentSizeInWindow)) {
                listener.onContentSizeInWindowChanged(
                        lastContentSizeInWindow,
                        newContentSizeInWindow
                );
            }
        } finally {
            lastContentSizeInWindow = newContentSizeInWindow;
        }
    }

    private void start() {
        helper.getDecorView().getViewTreeObserver()
                .addOnGlobalLayoutListener(layoutListener);
        helper.activity.getApplication()
                .registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    @Override
    public void close() {
        helper.getDecorView().getViewTreeObserver()
                .removeOnGlobalLayoutListener(layoutListener);
        helper.activity.getApplication()
                .unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    public interface IListener {

        void onDisplaySizeChanged(
                @Nullable DisplaySize oldDisplaySize,
                @Nullable DisplaySize newDisplaySize
        );

        void onWindowRectChanged(
                @Nullable WindowRect oldWindowRect,
                @NonNull WindowRect newWindowRect
        );

        void onSystemGapsChanged(
                @Nullable SystemGaps oldSystemGaps,
                @Nullable SystemGaps newSystemGaps
        );

        void onContentGapsChanged(
                @Nullable ContentGaps oldContentGaps,
                @NonNull ContentGaps newContentGaps
        );

        void onContentSizeInDisplayChanged(
                @Nullable ContentSize oldContentSizeInDisplay,
                @Nullable ContentSize newContentSizeInDisplay
        );

        void onContentSizeInWindowChanged(
                @Nullable ContentSize lastContentSizeInWindow,
                @NonNull ContentSize newContentSizeInWindow
        );
    }

}
