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

public class AdapterSchedulleLitle extends RecyclerView.Adapter<AdapterSchedulleLitle.ViewHolderDatos> implements View.OnClickListener {


    //ArrayList<String> ListDatos;
    ArrayList<Elemento_Evento> ListDatos;
    private View.OnClickListener Listener;
    private Context context;

    public AdapterSchedulleLitle(ArrayList<Elemento_Evento> listDatos,Context context) {
        this.ListDatos = listDatos;
        this.context=context;
    }



    @NonNull
    @Override
    public AdapterSchedulleLitle.ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.element_event_schedulle_litle,parent,false);
        view.setOnClickListener(this);
        return new AdapterSchedulleLitle.ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSchedulleLitle.ViewHolderDatos holder, int position) {

        boolean Hoy=false;
        Calendar Calendario= Calendar.getInstance();
        String FechaDeHoy;
        int Mes=Calendario.get(Calendar.MONTH);
        Mes=Mes+1;

        FechaDeHoy=Calendario.get(Calendar.DAY_OF_MONTH)+"/"+Mes+"/"+Calendario.get(Calendar.YEAR);




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


        holder.Card.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition));

        holder.Localidad.setText(ListDatos.get(position).getLocalidad());
        holder.Fecha.setText(ListDatos.get(position).getFechaDeEvento());
        holder.Lugar.setText(ListDatos.get(position).getLugar());
        holder.Informacion.setText(ListDatos.get(position).getInformacion());


            //hora actual
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            //System.out.println("Hora actual: " + dateFormat.format(date));
            //Hora actual
            Hoy=true;


            SimpleDateFormat objSDF  = new SimpleDateFormat("HH:mm");

            Date HoraI = null;
            Date HoraF = null;
            Date HoraA = new Date();

            String HI = null,HF = null,HA = null;
            try {
                HoraI = objSDF.parse(ListDatos.get(position).getHoraInicio());
                HoraF = objSDF.parse(ListDatos.get(position).getHoraFinal());
                HoraA = objSDF.parse(String.valueOf(new Date()));


            } catch (ParseException e) {
                e.printStackTrace();
            }

            HI=objSDF.format(HoraI);
            HF=objSDF.format(HoraF);
            HA=objSDF.format(HoraA);

            try {
                HoraI = objSDF.parse( HI);
                HoraF = objSDF.parse( HF);
                HoraA = objSDF.parse( HA);


            } catch (ParseException e) {
                e.printStackTrace();
            }


            System.out.println("HI:" + HI);
            System.out.println("HF:" + HF);
            System.out.println("HA:" + HA);
            System.out.println("HF:" + objSDF.format(HoraF));
            System.out.println("HA:" + objSDF.format(HoraA));

            System.out.println("Date1:" + objSDF.format(HoraI));
            System.out.println("Date2:" + objSDF.format(HoraF));
            System.out.println("Date3:" + objSDF.format(HoraA));
            System.out.println("Date1-Date3: " + HoraI.compareTo(HoraA));
            System.out.println("Date2-Date3: " + HoraF.compareTo(HoraA));

            System.out.println(HoraF.toString()+" : "+HoraA.toString()+ " : "+HoraF.compareTo(HoraA));
            System.out.println("Date3-Date3: " + HoraA.compareTo(HoraA));


            holder.ColorTarjetaCabecera.setBackgroundColor(Color.parseColor("#D97904"));
            holder.ColorTarjeta.setBackgroundColor(Color.parseColor("#F29F05"));









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

        TextView Lugar;
        TextView Informacion;
        TextView EstadoDeEvento;


        LinearLayout ColorTarjeta;
        LinearLayout ColorTarjetaCabecera;



        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            Localidad=itemView.findViewById(R.id.Localidad);
            Fecha=itemView.findViewById(R.id.Fecha);

            Lugar=itemView.findViewById(R.id.Lugar);
            Informacion=itemView.findViewById(R.id.Informacion);


            Card=itemView.findViewById(R.id.card);


            //los colores de tipo
            ColorTarjetaCabecera=itemView.findViewById(R.id.ColorTarjetaCabecera);
            ColorTarjeta=itemView.findViewById(R.id.ColorTarjeta);




        }


    }
}
