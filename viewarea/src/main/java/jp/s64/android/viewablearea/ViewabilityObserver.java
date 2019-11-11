package jp.s64.android.viewablearea;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ObjectsCompat;
import android.view.View;

import java.io.Closeable;

public class ViewabilityObserver implements Closeable {

    @NonNull
    private final AppAreaObserver appObserver;

    @NonNull
    private final ViewAreaObserver viewObserver;

    private final ViewabilityCalculator viewabilityCalc;

    @NonNull
    private final IListener listener;

    private final AppAreaObserver.IListener appListener = new AppAreaObserver.IListener() {

        @Override
        public void onDisplaySizeChanged(@Nullable DisplayDimension newValue) {
            ViewabilityObserver.this.displaySize = newValue;
        }

        @Override
        public void onWindowRectChanged(@NonNull WindowRect windowRect) {
            // no-op
        }

        @Override
        public void onSystemGapsChanged(@Nullable SystemGaps systemGaps) {
            // no-op
        }

        @Override
        public void onContentGapsChanged(@NonNull ContentGaps contentGaps) {
            ViewabilityObserver.this.contentGaps = contentGaps;
        }

        @Override
        public void onContentInDisplayChanged(@Nullable ContentRect contentInDisplay) {
            ViewabilityObserver.this.contentInDisplay = contentInDisplay;
        }

        @Override
        public void onContentInWindowChanged(@Nullable ContentRect contentInWindow) {
            // no-op
        }

    };

    private final ViewAreaObserver.IListener viewListener = new ViewAreaObserver.IListener() {

        @Override
        public void onViewRectInContentChanged(@Nullable ViewRect newValue) {
            // no-op
        }

        @Override
        public void onViewRectInWindowChanged(@Nullable ViewRect newValue) {
            ViewabilityObserver.this.viewRectInWindow = newValue;
        }

        @Override
        public void onViewRectInDisplayChanged(@NonNull ViewRect newValue) {
            // no-op
        }

    };

    private final AppAreaObserver.IEventListener events = new AppAreaObserver.IEventListener() {

        @Override
        public void onChangesCompleted() {
            ViewabilityObserver.this.onLayout();
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
            ViewabilityObserver.this.close(activity);
        }

    };

    @Nullable
    private DisplayDimension displaySize;

    @Nullable
    private ContentGaps contentGaps;

    @Nullable
    private ContentRect contentInDisplay;

    @Nullable
    private ViewRect viewRectInWindow;

    public ViewabilityObserver(
            @NonNull View view,
            @NonNull IListener listener
    ) {
        this.viewabilityCalc = new ViewabilityCalculator(view);
        this.appObserver = new AppAreaObserver(view, appListener);
        this.viewObserver = new ViewAreaObserver(view, viewListener, events);
        this.listener = listener;
    }

    @Nullable
    private RealViewRect lastRealViewRect;

    private void onLayout() {
        RealViewRect newRealViewRect;
        if (contentInDisplay == null || displaySize == null || contentGaps == null || viewRectInWindow == null) {
            newRealViewRect = null;
        } else {
            newRealViewRect = viewabilityCalc.getRealViewRect(
                    contentInDisplay,
                    displaySize,
                    contentGaps,
                    viewRectInWindow
            );
        }

        try {
            if (!ObjectsCompat.equals(lastRealViewRect, newRealViewRect)) {
                listener.onRealViewRectChanged(newRealViewRect);
            }
        } finally {
            lastRealViewRect = newRealViewRect;
        }
    }

    private void start() {
        viewabilityCalc.appCalc.areaCalculator.activity.getApplication()
                .registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    @Override
    public void close() {
        appObserver.close();
        viewObserver.close();
        viewabilityCalc.appCalc.areaCalculator.activity.getApplication()
                .unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    private void close(@NonNull Activity activity) {
        if (activity == viewabilityCalc.appCalc.areaCalculator.activity) {
            close();
        }
    }

    public interface IListener {

        void onRealViewRectChanged(@Nullable RealViewRect newValue);

    }

}
