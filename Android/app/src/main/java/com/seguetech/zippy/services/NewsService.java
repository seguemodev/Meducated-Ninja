package com.seguetech.zippy.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.j256.ormlite.dao.Dao;
import com.seguetech.zippy.R;
import com.seguetech.zippy.ZippyApplication;
import com.seguetech.zippy.data.model.rss.Item;
import com.seguetech.zippy.data.model.rss.Rss;
import com.seguetech.zippy.data.orm.ORMDatabaseHelper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;
import org.simpleframework.xml.transform.Transform;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import timber.log.Timber;

public class NewsService extends IntentService {
    public static final String TAG = NewsService.class.getName();
    private static final String LAST_CHECK = TAG + "_LAST_CHECK";

    @Inject
    OkHttpClient client;

    public NewsService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((ZippyApplication)getApplication()).inject(this);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        long lastNewsCheck = preferences.getLong(LAST_CHECK,0l);

        // we successfully got news in the last day.
        if (System.currentTimeMillis() - lastNewsCheck <= 86400000) {
            return;
        }

        final ArrayList<Item> items = new ArrayList<>();
        try {
            items.addAll(getFeedItems(getString(R.string.press_release_feed)));
            items.addAll(getFeedItems(getString(R.string.new_drugs_feed)));
        }
        catch (Exception e) {
            Timber.e(e,"Error getting news feeds.");
        }

        if (!items.isEmpty()) {
            Timber.w("TIME TO SAVE SOME FEEDS. " + items.size());
            try {
                ORMDatabaseHelper helper = ORMDatabaseHelper.getInstance(getApplicationContext());
                final Dao<Item, Long> itemDao = helper.getDao(Item.class);
                itemDao.callBatchTasks(new Callable<Void>() {
                    public Void call() throws Exception {
                        for (Item item : items) {
                            itemDao.createOrUpdate(item);
                        }
                        return null;
                    }
                });
                preferences.edit().putLong(LAST_CHECK, System.currentTimeMillis()).apply();
            }
            catch (Exception e) {
                Timber.e(e,"Error saving feed data.");
            }
        }


    }

    private List<Item> getFeedItems(String feedUrl) throws Exception {
        ArrayList<Item> items = new ArrayList<>();
        Request request = new Request.Builder().url(feedUrl).get().build();
        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            String data = response.body().string();
            Timber.e("Got response from server: " + data);
            DateFormat rssDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
            RegistryMatcher registryMatcher = new RegistryMatcher();
            registryMatcher.bind(Date.class, new DateTransformer(rssDateFormat));
            Serializer serializer = new Persister(registryMatcher);
            final Rss rss = serializer.read(Rss.class,data);
            if (rss != null && rss.channel != null && rss.channel.items.size() > 0) {
                items.addAll(rss.channel.items);
            }
        }
        return items;
    }

    public class DateTransformer implements Transform<Date> {
        private DateFormat dateFormat;


        public DateTransformer(DateFormat dateFormat)
        {
            this.dateFormat = dateFormat;
        }



        @Override
        public Date read(String value) throws Exception
        {
            return dateFormat.parse(value);
        }


        @Override
        public String write(Date value) throws Exception
        {
            return dateFormat.format(value);
        }


    }

}
