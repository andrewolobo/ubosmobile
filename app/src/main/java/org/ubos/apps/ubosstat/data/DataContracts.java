package org.ubos.apps.ubosstat.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import java.util.HashMap;

public final class DataContracts {
	public static final String AUTHORITY = "org.ubos.apps.ubosstat.todosprovider";

	public static final String FLAG_NOTIFY_CHANGE = "notify_change";

	private DataContracts() {
	}

	public static final class Indicators implements BaseColumns {
		private Indicators() {
		}

		public static final String TABLE_NAME = "items";
		public static final String URI_PATH = TABLE_NAME;
		public static final String URI_PATH_ID = TABLE_NAME + "/#";
		public static final int ITEM_ID_PATH_POSITION = 1;
		private static final String SCHEME = "content://";
		private static final String PATH_ITEMS = "/categories";
		private static final String PATH_ITEM_ID = "/categories/";
		public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_ITEMS);
		public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_ITEM_ID);
		public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse(SCHEME + AUTHORITY + PATH_ITEM_ID + "/#");
		public static final String CONTENT_TYPE = "vnd.linkedbytes.cursor.dir/vnd.fuelhoy.state";
		public static final String CONTENT_ITEM_TYPE = "vnd.linkedbytes.cursor.item/vnd.fuelhoy.state";

		// CONTENT OBSERVERS NOTIFICATIONS BEHAVIOUR

		public static final boolean NOTIFY_DELETE = false;
		public static final boolean NOTIFY_INSERT = false;

		// COLUMNS

		/**
		 * Province code
		 * <P>Type: TEXT NOT NULL</P>
		 */
		//column (field) names
		public static final String COLUMN_ID = BaseColumns._ID;
		public static final String COLUMN_TITLE = "title";
		public static final String COLUMN_HEADLINE = "headline";
		public static final String COLUMN_SUMMARY = "summary";
		public static final String COLUMN_UNIT = "unit";
		public static final String COLUMN_DESCRIPTION = "description";
		public static final String COLUMN_DATA = "data";
		public static final String COLUMN_PERIOD = "period";
		public static final String COLUMN_URL = "url";
		public static final String COLUMN_UPDATED_ON = "updated_on";
		/**
		 * Default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = COLUMN_TITLE + " ASC";


		// PROJECTION MAP

		public static final HashMap<String, String> sProjectionMap = new HashMap<String, String>();

		static {
			sProjectionMap.put(_ID, _ID);
			sProjectionMap.put(COLUMN_TITLE, COLUMN_TITLE);
			sProjectionMap.put(COLUMN_HEADLINE, COLUMN_HEADLINE);
		}

		// Static helper methods

		public static String setUpQueryBuilder(SQLiteQueryBuilder qb, long itemId, String orderBy) {
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(sProjectionMap);
			if (itemId != -1) {
				qb.appendWhere(_ID + "=" + itemId);
			}
			if (TextUtils.isEmpty(orderBy)) {
				orderBy = DEFAULT_SORT_ORDER;
			}

			return orderBy;
		}

		/**
		 * public static ContentValues getInsertContentValuesWithoutNotify(String code, String name)
		 * {
		 * ContentValues result = new ContentValues();
		 * <p>
		 * result.put(States.COLUMN_CODE, code);
		 * result.put(States.COLUMN_NAME, name);
		 * <p>
		 * return result;
		 * }
		 */

		/**
		public static ContentValues getInsertContentValues(String code, String name) {
			return getInsertContentValues(code, name, NOTIFY_INSERT);
		}
		 */

		/**
		 public static ContentValues getInsertContentValues(String code, String name, boolean notifyChange)
		 {
		 ContentValues result = new ContentValues();

		 result.put(States.COLUMN_CODE, code);
		 result.put(States.COLUMN_NAME, name);
		 result.put(DataContracts.FLAG_NOTIFY_CHANGE, notifyChange);

		 return result;
		 }
		 **/
	}
}
