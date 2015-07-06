package com.seguetech.zippy.fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.INegativeButtonDialogListener;
import com.seguetech.zippy.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectCabinetDialogFragment extends SimpleDialogFragment {
    private static final String TAG = SelectCabinetDialogFragment.class.getName();

    public static void show(Fragment fragment, int requestCode) {
        SelectCabinetDialogFragment selectCabinetDialogFragment = new SelectCabinetDialogFragment();
        selectCabinetDialogFragment.setTargetFragment(fragment, requestCode);
        selectCabinetDialogFragment.show(fragment.getActivity().getSupportFragmentManager(),TAG);
    }

    protected List<IInputPositiveButtonDialogListener> getInputPositiveButtonDialogListeners() {
        return getDialogListeners(IInputPositiveButtonDialogListener.class);
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        super.onShow(dialogInterface);
        View v = getView();
        if (v != null) {
            AutoCompleteTextView inputView = (AutoCompleteTextView)v.findViewById(R.id.input);
            if (inputView != null) {
                inputView.showDropDown();
            }
        }
    }

    @Override
    public Builder build(Builder builder) {

        builder.setTitle(R.string.select_cabinet);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.select_cabinet, null, false);
        builder.setView(v);
        final AutoCompleteTextView inputView = (AutoCompleteTextView) v.findViewById(R.id.input);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Set<String> stringSet = sharedPreferences.getStringSet("cabinets", new HashSet<String>());
        ArrayList<String> cabinets = new ArrayList<>(Arrays.asList(stringSet.toArray(new String[stringSet.size()])));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),R.layout.simple_list_item,cabinets);
        inputView.setAdapter(adapter);
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
