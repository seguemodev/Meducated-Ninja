package com.seguetech.zippy.ui;


import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.seguetech.zippy.BuildConfig;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

/**
 * Singleton class that provides an in-memory cache of loaded typefaces. Uses filenames to determine
 * types. This avoids a memory leak in android's TypeFace management, as well as ensures that only
 * one copy of a font is loaded for any given style.
 */
public class TypeManager {
    private static final String TAG = TypeManager.class.getSimpleName();
    private static final String TTFEXT = ".ttf";
    private static final String OTFEXT = ".otf";
    private static final String ITALICEXT = "_italic";
    private static final String BOLDEXT = "_bold";
    private static final String FONTPATH = "fonts";
    private static volatile TypeManager _instance;
    private static volatile Context mContext;
    private static volatile LruCache<String, Typeface> typefaceLruCache = null;
    private static volatile List<String> availableFonts = null;
    private static volatile List<String> availableVariants = null;

    private TypeManager(Context context) {
        mContext = context;
        try {
            availableFonts = Arrays.asList(context.getAssets().list(FONTPATH));
            availableVariants = new LinkedList<>();
        } catch (Exception e) {
            Timber.e(e, "Error getting available fonts.");
        }
        if (availableFonts != null && availableFonts.size() > 0) {
            int regularFontCount = 0;
            // for each regular font, count 4 spaces (regular, bold, italic, bold-italic).
            for (String font : availableFonts) {
                availableVariants.add(font.split("\\.")[0]);
                if (!font.contains("_")) {
                    regularFontCount += 4;
                }
            }
            typefaceLruCache = new LruCache<>(Math.max(regularFontCount, availableFonts.size()));
        } else {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "No available fonts?!?!");
            }
        }

    }

    public static TypeManager getInstance(Context context) {
        if (_instance == null) {
            synchronized (TypeManager.class) {
                if (_instance == null) {
                    // always use the application context, since activities can
                    // disappear on us.
                    _instance = new TypeManager(context.getApplicationContext());
                }
            }
        }
        return _instance;
    }

    /**
     * Clear typefaces from the cache. Normally only used in the Application class onLowMemory
     * method.
     */
    public static void clear() {
        if (_instance != null && typefaceLruCache != null) {
            synchronized (TypeManager.class) {
                typefaceLruCache.evictAll();
            }
        }
    }

    /**
     * Loads all the fonts available in assets/fonts into memory based on file name. Use of this is
     * optional, but some usages of TypeManagedTextView appear to require a redraw if the typeface is
     * not loaded ahead of time.
     *
     * @param context a context - will resolve to ApplicationContext
     */
    public static void precache(Context context) {
        getInstance(context);
        for (String font : availableFonts) {
            if (!font.contains("_")) {
                String prefix = font.split("\\.")[0];
                _instance.get(prefix, Typeface.NORMAL);
                _instance.get(prefix, Typeface.BOLD);
                _instance.get(prefix, Typeface.ITALIC);
                _instance.get(prefix, Typeface.BOLD_ITALIC);
            }
        }

    }

    public Typeface get(String name, int textStyle) {
        // if we don't have any available fonts or an instantiated cache, return null.
        if (availableFonts == null || typefaceLruCache == null) {
            if (BuildConfig.DEBUG) {
                Timber.e("Initialization not complete - available fonts/typefacelrucache problem.");
            }
            return null;
        }
        if (name == null) {
            if (BuildConfig.DEBUG) {
                Timber.e("No name passed!");
            }
            return null;
        }

        // force the name to lowercase, but keep the original name intact, just in case.
        String fontName = name.toLowerCase(Locale.ENGLISH);

        switch (textStyle) {
            case Typeface.BOLD: {
                if (availableVariants.contains(fontName + BOLDEXT)) {
                    fontName += BOLDEXT;
                }
                break;
            }
            case Typeface.ITALIC: {
                if (availableVariants.contains(fontName + ITALICEXT)) {
                    fontName += ITALICEXT;
                }
                break;
            }
            case Typeface.BOLD_ITALIC: {
                if (availableVariants.contains(fontName + BOLDEXT + ITALICEXT)) {
                    fontName += BOLDEXT + ITALICEXT;
                } else {
                    if (availableVariants.contains(fontName + BOLDEXT)) {
                        fontName += BOLDEXT;
                    }
                }
                break;
            }

        }

        Typeface typeface = typefaceLruCache.get(fontName);
        if (typeface != null) {
            return typeface;
        }
        // ttf first
        // most fonts will probably be ttf
        String ttfFontName = fontName + TTFEXT;
        if (availableFonts.contains(ttfFontName)) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Attempting to load ttf from disk: " + ttfFontName);
            }
            typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/" + ttfFontName);
        }

        // then otf
        String otfFontName = fontName + OTFEXT;
        if (typeface == null && availableFonts.contains(otfFontName)) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Attempting to load otf from disk: " + otfFontName);
            }
            typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/" + otfFontName);
        }

        if (typeface != null) {
            return typefaceLruCache.put(fontName, typeface);
        }

        // shouldn't ever get here.
        return null;
    }

    public Typeface get(String name) {
        return get(name, Typeface.NORMAL);
    }


}