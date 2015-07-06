package com.seguetech.zippy.modules;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.seguetech.zippy.ZippyApplication;
import com.seguetech.zippy.data.adapters.TermAdapter;
import com.seguetech.zippy.fragments.BaseFragment;
import com.seguetech.zippy.fragments.CabinetsFragment;
import com.seguetech.zippy.fragments.MedicineFragment;
import com.seguetech.zippy.fragments.SearchFragment;
import com.seguetech.zippy.services.ImageLookupService;
import com.seguetech.zippy.services.MedicineManagerService;
import com.seguetech.zippy.services.NewsService;
import com.seguetech.zippy.services.SearchService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        library = true,
        includes = {NetworkModule.class},
        injects = {
                ZippyApplication.class,
                SearchService.class,
                BaseFragment.class,
                CabinetsFragment.class,
                SearchFragment.class,
                MedicineManagerService.class,
                ImageLookupService.class,
                NewsService.class,
                MedicineFragment.class,
                TermAdapter.class
        }
)
public class ApplicationModule {
    private final ZippyApplication application;

    public ApplicationModule(ZippyApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    ZippyApplication provideApplication() {
        return application;
    }


    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new Gson();
    }
}
