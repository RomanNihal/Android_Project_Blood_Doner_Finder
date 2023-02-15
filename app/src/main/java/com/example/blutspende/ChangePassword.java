package com.example.blutspende;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blutspende.Utility.NetworkChangeListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePassword extends AppCompatActivity {
    private EditText oldPass, newPass, newConfirmPass;
    private String oldPassword, newPassword, newConfirmPassword;
    private Button button;
    private DatabaseReference databaseReference;
    private NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        oldPass=findViewById(R.id.changePassUserName);
        newPass=findViewById(R.id.changePassPass);
       newConfirmPass=findViewById(R.id.changePassConfirmPass);
       button=findViewById(R.id.changePassCreateButton);
       databaseReference= FirebaseDatabase.getInstance().getReference();

       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
        oldPassword=oldPass.getText().toString();
        newPassword= newPass.getText().toString();
        newConfirmPassword= newConfirmPass.getText().toString();


               if(oldPassword.isEmpty()){
                   oldPass.setError("Please enter !");
                   oldPass.requestFocus();
                   return;
               }
                if(newPassword.isEmpty()){
                    newPass.setError("Please enter !");
                    newPass.requestFocus();
                   return;
               }

               if(newConfirmPassword.isEmpty()){
                   newConfirmPass.setError("Please enter !");
                   newConfirmPass.requestFocus();
                   return;
               }

               if(newPassword.length()<6){
                   newPass.setError("at least 6 digit");
                   newPass.requestFocus();
                   return;
               }
               if(!newConfirmPassword.equals(newPassword)) {
                   newConfirmPass.setError("Didn't match with the password !");
                   newConfirmPass.requestFocus();
                   return;
               }
               final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
               String mail=firebaseUser.getEmail();
               String trimmedMail=mail.replace("@","");
               trimmedMail=trimmedMail.replace(".","");
               AuthCredential authCredential= EmailAuthProvider.getCredential(mail,oldPassword);
               String finalTrimmedMail = trimmedMail;
               firebaseUser.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void unused) {
                       firebaseUser.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void unused) {

                               databaseReference.child("users").child(finalTrimmedMail).child("password").setValue(newPassword);
                               Toast.makeText(ChangePassword.this, "Password Updated !", Toast.LENGTH_SHORT).show();

                               Intent intent=new Intent(ChangePassword.this,SplashScreen.class);
                               startActivity(intent);
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Toast.makeText(ChangePassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                           }
                       });
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
//                       Toast.makeText(ChangePassword.this,"", Toast.LENGTH_SHORT).show();
                       oldPass.setError("Wrong Password");
                       oldPass.requestFocus();
                   }
               });
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