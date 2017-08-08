package org.ubos.apps.ubosstat.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import database.DBHelper;

import static org.ubos.apps.ubosstat.data.TodosContract.CONTENT_AUTHORITY;
import static org.ubos.apps.ubosstat.data.TodosContract.PATH_TODOS;
import static org.ubos.apps.ubosstat.data.TodosContract.TodosEntry;


public class TodosProvider extends ContentProvider {
    //constants for the operation
    private static final int TODOS = 1;
    private static final int TODOS_ID = 2;
    //urimatcher
    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_TODOS, TODOS);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_TODOS + "/#", TODOS_ID);

    }

    private DBHelper helper;
    @Override
    public boolean onCreate() {
        helper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        //get db
        SQLiteDatabase db = helper.getReadableDatabase();

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        //cursor
        Cursor cursor;
        //our integer
        int match = uriMatcher.match(uri);
        //intables
        SQLiteQueryBuilder builder;
        switch (match) {
            case TODOS:
                Log.i("CP",  "TODOCS");
                builder = new SQLiteQueryBuilder();
                builder.setTables(TodosEntry.TABLE_NAME);
                String order = "orderB";
                cursor = db.query(TodosEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, orderBy);

                break;
            case TODOS_ID:
                selection = TodosEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(TodosEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, orderBy);
                break;

            default:
                throw new IllegalArgumentException("Query unknown URI: " + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case TODOS:
                return insertRecord(uri, contentValues, TodosEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("Insert unknown URI: " + uri);
        }
    }

    private Uri insertRecord(Uri uri, ContentValues values, String table) {
        //this time we need a writable database
        SQLiteDatabase db = helper.getWritableDatabase();
        long id = db.insert(table, null, values);
        if (id == -1) {
            Log.e("Error", "insert error for URI " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case TODOS:
                return deleteRecord(uri, null, null, TodosEntry.TABLE_NAME);
            case TODOS_ID:
                return deleteRecord(uri, selection, selectionArgs, TodosEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("Insert unknown URI: " + uri);
        }
    }

    private int deleteRecord(Uri uri, String selection, String[] selectionArgs, String tableName) {
        //this time we need a writable database
        SQLiteDatabase db = helper.getWritableDatabase();
        int id = db.delete(tableName, selection, selectionArgs);
        if (id == -1) {
            Log.e("Error", "delete unknown URI " + uri);
            return -1;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case TODOS:
                return updateRecord(uri, values, selection, selectionArgs, TodosEntry.TABLE_NAME);

            default:
                throw new IllegalArgumentException("Update unknown URI: " + uri);
        }
    }

    private int updateRecord(Uri uri, ContentValues values, String selection, String[] selectionArgs, String tableName) {
        //this time we need a writable database
        SQLiteDatabase db = helper.getWritableDatabase();
        int id = db.update(tableName, values, selection, selectionArgs);
        if (id == 0) {
            Log.e("Error", "update error for URI " + uri);
            return -1;
        }
        return id;
    }
}
