package com.example.student.telefony;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PomocnikBD extends SQLiteOpenHelper
{
    private Context mKontekst;
    public final static int WERSJA_BAZY = 1;
    public final static String NAZWA_BAZY = "baza_telefonow";
    public final static String NAZWA_TABELI = "telefony";
    public final static String ID = "_id";
    public final static String PRODUCENT = "producent";
    public final static String MODEL = "model";
    public final static String ANDROID = "android";
    public final static String WWW = "www";
    public final static String ETYKIETA = "PomocnikBD";
    public final static String TW_BAZY = "CREATE TABLE " + NAZWA_TABELI + "(" + ID + " integer primary key autoincrement, " + PRODUCENT + " text not null," + MODEL + " text not null," + ANDROID + " text not null," + WWW + " text);";
    private static final String KAS_BAZY = "DROP TABLE IF EXISTS " + NAZWA_TABELI;

    public PomocnikBD(Context context)
    {
        super(context, NAZWA_BAZY, null, WERSJA_BAZY);
        mKontekst = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        db.execSQL(TW_BAZY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

        db.execSQL(KAS_BAZY);
        onCreate(db);
    }
}