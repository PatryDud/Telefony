package com.example.student.telefony;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
    private SimpleCursorAdapter mAdapterKursora;
    private ListView mLista;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLista = (ListView) findViewById(R.id.lista_wartosci);
        wypelnijPola();

        mLista.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mLista.setMultiChoiceModeListener(wyborWieluElementowListy()); //Listener do listy, służy do zaznaczenia wielu elementów

        mLista.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent zamiar = new Intent(MainActivity.this, EditActivity.class);
                zamiar.putExtra(PomocnikBD.ID, l);
                startActivityForResult(zamiar, 0);
            }
        });
    }

    private void wypelnijPola()
    {
        getLoaderManager().initLoader(0, null, this);
        String[] mapujZ = new String[] { PomocnikBD.PRODUCENT, PomocnikBD.MODEL };
        int[] mapujDo = new int[] { R.id.producentText, R.id.modelText };
        mAdapterKursora = new SimpleCursorAdapter(this, R.layout.list_row, null, mapujZ, mapujDo, 0);
        mLista.setAdapter(mAdapterKursora);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        String[] projekcja = { PomocnikBD.ID, PomocnikBD.PRODUCENT, PomocnikBD.MODEL };
        CursorLoader loaderKursora = new CursorLoader(this, Phoneprovider.URI_ZAWARTOSCI, projekcja, null, null, null);
        return loaderKursora;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor dane)
    {
        mAdapterKursora.swapCursor(dane);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        mAdapterKursora.swapCursor(null);
    }


    private void tworzElement()
    {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(PomocnikBD.ID, (long) -1);
        startActivityForResult(intent, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        getLoaderManager().restartLoader(0, null, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pasek_akcji_lista_tel, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.dodaj) //Instrukcja wykona się gdy zostanie wciśnięty przycisk "Dodaj"
        {
            tworzElement();
        }

        return super.onOptionsItemSelected(item);
    }


    private AbsListView.MultiChoiceModeListener wyborWieluElementowListy()
    {
        return new AbsListView.MultiChoiceModeListener()
        {
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu)
            {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {}

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {}


            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu)
            {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.pasek_kontekstowy_listy, menu);
                return true;
            }


            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item)
            {
                if(item.getItemId() == R.id.kasuj_menu) //Instrukcja wykona się gdy zostanie wciśnięty przycisk "USUŃ"
                {
                    long[] zaznaczone = mLista.getCheckedItemIds();

                    for(int i = 0; i < zaznaczone.length; i++)
                    {
                        getContentResolver().delete(ContentUris.withAppendedId(Phoneprovider.URI_ZAWARTOSCI, zaznaczone[i]), null, null);
                    }

                    return true;
                }

                return false;
            }
        };
    }
}