package com.seguetech.zippy.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.seguetech.zippy.R;
import com.seguetech.zippy.ZippyApplication;
import com.seguetech.zippy.ZippyEvents;
import com.seguetech.zippy.data.model.openfda.Result;
import com.seguetech.zippy.services.ImageLookupService;
import com.seguetech.zippy.services.MedicineManagerService;
import com.seguetech.zippy.ui.TypeManagedTextView;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

import timber.log.Timber;

public class MedicineFragment extends BaseFragment implements IInputDialogListener {

    Result result;
    private static final int CABINET_CODE = 2;
    private ArrayList<String> images = new ArrayList<>();
    private UrlPagerAdapter imageAdapter;
    private ViewPager viewPager;
    private View imageContainer;

    @Inject
    Picasso picasso;

    public MedicineFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity.getIntent() != null && activity.getIntent().getExtras() != null && activity.getIntent().getExtras().containsKey("medicine")) {
            result = activity.getIntent().getExtras().getParcelable("medicine");
            if (result != null) {
                activity.setTitle(result.getOpenfda().getBrandName().get(0));
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((ZippyApplication)getActivity().getApplication()).inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_medicine, container, false);
        imageContainer = v.findViewById(R.id.imagecontainer);
        viewPager = (ViewPager)v.findViewById(R.id.viewpager);
        if (imageAdapter == null) {
            imageAdapter = new UrlPagerAdapter();
        }
        viewPager.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();

        CirclePageIndicator indicator = (CirclePageIndicator)v.findViewById(R.id.dots);
        indicator.setViewPager(viewPager);
        //indicator.setOnPageChangeListener(imageAdapter);



        if (result != null) {
            FloatingActionButton fab = (FloatingActionButton)v.findViewById(R.id.medicine_add_fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectCabinetDialogFragment.show(MedicineFragment.this,CABINET_CODE);
                }
            });

            ((TypeManagedTextView)v.findViewById(R.id.title)).setText(result.getOpenfda().getBrandName().get(0));

            // dosage
            if (result.getDosageAndAdministration() != null && result.getDosageAndAdministration().size() > 0) {
                v.findViewById(R.id.dose_header).setVisibility(View.VISIBLE);
                v.findViewById(R.id.dose_info).setVisibility(View.VISIBLE);
                ((TypeManagedTextView) v.findViewById(R.id.dose_info)).setText(Html.fromHtml(result.getDosageAndAdministration().get(0).replace("DOSAGE AND ADMINISTRATION","")));
            }
            else {
                v.findViewById(R.id.dose_header).setVisibility(View.GONE);
                v.findViewById(R.id.dose_info).setVisibility(View.GONE);
            }


            // side effects
            if (result.getAdverseReactions() != null && result.getAdverseReactions().size() > 0) {
                v.findViewById(R.id.side_effects_hr).setVisibility(View.VISIBLE);
                v.findViewById(R.id.side_effects_header).setVisibility(View.VISIBLE);
                v.findViewById(R.id.side_effects_info).setVisibility(View.VISIBLE);
                ((TypeManagedTextView) v.findViewById(R.id.side_effects_info)).setText(Html.fromHtml(result.getAdverseReactions().get(0).replace("ADVERSE REACTIONS", "")));
            }
            else {
                v.findViewById(R.id.side_effects_hr).setVisibility(View.GONE);
                v.findViewById(R.id.side_effects_header).setVisibility(View.GONE);
                v.findViewById(R.id.side_effects_info).setVisibility(View.GONE);
            }

            // interactions
            if (result.getDrugInteractions() != null && result.getDrugInteractions().size() > 0) {
                v.findViewById(R.id.interactions_hr).setVisibility(View.VISIBLE);
                v.findViewById(R.id.interactions_header).setVisibility(View.VISIBLE);
                v.findViewById(R.id.interactions_info).setVisibility(View.VISIBLE);
                ((TypeManagedTextView) v.findViewById(R.id.interactions_info)).setText(Html.fromHtml(result.getDrugInteractions().get(0).replace("DRUG INTERACTIONS","")));
            }
            else {
                v.findViewById(R.id.interactions_hr).setVisibility(View.GONE);
                v.findViewById(R.id.interactions_header).setVisibility(View.GONE);
                v.findViewById(R.id.interactions_info).setVisibility(View.GONE);
            }

            if (images == null || images.size() < 1) {
                Intent intent = new Intent(getActivity(),ImageLookupService.class);
                intent.putExtra(ImageLookupService.MEDICINE_KEY, result);
                getActivity().startService(intent);
            }

        }
        return v;
    }


    @Override
    public void onPositiveButtonClicked(int requestCode, String input) {
        Timber.w("save requested to cabinet input");
        Intent saveIntent = new Intent(getActivity(),MedicineManagerService.class);
        saveIntent.putExtra(MedicineManagerService.ACTION_KEY,MedicineManagerService.ACTION_ADD);
        saveIntent.putExtra(MedicineManagerService.CABINET_KEY,input);
        saveIntent.putExtra(MedicineManagerService.MEDICINE_KEY,result);
        getActivity().startService(saveIntent);
    }

    @Override
    public void onNegativeButtonClicked(int i) {

    }

    @Override
    public void onNeutralButtonClicked(int i) {

    }

    @SuppressWarnings("unused")
    @Override
    public void onEventMainThread(ZippyEvents.Message message) {
        if (message.getProducer().equals(MedicineManagerService.class.getName())) {
            View v = getView();
            if (v != null) {
                Snackbar.make(v, message.getMessage(), Snackbar.LENGTH_LONG).show();
            }
            return;
        }
        super.onEventMainThread(message);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(ZippyEvents.ImagesFound images) {
        Timber.d("Got Images");
        if (!images.getUrls().isEmpty()) {
            imageContainer.setVisibility(View.VISIBLE);
            this.images.clear();
            this.images.addAll(images.getUrls());
            imageAdapter.notifyDataSetChanged();
        }
        else {
            imageContainer.setVisibility(View.GONE);
        }
    }

    public class UrlPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = LayoutInflater.from(container.getContext()).inflate(R.layout.image, container, false);
            ImageView imageView = (ImageView)v.findViewById(R.id.image);
            container.addView(v);
            picasso.load(images.get(position)).noPlaceholder().into(imageView);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            picasso.cancelRequest((ImageView)object);
            container.removeView((ImageView)object);
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView)object);
        }
    }
}
