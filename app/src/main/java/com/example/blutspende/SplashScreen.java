package com.example.blutspende;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.example.blutspende.Utility.NetworkChangeListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {
    public static String  trimmedMail="",name = "",password= "",contactNumber= "",day= "",month= ""
            ,year= "",div= "",dis= "",bg= "",picLink= "";
    private DatabaseReference dRef;
    private ProgressBar progressBar;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        dRef = FirebaseDatabase.getInstance().getReference();
        progressBar=findViewById(R.id.progressBarSplashScreen);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.GRAY, android.graphics.PorterDuff.Mode.SRC_ATOP);

        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        String s= firebaseUser.getEmail();
        trimmedMail =  s.replace("@","");
        trimmedMail =  trimmedMail.replace(".","");

        dRef.child("users").child(trimmedMail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ModelNormalUser modelNormalUser = snapshot.getValue(ModelNormalUser.class);

                name=modelNormalUser.getUsername();
                password=modelNormalUser.getPassword();
                contactNumber=modelNormalUser.getPhone();
                day=modelNormalUser.getDay();
                month=modelNormalUser.getMonth();
                year=modelNormalUser.getYear();
                div=modelNormalUser.getDivision();
                dis=modelNormalUser.getDistrict();
                bg=modelNormalUser.getBloodGroup();
                picLink=modelNormalUser.getPictureLink();
                progressBar.setVisibility(View.INVISIBLE);
                Intent in=new Intent(SplashScreen.this,NavigationActivity.class);
                startActivity(in);
                finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
    }
    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}