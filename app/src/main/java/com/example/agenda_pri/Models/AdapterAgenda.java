package com.example.agenda_pri.Models;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class AdapterAgenda  extends RecyclerView.Adapter<AdapterAgenda.ViewHolderDatos> implements View.OnClickListener {


    //ArrayList<String> ListDatos;
    ArrayList<Elemento_Evento> ListDatos;
    private View.OnClickListener Listener;
    private  Context context;

    public AdapterAgenda(ArrayList<Elemento_Evento> listDatos,Context context) {
        this.ListDatos = listDatos;
        this.context=context;
    }



    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_evento_agenda,parent,false);
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
        holder.HoraInicio.setText(f2.format(dI).toLowerCase());
        holder.HoraFinal.setText(f2.format(dF).toLowerCase());


        if(!ListDatos.get(position).getLugar().equals(""))
        {
            holder.Lugar.setText(ListDatos.get(position).getLugar());
        }
        else
        {
            holder.Lugar.setText("n/a");
        }


        if(!ListDatos.get(position).getInformacion().equals(""))
        {
            holder.Informacion.setText(ListDatos.get(position).getInformacion());
        }
        else
        {
            holder.Informacion.setText("n/a");
        }


        if(!ListDatos.get(position).getResponsable().equals(""))
        {
            holder.Responsable.setText(ListDatos.get(position).getResponsable());
        }
        else
        {
            holder.Responsable.setText("n/a");
        }


        holder.TipoDeEvento = (ListDatos.get(position).getTipoDeEvento());

        holder.FechaLarga.setText (ListDatos.get(position).getFechaDeEventoLarga());


        if(ListDatos.get(position).getFechaDeEvento().equals(FechaDeHoy))
        {

            //hora actual
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            //System.out.println("Hora actual: " + dateFormat.format(date));
            //Hora actual
            Hoy=true;
            holder.EstadoDeEvento.setText("Hoy");

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

            if(HoraI.compareTo(HoraA)==-1&&HoraF.compareTo(HoraA)==1)
            {
                holder.EstadoDeEvento.setText("En curso");
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







            switch (holder.TipoDeEvento)
            {

                case "Publico":

                    holder.ColorTarjetaCabecera.setBackgroundResource(R.color.ColorPublic);
                    holder.ColorTarjeta.setBackgroundResource(R.color.ColorPublicComplement);
                    break;

                case "Privado":
                    holder.ColorTarjetaCabecera.setBackgroundResource(R.color.ColorPrivate);
                    holder.ColorTarjeta.setBackgroundResource(R.color.ColorPrivateComplement);
                    break;

                case "Tentador":
                    holder.ColorTarjetaCabecera.setBackgroundResource(R.color.ColorTentador);
                    holder.ColorTarjeta.setBackgroundResource(R.color.ColorTentadorComplement);
                    break;
            }


        }else
        {

            switch (holder.TipoDeEvento)
            {

                case "Publico":

                    holder.ColorTarjetaCabecera.setBackgroundResource(R.color.ColorPublic2);
                    holder.ColorTarjeta.setBackgroundResource(R.color.ColorPublicComplement2);
                    break;

                case "Privado":
                    holder.ColorTarjetaCabecera.setBackgroundResource(R.color.ColorPrivate2);
                    holder.ColorTarjeta.setBackgroundResource(R.color.ColorPrivateComplement2);
                    break;

                case "Tentador":
                    holder.ColorTarjetaCabecera.setBackgroundResource(R.color.ColorTentador2);
                    holder.ColorTarjeta.setBackgroundResource(R.color.ColorTentadorComplement2);
                    break;
            }

        }


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
        TextView FechaLarga;
        TextView HoraInicio;
        TextView HoraFinal;
        TextView Lugar;
        TextView Informacion;
        TextView Responsable;
        TextView EstadoDeEvento;
        String  TipoDeEvento;

        LinearLayout ColorTarjeta;
        LinearLayout ColorTarjetaCabecera;



        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            Localidad=itemView.findViewById(R.id.Localidad);
            Fecha=itemView.findViewById(R.id.Fecha);
            FechaLarga=itemView.findViewById(R.id.FechaLarga);
            HoraInicio=itemView.findViewById(R.id.HoraInicio);
            HoraFinal=itemView.findViewById(R.id.HoraFinal);
            Lugar=itemView.findViewById(R.id.Lugar);
            Informacion=itemView.findViewById(R.id.Informacion);
            Responsable=itemView.findViewById(R.id.Responsable);
            EstadoDeEvento=itemView.findViewById(R.id.EstadoDeEvento);

            Card=itemView.findViewById(R.id.card);


            //los colores de tipo
            ColorTarjetaCabecera=itemView.findViewById(R.id.ColorTarjetaCabecera);
            ColorTarjeta=itemView.findViewById(R.id.ColorTarjeta);




        }


    }
}
