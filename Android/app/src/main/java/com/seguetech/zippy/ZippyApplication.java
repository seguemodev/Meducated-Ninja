package com.seguetech.zippy;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.facebook.stetho.Stetho;
import com.seguetech.zippy.logging.CrashlyticsTree;
import com.seguetech.zippy.modules.ApplicationModule;
import com.seguetech.zippy.services.NewsService;
import com.seguetech.zippy.ui.TypeManager;
import com.squareup.leakcanary.RefWatcher;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;
import timber.log.Timber;

public class ZippyApplication extends Application {

    @Inject
    public SharedPreferences preferences;
    private ObjectGraph objectGraph;
    private RefWatcher refWatcher = RefWatcher.DISABLED;

    public static RefWatcher getRefWatcher(Context context) {
        ZippyApplication application = (ZippyApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new CrashlyticsTree(this));

        if (BuildConfig.DEBUG) {

            // uncomment to use leak canary.
            // refWatcher = LeakCanary.install(this);

            // setup stetho
            /*
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this).enableDumpapp(
                            Stetho.defaultDumperPluginsProvider(this)
                    ).enableWebKitInspector(
                            Stetho.defaultInspectorModulesProvider(this)
                    ).build()
            );
            */
        }

        objectGraph = ObjectGraph.create(getModules().toArray());
        objectGraph.inject(this);

        // pre-cache fonts.
        TypeManager.precache(this);
        startService(new Intent(this, NewsService.class));

    }

    private List<ApplicationModule> getModules() {
        return Collections.singletonList(new ApplicationModule(this));
    }

    /**
     * Returns the application ObjectGraph
     */
    @SuppressWarnings("unused")
    public ObjectGraph getObjectGraph() {
        return this.objectGraph;
    }

    /**
     * Provides object graph injection.
     *
     * @param object The object that wishes to have the graph injected.
     */
    public void inject(Object object) {
        objectGraph.inject(object);
    }

}

