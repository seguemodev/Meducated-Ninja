package com.seguetech.zippy.fragments;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.seguetech.zippy.R;
import com.seguetech.zippy.ZippyEvents;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

public class SortDialogFragment extends SimpleDialogFragment implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = SortDialogFragment.class.getName();

    private CheckBox ascending;
    private CheckBox descending;
    private CheckBox brandName;
    private CheckBox genericName;

    public static void show(FragmentActivity activity, int requestCode) {
        SortDialogFragment sortDialogFragment = new SortDialogFragment();
        sortDialogFragment.setTargetFragment(null, requestCode);
        sortDialogFragment.show(activity.getSupportFragmentManager(), TAG);
    }

    @Override
    public Builder build(Builder builder) {
        builder.setTitle(R.string.sort_settings);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.sort_settings, null, false);
        ascending = ((CheckBox)v.findViewById(R.id.ascending));
        descending = ((CheckBox)v.findViewById(R.id.descending));
        brandName = ((CheckBox)v.findViewById(R.id.brand_name));
        genericName = ((CheckBox)v.findViewById(R.id.generic_name));
        ascending.setOnCheckedChangeListener(this);
        descending.setOnCheckedChangeListener(this);
        brandName.setOnCheckedChangeListener(this);
        genericName.setOnCheckedChangeListener(this);

        builder.setView(v);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (preferences.getString("sort_direction","asc").equals("asc")) {
            ascending.setChecked(true);
        }
        else {
            descending.setChecked(true);
        }
        if (preferences.getString("sort_by","brand_name").equals("brand_name")) {
            brandName.setChecked(true);
        }
        else {
            genericName.setChecked(true);
        }

        builder.setNegativeButton(android.R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setPositiveButton(android.R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sortDirection = "asc";
                if (descending.isChecked()) {
                    sortDirection = "dsc";
                }

                String sortBy = "brand_name";
                if (genericName.isChecked()) {
                    sortBy = "generic_name";
                }

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("sort_direction",sortDirection);
                editor.putString("sort_by",sortBy);
                boolean committed = editor.commit();
                if (committed) {
                    EventBus.getDefault().post(new ZippyEvents.SortChanged(sortDirection, sortBy));
                }
                else {
                    Timber.w("Failed to commit sort settings.");
                }

                dismiss();
            }
        });
        return builder;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.ascending:
                if (isChecked && descending.isChecked()) {
                    descending.setChecked(false);
                }
                if (!isChecked && !descending.isChecked()) {
                    descending.setChecked(true);
                }
                break;
            case R.id.descending:
                if (isChecked && ascending.isChecked()) {
                    ascending.setChecked(false);
                }
                if (!isChecked && !ascending.isChecked()) {
                    ascending.setChecked(true);
                }
                break;
            case R.id.brand_name:
                if (isChecked && genericName.isChecked()) {
                    genericName.setChecked(false);
                }
                if (!isChecked && !genericName.isChecked()) {
                    genericName.setChecked(true);
                }
                break;
            case R.id.generic_name:
                if (isChecked && brandName.isChecked()) {
                    brandName.setChecked(false);
                }
                if (!isChecked && !brandName.isChecked()) {
                    brandName.setChecked(true);
                }
                break;
        }
    }
}
