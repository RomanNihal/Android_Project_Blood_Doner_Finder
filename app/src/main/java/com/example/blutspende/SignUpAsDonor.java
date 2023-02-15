package com.example.blutspende;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.blutspende.Utility.NetworkChangeListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SignUpAsDonor extends AppCompatActivity {

    final int code = 999;

    private  Bundle bundle;
    private String name,contactNumber,password,division="",district="",bloodGroup="";
    private  Spinner divisionSpinner,districtSpinner,bloodGroupSpinner;
    private CheckBox checkBox;
    private ProgressBar progressBar;
    private DatePicker datePicker;
    private ImageView imageView;
    private Button imageButton,createButton;
    private ArrayAdapter<String> adapterDivision;
    private  ArrayAdapter<String> adapterDistrict;
    private  ArrayAdapter<String> adapterBloodGroup;
    private String[] categoryDivision = {"","Barishal","Chattogram", "Dhaka","Khulna","Mymensingh","Rajshahi","Rangpur", "Sylhet"};

    private String[] categoryDistrictOfDhaka = {"","Dhaka", "Faridpur", "Gazipur",
            "Gopalganj", "Kishoreganj", "Madaripur","Manikganj", "Munshiganj", "Narayanganj"
            , "Narsinghdi", "Rajbari", "Shariatpur", "Tangail"};

    private String[] categoryDistrictOfSylhet = {"","Habiganj", "Moulvibazar", "Sunamganj", "Sylhet"};

    private String[] categoryDistrictOfRangpur = {"","Dinajpur", "Gaibandha", "Kurigram", "Lalmonirhat",
            "Nilphamari", "Panchagar", "Rangpur", "Thakurgaon"};

    private String[] categoryDistrictOfRajshahi={"","Bogura", "Joypurhat", "'Naogaon'", "Natore",
            "Nawabganj", "Pabna", "Rajshahi", "Sirajganj"};

    private String[] categoryDistrictOfKhulna={"","Bagerhat", "Chuadanga", "Jashore", "Jhenaidah", "Khulna", "Kushtia", "Magura", "Meherpur", "Narail", "Satkhira"};

    private String[] categoryDistrictOfBarishal={"","Barguna", "Barishal", "Bhola", "Jhalakati", "Patuakhali", "Pirojpur"};

    private String[] categoryDistrictOfMymensingh={"","Jamalpur","Mymensingh","Netrokona","Sherpur"};

    private String[] categoryDistrictOfChattogram={"","Bandarban", "Brahmanbaria", "Chandpur", "Chattogram",
            "Cox’s Bazar", "Cumilla", "Feni", "Khagrachhari", "Lakshmipur", "Noakhali", "Rangamati"};
    private String[] categoryBloodGroup = {"","A+","B+","O+","AB+","A-","B-","O-","AB-"};
    private int flag=-1,day,month,year;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String pictureLink ="https://www.pngitem.com/pimgs/m/22-220721_circled-user-male-type-user-colorful-icon-png.png?fbclid=IwAR3VIKtfsa7rYCRsLWYKwJ6WS0Ga54kQtLxQAowOiK55zPgD8h7QeMXwr-0";
    private NetworkChangeListener networkChangeListener=new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_as_donor);


        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();

        bundle=getIntent().getExtras();
        name=bundle.getString("name");
        password=bundle.getString("password");
        contactNumber=bundle.getString("contactNumber");

        progressBar=findViewById(R.id.signUpDonorProgressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.GRAY, android.graphics.PorterDuff.Mode.SRC_ATOP);

        divisionSpinner = findViewById(R.id.signUpDonorDivision);
        districtSpinner = findViewById(R.id.signUpDonorDistrict);
        bloodGroupSpinner = findViewById(R.id.signUpDonorBloodGroup);
        checkBox = findViewById(R.id.signUpDonorCheckBox);
        datePicker = findViewById(R.id.signUpDonorDatePicker);
        imageView = findViewById(R.id.signUpDonorImage);
        imageButton = findViewById(R.id.signUpDonorImageButton);
        createButton = findViewById(R.id.signUpDonorCreateButton);
        datePicker.setMaxDate(System.currentTimeMillis());

        adapterDivision = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryDivision);
        adapterDivision.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        divisionSpinner.setAdapter(adapterDivision);

        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                division = adapterView.getItemAtPosition(i).toString();
                if(division.equals("")){
                    districtSpinner.setEnabled(false);
                }
                if(division.equals("Dhaka")){
                    districtSpinner.setEnabled(true);

                    adapterDistrict = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryDistrictOfDhaka);
                    adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }
                if(division.equals("Sylhet")){
                    districtSpinner.setEnabled(true);

                    adapterDistrict = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryDistrictOfSylhet);
                    adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }
                if(division.equals("Barishal")){
                    districtSpinner.setEnabled(true);

                    adapterDistrict = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryDistrictOfBarishal);
                    adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }if(division.equals("Chattogram")){
                    districtSpinner.setEnabled(true);

                    adapterDistrict = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryDistrictOfChattogram);
                    adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }if(division.equals("Khulna")){
                    districtSpinner.setEnabled(true);

                    adapterDistrict = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryDistrictOfKhulna);
                    adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }if(division.equals("Mymensingh")){
                    districtSpinner.setEnabled(true);

                    adapterDistrict = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryDistrictOfMymensingh);
                    adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }if(division.equals("Rajshahi")){
                    districtSpinner.setEnabled(true);

                    adapterDistrict = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryDistrictOfRajshahi);
                    adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }if(division.equals("Rangpur")){
                    districtSpinner.setEnabled(true);

                    adapterDistrict = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryDistrictOfRangpur);
                    adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }

                districtSpinner.setAdapter(adapterDistrict);

                districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        district = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapterBloodGroup = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryBloodGroup);
        adapterBloodGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSpinner.setAdapter(adapterBloodGroup);

        bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bloodGroup = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

    public void signUpDonorCheckBox(View view) {
        boolean checkStatus = checkBox.isChecked();
        if(checkStatus){
            flag=0;
        }
        else{flag=-1;
        }

    }

    public void signUpDonorCreateAccount(View view) {

        if(division.isEmpty() || district.isEmpty() || bloodGroup.isEmpty() )
        {
            Toast.makeText(this, "Please enter every information", Toast.LENGTH_SHORT).show();
            return;
        }
        day= datePicker.getDayOfMonth();
        month=datePicker.getMonth()+1;
        year=datePicker.getYear();

        if(flag==-1){
            year=1800;
            month=1;
            day=1;
        }

        databaseReference.child("donors").
                child(division).child(district).child(bloodGroup).child("q"+contactNumber+"gmailcom").
                setValue(new ModelNormalUser(name,password,contactNumber,
                        division,district,bloodGroup,day+"",month+"",+year+"",pictureLink));
        databaseReference.child("users").
                child("q"+contactNumber+"gmailcom").
                setValue(new ModelNormalUser(name,password,contactNumber,
                        division,district,bloodGroup,day+"",month+"",+year+"",pictureLink));

        Intent i = new Intent(this,SplashScreen.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(i);


    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialogExit = new AlertDialog.Builder(SignUpAsDonor.this);

        dialogExit.setTitle("EXIT!!");
        dialogExit.setMessage("If you don't want to be a donor you can exit");

        dialogExit.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
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

    public void signUpPictureChoosingButton(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,code);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == code && resultCode == Activity.RESULT_OK && data != null) {
            progressBar.setVisibility(View.VISIBLE);
            createButton.setEnabled(false);
            Uri result = data.getData();

            String key=databaseReference.push().getKey();

            StorageReference storageRef = FirebaseStorage.getInstance().getReference(name+key);
            storageRef.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            pictureLink=uri.toString();
                            imageView.setImageURI(result);
                            progressBar.setVisibility(View.INVISIBLE);
                            createButton.setEnabled(true);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Couldn't Upload !!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}