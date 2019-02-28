package com.yesia.crudsqlite.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataDiriDbHelper extends SQLiteOpenHelper {
    // nama database
    public static String DATABASE_NAME = "biodata.db";

    // versi database
    public static final int DATABASE_VERSION = 1;

    // pembuatan table dan column database
    private static final String SQL_CREATE_TABLE_BIODATA = String.format("CREATE TABLE %s"
                    + "(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT NOT NULL)",
            DataDiriContract.TABLE_NAME,
            DataDiriContract.BiodataColumn._ID,
            DataDiriContract.BiodataColumn.COLUMN_NAMA,
            DataDiriContract.BiodataColumn.COLUMN_UMUR,
            DataDiriContract.BiodataColumn.COLUMN_GENDER,
            DataDiriContract.BiodataColumn.COLUMN_WEIGHT,
            DataDiriContract.BiodataColumn.TANGGAL);


    // constructor harus memasukan parameter nama database dan version database
    public DataDiriDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // method ini dipanggil pertama kali dipanggil pada saat aplikasi diinstall
    @Override
    public void onCreate(SQLiteDatabase db) {

        // query sql dibawah ini difungsikan untuk membuat sebuah table database
        db.execSQL(SQL_CREATE_TABLE_BIODATA);
    }


    /**method ini akan dipanggil ketika ada perbedaan version database pada device
    dan pada aplikasi yang akan kita install
    didalam method ini kita perlu menyesuaikan version database yang lama
    supaya bisa digunakan didalam vesion yang baru / perlu migrasi data*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXIT%s", DataDiriContract.TABLE_NAME));
        onCreate(db);
    }
}
