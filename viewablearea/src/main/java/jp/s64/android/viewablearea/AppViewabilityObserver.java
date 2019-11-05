package jp.s64.android.viewablearea;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public class AppViewabilityObserver {

    @NonNull
    private final AppAreaObserverBak areaObserver;

    @Nullable
    private DisplaySize displaySize = null;

    @Nullable
    private WindowRect windowRect = null;

    @Nullable
    private ContentSize contentSize = null;

    @Nullable
    private AppViewability lastViewability = null;

    @NonNull
    private final AppAreaObserverBak.IListener areaListener = new AppAreaObserverBak.IListener() {

        @Override
        public void onDisplaySizeChanged(
                @Nullable DisplaySize oldDisplaySize,
                @NonNull DisplaySize newDisplaySize
        ) {
            AppViewabilityObserver.this.onDisplaySizeChanged(oldDisplaySize, newDisplaySize);
        }

        @Override
        public void onWindowRectChanged(
                @Nullable WindowRect oldWindowRect,
                @NonNull WindowRect newWindowRect
        ) {
            AppViewabilityObserver.this.onWindowRectChanged(oldWindowRect, newWindowRect);
        }

        @Override
        public void onSystemGapsChanged(@Nullable SystemGaps oldSystemGaps, @Nullable SystemGaps newSystemGaps) {
            // TODO
        }

        @Override
        public void onContentGapsChanged(@Nullable ContentGaps oldContentGaps, @NonNull ContentGaps newContentGaps) {
            // TODO
        }

        @Override
        public void onContentSizeInDisplayChanged(@Nullable ContentSize oldContentSizeInDisplay, @Nullable ContentSize newContentSizeInDisplay) {
            // TODO
        }

        @Override
        public void onContentSizeInWindowChanged(@Nullable ContentSize lastContentSizeInWindow, @NonNull ContentSize newContentSizeInWindow) {
            // TODO
        }

        /*
        @Override
        public void onContentSizeChanged(
                @Nullable ContentSize oldContentSize,
                @NonNull ContentSize newContentSize
        ) {
            AppViewabilityObserver.this.onContentSizeChanged(oldContentSize, newContentSize);
        }
        */

    };

    @NonNull
    private IListener userListener;

    public AppViewabilityObserver(
            @NonNull Activity activity,
            @NonNull IListener listener
    ) {
        areaObserver = new AppAreaObserverBak(activity, areaListener);
        userListener = listener;
    }

    public AppViewabilityObserver(
            @NonNull View view,
            @NonNull IListener listener
    ) {
        areaObserver = new AppAreaObserverBak(view, areaListener);
        userListener = listener;
    }

    private void onDisplaySizeChanged(
            @Nullable DisplaySize oldDisplaySize,
            @NonNull DisplaySize newDisplaySize
    )
    {
        this.displaySize = newDisplaySize;
        reconfigured();
    }

    private void onWindowRectChanged(
            @Nullable WindowRect oldWindowRect,
            @NonNull WindowRect newWindowRect
    )
    {
        this.windowRect = newWindowRect;
        reconfigured();
    }

    private void onContentSizeChanged(
            @Nullable ContentSize lastContentSize,
            @NonNull ContentSize newContentSize
    )
    {
        this.contentSize = newContentSize;
        reconfigured();
    }

    private void reconfigured() {
        if (displaySize == null || windowRect == null || contentSize == null) {
            return;
        }

        int realContentAreaLeftInDisplay = windowRect.left + contentSize.left;
        int realContentAreaTopInDisplay = windowRect.top + contentSize.top;
        int realContentAreaRightInDisplay = realContentAreaLeftInDisplay + contentSize.getWidthInPixels();
        int realContentAreaBottomInDisplay = realContentAreaTopInDisplay + contentSize.getHeightInPixels();

        AppViewability newViewability = new AppViewability(
                new ContentSize(
                        realContentAreaLeftInDisplay,
                        realContentAreaTopInDisplay,
                        realContentAreaRightInDisplay,
                        realContentAreaBottomInDisplay
                )
        );

        try {
            if (!Utils.objectsEquals(lastViewability, newViewability)) {
                userListener.onAppViewabilityChanged(
                        lastViewability,
                        newViewability
                );
            }
        } finally {
            lastViewability = newViewability;
        }
    }

    public interface IListener {

        void onAppViewabilityChanged(
                @Nullable AppViewability oldViewability,
                @NonNull AppViewability newViewability
        );

    }

}
