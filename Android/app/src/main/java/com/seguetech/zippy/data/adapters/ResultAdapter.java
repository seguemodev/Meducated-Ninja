package com.seguetech.zippy.data.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seguetech.zippy.R;
import com.seguetech.zippy.Utils;
import com.seguetech.zippy.activities.MedicineActivity;
import com.seguetech.zippy.data.model.openfda.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultHolder> {

    private ArrayList<Result> results = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    public ResultAdapter(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }


    public void setResults(List<Result> results) {
        if (results != null) {
            this.results.clear();
            this.results.addAll(results);
            sort();
        }
    }

    // TODO: move the sorting operation into an async task.
    public void sort() {
        final String sortDirection = sharedPreferences.getString("sort_direction","asc");
        final String sortBy = sharedPreferences.getString("sort_by","brand_name");
        Collections.sort(results, new Comparator<Result>() {
            @Override
            public int compare(Result o1, Result o2) {
                int comparison;
                String left;
                String right;

                if (sortBy.equals("brand_name"))
                    comparison = Utils.listToString(o1.getOpenfda().getBrandName()).compareToIgnoreCase(Utils.listToString(o2.getOpenfda().getBrandName()));
                else
                    comparison = Utils.listToString(o1.getOpenfda().getGenericName()).compareToIgnoreCase(Utils.listToString(o2.getOpenfda().getGenericName()));

                if (sortDirection.equals("dsc")) {
                    comparison = comparison*-1;
                }
                return comparison;
            }
        });

        notifyDataSetChanged();
    }

    @Override
    public ResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_card, parent, false);
        return new ResultHolder(v);
    }

    @Override
    public void onBindViewHolder(ResultHolder holder, int position) {
        holder.bind(results.get(position));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class ResultHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView brandName;
        TextView genericName;
        //TextView manufacturerName;
        //TextView ndcs;
        Result result;

        public ResultHolder(View itemView) {
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
            v.getContext().startActivity(intent);
        }
    }


}
