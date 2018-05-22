package com.example.student.telefony;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditActivity extends Activity
{
    private long idRow;
    private EditText prodecentEditExt;
    private EditText modelEdiText;
    private EditText wersioneEdiText;
    private EditText webEdiText;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        prodecentEditExt = (EditText)findViewById(R.id.producentEdycja);
        modelEdiText = (EditText)findViewById(R.id.modelEdycja);
        wersioneEdiText = (EditText)findViewById(R.id.androidEdycja);
        webEdiText = (EditText)findViewById(R.id.wwwEdycja);


        idRow = -1;

        if(savedInstanceState != null)
        {
            idRow = savedInstanceState.getLong(PomocnikBD.ID);
        }

        else
        {
            Bundle bundle = getIntent().getExtras();

            if(bundle != null)
            {
                idRow = bundle.getLong(PomocnikBD.ID);
            }
        }

        if(idRow!= -1)
        {
            fillField();
        }
    }


    public void zapisz(View view)
    {
        if(sprawdzNapisy())
        {
            ContentValues wartosci = new ContentValues();
            wartosci.put(PomocnikBD.PRODUCENT, prodecentEditExt.getText().toString());
            wartosci.put(PomocnikBD.MODEL, modelEdiText.getText().toString());
            wartosci.put(PomocnikBD.ANDROID, wersioneEdiText.getText().toString());
            wartosci.put(PomocnikBD.WWW, webEdiText.getText().toString());

            if(idRow == -1)
            {
                Uri uriNowego = getContentResolver().insert(Phoneprovider.URI_ZAWARTOSCI, wartosci);
                idRow = Integer.parseInt(uriNowego.getLastPathSegment());
            }

            else
            {
                getContentResolver().update(ContentUris.withAppendedId(Phoneprovider.URI_ZAWARTOSCI, idRow), wartosci, null, null);
            }

            setResult(RESULT_OK);
            finish();
        }
        else
        {
            Toast.makeText(this, " Nie wszystkie pola zostały wypełnione bądź są wypełnione nieprawidłowo", Toast.LENGTH_SHORT).show();
        }
    }
    public void anuluj(View view)
    {
        setResult(RESULT_CANCELED);
        finish();
    }
    public void www(View view)
    {
        if(!webEdiText.getText().toString().equals("")) //Sprawdzenie czy pole WWW nie jest puste
        {
            String adres = webEdiText.getText().toString();
            String regex = "www\\.[a-zA-Z0-9]*\\.[a-zA-Z]*";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(adres);

if(matcher.matches()) {
    if (!adres.startsWith("http://") && !adres.startsWith("https://")) //Sprawdzenie czy adres zaczyna się od http:// lub https:// i jeśli nie to dodanie http:// do adresu
    {
        adres = "http://" + adres;
    }
    Intent zamiarPrzegladarki = new Intent("android.intent.action.VIEW", Uri.parse(adres));
    startActivity(zamiarPrzegladarki);

                    }
else{Toast.makeText(this, "zly adres strony", Toast.LENGTH_SHORT).show();}
                     }
        else
        {
            Toast.makeText(this, "Wypełnij adres strony", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putLong(PomocnikBD.ID, idRow);
    }
    private boolean sprawdzNapisy()
    {
        String adres = webEdiText.getText().toString();
        String regex = "www\\.[a-zA-Z0-9]*\\.[a-zA-Z]*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(adres);
        return !(prodecentEditExt.getText().toString().equals("") || modelEdiText.getText().toString().equals("") || wersioneEdiText.getText().toString().equals("") || webEdiText.getText().toString().equals("")||!matcher.matches());
    }
    private void fillField()
    {
        String projekcja[] = { PomocnikBD.PRODUCENT, PomocnikBD.MODEL, PomocnikBD.ANDROID, PomocnikBD.WWW };
        Cursor kursorTel = getContentResolver().query(ContentUris.withAppendedId(Phoneprovider.URI_ZAWARTOSCI, idRow), projekcja, null, null, null);
        kursorTel.moveToFirst();
        int indeksKolumny = kursorTel .getColumnIndexOrThrow(PomocnikBD.PRODUCENT);
        String wartosc = kursorTel.getString(indeksKolumny);
        prodecentEditExt.setText(wartosc);
        modelEdiText.setText(kursorTel.getString(kursorTel.getColumnIndexOrThrow(PomocnikBD.MODEL)));
        wersioneEdiText.setText(kursorTel.getString(kursorTel .getColumnIndexOrThrow(PomocnikBD.ANDROID)));
        webEdiText.setText(kursorTel.getString(kursorTel .getColumnIndexOrThrow(PomocnikBD.WWW)));
        kursorTel.close();
    }
}