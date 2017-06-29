package com.guma.desarrollo.gmv.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.guma.desarrollo.core.Actividad;
import com.guma.desarrollo.core.Actividades_model;
import com.guma.desarrollo.core.Agenda_model;
import com.guma.desarrollo.core.Clock;
import com.guma.desarrollo.core.ManagerURI;
import com.guma.desarrollo.core.Razon;
import com.guma.desarrollo.core.RazonDetalle;
import com.guma.desarrollo.core.Razon_model;
import com.guma.desarrollo.core.Row;
import com.guma.desarrollo.core.SQLiteHelper;
import com.guma.desarrollo.gmv.Adapters.ActividadesAdapter;
import com.guma.desarrollo.gmv.Adapters.CustomArrayAdapter;
import com.guma.desarrollo.gmv.Adapters.RazonesAdapter;
import com.guma.desarrollo.gmv.CategoriaInfo;
import com.guma.desarrollo.gmv.R;
import com.guma.desarrollo.gmv.api.Notificaciones;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RazonesActivity extends AppCompatActivity {
    List<Row> rows;
    List<Actividad> datos;
    private ListView listView;
    private ArrayList<CategoriaInfo> deptList = new ArrayList<>();
    TextView textView,textView2;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String CodCls,IdRazon;
    EditText etObservacion;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razones);
        listView = (ListView) findViewById(R.id.list);
        final RazonesAdapter listAdapter;
        timer = new Timer();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        setTitle(preferences.getString("NameClsSelected"," --ERROR--"));

        CodCls =  preferences.getString("ClsSelected","");

        textView2 = (TextView) findViewById(R.id.idTimer);
        //simpleExpandableListView = (ExpandableListView) findViewById(R.id.simpleExpandableListViewRazon);
        //listAdapter = new ActividadesAdapter(RazonActivity.this,deptList);
        //simpleExpandableListView.setAdapter(listAdapter);

        //listAdapter = new RazonesAdapter(RazonesActivity.this,deptList);
        datos = Actividades_model.getActividades(ManagerURI.getDirDb(), RazonesActivity.this);

        rows = new ArrayList<>(datos.size());
        for (int i = 0; i < datos.size(); i++) {
            Row row = new Row();
            row.setTitle(datos.get(i).getmActividad());
            row.setSubtitle(datos.get(i).getmCategoria());
            row.setSubsubtitle(datos.get(i).getmIdAE());
            row.setChecked(false);
            rows.add(row);
        }
        listView.setAdapter(new CustomArrayAdapter(this, rows));

        findViewById(R.id.btnSaveRazones).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /*verificar si hay al menos marcado uno para guardar*/
                ListView lvv = (ListView)findViewById(R.id.list);
                int CantReg=0;
                for (int j=0;j<lvv.getAdapter().getCount();j++){
                    CheckBox tvvTest  = (CheckBox) lvv.getAdapter().getView(j,null,null).findViewById(R.id.checkBox);
                    if (tvvTest.isChecked())
                    {
                        CantReg++;
                    }
                }
                if (CantReg>0)
                {
                /*preguntar si desea guardar*/
                    AlertDialog.Builder builder = new AlertDialog.Builder(RazonesActivity.this);
                    builder.setMessage("Desea Guardar la Visita?")
                            .setPositiveButton("Si",new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        /*INICIO GUARDAR*/
                                            ListView lv = (ListView)findViewById(R.id.list);
                                            //int count = lv.getAdapter().getCount();
                                            etObservacion = (EditText) findViewById(R.id.etObservacion);

                                            Razon ra = new Razon();
                                            ArrayList<RazonDetalle> rd = new ArrayList<RazonDetalle>();
                                            int key = SQLiteHelper.getId(ManagerURI.getDirDb(), RazonesActivity.this, "RAZON");
                                            IdRazon = preferences.getString("VENDEDOR", "00") + "R" + Clock.getIdDate() + String.valueOf(key);
                                            ra.setmIdRazon(IdRazon);
                                            ra.setmVendedor(preferences.getString("VENDEDOR",""));
                                            ra.setmCliente(preferences.getString("ClsSelected",""));
                                            ra.setmNombre(preferences.getString("NameClsSelected"," --ERROR--"));
                                            ra.setmFecha(Clock.getNow());
                                            ra.setmObservacion(etObservacion.getText().toString());
                                            ra.setmSend("0");

                                            for (int i = 0; i < lv.getAdapter().getCount(); i++)
                                            {
                                                TextView tvActividad = (TextView) lv.getAdapter().getView(i,null,null).findViewById(R.id.textViewTitle);
                                                TextView tvCategoria = (TextView) lv.getAdapter().getView(i,null,null).findViewById(R.id.textViewSubtitle);
                                                TextView tvIdAE = (TextView) lv.getAdapter().getView(i,null,null).findViewById(R.id.textViewSubSubtitle);
                                                CheckBox tvTest  = (CheckBox) lv.getAdapter().getView(i,null,null).findViewById(R.id.checkBox);

                                                if (tvTest.isChecked())
                                                {
                                                    rd.add(new RazonDetalle(IdRazon,tvIdAE.getText().toString(),tvActividad.getText().toString(),tvCategoria.getText().toString()));
                                                    ra.setRdet(rd);
                                                }
                                            }

                                            editor.putString("FINAL",Clock.getTime()).apply();
                                            Razon_model.SaveRazon(RazonesActivity.this,ra);
                                            Agenda_model.SaveLog(RazonesActivity.this,"RAZON","TIPO DE VISITA: RAZON: "+IdRazon);

                                            /*FIN GUARDAR*/
                                            startActivity(new Intent(RazonesActivity.this,AccionesActivity.class));
                                            editor.putString("BANDERA","2").apply();
                                            finish();
                                        }
                                    }
                            ).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();
                }
                else
                {
                    new Notificaciones().Alert(RazonesActivity.this,"Sin Actividades","Favor Seleccione al menos una Actividad como Razón de Visita...").setCancelable(false).setPositiveButton("OK", null).show();
                }

            }
        });
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                mHandler.obtainMessage(1).sendToTarget();
            }
        }, 0, 1000);
    }
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
                textView2.setText(Clock.getDiferencia(Clock.StringToDate(preferences.getString("iniTimer", "0000-00-00 00:00:00"), "yyyy-mm-dd HH:mm:ss"), Clock.StringToDate(Clock.getNow(), "yyyy-mm-dd HH:mm:ss"), "Timer"));
        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(RazonesActivity.this,AgendaActivity.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
