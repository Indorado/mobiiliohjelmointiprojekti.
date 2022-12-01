package com.example.mobiiliohjelmointiryhmaq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public class SecondFragment extends AppCompatActivity {
    CardView kortti5, kortti6, kortti7, kortti8;
    EditText muokkaaKaupunkia;
    TextView nykyinenKaupunki, nykyinenMaa, pvm, lampotila, tuulenNps, ilmanKst, tuntuuKuin;
    ImageView saatila;
    Date date = new Date();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
    String str = formatter.format(date);

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
        saatila = findViewById(R.id.idSaaTila);
        tuntuuKuin = findViewById(R.id.idTuntuuKuin);

        kortti5 = (CardView) findViewById(R.id.Kuvakortti11);
        kortti6 = (CardView) findViewById(R.id.Kuvakortti22);
        kortti7 = (CardView) findViewById(R.id.Kuvakortti33);
        kortti8 = (CardView) findViewById(R.id.Kuvakortti44);

        // Määritetään kortit näkymättömiksi jos kotikaupunki on tyhjä
        kortti5.setVisibility(View.GONE);
        kortti6.setVisibility(View.GONE);
        kortti7.setVisibility(View.GONE);
        kortti8.setVisibility(View.GONE);
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
                    JSONArray jsonWeather = jsonResponse.getJSONArray("weather");

                    String wind = jsonWind.getString("speed");// TUULENNOPEUS
                    String humidity = jsonMain.getString("humidity");// KOSTEUS
                    String feelsLike = jsonMain.getString("feels_like");// TUNTUU KUIN... EI HAETA VIELÄ!
                    String maa = jsonObjectSys.getString("country");// MAA
                    String currentWeather = jsonMain.getString("temp");// LÄMPÖTILA
                    String cityName = jsonResponse.getString("name");// KAUPUNKI
                    JSONObject object = jsonWeather.getJSONObject(0);

                    String icons = object.getString("icon");

                    String imageUri = "https://openweathermap.org/img/wn/" + icons + "@2x.png";
                    Picasso.get().load(imageUri).into(saatila);
                    Picasso.get().load(imageUri).resize(380, 380).into(saatila);

                    lampotila.setText(currentWeather + " °C");
                    tuntuuKuin.setText(feelsLike + " °C");
                    nykyinenKaupunki.setText(cityName + ",");
                    nykyinenMaa.setText(maa);
                    tuulenNps.setText(wind + " m/s");
                    ilmanKst.setText(humidity + " g/m³");
                    pvm.setText(str);

                    // Määritetään kortit näkyviin, kun kaupunkia on haettu
                    kortti5.setVisibility(View.VISIBLE);
                    kortti6.setVisibility(View.VISIBLE);
                    kortti7.setVisibility(View.VISIBLE);
                    kortti8.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, error -> {
            });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
            closeKeyboard();
        }
    }

    // Suljetaan näppäimistö, kun painetaan "hae" nappia
    private void closeKeyboard() {
        // Haetaan kohdennettu kohde (hakupalkki) getData funktiosta
        View view = this.getCurrentFocus();

        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
