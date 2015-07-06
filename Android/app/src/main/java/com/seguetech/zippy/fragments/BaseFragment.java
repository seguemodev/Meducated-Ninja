package com.seguetech.zippy.fragments;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

import com.seguetech.zippy.ZippyApplication;
import com.seguetech.zippy.ZippyEvents;

import de.greenrobot.event.EventBus;

public class BaseFragment extends Fragment {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().registerSticky(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ZippyApplication.getRefWatcher(getActivity()).watch(this);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(ZippyEvents.Message message) {
        View v = getView();
        if (v != null) {
            Snackbar.make(v, message.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

}
