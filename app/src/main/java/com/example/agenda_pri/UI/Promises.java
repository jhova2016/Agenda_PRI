package com.example.agenda_pri.UI;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.agenda_pri.Models.AdapterAgenda;
import com.example.agenda_pri.Models.AdapterPromises;
import com.example.agenda_pri.Models.AdapterSchedulleLitle;
import com.example.agenda_pri.Models.Element_Promise;
import com.example.agenda_pri.Models.Elemento_Evento;
import com.example.agenda_pri.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Promises#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Promises extends Fragment {

    LinearLayout LayoutProgressBar;

    RecyclerView listaPromises;
    AdapterPromises adapterPromises;
    ArrayList<Element_Promise> promised;


    AlertDialog DialogEliminarEvento;
    AlertDialog DialogEditarEventoFuncion;
    AlertDialog DialogAceptarEditar;
    AlertDialog DialogNewPromised;
    AlertDialog DialogEditPromised;
    AlertDialog DialogEliminarPromesa;

    AlertDialog DialogNewPromisedSelection;

    String TipoDeEventoEditarEvento;

    SharedPreferences sharedPreferences;

    FloatingActionButton BotonFlotante;

    //begin val of dialognewevent
    FirebaseAuth mAuth;

    RecyclerView RecyclerLastEvents;
    AdapterSchedulleLitle AdapterLastSchedulle;
    ArrayList<Elemento_Evento> ListLastEvents = new ArrayList<Elemento_Evento>();

    Button BtnCancelNewPromised;

    Spinner SpinnerTipoDeEvento;
    //end val of dialognewevent

    TextView NoSeEncontraron;

    FirebaseFirestore db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Promises() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Principal.
     */
    // TODO: Rename and change types and number of parameters
    public static Principal newInstance(String param1, String param2) {
        Principal fragment = new Principal();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      db = FirebaseFirestore.getInstance();
        sharedPreferences = getContext().getSharedPreferences("ADMINISTRADORES", MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_promises, container, false);
        LayoutProgressBar=view.findViewById(R.id.LayoutProgressBar);
        LayoutProgressBar.setVisibility(View.VISIBLE);

        NoSeEncontraron=view.findViewById(R.id.NoSeEncontraron);

        listaPromises = view.findViewById(R.id.recycler);
        listaPromises.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        promised = new ArrayList<>();
        adapterPromises = new AdapterPromises(promised,getContext());
        BotonFlotante=view.findViewById(R.id.FloatingCreateEvent);



        sharedPreferences = getContext().getSharedPreferences("ADMINISTRADORES", MODE_PRIVATE);


        BotonFlotante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*NewPromised();
                DialogNewPromised.show();*/
                DialogAddNewPromised();
                DialogNewPromisedSelection.show();


            }
        });
        String Aux=sharedPreferences.getString("SchedulleUser", mAuth.getCurrentUser().getEmail());


        DocumentReference docRef =db.collection("Users").document(Aux).collection("Actualizar").document("BanderaNuevaPromesa");

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                RellenarRecycler();

            }
        });



        LayoutProgressBar.setVisibility(View.INVISIBLE);
        return view;
    }

    public void RellenarRecycler()
    {
        promised.clear();
        adapterPromises.notifyDataSetChanged();
        listaPromises.setAdapter(adapterPromises);

        String Aux=sharedPreferences.getString("SchedulleUser", mAuth.getCurrentUser().getEmail());
        db.collection("Users").document(Aux).collection("Promises")
        //db.collection("Promises")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());


                                promised.add(new Element_Promise(
                                        document.getId(),
                                        document.get("Location").toString(),
                                        document.get("EventDate").toString(),
                                        document.get("Place").toString(),
                                        document.get("Information").toString(),
                                        document.get("Promised").toString(),
                                        document.get("PromisedEstate").toString()
                                ));
                                adapterPromises.notifyDataSetChanged();





                            }

                            if(promised.size()==0)
                            {
                                NoSeEncontraron.setVisibility(View.VISIBLE);
                            }




                            SimpleDateFormat objSDF  = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            for(int i = 0; i < promised.size() - 1; i++)
                            {

                                for(int j = 0; j < promised.size()  - 1; j++)
                                {
                                    Date dt_1 = null;
                                    Date dt_2 = null;
                                    try {

                                        dt_1 = objSDF.parse(promised.get(j).getEventDate()+" "+promised.get(j).getEventDate());
                                        dt_2 = objSDF.parse(promised.get(j+1).getEventDate()+" "+promised.get(j+1).getEventDate());

                                        System.out.println("Date1:" + objSDF.format(dt_1));
                                        System.out.println("Date2:" + objSDF.format(dt_2));

                                        if (dt_1.compareTo(dt_2)==1) {

                                            Element_Promise tmp=promised .get(j+1);
                                            promised.set(j+1,promised.get(j));
                                            promised.set(j,tmp);


                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }
                            adapterPromises.notifyDataSetChanged();

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });




        adapterPromises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EditPromised(promised.get(listaPromises.getChildAdapterPosition(v)).getId(),
                        promised.get(listaPromises.getChildAdapterPosition(v)).getLocation(),
                        promised.get(listaPromises.getChildAdapterPosition(v)).getPlace(),
                        promised.get(listaPromises.getChildAdapterPosition(v)).getInformation(),
                        promised.get(listaPromises.getChildAdapterPosition(v)).getEventDate(),
                        promised.get(listaPromises.getChildAdapterPosition(v)).getPromised()

                );
                DialogEditPromised.show();


            }
        });

        adapterPromises.notifyDataSetChanged();
        listaPromises.setAdapter(adapterPromises);
    }


    public void EditPromised(final String id,final String location, final String place, final String information, final String eventDate,final String promised)
    {

        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(getContext()).setCancelable(false);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_promised,null);

        final String ID=id;

        TextView Location=view.findViewById(R.id.Localidad);
        TextView Place=view.findViewById(R.id.Lugar);
        TextView EventDate=view.findViewById(R.id.Fecha);
        final EditText Promised=view.findViewById(R.id.EditPromised);
        Button Save=view.findViewById(R.id.Save);;
        Button Close=view.findViewById(R.id.Close);;
        ImageView Eliminar=view.findViewById(R.id.eliminarpromesa);;

        Location.setText(location);
        Place.setText(place);
        EventDate.setText(eventDate);
        Promised.setText(promised);

        Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogEliminarPromesa(ID);
                DialogEliminarPromesa.show();

            }
        });


        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Aux=sharedPreferences.getString("SchedulleUser", mAuth.getCurrentUser().getEmail());

                DocumentReference washingtonRef = db.collection("Users").document(Aux).collection("Promises").document(ID);

                washingtonRef
                        .update("Promised",  Promised.getText().toString()

                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(getContext(), "Promesa Editada", Toast.LENGTH_LONG).show();

                        DialogEditPromised.dismiss();
                    }
                });

            }
        });


        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogEditPromised.dismiss();
            }
        });


        builder.setView(view);
        DialogEditPromised=builder.create();
        DialogEditPromised.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }





    public void DialogAddNewPromised()
    {
        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(getContext()).setCancelable(false);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_new_promised,null);

        final TextView Location=view.findViewById(R.id.Location);
        final EditText Place=view.findViewById(R.id.Place);
        final EditText Information=view.findViewById(R.id.Information);
        final EditText EventDate=view.findViewById(R.id.Date);
        final EditText EditTextPromised=view.findViewById(R.id.editTextPromised);
        Button   BtnCancelNewPromised=view.findViewById(R.id.BtnCancelDialogNewPromised);
        Button   BtnAcceptNewPromised=view.findViewById(R.id.BtnAcceptDialogPromised);




        BtnAcceptNewPromised.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Map<String, Object> Promised = new HashMap<>();
                Promised.put("Location",Location.getText().toString());
                Promised.put("Place",Place.getText().toString());
                Promised.put("Information",Information.getText().toString());
                Promised.put("EventDate",EventDate.getText().toString());
                Promised.put("Promised",EditTextPromised.getText().toString());
                Promised.put("PromisedEstate","Promise");



                String Aux=sharedPreferences.getString("SchedulleUser", mAuth.getCurrentUser().getEmail());

                db.collection("Users").document(Aux).collection("Promises")
                        .add(Promised)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                                DialogNewPromisedSelection.dismiss();
                                Toast.makeText(getContext(), "Nueva promesa agregada", Toast.LENGTH_LONG).show();


                                //

                                SimpleDateFormat objSDFQuitar  = new SimpleDateFormat("HH:mm:ss");
                                Date date = new Date();

                                Map<String, Object> Hora = new HashMap<>();
                                Hora.put("UltimaActualizacion",  objSDFQuitar .format(date));


                                String Aux=sharedPreferences.getString("SchedulleUser", mAuth.getCurrentUser().getEmail());

                            db.collection("Users").document(Aux).collection("Actualizar").document("BanderaNuevaPromesa")
                                        .set(Hora)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error writing document", e);
                                            }
                                        });


                                //



                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });





            }
        });

        BtnCancelNewPromised.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogNewPromisedSelection.dismiss();

            }
        });



        builder.setView(view);
        DialogNewPromisedSelection=builder.create();
        DialogNewPromisedSelection.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }


    private void DialogEliminarPromesa(final String EntId)
    {

        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_aceptar_cancelar,null);

        TextView Titulo =view.findViewById(R.id.TituloAC);
        TextView Descripcion =view.findViewById(R.id.DescripcionAC);
        Button BtnAceptar=view.findViewById(R.id.BtnAceptarDialogEliminarEvento);
        Button BtnCancelar=view.findViewById(R.id.BtnCancelarDialogEliminarEvento);

        Titulo.setText("Desea borrar Promesa?");
        Descripcion.setText("Esta accion no se puede deshacer");

        BtnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Aux=sharedPreferences.getString("SchedulleUser", mAuth.getCurrentUser().getEmail());

                DocumentReference docRef = db.collection("Users").document(Aux).collection("Promises").document(EntId);
                docRef.delete();

                SimpleDateFormat objSDFQuitar  = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();

                Map<String, Object> Hora = new HashMap<>();
                Hora.put("UltimaActualizacion",  objSDFQuitar .format(date));

                db.collection("Users").document(Aux).collection("Actualizar").document("BanderaNuevaPromesa")

               // db.collection("Actualizar").document("BanderaNuevaPromesa")
                        .set(Hora)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                DialogEliminarPromesa.dismiss();
                                DialogEditPromised.dismiss();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });



            }
        });

        BtnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogEliminarPromesa.dismiss();

            }
        });



        builder.setView(view);
        DialogEliminarPromesa=builder.create();
        DialogEliminarPromesa.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }












}