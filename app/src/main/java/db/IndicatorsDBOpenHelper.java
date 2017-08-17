package db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class IndicatorsDBOpenHelper extends SQLiteOpenHelper {

	private static final String LOGTAG = "UBOSAPP";

	private static final String DATABASE_NAME = "indicators.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_INDICATORS = "indicators";
	public static final String TABLE_CATEGORIES = "categories";

	public static final String COLUMN_ID = "indicatorId";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_HEADLINE = "headline";
	public static final String COLUMN_SUMMARY = "summary";
	public static final String COLUMN_UNIT = "unit";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_DATA= "data";
	public static final String COLUMN_PERIOD = "period";
	public static final String COLUMN_URL= "url";
	public static final String COLUMN_UPDATED_ON= "updated_on";
	public static final String COLUMN_CHANGE_TYPE= "change_type";
	public static final String COLUMN_CHANGE_VALUE= "change_value";
	public static final String COLUMN_CHANGE_DESC= "change_desc";
	public static final String COLUMN_INDEX_VALUE= "index_value";
	public static final String COLUMN_INDEX_CATEGORY_ID= "cat_id";
	public static final String COLUMN_CAT_CATEGORY_ID= "cat_id";
	public static final String COLUMN_CAT_CATEGORY_NAME= "cat_name";



	/** An instance variable for SQLiteDatabase */
	private SQLiteDatabase mDB;

	/** Constructor */


	private static final String TABLE_CREATE =
			"CREATE TABLE " + TABLE_INDICATORS + " (" +
					COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					COLUMN_TITLE + " TEXT," +
					COLUMN_HEADLINE + " TEXT," +
					COLUMN_SUMMARY + " TEXT," +
					COLUMN_UNIT + " TEXT," +
					COLUMN_DESCRIPTION + " TEXT," +
					COLUMN_DATA + " TEXT," +
					COLUMN_PERIOD + " TEXT," +
					COLUMN_URL + " TEXT," +
					COLUMN_UPDATED_ON + " TEXT," +
					COLUMN_CHANGE_TYPE + " TEXT," +
					COLUMN_CHANGE_VALUE + " TEXT," +
					COLUMN_CHANGE_DESC + " TEXT," +
					COLUMN_INDEX_VALUE + " TEXT," +
	                COLUMN_INDEX_CATEGORY_ID + " TEXT" + ");";

	private static final String TABLE_CREATE_CAT =
			"CREATE TABLE " + TABLE_CATEGORIES + " (" +
					COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					COLUMN_CAT_CATEGORY_ID + " TEXT," +
					COLUMN_CAT_CATEGORY_NAME + " TEXT" + ");";




	public IndicatorsDBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		//this.mDB = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
		Log.i(LOGTAG, "Indicators Table Created");
		db.execSQL(TABLE_CREATE_CAT);
		Log.i(LOGTAG, "Categories Table Created");

		Log.i(LOGTAG, "Tables has been created");
	}


	/** Returns all the customers in the table */
	/** public Cursor getAllCustomers(){

		return mDB.query(TABLE_CREATE, new String[] { COLUMN_ID,  COLUMN_TITLE , COLUMN_HEADLINE, COLUMN_SUMMARY , COLUMN_UNIT , COLUMN_DESCRIPTION,COLUMN_DATA ,COLUMN_PERIOD,COLUMN_URL ,COLUMN_UPDATED_ON,COLUMN_CHANGE_TYPE,COLUMN_CHANGE_VALUE,COLUMN_CHANGE_DESC,COLUMN_INDEX_VALUE,COLUMN_INDEX_CATEGORY_ID } ,
				null, null, null, null,
				COLUMN_ID + " asc ");

	}
**/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INDICATORS);
		onCreate(db);
	}

}
