package com.example.oliver.clima_tepic;

import android.media.audiofx.AudioEffect;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String P = "http://api.openweathermap.org/data/2.5/weather?q=Tepic,mx&APPID=e4b59b7a742afcd62822bd2d4bbd9a1c";
    URL url;
    String json_string;

    TextView principal, descripcion, temperatura, humedad, min, max, visibilidad, viento, pais, nubes, amanecer,
    puesta, ciudad;

    FloatingActionButton floatingActionButton, floatingActionButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        principal = (TextView) findViewById(R.id.tv_principal);
        descripcion = (TextView) findViewById(R.id.tv_descrip);
        temperatura = (TextView) findViewById(R.id.tv_temp);
        humedad = (TextView) findViewById(R.id.tv_hum);
        min = (TextView) findViewById(R.id.tv_temp_min);
        max = (TextView) findViewById(R.id.tv_temp_max);
        visibilidad = (TextView) findViewById(R.id.tv_visib);
        viento = (TextView) findViewById(R.id.tv_wind_speed);
        nubes = (TextView) findViewById(R.id.tv_nubes);
        pais= (TextView) findViewById(R.id.tv_pais);
        amanecer = (TextView)findViewById(R.id.tv_sunrise);
        puesta = (TextView) findViewById(R.id.tv_sunset);
        ciudad = (TextView) findViewById(R.id.tv_city);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.floatingActionButton2);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    listaClima();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiar();
            }
        });
    }


    public void listaClima() throws IOException, JSONException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        url = new URL(P);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        int respuesta = connection.getResponseCode();
        InputStream inputStream = connection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();

        if (respuesta== HttpURLConnection.HTTP_OK){
            while((json_string = bufferedReader.readLine()) !=null){
                stringBuilder.append(json_string + "\n");
            }

            bufferedReader.close();
            inputStream.close();
            connection.disconnect();
            String temporal = stringBuilder.toString();
            JSONObject jsonObject = new JSONObject(temporal);
            JSONArray weather = jsonObject.getJSONArray("weather");

            for(int i=0; i<weather.length();i++){
                JSONObject c = weather.getJSONObject(i);
                principal.setText("Principal: "+c.getString("main"));
                descripcion.setText("Descripcion: "+c.getString("description"));;
            }//for

            JSONObject main = jsonObject.getJSONObject("main");
            temperatura.setText("Temperatura: "+ main.getString("temp"));
            humedad.setText("Humedad: "+main.getString("humidity"));
            min.setText("Temperatura Minima: "+main.getString("temp_min"));
            max.setText("Temperatura Maxima: "+main.getString("temp_max"));

            visibilidad.setText("Visibilidad: "+jsonObject.getString("visibility"));

            JSONObject wind = jsonObject.getJSONObject("wind");
            viento.setText("Velocidad del Viento: "+wind.getString("speed"));

            JSONObject clouds = jsonObject.getJSONObject("clouds");
            nubes.setText("Nubosidad(%): "+clouds.getString("all"));;
            JSONObject sys = jsonObject.getJSONObject("sys");

            pais.setText("Pais: "+sys.getString("country"));
            amanecer.setText("Amanecer(ms)"+sys.getString("sunrise"));;
            puesta.setText("Puesta de Sol: "+sys.getString("sunset"));;
            ciudad.setText("Ciudad: "+jsonObject.getString("name"));
        }

    }//listaclima

    public void limpiar(){

        principal.setText("Principal:");
        descripcion.setText("Descripcion:");
        temperatura.setText("Temperatura:");
        humedad.setText("Humedad:");
        min.setText("Temperatura Mínima:");
        max.setText("Temperatura Máxima.");
        visibilidad.setText("Visibilidad:");
        viento.setText("Velocidad del viento");
        nubes.setText("Nubosidad(%)");
        pais.setText("Pais:");
        amanecer.setText("Amanecer(ms):");
        puesta.setText("Puesta de Sol:");
        ciudad.setText("Ciudad: ");


    }
}
