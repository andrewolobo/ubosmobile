package database;


    public class ItemsTable {
        public static final String TABLE_ITEMS = "items";
        public static final String COLUMN_ID = "itemId";
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

        public static final String[] ALL_COLUMNS =
                {COLUMN_ID, COLUMN_TITLE, COLUMN_HEADLINE,
                        COLUMN_SUMMARY,COLUMN_UNIT, COLUMN_DESCRIPTION,COLUMN_DATA,COLUMN_PERIOD,COLUMN_URL,COLUMN_UPDATED_ON,COLUMN_CHANGE_TYPE};

        public static final String SQL_CREATE =
                "CREATE TABLE " + TABLE_ITEMS + "(" +
                        COLUMN_ID + " TEXT PRIMARY KEY," +
                        COLUMN_TITLE + " TEXT," +
                        COLUMN_HEADLINE + " TEXT," +
                        COLUMN_SUMMARY + " TEXT," +
                        COLUMN_UNIT + " TEXT," +
                        COLUMN_DESCRIPTION + " TEXT," +
                        COLUMN_DATA + " TEXT," +
                        COLUMN_PERIOD + " TEXT," +
                        COLUMN_URL + " TEXT," +
                        COLUMN_UPDATED_ON + " TEXT," +
                        COLUMN_CHANGE_TYPE + " TEXT" + ");";

        public static final String SQL_DELETE =
                "DROP TABLE " + TABLE_ITEMS;
    }
