package com.example.mobiiliohjelmointiryhmaq;
import android.Manifest;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

// Kuvioita ja fontteja voisi pienentää?

public class FirstFragment extends AppCompatActivity {
    CardView kortti1, kortti2, kortti3, kortti4;
    Button logOutBtn,button, addCity;
    private RelativeLayout Koti;
    private ProgressBar LadataanSivu;
    private TextView KaupunginNimi, Lampotila, Ehto, pvm, tuntuuKuin, tuulenNps, ilmanKst, nykyinenMaa, ilmanpaine;
    private TextInputEditText MuokkaaKaupunkia;
    private ImageView TaustaVariIV, SaaTilaIV, EtsiLogoIV;
    private RecyclerView RecycleSaaEnnuste;
    Date date = new Date();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
    String str = formatter.format(date);
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final String kotiKaupunki = user.getDisplayName();
    private final String url = "https://api.openweathermap.org/data/2.5/weather?q="+ kotiKaupunki +"&units=metric&appid=a012a806d418509506a86dcde2dc62bb";

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
            ilmanpaine = findViewById(R.id.IlmanPaineTeksti);
            SaaTilaIV = findViewById(R.id.idSaaTila);
            Button logOutBtn = (Button) findViewById(R.id.idLogOut);
            pvm = findViewById(R.id.idDate);
            Button button = (Button) findViewById(R.id.idHaeLisaaBtn);
            Button addCity = (Button) findViewById(R.id.idKotiKaupunki);

            // Määritetään kortit, jotta voidaan tehdä päänäytöstä tyhjä
            // jos kotikaupunkia ei ole määritetty
            kortti1 = (CardView) findViewById(R.id.Kuvakortti1);
            kortti2 = (CardView) findViewById(R.id.Kuvakortti2);
            kortti3 = (CardView) findViewById(R.id.Kuvakortti3);
            kortti4 = (CardView) findViewById(R.id.Kuvakortti4);

            // Määritetään päivämäärä
            pvm.setText(str);

            // Määritetään kortit näkymättömiksi jos kotikaupunki on tyhjä
            if ( kotiKaupunki == "" ) {
                KaupunginNimi.setText("Lisää kaupunki");
                kortti1.setVisibility(View.GONE);
                kortti2.setVisibility(View.GONE);
                kortti3.setVisibility(View.GONE);
                kortti4.setVisibility(View.GONE);
            } else {
                kortti1.setVisibility(View.VISIBLE);
                kortti2.setVisibility(View.VISIBLE);
                kortti3.setVisibility(View.VISIBLE);
                kortti4.setVisibility(View.VISIBLE);

                // Datan näyttäminen
                getData();
            }

            addCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToUser();
                }
            });

            // Toinen sivu
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        goToSecondFragment();
                    }
            });

            logOutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToLogInFragment();
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
                JSONArray jsonWeather = jsonResponse.getJSONArray("weather");


                // Haetaan taulun sisällä olevien taulujen objecteja
                String currentWeather = jsonMain.getString("temp"); // LÄMPÖTILA
                String wind = jsonWind.getString("speed");// TUULENNOPEUS
                String humidity = jsonMain.getString("humidity");// KOSTEUS
                String feelsLike = jsonMain.getString("feels_like"); // TUNTUU KUIN
                String pressure = jsonMain.getString("pressure");
                String maa = jsonObjectSys.getString("country");// MAA

                // String cityName = jsonResponse.getString("name");// KAUPUNKI --> Jos haetaan tieto jostain muualta kuin String
                JSONObject object = jsonWeather.getJSONObject(0);

                String icons = object.getString("icon");

                String imageUri = "https://openweathermap.org/img/wn/" + icons + "@2x.png";
                Picasso.get().load(imageUri).into(SaaTilaIV);
                Picasso.get().load(imageUri).resize(380, 380).into(SaaTilaIV);


                // Määritetään tekstit
                Lampotila.setText(currentWeather + " °C");
                tuntuuKuin.setText(feelsLike + " °C");
                tuulenNps.setText(wind + " m/s");
                ilmanKst.setText(humidity + " g/m³");
                ilmanpaine.setText(pressure + " Pa");
                nykyinenMaa.setText(maa);
                KaupunginNimi.setText(kotiKaupunki);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {

        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        //Voidaan mahdollisesti hakea GPS avulla, jos jää aikaa.
    }
    // Lähdetään toiselle sivulle
    public void goToSecondFragment() {
        Intent intent = new Intent(this, SecondFragment.class);
        startActivity(intent);
    }
    public void goToUser() {
        Intent intent = new Intent(this, UserFragment.class);
        startActivity(intent);
    }
    // Kirjaudutaan ulos
    public void goToLogInFragment() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LogInFragment.class);
        startActivity(intent);
        Toast.makeText(FirstFragment.this, "Logged out", Toast.LENGTH_SHORT).show();
    }
}
