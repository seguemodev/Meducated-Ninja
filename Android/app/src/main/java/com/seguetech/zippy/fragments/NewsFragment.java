package com.seguetech.zippy.fragments;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.seguetech.zippy.R;
import com.seguetech.zippy.data.model.rss.Item;
import com.seguetech.zippy.data.orm.NewsListBinder;
import com.seguetech.zippy.data.orm.ORMDatabaseHelper;
import com.seguetech.zippy.data.orm.ORMLoader;
import com.seguetech.zippy.data.orm.ORMQueryAdapter;

import timber.log.Timber;

public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    public static final int CURSOR_LOADER = 18;
    private Dao<Item,Long> itemDao = null;
    private ORMQueryAdapter<Item> queryAdapter = null;
    private ListView listView = null;
    private ORMLoader<Item> loader = null;

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }


    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(CURSOR_LOADER,null,this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getLoaderManager().destroyLoader(CURSOR_LOADER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, parent, false);
        listView = (ListView)view.findViewById(android.R.id.list);
        if (queryAdapter != null) {
            listView.setAdapter(queryAdapter);
        }
        listView.setOnItemClickListener(this);
        return view;
    }

    public Dao<Item, Long> getNewsItemDao() {
        if (itemDao == null) {
            try {
                itemDao = ORMDatabaseHelper.getInstance(getActivity().getApplicationContext()).getDao(Item.class);
            }
            catch (Exception e) {
                Timber.e(e, "item dao retrieval failed.");
            }
        }
        return itemDao;
    }

    public PreparedQuery<Item> getPreparedQuery() {
        PreparedQuery<Item> preparedQuery = null;
        try {
            QueryBuilder<Item, Long> queryBuilder = getNewsItemDao().queryBuilder();
            queryBuilder.orderBy("pubDate",false);
            preparedQuery = queryBuilder.prepare();
        }
        catch (Exception e) {
            Timber.e(e,"prepared query generation failed.");
        }
        if (queryAdapter == null) {
            queryAdapter = new ORMQueryAdapter<>(getActivity().getApplicationContext(),R.layout.news_item, preparedQuery, new NewsListBinder());
            if (listView != null) {
                listView.setAdapter(queryAdapter);
            }
        }
        return preparedQuery;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        try {
            Context context = getActivity().getApplicationContext();
            loader = new ORMLoader<>(context,ORMDatabaseHelper.getInstance(context),getPreparedQuery());
        }
        catch (Exception e) {
            Timber.e(e,"loader creation failed.");
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (queryAdapter != null) {
            queryAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        refresh();
    }

    public void refresh() {
        PreparedQuery<Item> pq = getPreparedQuery();
        if (pq != null && queryAdapter != null) {
            queryAdapter.swapCursor(null);
            queryAdapter.setQuery(pq);
            queryAdapter.notifyDataSetChanged();
        }
        if (pq != null && loader != null) {
            loader.setPreparedQuery(pq);
            loader.onContentChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object urlObject = view.getTag(R.id.url_tag);
        if (urlObject != null && urlObject instanceof String) {
            final String url = (String) view.getTag(R.id.url_tag);

            if (url.toLowerCase().endsWith(".zip")) {
                Snackbar snackbar = Snackbar.make(view, "This is a file.", Snackbar.LENGTH_LONG).setAction("DOWNLOAD", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DownloadManager.Request r = new DownloadManager.Request(Uri.parse(url));
                        String[] parts = Uri.parse(url).getPath().split("/");
                        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, parts[parts.length-1]);
                        r.allowScanningByMediaScanner();
                        r.setDescription(url);
                        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        DownloadManager dm = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                        dm.enqueue(r);
                        /*
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                        */
                    }
                });
                snackbar.show();
                snackbar.getView().setTag(R.id.url_tag, url);
            }
            else {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        }
    }

}
