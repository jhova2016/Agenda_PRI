package com.example.agenda_pri.UI;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.agenda_pri.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class AgregarEvento extends Fragment {

    private FirebaseAuth mAuth;


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


    Spinner SpinnerTipoDeEvento;



    public AgregarEvento() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AgregarEvento newInstance(String param1, String param2) {
        AgregarEvento fragment = new AgregarEvento();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_agregar_evento, container, false);

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

                mAuth = FirebaseAuth.getInstance();
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

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Eventos")
                        .add(Evento)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());



                                SimpleDateFormat objSDFQuitar  = new SimpleDateFormat("HH:mm:ss");
                                Date date = new Date();

                                Map<String, Object> Hora = new HashMap<>();
                                Hora.put("UltimaActualizacion",  objSDFQuitar .format(date));

                                FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                                db2.collection("Actualizar").document("Bandera")
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


                              /*  db2.collection("Actualizar").document("NuevoEventoPropuesto")
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
*/

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



                //replaceFragment(new AgregarEvento());
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





        return view;
    }




}