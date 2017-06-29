package com.guma.desarrollo.gmv.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.guma.desarrollo.core.Agenda;
import com.guma.desarrollo.core.Agenda_model;
import com.guma.desarrollo.core.Articulo;
import com.guma.desarrollo.core.Clientes;
import com.guma.desarrollo.core.Clientes_model;
import com.guma.desarrollo.core.Clock;
import com.guma.desarrollo.core.Cobros;
import com.guma.desarrollo.core.Cobros_model;
import com.guma.desarrollo.core.Facturas;
import com.guma.desarrollo.core.ManagerURI;
import com.guma.desarrollo.core.Pedidos_model;
import com.guma.desarrollo.core.Productos;
import com.guma.desarrollo.core.Razon_model;
import com.guma.desarrollo.gmv.Adapters.CustomAdapter;
import com.guma.desarrollo.gmv.Adapters.Facturas_Leads;
import com.guma.desarrollo.gmv.Adapters.Productos_Leads;
import com.guma.desarrollo.gmv.ChildInfo;
import com.guma.desarrollo.gmv.GroupInfo;
import com.guma.desarrollo.gmv.MyApplication;
import com.guma.desarrollo.gmv.R;
import com.guma.desarrollo.gmv.Tasks.TaskDownload;
import com.guma.desarrollo.gmv.Tasks.TaskUnload;
import com.guma.desarrollo.gmv.api.Class_retrofit;
import com.guma.desarrollo.gmv.api.ConnectivityReceiver;
import com.guma.desarrollo.gmv.api.Notificaciones;
import com.guma.desarrollo.gmv.api.Servicio;
import com.guma.desarrollo.gmv.models.Respuesta_productos;
import com.guma.desarrollo.gmv.models.Respuesta_usuario;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgendaActivity extends AppCompatActivity  implements ConnectivityReceiver.ConnectivityReceiverListener {
    private Menu menu;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private boolean checked;
    public ProgressDialog pdialog;
    private ListView listView;
    private String Usuario;
    ArrayList<Productos> aryProductos;
    TextView Contadores,txtError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        aryProductos = new ArrayList<>();
        loadData();
        ReferenciasContexto.setContextArticulo(AgendaActivity.this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        Usuario = preferences.getString("Id","0");
        setTitle(preferences.getString("Nombre","0").toUpperCase());
        checked = preferences.getBoolean("pref",false);
        listView = (ListView) findViewById(R.id.lstFacturas);
        listView = (ListView) findViewById(R.id.lstProductos);
        pdialog = ProgressDialog.show(AgendaActivity.this, "","Procesando...", true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivityForResult(new Intent(AgendaActivity.this,FacturasActivity.class).putExtra("IdU",((Productos) adapterView.getItemAtPosition(i)).getmId()),0);
            }
        });
        Contadores = (TextView) findViewById(R.id.cProducto);
        txtError = (TextView) findViewById(R.id.tError);


        findViewById(R.id.imgMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[]items= {"ACERCA DE", "SALIR"};
                new AlertDialog.Builder(v.getContext()).setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (items[which].equals(items[0])){
                            Toast.makeText(AgendaActivity.this, "LLAMARA AL ACERCA DE", Toast.LENGTH_SHORT).show();
                        }else{
                            if (items[which].equals(items[1])){
                                editor.putBoolean("pref", checked = !checked).apply();
                                finish();
                            }else{
                                Toast.makeText(AgendaActivity.this, "Se produjo un error", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }).create().show();

            }
        });


        //ESTILO PARA LA TABLA
        AssetManager assetMgr = getResources().getAssets();
        TextView tLabel1,tLabel2,tLabel3,tLabel4;
        tLabel1 = (TextView) findViewById(R.id.t1);
        tLabel2 = (TextView) findViewById(R.id.t2);
        tLabel3 = (TextView) findViewById(R.id.t3);
        tLabel4 = (TextView) findViewById(R.id.t4);
        tLabel1.setTypeface(Typeface.createFromAsset(assetMgr ,"fonts/roboto_bold.ttf"));
        tLabel2.setTypeface(Typeface.createFromAsset(assetMgr ,"fonts/roboto_bold.ttf"));
        tLabel3.setTypeface(Typeface.createFromAsset(assetMgr ,"fonts/roboto_bold.ttf"));
        tLabel4.setTypeface(Typeface.createFromAsset(assetMgr ,"fonts/roboto_bold.ttf"));
        Contadores.setTypeface(Typeface.createFromAsset(assetMgr ,"fonts/roboto_bold.ttf"));



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0 && resultCode==RESULT_OK){

        }
    }



    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }
    private void showSnack(boolean isConnected) {
       // menu.getItem(0).setIcon(isConnected ? getResources().getDrawable(R.drawable.btngreen) : getResources().getDrawable(R.drawable.btnred));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        checkConnection();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_agenda, menu);
        this.menu = menu;
        return true;
    }


    private void loadData(){
        new TaskLogin().execute();

    }

    private class TaskLogin extends AsyncTask<String,String,String> {


        @Override
        protected String doInBackground(String... params) {
            Class_retrofit.Objfit().create(Servicio.class).obtenerProductos(Integer.parseInt(Usuario)).enqueue(new Callback<Respuesta_productos>() {
                @Override
                public void onResponse(Call<Respuesta_productos> call, Response<Respuesta_productos> response) {
                    if(response.isSuccessful()){
                        txtError.setVisibility(TextView.VISIBLE);
                        if (response.body().getResults().get(0).getmCount() == 0 ){
                            txtError.setText("No Posee productos activos.");
                            txtError.setVisibility(TextView.GONE);
                            pdialog.dismiss();
                        }else {
                            aryProductos = response.body().getResults();
                            listView.setAdapter(new Productos_Leads(AgendaActivity.this, aryProductos));
                            Contadores.setText("CANTIDAD DE SOLICITUDES: " + aryProductos.size() );
                            pdialog.dismiss();
                        }
                    }else{

                        txtError.setText("No es posible cargar los datos en este momento.");
                        pdialog.dismiss();
                    }
                }
                @Override
                public void onFailure(Call<Respuesta_productos> call, Throwable t) {
                    txtError.setText("No es posible cargar los datos en este momento.");
                    pdialog.dismiss();
                }
            });
            return null;
        }
    }
}