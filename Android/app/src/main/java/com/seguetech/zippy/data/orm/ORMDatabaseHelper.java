package com.seguetech.zippy.data.orm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.seguetech.zippy.R;
import com.seguetech.zippy.data.model.rss.Item;

import java.sql.SQLException;

import timber.log.Timber;

/**
 * This is a helper class for manipulating the Sqlite database through ORMLite.
 * @author andrachekm
 *
 */
public class ORMDatabaseHelper extends OrmLiteSqliteOpenHelper {
	public static final String DATABASE_NAME="news.db";
	private static final int DATABASE_VERSION=1;
	public static final String TAG = ORMDatabaseHelper.class.getName();

	@SuppressWarnings("unused")
	private final Context mContext;
	private static volatile ORMDatabaseHelper _instance;

	private ORMDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
		mContext = context.getApplicationContext();
	}

	public synchronized static ORMDatabaseHelper getInstance(Context context) {
		if (_instance == null) {
			synchronized(ORMDatabaseHelper.class){
				_instance = new ORMDatabaseHelper(context.getApplicationContext());
			}
		}
		return _instance;
	}


	/**
	 * This method is used to create the database and initialize the data in it.
	 */
	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		try {
			TableUtils.createTableIfNotExists(connectionSource, Item.class);
		}
		catch (SQLException e) {
			Timber.e(e, "An error occurred while attempting to setup the database.");
		}
	}

	/**
	 * This method is used for performing programmatic database upgrades.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		Log.e(TAG,"Old Version: " + oldVersion + ", New Version: " + newVersion);
	}


}
