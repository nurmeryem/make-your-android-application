# make-your-android-application
Web sitenizi android uygulamasına kolayca dönüştürün.

Java programlama dili kullanılarak yazılmıştır.

## Özellikler:
* İnternet kontrolü (İnternet bağlantısı kesildiğinde ekrana uyarı mesajı vermesi)
* Bildirim
* Geri tuşu kontrolü	
* Uygulama puanlama penceresi


## Nasıl kullanılır 
### Webview oluşturma :

```java webview=(WebView) findViewById(R.id.webview);
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
```

### İnternet kontrolü :
```java
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
 ```
 
 ### Uygulamayı kapatmak istediğinize emin misiniz?
 
 ```java
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
```

### Telefona bildirim gönder :
```java 
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
//                     channel.enableVibration(true);
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
```
### Uygulama puanlama penceresi :
```java
public void AppRateThis() {

        AppRate.with(this)
                .setInstallDays(0) //uygulama telefona yüklendikten bir gün sonra degerlendirme penceresi gelsin
                .setLaunchTimes(3) //  uyari iletişim kutusu, kullanici uygulamayi yükledikten ve üç kez başlattiktan bir gün sonra gösterilecektir.
                .setRemindInterval(4) // tekrar hatırlat secenegine tiklayinca hatirlatma araliklari
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);
    }
```
## Projeyi çalıştırmak için : 
* ``` git clone https://github.com/nurmeryem/make-your-android-application.git ```
* ``` android studio -> file -> open -> make-your-android-application ```
* ``` run project ```
