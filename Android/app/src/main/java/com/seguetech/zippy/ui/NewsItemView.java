package com.seguetech.zippy.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.style.LeadingMarginSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.seguetech.zippy.R;
import com.seguetech.zippy.data.model.rss.Item;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class NewsItemView extends RelativeLayout {
	private TextView headlineView;
	private ImageView imageView;
	private TextView descriptionView;
	private TextView datelineView;
	private Item item;
	@SuppressWarnings("FieldCanBeLocal")
	private NewsItemTarget target;

	public static final String TAG = NewsItemView.class.getSimpleName();

	@SuppressWarnings("unused")
	public NewsItemView(Context context) {
		super(context);
	}

	@SuppressWarnings("unused")
	public NewsItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@SuppressWarnings("unused")
	public NewsItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Do id lookups here.
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		headlineView = (TextView)findViewById(R.id.news_headline);
		imageView = (ImageView)findViewById(R.id.news_image);
		descriptionView = (TextView)findViewById(R.id.news_description);
		datelineView = (TextView)findViewById(R.id.news_dateline);
	}

	public void showNewsItem(Item item) {
		this.item = item;
		headlineView.setText(item.title);
		datelineView.setText(item.dateline);
		descriptionView.setText(item.description);
		if (item.image != null) {
			target = new NewsItemTarget(imageView,descriptionView);
			Picasso.with(getContext()).load(item.image).into(target);
		}
		else {
			imageView.setVisibility(View.GONE);
		}

		this.setTag(R.id.url_tag,item.link);
	}

	public Item getItem() {
		return this.item;
	}

	private class NewsItemTarget implements Target {

		private ImageView imageView;
		private TextView descriptionView;
		public NewsItemTarget(ImageView imageView,TextView descriptionView) {
			this.imageView = imageView;
			this.descriptionView = descriptionView;
		}

		@Override
		public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
			// use DENSITY_HIGH for the size of the image.
			bitmap.setDensity(DisplayMetrics.DENSITY_HIGH);
			BitmapDrawable drawable = new BitmapDrawable(getResources(),bitmap);
			imageView.setImageDrawable(drawable);
			imageView.setVisibility(View.VISIBLE);
			imageView.measure(MeasureSpec.UNSPECIFIED,MeasureSpec.UNSPECIFIED);
			int imageWidth = imageView.getMeasuredWidth();

			Context context = getContext();
			if (context == null) {
				return;
			}
			Resources resources = getResources();
			if (resources == null) {
				return;
			}
			DisplayMetrics dspMetrics = resources.getDisplayMetrics();
			if (dspMetrics == null) {
				return;
			}
			WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
			if (windowManager == null) {
				return;
			}

			DisplayMetrics metrics = new DisplayMetrics();
			windowManager.getDefaultDisplay().getMetrics(metrics);
			int windowWidth = metrics.widthPixels;
			//int windowHeight = metrics.heightPixels;
			// the image is more than 50%
			int margin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, dspMetrics));
			if ((imageWidth * 2) >= windowWidth) {
				LayoutParams descriptionLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				descriptionLayoutParams.addRule(RelativeLayout.BELOW, R.id.news_image);
				descriptionLayoutParams.setMargins(0, margin, 0, 0);
				descriptionView.setLayoutParams(descriptionLayoutParams);

				RelativeLayout.LayoutParams imageLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				imageLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
				imageView.setLayoutParams(imageLayoutParams);
			} else {
				LayoutParams descriptionLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				descriptionLayoutParams.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
				descriptionLayoutParams.setMargins(margin, 0, 0, 0);
				descriptionView.setLayoutParams(descriptionLayoutParams);

				RelativeLayout.LayoutParams imageLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				imageView.setLayoutParams(imageLayoutParams);
			}
			imageView.invalidate();
			descriptionView.invalidate();
		}

		@Override
		public void onBitmapFailed(Drawable errorDrawable) {
			imageView.setVisibility(View.GONE);
		}

		@Override
		public void onPrepareLoad(Drawable placeHolderDrawable) {
			imageView.setVisibility(View.GONE);
		}

		@Override
		public int hashCode() {
			return imageView.hashCode();
		}

		@Override
		public boolean equals(Object other) {
			if (NewsItemView.class.isAssignableFrom(other.getClass())) {
				return ((NewsItemView)other).imageView.equals(imageView);
			}
			return super.equals(other);
		}
	}

	@SuppressWarnings("unused")
	public class MyLeadingMarginSpan2 implements LeadingMarginSpan.LeadingMarginSpan2 {
		private int margin;
		private int lines;

		public MyLeadingMarginSpan2(int lines, int margin) {
			this.margin = margin;
			this.lines = lines;
		}

		@Override
		public int getLeadingMargin(boolean first) {
			return first ? margin : 0;
		}

		@Override
		public int getLeadingMarginLineCount() {
			return lines;
		}

		@Override
		public void drawLeadingMargin(Canvas c, Paint p, int x, int dir,
									  int top, int baseline, int bottom, CharSequence text,
									  int start, int end, boolean first, Layout layout) {}
	}
}