package com.example.madalina.aplicatiezara;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private MySQLiteHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    // deschidere baza de date
    public DBManager open() throws SQLException {
        dbHelper = new MySQLiteHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    //inchidere baza de date
    public void close() {
        dbHelper.close();
    }

    //Insereaza in baza de date produsul folosind numele, pretul si adresa de stocare a imaginii
    public void insert(String name, int price, String url) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(MySQLiteHelper.NUME_PRODUS, name);
        contentValue.put(MySQLiteHelper.PRET_PRODUST, price);
        contentValue.put(MySQLiteHelper.LOCATIE_POZAT, url);
        try {
            database.insert(MySQLiteHelper.NUME_TABELA, null, contentValue);
        }  catch (Exception e)
        {
            Log.e("ERROR", e.toString());
        }
        System.out.println("Date introduse");
    }

    public ArrayList<DataModel> getAllCotacts() {
        ArrayList<DataModel> array_list = new ArrayList<DataModel>();

        Cursor res =  database.rawQuery( "select * from produse", null );
        res.moveToFirst();

        while(res.isAfterLast() == false) {
            //System.out.println(res.getString(0));
            //afisare date luate din baza de date pentru testare
            System.out.println(res.getString(1));
            System.out.println(res.getString(2));
            System.out.println(res.getString(3));
            String nume = res.getString(1);
            int pret = res.getInt(2);
            // creare Bitmap folosind adresa de stocare a fisierului
            String path = res.getString(3);
            //deschidere fisier dupa locatia din telefon
            File imgFile = new File(path);
            //initializare bitmap null
            Bitmap myBitmap = null;
            //verificam daca imaginea mai exista in telefon
            if (imgFile.exists()) {
                //decodam fisierul pentru a crea imaginea
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            }
            //creare model cu numele, pretul si imaginea luate din baza de date
            DataModel a = new DataModel(nume, Integer.toString(pret), myBitmap);
            //adaugarea modelului in lista ce va fi returnata cand toata baza de date a fost parcursa
            array_list.add(a);
            //dute la urmatoarea linie din tabela
            res.moveToNext();

        }
        //returnare lista cu produsele din baza de date
        return array_list;
    }
    // stergere linia din baza de date care corespunde cu numele introdus
    public void delete(String name) {

        database.delete(MySQLiteHelper.NUME_TABELA, MySQLiteHelper.NUME_PRODUS + "=?", new String[]{name});
    }

}