package com.seguetech.zippy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.IPositiveButtonDialogListener;
import com.seguetech.zippy.R;
import com.seguetech.zippy.data.model.openfda.Result;
import com.seguetech.zippy.services.MedicineManagerService;

import timber.log.Timber;

public class MedicineActivity extends BaseActivity implements IPositiveButtonDialogListener {

    String cabinet = null;
    Result medicine = null;

    private static final int DELETE_CODE = 4;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            if (extras.containsKey("cabinet")) {
                cabinet = extras.getString("cabinet","unknown cabinet");
            }
            if (extras.containsKey("medicine")) {
                medicine = extras.getParcelable("medicine");
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (cabinet != null && medicine != null) {
            getMenuInflater().inflate(R.menu.menu_medicine, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.delete_medicine && cabinet != null && medicine != null) {
            SimpleDialogFragment.createBuilder(this,getSupportFragmentManager())
                    .setNegativeButtonText(android.R.string.cancel)
                    .setPositiveButtonText(android.R.string.ok).setRequestCode(DELETE_CODE).setTitle(R.string.confirm_delete_title).setMessage(String.format(getString(R.string.confirm_delete_message),cabinet)).show();
            Timber.w("Deleting medication from cabinet: " + cabinet);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPositiveButtonClicked(int i) {
        if (i == DELETE_CODE) {
            Intent deleteIntent = new Intent(this, MedicineManagerService.class);
            deleteIntent.putExtra(MedicineManagerService.ACTION_KEY, MedicineManagerService.ACTION_DELETE_MEDICINE);
            deleteIntent.putExtra(MedicineManagerService.CABINET_KEY, cabinet);
            deleteIntent.putExtra(MedicineManagerService.MEDICINE_KEY, medicine);
            startService(deleteIntent);
        }

    }
}
