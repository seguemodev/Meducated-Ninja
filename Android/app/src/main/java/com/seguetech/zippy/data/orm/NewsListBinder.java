package com.seguetech.zippy.data.orm;

import android.view.View;

import com.seguetech.zippy.data.model.rss.Item;
import com.seguetech.zippy.ui.NewsItemView;

import timber.log.Timber;


public class NewsListBinder implements ORMQueryAdapter.ListItemBinder<Item> {

	public NewsListBinder() {
	}

	@Override
	public void setItemContent(View listItem, Item dto) {
		NewsItemView view = (NewsItemView)listItem;
		view.showNewsItem(dto);
	}
}
