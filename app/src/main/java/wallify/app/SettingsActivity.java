package wallify.app;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.widget.Toast;

public class SettingsActivity extends Activity {

    private Switch switchAuto;
    private Spinner spinnerFrecuencia;
    private int frecuenciaHoras = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchAuto = (Switch) findViewById(R.id.switchAuto);
        spinnerFrecuencia = (Spinner) findViewById(R.id.spinnerFrecuencia);

        final SharedPreferences prefs = getSharedPreferences("WallifyPrefs", MODE_PRIVATE);
        boolean autoChange = prefs.getBoolean("autoChange", false);
        frecuenciaHoras = prefs.getInt("frecuenciaHoras", 1);

        switchAuto.setChecked(autoChange);

        // Opciones de frecuencia en horas
        final String[] opciones = {"1 hora", "3 horas", "6 horas", "12 horas", "24 horas"};
        final int[] valores = {1, 3, 6, 12, 24};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                opciones
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrecuencia.setAdapter(adapter);

        // Seleccionar la opción guardada
        for (int i = 0; i < valores.length; i++) {
            if (valores[i] == frecuenciaHoras) {
                spinnerFrecuencia.setSelection(i);
                break;
            }
        }

        spinnerFrecuencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                frecuenciaHoras = valores[position];
                prefs.edit().putInt("frecuenciaHoras", frecuenciaHoras).apply();
                if (switchAuto.isChecked()) {
                    startAutoChange(frecuenciaHoras);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });

        switchAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.edit().putBoolean("autoChange", isChecked).apply();
                if (isChecked) {
                    startAutoChange(frecuenciaHoras);
                } else {
                    stopAutoChange();
                }
            }
        });
    }

    /**
     * Inicia el cambio automático de fondos cada X horas
     */
    private void startAutoChange(int horas) {
        Intent intent = new Intent(this, WallpaperChanger.class);
        PendingIntent pendingIntent = PendingIntent.getService(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long intervalo = horas * 60 * 60 * 1000; // horas → milisegundos

        if (manager != null) {
            manager.cancel(pendingIntent); // cancelar alarmas previas para evitar duplicados
            manager.setRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + intervalo,
                    intervalo,
                    pendingIntent
            );
            Toast.makeText(this, "Cambio automático cada " + horas + " horas", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Detiene el cambio automático de fondos
     */
    private void stopAutoChange() {
        Intent intent = new Intent(this, WallpaperChanger.class);
        PendingIntent pendingIntent = PendingIntent.getService(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (manager != null) {
            manager.cancel(pendingIntent);
            Toast.makeText(this, "Cambio automático desactivado", Toast.LENGTH_SHORT).show();
        }
    }
}