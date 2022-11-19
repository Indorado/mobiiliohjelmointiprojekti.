package com.example.mobiiliohjelmointiryhmaq;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

public class SecondFragment extends AppCompatActivity {
    EditText muokkaaKaupunkia;
    TextView nykyinenKaupunki, nykyinenMaa, pvm, lampotila, tuulenNps, ilmanKst; //tuntuuKuin
    Date date = new Date();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
    String str = formatter.format(date);

    // Yritetään saada näppäimistö katoamaan itsekseen, kun "hae" nappia on painettu
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_second);

        muokkaaKaupunkia = findViewById(R.id.idMuokkaaKaupunkia);
        nykyinenKaupunki = findViewById(R.id.idNykyinenKaupunki);
        nykyinenMaa = findViewById(R.id.idNykyinenMaa);
        pvm = findViewById(R.id.idDate);
        lampotila = findViewById(R.id.idLampotila);
        tuulenNps = findViewById(R.id.idTuuliArvo);
        ilmanKst = findViewById(R.id.idKosteus);
        // tuntuuKuin = findViewById(R.id.idTuntuuKuin);
    }

    @SuppressLint("SetTextI18n")
    public void getData(View view) {
        String tempUrl;
        String city = muokkaaKaupunkia.getText().toString().trim();
        if (city.equals("")) nykyinenKaupunki.setText("Ei voi olla tyhjä");
        else {
            String url = "https://api.openweathermap.org/data/2.5/weather";
            String appid = "3b8f9987ddadf8e26fcaa86cef8bfd62";
            String units = "metric";
            tempUrl = url + "?q=" + city + "&units=" + units + "&appid=" + appid;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl, response -> {
                // Tarkistetaan saadaanko yhteys osoitteeseen
                Log.e("Res: ", response);

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONObject jsonMain = jsonResponse.getJSONObject("main");
                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    JSONObject jsonWind = jsonResponse.getJSONObject("wind");

                    String wind = jsonWind.getString("speed");// TUULENNOPEUS
                    String humidity = jsonMain.getString("humidity");// KOSTEUS
                    //String feelsLike = jsonMain.getString("feels_like");// TUNTUU KUIN... EI HAETA VIELÄ!
                    String maa = jsonObjectSys.getString("country");// MAA
                    String currentWeather = jsonMain.getString("temp");// LÄMPÖTILA
                    String cityName = jsonResponse.getString("name");// KAUPUNKI

                    lampotila.setText(currentWeather + " °C");
                    nykyinenKaupunki.setText(cityName + ",");
                    nykyinenMaa.setText(maa);
                    tuulenNps.setText(wind + " m/s");
                    ilmanKst.setText(humidity + "g/m³");
                    pvm.setText(str);
                    //tuntuuKuin.setText(feelsLike + " °C");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, error -> {
            });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

    }
}
