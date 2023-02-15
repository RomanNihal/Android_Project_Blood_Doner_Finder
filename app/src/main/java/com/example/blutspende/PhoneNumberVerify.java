package com.example.blutspende;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blutspende.Utility.NetworkChangeListener;

public class PhoneNumberVerify extends AppCompatActivity {
   private EditText editText;
   private Bundle bundle;
   private Button button;
   private TextView textView;
   private String from;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_verify);
        editText=findViewById(R.id.phoneNumberVerifyEditText);
        button=findViewById(R.id.phoneNumberVerifyButton);
        textView=findViewById(R.id.textViewPhoneNumberVerify);
        bundle=getIntent().getExtras();
        from= bundle.getString("fromWhere");

        if(from.equals("forgetPass") ){
            textView.setText("! Enter the registered number !");
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s="+88"+editText.getText().toString();
                if(s.length()!=14){
                    editText.setError("Enter a valid number");
                    editText.requestFocus();
                    return;
                }
                Intent intent=new Intent(getApplicationContext(),OTPverify.class);
                intent.putExtra("phoneNumber",s);
                if(from.equals("forgetPass")){
                    intent.putExtra("fromWhere","forgetPass");
                }
                else{
                    intent.putExtra("fromWhere","signUp");
                }
                startActivity(intent);

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