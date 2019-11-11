package jp.s64.android.viewability.core;

import jp.s64.android.viewability.core.IDimension;

public interface IRect extends IDimension {

    int getLeftInPixels();

    int getTopInPixels();

    int getRightInPixels();

    int getBottomInPixels();

}
