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

public class AdapterPropuestos extends RecyclerView.Adapter<AdapterPropuestos.ViewHolderDatos> implements View.OnClickListener {


    //ArrayList<String> ListDatos;
    ArrayList<Elemento_Evento> ListDatos;
    private View.OnClickListener Listener;
    private Context context;

    public AdapterPropuestos(ArrayList<Elemento_Evento> listDatos,Context context) {
        this.ListDatos = listDatos;
        this.context=context;
    }



    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_evento_propuesto_agenda,parent,false);
        view.setOnClickListener(this);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {

        boolean Hoy=false;
        Calendar Calendario= Calendar.getInstance();
        String FechaDeHoy;
        int Mes=Calendario.get(Calendar.MONTH);
        Mes=Mes+1;

        FechaDeHoy=Calendario.get(Calendar.DAY_OF_MONTH)+"/"+Mes+"/"+Calendario.get(Calendar.YEAR);



        holder.Card.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition));
        Log.d(TAG, "------------------------"+ListDatos.get(position).getFechaDeEvento()+"..."+FechaDeHoy);


        DateFormat f1 = new SimpleDateFormat("HH:mm");
        Date dI = null;
        Date dF = null;
        try {
            dI = f1.parse(ListDatos.get(position).getHoraInicio());
            dF = f1.parse(ListDatos.get(position).getHoraFinal());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat f2 = new SimpleDateFormat("hh:mma");


        holder.Localidad.setText(ListDatos.get(position).getLocalidad());
        holder.Fecha.setText(ListDatos.get(position).getFechaDeEvento());
        holder.HoraInicio.setText(f2.format(dI).toLowerCase());
        holder.HoraFinal.setText(f2.format(dF).toLowerCase());
        holder.Lugar.setText(ListDatos.get(position).getLugar());
        holder.Informacion.setText(ListDatos.get(position).getInformacion());
        holder.Responsable.setText(ListDatos.get(position).getResponsable());
        holder.TipoDeEvento.setText(ListDatos.get(position).getTipoDeEvento());
        holder.FechaDeCalendarizacion.setText(ListDatos.get(position).getFechaDeCalendarizacion());
        holder.QuienCalendarizo.setText(ListDatos.get(position).getQuienCalendarizo());
        holder.Vestimenta.setText(ListDatos.get(position).getVestimenta());



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
        TextView HoraInicio;
        TextView HoraFinal;
        TextView Lugar;
        TextView Informacion;
        TextView Responsable;
        TextView EstadoDeEvento;
        TextView TipoDeEvento;
        TextView FechaDeCalendarizacion;
        TextView QuienCalendarizo;
        TextView Vestimenta;



        LinearLayout ColorTarjeta;
        LinearLayout ColorTarjetaCabecera;



        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            Localidad=itemView.findViewById(R.id.Localidad);
            Fecha=itemView.findViewById(R.id.Fecha);
            HoraInicio=itemView.findViewById(R.id.HoraInicio);
            HoraFinal=itemView.findViewById(R.id.HoraFinal);
            Lugar=itemView.findViewById(R.id.Lugar);
            Informacion=itemView.findViewById(R.id.Informacion);
            Responsable=itemView.findViewById(R.id.Responsable);
            EstadoDeEvento=itemView.findViewById(R.id.EstadoDeEvento);
            TipoDeEvento=itemView.findViewById(R.id.TipoDeEvento);
            FechaDeCalendarizacion=itemView.findViewById(R.id.FechaDeCalendarizacion);
            QuienCalendarizo=itemView.findViewById(R.id.QuienCalendarizo);
            Vestimenta=itemView.findViewById(R.id.Vestimenta);

            Card=itemView.findViewById(R.id.card);


            //los colores de tipo
            ColorTarjetaCabecera=itemView.findViewById(R.id.ColorTarjetaCabecera);
            ColorTarjeta=itemView.findViewById(R.id.ColorTarjeta);




        }


    }
}
