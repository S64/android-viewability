package jp.s64.android.viewablearea;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

public class Utils {

    // Backport of Java7's `Objects::equals`
    public static boolean objectsEquals(Object a, Object b) {
        if (a == b) {
            return true;
        } else if (a == null || b == null) {
            return false;
        }

        return a.equals(b);
    }

}
