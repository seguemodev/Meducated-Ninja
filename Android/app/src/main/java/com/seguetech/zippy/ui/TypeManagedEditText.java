package com.seguetech.zippy.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.seguetech.zippy.R;

import timber.log.Timber;

/**
 * TextView implementation that uses a custom font, loaded via the TypeManager class.
 * This supports using the android:textStyle attribute to set bold/italic/bolid|italic, rather
 * than having to specify the full font name.
 */
@SuppressWarnings("unused")
public class TypeManagedEditText extends AppCompatEditText {

    @SuppressWarnings("unused")
    public TypeManagedEditText(Context context) {
        this(context, null);
    }

    @SuppressWarnings("unused")
    public TypeManagedEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    @SuppressWarnings("unused")
    public TypeManagedEditText(Context context, AttributeSet attrs, int defStyle) {
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
