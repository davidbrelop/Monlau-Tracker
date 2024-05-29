package com.example.monlau_tracker;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        // Obtener el nombre del usuario de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
/*
        ArrayList arrayOfSessions = new ArrayList<Session>();
        Set<String> setOfNames = new HashSet<>();

        // LUNES
        Session session1 = new Session("M9", 1, "15:10", "16:10");
        arrayOfSessions.add(session1);
        setOfNames.add("M9 - Programación de servicios");

        Session session2 = new Session("M8", 1, "16:10", "17:10");
        arrayOfSessions.add(session2);
        setOfNames.add("M8 - Programación multimedia");

        Session session3 = new Session("M13", 1, "17:30", "18:30");
        arrayOfSessions.add(session3);
        setOfNames.add("M13 - Proyecto de síntesis");

        Session session4 = new Session("M6", 1, "18:30", "19:30");
        arrayOfSessions.add(session4);
        setOfNames.add("M6 - Acceso a datos");

        Session session5 = new Session("M7", 1, "19:30", "20:30");
        arrayOfSessions.add(session5);
        setOfNames.add("M7 - Desarrollo de interfaces");

        // MARTES
        Session session6 = new Session("M13", 2, "15:10", "16:10");
        arrayOfSessions.add(session6);
        setOfNames.add("M13 - Proyecto de síntesis");

        Session session7 = new Session("M13", 2, "16:10", "17:10");
        arrayOfSessions.add(session7);
        setOfNames.add("M13 - Proyecto de síntesis");

        Session session8 = new Session("M3", 2, "17:30", "18:30");
        arrayOfSessions.add(session8);
        setOfNames.add("M3 - Programación");

        Session session9 = new Session("M3", 2, "18:30", "19:30");
        arrayOfSessions.add(session9);
        setOfNames.add("M3 - Programación");

        Session session10 = new Session("M10", 2, "19:30", "20:30");
        arrayOfSessions.add(session10);
        setOfNames.add("M10 - Sistemas de gestión empresarial");

        // MIÉRCOLES
        Session session11 = new Session("M13", 3, "15:10", "16:10");
        arrayOfSessions.add(session11);
        setOfNames.add("M13 - Proyecto de síntesis");

        Session session12 = new Session("M13", 3, "16:10", "17:10");
        arrayOfSessions.add(session12);
        setOfNames.add("M13 - Proyecto de síntesis");

        Session session13 = new Session("M7", 3, "17:30", "18:30");
        arrayOfSessions.add(session13);
        setOfNames.add("M7 - Desarrollo de interfaces");

        Session session14 = new Session("M6", 3, "18:30", "19:30");
        arrayOfSessions.add(session14);
        setOfNames.add("M6 - Acceso a datos");

        Session session15 = new Session("M9", 3, "19:30", "20:30");
        arrayOfSessions.add(session15);
        setOfNames.add("M9 - Programación de servicios");

        // JUEVES
        Session session16 = new Session("M7", 4, "15:10", "16:10");
        arrayOfSessions.add(session16);
        setOfNames.add("M7 - Desarrollo de interfaces");

        Session session17 = new Session("M6", 4, "16:10", "17:10");
        arrayOfSessions.add(session17);
        setOfNames.add("M6 - Acceso a datos");

        Session session18 = new Session("M13", 4, "17:30", "18:30");
        arrayOfSessions.add(session18);
        setOfNames.add("M13 - Proyecto de síntesis");

        Session session19 = new Session("M13", 4, "18:30", "19:30");
        arrayOfSessions.add(session19);
        setOfNames.add("M13 - Proyecto de síntesis");

        Session session20 = new Session("M13", 4, "19:30", "20:30");
        arrayOfSessions.add(session20);
        setOfNames.add("M13 - Proyecto de síntesis");

        // VIERNES
        Session session21 = new Session("M10", 5, "15:10", "16:10");
        arrayOfSessions.add(session21);
        setOfNames.add("M10 - Sistemas de gestión empresarial");

        Session session22 = new Session("M8", 5, "16:10", "17:10");
        arrayOfSessions.add(session22);
        setOfNames.add("M8 - Programación multimedia");

        Session session23 = new Session("M13", 5, "17:30", "18:30");
        arrayOfSessions.add(session23);
        setOfNames.add("M13 - Proyecto de síntesis");

        Session session24 = new Session("M8", 5, "18:30", "19:30");
        arrayOfSessions.add(session24);
        setOfNames.add("M8 - Programación multimedia");


        showSessionsOnScreen(arrayOfSessions, setOfNames);
*/


        try {
            // Consultar el calendario semanal de este usuario
            GetScheduleTask getScheduleTask = (GetScheduleTask) new GetScheduleTask(ScheduleActivity.this).execute(username);
        }
        catch (Exception e) {
        }

    }

    public void showSessionsOnScreen(ArrayList<Session> arrayOfSessions, Set<String> setOfNames) {

        // Contamos los valores únicos de "hora_inicio", ya que representará la cantidad de filas de la tabla
        HashSet<String> uniqueStartTimes = new HashSet<>();
        for (Session session : arrayOfSessions) {
            uniqueStartTimes.add(session.hora_inicio);
        }
        int distinctStartTimesCount = uniqueStartTimes.size();

        // Convertir el HashSet a una lista
        ArrayList<String> sortedList = new ArrayList<>(uniqueStartTimes);

        // Ordenar la lista en orden ascendente
        Collections.sort(sortedList);

        // Asignamos un valor a cada "hora_inicio"
        Map<Integer, String> assignedNumbers = new HashMap<>();
        int count = 1;
        for(String startTime : sortedList) {
            assignedNumbers.put(count, startTime);
            count++;
        }

        // Creamos la tabla dinámicamente
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setBackground(getResources().getDrawable(R.drawable.background_half_edited));
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TableLayout table = new TableLayout(this);
        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        tableParams.setMargins(0, 0, 0, 0);
        table.setPadding(25, 60, 25, 0);
        table.setLayoutParams(tableParams);

        for (int i = 0; i <= distinctStartTimesCount; i++) {

            TableRow row = new TableRow(this);
            row.setWeightSum(2);
            row.setPadding(1, 10, 1, 10);
            row.setGravity(Gravity.CENTER);
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1);

            for (int j = 0; j < 6; j++) {

                TableRow.LayoutParams textViewParams = new TableRow.LayoutParams();
                TextView cell = new TextView(this);

                if(i==0){

                    GradientDrawable border = new GradientDrawable();
                    border.setColor(0xFF000000); // Color de fondo negro
                    border.setStroke(5, 0xFF000000); // Color del borde negro
                    //cell.setBackground(border);
                    cell.setBackground(getResources().getDrawable(R.drawable.border_attendance_title));

                    switch (j){
                        case 0:
                            cell.setText("");
                            cell.setBackground(null);
                            break;
                        case 1:
                            cell.setText("LUN");
                            break;
                        case 2:
                            cell.setText("MAR");
                            break;
                        case 3:
                            cell.setText("MIÉ");
                            break;
                        case 4:
                            cell.setText("JUE");
                            break;
                        case 5:
                            cell.setText("VIE");
                            break;
                    }

                    Typeface customFont = ResourcesCompat.getFont(this, R.font.open_sans);
                    cell.setTypeface(customFont, Typeface.BOLD);
                    cell.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                    cell.setTextColor(getResources().getColor(R.color.white));
                    cell.setPadding(6, 45, 6, 45);
                }

                else{
                    if(j==0){

                        String hora_final = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            hora_final = LocalTime.parse(assignedNumbers.get(i)).plusHours(1).format(DateTimeFormatter.ofPattern("HH:mm"));
                        }

                        GradientDrawable border = new GradientDrawable();
                        border.setColor(0x80000000); // Color de fondo negro transparente
                        //border.setStroke(5, 0x80000000); // Color del borde negro transparente
                        //cell.setBackground(border);
                        cell.setBackground(getResources().getDrawable(R.drawable.border_attendance_title));

                        cell.setText(assignedNumbers.get(i) + "\n────\n" + hora_final);
                        Typeface customFont = ResourcesCompat.getFont(this, R.font.open_sans);
                        cell.setTypeface(customFont, Typeface.BOLD);
                        cell.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                        cell.setTextColor(getResources().getColor(R.color.white));
                        cell.setPadding(3, 30, 3, 30);
                    }
                    else{
                        Session s = getSessionByDayAndStartTime(arrayOfSessions, j, assignedNumbers.get(i));
                        if (s == null) {
                            cell.setText("");
                        }
                        else {
                            cell.setText(s.nombre);
                            cell.setBackground(getResources().getDrawable(R.drawable.border_alternative_attendance_title));
                            cell.setPadding(6, 90, 6, 90);
                        }
                        cell.setTypeface(null, Typeface.BOLD);
                        cell.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                        cell.setGravity(Gravity.CENTER);
                        cell.setTextColor(getResources().getColor(R.color.black));

                    }
                }

                cell.setGravity(Gravity.CENTER);
                cell.setLayoutParams(textViewParams);
                row.addView(cell);
            }

            row.setLayoutParams(params);
            table.addView(row, params);
        }

        table.setStretchAllColumns(true);
        linearLayout.addView(table);

        // Crear un TextView
        TextView infoCourses = new TextView(this);

        // Ajustar parámetros de diseño según sea necesario
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        // Margen superior para separar el TextView de la tabla
        params.setMargins(30, 70, 30, 70);
        infoCourses.setLayoutParams(params);

        Typeface customFont = ResourcesCompat.getFont(this, R.font.open_sans);
        infoCourses.setTypeface(customFont, Typeface.BOLD);
        infoCourses.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        infoCourses.setTextColor(getResources().getColor(R.color.white));
        infoCourses.setPadding(106, 45, 106, 45);
        infoCourses.setBackground(getResources().getDrawable(R.drawable.border_attendance_title));
        infoCourses.setLetterSpacing(0.1f);

        // Convertir el Set a una lista
        List<String> nombresOrdenados = new ArrayList<>(setOfNames);

        // Ordenar la lista alfabéticamente
        Collections.sort(nombresOrdenados, new NumericalStringComparator());

        StringBuilder sb = new StringBuilder();
        for (String nombre : nombresOrdenados) {
            sb.append(" ").append(nombre).append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);

        infoCourses.setText(sb.toString());

        // Añadir el TextView al LinearLayout
        linearLayout.addView(infoCourses);

        setContentView(linearLayout);
    }

    private Session getSessionByDayAndStartTime(ArrayList<Session> arrayOfSessions, int dia_semanal, String hora_inicio) {
        for (Session session : arrayOfSessions) {
            if (session.dia_semanal == dia_semanal && session.hora_inicio.equals(hora_inicio)) {
                return session; // Retorna la Session si coincide en "dia_semanal" y "hora_inicio"
            }
        }
        return null; // Retorna "null" si no se encuentra ninguna Session con los atributos dados
    }
}