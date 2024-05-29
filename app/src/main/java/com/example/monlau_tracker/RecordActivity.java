package com.example.monlau_tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class RecordActivity extends AppCompatActivity {

    private Date lastSelectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        // Obtener el nombre del usuario de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        // Obtener la referencia al CalendarView
        CalendarView calendar = findViewById(R.id.calendar);

        // Obtener la fecha actual en el formato deseado
        Calendar c = Calendar.getInstance();

        if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            c.add(Calendar.DAY_OF_MONTH, -1);
            calendar.setDate(c.getTimeInMillis());
        }
        else if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            c.add(Calendar.DAY_OF_MONTH, -2);
            calendar.setDate(c.getTimeInMillis());
        }

        lastSelectedDate = c.getTime();

        Date fecha = c.getTime();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        String fechaFormateada = formato.format(fecha);

        //ArrayList arrayOfAttendances = new ArrayList<Attendance>();
        //showAttendancesOnScreen(arrayOfAttendances);

        try {
            // Consultar la asistencia del usuario en la fecha seleccionada
            GetAttendanceTask getAttendanceTask = (GetAttendanceTask) new GetAttendanceTask(RecordActivity.this).execute(username, fechaFormateada);
        }
        catch (Exception e) {
        }

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                Calendar c = Calendar.getInstance();
                c.set(year,month,dayOfMonth);

                if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    calendar.setDate(lastSelectedDate.getTime());
                }
                else{

                    lastSelectedDate = c.getTime();

                    Date fecha = c.getTime();
                    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                    String fechaFormateada = formato.format(fecha);

                    //ArrayList arrayOfAttendances = new ArrayList<Attendance>();
                    //showAttendancesOnScreen(arrayOfAttendances);

                    try {
                        // Consultar la asistencia del usuario en la fecha seleccionada
                        GetAttendanceTask getAttendanceTask = (GetAttendanceTask) new GetAttendanceTask(RecordActivity.this).execute(username, fechaFormateada);
                    }
                    catch (Exception e) {
                    }
                }
            }
        });
    }

    public void showAttendancesOnScreen(ArrayList<Attendance> arrayOfAttendances) {

        LinearLayout linearLayoutTitulos = findViewById(R.id.linearLayoutTitulos);
        ListView listViewAttendances = findViewById(R.id.listViewAttendances);
        TextView textViewInfo = findViewById(R.id.textViewInfo);

        if(arrayOfAttendances.isEmpty()){
            linearLayoutTitulos.setVisibility(View.INVISIBLE);
            listViewAttendances.setVisibility(View.INVISIBLE);
            textViewInfo.setVisibility(View.VISIBLE);
        }
        else{
            linearLayoutTitulos.setVisibility(View.VISIBLE);
            listViewAttendances.setVisibility(View.VISIBLE);
            textViewInfo.setVisibility(View.INVISIBLE);

            // Definimos el adaptador propio y lo asociamos al ListView
            AttendanceAdapter adaptador = new AttendanceAdapter(this, arrayOfAttendances);
            listViewAttendances.setAdapter(adaptador);
            // Ponemos la atención en el último ítem del ListView
            listViewAttendances.setSelection(0);
        }
    }
}