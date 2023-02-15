package com.example.blutspende;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blutspende.Utility.NetworkChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpAsNormalUser extends AppCompatActivity {

   private Bundle bundle;
    private String contactNumber;
    private EditText userName, contact, pass, confirmPass;
    private Button confirmButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private CheckBox checkBox;
    private int flag=-1;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_as_normal_user);
        bundle=getIntent().getExtras();
        contactNumber=bundle.getString("contactNumber");


        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();

        userName=findViewById(R.id.signUpUserName);

        pass=findViewById(R.id.signUpPass);
        pass=findViewById(R.id.signUpPass);
        confirmPass=findViewById(R.id.signUpConfirmPass);
        confirmButton=findViewById(R.id.signUpCreateButton);
        checkBox=findViewById(R.id.signUpCheckbox);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=userName.getText().toString();
                String password=pass.getText().toString();
                String confirmPassword=confirmPass.getText().toString();

                if(name.isEmpty()){
                    userName.setError("Please enter !");
                    userName.requestFocus();
                    return;
                }
                if(password.isEmpty()){
                    pass.setError("Please enter !");
                    pass.requestFocus();
                    return;
                }
                if(confirmPassword.isEmpty()){
                    confirmPass.setError("Please enter !");
                    confirmPass.requestFocus();
                    return;
                }
                if(password.length()<6){
                    pass.setError("at least 6 digit");
                    pass.requestFocus();
                    return;
                }
                if(!confirmPassword.equals(password)){
                    confirmPass.setError("Didn't match with the password !");
                    confirmPass.requestFocus();
                    return;
                }
                boolean status=checkBox.isChecked();
                if(status){
                    flag=0;
                }

                String newName = "q"+contactNumber + "@gmail.com";
                firebaseAuth.createUserWithEmailAndPassword(newName,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            databaseReference.child("users").child("q"+contactNumber + "gmailcom").
                            setValue(new ModelNormalUser(name, password, contactNumber,"",
                                    "","","","","",
                                    "https://www.pngitem.com/pimgs/m/22-220721_circled-user-male-type-user-colorful-icon-png.png?fbclid=IwAR3VIKtfsa7rYCRsLWYKwJ6WS0Ga54kQtLxQAowOiK55zPgD8h7QeMXwr-0"));


                            Toast.makeText(SignUpAsNormalUser.this, "Successfully signed up", Toast.LENGTH_SHORT).show();

                            if(flag==0){
                                Intent intent=new Intent(getApplicationContext(),SignUpAsDonor.class);
                                intent.putExtra("name",name);
                                intent.putExtra("password",password);
                                intent.putExtra("contactNumber",contactNumber);
                                intent.putExtra("fromWhere","signIn");
                                startActivity(intent);
                            }
                            else{
                                startActivity(new Intent(SignUpAsNormalUser.this,SplashScreen.class));
                                finish();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorText=e.getMessage();
                        if(errorText.equals("The email address is already in use by another account.")){
                            errorText="This number is already taken !";
                        }
                        Toast.makeText(SignUpAsNormalUser.this,errorText, Toast.LENGTH_SHORT).show();
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