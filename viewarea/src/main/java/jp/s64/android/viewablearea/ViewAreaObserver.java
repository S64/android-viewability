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

import jp.s64.android.viewability.core.dimension.DisplayDimension;
import jp.s64.android.viewability.core.gaps.ContentGaps;
import jp.s64.android.viewability.core.gaps.SystemGaps;
import jp.s64.android.viewability.core.rect.ContentRect;
import jp.s64.android.viewability.core.rect.WidgetRectInContent;
import jp.s64.android.viewability.core.rect.WidgetRectInDisplay;
import jp.s64.android.viewability.core.rect.WidgetRectInWindow;
import jp.s64.android.viewability.core.rect.WindowRect;

public class ViewAreaObserver implements Closeable {

    @NonNull
    private final AppAreaObserver appAreaObserver;

    @NonNull
    private final ViewAreaCalculator calc;

    @NonNull
    private final IListener listener;

    @Nullable
    private final AppAreaObserver.IEventListener events;

    @NonNull
    private final AppAreaObserver.IEventListener appAreaEvents = new AppAreaObserver.IEventListener() {

        @Override
        public void onChangesCompleted() {
            ViewAreaObserver.this.onLayout();
        }

    };

    @NonNull
    private final ViewTreeObserver.OnDrawListener drawListener = new ViewTreeObserver.OnDrawListener() {

        @Override
        public void onDraw() {
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
        public void onDisplaySizeChanged(@Nullable DisplayDimension newValue) {
            // no-op
        }

        @Override
        public void onWindowRectChanged(@NonNull WindowRect newValue) {
            // no-op
        }

        @Override
        public void onSystemGapsChanged(@Nullable SystemGaps newValue) {
            ViewAreaObserver.this.systemGaps = newValue;
        }

        @Override
        public void onContentGapsChanged(@NonNull ContentGaps newValue) {
            ViewAreaObserver.this.contentGaps = newValue;
        }

        @Override
        public void onContentInDisplayChanged(@Nullable ContentRect newValue) {
            // no-op
        }

        @Override
        public void onContentInWindowChanged(@Nullable ContentRect newValue) {
            // no-op
        }

    };

    public ViewAreaObserver(
            @NonNull View view,
            @NonNull IListener listener
    ) {
        this(view, listener, null);
    }

    public ViewAreaObserver(
            @NonNull View view,
            @NonNull IListener listener,
            @Nullable AppAreaObserver.IEventListener events
    ) {
        this.appAreaObserver = new AppAreaObserver(view, appAreaListener, appAreaEvents);
        this.calc = new ViewAreaCalculator(view);
        this.listener = listener;
        this.events = events;
        start();
    }

    @Nullable
    private ContentGaps contentGaps;

    @Nullable
    private SystemGaps systemGaps;

    @Nullable
    private WidgetRectInDisplay lastWidgetRectInDisplay;

    @Nullable
    private WidgetRectInContent lastWidgetRectInContent;

    @Nullable
    private WidgetRectInWindow lastWidgetRectInWindow;

    private void onLayout() {
        WidgetRectInDisplay widgetRectInDisplay = calc.getViewRectInDisplay();
        try {
            if (!ObjectsCompat.equals(lastWidgetRectInDisplay, widgetRectInDisplay)) {
                listener.onViewRectInDisplayChanged(widgetRectInDisplay);
            }
        } finally {
            lastWidgetRectInDisplay = widgetRectInDisplay;
        }

        WidgetRectInWindow widgetRectInWindow = systemGaps != null ? calc.getViewRectInWindow(systemGaps, widgetRectInDisplay) : null;
        try {
            if (!ObjectsCompat.equals(lastWidgetRectInWindow, widgetRectInWindow)) {
                listener.onViewRectInWindowChanged(widgetRectInWindow);
            }
        } finally {
            lastWidgetRectInWindow = widgetRectInWindow;
        }

        WidgetRectInContent widgetRectInContent = contentGaps != null && widgetRectInWindow != null
                ? calc.getViewRectInContent(contentGaps, widgetRectInWindow) : null;
        try {
            if (!ObjectsCompat.equals(lastWidgetRectInContent, widgetRectInContent)) {
                listener.onViewRectInContentChanged(widgetRectInContent);
            }
        } finally {
            lastWidgetRectInContent = widgetRectInContent;
        }

        if (events != null) {
            events.onChangesCompleted();
        }
    }

    private void start() {
        calc.requireActivity().getWindow().getDecorView().getViewTreeObserver()
                .addOnDrawListener(drawListener);
        calc.requireActivity().getApplication()
                .registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    @Override
    public void close() {
        appAreaObserver.close();
        calc.requireActivity().getWindow().getDecorView().getViewTreeObserver()
                .removeOnDrawListener(drawListener);
        calc.requireActivity().getApplication()
                .unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    private void close(@NonNull Activity activity) {
        if (activity == calc.requireActivity()) {
            close();
        }
    }

    public interface IListener {

        void onViewRectInContentChanged(@Nullable WidgetRectInContent newValue);
        void onViewRectInWindowChanged(@Nullable WidgetRectInWindow newValue);
        void onViewRectInDisplayChanged(@NonNull WidgetRectInDisplay newValue);

    }

}
