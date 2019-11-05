package jp.s64.android.viewablearea;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ObjectsCompat;
import android.view.View;
import android.view.ViewTreeObserver;

import java.io.Closeable;

public class ViewAreaObserver implements Closeable {

    @NonNull
    private final AppAreaObserver appAreaObserver;

    @NonNull
    private final ViewAreaCalculator calc;

    @NonNull
    private final IListener listener;

    @NonNull
    private final AppAreaObserver.IEventListener appAreaEvents = new AppAreaObserver.IEventListener() {

        @Override
        public void onChangesCompleted() {
            ViewAreaObserver.this.onLayout();
        }

    };

    @NonNull
    private final ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            ViewAreaObserver.this.onLayout();
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
            ViewAreaObserver.this.close(activity);
        }

    };

    @NonNull
    private final AppAreaObserver.IListener appAreaListener = new AppAreaObserver.IListener() {

        @Override
        public void onDisplaySizeChanged(@Nullable DisplaySize newValue) {
            // no-op
        }

        @Override
        public void onWindowRectChanged(@NonNull WindowRect windowRect) {
            // no-op
        }

        @Override
        public void onSystemGapsChanged(@Nullable SystemGaps systemGaps) {
            ViewAreaObserver.this.systemGaps = systemGaps;
        }

        @Override
        public void onContentGapsChanged(@NonNull ContentGaps contentGaps) {
            ViewAreaObserver.this.contentGaps = contentGaps;
        }

        @Override
        public void onContentInDisplayChanged(@Nullable ContentSize contentInDisplay) {
            // no-op
        }

        @Override
        public void onContentInWindowChanged(@Nullable ContentSize contentInWindow) {
            // no-op
        }

    };

    public ViewAreaObserver(
            @NonNull View view,
            @NonNull IListener listener
    ) {
        this.appAreaObserver = new AppAreaObserver(view, appAreaListener, appAreaEvents);
        this.calc = new ViewAreaCalculator(view);
        this.listener = listener;
        start();
    }

    @Nullable
    private ContentGaps contentGaps;

    @Nullable
    private SystemGaps systemGaps;

    @Nullable
    private ViewRect lastViewRectInDisplay;

    @Nullable
    private ViewRect lastViewRectInContent;

    @Nullable
    private ViewRect lastViewRectInWindow;

    private void onLayout() {
        ViewRect viewRectInDisplay = calc.getViewRectInDisplay();
        try {
            if (!ObjectsCompat.equals(lastViewRectInDisplay, viewRectInDisplay)) {
                listener.onViewRectInDisplayChanged(viewRectInDisplay);
            }
        } finally {
            lastViewRectInDisplay = viewRectInDisplay;
        }

        ViewRect viewRectInWindow = systemGaps != null ? calc.getViewRectInWindow(systemGaps, viewRectInDisplay) : null;
        try {
            if (!ObjectsCompat.equals(lastViewRectInWindow, viewRectInWindow)) {
                listener.onViewRectInWindowChanged(viewRectInWindow);
            }
        } finally {
            lastViewRectInWindow = viewRectInWindow;
        }

        ViewRect viewRectInContent = contentGaps != null ? calc.getViewRectInContent(contentGaps, viewRectInWindow) : null;
        try {
            if (!ObjectsCompat.equals(lastViewRectInContent, viewRectInContent)) {
                listener.onViewRectInContentChanged(viewRectInContent);
            }
        } finally {
            lastViewRectInContent = viewRectInContent;
        }
    }

    private void start() {
        calc.requireActivity().getWindow().getDecorView().getViewTreeObserver()
                .addOnGlobalLayoutListener(layoutListener);
        calc.requireActivity().getApplication()
                .registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    @Override
    public void close() {
        appAreaObserver.close();
        calc.requireActivity().getWindow().getDecorView().getViewTreeObserver()
                .removeOnGlobalLayoutListener(layoutListener);
        calc.requireActivity().getApplication()
                .unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    private void close(@NonNull Activity activity) {
        if (activity == calc.requireActivity()) {
            close();
        }
    }

    public interface IListener {

        void onViewRectInContentChanged(@Nullable ViewRect newValue);
        void onViewRectInWindowChanged(@Nullable ViewRect newValue);
        void onViewRectInDisplayChanged(@NonNull ViewRect newValue);

    }

}
