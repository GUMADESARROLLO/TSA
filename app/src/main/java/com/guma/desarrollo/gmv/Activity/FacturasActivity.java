package com.guma.desarrollo.gmv.Activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.guma.desarrollo.core.Clientes_model;
import com.guma.desarrollo.core.Facturas;
import com.guma.desarrollo.core.Historico;
import com.guma.desarrollo.core.ManagerURI;
import com.guma.desarrollo.core.Productos;
import com.guma.desarrollo.gmv.Adapters.Facturas_Leads;
import com.guma.desarrollo.gmv.R;
import com.guma.desarrollo.gmv.api.Class_retrofit;
import com.guma.desarrollo.gmv.api.Notificaciones;
import com.guma.desarrollo.gmv.api.Servicio;
import com.guma.desarrollo.gmv.models.Respuesta_Historico;
import com.guma.desarrollo.gmv.models.Respuesta_usuario;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacturasActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,SearchView.OnCloseListener {

    private SearchView searchView;
    private MenuItem searchItem;
    private SearchManager searchManager;

    public ProgressDialog pdialog;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    TextView txtError;
    private ListView listView;
    ArrayList<Historico> fList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){ getSupportActionBar().setDisplayHomeAsUpEnabled(true); }
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        setTitle("HISTORICO");
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        pdialog = ProgressDialog.show(FacturasActivity.this, "", "Procesando...", true);
        listView = (ListView) findViewById(R.id.lstFacturas);
        fList = new ArrayList<>();
        loadData();
        TextView t1,t2,t3;
        AssetManager assetMgr = getResources().getAssets();
        t1= (TextView) findViewById(R.id.tx1);
        t2= (TextView) findViewById(R.id.tx3);
        t3= (TextView) findViewById(R.id.tx2);
        t1.setTypeface(Typeface.createFromAsset(assetMgr ,"fonts/roboto_bold.ttf"));
        t2.setTypeface(Typeface.createFromAsset(assetMgr ,"fonts/roboto_bold.ttf"));
        t3.setTypeface(Typeface.createFromAsset(assetMgr ,"fonts/roboto_bold.ttf"));
        txtError = (TextView) findViewById(R.id.tErrorHistico);



    }
    public boolean onOptionsItemSelected(MenuItem item)    {
        int id = item.getItemId();
        if (id == 16908332){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo (searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.requestFocus();
        return true;
    }
    @Override
    public boolean onClose() {
        filterData("");
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        filterData(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filterData(newText);
        return false;
    }
    private void loadData(){
        new TaskLogin().execute();

    }
    public void filterData(String query) {
        if (query.isEmpty()){
            listView.setAdapter(new Facturas_Leads(this, fList));
        }else{
            ArrayList<Historico> newList = new ArrayList<>();
            for(Historico articulo:fList){
                if (articulo.getmStatus().toLowerCase().contains(query)){
                    newList.add(articulo);
                }
            }
            listView.setAdapter(new Facturas_Leads(this, newList));
        }
    }
    private class TaskLogin extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {

            Class_retrofit.Objfit().create(Servicio.class).obtenerHistorico(Integer.parseInt(getIntent().getStringExtra("IdU"))).enqueue(new Callback<Respuesta_Historico>() {
                @Override
                public void onResponse(Call<Respuesta_Historico> call, Response<Respuesta_Historico> response) {
                    txtError.setVisibility(TextView.VISIBLE);
                    if(response.isSuccessful()){
                        if (response.body().getResults().get(0).getmCount()==0){
                            txtError.setText("No Posee historico.");
                            txtError.setVisibility(TextView.GONE);
                            pdialog.dismiss();
                        }else {
                            fList = response.body().getResults();
                            pdialog.dismiss();
                            listView.setAdapter(new Facturas_Leads(FacturasActivity.this, fList));
                        }
                    }else{
                        txtError.setText("No es posible cargar los datos en este momento.");
                        pdialog.dismiss();
                    }
                }
                @Override
                public void onFailure(Call<Respuesta_Historico> call, Throwable t) {
                    txtError.setText("No es posible cargar los datos en este momento.");
                    pdialog.dismiss();
                }
            });
            return null;
        }
    }

}
