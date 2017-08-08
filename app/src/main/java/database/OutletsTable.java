package database;

/**
 * Created by Dell on 3/15/2017.
 */



public class OutletsTable {
    public static final String TABLE_OUTLETS = "outlets";
    public static final String COLUMN_OUTLET_ID = "outletId";
    public static final String COLUMN_OUTLET_NAME = "outletName";


    public static final String[] ALL_COLUMNS =
            {COLUMN_OUTLET_ID, COLUMN_OUTLET_NAME};

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_OUTLETS + "(" +
                    COLUMN_OUTLET_ID + " TEXT PRIMARY KEY," +
                    COLUMN_OUTLET_NAME + " TEXT" + ");";

    public static final String SQL_DELETE =
            "DROP TABLE " + TABLE_OUTLETS;
}
