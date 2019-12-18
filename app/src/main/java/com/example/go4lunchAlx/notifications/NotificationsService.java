package com.example.go4lunchAlx.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.example.go4lunchAlx.R;
import com.example.go4lunchAlx.detail.DetailActivity;
import com.example.go4lunchAlx.di.DI;
import com.example.go4lunchAlx.models.User;
import com.example.go4lunchAlx.service.RestApiService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationsService extends FirebaseMessagingService {

    private final int NOTIFICATION_ID = 007;
    private final String NOTIFICATION_TAG = "GO4LUNCH";
    private RestApiService service = DI.getRestApiService();
    private SharedPreferences sharedPreferences;
    private SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yy");
    private User currentUser;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.go4lunchAlx", Context.MODE_PRIVATE);

        if (sharedPreferences.getString("notifPrefs", "on").equals("on")) {
            currentUser = service.getUserById(service.getCurrentUserId());
            if (currentUser.getDateSelection()!= null && ft.format(new Date())
                    .equals(ft.format(new Date(currentUser.getDateSelection())))) {
                sendVisualNotification();
            }
        }
    }

    // ---

    private void sendVisualNotification() {

        // 1 - Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("restoId", currentUser
                .getSelectedRestaurant());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // 2 - Create a Style for the Notification
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(getString(R.string.notification_title));
        inboxStyle.addLine(getString(R.string.notification_body));

        // 3 - Create a Channel (Android 8)
        String channelId = getString(R.string.default_notification_channel_id);

        // 4 - Build a Notification object
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_yourlunch)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.notification_title))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(inboxStyle);

        // 5 - Add the Notification to the Notification Manager and show it.
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 6 - Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Message Firebase";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        // 7 - Show notification
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }
}