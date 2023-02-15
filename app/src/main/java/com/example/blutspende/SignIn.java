package com.example.blutspende;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blutspende.Utility.NetworkChangeListener;
import com.example.blutspende.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {
    private EditText userName,pass;
    private FirebaseAuth mAuth;
   private NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in2);
        mAuth=FirebaseAuth.getInstance();
        userName=findViewById(R.id.signInUserName);
        pass=findViewById(R.id.signInPass);

    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    @Override
    protected void onStart() {
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null){
            startActivity(new Intent(this,SplashScreen.class));
            finish();
        }
    }

    public void signUp(View view) {

        Intent intent=new Intent(this,PhoneNumberVerify.class);
        intent.putExtra("fromWhere","signUp");
        startActivity(intent);
    }

    public void signInButton(View view) {
        String name=userName.getText().toString().trim();
        String password=pass.getText().toString().trim();
        if(name.isEmpty()){
            userName.setError("Enter number");
            userName.requestFocus();
            return;
        }
        if(name.length()!=11){
            userName.setError("Not valid");
            userName.requestFocus();
            return;
        }
        if(password.isEmpty()){
            pass.setError("Enter password");
            pass.requestFocus();
            return;
        }
        if(password.length()<6){
            pass.setError("at least 6 digit");
            pass.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword("q+88"+name+"@gmail.com",password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(SignIn.this, "Successfully signed in..", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),SplashScreen.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignIn.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void forgetPass(View view) {
        Intent intent=new Intent(this,PhoneNumberVerify.class);
        intent.putExtra("fromWhere","forgetPass");
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialogExit = new AlertDialog.Builder(SignIn.this);

        dialogExit.setTitle("EXIT!!");
        dialogExit.setMessage("Are you sure?");

        dialogExit.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
                finish();
            }
        });

        dialogExit.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogExit.show();
    }
}
