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
import com.guma.desarrollo.core.Productos;
import com.guma.desarrollo.gmv.R;

import java.util.List;

/**
 * Created by maryan.espinoza on 07/03/2017.
 */

public class Productos_Leads extends ArrayAdapter<Productos> {
    private AssetManager assetMgr;
    public Productos_Leads(Context context, List<Productos> objects) {
        super(context, 0, objects);
        assetMgr = context.getResources().getAssets();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         if (null == convertView) {
           convertView = inflater.inflate(R.layout.list_productos,parent,false);
         }

        TextView tProducto  = (TextView) convertView.findViewById(R.id.lst_producto);
        TextView tFactura   = (TextView) convertView.findViewById(R.id.lst_factura);
        TextView tDocumento = (TextView) convertView.findViewById(R.id.lst_documento);

        tProducto.setTypeface(Typeface.createFromAsset(assetMgr ,"fonts/roboto_light_italic.ttf"));
        tFactura.setTypeface(Typeface.createFromAsset(assetMgr ,"fonts/roboto_light_italic.ttf"));
        tDocumento.setTypeface(Typeface.createFromAsset(assetMgr ,"fonts/roboto_light_italic.ttf"));

        Productos p = getItem(position);

        tProducto.setText(p.getmProducto());
        tFactura.setText(p.getmFactura());
        tDocumento.setText(p.getmDOc());
        return convertView;
    }
}
