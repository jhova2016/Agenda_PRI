package com.example.agenda_pri.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda_pri.R;
import com.example.agenda_pri.ServicioNuevoEvento;
import com.example.agenda_pri.UI.Login.SingIN;
import com.example.agenda_pri.Utilerias.FragmentUiManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.util.Calendar;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    ImageView BtnOpciones;

    BottomNavigationView bottomNavigationView;

    private FirebaseAnalytics mFirebaseAnalytics;


    AlertDialog DialogOpciones;



    SharedPreferences sharedPreferences;

    TextView Identificador;
    TextView Correo;
    Fragment FragSchedulle ;
    Fragment FragProposed ;
    Fragment FragPromises ;


    FragmentUiManager fragmentUiManager;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FragSchedulle = new Principal();
        FragProposed = new Propuestos();
        FragPromises = new Promises() ;
        BtnOpciones=findViewById(R.id.ImageBtnOpciones);

        fragmentUiManager=new FragmentUiManager(fragmentManager,FragSchedulle,FragProposed,FragPromises);


        fragmentUiManager.FragmentUiManager("Schedulle");



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



        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        sharedPreferences = getSharedPreferences("ADMINISTRADORES", MODE_PRIVATE);
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

        BtnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), SingIN.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                stopService(intent);
                finish();
            }
        });



        builder.setView(view);
        DialogOpciones=builder.create();
        DialogOpciones.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        sharedPreferences = getSharedPreferences("ADMINISTRADORES", MODE_PRIVATE);

        TextView Titulo = null;
        Titulo=findViewById(R.id.Titulo);
        switch (item.getItemId()) {
            case R.id.action_inicio:
                fragmentUiManager.FragmentUiManager("Schedulle");
                Titulo.setText("Agenda");

                break;
            case R.id.action_posibles:

                fragmentUiManager.FragmentUiManager("Proposed");
                Titulo.setText("Propuestas");

                break;

            case R.id.action_promises:

                if(sharedPreferences.getInt("ADMIN", 1)==1)
                {
                    fragmentUiManager.FragmentUiManager("Promised");
                    Titulo.setText("Promesas");

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Opción desactivada",Toast.LENGTH_SHORT).show();
                }


                break;

        }

        return true;
    }



}