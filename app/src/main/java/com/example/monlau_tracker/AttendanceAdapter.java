package com.example.monlau_tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AttendanceAdapter extends ArrayAdapter<Attendance> {
    // El constructor solo recibe dos parámetros, pero le pasa tres al padre
    // El Layout que se le pasa es el 0 debido a que se le pasa uno propio más abajo al realizar el Inflate
    public AttendanceAdapter(Context context, ArrayList<Attendance> attendances) {
        super(context, 0, attendances);
    }

    private static class ViewHolder {
        TextView nombre;
        ImageView status;
    }
    // El método getView se llamará tantas veces como registros tengan los datos a visualizar
    // Si el array usado posee 10 valores el getView se llamará 10 veces
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        // En la variable "position" tenemos la posición del array que estoy pintando.
        // El getItem es un método propio del ArrayAdapter, en este caso el tipo de adaptador usado es el de la clase "Attendance"
        // por lo tanto el getItem nos devolverá un objeto de tipo "Attendance" que está en la posición "position"
        // En los usos básicos de adaptadores en los spinner se usa un ArrayAdapter<string>,
        // por lo tanto el getItem nos devolvería un String
        Attendance attendance = getItem(position);
        // Validamos si nos pasan por parámetro la vista a visualizar
        // En caso que esté vacía usaremos la vista (el Layout) que hemos creado para visualizar los elementos
        // El Inflater se encarga de pintarlo
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_attendance, parent, false);
            // Crear un ViewHolder para contener las vistas de la fila
            viewHolder = new ViewHolder();
            viewHolder.status = convertView.findViewById(R.id.status);
            convertView.setTag(viewHolder);
        }
        else {
            // Reutilizar el ViewHolder existente
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Creamos las variables que apuntan a los TextView o ImageView definidos en el layout "item_attendance.xml"
        TextView nombre = (TextView) convertView.findViewById(R.id.nombre);
        ImageView status = (ImageView) convertView.findViewById(R.id.status);
        // Informamos los valores de los TextView o ImageView
        nombre.setText(attendance.nombre);
        if(attendance.status.equals("presente")){
            status.setImageResource(R.drawable.green_circle);
        }
        else if(attendance.status.equals("ausente")){
            status.setImageResource(R.drawable.red_circle);
        }
        else if (attendance.status.equals("tarde")){
            status.setImageResource(R.drawable.yellow_circle);
        }
        // Devolvemos la vista para que se pinte (Render) por la pantalla
        return convertView;
    }
}