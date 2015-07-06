package com.seguetech.zippy.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seguetech.zippy.R;
import com.seguetech.zippy.ZippyApplication;
import com.seguetech.zippy.ZippyEvents;
import com.seguetech.zippy.data.model.openfda.Result;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

/**
 * This service is responsible for handling all communications with file system cabinet storage.
 */
public class MedicineManagerService extends IntentService {

    public static final String TAG = MedicineManagerService.class.getName();

    public static final String ACTION_KEY = TAG + "_ACTION";
    public static final String CABINET_KEY = TAG + "_CABINET";
    public static final String MEDICINE_KEY = TAG + "_MEDICINE";
    public static final String CABINET_REQUESTED_NAME_KEY = TAG + "_CABINET_REQUESTED_NAME";

    public static final int ACTION_READ = 0;
    public static final int ACTION_ADD = 1;
    public static final int ACTION_DELETE_MEDICINE = 2;
    public static final int ACTION_RENAME = 3;
    public static final int ACTION_DELETE_CABINET = 4;

    // lock object is used to ensure we don't attempt to read/write from a cabinet.
    // we could use a pool of locks for per-file locks, but collisions should be rare anyway.
    private static final Object fileLock = new Object();

    @Inject
    Gson gson;

    public MedicineManagerService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((ZippyApplication)getApplication()).inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int action = intent.getIntExtra(ACTION_KEY,-1);
        if (action < 0) {
            Timber.w("Service did not receive a proper ACTION_KEY.");
        }

