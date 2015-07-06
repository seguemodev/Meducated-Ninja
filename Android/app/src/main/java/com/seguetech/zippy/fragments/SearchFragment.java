package com.seguetech.zippy.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.seguetech.zippy.R;
import com.seguetech.zippy.ZippyEvents;
import com.seguetech.zippy.data.adapters.ResultAdapter;
import com.seguetech.zippy.data.adapters.TermAdapter;
import com.seguetech.zippy.data.model.openfda.Result;
import com.seguetech.zippy.services.SearchService;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchFragment extends BaseFragment implements View.OnClickListener {

    ResultAdapter adapter;
    AutoCompleteTextView searchField;
    MaterialDialog waitDialog;
    RecyclerView rv;
    View empty;

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }


    @SuppressWarnings("unused")
    public void onEventMainThread(ZippyEvents.SearchResults results) {
        if (adapter != null) {
            adapter.setResults(results.getResults());
            EventBus.getDefault().removeStickyEvent(ZippyEvents.SearchResults.class);
        }
        clearDialog();
    }

    @Override
    @SuppressWarnings("unused")
    public void onEventMainThread(ZippyEvents.Message message) {
        if (message.getProducer().equals(SearchService.TAG)) {
            adapter.setResults(new ArrayList<Result>());
            // clear any sticky search results.
            EventBus.getDefault().removeStickyEvent(ZippyEvents.SearchResults.class);
            clearDialog();
        }
    }

    private void clearDialog() {
        if (waitDialog != null) {
            waitDialog.dismiss();
            waitDialog = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.search_button);
        imageButton.setOnClickListener(this);

        searchField = (AutoCompleteTextView) view.findViewById(R.id.search_field);
        TermAdapter termAdapter = new TermAdapter(getActivity());
        searchField.setAdapter(termAdapter);
        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND) {
                    search();
                    hideKeyboard(v);
                    return true;
                }
                return false;
            }
        });

        rv = (RecyclerView) view.findViewById(R.id.search_results);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        empty = view.findViewById(android.R.id.empty);
        adapter = new ResultAdapter(getActivity());
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkAdapterIsEmpty();
            }
        });
        rv.setAdapter(adapter);
        return view;
    }

    public void checkAdapterIsEmpty() {
        if (adapter != null && rv != null && empty != null) {
            if (adapter.getItemCount() == 0) {
                rv.setVisibility(View.GONE);
                empty.setVisibility(View.VISIBLE);
            }
            else {
                rv.setVisibility(View.VISIBLE);
                empty.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        hideKeyboard(v);
        search();
    }

    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    private void search() {
        if (searchField.getText() != null && searchField.getText().toString().trim().length() > 0) {
            waitDialog = new MaterialDialog.Builder(getActivity()).title(R.string.loading).content(R.string.please_wait).progress(true,0).build();
            waitDialog.show();
            Intent i = new Intent(getActivity(), SearchService.class);
            i.putExtra(SearchService.TERM_KEY, searchField.getText().toString());
            getActivity().startService(i);
        }
    }
}
