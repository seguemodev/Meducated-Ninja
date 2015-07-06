package com.seguetech.zippy.data.orm;

/*
 * Based on CursorLoader.java:
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.j256.ormlite.android.AndroidCompiledStatement;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.StatementBuilder.StatementType;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class ORMLoader<T> extends AsyncTaskLoader<Cursor> {
	final ForceLoadContentObserver mObserver;
	private OrmLiteSqliteOpenHelper mDatabaseHelper;

	private Cursor mCursor;
	private PreparedQuery<T> mQuery;

	public ORMLoader(Context context, OrmLiteSqliteOpenHelper databaseHelper, PreparedQuery<T> query) throws Exception {
		super(context);
		if (databaseHelper == null) {
			throw new Exception("Database Helper cannot be null");
		}
		if (query == null) {
			throw new Exception("Query cannot be null");
		}
		mDatabaseHelper = databaseHelper;
		mObserver = new ForceLoadContentObserver();
		mQuery = query;
	}

	/* Runs on a worker thread */
	@Override
	public Cursor loadInBackground() {
		Cursor cursor;
		try {
			cursor = ((AndroidCompiledStatement) mQuery.compile(mDatabaseHelper.getConnectionSource().getReadOnlyConnection(), StatementType.SELECT)).getCursor();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		if (cursor != null) {
			cursor.getCount();
			registerContentObserver(cursor, mObserver);
		}
		return cursor;
	}

	/**
	 * Registers an observer to get notifications from the content provider
	 * when the cursor needs to be refreshed.
	 */
	void registerContentObserver(Cursor cursor, ContentObserver observer) {
		cursor.registerContentObserver(observer);
	}

	/* Runs on the UI thread */
	@Override
	public void deliverResult(Cursor cursor) {
		if (isReset()) {
			// An async query came in while the loader is stopped
			if (cursor != null) {
				cursor.close();
			}
			return;
		}
		Cursor oldCursor = mCursor;
		mCursor = cursor;

		if (isStarted()) {
			super.deliverResult(cursor);
		}

		if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
			oldCursor.close();
		}
	}

	/**
	 * Starts an asynchronous load of the contacts list data. When the result is ready the callbacks
	 * will be called on the UI thread. If a previous load has been completed and is still valid
	 * the result may be passed to the callbacks immediately.
	 * <p/>
	 * Must be called from the UI thread
	 */
	@Override
	protected void onStartLoading() {
		if (mCursor != null) {
			deliverResult(mCursor);
		}
		if (takeContentChanged() || mCursor == null) {
			forceLoad();
		}
	}

	/**
	 * Must be called from the UI thread
	 */
	@Override
	protected void onStopLoading() {
		// Attempt to cancel the current load task if possible.
		cancelLoad();
	}

	@Override
	public void onCanceled(Cursor cursor) {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}

	@Override
	protected void onReset() {

		// Ensure the loader is stopped
		onStopLoading();

		if (mCursor != null && !mCursor.isClosed()) {
			mCursor.close();
		}

		mCursor = null;
		super.onReset();
	}


	public void setPreparedQuery(PreparedQuery<T> preparedQuery) {
		this.mQuery = preparedQuery;
	}

	@Override
	public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
		super.dump(prefix, fd, writer, args);
		writer.print(prefix);
		writer.print("mCursor=");
		writer.println(mCursor);
	}
}
