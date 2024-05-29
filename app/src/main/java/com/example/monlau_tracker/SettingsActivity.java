package com.example.monlau_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private static final int REQUEST_GEOLOCALIZACION = 1;
    private static final int REQUEST_NOTIFICATIONS = 2;

    private static String[] PERMISSIONS_GEOLOCALIZACION = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static String[] PERMISSIONS_NOTIFICATIONS = {
            Manifest.permission.ACCESS_NOTIFICATION_POLICY
    };

    public Context settingsContext;
    public SwitchCompat switchGeolocalizacion, switchNotificaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsContext = this;

        switchGeolocalizacion = findViewById(R.id.switchGeolocalizacion);
        switchNotificaciones = findViewById(R.id.switchNotificaciones);

        // Modificamos el estado de los Switch según las preferencias del usuario
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        boolean geolocation = sharedPreferences.getBoolean("geolocation", false);
        boolean notifications = sharedPreferences.getBoolean("notifications", false);
        switchGeolocalizacion.setChecked(geolocation);
        switchNotificaciones.setChecked(notifications);

        // Agregamos un Listener al SwitchCompat de la geolocalización
        switchGeolocalizacion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Solicitar los permisos de geolocalización al usuario
                    if (ActivityCompat.checkSelfPermission(settingsContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(settingsContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SettingsActivity.this, PERMISSIONS_GEOLOCALIZACION, REQUEST_GEOLOCALIZACION);
                    }
                    else{
                        // Activar el servicio de geolocalización
                        Intent serviceIntent = new Intent(settingsContext, LocationService.class);
                        startService(serviceIntent);
                    }

                    // Obtener la instancia de SharedPreferences y modificar su valor
                    SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("geolocation", true);
                    // Aplicar los cambios
                    editor.apply();
                }
                else {
                    // Desactivar el servicio de geolocalización
                    Intent serviceIntent = new Intent(settingsContext, LocationService.class);
                    stopService(serviceIntent);

                    // Obtener la instancia de SharedPreferences y modificar su valor
                    SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("geolocation", false);
                    // Aplicar los cambios
                    editor.apply();
                }
            }
        });
/*
        // Agregamos un Listener al SwitchCompat de las notificaciones
        switchNotificaciones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Solicitar los permisos de notificaciones al usuario
                    if (ActivityCompat.checkSelfPermission(settingsContext, android.Manifest.permission.ACCESS_NOTIFICATION_POLICY) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SettingsActivity.this, PERMISSIONS_NOTIFICATIONS, REQUEST_NOTIFICATIONS);
                    }

                    // Obtener la instancia de SharedPreferences y modificar su valor
                    SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("notifications", true);
                    // Aplicar los cambios
                    editor.apply();
                }
                else{
                    // Obtener la instancia de SharedPreferences y modificar su valor
                    SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("notifications", false);
                    // Aplicar los cambios
                    editor.apply();
                }
            }
        });*/

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Modificamos el estado de los Switch según las preferencias del usuario
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        boolean geolocation = sharedPreferences.getBoolean("geolocation", false);
        boolean notifications = sharedPreferences.getBoolean("notifications", false);
        switchGeolocalizacion.setChecked(geolocation);
        switchNotificaciones.setChecked(notifications);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_GEOLOCALIZACION) {
            // Verificar si se han concedido los permisos
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Los permisos fueron concedidos y podemos continuar con la lógica de la aplicación

                // Activar el servicio de geolocalización
                Intent serviceIntent = new Intent(settingsContext, LocationService.class);
                startService(serviceIntent);

                // Obtener la instancia de SharedPreferences y modificar su valor
                SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("geolocation", true);
            }
            else {
                // Desactivamos el SwitchCompat, ya que no se han aceptado los permisos de geolocalización
                switchGeolocalizacion.setChecked(false);

                // Obtener la instancia de SharedPreferences y modificar su valor
                SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("geolocation", false);
            }
        }/*
        if (requestCode == REQUEST_NOTIFICATIONS) {
            // Verificar si se han concedido los permisos
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Los permisos fueron concedidos y podemos continuar con la lógica de la aplicación

                // Obtener la instancia de SharedPreferences y modificar su valor
                SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("notifications", true);
            }
            else {
                // Desactivamos el SwitchCompat, ya que no se han aceptado los permisos de notificaciones
                switchNotificaciones.setChecked(false);

                // Obtener la instancia de SharedPreferences y modificar su valor
                SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("notifications", false);
            }
        }*/
    }
}