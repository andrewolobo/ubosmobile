package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_FILE_NAME = "nadias.db";
    public static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(ItemsTable.SQL_CREATE);
        db.execSQL(CategoriesTable.SQL_CREATE);
        db.execSQL(OutletsTable.SQL_CREATE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ItemsTable.SQL_DELETE);
        db.execSQL(CategoriesTable.SQL_DELETE);
        db.execSQL(OutletsTable.SQL_DELETE);

        onCreate(db);
    }

    public void item(String insertItems) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(insertItems);
        Log.d("Data...","inserted");


           db.close(); // Closing database connection
    }

    // insert remote data

    //---insert data into SQLite DB---
    public long insert(String id , String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result ;

        ContentValues initialValues = new ContentValues();
        initialValues.put(OutletsTable.COLUMN_OUTLET_ID, id);
        initialValues.put(OutletsTable.COLUMN_OUTLET_NAME, name);
        result = db.insert(OutletsTable.TABLE_OUTLETS, null, initialValues);
        db.close();// Closing database connection
        Log.d("Outlet Data.....","Inserted");
        return result;
    }

    // update items

    public void updateItem(String updateItems) {
        SQLiteDatabase db = this.getWritableDatabase();
Log.d("SQL Query",updateItems);
        db.execSQL(updateItems);
        Log.d("Data.....","updated");


        db.close(); // Closing database connection
    }

    // update items

    public Cursor sendItem(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + ItemsTable.TABLE_ITEMS + " WHERE " + ItemsTable.COLUMN_ID + " = '" + id + "';";

        //   String selectQuery = "SELECT * FROM " + ItemsTable.TABLE_ITEMS +";";

        Log.d("SendQuery", selectQuery);

        //  Cursor cursor = db.query(ItemsTable.TABLE_ITEMS, new String[] { ItemsTable.COLUMN_NAME, ItemsTable.COLUMN_OUTLET,ItemsTable.COLUMN_PRICE,ItemsTable.COLUMN_QUANTITY,ItemsTable.COLUMN_UNIT,ItemsTable.COLUMN_LONGITUDE,ItemsTable.COLUMN_LATITUDE,ItemsTable.COLUMN_DATE,ItemsTable.COLUMN_REMARKS}, ItemsTable.COLUMN_ID + "=?",
        //         new String[] { String.valueOf(id) }, null, null, null, null);

        Cursor cursor = db.rawQuery(selectQuery, null);
        /**
        if (cursor.getCount() > 0) {

            Log.d("Set", "Set ...");

            if (cursor.moveToFirst()) {
                do {
                    //  Contact contact = new Contact();
                 System.out.println(cursor.getString(0));

                    System.out.println(cursor.getString(
                            cursor.getColumnIndex(ItemsTable.COLUMN_NAME)));
                    System.out.println(cursor.getString(
                            cursor.getColumnIndex(ItemsTable.COLUMN_OUTLET)));
                    System.out.println(cursor.getString(
                            cursor.getColumnIndex(ItemsTable.COLUMN_PRICE)));
                    System.out.println(cursor.getString(
                            cursor.getColumnIndex(ItemsTable.COLUMN_QUANTITY)));
                    System.out.println(cursor.getString(
                            cursor.getColumnIndex(ItemsTable.COLUMN_UNIT)));
                    System.out.println(cursor.getString(
                            cursor.getColumnIndex(ItemsTable.COLUMN_LONGITUDE)));
                    System.out.println(cursor.getString(
                            cursor.getColumnIndex(ItemsTable.COLUMN_LATITUDE)));
                    System.out.println(cursor.getString(
                            cursor.getColumnIndex(ItemsTable.COLUMN_DATE)));
                    System.out.println(cursor.getString(
                            cursor.getColumnIndex(ItemsTable.COLUMN_REMARKS)));

                } while (cursor.moveToNext());
            }
        } else {
            Log.d("Not Set", "Not Set");
        } **/
        return cursor;
    }
}
