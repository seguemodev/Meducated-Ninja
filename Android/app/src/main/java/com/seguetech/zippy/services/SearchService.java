package com.seguetech.zippy.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.seguetech.zippy.BuildConfig;
import com.seguetech.zippy.R;
import com.seguetech.zippy.ZippyApplication;
import com.seguetech.zippy.ZippyEvents;
import com.seguetech.zippy.data.model.openfda.OpenFdaResponse;
import com.seguetech.zippy.data.model.openfda.Result;
import com.seguetech.zippy.data.rest.FdaService;

import java.util.ArrayList;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

public class SearchService extends IntentService {
    public static final String TAG = SearchService.class.getName();
    public static final String TERM_KEY = TAG + "-Term";
    private static final String FULL_TERM = "openfda.brand_name:%s+openfda.generic_name:%s+openfda.product_ndc:%s";
    @Inject
    FdaService fdaService;

    public SearchService() {
        super(SearchService.class.getName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((ZippyApplication) getApplication()).inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String term = intent.getStringExtra(TERM_KEY).trim();

        // no network, nothing to do.
        if (!isConnected()) {
            EventBus.getDefault().post(new ZippyEvents.Message(TAG,getString(R.string.network_unavailable)));
            return;
        }

        if ( term.trim().length() > 0) {
            if (term.contains(" ")) {
                term = "\"" + term.replace(" ", "+") + "\"";
            }
        }
        else {
            // term is null, or is just whitespace.
            return;
        }

        try {
            OpenFdaResponse response = fdaService.search(BuildConfig.OPENFDA_API_KEY, String.format(FULL_TERM, term, term, term), 100);
            if (response != null && response.getResults() != null && response.getResults().size() > 0) {
                ArrayList<Result> usableResults = new ArrayList<>();
                for (Result result : response.getResults()) {
                    if (result.getOpenfda() != null && (
                            (result.getOpenfda().getBrandName() != null && result.getOpenfda().getBrandName().size() > 0)
                            ||
                            (result.getOpenfda().getGenericName() != null && result.getOpenfda().getGenericName().size() > 0)
                    )) {
                        usableResults.add(result);
                    }
                }
                if (response.getMeta().getResults().getTotal() > 100) {
                    EventBus.getDefault().post(new ZippyEvents.Message(TAG, String.format(getString(R.string.too_many_results), response.getMeta().getResults().getTotal())));
                }
                EventBus.getDefault().postSticky(new ZippyEvents.SearchResults(term, usableResults));
            } else {
                EventBus.getDefault().postSticky(new ZippyEvents.SearchResults(term, new ArrayList<Result>()));
            }
        } catch (Exception e) {
            Timber.e(e, "Error retrieving response.");
            EventBus.getDefault().post(new ZippyEvents.Message(TAG,getString(R.string.search_error)));
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
