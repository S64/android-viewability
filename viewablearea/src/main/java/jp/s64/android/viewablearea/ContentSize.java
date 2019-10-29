package jp.s64.android.viewablearea;

public class ContentSize implements ISize {

    private final int widthInPixels;
    private final int heightInPixels;

    public ContentSize(
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

        ContentSize that = (ContentSize) o;

        if (widthInPixels != that.widthInPixels) return false;
        return heightInPixels == that.heightInPixels;
    }

    @Override
    public int hashCode() {
        int result = widthInPixels;
        result = 31 * result + heightInPixels;
        return result;
    }

}
