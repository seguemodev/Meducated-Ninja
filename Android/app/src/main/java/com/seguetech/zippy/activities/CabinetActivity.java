package com.seguetech.zippy.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.ISimpleDialogListener;
import com.seguetech.zippy.R;
import com.seguetech.zippy.ZippyEvents;
import com.seguetech.zippy.fragments.CabinetFragment;
import com.seguetech.zippy.fragments.EditCabinetDialogFragment;
import com.seguetech.zippy.fragments.IInputPositiveButtonDialogListener;
import com.seguetech.zippy.services.MedicineManagerService;

import java.util.HashSet;
import java.util.Set;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

public class CabinetActivity extends BaseActivity implements ISimpleDialogListener,IInputPositiveButtonDialogListener {

    private static final int DELETE_CODE = 0;
    private static final int EDIT_CODE = 1;

    private String cabinet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey("cabinet")) {
            cabinet = savedInstanceState.getString("cabinet", "Unknown Cabinet");
        }
        if (cabinet == null && getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey("cabinet")) {
            cabinet = getIntent().getExtras().getString("cabinet","Unknown Cabinet");
        }
        if (cabinet == null) {
            cabinet = "Unknown Cabinet";
        }
        setTitle(cabinet);
        setContentView(R.layout.activity_cabinet);
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (f instanceof CabinetFragment) {
            ((CabinetFragment)f).setCabinet(cabinet);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (cabinet != null) {
            outState.putString("cabinet", cabinet);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cabinet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.delete_cabinet:
                SimpleDialogFragment.createBuilder(this, getSupportFragmentManager())
                        .setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to delete '" + cabinet + "'?")
                        .setPositiveButtonText("YES")
                        .setNegativeButtonText("NO")
                        .setRequestCode(DELETE_CODE)
                        .show();
                break;
            case R.id.edit_cabinet:
                EditCabinetDialogFragment.show(this,EDIT_CODE,cabinet);
                break;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNegativeButtonClicked(int i) {

    }

    @Override
    public void onNeutralButtonClicked(int i) {

    }

    @Override
    public void onPositiveButtonClicked(int i) {
        if (i == DELETE_CODE) {
            Intent intent = new Intent(this,MedicineManagerService.class);
            intent.putExtra(MedicineManagerService.ACTION_KEY,MedicineManagerService.ACTION_DELETE_CABINET);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            Set<String> cabinets = new HashSet<>();
            cabinets.addAll(preferences.getStringSet("cabinets", new HashSet<String>()));
            cabinets.remove(cabinet);
            SharedPreferences.Editor e = preferences.edit();
            e.putStringSet("cabinets",cabinets);
            boolean updated = e.commit();
            if (updated) {
                finish();
            }
        }
        Timber.d("Request Code");
    }

    // TODO: will need to rename the stored .json file here as well.
    @Override
    public void onPositiveButtonClicked(int requestCode, String input) {
        if (input != null && input.trim().length() > 0 && !input.equals(cabinet)) {
            Intent intent = new Intent(this, MedicineManagerService.class);
            intent.putExtra(MedicineManagerService.ACTION_KEY, MedicineManagerService.ACTION_RENAME);
            intent.putExtra(MedicineManagerService.CABINET_KEY, cabinet);
            intent.putExtra(MedicineManagerService.CABINET_REQUESTED_NAME_KEY, input);
            startService(intent);
        }
    }

    public void onEventMainThread(ZippyEvents.CabinetRenamed renamed) {
        if (renamed.getOriginalName().equals(cabinet)) {
            cabinet = renamed.getNewName();
            setTitle(cabinet);
        }
    }
}
