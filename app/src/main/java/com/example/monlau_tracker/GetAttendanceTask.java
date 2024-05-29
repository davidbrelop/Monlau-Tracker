package com.example.monlau_tracker;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class GetAttendanceTask extends AsyncTask<String, Void, String> {

    private Context context;
    private Exception exception;

    public GetAttendanceTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String username = params[0];
        String date = params[1];
        String url = "http://172.17.127.254:8200/webservice/rest/get_attendance_by_username_and_date.php";
        String resultado = null;

        try {
            // Creamos la conexión HTTP
            URL direccion = new URL(url);
            HttpURLConnection conexion = (HttpURLConnection) direccion.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setDoOutput(true);
            conexion.setConnectTimeout(100);

            // Creamos los datos del formulario
            String datos = "username=" + username + "&date=" + date;

            // Escribimos los datos del formulario en la solicitud HTTP
            OutputStream salida = conexion.getOutputStream();
            byte[] bytes = datos.getBytes(StandardCharsets.UTF_8);
            salida.write(bytes);
            salida.flush();
            salida.close();

            // Leemos la respuesta del servidor
            InputStream entrada = conexion.getInputStream();
            BufferedReader lector = new BufferedReader(new InputStreamReader(entrada));
            StringBuilder respuesta = new StringBuilder();
            String linea;

            while ((linea = lector.readLine()) != null) {
                respuesta.append(linea);
            }

            // Cerramos la conexión HTTP
            entrada.close();
            conexion.disconnect();

            // Procesamos la respuesta del servidor
            resultado = respuesta.toString();
        }
        catch (Exception e) {
            // Capturamos la excepción
            exception = e;
        }
        return resultado;
    }

    @Override
    protected void onPostExecute(String resultado) {
        super.onPostExecute(resultado);
        // Comprobamos si hay una excepción y mostramos un Toast en el hilo principal
        if (exception != null) {
            mostrarError(exception);
        }
        else{
            ArrayList arrayOfAttendances = new ArrayList<Attendance>();

            if(!resultado.equals("0 resultados")){

                Document document = convertirStringToXMLDocument(resultado);
                NodeList listaItem = document.getElementsByTagName("asistencia");

                for (int i = 0; i < listaItem.getLength(); i++) {
                    Element element = (Element) listaItem.item(i);
                    String var_nombre =  element.getElementsByTagName("nombre").item(0).getTextContent();
                    String var_status =  element.getElementsByTagName("status").item(0).getTextContent();
                    Attendance attendance = new Attendance(var_nombre, var_status);
                    arrayOfAttendances.add(attendance);
                }
            }

            // Enviar el ArrayList a la actividad principal
            ((RecordActivity) context).showAttendancesOnScreen(arrayOfAttendances);
        }
    }

    private static Document convertirStringToXMLDocument(String xmlString) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder ;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void mostrarError(final Exception e) {
        // Utilizamos un Handler para mostrar el Toast en el hilo principal
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                // Mostramos el Toast con el mensaje de error
                Toast.makeText(context, "No se ha podido establecer conexión con la base de datos.", Toast.LENGTH_LONG).show();
            }
        });
    }
}