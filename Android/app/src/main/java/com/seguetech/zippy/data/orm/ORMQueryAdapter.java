package com.seguetech.zippy.data.orm;

import android.content.Context;
import android.database.Cursor;
import android.widget.ResourceCursorAdapter;
import android.view.View;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;

/**
 * This binds the query results to list items.
 * Type-safe, so it should work with any properly annotated class.
 * @param <T>
 */
public class ORMQueryAdapter<T> extends ResourceCursorAdapter {

	protected PreparedQuery<T> mQuery;
	protected ListItemBinder<T> mListItemBinder;

	public ORMQueryAdapter(Context context, int layout, PreparedQuery<T> query, ListItemBinder<T> listItemBinder) {
		super(context,layout,null,false);
		mQuery = query;
		mListItemBinder = listItemBinder;
	}

	@SuppressWarnings("unused")
	public PreparedQuery<T> getQuery() {
		return mQuery;
	}

	public void setQuery(PreparedQuery<T> query) {
		mQuery = query;
	}

	@Override
	public void bindView(View listItem, Context context, Cursor cursor) {
		if (mQuery == null || cursor == null || listItem == null || context == null) {
			return;
		}

		try {
			T dto = mQuery.mapRow(new AndroidDatabaseResults(cursor,null));
			mListItemBinder.setItemContent(listItem, dto);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object getItem(int position) {
		try {
			return mQuery.mapRow(new AndroidDatabaseResults((Cursor) super.getItem(position), null));
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public interface ListItemBinder<T> {
		void setItemContent(View listItem, T dto);
	}
}