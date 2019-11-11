package jp.s64.android.viewability;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public class ViewUtils {

    @Nullable
    public static Activity getActivity(@NonNull View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    @NonNull
    public static Activity requireActivity(@NonNull View view) {
        Activity ret = getActivity(view);
        if (ret == null) {
            throw new IllegalStateException("Activity not found.");
        }
        return ret;
    }

}
