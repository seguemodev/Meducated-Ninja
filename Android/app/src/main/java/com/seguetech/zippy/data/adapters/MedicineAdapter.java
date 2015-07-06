package com.seguetech.zippy.data.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seguetech.zippy.R;
import com.seguetech.zippy.Utils;
import com.seguetech.zippy.activities.MedicineActivity;
import com.seguetech.zippy.data.model.openfda.Result;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.util.ArrayList;

import timber.log.Timber;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineHolder> {
    private String cabinet;
    private File cabinetDirectory;
    private volatile ArrayList<Result> medicines = new ArrayList<>();

    public MedicineAdapter() {
    }

    public MedicineAdapter(Context context, String cabinet) {
        cabinetDirectory = new File(context.getFilesDir(),"cabinets");
        this.cabinet = cabinet;
        loadMedicines();
    }

    public void setCabinet(String cabinet) {
        Timber.w("Setting cabinet to " + cabinet);
        this.cabinet = cabinet;
        loadMedicines();
    }

    public String getCabinet() {
        return this.cabinet;
    }

    public void loadMedicines() {
        new LoadMedicinesTask().execute();
    }

    @Override
    public MedicineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_card, parent, false);
        return new MedicineHolder(v);
    }

    @Override
    public void onBindViewHolder(MedicineHolder holder, int position) {
        holder.bind(medicines.get(position));
    }

    @Override
    public int getItemCount() {
        if (medicines != null) {
            return medicines.size();
        }
        return 0;
    }

    public class MedicineHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView brandName;
        TextView genericName;
        //TextView manufacturerName;
        //TextView ndcs;
        Result result;

        public MedicineHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            brandName = (TextView) itemView.findViewById(R.id.brand_name);
            genericName = (TextView) itemView.findViewById(R.id.generic_name);
            //manufacturerName = (TextView) itemView.findViewById(R.id.manufacturer);
            //ndcs = (TextView) itemView.findViewById(R.id.ndcs);
        }


        public void bind(Result result) {
            brandName.setText(Utils.listToString(result.getOpenfda().getBrandName()));
            genericName.setText(Utils.listToString(result.getOpenfda().getGenericName()));
            //manufacturerName.setText(listToString(result.getOpenfda().getManufacturerName()));
            //ndcs.setText(listToString(result.getOpenfda().getPackageNdc()));
            this.result = result;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), MedicineActivity.class);
            intent.putExtra("medicine",result);
            intent.putExtra("cabinet",cabinet);
            v.getContext().startActivity(intent);
        }
    }

    // TODO: move the loading into the MedicineManagerService so thread safety ont he cabinet files is guaranteed.
    private class LoadMedicinesTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            medicines = new ArrayList<>();
            notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Gson gson = new Gson();
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                if (!cabinetDirectory.exists()) {
                    return null;
                }
                File inFile = new File(cabinetDirectory + "/" + Base64.encodeToString(md.digest(cabinet.getBytes()), Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING) + ".cabinet");
                if (!inFile.exists()) {
                    return null;
                }
                Type resultsIn = new TypeToken<ArrayList<Result>>(){}.getType();
                medicines = gson.fromJson(new FileReader(inFile),resultsIn);
            }
            catch (Exception e) {
                Timber.e(e, "Failed to load files.");
            }
            return null;
        }

        @Override
        public void onPostExecute(Void result) {
            MedicineAdapter.this.notifyDataSetChanged();
        }
    }
}
