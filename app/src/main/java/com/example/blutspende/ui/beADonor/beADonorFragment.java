package com.example.blutspende.ui.beADonor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.blutspende.NavigationActivity;
import com.example.blutspende.R;
import com.example.blutspende.SignIn;
import com.example.blutspende.SignUpAsDonor;
import com.example.blutspende.SplashScreen;
import com.example.blutspende.databinding.FragmentBeADonorBinding;
import com.example.blutspende.databinding.FragmentGalleryBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class beADonorFragment extends Fragment {

    final int code = 999;

    private FragmentBeADonorBinding binding;
    private String trimmedMail,name,contactNumber,password,division="",district="",bloodGroup="";
    private Spinner divisionSpinner,districtSpinner,bloodGroupSpinner;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBeADonorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressBar = binding.beADonorProgressBar;
        progressBar.getIndeterminateDrawable().setColorFilter(Color.GRAY, android.graphics.PorterDuff.Mode.SRC_ATOP);
        divisionSpinner = binding.beADonorDivision;
        districtSpinner = binding.beADonorDistrict;
        bloodGroupSpinner = binding.beADonorBloodGroup;
        checkBox = binding.beADonorCheckBox;
        datePicker = binding.beADonorDatePicker;
        imageView = binding.beADonorImage;
        imageButton = binding.beADonorImageButton;
        createButton = binding.beADonorCreateButton;
        datePicker.setMaxDate(System.currentTimeMillis());

        trimmedMail=SplashScreen.trimmedMail;
        name=SplashScreen.name;
        password=SplashScreen.password;
        contactNumber=SplashScreen.contactNumber;

        adapterDivision = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoryDivision);
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

                    adapterDistrict = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoryDistrictOfDhaka);
                    adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }
                if(division.equals("Sylhet")){
                    districtSpinner.setEnabled(true);

                    adapterDistrict = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoryDistrictOfSylhet);
                    adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }
                if(division.equals("Barishal")){
                    districtSpinner.setEnabled(true);

                    adapterDistrict = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoryDistrictOfBarishal);
                    adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }if(division.equals("Chattogram")){
                    districtSpinner.setEnabled(true);

                    adapterDistrict = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoryDistrictOfChattogram);
                    adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }if(division.equals("Khulna")){
                    districtSpinner.setEnabled(true);

                    adapterDistrict = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoryDistrictOfKhulna);
                    adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }if(division.equals("Mymensingh")){
                    districtSpinner.setEnabled(true);

                    adapterDistrict = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoryDistrictOfMymensingh);
                    adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }if(division.equals("Rajshahi")){
                    districtSpinner.setEnabled(true);

                    adapterDistrict = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoryDistrictOfRajshahi);
                    adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }if(division.equals("Rangpur")){
                    districtSpinner.setEnabled(true);

                    adapterDistrict = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoryDistrictOfRangpur);
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

        adapterBloodGroup = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoryBloodGroup);
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

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checkStatus = checkBox.isChecked();
                if(checkStatus){
                    flag=0;
                }
                else{flag=-1;
                }
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,code);;
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(division.isEmpty() || district.isEmpty() || bloodGroup.isEmpty() )
                {
                    Toast.makeText(getContext(), "Please enter every information", Toast.LENGTH_SHORT).show();
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

                databaseReference.child("users").child(trimmedMail).child("district").setValue(district+"");
                databaseReference.child("users").child(trimmedMail).child("division").setValue(division+"");
                databaseReference.child("users").child(trimmedMail).child("bloodGroup").setValue(bloodGroup+"");
                databaseReference.child("users").child(trimmedMail).child("day").setValue(day+"");
                databaseReference.child("users").child(trimmedMail).child("month").setValue(month+"");
                databaseReference.child("users").child(trimmedMail).child("year").setValue(year+"");
                databaseReference.child("users").child(trimmedMail).child("pictureLink").setValue(pictureLink+"");

                databaseReference.child("donors").child(division).child(district).child(bloodGroup).child(trimmedMail).child("district").setValue(district+"");
                databaseReference.child("donors").child(division).child(district).child(bloodGroup).child(trimmedMail).child("division").setValue(division+"");
                databaseReference.child("donors").child(division).child(district).child(bloodGroup).child(trimmedMail).child("bloodGroup").setValue(bloodGroup+"");
                databaseReference.child("donors").child(division).child(district).child(bloodGroup).child(trimmedMail).child("day").setValue(day+"");
                databaseReference.child("donors").child(division).child(district).child(bloodGroup).child(trimmedMail).child("month").setValue(month+"");
                databaseReference.child("donors").child(division).child(district).child(bloodGroup).child(trimmedMail).child("year").setValue(year+"");
                databaseReference.child("donors").child(division).child(district).child(bloodGroup).child(trimmedMail).child("pictureLink").setValue(pictureLink+"");
                databaseReference.child("donors").child(division).child(district).child(bloodGroup).child(trimmedMail).child("username").setValue(name+"");
                databaseReference.child("donors").child(division).child(district).child(bloodGroup).child(trimmedMail).child("phone").setValue(contactNumber+"");
                databaseReference.child("donors").child(division).child(district).child(bloodGroup).child(trimmedMail).child("password").setValue(password+"");

                Intent intent = new Intent(getContext(), SplashScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        return root;
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
                    Toast.makeText(getContext(), "Couldn't Upload !!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}