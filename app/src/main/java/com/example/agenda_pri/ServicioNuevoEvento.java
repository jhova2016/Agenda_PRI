package com.example.agenda_pri;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.agenda_pri.Models.Elemento_Evento;
import com.example.agenda_pri.UI.Login.SingIN;
import com.example.agenda_pri.UI.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class ServicioNuevoEvento extends Service {

    //Inicio

    SharedPreferences sharedPreferences;

    public ServicioNuevoEvento() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {





        sharedPreferences = this.getSharedPreferences("ADMINISTRADORES", MODE_PRIVATE);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Actualizar").document("NuevoEventoPropuesto");

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot , @Nullable FirebaseFirestoreException error) {


                if(!snapshot.get("UltimaActualizacion").equals( sharedPreferences.getString("ACTUALIZACION", "")))
                {

                    // Create an explicit intent for an Activity in your app

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                    createNotificationChannel( snapshot.get("UltimaActualizacion").toString());

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),  snapshot.get("UltimaActualizacion").toString())
                            .setSmallIcon(R.mipmap.ic_inicio)
                            .setContentTitle("Nuevo evento propuesto")
                            .setContentText(snapshot.get("FechaDeEvento").toString()+snapshot.get("HoraInicio").toString())
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(snapshot.get("FechaDeEvento").toString()+"  "+snapshot.get("HoraInicio").toString()+"  "+snapshot.get("Lugar").toString()+"  "+snapshot.get("QuienCalendarizo").toString()))
                            .setTicker("nueva notificacio")
                            .setWhen(System.currentTimeMillis())
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                    // notificationId is a unique int for each notification that you must define
                    int valorDado = (int) Math.floor(Math.random()*20+1);
                    notificationManager.notify(valorDado, builder.build());

                    sharedPreferences.edit().putString("ACTUALIZACION", (String) snapshot.get("UltimaActualizacion")).apply();
                }







            }

        });




        return START_STICKY;
    }

    private void createNotificationChannel(String ID) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = ID;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(ID, name, importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager =(NotificationManager) getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"Se paro mi servicio",Toast.LENGTH_LONG).show();
    }


}
