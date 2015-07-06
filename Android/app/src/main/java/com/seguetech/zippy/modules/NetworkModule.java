package com.seguetech.zippy.modules;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.seguetech.zippy.BuildConfig;
import com.seguetech.zippy.R;
import com.seguetech.zippy.ZippyApplication;
import com.seguetech.zippy.data.rest.FdaService;
import com.seguetech.zippy.data.rest.RxImageService;
import com.seguetech.zippy.data.rest.TermService;
import com.seguetech.zippy.logging.TimberInterceptor;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import timber.log.Timber;

@Module(complete = false, library = true)
public class NetworkModule {

    private static final int CACHE_SIZE = 10 * 1024 * 1024;

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(ZippyApplication application) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(60, TimeUnit.SECONDS);
        client.setReadTimeout(60, TimeUnit.SECONDS);
        client.setWriteTimeout(60, TimeUnit.SECONDS);
        File cacheFile = new File(application.getCacheDir(), "http");
        Cache cache = new Cache(cacheFile, CACHE_SIZE);
        client.setCache(cache);

        //client.networkInterceptors().add(new StethoInterceptor());
        client.networkInterceptors().add(new TimberInterceptor());

        client.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                if (response.isSuccessful() && !chain.request().headers("OkHttp-Force-Offline").isEmpty()) {
                    Response.Builder responseBuilder = response.newBuilder();
                    if (response.header("Cache-Control","no-cache").contains("no-cache")) {
                        responseBuilder.header("Cache-Control", "public, max-age=120");
                    }
                    if (response.header("Expires").equals("-1")) {
                        responseBuilder.removeHeader("Expires");
                    }
                    responseBuilder.removeHeader("Pragma");
                    return responseBuilder.build();
                }
                return response;
            }
        });


        return client;
    }

    @Provides
    @Singleton
    Picasso providePicasso(ZippyApplication application, OkHttpClient client) {
        return new Picasso.Builder(application).downloader(new OkHttpDownloader(client)).indicatorsEnabled(BuildConfig.DEBUG).build();
    }

    @Provides
    @Singleton
    FdaService provideFdaService(ZippyApplication application, OkHttpClient client) {
        return new RestAdapter.Builder().setClient(new OkClient(client)).setEndpoint(application.getString(R.string.fda_server)).build().create(FdaService.class);
    }

    @Provides
    @Singleton
    RxImageService provideRxImageService(ZippyApplication application, OkHttpClient client) {
        return new RestAdapter.Builder().setClient(new OkClient(client)).setEndpoint(application.getString(R.string.rximage_server)).build().create(RxImageService.class);
    }

    @Provides
    @Singleton
    TermService provideTermService(ZippyApplication application, OkHttpClient client) {
        return new RestAdapter.Builder().setClient(new OkClient(client)).setEndpoint(application.getString(R.string.term_server)).build().create(TermService.class);
    }
}
