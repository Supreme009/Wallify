package wallify.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

    // Lista de fondos disponibles en drawable
    private int[] wallpapers = {
        R.drawable.sample1,
        R.drawable.sample2,
        R.drawable.sample3,
        R.drawable.sample4,
        R.drawable.sample5,
        R.drawable.sample6
    };

    private int tipoFondo = WallpaperManager.FLAG_SYSTEM; // por defecto pantalla principal
    private int selectedWallpaper = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView = (GridView) findViewById(R.id.gridWallpapers);
        ImageView btnSettings = (ImageView) findViewById(R.id.btnSettings);

        // Adaptador de wallpapers
        GridAdapter adapter = new GridAdapter(this, wallpapers);
        gridView.setAdapter(adapter);

        // Click en un wallpaper → abre diálogo de preview
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedWallpaper = wallpapers[position];
                mostrarDialogoCambio();
            }
        });

        // Botón de configuración → abre SettingsActivity
        btnSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Muestra un diálogo con preview del fondo seleccionado
     */
    private void mostrarDialogoCambio() {
        if (selectedWallpaper == -1) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_wallpaper_preview, null);

        ImageView previewImage = (ImageView) dialogView.findViewById(R.id.dialogImage);
        previewImage.setImageResource(selectedWallpaper);

        builder.setView(dialogView);
        builder.setTitle("Cambiar Fondo");
        builder.setPositiveButton("Aplicar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mostrarOpcionesFondo();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    /**
     * Muestra opciones para aplicar el fondo (principal, bloqueo, ambas)
     */
    private void mostrarOpcionesFondo() {
        final String[] opciones = {"Pantalla principal", "Pantalla de bloqueo", "Ambas"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona dónde aplicar el fondo");

        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        tipoFondo = WallpaperManager.FLAG_SYSTEM;
                        break;
                    case 1:
                        tipoFondo = WallpaperManager.FLAG_LOCK;
                        break;
                    case 2:
                        tipoFondo = WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK;
                        break;
                }
                changeWallpaper(selectedWallpaper);
            }
        });
        builder.show();
    }

    /**
     * Aplica el fondo seleccionado en la pantalla elegida
     */
    private void changeWallpaper(int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);

        try {
            wallpaperManager.setBitmap(bitmap, null, true, tipoFondo);
            Toast.makeText(this, "Fondo cambiado correctamente", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error al cambiar el fondo", Toast.LENGTH_SHORT).show();
        }
    }
}