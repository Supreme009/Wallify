package wallify.app;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.IOException;

public class WallpaperUtils {

    public static void setWallpaper(Context context, int resId) {
        WallpaperManager wm = WallpaperManager.getInstance(context);
        Bitmap bitmap = null;

        try {
            // Decodificar la imagen en un tamaño optimizado
            bitmap = BitmapFactory.decodeResource(context.getResources(), resId);

            if (bitmap != null) {
                wm.setBitmap(bitmap);
                Log.i("WallpaperUtils", "Fondo de pantalla establecido correctamente.");
            } else {
                Log.e("WallpaperUtils", "Error: el bitmap es nulo.");
            }

        } catch (IOException e) {
            Log.e("WallpaperUtils", "Error al establecer fondo: " + e.getMessage());
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            Log.e("WallpaperUtils", "Error de memoria al establecer fondo: " + e.getMessage());
        } finally {
            // Liberar memoria del bitmap si ya no se usa
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }
}