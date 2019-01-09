package com.example.madalina.aplicatiezara;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private ArrayList<DataModel> data;
    private RecyclerView.Adapter adapter;
    private String numeProdus;
    private int pret;

    private DBManager dbManager;
    private Context context;

    public DownloadImageTask(ArrayList<DataModel> data, RecyclerView.Adapter adapter, String numeProdus, int pret) {
        this.data = data;
        this.adapter = adapter;
        this.numeProdus = numeProdus;
        this.pret = pret;
    }

    protected Bitmap doInBackground(String... paths) {
        Bitmap bmp = null;
        try {
            // incarca poza in memorie
            InputStream in = new FileInputStream(paths[0]);
            bmp = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            throw new RuntimeException(e);
        }
        return bmp;
    }

    protected void onPostExecute(Bitmap poza) {
        //adauga numele, pretul si poza produsului la lista de  produse din aplicatie
        data.add(new DataModel(
                numeProdus,
                String.valueOf(pret) + " lei",
                poza
        ));
        adapter.notifyDataSetChanged();

    }

    public void setcontext(Context context) {
        this.context = context;
    }

}