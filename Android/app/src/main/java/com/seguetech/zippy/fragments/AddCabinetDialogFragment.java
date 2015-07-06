package com.seguetech.zippy.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.avast.android.dialogs.core.BaseDialogFragment;
import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.INegativeButtonDialogListener;
import com.seguetech.zippy.R;

import java.util.List;

public class AddCabinetDialogFragment extends SimpleDialogFragment {
    private static final String TAG = AddCabinetDialogFragment.class.getName();

    public static void show(FragmentActivity activity, Fragment fragment, int requestCode) {
        AddCabinetDialogFragment addCabinetDialogFragment = new AddCabinetDialogFragment();
        addCabinetDialogFragment.setTargetFragment(fragment, requestCode);
        addCabinetDialogFragment.show(activity.getSupportFragmentManager(), TAG);
    }

    protected List<IInputPositiveButtonDialogListener> getInputPositiveButtonDialogListeners() {
        return getDialogListeners(IInputPositiveButtonDialogListener.class);
    }

    @Override
    public BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder) {
        builder.setTitle(R.string.add_cabinet);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.add_cabinet, null, false);
        builder.setView(v);
        final EditText inputView = (EditText) v.findViewById(R.id.input);
        builder.setNegativeButton(android.R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (INegativeButtonDialogListener listener : getNegativeButtonDialogListeners()) {
                    listener.onNegativeButtonClicked(mRequestCode);
                }
                dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.add), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (IInputPositiveButtonDialogListener listener : getInputPositiveButtonDialogListeners()) {
                    listener.onPositiveButtonClicked(mRequestCode, inputView.getText().toString().trim());
                }
                dismiss();
            }
        });
        return builder;
    }
}
