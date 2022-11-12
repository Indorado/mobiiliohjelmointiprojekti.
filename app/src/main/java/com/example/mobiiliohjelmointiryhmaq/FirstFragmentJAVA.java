package com.example.mobiiliohjelmointiryhmaq;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

// Tämä JAVA tiedosto on sitä varten että sovellusta aukaistaessa tulee lataus ympyrä pyörimään
// saadaan xml tiedostosta käyttöön tiedot tätä kautta.
// tämän lisäksi pitää firstFragment.xml tiedostoon lisätä muutama juttu



public class FirstFragmentJAVA extends AppCompatActivity {

    private RelativeLayout Koti;
    private ProgressBar LadataanSivu;
    private TextView KaupunginNimi, Lampotila, Ehto;
    private TextInputEditText MuokkaaKaupunkia;
    private ImageView TaustaVariIV, SaaTilaIV, EtsiLogoIV;
    private RecyclerView RecycleSaaEnnuste;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        Koti = findViewById(R.id.idKoti);
        LadataanSivu = findViewById(R.id.idPBLoading);
        KaupunginNimi = findViewById(R.id.idNykyinenKaupunki);
        Lampotila = findViewById(R.id.idLampotila);
        Ehto = findViewById(R.id.idEhto);
        MuokkaaKaupunkia = findViewById(R.id.idMuokkaaKaupunkia);
        RecycleSaaEnnuste = findViewById(R.id.idRecycleSaaEnnuste);
        Koti = findViewById(R.id.idKoti);
        TaustaVariIV = findViewById(R.id.idTaustaVari);
        SaaTilaIV = findViewById(R.id.idSaaTila);
        EtsiLogoIV = findViewById(R.id.idEtsiLogo);
        */
    }
}
