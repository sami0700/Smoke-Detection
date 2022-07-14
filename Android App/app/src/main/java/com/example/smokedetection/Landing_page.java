package com.example.smokedetection;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.INotificationSideChannel;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class Landing_page extends AppCompatActivity {
    TextView value,smokeDetected,ambulance,fire,call;
    Button detectSmoke;
    ImageView calling;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        findViewById();
        clicklistener();
    }
    public void findViewById(){
        value = findViewById(R.id.valueTextView);
        smokeDetected=findViewById(R.id.detectedTextView);
        detectSmoke=findViewById(R.id.detectSmoke);
        fire=findViewById(R.id.fire);
        ambulance=findViewById(R.id.ambulance);
        call=findViewById(R.id.emergency);
        calling=findViewById(R.id.calling);
    }
    public void firebase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference();
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ph = dataSnapshot.child("Value").getValue().toString();
                String data = dataSnapshot.child("Sensor").getValue().toString();
                Integer smokeValue= Integer.parseInt(ph);
                value.setText(smokeValue.toString());
                if(smokeValue>Integer.valueOf(data)){
                    smokedetect();
                }
                else {
                    smokenotdetect();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Failed to read value",error.toException().toString());
            }
        });
    }
    public void smokedetect(){
        detectSmoke.setVisibility(View.INVISIBLE);
        smokeDetected.setVisibility(View.VISIBLE);
        call.setVisibility(View.VISIBLE);
        fire.setVisibility(View.VISIBLE);
        ambulance.setVisibility(View.VISIBLE);
        calling.setVisibility(View.VISIBLE);
        value.setTextColor(Color.RED);
    }
    public void smokenotdetect(){
        value.setTextColor(Color.WHITE);
        detectSmoke.setVisibility(View.VISIBLE);
        smokeDetected.setVisibility(View.INVISIBLE);
        call.setVisibility(View.INVISIBLE);
        fire.setVisibility(View.INVISIBLE);
        ambulance.setVisibility(View.INVISIBLE);
        calling.setVisibility(View.INVISIBLE);
    }
    public void alertdialogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Landing_page.this);
        builder.setCancelable(true);
        builder.setTitle("Smoke Detected");
        builder.setMessage("");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                smokenotdetect();
            }
        });
        builder.show();
    }
    public void clicklistener(){
        detectSmoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebase();
                Animation animAlpha = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_alpha);
                view.startAnimation(animAlpha);
            }
        });
        if(ActivityCompat.checkSelfPermission(Landing_page.this, Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED) {
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s = "tel:" + "112";
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(s));
                    startActivity(intent);
                }
            });

            ambulance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s = "tel:" + "102";
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(s));
                    startActivity(intent);
                }
            });
            fire.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s = "tel:" + "101";
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(s));
                    startActivity(intent);
                }
            });
        }
        else{
            ActivityCompat.requestPermissions(Landing_page.this,new String[]{Manifest.permission.CALL_PHONE},44);
        }
    }
}
