package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Category;
import model.Indicator;

public class IndicatorsDataSource {

	public static final String LOGTAG="UGSTAT";

	SQLiteOpenHelper dbhelper;
	SQLiteDatabase database;

	private static final String[] allColumns = {
			IndicatorsDBOpenHelper.COLUMN_ID,
			IndicatorsDBOpenHelper.COLUMN_TITLE,
			IndicatorsDBOpenHelper.COLUMN_HEADLINE,
			IndicatorsDBOpenHelper.COLUMN_SUMMARY,
			IndicatorsDBOpenHelper.COLUMN_UNIT,
			IndicatorsDBOpenHelper.COLUMN_DESCRIPTION,
			IndicatorsDBOpenHelper.COLUMN_DATA,
			IndicatorsDBOpenHelper.COLUMN_PERIOD,
			IndicatorsDBOpenHelper.COLUMN_URL,
			IndicatorsDBOpenHelper.COLUMN_UPDATED_ON,
			IndicatorsDBOpenHelper.COLUMN_CHANGE_TYPE,
			IndicatorsDBOpenHelper.COLUMN_URL,
			IndicatorsDBOpenHelper.COLUMN_UPDATED_ON,
			IndicatorsDBOpenHelper.COLUMN_CHANGE_TYPE,
			IndicatorsDBOpenHelper.COLUMN_CHANGE_VALUE,
			IndicatorsDBOpenHelper.COLUMN_CHANGE_DESC,
			IndicatorsDBOpenHelper.COLUMN_INDEX_VALUE,
			IndicatorsDBOpenHelper.COLUMN_INDEX_CATEGORY_ID

};

	private static final String[] allCatColumns = {
			IndicatorsDBOpenHelper.COLUMN_ID,
			IndicatorsDBOpenHelper.COLUMN_INDEX_CATEGORY_ID,
			IndicatorsDBOpenHelper.COLUMN_CAT_CATEGORY_NAME

	};
	public IndicatorsDataSource(Context context) {
		dbhelper = new IndicatorsDBOpenHelper(context);
	}


	public void open() {
		Log.i(LOGTAG, "Database opened");
		database = dbhelper.getWritableDatabase();
	}

	public void close() {
		Log.i(LOGTAG, "Database closed");
		dbhelper.close();
	}

	public Indicator create(Indicator indicator) {
		ContentValues values = new ContentValues();
		values.put(IndicatorsDBOpenHelper.COLUMN_TITLE, indicator.getTitle());
		values.put(IndicatorsDBOpenHelper.COLUMN_HEADLINE, indicator.getHeadline());
		values.put(IndicatorsDBOpenHelper.COLUMN_SUMMARY, indicator.getSummary());
		values.put(IndicatorsDBOpenHelper.COLUMN_UNIT, indicator.getUnit());
		values.put(IndicatorsDBOpenHelper.COLUMN_DESCRIPTION, indicator.getDescription());
		values.put(IndicatorsDBOpenHelper.COLUMN_DATA, indicator.getData());
		values.put(IndicatorsDBOpenHelper.COLUMN_PERIOD, indicator.getPeriod());
		values.put(IndicatorsDBOpenHelper.COLUMN_URL, indicator.getUrl());
		values.put(IndicatorsDBOpenHelper.COLUMN_UPDATED_ON, indicator.getUpdated_on());
		values.put(IndicatorsDBOpenHelper.COLUMN_CHANGE_TYPE, indicator.getChangeType());
		values.put(IndicatorsDBOpenHelper.COLUMN_CHANGE_VALUE, indicator.getChange_value());
		values.put(IndicatorsDBOpenHelper.COLUMN_CHANGE_DESC, indicator.getChange_desc());
		values.put(IndicatorsDBOpenHelper.COLUMN_INDEX_CATEGORY_ID, indicator.getCat_id());



		long insertid = database.insert(IndicatorsDBOpenHelper.TABLE_INDICATORS, null, values);
		indicator.setId(insertid);
		close();
		return indicator;
	}

