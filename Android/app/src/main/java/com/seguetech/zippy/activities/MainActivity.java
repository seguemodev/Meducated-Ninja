package com.seguetech.zippy.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.seguetech.zippy.R;
import com.seguetech.zippy.fragments.CabinetsFragment;
import com.seguetech.zippy.fragments.IInputDialogListener;
import com.seguetech.zippy.fragments.NewsFragment;
import com.seguetech.zippy.fragments.SearchFragment;
import com.seguetech.zippy.fragments.SortDialogFragment;
import com.seguetech.zippy.ui.TypeManagedSpan;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener,IInputDialogListener {
    ViewPager viewPager;
    TabLayout tabLayout;

    private static final int SORT_CODE = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        MainViewPagerAdapter adapter = MainViewPagerAdapter.newInstance(this,getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(3);
        if (savedInstanceState == null && getIntent() != null && getIntent().hasExtra("TAB_REQUESTED")) {
            int requestedTab = getIntent().getIntExtra("TAB_REQUESTED",-1);
            switch(requestedTab) {
                case R.id.search_button:
                    viewPager.setCurrentItem(1,false);
                    break;
                case R.id.cabinet_button:
                    viewPager.setCurrentItem(0,false);
                    break;
                case R.id.news_button:
                    viewPager.setCurrentItem(2,false);
                    break;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (viewPager.getCurrentItem()) {
            case 0:
                getMenuInflater().inflate(R.menu.menu_main, menu);
                return true;
            case 1:
                getMenuInflater().inflate(R.menu.menu_search, menu);
                return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.sort:
                SortDialogFragment.show(this,SORT_CODE);
                return true;
            case R.id.about:
                Intent aboutIntent = new Intent(this,AboutActivity.class);
                startActivity(aboutIntent);
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        supportInvalidateOptionsMenu();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPositiveButtonClicked(int requestCode, String input) {

    }

    @Override
    public void onNegativeButtonClicked(int i) {

    }

    @Override
    public void onNeutralButtonClicked(int i) {

    }

    private static final class MainViewPagerAdapter extends FragmentPagerAdapter {

        private final CharSequence cabinetTitle;
        private final CharSequence searchTitle;
        private final CharSequence newsTitle;

        public static MainViewPagerAdapter newInstance(Context context, FragmentManager fragmentManager) {
            SpannableString cabinetSpan = new SpannableString(context.getString(R.string.medicine_cabinets));
            cabinetSpan.setSpan(new TypeManagedSpan(context, "robotoslab"), 0, cabinetSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            SpannableString searchSpan = new SpannableString(context.getString(R.string.search));
            searchSpan.setSpan(new TypeManagedSpan(context, "robotoslab"), 0, searchSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            SpannableString newsSpan = new SpannableString(context.getString(R.string.news));
            newsSpan.setSpan(new TypeManagedSpan(context, "robotoslab"), 0, newsSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            return new MainViewPagerAdapter(fragmentManager,cabinetSpan,searchSpan,newsSpan);
        }

        private MainViewPagerAdapter(FragmentManager manager, CharSequence cabinetTitle, CharSequence searchTitle, CharSequence newsTitle) {
            super(manager);

            this.cabinetTitle = cabinetTitle;
            this.searchTitle = searchTitle;
            this.newsTitle = newsTitle;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return CabinetsFragment.newInstance();
                case 1:
                    return SearchFragment.newInstance();
                case 2:
                    return NewsFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch(position) {
                case 0:
                    return cabinetTitle;
                case 1:
                    return searchTitle;
                case 2:
                    return newsTitle;
            }
            return "";
        }
    }
}
