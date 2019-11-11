package jp.s64.android.viewablearea;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ObjectsCompat;
import android.view.View;

import java.io.Closeable;

public class AppViewabilityObserver implements Closeable {

    @NonNull
    private final AppViewabilityCalculator viewabilityCalc;

    @NonNull
    private final AppAreaObserver areaObserver;

    @NonNull
    private final AppAreaObserver.IEventListener appAreaEvents = new AppAreaObserver.IEventListener() {

        @Override
        public void onChangesCompleted() {
            AppViewabilityObserver.this.onLayout();
        }

    };

    @NonNull
    private final AppAreaObserver.IListener appAreaListener = new AppAreaObserver.IListener() {

        @Override
        public void onDisplaySizeChanged(@Nullable DisplaySize newValue) {
            AppViewabilityObserver.this.displaySize = newValue;
        }

        @Override
        public void onWindowRectChanged(@NonNull WindowRect windowRect) {
            AppViewabilityObserver.this.windowRect = windowRect;
        }

        @Override
        public void onSystemGapsChanged(@Nullable SystemGaps systemGaps) {
            // no-op
        }

        @Override
        public void onContentGapsChanged(@NonNull ContentGaps contentGaps) {
            // no-op
        }

        @Override
        public void onContentInDisplayChanged(@Nullable ContentSize contentInDisplay) {
            AppViewabilityObserver.this.contentInDisplay = contentInDisplay;
        }

        @Override
        public void onContentInWindowChanged(@Nullable ContentSize contentInWindow) {
            // no-op
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
            AppViewabilityObserver.this.close(activity);
        }

    };

    @NonNull
    private final IListener listener;

    public AppViewabilityObserver(@NonNull Activity activity, @NonNull IListener listener) {
        this(
                new AppViewabilityCalculator(
                        new AppAreaCalculator(activity)
                ),
                listener
        );
    }

    public AppViewabilityObserver(@NonNull View view, @NonNull IListener listener) {
        this(
                new AppViewabilityCalculator(
                        new AppAreaCalculator(view)
                ),
                listener
        );
    }

    @Nullable
    private WindowRect windowRect;

    @Nullable
    private DisplaySize displaySize;

    @Nullable
    private ContentSize contentInDisplay;

    public AppViewabilityObserver(
            @NonNull AppViewabilityCalculator appViewabilityCalculator,
            @NonNull IListener listener
    ) {
        this.viewabilityCalc = appViewabilityCalculator;
        this.areaObserver = new AppAreaObserver(
                appViewabilityCalculator.areaCalculator,
                appAreaListener,
                appAreaEvents
        );
        this.listener = listener;
    }

    @Nullable
    private WindowViewability lastWindowViewability;

    @Nullable
    private ContentViewability lastContentViewability;

    private void onLayout() {
        {
            WindowViewability newWindowViewability;

            if (windowRect != null && displaySize != null) {
                newWindowViewability = viewabilityCalc.getWindowViewability(windowRect, displaySize);
            } else {
                newWindowViewability = null;
            }

            try {
                if (!ObjectsCompat.equals(lastWindowViewability, newWindowViewability)) {
                    listener.onWindowViewabilityChanged(
                            newWindowViewability
                    );
                }
            } finally {
                lastWindowViewability = newWindowViewability;
            }
        }
        {
            ContentViewability newContentViewability;

            if (contentInDisplay != null && displaySize != null) {
                newContentViewability = viewabilityCalc.getContentViewability(contentInDisplay, displaySize);
            } else {
                newContentViewability = null;
            }

            try {
                if (!ObjectsCompat.equals(lastContentViewability, newContentViewability)) {
                    listener.onContentViewabilityChanged(
                            newContentViewability
                    );
                }
            } finally {
                lastContentViewability = newContentViewability;
            }
        }
    }

    private void start() {
        viewabilityCalc.areaCalculator.activity.getApplication()
                .registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    @Override
    public void close() {
        areaObserver.close();
        viewabilityCalc.areaCalculator.activity.getApplication()
                .unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    private void close(@NonNull Activity activity) {
        if (activity == viewabilityCalc.areaCalculator.activity) {
            close();
        }
    }

    public interface IListener {

        void onWindowViewabilityChanged(@Nullable WindowViewability newValue);
        void onContentViewabilityChanged(@Nullable ContentViewability newValue);

    }

}
