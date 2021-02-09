package com.example.agenda_pri.Models;

import android.content.Context;
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

import java.util.ArrayList;

public class AdapterMail extends RecyclerView.Adapter<AdapterMail.ViewHolderDatos> implements View.OnClickListener {


        //ArrayList<String> ListDatos;
        ArrayList<String> ListDatos;
private View.OnClickListener Listener;
private Context context;

public AdapterMail(ArrayList<String> listDatos,Context context) {
        this.ListDatos = listDatos;
        this.context=context;
        }



@NonNull
@Override
public AdapterMail.ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.element_mail,parent,false);
        view.setOnClickListener(this);
        return new AdapterMail.ViewHolderDatos(view);
        }

@Override
public void onBindViewHolder(@NonNull AdapterMail.ViewHolderDatos holder, int position) {

        holder.Mail.setText(ListDatos.get(position));


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


    TextView Mail;


    public ViewHolderDatos(@NonNull View itemView) {
        super(itemView);
        Mail=itemView.findViewById(R.id.Mail);
    }


}
}
