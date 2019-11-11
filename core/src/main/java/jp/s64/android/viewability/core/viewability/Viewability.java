package jp.s64.android.viewability.core.viewability;

import android.support.annotation.NonNull;

public class Viewability {

    public final float viewability;
    public final boolean isPaused;
    public final boolean isWidgetStateShown;

    public Viewability(
            float viewability,
            boolean isPaused,
            boolean isWidgetStateShown
    ) {
        this.viewability = viewability;
        this.isPaused = isPaused;
        this.isWidgetStateShown = isWidgetStateShown;
    }

    // TODO: equals & hashCode

    @NonNull
    @Override
    public String toString() {
        return new StringBuilder()
                .append(viewability)
                .append(" (")
                .append("isPaused: ")
                .append(isPaused)
                .append(", ")
                .append("isWidgetStateShown: ")
                .append(isWidgetStateShown)
                .append(")")
                .toString();
    }

}
