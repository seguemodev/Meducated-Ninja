package com.seguetech.zippy.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.seguetech.zippy.ui.TypeManagedSpan;


public class BaseActivity extends AppCompatActivity {

    /**
     * Lowers the action bar and sets custom action bar title font.
     *
     * @param savedInstanceState saved instance state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lowerActionBar();
        setTitle(getTitle());
    }

    /**
     * Sets the elevation on the action bar to 0, so that there's no shadow beneath it.
     */
    private void lowerActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setElevation(0);
        }
    }

    /**
     * Overrides the default implementation to utilize our custom font.
     *
     * @param title the title.
     */
    @Override
    public void setTitle(CharSequence title) {
        String value = title.toString();
        SpannableString s = new SpannableString(value);
        s.setSpan(new TypeManagedSpan(this, "robotoslab"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (value.endsWith(".ninja")) {
            StyleSpan bold = new StyleSpan(android.graphics.Typeface.BOLD);
            StyleSpan italics = new StyleSpan(android.graphics.Typeface.ITALIC);
            s.setSpan(bold, 0, value.indexOf("."), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(italics, value.indexOf("."), s.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(new RelativeSizeSpan(0.75f),value.indexOf("."),s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        super.setTitle(s);
    }

    /**
     * Overrides the default implementation to utilize our custom font.
     *
     * @param titleId the string id of the title.
     */
    @Override
    public void setTitle(int titleId) {
        setTitle(getString(titleId));
    }


}
