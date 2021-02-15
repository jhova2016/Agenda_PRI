package com.example.agenda_pri.UI.Login;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.agenda_pri.UI.MainActivity;
import com.example.agenda_pri.R;
import com.example.agenda_pri.UI.Principal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;

public class SingIN extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextView Registrarse;

    TextView Correo,Contraseña;
    FloatingActionButton BtnLogin;


    AlertDialog waitUploadData;
    SharedPreferences sharedPreferences;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_i_n);
        Registrarse=findViewById(R.id.BtnRegistrarse);
        Correo=findViewById(R.id.editTextUser);
        Contraseña=findViewById(R.id.editTextPasdword);
        BtnLogin=findViewById(R.id.BtnLogin);
        mAuth = FirebaseAuth.getInstance();

        Registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), SingUP.class);
                startActivity(intent);
            }
        });

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Correo.getText().toString().equals("")||Contraseña.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Complete ambos campos", Toast.LENGTH_LONG).show();
                }
                else {
                    String AuxCorreo=null;
                    AuxCorreo=Correo.getText().toString();
                    AuxCorreo=AuxCorreo.toLowerCase();
                    AuxCorreo=AuxCorreo.replace(" ","");
                    RevisarUsuario(AuxCorreo,Contraseña.getText().toString());
                }


            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void RevisarUsuario(String email, String password) {



        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI2(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            updateUI(null);
                        }

                        // ...
                    }
                });

    }

    private void updateUI(final FirebaseUser currentUser){
        if (currentUser != null ){

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), "Revise usuario, contraseña y coneccion a internet ", Toast.LENGTH_SHORT).show();

        }
    }

    private void updateUI2(final FirebaseUser currentUser){
        if (currentUser != null ){

            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseAuth mAuth=FirebaseAuth.getInstance();
            db.collection("Users").document(currentUser.getEmail()).collection("Acceso").document(currentUser.getEmail())
            //DocumentReference docRef1 = db.collection("Acceso").document(mAuth.getUid());
            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            if(document.get("Aceptado").toString().equals("true"))
                            {

                                sharedPreferences = getSharedPreferences("ADMINISTRADORES", MODE_PRIVATE);

                                if(document.get("Admin").toString().equals("true"))
                                {
                                    //si es admin
                                    sharedPreferences.edit().putInt("ADMIN",1).apply();
                                }else
                                {
                                    //si es admin
                                    sharedPreferences.edit().putInt("ADMIN",2).apply();
                                }



                                //descargar archivo de nbube
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();


                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Notifiqué al administrador de la app de su registro", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });





        }
        else{
            Toast.makeText(getApplicationContext(), "Revise usuario y contraseña", Toast.LENGTH_SHORT).show();
        }
    }






}
