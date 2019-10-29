package jp.s64.android.viewablearea;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public class ViewabilityObserver {

    @NonNull
    private final ViewPositionObserver viewObserver;

    /*
    @NonNull
    private final AppViewabilityObserver appObserver;
    */

    @NonNull
    private final ViewPositionObserver.IListener listener = new ViewPositionObserver.IListener() {

        @Override
        public void onViewRectChanged(@Nullable ViewRect oldViewRect, @NonNull ViewRect newViewRect) {
            ViewabilityObserver.this.onViewRectChanged(oldViewRect, newViewRect);
        }

    };

    @Nullable
    private ViewRect viewRect = null;

    public ViewabilityObserver(
            @NonNull View view
    ) {
        viewObserver = new ViewPositionObserver(view, listener);
        //appObserver = new AppViewabilityObserver(view);
    }

    private void onViewRectChanged(
            @Nullable ViewRect oldViewRect,
            @NonNull ViewRect newViewRect
    ) {
        this.viewRect = newViewRect;
        reconfigure();
    }

    private void reconfigure() {

    }

}
