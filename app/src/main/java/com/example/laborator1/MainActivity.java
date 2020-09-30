package com.example.laborator1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;
import static com.example.laborator1.R.drawable.ic_baseline_notifications_24;

public class MainActivity extends AppCompatActivity {

    private NotificationManager notificationManager;
    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final int NOTIFY_ID = 1;
    private Handler mHandler = new Handler();
    private int chooseCamera = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        turnOnCamera();
        pushNotification();
        search();
        getImage();
    }

    public void turnOnCamera() {

        final Button button = (Button) findViewById(R.id.check);
        final RadioButton front = (RadioButton) findViewById(R.id.bCamera);
        final RadioButton back = (RadioButton) findViewById(R.id.fCamera);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(front.isChecked()) {
                    //back camera
                    chooseCamera = 0;
                    openCamera();
                }

                if(back.isChecked()) {
                    //front camera
                    chooseCamera = 1;
                    openCamera();
                }

            }
        });
    }

    public void openCamera() {
        Intent intent = new Intent(MainActivity.this, CameraBack.class);
        intent.putExtra("CHOOSE_CAMERA", chooseCamera);
        startActivity(intent);
    }

    public static void createChannelIfNeeded(NotificationManager manager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    public void displayNotification(View view) {
        mHandler.postDelayed(mNoteRunnable, 10000);
    }

    private Runnable mNoteRunnable = new Runnable() {
        @Override
        public void run() {
            notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                            .setAutoCancel(false)
                            .setSmallIcon(ic_baseline_notifications_24)
                            .setWhen(System.currentTimeMillis())
                            .setContentIntent(pendingIntent)
                            .setContentTitle("Notification")
                            .setContentText("10 sec. passed!")
                            .setPriority(PRIORITY_HIGH);
            createChannelIfNeeded(notificationManager);
            notificationManager.notify(NOTIFY_ID, notificationBuilder.build());

        }
    };

    public void pushNotification() {

        final Button button = (Button) findViewById(R.id.pushNotification);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayNotification(view);
            }
        });
    }

    public void search() {

        final Button button = (Button) findViewById(R.id.searchButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchFor =  ((EditText)findViewById(R.id.searchWord)).getText().toString();
                Intent viewSearch = new Intent(Intent.ACTION_WEB_SEARCH);
                viewSearch.putExtra(SearchManager.QUERY, searchFor);
                startActivity(viewSearch);
            }
        });
    }

    public void getImage() {
        final Button button = (Button) findViewById(R.id.randomImage);
        final String url = "https://picsum.photos/200/300?random=1";
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomImage(url);
            }
        });
    }

    public void randomImage(String url) {
        ImageView img= (ImageView) findViewById(R.id.imageView);
        Picasso.with(MainActivity.this)
                .load(url)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(img);
    }

}