	// get all indicators
	public List<Indicator> findAll(String KE_CAT_ID) {
		List<Indicator> indicators = new ArrayList<Indicator>();

		Cursor cursor = database.query(IndicatorsDBOpenHelper.TABLE_INDICATORS, allColumns,
				IndicatorsDBOpenHelper.COLUMN_INDEX_CATEGORY_ID + "='" + KE_CAT_ID + "'", null, null, null, null);

		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Indicator indicator = new Indicator();

				indicator.setId(cursor.getLong(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_ID)));
				indicator.setTitle(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_TITLE)));
				indicator.setHeadline(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_HEADLINE)));
				indicator.setSummary(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_SUMMARY)));
				indicator.setDescription(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_DESCRIPTION)));
				indicator.setData(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_DATA)));
				indicator.setPeriod(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_PERIOD)));
				indicator.setUnit(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_UNIT)));
				indicator.setUrl(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_URL)));
				indicator.setUpdated_on(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_UPDATED_ON)));
				indicator.setChangeType(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_CHANGE_TYPE)));
				indicator.setChange_value(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_CHANGE_VALUE)));
				indicator.setChange_desc(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_CHANGE_DESC)));
				indicator.setIndex_value(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_INDEX_VALUE)));
				indicator.setCat_id(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_INDEX_CATEGORY_ID)));
				indicators.add(indicator);
				close();
			}
		}

		return indicators;
	}

	// update indicator
	public int updateIndicator(HashMap<String, String> queryValues) {

		ContentValues values = new ContentValues();

		// values.put(IndicatorsDBOpenHelper.COLUMN_STATUS, status);

		// DB QueryValues Object to insert into SQLite
		// queryValues = new HashMap<String, String>();
		// Add userID extracted from Object

		String id = queryValues.get("indicatorId");

		System.out.println("table ID :"+ id);

		// values.put(IndicatorsDBOpenHelper.COLUMN_ID, queryValues.get("id"));

		values.put(IndicatorsDBOpenHelper.COLUMN_TITLE, queryValues.get("title"));

		values.put(IndicatorsDBOpenHelper.COLUMN_HEADLINE,queryValues.get("headline"));
		values.put(IndicatorsDBOpenHelper.COLUMN_SUMMARY, queryValues.get("summary"));
		values.put(IndicatorsDBOpenHelper.COLUMN_UNIT, queryValues.get("unit"));
		values.put(IndicatorsDBOpenHelper.COLUMN_DESCRIPTION, queryValues.get("description"));
		values.put(IndicatorsDBOpenHelper.COLUMN_DATA, queryValues.get("data"));
		values.put(IndicatorsDBOpenHelper.COLUMN_PERIOD, queryValues.get("period"));
		values.put(IndicatorsDBOpenHelper.COLUMN_URL, queryValues.get("url"));

		values.put(IndicatorsDBOpenHelper.COLUMN_UPDATED_ON, queryValues.get("updated_on"));
		values.put(IndicatorsDBOpenHelper.COLUMN_CHANGE_TYPE,queryValues.get("change_type"));
		values.put(IndicatorsDBOpenHelper.COLUMN_CHANGE_VALUE, queryValues.get("change_value"));
		values.put(IndicatorsDBOpenHelper.COLUMN_CHANGE_DESC, queryValues.get("change_desc"));

		values.put(IndicatorsDBOpenHelper.COLUMN_INDEX_VALUE, queryValues.get("index_value"));
		values.put(IndicatorsDBOpenHelper.COLUMN_INDEX_CATEGORY_ID, queryValues.get("cat_id"));
		// values.put(ItemsTable.COLUMN_ID, id);
System.out.println("Updating items..."+id);

		// updating row
		return database.update(IndicatorsDBOpenHelper.TABLE_INDICATORS, values, IndicatorsDBOpenHelper.COLUMN_ID + "=?",
				new String[] { id });
	}

	// update get all indicators
	public Cursor findAllIndicators() {
	//	List<Indicator> indicators = new ArrayList<Indicator>();

		Cursor cursor = database.query(IndicatorsDBOpenHelper.TABLE_INDICATORS, allColumns,
				null, null, null, null, null);

		/**
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Indicator indicator = new Indicator();
				indicator.setId(cursor.getLong(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_ID)));
				indicator.setTitle(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_TITLE)));
				indicator.setHeadline(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_HEADLINE)));
				indicator.setSummary(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_SUMMARY)));
				indicator.setDescription(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_DESCRIPTION)));
				indicator.setData(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_DATA)));
				indicator.setPeriod(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_PERIOD)));
				indicator.setUrl(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_URL)));
				indicator.setUpdated_on(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_UPDATED_ON)));
				indicator.setChangeType(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_CHANGE_TYPE)));
				indicator.setChange_value(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_CHANGE_VALUE)));
				indicator.setChange_desc(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_CHANGE_DESC)));
				indicator.setIndex_value(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_INDEX_VALUE)));
				indicator.setCat_id(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_INDEX_CATEGORY_ID)));
	//			indicators.add(indicator);
				close();
			}
		}
    **/
		return cursor;
	}

	// get all native indicator categories
	public Cursor findAllNativeCategories() {


		Cursor cursor = database.query(IndicatorsDBOpenHelper.TABLE_CATEGORIES , allCatColumns,
				null, null, null, null, null);


		return cursor;
	}

