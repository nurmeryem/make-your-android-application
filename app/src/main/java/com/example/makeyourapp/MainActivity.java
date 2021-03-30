package com.example.makeyourapp;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public String lasturl = "https://nurmeryem.github.io/";
    private static final String TAG = "MainActivity";
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        checknet();
        checkConnection();



        yap();
        //addNotification();
        //AppRateThis();
    }


    /*public void AppRateThis() {

        AppRate.with(this)
                .setInstallDays(0) //uygulama telefona yüklendikten bir gün sonra degerlendirme penceresi gelsin
                .setLaunchTimes(0) //  uyari iletişim kutusu, kullanici uygulamayi yükledikten ve üç kez başlattiktan bir gün sonra gösterilecektir.
                .setRemindInterval(0) // tekrar hatırlat secenegine tiklayinca hatirlatma araliklari
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);
    }*/

    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_noti) //set icon for notification
                        .setContentTitle("Bildirim Denemesi") //set title of notification
                        .setContentText("Bu Bir Bildirim Mesajıdır")//this is notification message
                        .setAutoCancel(true) // makes auto cancel of notification
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification


        Intent notificationIntent = new Intent(MainActivity.this, AfterNotificationActivity.class);


        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //notification message will get at NotificationView
        notificationIntent.putExtra("message", "Bu Bir Bildirim Mesajıdır");

        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);


        Log.w(TAG, "index=" + pendingIntent);

        // Add as notification


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            //When sdk version is larger than26
            String id = "channel_1";
            String description = "143";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(id, description, importance);
//                     channel.enableLights(true);
//                     channel.enableVibration(true);//
            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(MainActivity.this, id)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setSmallIcon(R.drawable.ic_noti)
                    .setContentTitle("başlık")
                    .setContentText("text")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
            manager.notify(1, notification);
        } else {
            //When sdk version is less than26
            Notification notification = new NotificationCompat.Builder(MainActivity.this)
                    .setContentTitle("başlık")
                    .setContentText("text")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_noti)
                    .build();
            manager.notify(1, notification);
        }

    }


    public void yap() {

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.show();
        checknet();
        checkConnection();
        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);

        webview.loadUrl(lasturl);
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new MyWebViewClient());


        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) { //web yuklemesi bitiince
                super.onPageFinished(view, url);
                lasturl = url;
                Log.i("Listener", "Finish");
                progressDialog.dismiss();
            }


            @Override
            public void onReceivedError(final WebView webview, int errorCode, String description, String failingUrl) {
                //final WebView webview2 = (WebView) findViewById(R.id.webview);
                //webview2.getSettings().setJavaScriptEnabled(true);

                String summary = "<html><body></body></html>";
                webview.loadData(summary, "text/html", null);
                Intent i = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(i);

               /* AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Check your internet connection and try again.");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //webview.loadUrl("https://nurmeryem.github.io/");

                        yap();
                        // startActivity(getIntent());
                    }
                });

                alertDialog.show();

*/
            }


        });


        //internetIsConnected();

        if (checknet() == false) {


            //progressDialog.setCancelable(false);
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Uyarı");
            alertDialog.setMessage("Uygulamamızın çalışabilmesi için internet bağlantısı gerekiyor. Tekrar deneyebilir veya bağlantı ayarlarınızı gözden geçirebilirsiniz.");
            alertDialog.setCancelable(false);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Tekrar Dene", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    yap();
                    ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
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

                    //startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                }
            });

            alertDialog.show();

            progressDialog.show();


            Intent i = new Intent(MainActivity.this, Main2Activity.class);
            startActivity(i);


            /*AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Check your internet connection and try again.");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yeniden dene", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
//                    finish();
                    //   startActivity(getIntent());
                    yap();
                    Intent i = new Intent(MainActivity.this,Main2Activity.class);
                    startActivity(i);
                }
            });

            alertDialog.show();*/
            Toast.makeText(getApplicationContext(), "Lütfen internet bağlantınızı kontrol edip yeniden deneyiniz.", Toast.LENGTH_LONG).show();
            //Intent intent = new Intent(MainActivity.this, Main2Activity.class);
            //startActivity(intent);

            //webview.reload();
            //showSimpleDialog(v);
        } else {
            Toast.makeText(getApplicationContext(), "Bağlanılıyor lütfen bekleyiniz...", Toast.LENGTH_LONG).show();
            // webview.reload();
            Log.d(TAG, "else if e girdi");

        }


    }

