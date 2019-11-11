package jp.s64.android.viewablearea;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ObjectsCompat;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;

import java.io.Closeable;

public class AppAreaObserver implements Closeable {

    @NonNull
    private final AppAreaCalculator calc;

    @NonNull
    private final IListener listener;

    @Nullable
    private final IEventListener events;

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
            AppAreaObserver.this.close(activity);
        }

    };

    public AppAreaObserver(
            @NonNull Activity activity,
            @NonNull IListener listener,
            @Nullable IEventListener events
    ) {
        this(
                new AppAreaCalculator(activity),
                listener,
                events
        );
    }

    public AppAreaObserver(
            @NonNull AppAreaCalculator appAreaCalculator,
            @NonNull IListener listener
    ) {
        this(
                appAreaCalculator,
                listener,
                null
        );
    }

    public AppAreaObserver(
            @NonNull AppAreaCalculator appAreaCalculator,
            @NonNull IListener listener,
            @Nullable IEventListener events
    ) {
        this.calc = appAreaCalculator;
        this.listener = listener;
        this.events = events;
        start();
    }

    public AppAreaObserver(
            @NonNull View view,
            @NonNull IListener listener,
            @Nullable IEventListener events
    ) {
        this(
                ViewUtils.requireActivity(view),
                listener,
                events
        );
    }

    public AppAreaObserver(
            @NonNull View view,
            @NonNull IListener listener
    ) {
        this(view, listener, null);
    }

    public AppAreaObserver(
            @NonNull Activity activity,
            @NonNull IListener listener
    ) {
        this(activity, listener, null);
    }

    private void start() {
        calc.getDecorView().getViewTreeObserver()
                .addOnGlobalLayoutListener(layoutListener);
        calc.activity.getApplication()
                .registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    @Nullable
    private DisplayDimension lastDisplaySize;

    @Nullable
    private WindowRect lastWindowRect;

    @Nullable
    private SystemGaps lastSystemGaps;

    @Nullable
    private ContentGaps lastContentGaps;

    @Nullable
    private ContentRect lastContentInDisplay;

    @Nullable
    private ContentRect lastContentInWindow;

    private void onLayout() {
        Window window;
        View decorView;
        Display display;
        View contentView;
        ContentRect contentRect;
        {
            window = calc.getWindow();
            decorView = calc.getDecorView(window);
            display = calc.getCurrentDisplay(decorView);

            contentView = calc.getContentView(decorView);
            contentRect = calc.getContentSize(contentView);
        }

        DisplayDimension displaySize = display != null ? calc.getDisplaySize(display) : null;
        try {
            if (!ObjectsCompat.equals(lastDisplaySize, displaySize)) {
                listener.onDisplaySizeChanged(displaySize);
            }
        } finally {
            lastDisplaySize = displaySize;
        }

        WindowRect windowRect = calc.getWindowRect(decorView);
        try {
            if (!ObjectsCompat.equals(lastWindowRect, windowRect)) {
                listener.onWindowRectChanged(windowRect);
            }
        } finally {
            lastWindowRect = windowRect;
        }

        SystemGaps systemGaps = displaySize != null ? calc.getSystemGaps(displaySize, windowRect) : null;
        try {
            if (!ObjectsCompat.equals(lastSystemGaps,systemGaps)) {
                listener.onSystemGapsChanged(systemGaps);
            }
        } finally {
            lastSystemGaps = systemGaps;
        }

        ContentGaps contentGaps = calc.getContentGaps(contentRect, windowRect);
        try {
            if (!ObjectsCompat.equals(lastContentGaps, contentGaps)) {
                listener.onContentGapsChanged(contentGaps);
            }
        } finally {
            lastContentGaps = contentGaps;
        }

        ContentRect contentInDisplay = displaySize != null ? calc.getContentInDisplay(displaySize, systemGaps, contentGaps) : null;
        try {
            if (!ObjectsCompat.equals(lastContentInDisplay, contentInDisplay)) {
                listener.onContentInDisplayChanged(contentInDisplay);
            }
        } finally {
            lastContentInDisplay = contentInDisplay;
        }

        ContentRect contentInWindow = calc.getContentInWindow(contentView, contentGaps);
        try {
            if (!ObjectsCompat.equals(lastContentInWindow, contentInWindow)) {
                listener.onContentInWindowChanged(contentInWindow);
            }
        } finally {
            lastContentInWindow = contentInWindow;
        }

        if (events != null) {
            events.onChangesCompleted();
        }
    }

    @Override
    public void close() {
        calc.getDecorView().getViewTreeObserver()
                .removeOnGlobalLayoutListener(layoutListener);
        calc.activity.getApplication()
                .unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    private void close(@NonNull Activity activity) {
        if (activity == calc.activity) {
            close();
        }
    }

    public interface IListener {

        void onDisplaySizeChanged(@Nullable DisplayDimension newValue);

        void onWindowRectChanged(@NonNull WindowRect windowRect);

        void onSystemGapsChanged(@Nullable SystemGaps systemGaps);

        void onContentGapsChanged(@NonNull ContentGaps contentGaps);

        void onContentInDisplayChanged(@Nullable ContentRect contentInDisplay);

        void onContentInWindowChanged(@Nullable ContentRect contentInWindow);

    }

    public interface IEventListener {

        void onChangesCompleted();

    }

}
