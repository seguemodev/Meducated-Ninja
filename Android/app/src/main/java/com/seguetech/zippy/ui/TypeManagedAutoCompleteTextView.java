package com.seguetech.zippy.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;

import com.seguetech.zippy.R;

import timber.log.Timber;

public class TypeManagedAutoCompleteTextView extends AppCompatAutoCompleteTextView {
    @SuppressWarnings("unused")
    public TypeManagedAutoCompleteTextView(Context context) {
        this(context, null);
    }

    @SuppressWarnings("unused")
    public TypeManagedAutoCompleteTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.autoCompleteTextViewStyle);
    }

    @SuppressWarnings("unused")
    public TypeManagedAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (!isInEditMode()) {
            String styledTypeFaceName = null;
            int textStyle = Typeface.NORMAL;
            TypedArray styledAttributes = null;
            try {
                styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TypeManagedView);
                if (styledAttributes != null) {
                    styledTypeFaceName = styledAttributes.getString(R.styleable.TypeManagedView_typeface);
                    textStyle = styledAttributes.getInt(R.styleable.TypeManagedView_android_textStyle, Typeface.NORMAL);
                }
            } catch (Exception e) {
                Timber.e(e, "Problem loading attributes.");
            } finally {
                if (styledAttributes != null) {
                    styledAttributes.recycle();
                }
            }
            if (styledTypeFaceName != null) {
                Typeface managedTypeface = TypeManager.getInstance(getContext()).get(styledTypeFaceName, textStyle);
                if (managedTypeface != null)
                    setTypeface(managedTypeface, textStyle);
            }
        }
    }

}
