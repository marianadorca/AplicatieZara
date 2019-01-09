package com.example.madalina.aplicatiezara;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel> data;
    static View.OnClickListener myOnClickListener;
    private String mNumeProdus;
    private int mPret;
    private Uri mPhotoURI;
    private int mTotalDePlata;
    private String mCurrentPhotoPath;
    private DBManager dbManager;

    // metoda care se apeeaa atunci cand se creaza activitatea
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializare baza de date si deschidere
        dbManager = new DBManager(this);
        dbManager.open();
        //dbManager.insert("a",1,"a");
        //ArrayList<String> test = dbManager.getAllCotacts();

        // initialiari
        myOnClickListener = new MyOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<>();

        ArrayList<DataModel> test = dbManager.getAllCotacts();
        //Testare functionalitate cod in consola
        for(int i = 0; i < test.size(); i++) {
            System.out.println(test.get(i).getName());
            System.out.println(test.get(i).getPrice());
            System.out.println(test.get(i).getImage());
        }
        //Daca baza de date e goala se foloseste lista goala
        //In caz contrar se foloseste lista de produse preluata din baza de date
        if(test == null) {
            adapter = new CustomAdapter(data);
        } else {
            data = test;
            adapter = new CustomAdapter(data);
        }

        recyclerView.setAdapter(adapter);

        // cream butonul acela ca o bulina rosie si zicem ce se intampla cand apasam pe el
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // dialogul pt nume produs
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Numele produsului");

                // Setare introducere text
                final EditText input = new EditText(MainActivity.this);

                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);


                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mNumeProdus = input.getText().toString();

                        // dialogul cre apare pt pretul produsului
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Pretul produsului");

                        // Introducere pret
                        final EditText input = new EditText(MainActivity.this);
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        builder.setView(input);

                        // Set up the buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPret = Integer.parseInt(input.getText().toString());
                                // apelam metoda care declanseaza camera
                                dispatchTakePictureIntent();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    // atunci cand dam tap pe un produs sa dispara si sa se stearga din baza de date
    private class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            removeItem(v);
        }

        private void removeItem(View v) {
            int selectedItemPosition = recyclerView.getChildPosition(v);
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForPosition(selectedItemPosition);
            TextView textViewName
                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewName);
            String selectedName = (String) textViewName.getText();
            TextView textViewPrice
                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewPrice);

            //Stergere produs din lista si din baza de date dupa nume
            System.out.println("Pozitia "+ selectedItemPosition);
            System.out.println(data.get(selectedItemPosition).getName());
            dbManager.delete(data.get(selectedItemPosition).getName());

            data.remove(selectedItemPosition);
            //System.out.println("Pozitia "+ selectedItemPosition);
           // System.out.println(data.get(selectedItemPosition).getName());
            adapter.notifyItemRemoved(selectedItemPosition);



            // ne apare un popup cu totalul de plata
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Produs sters din baza de date");

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
            //dbManager.delete(selectedItemPosition);
        }
    }

    // creaza in galerie un fisier gol in care va fi salvata poza
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Salvare fisier dupa path
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // metoda care apeleaza createImageFile pt a crea un fisier gol si apoi porneste camera
    // si salveaza poza facuta in fisierul abia creat
    private void dispatchTakePictureIntent() {
        // se face un intent care sa fie trimit la Android pt a ii zice sa porneasca camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                throw new RuntimeException(ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                // ia calea fisierului gol in care va fi salvata poza
                mPhotoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                System.out.println(mPhotoURI);
                // stabileste unde va fi salvata poza dupa ce va fi facuta
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoURI);
                // da comanda propriu zisa de a porni camera
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    // se apeleaza dupa ce camera a facut poza
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        // verifica daca poza a fost facuta cu succes
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // creaza un task/thread pt incarcarea pozei din galerie in aplicatie
            DownloadImageTask task = new DownloadImageTask(data, adapter, mNumeProdus, mPret);
            task.setcontext(this);
            task.execute(mCurrentPhotoPath);
            //Insereaza produsul in baza de date
            dbManager.insert(mNumeProdus,mPret,mCurrentPhotoPath);
        }
    }
}