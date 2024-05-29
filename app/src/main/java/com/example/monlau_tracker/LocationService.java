package com.example.monlau_tracker;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class LocationService extends Service implements LocationListener {

    private LocationManager locationManager;
    private static String CHANNEL_ID = "GEO01";
    private NotificationCompat.Builder builder = null;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Obtener el contexto de la aplicación
            Context appContext = getApplicationContext();
            Toast.makeText(appContext, "SERVICIO INICIADO.", Toast.LENGTH_LONG).show();

            // Crear el canal de notificación
            createNotificationChannel();

            // Create an explicit intent for an Activity in your app.
            //Intent intent = new Intent(this, AlertActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.monlau_icon)
                    .setContentTitle("¡ATENCIÓN!")
                    .setContentText("Parece que has salido de Monlau sin fichar. Vuelve atrás y completa tu fichaje de salida.")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    // Set the intent that fires when the user taps the notification.
                    //.setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        }

        return START_STICKY;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        // Calcular la distancia en metros entre el usuario y Monlau
        int distancia = calcularDistanciaToMonlau(location.getLatitude(), location.getLongitude());

        // Obtener el estado del usuario a partir de la instancia de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String status = sharedPreferences.getString("status", "");
        boolean avisado = sharedPreferences.getBoolean("avisado", false);

        if (distancia > 50 && !avisado && status.equals("in")) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                // El permiso está concedido, proceder a enviar la notificación
                notificationManager.notify(1, builder.build());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("avisado", true);
                // Aplicar los cambios
                editor.apply();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
        // Obtener el contexto de la aplicación
        Context appContext = getApplicationContext();
        Toast.makeText(appContext, "SERVICIO DETENIDO.", Toast.LENGTH_LONG).show();
    }

    private int calcularDistanciaToMonlau(double latitud, double longitud) {

        Location location1 = new Location("myLocation");
        location1.setLatitude(latitud);
        location1.setLongitude(longitud);

        Location location2 = new Location("Monlau");
        location2.setLatitude(41.42311729609058);
        location2.setLongitude(2.190582451348262);

        // Calcula la distancia entre los dos puntos en metros
        float distanciaEnMetros = location1.distanceTo(location2);

        // Convierte la distancia a un entero sin decimales
        int distanciaSinDecimales = (int) distanciaEnMetros;

        return distanciaSinDecimales;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because the NotificationChannel class is not in the Support Library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ALERT CHANNEL";
            String description = "Aquí se publicarán las notificaciones al superar una distancia concreta respecto a Monlau.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system. You can't change the importance or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}