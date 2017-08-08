package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import model.DataItem;
import model.NavDrawerItem;
import model.Outlet;

import java.util.ArrayList;
import java.util.List;


public class DataSource {

    private Context mContext;
    private SQLiteDatabase mDatabase;
    SQLiteOpenHelper mDbHelper;
    ArrayList UpdateRecView ;
    String itemID,  outletName , outletNameID;

    public DataSource(Context context) {
        this.mContext = context;
        mDbHelper = new DBHelper(mContext);
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void open() {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    /**  Item table SQL functions   **/


/**
    public Outlet createOutletItem(Outlet item) {
        ContentValues values = item.toValues();

        Log.d("Insert Outlets","Heere we are");
      mDatabase.insert(OutletsTable.TABLE_OUTLETS, null, values);
        return item;
    }
    /**  Insert Remote Outlets  **/
/**
    public void insertOutletItem()
    {



    }



    public long getOutletItemsCount() {
        return DatabaseUtils.queryNumEntries(mDatabase, OutletsTable.TABLE_OUTLETS);
    }
*/

public long getDataItemsCount() {
    return DatabaseUtils.queryNumEntries(mDatabase, ItemsTable.TABLE_ITEMS);

}

/**
    public void populateOutletsTable(List<Outlet> OutletItem) {
        Log.d("Outlet item" , "Popp ....");
        long numItems = getOutletItemsCount();
        if (numItems == 0) {
            for (Outlet item :
                    OutletItem) {
                try {
                    createOutletItem(item);
                    Log.d("Outlet item" , "Success" + item);
                } catch (SQLiteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
**/
    public void seedNavDatabase(List<NavDrawerItem> navDrawerItemList) {
        long numItems = getDataItemsCount();
        if (numItems == 0) {
            for (NavDrawerItem item :
                    navDrawerItemList) {
                try {
                    createNavItem(item);

                } catch (SQLiteException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public Cursor getAllOutlets() {

       // List<Outlet> itemoutlet = new ArrayList<>();

        Cursor cursor = mDatabase.query(OutletsTable.TABLE_OUTLETS, OutletsTable.ALL_COLUMNS,
                null, null, null, null, null);

      /**  while (cursor.moveToNext()) {
            Outlet item = new Outlet();
            item.setoutletId(cursor.getString(
                    cursor.getColumnIndex(OutletsTable.COLUMN_OUTLET_ID)));
           // Log.d("Column ID",cursor.getString(
                    cursor.getColumnIndex(OutletsTable.COLUMN_OUTLET_ID)));
            item.setOutletName(cursor.getString(
                    cursor.getColumnIndex(OutletsTable.COLUMN_OUTLET_NAME)));


            itemoutlet.add(item);
        }
       **/
       // cursor.close();
        return cursor;
    }

    /** Navigation item SQL functions     */

    public NavDrawerItem createNavItem(NavDrawerItem item) {
        ContentValues values = item.toValues();
        Log.d("Cat","Insert");
        mDatabase.insert(CategoriesTable.TABLE_ITEMS, null, values);
        return item;
    }

    public long getNavDataItemsCount() {
        return DatabaseUtils.queryNumEntries(mDatabase, CategoriesTable.TABLE_ITEMS);
    }



    public List<NavDrawerItem> getNavAllItems() {
        List<NavDrawerItem> navDataItems = new ArrayList<>();

        Cursor cursor = mDatabase.query(CategoriesTable.TABLE_ITEMS, CategoriesTable.ALL_COLUMNS,
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            NavDrawerItem item = new NavDrawerItem();

            item.setItemId(cursor.getString(
                    cursor.getColumnIndex(CategoriesTable.COLUMN_ID)));
            item.setCategory(cursor.getString(
                    cursor.getColumnIndex(CategoriesTable.COLUMN_CATEGORY)));

            navDataItems.add(item);



        }

        return navDataItems;
    }
    public Cursor getCNavAllItems() {
        //  List<NavDrawerItem> navDataItems = new ArrayList<>();

        Cursor NavCursor = mDatabase.query(CategoriesTable.TABLE_ITEMS, CategoriesTable.ALL_COLUMNS,
                null, null, null, null, null);

        return NavCursor;
    }



    public Cursor getRMoutlets() {
        //  List<NavDrawerItem> navDataItems = new ArrayList<>();

        Cursor NavCursor = mDatabase.query(OutletsTable.TABLE_OUTLETS, OutletsTable.ALL_COLUMNS,
                null, null, null, null, null);

        return NavCursor;
    }

/**
    public List getDrawCatItems(Integer position) {

  //      ArrayList<String> mArrayList = new ArrayList<String>();

        List<DataItem> dataItems = new ArrayList<>();

     //   String sPosition = String.valueOf(position) ;

       int sPosition = position + 1 ;

        String[] columns ={ItemsTable.COLUMN_NAME};

        Cursor cursor = mDatabase.query(ItemsTable.TABLE_ITEMS, ItemsTable.ALL_COLUMNS,
                    ItemsTable.COLUMN_OUTLET + "='" + sPosition + "'", null, null, null, null);
/**
        while (UpdateCursor.moveToNext()) {

            Log.d("Items",UpdateCursor.getString(
                    UpdateCursor.getColumnIndex(ItemsTable.COLUMN_NAME)));

            mArrayList.add(UpdateCursor.getString(
                    UpdateCursor.getColumnIndex(ItemsTable.COLUMN_NAME)));

        }

        return mArrayList;


    }
**/
/**
    while (cursor.moveToNext()) {
        DataItem item = new DataItem();
        item.setItemId(cursor.getString(
                cursor.getColumnIndex(ItemsTable.COLUMN_ID)));
        item.setItemName(cursor.getString(
                cursor.getColumnIndex(ItemsTable.COLUMN_NAME)));

        item.setOutlet(cursor.getString(
                cursor.getColumnIndex(ItemsTable.COLUMN_OUTLET)));

        item.setPrice(cursor.getInt(
                cursor.getColumnIndex(ItemsTable.COLUMN_PRICE)));

        item.setQuantity(cursor.getDouble(
                cursor.getColumnIndex(ItemsTable.COLUMN_QUANTITY)));

        item.setUnit(cursor.getString(
                cursor.getColumnIndex(ItemsTable.COLUMN_UNIT)));

        item.setLongitude(cursor.getDouble(
                cursor.getColumnIndex(ItemsTable.COLUMN_LONGITUDE)));

        item.setLatitude(cursor.getDouble(
                cursor.getColumnIndex(ItemsTable.COLUMN_LATITUDE)));

        item.setDate(cursor.getString(
                cursor.getColumnIndex(ItemsTable.COLUMN_DATE)));

        item.setRemarks(cursor.getString(
                cursor.getColumnIndex(ItemsTable.COLUMN_REMARKS)));
        dataItems.add(item);
    }

    return dataItems;

}


    public int updateItem(String title, String price, int id) {

        ContentValues values = new ContentValues();
        values.put(ItemsTable.COLUMN_QUANTITY, title);
        values.put(ItemsTable.COLUMN_PRICE, price);


        // updating row
        return mDatabase.update(ItemsTable.TABLE_ITEMS, values, ItemsTable.COLUMN_ID + "  ?",
                new String[] { String.valueOf(id) });
    }


  public String getItemID(String title)
  {


      String[] columns ={ItemsTable.COLUMN_ID};

      Cursor UpdateCursor = mDatabase.query(ItemsTable.TABLE_ITEMS, ItemsTable.ALL_COLUMNS,
              ItemsTable.COLUMN_NAME + "='" + title + "'", null, null, null, null);

      while (UpdateCursor.moveToNext()) {

          itemID =  UpdateCursor.getString(UpdateCursor.getColumnIndex(ItemsTable.COLUMN_ID));

      }

      return itemID ;
  }



// get outlet name

    public String getOutletName(String position)
    {


        String[] columns ={ItemsTable.COLUMN_ID};

        Cursor getOutletCursor = mDatabase.query(OutletsTable.TABLE_OUTLETS, OutletsTable.ALL_COLUMNS,
                OutletsTable.COLUMN_OUTLET_ID + "='" + position + "'", null, null, null, null);

        while (getOutletCursor.moveToNext()) {

            outletName =  getOutletCursor.getString(getOutletCursor.getColumnIndex(OutletsTable.COLUMN_OUTLET_NAME));

        }

        return  outletName ;
    }
**/


}

