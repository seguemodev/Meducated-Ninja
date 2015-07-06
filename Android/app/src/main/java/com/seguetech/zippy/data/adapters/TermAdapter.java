package com.seguetech.zippy.data.adapters;


import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.seguetech.zippy.R;
import com.seguetech.zippy.ZippyApplication;
import com.seguetech.zippy.data.rest.TermService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class TermAdapter extends ArrayAdapter<String> implements Filterable {

    private List<String> resultList = new ArrayList<>();

    @Inject
    TermService termService;

    public TermAdapter(Activity activity) {
        super(activity, R.layout.simple_list_item);
        ((ZippyApplication)activity.getApplication()).inject(this);
    }


    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    try {
                        ArrayList<String> terms = termService.search(constraint.toString());
                        // Assign the data to the FilterResults
                        filterResults.values = terms;
                        filterResults.count = terms.size();
                    }
                    catch (Exception e) {
                        Timber.e(e, "Couldn't lookup search terms.");
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<String>) results.values;
                    notifyDataSetChanged();
                }
                else
                {
                    resultList.clear();
                    notifyDataSetChanged();
                }
            }};
    }

}
