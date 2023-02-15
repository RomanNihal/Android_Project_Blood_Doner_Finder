package com.example.blutspende;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OldPassword extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private Bundle bundle;
    private String contactNumber;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_password);
        textView=findViewById(R.id.textViewOldPassword);
        bundle=getIntent().getExtras();
        contactNumber= bundle.getString("contactNumber");
        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child("q"+contactNumber+"gmailcom").child("password").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String pass=snapshot.getValue(String.class);
                    textView.setText("Your password is- '"+pass+"'. You can now login using this password and if you want to change your password you can do it after logging in.");
                }
                else{
                    textView.setText("We couldn't find any account linked with the number.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}