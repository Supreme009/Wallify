package wallify.app;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import java.util.Random;

public class WallpaperChanger extends IntentService {

    public WallpaperChanger() {
        super("WallpaperChanger");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            int[] wallpapers = {
                    R.drawable.sample1,
                    R.drawable.sample2,
                    R.drawable.sample3,
                    R.drawable.sample4,
                    R.drawable.sample5,
                    R.drawable.sample6
            };

            int randomIndex = new Random().nextInt(wallpapers.length);
            int selectedWallpaper = wallpapers[randomIndex];

            WallpaperUtils.setWallpaper(this, selectedWallpaper);
            Log.i("WallpaperChanger", "Fondo cambiado correctamente: " + selectedWallpaper);
            mostrarNotificacion("Wallify", "Fondo cambiado automáticamente");
        } catch (Exception e) {
            Log.e("WallpaperChanger", "Error al cambiar fondo: " + e.getMessage());
        }
    }

    private void mostrarNotificacion(String titulo, String mensaje) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String channelId = "wallify_auto_change";

        if (manager == null) return;

        // Para Android Oreo y superior
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Wallify Auto Change",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Notificaciones del cambio automático de fondo");
            manager.createNotificationChannel(channel);
        }

        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= 26) {
            builder = new Notification.Builder(this, channelId);
        } else {
            builder = new Notification.Builder(this);
        }

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(titulo)
                .setContentText(mensaje)
                .setAutoCancel(true);

        Notification notification = builder.build();
        manager.notify(new Random().nextInt(10000), notification);
    }
}