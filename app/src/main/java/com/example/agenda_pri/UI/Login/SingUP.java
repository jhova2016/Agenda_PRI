package com.example.agenda_pri.UI.Login;

import android.content.Intent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class SingUP extends AppCompatActivity {
    private FirebaseAuth mAuth;

    TextView Correo,Contraseña,ConfrimarContraseña;
    String AuxCorreo=null;
    FloatingActionButton Registrarse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_u_p);
        mAuth = FirebaseAuth.getInstance();
        Correo=findViewById(R.id.editTextUser);
        Contraseña=findViewById(R.id.editTextPassword);
        ConfrimarContraseña=findViewById(R.id.editTextConfirmarPassword);
        Registrarse=findViewById(R.id.BtnRegistrarse);

        Registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(Contraseña.getText().toString().equals("")||ConfrimarContraseña.getText().toString().equals("")||Correo.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Todos los campos deben ser llenados",
                            Toast.LENGTH_SHORT).show();

                }
                else {
                    if(Contraseña.getText().toString().equals(ConfrimarContraseña.getText().toString()))
                    {
                        AuxCorreo=Correo.getText().toString();
                        AuxCorreo=AuxCorreo.toLowerCase();
                        AuxCorreo=AuxCorreo.replace(" ","");
                        crearUsuario(AuxCorreo, Contraseña.getText().toString());
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden",
                                Toast.LENGTH_SHORT).show();
                    }

                }



            }
        });

    }

    public void crearUsuario(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null ){

            Map<String, Object> Evento = new HashMap<>();
            Evento.put("Aceptado",  true);
            Evento.put("Admin",  true);
            Evento.put("Correo",  AuxCorreo);

            Map<String, Object> Mail = new HashMap<>();
            Mail.put("Mail",  AuxCorreo);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(AuxCorreo).collection("Acceso").document(AuxCorreo)

            //db.collection("Acceso").document(mAuth.getUid())
                    .set(Evento)
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

            db.collection("Users").document(AuxCorreo).collection("Solicitudes").document(AuxCorreo)
                    .set(Mail)
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

            Intent intent = new Intent(this,SingIN.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), "Revise usuario y contraseña", Toast.LENGTH_SHORT).show();
        }
    }

}
