package com.example.agenda_pri.Models;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda_pri.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterPeticiones extends RecyclerView.Adapter<AdapterPeticiones.ViewHolderDatos> implements View.OnClickListener {


        //ArrayList<String> ListDatos;
        ArrayList<Element_Peticion> ListDatos;
private View.OnClickListener Listener;
private Context context;

        FirebaseAuth mAuth;

public AdapterPeticiones(ArrayList<Element_Peticion> listDatos,Context context) {
        this.ListDatos = listDatos;
        this.context=context;
        }



@NonNull
@Override
public AdapterPeticiones.ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.element_peticion,parent,false);
        view.setOnClickListener(this);
        return new AdapterPeticiones.ViewHolderDatos(view);
        }

@Override
public void onBindViewHolder(@NonNull AdapterPeticiones.ViewHolderDatos holder, final int position) {

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        holder.Mail.setText(ListDatos.get(position).getCorreo());
        holder.Aceptado.setChecked(Boolean.parseBoolean(ListDatos.get(position).getAceptado()));
        holder.Admin.setChecked(Boolean.parseBoolean(ListDatos.get(position).getAdmin()));
        mAuth = FirebaseAuth.getInstance();



        holder.Aceptado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        final Map<String, Object> Peticion = new HashMap<>();
                        Peticion.put("Correo", ListDatos.get(position).getCorreo());
                        Peticion.put("Admin",Boolean.parseBoolean(ListDatos.get(position).getAdmin()) );
                        if (isChecked) {
                                // The toggle is enabled
                                Peticion.put("Aceptado", true);
                                db.collection("Users").document(mAuth.getCurrentUser().getEmail()).collection("Acceso").document(ListDatos.get(position).getCorreo()).set(Peticion);

                        } else {
                                // The toggle is disabled
                                Peticion.put("Aceptado", false);
                                db.collection("Users").document(mAuth.getCurrentUser().getEmail()).collection("Acceso").document(ListDatos.get(position).getCorreo()).set(Peticion);


                        }
                }
        });


        holder.Admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        final Map<String, Object> Peticion = new HashMap<>();
                        Peticion.put("Correo", ListDatos.get(position).getCorreo());
                        Peticion.put("Aceptado", Boolean.parseBoolean(ListDatos.get(position).getAceptado()));
                        if (isChecked) {
                                // The toggle is enabled
                                Peticion.put("Admin", true);
                                db.collection("Users").document(mAuth.getCurrentUser().getEmail()).collection("Acceso").document(ListDatos.get(position).getCorreo()).set(Peticion);

                        } else {
                                // The toggle is disabled
                                Peticion.put("Admin", false);
                                db.collection("Users").document(mAuth.getCurrentUser().getEmail()).collection("Acceso").document(ListDatos.get(position).getCorreo()).set(Peticion);


                        }
                }
        });








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
    Switch Aceptado;
    Switch Admin;


    public ViewHolderDatos(@NonNull View itemView) {
        super(itemView);
        Mail=itemView.findViewById(R.id.Mail);
            Aceptado=itemView.findViewById(R.id.switchAceptado);
            Admin=itemView.findViewById(R.id.switchAdmin);
    }


}
}
