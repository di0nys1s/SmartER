package com.di0nys1s.smarter.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "smartER.db";
    private final Context context;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private String[] columns = {
            DBStructure.tableEntry.COLUMN_USAGEID,
            DBStructure.tableEntry.COLUMN_RESID,
            DBStructure.tableEntry.COLUMN_DATE,
            DBStructure.tableEntry.COLUMN_USAGEHOUR,
            DBStructure.tableEntry.COLUMN_FRIDGEUSAGE,
            DBStructure.tableEntry.COLUMN_ACUSAGE,
            DBStructure.tableEntry.COLUMN_WMUSAGE,
            DBStructure.tableEntry.COLUMN_TEMPERATURE,
    };

    private static final String SQL_CREATE_ENTRIES =

            "CREATE TABLE " + DBStructure.tableEntry.TABLE_NAME + " (" +
                    DBStructure.tableEntry.COLUMN_USAGEID + " INTEGER PRIMARY KEY," +
                    DBStructure.tableEntry.COLUMN_RESID + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_DATE + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_USAGEHOUR + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_FRIDGEUSAGE + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_ACUSAGE + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_WMUSAGE + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_TEMPERATURE + TEXT_TYPE +
                    " );";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBStructure.tableEntry.TABLE_NAME;


    private MySQLiteOpenHelper myDBHelper;
    private SQLiteDatabase db;

    public DBManager(Context ctx) {
        this.context = ctx;
        myDBHelper = new MySQLiteOpenHelper(context);
    }

    private static class MySQLiteOpenHelper extends SQLiteOpenHelper {

        public MySQLiteOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply t o discard the data and start over db.execSQL(SQL_DELETE_ENTRIES);
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    public DBManager open() throws SQLException {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        myDBHelper.close();
    }

    public long insertUsage(Integer resid, String date,
                            Integer usagehour, Double fridgeusage,
                            Double acusage, Double wmusage,
                            Double temperature) {

        ContentValues values = new ContentValues();
        values.put(DBStructure.tableEntry.COLUMN_RESID, resid);
        values.put(DBStructure.tableEntry.COLUMN_DATE, date);
        values.put(DBStructure.tableEntry.COLUMN_USAGEHOUR, usagehour);
        values.put(DBStructure.tableEntry.COLUMN_FRIDGEUSAGE, fridgeusage);
        values.put(DBStructure.tableEntry.COLUMN_ACUSAGE, acusage);
        values.put(DBStructure.tableEntry.COLUMN_WMUSAGE, wmusage);
        values.put(DBStructure.tableEntry.COLUMN_TEMPERATURE, temperature);

        return db.insert(DBStructure.tableEntry.TABLE_NAME, null, values);
    }

    public int deleteUsageHour(String rowId) {

        String[] selectionArgs = { String.valueOf(rowId) };
        String selection = DBStructure.tableEntry.COLUMN_USAGEHOUR + " LIKE ?";

        return db.delete(DBStructure.tableEntry.TABLE_NAME, selection, selectionArgs);
    }

    public Cursor getAllAppliances() {
        return db.query(DBStructure.tableEntry.TABLE_NAME, columns, null,
                null, null, null, null, null);
    }

    public Cursor getFridgeUsage(String usageId) throws SQLException {
        String[] selectionArgs = { String.valueOf(usageId) };
        String selection = DBStructure.tableEntry.COLUMN_FRIDGEUSAGE + " LIKE ?";
        Cursor cursor = db.query(DBStructure.tableEntry.TABLE_NAME, columns,
                selection, selectionArgs, null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public Cursor getUserUsage(String id) throws SQLException {
        String[] selectionArgs = {String.valueOf(id) };
        String selection = DBStructure.tableEntry.COLUMN_RESID + " LIKE ?";
        Cursor cursor = db.query(DBStructure.tableEntry.TABLE_NAME, columns,
                selection, selectionArgs, null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public int deleteUsageById(int resId) {

        String[] selectionArgs = { String.valueOf(resId) };
        String selection = DBStructure.tableEntry.COLUMN_RESID + " LIKE ?";

        return db.delete(DBStructure.tableEntry.TABLE_NAME, selection,selectionArgs );
    }

}
