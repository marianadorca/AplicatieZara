package com.example.madalina.aplicatiezara;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String COLUMN_ID = "_id";
    public static final String NUME_TABELA = "produse";
    public static final String NUME_PRODUS = "NumeProdus";
    public static final String PRET_PRODUST = "PretProdus";
    public static final String LOCATIE_POZAT = "Locatie";

    private static final String DATABASE_NAME = "magazin.db";
    private static final int DATABASE_VERSION = 1;

    // sintaxa creare baza de date
    private static final String DATABASE_CREATE = "create table "
            + NUME_TABELA + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + NUME_PRODUS
            + " text not null, " + PRET_PRODUST
            + " integer not null, " + LOCATIE_POZAT
            + " text );";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    //stergere tabela daca se trece la alta versiunde de baza de date
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + NUME_TABELA);
        onCreate(db);
    }

}