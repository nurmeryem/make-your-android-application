package com.example.makeyourapp;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG = "Main2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //ImageButton yenile = (ImageButton)findViewById(R.id.imageButton);
        ImageView iv = (ImageView)findViewById(R.id.womenlogo);


        AlertDialog alertDialog = new AlertDialog.Builder(Main2Activity.this).create();
        alertDialog.setTitle("Uyarı");
        alertDialog.setMessage("Uygulamamızın çalışabilmesi için internet bağlantısı gerekiyor. Tekrar deneyebilir veya bağlantı ayarlarınızı gözden geçirebilirsiniz.");
        alertDialog.setCancelable(false);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Tekrar Dene", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                yap();
                ProgressDialog progressDialog = new ProgressDialog(Main2Activity.this);
                // progressDialog.setMessage("Loading..."); // Setting Message
                // progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);

                // startActivity(getIntent());
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Ayarlar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));

                checknet();
            }});

        alertDialog.show();
    }

    public void yap() {

        Log.w("TAG","Main2 ye girdi ve yap fonksiyonu çalıştı..");
        Intent intent = new Intent(Main2Activity.this, MainActivity.class);
        startActivity(intent);

    }

    public boolean checknet() { //internet varsa true yoksa false donduruyor
        return internetIsConnected();
    }

    public boolean internetIsConnected() { //internet baglantisini kontrol edip true ya da false bir deger gonderiyor.
        try {
            // wifi ayarlarına girdikten sonra tekrar alert gostermesi icin
            AlertDialog alertDialog = new AlertDialog.Builder(Main2Activity.this).create();
            alertDialog.setTitle("Uyarı");
            alertDialog.setMessage("Uygulamamızın çalışabilmesi için internet bağlantısı gerekiyor. Tekrar deneyebilir veya bağlantı ayarlarınızı gözden geçirebilirsiniz.");
            alertDialog.setCancelable(false);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Tekrar Dene", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    yap();
                    ProgressDialog progressDialog = new ProgressDialog(Main2Activity.this);
                    // progressDialog.setMessage("Loading..."); // Setting Message
                    // progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                    progressDialog.show(); // Display Progress Dialog
                    progressDialog.setCancelable(false);
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Ayarlar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    checknet();
                }});

            alertDialog.show();



            String command = "ping -c 1 google.com";
            //Toast.makeText(getApplicationContext(), "İnternet yok", Toast.LENGTH_LONG).show();
            Log.d(TAG, "try a girdi");
            Log.v(TAG, "ping den sonuç:" + Runtime.getRuntime().exec(command).waitFor()); //2
            return (Runtime.getRuntime().exec(command).waitFor() == 1 || Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }

    }

}