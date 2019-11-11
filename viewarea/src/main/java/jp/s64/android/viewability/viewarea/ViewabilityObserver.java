package jp.s64.android.viewability.viewarea;

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
import jp.s64.android.viewability.core.rect.RealWidgetRect;
import jp.s64.android.viewability.core.rect.WidgetRectInContent;
import jp.s64.android.viewability.core.rect.WidgetRectInDisplay;
import jp.s64.android.viewability.core.rect.WidgetRectInWindow;
import jp.s64.android.viewability.core.rect.WindowRect;
import jp.s64.android.viewability.apparea.AppAreaObserver;

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
        public void onWindowRectChanged(@NonNull WindowRect newValue) {
            // no-op
        }

        @Override
        public void onSystemGapsChanged(@Nullable SystemGaps newValue) {
            // no-op
        }

        @Override
        public void onContentGapsChanged(@NonNull ContentGaps newValue) {
            ViewabilityObserver.this.contentGaps = newValue;
        }

        @Override
        public void onContentInDisplayChanged(@Nullable ContentRect newValue) {
            ViewabilityObserver.this.contentInDisplay = newValue;
        }

        @Override
        public void onContentInWindowChanged(@Nullable ContentRect newValue) {
            // no-op
        }

    };

    private final ViewAreaObserver.IListener viewListener = new ViewAreaObserver.IListener() {

        @Override
        public void onViewRectInContentChanged(@Nullable WidgetRectInContent newValue) {
            // no-op
        }

        @Override
        public void onViewRectInWindowChanged(@Nullable WidgetRectInWindow newValue) {
            ViewabilityObserver.this.widgetRectInWindow = newValue;
        }

        @Override
        public void onViewRectInDisplayChanged(@NonNull WidgetRectInDisplay newValue) {
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
    private WidgetRectInWindow widgetRectInWindow;

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
    private RealWidgetRect lastRealWidgetRect;

    private void onLayout() {
        RealWidgetRect newRealWidgetRect;
        if (contentInDisplay == null || displaySize == null || contentGaps == null || widgetRectInWindow == null) {
            newRealWidgetRect = null;
        } else {
            newRealWidgetRect = viewabilityCalc.getRealViewRect(
                    contentInDisplay,
                    displaySize,
                    contentGaps,
                    widgetRectInWindow
            );
        }

        try {
            if (!ObjectsCompat.equals(lastRealWidgetRect, newRealWidgetRect)) {
                listener.onRealViewRectChanged(newRealWidgetRect);
            }
        } finally {
            lastRealWidgetRect = newRealWidgetRect;
        }
    }

    private void start() {
        viewabilityCalc.requireActivity().getApplication()
                .registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    @Override
    public void close() {
        appObserver.close();
        viewObserver.close();
        viewabilityCalc.requireActivity().getApplication()
                .unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    private void close(@NonNull Activity activity) {
        if (activity == viewabilityCalc.requireActivity()) {
            close();
        }
    }

    public interface IListener {

        void onRealViewRectChanged(@Nullable RealWidgetRect newValue);

    }

}
