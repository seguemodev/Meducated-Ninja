package com.seguetech.zippy;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

public class EspressoUtils {
    private EspressoUtils() {
    }

    public static Activity getActivity(View view) {
        Context context = view.getContext();
        while (!(context instanceof Activity)) {
            if (context instanceof ContextWrapper) {
                context = ((ContextWrapper) context).getBaseContext();
            } else {
                throw new IllegalStateException("Got a context of class "
                        + context.getClass()
                        + " and I don't know how to get the Activity from it");
            }
        }
        return (Activity) context;
    }
}
