package jp.s64.android.viewablearea;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public class ViewabilityObserverBak {

    @NonNull
    private final ViewPositionObserver viewObserver;

    @NonNull
    private final AppViewabilityObserver appObserver;

    @NonNull
    private final ViewPositionObserver.IListener listener = new ViewPositionObserver.IListener() {

        @Override
        public void onViewRectChanged(@Nullable ViewRect oldViewRect, @NonNull ViewRect newViewRect) {
            ViewabilityObserverBak.this.onViewRectChanged(oldViewRect, newViewRect);
        }

    };

    /*
    private final AppViewabilityObserver.IListener appListener = new AppViewabilityObserver.IListener() {

        @Override
        public void onAppViewabilityChanged(@Nullable AppViewability oldViewability, @NonNull AppViewability newViewability) {
            ViewabilityObserverBak.this.onAppViewabilityChanged(oldViewability, newViewability);
        }

    };
    */

    @Nullable
    private ViewRect viewRect = null;

    @Nullable
    private AppViewability contentArea = null;

    @Nullable
    private Viewability lastViewability = null;

    @NonNull
    private final IListener userListener;

    public ViewabilityObserverBak(
            @NonNull View view,
            @NonNull IListener listener
    ) {
        viewObserver = new ViewPositionObserver(view, this.listener);
        appObserver = /*new AppViewabilityObserver(view, appListener);*/ null;
        userListener = listener;
    }

    private void onViewRectChanged(
            @Nullable ViewRect oldViewRect,
            @NonNull ViewRect newViewRect
    ) {
        this.viewRect = newViewRect;
        reconfigure();
    }

    private void onAppViewabilityChanged(
            @Nullable AppViewability oldViewability,
            @NonNull AppViewability newViewability
    ) {
        this.contentArea = newViewability;
        reconfigure();
    }

    private void reconfigure() {
        if (viewRect == null || contentArea == null) {
            return;
        }

        int realViewPositionLeftInDisplay = contentArea.getContentInDisplay().left + viewRect.left;
        int realViewPositionTopInDisplay = contentArea.getContentInDisplay().top + viewRect.top;
        int realViewPositionRightInDisplay = realViewPositionLeftInDisplay + viewRect.getWidthInPixels();
        int realViewPositionBottomInDisplay = realViewPositionTopInDisplay + viewRect.getHeightInPixels();

        Viewability newViewability = new Viewability(
                new ViewRect(
                        realViewPositionLeftInDisplay,
                        realViewPositionTopInDisplay,
                        realViewPositionRightInDisplay,
                        realViewPositionBottomInDisplay
                )
        );

        try {
            if (!Utils.objectsEquals(lastViewability, newViewability)) {
                userListener.onViewabilityChanged(
                        lastViewability,
                        newViewability
                );
            }
        } finally {
            lastViewability = newViewability;
        }
    }

    public interface IListener {

        void onViewabilityChanged(
                @Nullable Viewability oldViewability,
                @NonNull Viewability newViewability
        );

    }

}
