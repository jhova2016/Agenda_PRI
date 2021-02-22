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

import com.example.agenda_pri.Models.AdapterPropuestos;
import com.example.agenda_pri.Models.Elemento_Evento;
import com.example.agenda_pri.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
 * Use the {@link Propuestos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Propuestos extends Fragment {

    LinearLayout LayoutProgressBar;

    RecyclerView listaEventos;
    AdapterPropuestos adapterAgenda;
    ArrayList<Elemento_Evento> Evento;

    AlertDialog DialogEvento;
    AlertDialog DialogEliminarEvento;
    AlertDialog DialogEditarEventoFuncion;
    AlertDialog DialogAceptarEditar;
    AlertDialog DialogNewEvent;

    String TipoDeEventoEditarEvento;

    SharedPreferences sharedPreferences;

    FloatingActionButton BotonFlotante;

    //begin val of dialognewevent


    FirebaseAuth mAuth;
    //



   /* en la oficina
    mañna
    carrizal basmba moroo
    alkas 12


    a las 4
    crucero hacia la 20 derecha arbol de guaya
*/
    String TipoDeEvento;
    EditText Localidad;
    EditText Lugar;
    EditText Informacion;
    TextView FechaDeEvento;
    TextView HoraInicio;
    TextView HoraFinal;
    EditText Responsable;
    EditText Vestimenta;

    String EstadoDeEvento;
    String CoordenadasActuales;
    String CoordenadasEvento;
    String FechaDeCalendarizacion;
    String QuienCalendarizo;

    Button BtnGuardadEvento;
    Button BtnCancelarGuardadEvento;


    Spinner SpinnerTipoDeEvento;

    TextView NoSeEncontraron;



    //end val of dialognewevent

    FirebaseFirestore db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Propuestos() {
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
        mAuth = FirebaseAuth.getInstance();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    private void DialogAceptarEditarEvento(final String ID,
                                           final String EntTipoDeEvento,
                                           final String EntLocalidad,
                                           final String EntLugar,
                                           final String EntInformacion,
                                           final String EntFechaDeEvento,
                                           final String EntHoraInicio,
                                           final String EntHoraFinal,
                                           final String EntResponsable,
                                           final String EntVestimenta
    )
    {

        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_aceptar_cancelar,null);

        TextView Titulo =view.findViewById(R.id.TituloAC);
        TextView Descripcion =view.findViewById(R.id.DescripcionAC);
        Button BtnAceptar=view.findViewById(R.id.BtnAceptarDialogEliminarEvento);
        Button BtnCancelar=view.findViewById(R.id.BtnCancelarDialogEliminarEvento);

        Titulo.setText("Desea editar este evento?");
        Descripcion.setText("Esta accion no se puede deshacer");


        BtnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String Aux=sharedPreferences.getString("SchedulleUser", mAuth.getCurrentUser().getEmail());
                DocumentReference washingtonRef = db.collection("Users").document(Aux).collection("Eventos").document(ID);


                washingtonRef
                        .update("TipoDeEvento",  TipoDeEventoEditarEvento,
                                "Localidad",     EntLocalidad,
                                "Lugar",         EntLugar,
                                "Informacion",   EntInformacion,
                                "FechaDeEvento", EntFechaDeEvento,
                                "HoraInicio",    EntHoraInicio,
                                "HoraFinal",     EntHoraFinal,
                                "Responsable",   EntResponsable,
                                "Vestimenta",    EntVestimenta
                        )
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");


                                SimpleDateFormat objSDFQuitar  = new SimpleDateFormat("HH:mm:ss");
                                Date date = new Date();

                                Map<String, Object> Hora = new HashMap<>();
                                Hora.put("UltimaActualizacion",  objSDFQuitar .format(date));



                                db.collection("Users").document(Aux).collection("Actualizar").document("Bandera")
                                //db.collection("Actualizar").document("Bandera")
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



                                Toast.makeText(getContext(), "Evento Editado", Toast.LENGTH_LONG).show();


                                DialogAceptarEditar.dismiss();
                                DialogEditarEventoFuncion.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });


            }
        });

        BtnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogAceptarEditar.dismiss();

            }
        });



        builder.setView(view);
        DialogAceptarEditar=builder.create();
        DialogAceptarEditar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    private void DialogEliminarEvento(final String EntId)
    {

        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_aceptar_cancelar,null);

        TextView Titulo =view.findViewById(R.id.TituloAC);
        TextView Descripcion =view.findViewById(R.id.DescripcionAC);
        Button BtnAceptar=view.findViewById(R.id.BtnAceptarDialogEliminarEvento);
        Button BtnCancelar=view.findViewById(R.id.BtnCancelarDialogEliminarEvento);

        Titulo.setText("Desea borrar evento?");
        Descripcion.setText("Esta accion no se puede deshacer");

        BtnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String Aux=sharedPreferences.getString("SchedulleUser", mAuth.getCurrentUser().getEmail());


                DocumentReference docRef =db.collection("Users").document(Aux).collection("Eventos").document(EntId);
               // DocumentReference docRef = db.collection("Eventos").document(EntId);
                docRef.delete();

                SimpleDateFormat objSDFQuitar  = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();

                Map<String, Object> Hora = new HashMap<>();
                Hora.put("UltimaActualizacion",  objSDFQuitar .format(date));

                db.collection("Users").document(Aux).collection("Actualizar").document("Bandera")
                        .set(Hora)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                DialogEliminarEvento.dismiss();
                                DialogEvento.dismiss();
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
                DialogEliminarEvento.dismiss();

            }
        });



        builder.setView(view);
        DialogEliminarEvento=builder.create();
        DialogEliminarEvento.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }


    private void DialogAceptarEventoFuncion(final String ID,
                                            final String EntLocalidad,
                                            final String EntLugar,
                                            final String EntInformacion,
                                            final String EntFechaDeEvento,
                                            final String EntTipoDeEvento,
                                            final String EntHoraInicio,
                                            final String EntHoraFinal,
                                            final String EntResponsable,
                                            final String EntVestimenta,
                                            final String EntEstadoDeEvento,
                                            final String EntFechaDeCalendarizacion,
                                            final String EntQuienCalendarizo

    )
    {
        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_aceptar_evento,null);


        ImageView    BtnElminarEvento=view.findViewById(R.id.ImgBorrar);
        ImageView    BtnEditarEvento=view.findViewById(R.id.ImgEditar);

        DateFormat f1 = new SimpleDateFormat("HH:mm");
        Date dI = null;
        Date dF = null;
        try {
            dI = f1.parse(EntHoraInicio);
            dF = f1.parse(EntHoraFinal);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat f2 = new SimpleDateFormat("hh:mm a");


        Button BtnDialog=view.findViewById(R.id.BtnAceptarDialogAceptarEvento);
        Button BtnCancelarDialog=view.findViewById(R.id.BtnCancelarDialogAceptarEvento);



        builder.setView(view);
        DialogEvento=builder.create();
        DialogEvento.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        BtnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Aux=sharedPreferences.getString("SchedulleUser", mAuth.getCurrentUser().getEmail());

                DocumentReference washingtonRef = db.collection("Users").document(Aux).collection("Eventos").document(ID);

                washingtonRef
                        .update("EstadoDeEvento",  "Futuro"
                        )
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");


                                SimpleDateFormat objSDFQuitar  = new SimpleDateFormat("HH:mm:ss");
                                Date date = new Date();

                                Map<String, Object> Hora = new HashMap<>();
                                Hora.put("UltimaActualizacion",  objSDFQuitar .format(date));

                                db.collection("Users").document(Aux).collection("Actualizar").document("Bandera")
                                //db.collection("Actualizar").document("Bandera")
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



                                Toast.makeText(getContext(), "Evento aceptado", Toast.LENGTH_LONG).show();

                                DialogEvento.dismiss();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });

                DialogEvento.dismiss();
            }
        });

        BtnCancelarDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogEvento.dismiss();
            }
        });

        BtnElminarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogEliminarEvento(ID);
                DialogEliminarEvento.show();

            }
        });

        BtnEditarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogEditarEvento(
                        ID,
                        EntLocalidad,
                        EntLugar,
                        EntInformacion,
                        EntFechaDeEvento,
                        EntTipoDeEvento,
                        EntHoraInicio,
                        EntHoraFinal,
                        EntResponsable,
                        EntVestimenta,
                        EntEstadoDeEvento,
                        EntFechaDeCalendarizacion,
                        EntQuienCalendarizo
                );
                DialogEvento.dismiss();
                DialogEditarEventoFuncion.show();


            }
        });




    }


    public void DialogEditarEvento(final String ID,
                                   String EntLocalidad,
                                   String EntLugar,
                                   String EntInformacion,
                                   String EntFechaDeEvento,
                                   String EntTipoDeEvento,
                                   String EntHoraInicio,
                                   String EntHoraFinal,
                                   String EntResponsable,
                                   String EntVestimenta,
                                   String EntEstadoDeEvento,
                                   String EntFechaDeCalendarizacion,
                                   String EntQuienCalendarizo)
    {

        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(getContext()).setCancelable(false);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_editar_evento,null);

        Button BtnAceptarEditarEventoDialog;
        Button BtnCancelarEditarEventoDialog;

        final String TipoDeEvento=TipoDeEventoEditarEvento;
        final EditText Localidad;
        final EditText Lugar;
        final EditText Informacion;
        final TextView FechaDeEvento;
        final TextView HoraInicio;
        final TextView HoraFinal;
        final EditText Responsable;
        final EditText Vestimenta;

        Localidad       =view.findViewById(R.id.editLocalidad);
        Lugar           =view.findViewById(R.id.editLugar);
        Informacion     =view.findViewById(R.id.editInformacion);
        FechaDeEvento   =view.findViewById(R.id.editFechaDeEvento);
        HoraInicio      =view.findViewById(R.id.editHoraInicio);
        HoraFinal       =view.findViewById(R.id.editHoraFinal);
        Responsable     =view.findViewById(R.id.editResponsable);
        Vestimenta      =view.findViewById(R.id.editVestimenta);

        final ArrayList<String> listSpinner =new ArrayList<String>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.controlspinner, listSpinner);

        Spinner SpinnerTipoDeEvento;
        SpinnerTipoDeEvento=view.findViewById(R.id.SpinnerTipoDeEvento);
        listSpinner.add("Publico");
        listSpinner.add("Privado");
        listSpinner.add("Tentador");
        adapter.notifyDataSetChanged();
        SpinnerTipoDeEvento.setAdapter(adapter);

        SpinnerTipoDeEvento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TipoDeEventoEditarEvento=listSpinner.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        FechaDeEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar Calendario= Calendar.getInstance();
                int Dia=Calendario.get(Calendar.DAY_OF_MONTH);
                int Mes=Calendario.get(Calendar.MONTH);
                int Año=Calendario.get(Calendar.YEAR);

                DatePickerDialog CalendarioDialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month=month+1;
                        FechaDeEvento.setText(dayOfMonth+"/"+month+"/"+year);

                    }
                },Año,Mes,Dia);
                CalendarioDialog.show();
            }
        });


        HoraInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog HoraDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Date dt_1 = null;
                        SimpleDateFormat objSDF = new SimpleDateFormat("HH:mm");
                        try {
                            dt_1 = objSDF.parse(hourOfDay+":"+minute);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        HoraInicio.setText( objSDF.format(dt_1));
                    }

                },12,00,false);
                HoraDialog.show();

            }
        });

        HoraFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog HoraDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Date dt_1 = null;
                        SimpleDateFormat objSDF = new SimpleDateFormat("HH:mm");
                        try {
                            dt_1 = objSDF.parse(hourOfDay+":"+minute);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        HoraFinal.setText( objSDF.format(dt_1));
                    }

                },12,00,false);
                HoraDialog.show();


            }
        });



        BtnAceptarEditarEventoDialog =view.findViewById(R.id.BtnGuardarEvento);
        BtnCancelarEditarEventoDialog =view.findViewById(R.id.BtnCancelarGuardarEvento);



        Localidad.setText(EntLocalidad);
        Lugar.setText(EntLugar);
        Informacion.setText(EntInformacion);
        FechaDeEvento.setText(EntFechaDeEvento);
        HoraInicio.setText(EntHoraInicio);
        HoraFinal.setText(EntHoraFinal);
        Responsable.setText(EntResponsable);
        Vestimenta.setText(EntVestimenta);



        BtnAceptarEditarEventoDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAceptarEditarEvento(
                        ID,
                        TipoDeEventoEditarEvento,
                        Localidad.getText().toString(),
                        Lugar.getText().toString(),
                        Informacion.getText().toString(),
                        FechaDeEvento.getText().toString(),
                        HoraInicio.getText().toString(),
                        HoraFinal.getText().toString(),
                        Responsable.getText().toString(),
                        Vestimenta.getText().toString());

                DialogAceptarEditar.show();



            }
        });

        BtnCancelarEditarEventoDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogEditarEventoFuncion.dismiss();

            }
        });


        builder.setView(view);
        DialogEditarEventoFuncion=builder.create();
        DialogEditarEventoFuncion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_propuestos, container, false);
        LayoutProgressBar=view.findViewById(R.id.LayoutProgressBar);
        LayoutProgressBar.setVisibility(View.VISIBLE);

        listaEventos = view.findViewById(R.id.recycler);
        listaEventos.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        Evento = new ArrayList<Elemento_Evento>();
        adapterAgenda = new AdapterPropuestos(Evento,getContext());
        BotonFlotante=view.findViewById(R.id.FloatingCreateEvent);

        NoSeEncontraron=view.findViewById(R.id.NoSeEncontraron);



        sharedPreferences = getContext().getSharedPreferences("ADMINISTRADORES", MODE_PRIVATE);

        if(sharedPreferences.getInt("ADMIN", 1)==1)
        {
            BotonFlotante.setVisibility(View.VISIBLE);
        }

        BotonFlotante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NewEvent();
                DialogNewEvent.show();


            }
        });


        final String Aux=sharedPreferences.getString("SchedulleUser", mAuth.getCurrentUser().getEmail());

        DocumentReference docRef = db.collection("Users").document(Aux).collection("Actualizar").document("Bandera");

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                Evento.clear();
                adapterAgenda.notifyDataSetChanged();
                listaEventos.setAdapter(adapterAgenda);

                db.collection("Users").document(Aux).collection("Eventos")
                //db.collection("Eventos")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());



                                        SimpleDateFormat objSDFQuitar  = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                        if(document.get("EstadoDeEvento").toString().equals("Propuesto"))
                                        {

                                            try {

                                                Date dtEvento  = objSDFQuitar.parse( document.get("FechaDeEvento").toString()+" "+document.get("HoraFinal").toString());
                                                Date date = new Date();
                                                System.out.println("Hora y fecha actual: "+objSDFQuitar .format(date));
                                                if (dtEvento.compareTo(date)>0) {

                                                    Evento.add(new Elemento_Evento(
                                                            document.getId(),
                                                            document.get("TipoDeEvento").toString(),
                                                            document.get("Localidad").toString(),
                                                            document.get("Lugar").toString(),
                                                            document.get("Informacion").toString(),
                                                            document.get("FechaDeEvento").toString(),
                                                            "",
                                                            document.get("HoraInicio").toString(),
                                                            document.get("HoraFinal").toString(),
                                                            document.get("Responsable").toString(),
                                                            document.get("Vestimenta").toString(),
                                                            document.get("EstadoDeEvento").toString(),
                                                            document.get("CoordenadasActuales").toString(),
                                                            document.get("CoordenadasEvento").toString(),
                                                            document.get("FechaDeCalendarizacion").toString(),
                                                            document.get("QuienCalendarizo").toString() ));
                                                    adapterAgenda.notifyDataSetChanged();
                                                }
                                                else
                                                {
                                                    System.out.println("ya paso");
                                                }


                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }


                                        }


                                    }

                                    if(Evento.size()==0)
                                    {
                                        NoSeEncontraron.setVisibility(View.VISIBLE);
                                    }


                                    SimpleDateFormat objSDF  = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                    for(int i = 0; i < Evento.size() - 1; i++)
                                    {

                                        for(int j = 0; j < Evento.size()  - 1; j++)
                                        {
                                            Date dt_1 = null;
                                            Date dt_2 = null;
                                            try {

                                                dt_1 = objSDF.parse(Evento.get(j).getFechaDeEvento()+" "+Evento.get(j).getHoraInicio());
                                                dt_2 = objSDF.parse(Evento.get(j+1).getFechaDeEvento()+" "+Evento.get(j+1).getHoraInicio());
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            System.out.println("Date1:" + objSDF.format(dt_1));
                                            System.out.println("Date2:" + objSDF.format(dt_2));

                                            if (dt_1.compareTo(dt_2)==1) {

                                                Elemento_Evento tmp=Evento .get(j+1);
                                                Evento.set(j+1,Evento.get(j));
                                                Evento.set(j,tmp);


                                            }

                                           /* if (Integer.parseInt(Evento.get(j).getHoraInicio() ) < Integer.parseInt(Evento.get(j+1).getHoraInicio() ) )
                                            {

                                                Elemento_Evento tmp=Evento .get(j+1);
                                                Evento.set(j+1,Evento.get(j));
                                                Evento.set(j,tmp);


                                            }*/
                                            //adapterAgenda.notifyDataSetChanged();
                                        }
                                    }
                                    adapterAgenda.notifyDataSetChanged();

                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });


            }
        });


        adapterAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                sharedPreferences = getContext().getSharedPreferences("ADMINISTRADORES", MODE_PRIVATE);
                if(sharedPreferences.getInt("ADMIN", 1)==1)
                {
                     DialogAceptarEventoFuncion(
                        Evento.get(listaEventos.getChildAdapterPosition(v)).getId(),
                        Evento.get(listaEventos.getChildAdapterPosition(v)).getLocalidad(),
                        Evento.get(listaEventos.getChildAdapterPosition(v)).getLugar(),
                        Evento.get(listaEventos.getChildAdapterPosition(v)).getInformacion(),
                        Evento.get(listaEventos.getChildAdapterPosition(v)).getFechaDeEvento(),
                        Evento.get(listaEventos.getChildAdapterPosition(v)).getTipoDeEvento(),
                        Evento.get(listaEventos.getChildAdapterPosition(v)).getHoraInicio(),
                        Evento.get(listaEventos.getChildAdapterPosition(v)).getHoraFinal(),
                        Evento.get(listaEventos.getChildAdapterPosition(v)).getResponsable(),
                        Evento.get(listaEventos.getChildAdapterPosition(v)).getVestimenta(),
                        Evento.get(listaEventos.getChildAdapterPosition(v)).getEstadoDeEvento(),
                        Evento.get(listaEventos.getChildAdapterPosition(v)).getFechaDeCalendarizacion(),
                        Evento.get(listaEventos.getChildAdapterPosition(v)).getQuienCalendarizo()
                );
                DialogEvento.show();



                }


            }
        });

        adapterAgenda.notifyDataSetChanged();
        listaEventos.setAdapter(adapterAgenda);
        LayoutProgressBar.setVisibility(View.INVISIBLE);
        return view;
    }


    private void NewEvent()
    {
        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(getContext()).setCancelable(false);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_agregar_evento,null);

        final ArrayList<String> listSpinner =new ArrayList<String>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.controlspinner, listSpinner);


        Localidad       =view.findViewById(R.id.editLocalidad);
        Lugar           =view.findViewById(R.id.editLugar);
        Informacion     =view.findViewById(R.id.editInformacion);
        FechaDeEvento   =view.findViewById(R.id.editFechaDeEvento);
        HoraInicio      =view.findViewById(R.id.editHoraInicio);
        HoraFinal       =view.findViewById(R.id.editHoraFinal);
        Responsable     =view.findViewById(R.id.editResponsable);
        Vestimenta      =view.findViewById(R.id.editVestimenta);

        BtnGuardadEvento=view.findViewById(R.id.BtnGuardarEvento);

        BtnCancelarGuardadEvento=view.findViewById(R.id.BtnCancelarGuardarEvento);



        SpinnerTipoDeEvento=view.findViewById(R.id.SpinnerTipoDeEvento);

        listSpinner.add("Publico");
        listSpinner.add("Privado");
        listSpinner.add("Tentador");

        adapter.notifyDataSetChanged();
        SpinnerTipoDeEvento.setAdapter(adapter);


        SpinnerTipoDeEvento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TipoDeEvento=listSpinner.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        BtnGuardadEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String Aux=sharedPreferences.getString("SchedulleUser", mAuth.getCurrentUser().getEmail());


                db.collection("Users").document(Aux).collection("DiasBloqueados")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    Boolean FechaBloqueada =false;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(FechaDeEvento.getText().toString().equals(document.get("Fecha").toString()))
                                        {
                                            FechaBloqueada=true;
                                        }
                                    }

                                    if(FechaBloqueada==true)
                                    {
                                        Toast.makeText(getContext(), "Fecha bloqueada", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {

                                        if(HoraInicio.getText().toString().equals("")||HoraFinal.getText().toString().equals("")||FechaDeEvento.getText().toString().equals("")||Localidad.getText().toString().equals(""))
                                        {

                                            Toast.makeText(getContext(), "Los campos con * son obligatorios", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {

                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Calendar Calendario= Calendar.getInstance();

                                            EstadoDeEvento="Propuesto";
                                            CoordenadasActuales="0";
                                            CoordenadasEvento="0";

                                            int Mes=Calendario.get(Calendar.MONTH);
                                            Mes=Mes+1;
                                            FechaDeCalendarizacion=Calendario.get(Calendar.DAY_OF_MONTH)+"/"+Mes+"/"+Calendario.get(Calendar.YEAR);
                                            QuienCalendarizo=user.getEmail();;





                                            Map<String, Object> Evento = new HashMap<>();
                                            Evento.put("TipoDeEvento",  TipoDeEvento);
                                            Evento.put("Localidad",     Localidad.getText().toString());
                                            Evento.put("Lugar",         Lugar.getText().toString());
                                            Evento.put("Informacion",   Informacion.getText().toString());
                                            Evento.put("FechaDeEvento", FechaDeEvento.getText().toString());
                                            Evento.put("HoraInicio",    HoraInicio.getText().toString());
                                            Evento.put("HoraFinal",     HoraFinal.getText().toString());
                                            Evento.put("Responsable",   Responsable.getText().toString());
                                            Evento.put("Vestimenta",    Vestimenta.getText().toString());

                                            Evento.put("EstadoDeEvento",        EstadoDeEvento);
                                            Evento.put("CoordenadasActuales",   CoordenadasActuales);
                                            Evento.put("CoordenadasEvento",     CoordenadasEvento);
                                            Evento.put("FechaDeCalendarizacion",FechaDeCalendarizacion);
                                            Evento.put("QuienCalendarizo",      QuienCalendarizo);

                                            final String Aux=sharedPreferences.getString("SchedulleUser", mAuth.getCurrentUser().getEmail());

                                            db.collection("Users").document(Aux).collection("Eventos")
                                            //db.collection("Eventos")
                                                    .add(Evento)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());



                                                            SimpleDateFormat objSDFQuitar  = new SimpleDateFormat("HH:mm:ss");
                                                            Date date = new Date();

                                                            Map<String, Object> Hora = new HashMap<>();
                                                            Hora.put("UltimaActualizacion",  objSDFQuitar .format(date));

                                                            final String Aux=sharedPreferences.getString("SchedulleUser", mAuth.getCurrentUser().getEmail());

                                                            db.collection("Users").document(Aux).collection("Actualizar").document("Bandera")
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


                                                            Map<String, Object> NuevaInfoDeNotificacion = new HashMap<>();
                                                            NuevaInfoDeNotificacion.put("FechaDeEvento", FechaDeEvento.getText().toString());
                                                            NuevaInfoDeNotificacion.put("HoraInicio",    HoraInicio.getText().toString());
                                                            NuevaInfoDeNotificacion.put("Informacion",   Informacion.getText().toString());
                                                            NuevaInfoDeNotificacion.put("Lugar",         Lugar.getText().toString());
                                                            NuevaInfoDeNotificacion.put("QuienCalendarizo",      QuienCalendarizo);
                                                            NuevaInfoDeNotificacion.put("UltimaActualizacion",  objSDFQuitar .format(date));

                                                           

                                                            db.collection("Users").document(Aux).collection("Actualizar").document("NuevoEventoPropuesto")

                                                           // db.collection("Actualizar").document("NuevoEventoPropuesto")
                                                                    .set(NuevaInfoDeNotificacion)
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


                                                            Toast.makeText(getContext(), "Evento agregado", Toast.LENGTH_LONG).show();
                                                            Localidad.setText("");
                                                            Lugar.setText("");
                                                            Informacion.setText("");
                                                            FechaDeEvento.setText("");
                                                            HoraInicio.setText("");
                                                            HoraFinal.setText("");
                                                            Responsable.setText("");
                                                            Vestimenta.setText("");


                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w(TAG, "Error adding document", e);
                                                        }
                                                    });



                                        }



                                    }





                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });






                //replaceFragment(new AgregarEvento());
            }
        });

        BtnCancelarGuardadEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogNewEvent.dismiss();
            }
        });



        FechaDeEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar Calendario= Calendar.getInstance();
                int Dia=Calendario.get(Calendar.DAY_OF_MONTH);
                int Mes=Calendario.get(Calendar.MONTH);
                int Año=Calendario.get(Calendar.YEAR);

                DatePickerDialog CalendarioDialog=new DatePickerDialog(getContext(), R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month=month+1;
                        FechaDeEvento.setText(dayOfMonth+"/"+month+"/"+year);

                    }
                },Año,Mes,Dia);
                CalendarioDialog.show();
            }
        });

        HoraInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog HoraDialog=new TimePickerDialog(getContext(), R.style.datepicker, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Date dt_1 = null;
                        SimpleDateFormat objSDF = new SimpleDateFormat("HH:mm");
                        try {
                            dt_1 = objSDF.parse(hourOfDay+":"+minute);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        HoraInicio.setText( objSDF.format(dt_1));
                    }

                },12,00,false);
                HoraDialog.show();

            }
        });

        HoraFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog HoraDialog=new TimePickerDialog(getContext(), R.style.datepicker, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Date dt_1 = null;
                        SimpleDateFormat objSDF = new SimpleDateFormat("HH:mm");
                        try {
                            dt_1 = objSDF.parse(hourOfDay+":"+minute);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        HoraFinal.setText( objSDF.format(dt_1));
                    }

                },12,00,false);
                HoraDialog.show();


            }
        });







        builder.setView(view);
        DialogNewEvent=builder.create();
        DialogNewEvent.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }







}