        switch(action) {
            case ACTION_READ:
                read(intent);
                break;
            case ACTION_ADD:
                add(intent);
                break;
            case ACTION_DELETE_MEDICINE:
                deleteMedicine(intent);
                break;
            case ACTION_DELETE_CABINET:
                deleteCabinet(intent);
                break;
            case ACTION_RENAME:
                rename(intent);
        }
    }

    private void read(Intent intent) {
    }

    private void add(Intent intent) {
        if (!intent.hasExtra(CABINET_KEY) || !intent.hasExtra(MEDICINE_KEY)) {
            Timber.w("A required extra is missing.");
            return;
        }

        String cabinet = intent.getStringExtra(CABINET_KEY).trim();
        Result result = intent.getParcelableExtra(MEDICINE_KEY);

        if (result == null || cabinet.trim().length() < 1) {
            Timber.w("Invalid intent parameters.");
            return;
        }

        cabinet = createCabinetIfNeeded(cabinet);

        ArrayList<Result> results = new ArrayList<>();

        boolean alreadyAdded = false;
        boolean success = true;

        synchronized (fileLock) {
            Writer writer = null;
            try {
                File outFile = new File(getCabinetDirectory() + "/" + getCabinetFileName(cabinet));

                if (!outFile.exists()) {
                    Timber.w("File doesn't exist, saving.");
                    results.add(result);
                } else {
                    // read the data in, add the new entry, save it.
                    Type resultsIn = new TypeToken<ArrayList<Result>>() {
                    }.getType();
                    results = gson.fromJson(new FileReader(outFile), resultsIn);
                    if (!results.contains(result)) {
                        results.add(result);
                    } else {
                        alreadyAdded = true;
                        // we already have that entry, nothing to do.
                        results = null;
                    }
                }

                if (results != null) {
                    writer = new FileWriter(outFile);
                    gson.toJson(results, writer);
                }

            } catch (Exception e) {
                success = false;
                Timber.e(e, "Couldn't save medicine to cabinet.");
            } finally {
                if (writer != null) {
                    try {
                        writer.flush();
                        writer.close();
                    } catch (Exception e) {
                        Timber.d("An error occurred closing the OutputStreamWriter.");
                    }
                }
            }
        }

        if (success) {
            if (alreadyAdded) {
                EventBus.getDefault().post(new ZippyEvents.Message(TAG, String.format(getString(R.string.already_in_cabinet), result.getOpenfda().getBrandName().get(0), cabinet)));
            }
            else {
                EventBus.getDefault().post(new ZippyEvents.Message(TAG, String.format(getString(R.string.saved_medicine), result.getOpenfda().getBrandName().get(0), cabinet)));
            }
        }
        else {
            EventBus.getDefault().post(new ZippyEvents.Message(TAG, String.format(getString(R.string.save_medicine_failed), result.getOpenfda().getBrandName().get(0), cabinet)));
        }

    }

    /**
     * Deltes a medication from a cabinet.
     * @param intent incoming intent.
     */
    private void deleteMedicine(Intent intent) {
        if (!intent.hasExtra(CABINET_KEY) || !intent.hasExtra(MEDICINE_KEY)) {
            Timber.w("A required extra is missing.");
            return;
        }

        String cabinet = intent.getStringExtra(CABINET_KEY).trim();
        Result result = intent.getParcelableExtra(MEDICINE_KEY);

        if (result == null || cabinet.trim().length() < 1) {
            Timber.w("Invalid intent parameters.");
            return;
        }

        synchronized (fileLock) {
            Writer writer = null;
            try {
                File outFile = new File(getCabinetDirectory() + "/" + getCabinetFileName(cabinet));
                if (outFile.exists()) {
                    // read the data in, add the new entry, save it.
                    Type resultsIn = new TypeToken<ArrayList<Result>>(){}.getType();
                    ArrayList<Result> results = gson.fromJson(new FileReader(outFile),resultsIn);
                    if (results.contains(result)) {
                        results.remove(result);
                        writer = new FileWriter(outFile);
                        gson.toJson(results, writer);
                        EventBus.getDefault().post(new ZippyEvents.Message(TAG,String.format(getString(R.string.deleted_medicine), result.getOpenfda().getBrandName().get(0), cabinet)));
                    }

                }

            }
            catch (Exception e) {
                Timber.e(e, "Couldn't delete medicine from cabinet.");
            }
            finally {
                if (writer != null) {
                    try {
                        writer.flush();
                        writer.close();
                    }
                    catch (Exception e) {
                        Timber.d("An error occurred closing the OutputStreamWriter.");
                        EventBus.getDefault().post(new ZippyEvents.Message(TAG,String.format(getString(R.string.delete_medicine_failed),result.getOpenfda().getBrandName().get(0),cabinet)));
                    }
                }

            }
        }

    }

    /**
     * Deletes a cabinet and it's associated file.
     * @param intent incoming intent.
     */
    private void deleteCabinet(Intent intent) {

    }

    /**
     * Renames a cabinet and it's data file.
     * @param intent incoming intent.
     */
    private void rename(Intent intent) {
        if (!intent.hasExtra(CABINET_KEY) || !intent.hasExtra(CABINET_REQUESTED_NAME_KEY)) {
            Timber.w("A required extra is missing.");
            return;
        }

        String cabinet = intent.getStringExtra(CABINET_KEY).trim();
        String newName = intent.getStringExtra(CABINET_REQUESTED_NAME_KEY).trim();

        if (cabinet.trim().length() < 1 || newName.trim().length() < 1) {
            Timber.w("Invalid intent parameters.");
            return;
        }


        String newCabinet = createCabinet(newName);

        // keep track of success so we can send the event outside of the file lock.
        boolean success = true;

        synchronized (fileLock) {
            try {
                File oldFile = new File(getCabinetDirectory() + "/" + getCabinetFileName(cabinet));
                if (oldFile.exists()) {
                    File newFile = new File(getCabinetDirectory() + "/" + getCabinetFileName(newCabinet));
                    copyFile(oldFile,newFile);
                }
                deleteCabinet(cabinet);
            }
            catch (Exception e) {
                success = false;
                Timber.e(e, "Failed to delete cabinet.");
            }
        }

        if (success) {
            EventBus.getDefault().post(new ZippyEvents.CabinetRenamed(cabinet, newCabinet));
        }


    }

    /**
     * Deletes a cabinet and it's associated data file.
     * @param cabinet the cabinet to delete.
     */
    private void deleteCabinet(String cabinet) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        HashSet<String> cabinets = new HashSet<>();
        cabinets.addAll(sharedPreferences.getStringSet("cabinets",new HashSet<String>()));
        cabinets.remove(cabinet);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("cabinets",cabinets);
        boolean committed = editor.commit();
        if (committed) {
            Timber.d("Deleted Cabinet: " + cabinet);
        }
        synchronized (fileLock) {
            try {
                File cabinetFile = new File(getCabinetDirectory() + "/" + getCabinetFileName(cabinet));
                if (cabinetFile.exists()) {
                    boolean deleted = cabinetFile.delete();
                }
            }
            catch (Exception e) {
                Timber.e(e,"Failed to delete cabinet data file for " + cabinet);
            }
        }
    }


    /**
     * Creates a cabinet.
     * @param cabinet the requested cabinet name.
     * @return the actual cabinet created.
     */
    private String createCabinet(String cabinet) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        HashSet<String> cabinets = new HashSet<>();
        cabinets.addAll(sharedPreferences.getStringSet("cabinets",new HashSet<String>()));
        String originalCabinet = cabinet;
        int i = 0;
        while (cabinets.contains(cabinet)) {
            cabinet = originalCabinet + " (" + ++i + ")";
        }
        cabinets.add(cabinet);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("cabinets",cabinets);
        boolean committed = editor.commit();
        if (committed) {
            EventBus.getDefault().post(new ZippyEvents.Message(TAG,String.format(getString(R.string.created_cabinet),cabinet)));
        }
        return cabinet;
    }

    /**
     * Creates a cabinet, but only if it doesn't already exist.
     * @param cabinet the requested cabinet name.
     * @return the cabinet name created.
     */
    private String createCabinetIfNeeded(String cabinet) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        HashSet<String> cabinets = new HashSet<>();
        cabinets.addAll(sharedPreferences.getStringSet("cabinets",new HashSet<String>()));
        if (!cabinets.contains(cabinet)) {
            cabinet = createCabinet(cabinet);
        }
        return cabinet;
    }

    /**
     * The name of the cabinet file.
     * @param cabinet the cabinet to get the file for.
     * @return the cabinet file name.
     * @throws Exception an error.
     */
    private String getCabinetFileName(String cabinet) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return Base64.encodeToString(md.digest(cabinet.getBytes()), Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING) + ".cabinet";
    }

    /**
     * Gets the cabinet directory from the android data dir.
     * @return
     * @throws Exception
     */
    private File getCabinetDirectory() throws Exception {
        File cabinetDirectory = new File(getFilesDir(),"cabinets");
        if (!cabinetDirectory.exists()) {
            //noinspection ResultOfMethodCallIgnored
            cabinetDirectory.mkdirs();
        }
        return cabinetDirectory;
    }

    private static void copyFile(File sourceFile, File destFile) throws IOException {
        if(!destFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        }
        finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
        }
    }
}
