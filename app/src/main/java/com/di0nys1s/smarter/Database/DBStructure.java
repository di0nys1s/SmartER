package com.di0nys1s.smarter.Database;

import android.provider.BaseColumns;

public class DBStructure {

    public static abstract class tableEntry implements BaseColumns {

        public static final String TABLE_NAME = "hourlyusage";
        public static final String COLUMN_USAGEID = "usageid";
        public static final String COLUMN_RESID = "resid";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_USAGEHOUR = "usagehour";
        public static final String COLUMN_FRIDGEUSAGE = "fridgeusage";
        public static final String COLUMN_ACUSAGE = "acusage";
        public static final String COLUMN_WMUSAGE = "wmusage";
        public static final String COLUMN_TEMPERATURE = "temperature";
    }
}
