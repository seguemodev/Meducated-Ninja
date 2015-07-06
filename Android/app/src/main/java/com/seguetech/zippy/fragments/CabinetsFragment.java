package com.seguetech.zippy.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seguetech.zippy.R;
import com.seguetech.zippy.data.adapters.CabinetsAdapter;

import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class CabinetsFragment extends BaseFragment implements IInputDialogListener {


    CabinetsAdapter cabinetsAdapter;

    public CabinetsFragment() {
    }

    public static CabinetsFragment newInstance() {
        return new CabinetsFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (cabinetsAdapter != null) {
            cabinetsAdapter.refresh();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("onCreateView");
        View view = inflater.inflate(R.layout.fragment_cabinets, container, false);

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.cabinets);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        //rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        cabinetsAdapter = new CabinetsAdapter(getActivity());
        rv.setAdapter(cabinetsAdapter);

        // swipe to dismiss.
        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.RIGHT, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // no drag and drop, so we can return false here.
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // remove the item.
                cabinetsAdapter.removeCabinet(viewHolder);
            }
        });
        // hook it to the recyclerview.
        swipeToDismissTouchHelper.attachToRecyclerView(rv);

        view.findViewById(R.id.cabinets_add_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCabinetDialogFragment.show(getActivity(), CabinetsFragment.this, 0);
            }
        });
        return view;
    }

    @Override
    public void onNegativeButtonClicked(int i) {
        // noop.
    }

    @Override
    public void onNeutralButtonClicked(int i) {
        // noop.
    }

    @Override
    public void onPositiveButtonClicked(int i, String input) {
        if (cabinetsAdapter != null) {
            cabinetsAdapter.addCabinet(input);
        }
    }
}
