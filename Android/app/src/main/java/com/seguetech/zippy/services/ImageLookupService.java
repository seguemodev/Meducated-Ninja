package com.seguetech.zippy.services;


import android.app.IntentService;
import android.content.Intent;

import com.seguetech.zippy.ZippyApplication;
import com.seguetech.zippy.ZippyEvents;
import com.seguetech.zippy.data.model.openfda.Result;
import com.seguetech.zippy.data.model.rximage.NlmRxImage;
import com.seguetech.zippy.data.model.rximage.RxImage;
import com.seguetech.zippy.data.rest.RxImageService;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class ImageLookupService extends IntentService {

    public static final String TAG = ImageLookupService.class.getName();
    public static final String MEDICINE_KEY = TAG + "_MEDICINE";
    private final Pattern threeParts;

    @Inject
    RxImageService rxImageService;

    public ImageLookupService() {
        super(TAG);
        threeParts = Pattern.compile("(.*-.*)(-.*)");
    }

    public void onCreate() {
        super.onCreate();
        ((ZippyApplication)getApplication()).inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Result result = intent.getParcelableExtra(MEDICINE_KEY);
        Set<String> codes = getUniqueNDCs(result);
        LinkedHashSet<String> urls = new LinkedHashSet<>();

        // one more codes was found.
        if (!codes.isEmpty()) {
            for (String code : codes) {
                // valid resolutions are 120, 300, 600, 800, 1024, full
                // we should get the resolution closest to the display, if available.
                RxImage rxImage = rxImageService.search(code,"1024");
                if (rxImage.getReplyStatus().getSuccess() && rxImage.getNlmRxImages().size() > 0) {
                    for (NlmRxImage image : rxImage.getNlmRxImages()) {
                        urls.add(image.getImageUrl());
                    }
                }
            }
        }

        if (!urls.isEmpty()) {
            EventBus.getDefault().post(new ZippyEvents.ImagesFound(urls));
        }


    }

    private Set<String> getUniqueNDCs(Result result) {
        LinkedHashSet<String> ndcSet = new LinkedHashSet<>();

        for (String ndc : result.getOpenfda().getPackageNdc()) {
            Matcher m = threeParts.matcher(ndc);
            // three part NDC, we just want the first two.
            if (m.matches()) {
                ndc = m.group(1);
            }
            ndcSet.add(ndc);
        }

        for (String ndc : result.getOpenfda().getProductNdc()) {
            Matcher m = threeParts.matcher(ndc);
            // three part NDC, we just want the first two.
            if (m.matches()) {
                ndc =m.group(1);
            }
            ndcSet.add(ndc);
        }
        for (String ndc : result.getOpenfda().getOriginalPackagerProductNdc()) {
            Matcher m = threeParts.matcher(ndc);
            // three part NDC, we just want the first two.
            if (m.matches()) {
                ndc = m.group(1);
            }
            ndcSet.add(ndc);
        }

        return ndcSet;
    }
}
