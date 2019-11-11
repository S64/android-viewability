package jp.s64.android.viewability.apparea;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ObjectsCompat;
import android.view.View;

import java.io.Closeable;

import jp.s64.android.viewability.core.dimension.DisplayDimension;
import jp.s64.android.viewability.core.gaps.ContentGaps;
import jp.s64.android.viewability.core.gaps.SystemGaps;
import jp.s64.android.viewability.core.rect.ContentRect;
import jp.s64.android.viewability.core.rect.WindowRect;
import jp.s64.android.viewability.core.viewability.ContentViewability;
import jp.s64.android.viewability.core.viewability.WindowViewability;

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
        public void onDisplaySizeChanged(@Nullable DisplayDimension newValue) {
            AppViewabilityObserver.this.displayDimension = newValue;
        }

        @Override
        public void onWindowRectChanged(@NonNull WindowRect newValue) {
            AppViewabilityObserver.this.windowRect = newValue;
        }

        @Override
        public void onSystemGapsChanged(@Nullable SystemGaps newValue) {
            // no-op
        }

        @Override
        public void onContentGapsChanged(@NonNull ContentGaps newValue) {
            // no-op
        }

        @Override
        public void onContentInDisplayChanged(@Nullable ContentRect newValue) {
            AppViewabilityObserver.this.contentInDisplay = newValue;
        }

        @Override
        public void onContentInWindowChanged(@Nullable ContentRect newValue) {
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

    public AppViewabilityObserver(@NonNull View widget, @NonNull IListener listener) {
        this(
                new AppViewabilityCalculator(
                        new AppAreaCalculator(widget)
                ),
                listener
        );
    }

    @Nullable
    private WindowRect windowRect;

    @Nullable
    private DisplayDimension displayDimension;

    @Nullable
    private ContentRect contentInDisplay;

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

            if (windowRect != null && displayDimension != null) {
                newWindowViewability = viewabilityCalc.getWindowViewability(windowRect, displayDimension);
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

            if (contentInDisplay != null && displayDimension != null) {
                newContentViewability = viewabilityCalc.getContentViewability(contentInDisplay, displayDimension);
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
