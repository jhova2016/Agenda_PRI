package com.example.agenda_pri.Models;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda_pri.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class AdapterPromises extends RecyclerView.Adapter<AdapterPromises.ViewHolderDatos> implements View.OnClickListener {


    //ArrayList<String> ListDatos;
    ArrayList<Element_Promise> ListDatos;
    private View.OnClickListener Listener;
    private Context context;

    public AdapterPromises(ArrayList<Element_Promise> listDatos,Context context) {
        this.ListDatos = listDatos;
        this.context=context;
    }



    @NonNull
    @Override
    public AdapterPromises.ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.element_promises,parent,false);
        view.setOnClickListener(this);
        return new AdapterPromises.ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPromises.ViewHolderDatos holder, int position) {



        holder.Card.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition));



        holder.Localidad.setText(ListDatos.get(position).getLocation());
        holder.Fecha.setText(ListDatos.get(position).getEventDate());

        holder.Promised.setText(ListDatos.get(position).getPromised());

    }

    @Override
    public int getItemCount() {
        return ListDatos.size();
    }

    public  void  setOnClickListener(View.OnClickListener listener)
    {
        this.Listener=listener;
    }

    @Override
    public void onClick(View v) {
        if(Listener!=null)
        {
            Listener.onClick(v);
        }

    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        CardView Card;

        TextView Localidad;
        TextView Fecha;
        TextView Promised;



        LinearLayout ColorTarjeta;
        LinearLayout ColorTarjetaCabecera;



        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            Localidad=itemView.findViewById(R.id.Localidad);
            Fecha=itemView.findViewById(R.id.Fecha);
            Promised=itemView.findViewById(R.id.Promised);


            Card=itemView.findViewById(R.id.card);

            //los colores de tipo
            ColorTarjetaCabecera=itemView.findViewById(R.id.ColorTarjetaCabecera);
            ColorTarjeta=itemView.findViewById(R.id.ColorTarjeta);




        }


    }
}
