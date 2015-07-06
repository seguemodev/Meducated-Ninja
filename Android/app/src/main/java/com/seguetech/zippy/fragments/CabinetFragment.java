package com.seguetech.zippy.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seguetech.zippy.R;
import com.seguetech.zippy.ZippyEvents;
import com.seguetech.zippy.data.adapters.MedicineAdapter;


public class CabinetFragment extends BaseFragment {

    private String cabinet;
    private MedicineAdapter adapter;
    private RecyclerView recyclerView;

    public CabinetFragment() {

    }

    public CabinetFragment newInstance(String cabinet) {
        CabinetFragment fragment = new CabinetFragment();
        Bundle args = new Bundle();
        args.putString("cabinet",cabinet);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.cabinet != null && this.adapter != null) {
            this.adapter.setCabinet(this.cabinet);
        }
    }

    public void setCabinet(String cabinet) {
        this.cabinet = cabinet;
        if (adapter == null) {
            adapter = new MedicineAdapter(getActivity(),this.cabinet);
            if (recyclerView != null) {
                recyclerView.setAdapter(adapter);
            }
        }
        else {
            adapter.setCabinet(cabinet);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cabinet, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.medicines);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (cabinet != null) {
            setCabinet(cabinet);
        }
        if (getArguments() != null && getArguments().containsKey("cabinet")) {
            setCabinet(getArguments().getString("cabinet"));
        }
        return view;
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(ZippyEvents.CabinetRenamed renamed) {
        if (renamed.getOriginalName().equals(cabinet)) {
            setCabinet(renamed.getNewName());
        }
    }
}
