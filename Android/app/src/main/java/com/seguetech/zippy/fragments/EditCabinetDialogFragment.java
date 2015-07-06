package com.seguetech.zippy.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.avast.android.dialogs.core.BaseDialogFragment;
import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.INegativeButtonDialogListener;
import com.seguetech.zippy.R;

import java.util.List;

public class EditCabinetDialogFragment extends SimpleDialogFragment {
    private static final String TAG = EditCabinetDialogFragment.class.getName();

    public static void show(FragmentActivity activity, int requestCode, String value) {
        EditCabinetDialogFragment fragment = new EditCabinetDialogFragment();
        Bundle args = new Bundle();
        args.putString("init",value);
        fragment.setArguments(args);
        fragment.setTargetFragment(null, requestCode);
        fragment.show(activity.getSupportFragmentManager(),TAG);
    }


    protected List<IInputPositiveButtonDialogListener> getInputPositiveButtonDialogListeners() {
        return getDialogListeners(IInputPositiveButtonDialogListener.class);
    }

    @Override
    public BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder) {

        builder.setTitle(R.string.add_cabinet);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.edit_cabinet, null, false);
        builder.setView(v);
        final EditText inputView = (EditText) v.findViewById(R.id.input);
        inputView.setText(getArguments().getString("init"));
        builder.setNegativeButton(getString(R.string.cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (INegativeButtonDialogListener listener : getNegativeButtonDialogListeners()) {
                    listener.onNegativeButtonClicked(mRequestCode);
                }
                dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.save), new View.OnClickListener() {
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
