package com.example.blutspende;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blutspende.Utility.NetworkChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPverify extends AppCompatActivity {
   private FirebaseAuth mAuth;
    private EditText editTextOtp;
    private Button buttonOtp;
    private Bundle bundle;
    private String codeBySystem;
    private String phoneNumber,from;
    private NetworkChangeListener networkChangeListener=new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverify);
        Toast.makeText(this, "Wait for some moment ", Toast.LENGTH_LONG).show();
        editTextOtp = findViewById(R.id.editTextNumber);
        buttonOtp = findViewById(R.id.button2);

        mAuth = FirebaseAuth.getInstance();


        bundle = getIntent().getExtras();
        phoneNumber = bundle.getString("phoneNumber");
        from= bundle.getString("fromWhere");


        sendVerificationCode(phoneNumber);

    }

    private void sendVerificationCode(String phoneNumber) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            String code=credential.getSmsCode()+"";

            if(!code.isEmpty()){

                editTextOtp.setText(code);
                verifyCode(credential.getSmsCode());

            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OTPverify.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            codeBySystem=verificationId;
        }
    };
    private void verifyCode(String smsCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, smsCode);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                               mAuth.signOut();


                            if(from.equals("forgetPass") ){
                                Intent in=new Intent(getApplicationContext(),OldPassword.class);
                                in.putExtra("contactNumber",phoneNumber);
                                startActivity(in);
                                finish();
                                Toast.makeText(OTPverify.this, "Verified", Toast.LENGTH_SHORT).show();

                            }
                            else{
                                Intent i=new Intent(getApplicationContext(),SignUpAsNormalUser.class);
                                i.putExtra("contactNumber",phoneNumber);
                                startActivity(i);
                                Toast.makeText(OTPverify.this, "Verified", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(OTPverify.this, "Couldn't Verify !", Toast.LENGTH_SHORT).show();                            }
                        }
                    }
                });
    }
    public void otpConfirmButton(View view) {

        String code=editTextOtp.getText().toString();
        if(code.isEmpty()){
            editTextOtp.setError("Enter OTP");
            editTextOtp.requestFocus();
            return;
        }
        verifyCode(code);
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