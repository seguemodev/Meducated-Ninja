package com.seguetech.zippy.ui;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;


public class TypeManagedSpan extends MetricAffectingSpan {
    private final String typeFaceName;
    private final Context context;


    public TypeManagedSpan(Context context, String typeFaceName) {
        this.context = context.getApplicationContext();
        this.typeFaceName = typeFaceName;
    }

    @Override
    public void updateDrawState(final TextPaint drawState) {
        apply(drawState);
    }

    @Override
    public void updateMeasureState(final TextPaint paint) {
        apply(paint);
    }

    private void apply(final Paint paint) {
        final Typeface oldTypeface = paint.getTypeface();
        final int oldStyle = oldTypeface != null ? oldTypeface.getStyle() : 0;
        Typeface tf = TypeManager.getInstance(context).get(typeFaceName, oldStyle);
        if (tf == null) return;

        final int fakeStyle = oldStyle & ~tf.getStyle();

        if ((fakeStyle & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fakeStyle & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(tf);
    }
}
