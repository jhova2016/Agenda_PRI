package com.example.agenda_pri.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NativeActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda_pri.Models.AdapterMail;
import com.example.agenda_pri.Models.AdapterPeticiones;
import com.example.agenda_pri.Models.Element_Peticion;
import com.example.agenda_pri.R;
import com.example.agenda_pri.ServicioNuevoEvento;
import com.example.agenda_pri.UI.Login.SingIN;
import com.example.agenda_pri.Utilerias.FragmentUiManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    ImageView BtnOpciones;
    BottomNavigationView bottomNavigationView;
    private FirebaseAnalytics mFirebaseAnalytics;

    AlertDialog DialogOpciones;
    AlertDialog DialogPeticiones;
    AlertDialog DialogChamgleMail;


    SharedPreferences sharedPreferences;

    TextView Identificador;
    TextView Correo;
    Fragment FragSchedulle ;
    Fragment FragProposed ;
    Fragment FragPromises ;

    ArrayList<Element_Peticion> ListLastEvents = new ArrayList<Element_Peticion>();

    FirebaseAuth mAuth;

    FragmentUiManager fragmentUiManager;

    RecyclerView RecyclerLastEvents;
    AdapterPeticiones AdapterLastSchedulle;

    AdapterMail Adaptermail;


    FirebaseFirestore db;
    LinearLayout BtnChamgleMail;

    TextView TxtChamgleMail;

    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        fragmentManager = getSupportFragmentManager();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FragSchedulle = new Principal();
        FragProposed = new Propuestos();
        FragPromises = new Promises() ;
        BtnOpciones=findViewById(R.id.ImageBtnOpciones);

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("ADMINISTRADORES", MODE_PRIVATE);

        fragmentUiManager=new FragmentUiManager(fragmentManager,FragSchedulle,FragProposed,FragPromises);


        fragmentUiManager.FragmentUiManager("Schedulle");

        TxtChamgleMail= findViewById(R.id.Mail);
        String Aux=sharedPreferences.getString("SchedulleUser", mAuth.getCurrentUser().getEmail());
        TxtChamgleMail.setText(Aux);


        Identificador=findViewById(R.id.Identificador);
        Correo=findViewById(R.id.Correo);


        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();


        Correo.setText(user.getEmail());
        db.collection("Users").document(mAuth.getUid()).collection("Acceso").document(mAuth.getUid())
        //DocumentReference docRef1 = db.collection("Acceso").document(mAuth.getUid());
        //docRef1
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        if(!document.get("Aceptado").toString().equals("true"))
                        {
                            Toast.makeText(getApplicationContext(), "Revise usuario, contraseña y coneccion a internet ", Toast.LENGTH_SHORT).show();

                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(getApplicationContext(), SingIN.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }
                        else
                        {


                            //replaceFragment(FragSchedulle);
                            bottomNavigationView.setSelectedItemId(R.id.action_inicio);



                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        BtnOpciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogOpcionesFuncion();
                DialogOpciones.show();
            }
        });

        BtnChamgleMail = findViewById(R.id.changeMail);
        BtnChamgleMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogChangleMail();
                DialogChamgleMail.show();

            }
        });



        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if(sharedPreferences.getInt("ADMIN", 1)==1)
        {
            Identificador.setText("Administrador");
            Intent intent=new Intent(MainActivity.this,ServicioNuevoEvento.class);
            startService(intent);

        }
        else
        {
            Identificador.setText("Espectador");
            Intent intent=new Intent(MainActivity.this,ServicioNuevoEvento.class);
            stopService(intent);
        }




    }



    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment_placeholder, fragment);
        fragmentTransaction.commit();
    }

    public void DialogOpcionesFuncion ()
    {
        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_opciones,null);


        Button BtnCerrarSesion;
        BtnCerrarSesion=view.findViewById(R.id.BtnOpcionesDialog2);

        Button BtnPeticiones;
        BtnPeticiones=view.findViewById(R.id.BtnPeticiones);

        BtnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferences = getApplication().getSharedPreferences("ADMINISTRADORES", MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), SingIN.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                stopService(intent);
                finish();
            }
        });

        BtnPeticiones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogPeticiones ();
                DialogPeticiones.show();


            }
        });



        builder.setView(view);
        DialogOpciones=builder.create();
        DialogOpciones.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void DialogPeticiones ()
    {
        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.peticiones,null);

        RecyclerLastEvents = view.findViewById(R.id.recyclerLastEvents);
        RecyclerLastEvents.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        ListLastEvents.clear();
        final String Aux=mAuth.getCurrentUser().getEmail();
        db.collection("Users").document(Aux).collection("Acceso")
                //db.collection("Eventos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                ListLastEvents.add(new Element_Peticion(document.get("Correo").toString() ,document.get("Aceptado").toString() ,document.get("Admin").toString() ) );
                                //ListLastEvents.add(document.get("Correo").toString() );

                            }

                            AdapterLastSchedulle = new AdapterPeticiones(ListLastEvents,getApplicationContext());
                            AdapterLastSchedulle.notifyDataSetChanged();
                            RecyclerLastEvents.setAdapter(AdapterLastSchedulle);


                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });







        Button BtnCerrarSesion;
        BtnCerrarSesion=view.findViewById(R.id.BtnCancelNewPromised);

        BtnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });



        builder.setView(view);
        DialogPeticiones=builder.create();
        DialogPeticiones.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        sharedPreferences = getSharedPreferences("ADMINISTRADORES", MODE_PRIVATE);


        switch (item.getItemId()) {
            case R.id.action_inicio:

                fragmentUiManager.FragmentUiManager("Schedulle");


                break;
            case R.id.action_posibles:

                fragmentUiManager.FragmentUiManager("Proposed");


                break;

            case R.id.action_promises:

                if(sharedPreferences.getInt("ADMIN", 1)==1)
                {
                    fragmentUiManager.FragmentUiManager("Promised");


                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Opción desactivada",Toast.LENGTH_SHORT).show();
                }


                break;

        }

        return true;
    }


    public void DialogChangleMail()
    {
        final ArrayList<String> ListLastEvents = new ArrayList<String>();


        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(this).setCancelable(true);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_changle_mail,null);

        Button BtnRequest=view.findViewById(R.id.BtnRequest);
        final EditText MailRequest=view.findViewById(R.id.editRequest);
        RecyclerLastEvents = view.findViewById(R.id.recycler);
        RecyclerLastEvents.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));

        ListLastEvents.clear();
        final String Aux=mAuth.getCurrentUser().getEmail();
        db.collection("Users").document(Aux).collection("Solicitudes")
                //db.collection("Eventos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                ListLastEvents.add(document.get("Mail").toString() );

                            }

                            Adaptermail = new AdapterMail(ListLastEvents,getApplicationContext());
                            Adaptermail.notifyDataSetChanged();
                            RecyclerLastEvents.setAdapter(Adaptermail);

                            Adaptermail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    sharedPreferences.edit().putString("SchedulleUser",ListLastEvents.get(RecyclerLastEvents.getChildAdapterPosition(v))).apply();
                                    String Aux=sharedPreferences.getString("SchedulleUser", mAuth.getCurrentUser().getEmail());
                                    db.collection("Users").document(Aux).collection("Acceso").document(mAuth.getCurrentUser().getEmail())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                    sharedPreferences.edit().putString("Admin",task.getResult().get("Admin").toString()).apply();
                                                    sharedPreferences.edit().putString("Aceptado",task.getResult().get("Aceptado").toString()).apply();

                                                    Intent intent = getIntent();
                                                    finish();
                                                    startActivity(intent);


                                                }
                                            });
                                    DialogChamgleMail.dismiss();



                                }
                            });



                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });





        BtnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String SolicitudPara=MailRequest.getText().toString();
                if(!SolicitudPara.equals(""))
                {
                    SolicitudPara=SolicitudPara.toLowerCase();
                    SolicitudPara=SolicitudPara.replace(" ","");
                    final String finalSolicitudPara = SolicitudPara;


                    DocumentReference docRef = db.collection("Users").document(finalSolicitudPara).collection("Acceso").document(finalSolicitudPara);

                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {

                                    Map<String, Object> Evento = new HashMap<>();
                                    Evento.put("Aceptado",  false);
                                    Evento.put("Admin",  false);
                                    Evento.put("Correo",  mAuth.getCurrentUser().getEmail());

                                    final FirebaseFirestore db2 = FirebaseFirestore.getInstance();


                                    // db.collection("Acceso").document(mAuth.getUid())
                                    db2.collection("Users").document(finalSolicitudPara).collection("Acceso").document(mAuth.getCurrentUser().getEmail())
                                            .set(Evento).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(getApplicationContext(),"Si se añadio",Toast.LENGTH_LONG).show();
                                        }
                                    })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                    Toast.makeText(getApplicationContext(),"Ocurrio un error",Toast.LENGTH_LONG).show();
                                                }
                                            });

                                    Map<String, Object> Sol = new HashMap<>();
                                    Sol.put("Mail", finalSolicitudPara);
                                    db2.collection("Users").document(mAuth.getCurrentUser().getEmail()).collection("Solicitudes").document(finalSolicitudPara)
                                            .set(Sol);



                                } else {
                                    Toast.makeText(getApplicationContext(),"No Existe Este Usuario",Toast.LENGTH_LONG).show();

                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });


                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Ingrese dato",Toast.LENGTH_LONG).show();
                }



            }
        });






        builder.setView(view);
        DialogChamgleMail=builder.create();
        DialogChamgleMail.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }




}