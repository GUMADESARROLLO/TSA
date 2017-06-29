package com.guma.desarrollo.gmv.Adapters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.guma.desarrollo.core.Facturas;
import com.guma.desarrollo.core.Funciones;
import com.guma.desarrollo.core.Historico;
import com.guma.desarrollo.gmv.R;

import java.util.List;

/**
 * Created by maryan.espinoza on 07/03/2017.
 */

public class Facturas_Leads extends ArrayAdapter<Historico> {
    private AssetManager assetMgr;
    public Facturas_Leads(Context context, List<Historico> objects) {
        super(context, 0, objects);
        assetMgr = context.getResources().getAssets();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (null == convertView) {
           convertView = inflater.inflate(R.layout.list_facturas_puntos,parent,false);
        }

        TextView tVia = (TextView) convertView.findViewById(R.id.lst_fecha);
        TextView tStatus = (TextView) convertView.findViewById(R.id.lst_factura);
        TextView tFecha = (TextView) convertView.findViewById(R.id.lst_puntos);

        tVia.setTypeface(Typeface.createFromAsset(assetMgr ,"fonts/roboto_light_italic.ttf"));
        tStatus.setTypeface(Typeface.createFromAsset(assetMgr ,"fonts/roboto_light_italic.ttf"));
        tFecha.setTypeface(Typeface.createFromAsset(assetMgr ,"fonts/roboto_light_italic.ttf"));

        Historico h = getItem(position);

        tVia.setText(h.getmVia());
        tStatus.setText(h.getmStatus());
        tFecha.setText(h.getmFecha());
        // Factura.setText(lead.getmFactura());
        // Fecha.setText(lead.getmFecha());
        // Remanente.setText(Funciones.NumberFormat(Float.parseFloat(lead.getmRemanente())));
         return convertView;
    }
}
