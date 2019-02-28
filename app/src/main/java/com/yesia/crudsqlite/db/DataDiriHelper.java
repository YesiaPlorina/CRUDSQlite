package com.yesia.crudsqlite.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.yesia.crudsqlite.model.DataDiri;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.yesia.crudsqlite.db.DataDiriContract.BiodataColumn.COLUMN_GENDER;
import static com.yesia.crudsqlite.db.DataDiriContract.BiodataColumn.COLUMN_NAMA;
import static com.yesia.crudsqlite.db.DataDiriContract.BiodataColumn.COLUMN_UMUR;
import static com.yesia.crudsqlite.db.DataDiriContract.BiodataColumn.COLUMN_WEIGHT;
import static com.yesia.crudsqlite.db.DataDiriContract.BiodataColumn.TANGGAL;
import static com.yesia.crudsqlite.db.DataDiriContract.TABLE_NAME;

public class DataDiriHelper {

    private static String DATABASE_TABEL = TABLE_NAME;
    private DataDiriDbHelper biodataDbHelper;
    private SQLiteDatabase database;
    private Context context;

    public DataDiriHelper(Context context) {
        this.context = context;
    }


    // method untuk membuat dan membaca data database
    public DataDiriHelper open() throws SQLException {
        biodataDbHelper = new DataDiriDbHelper(context);
        database = biodataDbHelper.getWritableDatabase();
        return this;
    }


    // jika sudah dibuat close database nya
    public void close() {
        biodataDbHelper.close();
    }


    // pada proses load data dilakukan dengan eksekusi query()
    public ArrayList<DataDiri> query() {
        ArrayList<DataDiri> arrayList = new ArrayList<DataDiri>();

        // cursor digunakan ambil data hasil dari query
        // lalu ditampung dalam cursor
        Cursor cursor = database.query(DATABASE_TABEL,
                null,
                null,
                null,
                null,
                null,
                _ID + " DESC",
                null);
        cursor.moveToFirst();

        DataDiri biodata;
        if (cursor.getCount() > 0) {

            // looping untuk mengakses data satu persatu didalam cursor
            do {

                biodata = new DataDiri();
                biodata.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                biodata.setName(cursor.getString(cursor.getColumnIndexOrThrow(
                        COLUMN_NAMA)));
                biodata.setAge(cursor.getString(cursor.getColumnIndexOrThrow(
                        COLUMN_UMUR)));
                biodata.setGender(cursor.getString(cursor.getColumnIndexOrThrow(
                        COLUMN_GENDER)));
                biodata.setWeight(cursor.getString(cursor.getColumnIndexOrThrow(
                        COLUMN_WEIGHT)));
                biodata.setDate(cursor.getString(cursor.getColumnIndexOrThrow(
                        TANGGAL)));

                // data yang sudah diakses tampung ke arraylist dan tambahkan
                // lalu ditampung kedalam class model
                arrayList.add(biodata);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }

        // tutup setelah mengakses data
        cursor.close();

        // kembalian data nya ke dalam bentuk arraylsit
        return arrayList;

    }


    // insert data
    public long insert(DataDiri biodata) {

        ContentValues values = new ContentValues();

        values.put(COLUMN_NAMA,
                biodata.getName());
        values.put(COLUMN_UMUR,
                biodata.getAge());
        values.put(COLUMN_GENDER,
                biodata.getGender());
        values.put(COLUMN_WEIGHT,
                biodata.getWeight());
        values.put(TANGGAL,
                biodata.getDate());

        return database.insert(DATABASE_TABEL, null, values);
    }

    //update data
    public long update(DataDiri biodata) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA,
                biodata.getName());
        values.put(COLUMN_UMUR,
                biodata.getAge());
        values.put(COLUMN_GENDER,
                biodata.getGender());
        values.put(COLUMN_WEIGHT,
                biodata.getWeight());
        values.put(TANGGAL,
                biodata.getDate());

        return database.update(
                DATABASE_TABEL, values,
                _ID
                        + " = '"
                        + biodata.getId()
                        + "'", null);

    }


    // delete data
    public int delete(int id) {
        return database.delete(
                TABLE_NAME, _ID
                        + " = '" + id
                        + "'", null);

    }
}
