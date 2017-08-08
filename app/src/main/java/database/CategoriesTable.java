package database;


    public class CategoriesTable {
        public static final String TABLE_ITEMS = "categories";
        public static final String COLUMN_ID = "itemId";
        public static final String COLUMN_CATEGORY = "category";


        public static final String[] ALL_COLUMNS =
                {COLUMN_ID, COLUMN_CATEGORY};

        public static final String SQL_CREATE =
                "CREATE TABLE " + TABLE_ITEMS + "(" +
                        COLUMN_ID + " TEXT PRIMARY KEY," +
                        COLUMN_CATEGORY + " TEXT" + ");";

        public static final String SQL_DELETE =
                "DROP TABLE " + TABLE_ITEMS;
    }
