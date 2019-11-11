package jp.s64.android.viewability.core.dimension;

import android.support.annotation.NonNull;
import android.support.v4.util.ObjectsCompat;

import jp.s64.android.viewability.core.IDimension;

class BaseDimension implements IDimension {

    private final int widthInPixels;
    private final int heightInPixels;

    protected BaseDimension(
            int widthInPixels,
            int heightInPixels
    ) {
        this.widthInPixels = widthInPixels;
        this.heightInPixels = heightInPixels;
    }

    @Override
    public int getWidthInPixels() {
        return widthInPixels;
    }

    @Override
    public int getHeightInPixels() {
        return heightInPixels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseDimension that = (BaseDimension) o;
        return getWidthInPixels() == that.getWidthInPixels() &&
                getHeightInPixels() == that.getHeightInPixels();
    }

    @Override
    public int hashCode() {
        return ObjectsCompat.hash(getWidthInPixels(), getHeightInPixels());
    }

    @NonNull
    @Override
    public String toString() {
        return new StringBuilder()
                .append(widthInPixels)
                .append("x")
                .append(heightInPixels)
                .toString();
    }

}
