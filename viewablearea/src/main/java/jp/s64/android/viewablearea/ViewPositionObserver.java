package jp.s64.android.viewablearea;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;

import java.io.Closeable;

public class ViewPositionObserver implements Closeable {

    @NonNull
    private final ViewAreaCalculator helper;

    @NonNull
    private final ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            ViewPositionObserver.this.onLayout();
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
            ViewPositionObserver.this.close();
        }

    };

    @NonNull
    private final IListener listener;

    @Nullable
    private ViewRect lastViewRect = null;

    public ViewPositionObserver(
            @NonNull View view,
            @NonNull IListener listener
    ) {
        this.helper = new ViewAreaCalculator(view, null);
        this.listener = listener;
        start();
    }

    private void onLayout() {
        ViewRect newViewRect = helper.getViewRectInDisplay();

        try {
            if (!Utils.objectsEquals(lastViewRect, newViewRect)) {
                listener.onViewRectChanged(
                        lastViewRect,
                        newViewRect
                );
            }
        } finally {
            lastViewRect = newViewRect;
        }
    }

    private void start() {
        helper.requireActivity().getWindow().getDecorView().getViewTreeObserver()
                .addOnGlobalLayoutListener(layoutListener);
        helper.requireActivity().getApplication()
                .registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    @Override
    public void close() {
        helper.requireActivity().getWindow().getDecorView().getViewTreeObserver()
                .removeOnGlobalLayoutListener(layoutListener);
        helper.requireActivity().getApplication()
                .unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    public interface IListener {

        void onViewRectChanged(
                @Nullable ViewRect oldViewRect,
                @NonNull ViewRect newViewRect
        );

    }

}
