package com.yesia.crudsqlite.db;

import android.provider.BaseColumns;

public class DataDiriContract {
    public static String TABLE_NAME = "orang";

    public static final class BiodataColumn implements BaseColumns {

        public static final String COLUMN_NAMA = "name";
        public static final String COLUMN_UMUR = "umur";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String TANGGAL = "tanggal";

    }
}
