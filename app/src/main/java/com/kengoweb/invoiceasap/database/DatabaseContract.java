package com.kengoweb.invoiceasap.database;

import android.provider.BaseColumns;

/**
 * Created by vokrut on 11.10.2016.
 */

public final class DatabaseContract {
    public DatabaseContract() {
    }

    public static abstract class LocationsTable implements BaseColumns {
        public static final String TABLE_NAME = "locations";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_GEO_LAT = "latitude";
        public static final String COLUMN_GEO_LNG = "longitude";
    }

    public static abstract class BeersTable implements BaseColumns {
        static final String TABLE_NAME = "beers";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TYPE = "type";
    }

}