//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        //geri butonunu yakalıyoruz
//
//        //düşün
//
//
//        if(lasturl == "https://nurmeryem.github.io/"")
//        {
//            if(keyCode == KeyEvent.KEYCODE_BACK && isTaskRoot() ) {
//                //Programdan çıkmak isteyip istemediğini soruyoruz
//                new AlertDialog.Builder(this)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setTitle(R.string.quit)
//                        .setMessage(R.string.really_quit)
//                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                //Aktiviteyi durduruyoruz
//                                finish();
//                            }
//                        })
//                        .setNegativeButton(R.string.no, null)
//                        .show();
//
//                return true;
//            }
//            else {
//                return super.onKeyDown(keyCode, event);
//            }
//        }
//        else{
//            return super.onKeyDown(keyCode, event);
//        }
//
//    }
//    public void onBackPressed() {
//
//
//        if (webview.canGoBack()) {
//            webview.goBack();
//            return;
//        }
//        else{
//            new AlertDialog.Builder(this)
//                    .setTitle("Really Exit?")
//                    .setMessage("Are you sure you want to exit?")
//                    .setNegativeButton(android.R.string.no, null)
//                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//                        public void onClick(DialogInterface arg0, int arg1) {
//                            MainActivity.super.onBackPressed();
//                        }
//                    }).create().show();
//        }
//
//
//
//
//    }

    public boolean checknet() { //internet varsa true yoksa false donduruyor
        return internetIsConnected();
    }

    public boolean internetIsConnected() { //internet baglantisini kontrol edip true ya da false bir deger gonderiyor.
        try {
            String command = "ping -c 1 google.com";
            //Toast.makeText(getApplicationContext(), "İnternet yok", Toast.LENGTH_LONG).show();
            Log.d(TAG, "try a girdi");
            Log.v(TAG, "ping den sonuç:" + Runtime.getRuntime().exec(command).waitFor()); //2
            return (Runtime.getRuntime().exec(command).waitFor() == 1 || Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }

    }

    public void checkConnection() {
        ConnectivityManager manager = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                Toast.makeText(this, "Wifi ile bağlısınız.", Toast.LENGTH_LONG).show();

            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                Toast.makeText(getApplicationContext(), "Mobil Bağlantı var", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getApplicationContext(), "Lütfen internet bağlantınızı kontrol edip tekrar deneyiniz", Toast.LENGTH_LONG).show();

            }
        }
    }

    public class MyWebViewClient extends WebViewClient {

        //final ProgressDialog pd = ProgressDialog.show(MainActivity.this, "", "Please wait, your transaction is being processed...", true);

       /* public void onPageStarted(WebView view,String url){
            pd.show();
            //pd.dismiss();
        }*/

        @Override
        public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
            // Redirect to deprecated method, so you can use it in all SDK versions
            Log.w(TAG, "--------------------------------------------------------------------------------------------------------------------------");
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            //view.loadUrl("https://nurmeryem.github.io/");

            view.setWebChromeClient(new WebChromeClient());
            view.setWebViewClient(new MyWebViewClient());
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = view.getUrl();
            Log.d("LOG", "previous_url: " + url);
            return false;
        }

        public class MyJavaScriptInterface {
            @JavascriptInterface
            public void onUrlChange(String url) {
                Log.d("LOG", "current_url: " + url);
            }
        }

    }


    public void onBackPressed() {
        Log.w("TAG", "onBackPressed() metoduna girdi-------------------------------------------->" + lasturl);

        if (!lasturl.equals("https://nurmeryem.github.io/") && webview.canGoBack()) {
            webview.goBack();

        } else {

            new AlertDialog.Builder(this)
                    .setTitle("")
                    .setMessage("Uygulamayı kapatmak istediğinize emin misiniz?")
                    .setNegativeButton(R.string.no, null)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            setResult(RESULT_OK, new Intent().putExtra("Çıkış", true));
                            finish();
                        }

                    }).create().show();


            //finish();
            //super.onBackPressed();


        }
    }


}