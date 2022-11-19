package com.example.mobiiliohjelmointiryhmaq;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

// Kuvioita ja fontteja voisi pienentää?

public class FirstFragment extends AppCompatActivity {
    private Button button; // Mennään seuravaan näyttöön.

    private RelativeLayout Koti;
    private ProgressBar LadataanSivu;
    private TextView KaupunginNimi, Lampotila, Ehto, pvm, tuntuuKuin, tuulenNps, ilmanKst, nykyinenMaa;
    private TextInputEditText MuokkaaKaupunkia;
    private ImageView TaustaVariIV, SaaTilaIV, EtsiLogoIV;
    private RecyclerView RecycleSaaEnnuste;
    Date date = new Date();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
    String str = formatter.format(date);

    private final String url = "https://api.openweathermap.org/data/2.5/weather?q=Kuopio&units=metric&appid=a012a806d418509506a86dcde2dc62bb";
    // Tästä tehdään GPS homma jos ehtii..

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_first);

        KaupunginNimi = findViewById(R.id.idNykyinenKaupunki);
        Lampotila = findViewById(R.id.idLampotila);
        TaustaVariIV = findViewById(R.id.idTaustaVari);
        SaaTilaIV = findViewById(R.id.idSaaTila);
        tuntuuKuin = findViewById(R.id.TuntuuKuinTeksti);
        tuulenNps = findViewById(R.id.TuulenVoimakkuusTeksti);
        ilmanKst = findViewById(R.id.IlmankosteusTeksti);
        nykyinenMaa = findViewById(R.id.idMaa);

        pvm = findViewById(R.id.idDate);
        button = (Button) findViewById(R.id.idHaeLisaaBtn);

        // Ei tarvita??
        //Koti = findViewById(R.id.idKoti);
        //LadataanSivu = findViewById(R.id.idPBLoading);
        //Ehto = findViewById(R.id.idEhto);
        //MuokkaaKaupunkia = findViewById(R.id.idMuokkaaKaupunkia);
        //RecycleSaaEnnuste = findViewById(R.id.idRecycleSaaEnnuste);
        //EtsiLogoIV = findViewById(R.id.idEtsiLogo);

        getData();
        // Toinen sivu
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SecondFragment();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    public void getData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            // Tarkistetaan saadaanko yhteys osoitteeseen
            Log.e("Res: ", response);

            try {
                // Haetaan rajapinnan taulu tietyn kaupungin säätilasta
                JSONObject jsonResponse = new JSONObject(response);

                // Haetaan taulun sisällä olevia tauluja
                JSONObject jsonMain = jsonResponse.getJSONObject("main");
                JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                JSONObject jsonWind = jsonResponse.getJSONObject("wind");

                // Haetaan taulun sisällä olevien taulujen objecteja
                String currentWeather = jsonMain.getString("temp"); // LÄMPÖTILA
                String wind = jsonWind.getString("speed");// TUULENNOPEUS
                String humidity = jsonMain.getString("humidity");// KOSTEUS
                String feelsLike = jsonMain.getString("feels_like"); // TUNTUU KUIN
                String maa = jsonObjectSys.getString("country");// MAA
                // String cityName = jsonResponse.getString("name");// KAUPUNKI

                // Määritetään tekstit
                Lampotila.setText(currentWeather + " °C");
                tuntuuKuin.setText(feelsLike + " °C");
                tuulenNps.setText(wind + " m/s");
                ilmanKst.setText(humidity + "g/m³");
                nykyinenMaa.setText(maa);

                pvm.setText(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {

        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        KaupunginNimi.setText("Kuopio,");//Voidaan mahdollisesti hakea GPS avulla, jos jää aikaa.
    }
    // Lähdetään toiselle sivulle
    public void SecondFragment() {
        Intent intent = new Intent(this, SecondFragment.class);
        startActivity(intent);
    }
}