// get all
// get all categories

public List<Category> findAllCategories() {
	List<Category> categories = new ArrayList<Category>();

	String orderBy = "cat_name ASC";

	Cursor cursor = database.query(IndicatorsDBOpenHelper.TABLE_CATEGORIES, allCatColumns,
			null, null, null,null, orderBy);

	Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
	if (cursor.getCount() > 0) {
		while (cursor.moveToNext()) {
			Category category = new Category();
			category.setId(cursor.getLong(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_ID)));
			category.setCat_id(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_CAT_CATEGORY_ID)));
			category.setCat_name(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_CAT_CATEGORY_NAME)));

			categories.add(category);
			close();
		}
	}

	return categories;
}

// populate items from RESTFUL web service into the indicators table

	public void insertIndicator(String title, String headline , String summary , String unit, String description, String data , String period , String url, String updated_on, String change_type,  String change_value, String change_desc , String index_value , String cat_id) {
		SQLiteDatabase database = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("title", title);
		values.put("headline", headline);
		values.put("summary", summary);
		values.put("unit", unit);
		values.put("description", description);
		values.put("data", data);
		values.put("period", period);
		values.put("url", url);
		values.put("updated_on", updated_on);
		values.put("change_type", change_type);
		values.put("change_value", change_value);
		values.put("change_desc", change_desc);
		values.put("index_value", index_value);
		values.put("cat_id", cat_id);
		database.insert("indicators", null, values);
		Log.i(LOGTAG, "inserted values" + change_type);
		close();
	}

	// populate category table with restful indicator

	public void insertCategory(String cat_id, String cat_name) {
		SQLiteDatabase database = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("cat_id", cat_id);
		values.put("cat_name", cat_name);

		database.insert("categories", null, values);
		Log.i(LOGTAG, "inserted category values" + cat_name);
		close();
	}
// populate items from RESTFUL web service into the categories table

	public void insertCategories(String id, String cat_name) {
		SQLiteDatabase database = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("cat_id", id);
		values.put("cat_name", cat_name);
		database.insert("categories", null, values);
		Log.i(LOGTAG, "inserted values" + "category values");
		close();
	}

// get category specific items from the indicators table

	public List<Indicator> getSpecificCategories(String CatID) {

		List<Indicator> indicators = new ArrayList<Indicator>();

		Cursor cursor = database.query(IndicatorsDBOpenHelper.TABLE_INDICATORS, allColumns,
				IndicatorsDBOpenHelper.COLUMN_INDEX_CATEGORY_ID + "='" + CatID + "'", null, null, null, null);

		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Indicator indicator = new Indicator();
				indicator.setId(cursor.getLong(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_ID)));
				indicator.setTitle(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_TITLE)));
				indicator.setHeadline(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_HEADLINE)));
				indicator.setSummary(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_SUMMARY)));
				indicator.setDescription(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_DESCRIPTION)));
				indicator.setData(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_DATA)));
				indicator.setPeriod(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_PERIOD)));
				indicator.setUnit(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_UNIT)));
				indicator.setUrl(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_URL)));
				indicator.setUpdated_on(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_UPDATED_ON)));
				indicator.setChangeType(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_CHANGE_TYPE)));
				indicator.setChange_value(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_CHANGE_VALUE)));
				indicator.setChange_desc(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_CHANGE_DESC)));
				indicator.setIndex_value(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_INDEX_VALUE)));
				indicator.setCat_id(cursor.getString(cursor.getColumnIndex(IndicatorsDBOpenHelper.COLUMN_INDEX_CATEGORY_ID)));
				indicators.add(indicator);
				close();
			}
		}

		return indicators;
	}


    public Cursor getMissingCategories(String CatName) {

        Cursor cursor = database.query(IndicatorsDBOpenHelper.TABLE_CATEGORIES,allCatColumns,
                IndicatorsDBOpenHelper.COLUMN_CAT_CATEGORY_NAME + "='" + CatName + "'", null, null, null, null);

        return cursor;
    }

}
