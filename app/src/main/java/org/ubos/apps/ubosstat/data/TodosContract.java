package org.ubos.apps.ubosstat.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class TodosContract {
    //URI section
    public static final String CONTENT_AUTHORITY = "org.ubos.apps.ubosstat.todosprovider";
    public static final String PATH_TODOS="todos";
    public static final String PATH_CATEGORIES="categories";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public String concatContent(String path){
        return "content://" + path;
    }

    public static final class TodosEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TODOS);

        // Table name
        public static final String TABLE_NAME = "items";
        //column (field) names
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_HEADLINE = "headline";
        public static final String COLUMN_SUMMARY = "summary";
        public static final String COLUMN_UNIT = "unit";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DATA= "data";
        public static final String COLUMN_PERIOD = "period";
        public static final String COLUMN_URL= "url";
        public static final String COLUMN_UPDATED_ON= "updated_on";
    }

    /**
    public static final class CategoriesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CATEGORIES);

        // Table name
        public static final String TABLE_NAME = "items";
        //column names
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_DESCRIPTION = "description";
    }

     **/
}
