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
import com.seguetech.zippy.activities.CabinetActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import timber.log.Timber;

@SuppressWarnings("unused")
public class CabinetsAdapter extends RecyclerView.Adapter<CabinetsAdapter.CabinetsHolder> {

    private ArrayList<String> cabinets = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private static final Object editLock = new Object();

    public CabinetsAdapter(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Set<String> stringSet = sharedPreferences.getStringSet("cabinets", new HashSet<String>());
        cabinets = new ArrayList<>(Arrays.asList(stringSet.toArray(new String[stringSet.size()])));
        sort();
    }

    private void sort() {
        Collections.sort(cabinets, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });
    }

    public synchronized void addCabinet(String input) {
        synchronized (editLock) {
            String originalInput = input;
            int i = 0;
            while (cabinets.contains(input)) {
                input = originalInput + " (" + ++i + ")";
            }
            cabinets.add(input);
            sort();
            int position = cabinets.indexOf(input);
            notifyItemInserted(position);
            saveCabinets();
        }
    }

    private void saveCabinets() {
        synchronized (editLock) {
            Set<String> set = new HashSet<>();
            set.addAll(cabinets);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet("cabinets", set);
            boolean saved = editor.commit();
            if (saved) {
                Timber.d("Saved Cabinet.");
            } else {
                Timber.e("Couldn't Save Cabinet.");
            }
        }
    }

    public void removeCabinet(RecyclerView.ViewHolder viewHolder) {
        synchronized (editLock) {
            int position = viewHolder.getAdapterPosition();
            cabinets.remove(position);
            notifyItemRemoved(position);
            saveCabinets();
        }
    }

    @Override
    public CabinetsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cabinet, parent, false);
        view.setClickable(true);
        return new CabinetsHolder(view);
    }

    @Override
    public void onBindViewHolder(CabinetsHolder holder, int position) {
        holder.bind(cabinets.get(position));
    }

    public void refresh() {
        cabinets = new ArrayList<>(Arrays.asList(sharedPreferences.getStringSet("cabinets", new HashSet<String>()).toArray(new String[0])));
        sort();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cabinets.size();
    }

    public static class CabinetsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView cabinetName;

        public CabinetsHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            cabinetName = (TextView) itemView.findViewById(R.id.cabinet_name);
        }

        public void bind(String cabinet) {
            cabinetName.setText(cabinet);
        }

        @Override
        public void onClick(View v) {
            // EventBus.getDefault().post(new ZippyEvents.OpenCabinet(cabinetName.getText().toString()));
            Intent i = new Intent(v.getContext(), CabinetActivity.class);
            i.putExtra("cabinet",cabinetName.getText().toString());
            v.getContext().startActivity(i);
        }
    }
}
