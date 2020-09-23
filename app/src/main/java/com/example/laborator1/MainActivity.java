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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;
import static com.example.laborator1.R.drawable.ic_baseline_notifications_24;

public class MainActivity extends AppCompatActivity {

    private NotificationManager notificationManager;
    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final int NOTIFY_ID = 1;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setText();
        pushNotification();
        search();
    }

    public void setText() {
        final TextView textView2 = (TextView) findViewById(R.id.textView2);
        final Button button = (Button) findViewById(R.id.check);
        final RadioButton front = (RadioButton) findViewById(R.id.bCamera);
        final RadioButton back = (RadioButton) findViewById(R.id.fCamera);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(front.isChecked()) {
                    textView2.setText("Back Camera is on");
                }

                if(back.isChecked()) {
                    textView2.setText("Front Camera is on");
                }
            }
        });
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

}