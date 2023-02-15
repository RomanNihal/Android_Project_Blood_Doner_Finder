package com.example.blutspende.ui.gallery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.blutspende.ModelNormalUser;
import com.example.blutspende.NavigationActivity;
import com.example.blutspende.R;
import com.example.blutspende.SignIn;
import com.example.blutspende.SplashScreen;
import com.example.blutspende.databinding.FragmentGalleryBinding;
import com.example.blutspende.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    int flagDiv, flagDis;
    final int code = 999;

    private EditText userName;
    private TextView imageButton;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Button updateButton;
    private Bundle bundle;
    private String  trimmedMail,name,password,contactNumber,day,month,year,div,dis,bg,picLink;
    private Spinner divisionSpinner,districtSpinner,bloodGroupSpinner;
    private ArrayAdapter<String> adapterDivision;
    private ArrayAdapter<String> adapterDistrict;
    private ArrayAdapter<String> adapterBloodGroup;
    private String[] categoryDivision = {"# " + SplashScreen.div,"Barishal","Chattogram", "Dhaka","Khulna","Mymensingh","Rajshahi","Rangpur", "Sylhet"};

    private String[] categoryDistrictOfDhaka = {"# " + SplashScreen.dis,"Dhaka", "Faridpur", "Gazipur",
            "Gopalganj", "Kishoreganj", "Madaripur","Manikganj", "Munshiganj", "Narayanganj"
            , "Narsinghdi", "Rajbari", "Shariatpur", "Tangail"};

    private String[] categoryDistrictOfSylhet = {"# " + SplashScreen.dis,"Habiganj", "Moulvibazar", "Sunamganj", "Sylhet"};

    private String[] categoryDistrictOfRangpur = {"# " + SplashScreen.dis,"Dinajpur", "Gaibandha", "Kurigram", "Lalmonirhat",
            "Nilphamari", "Panchagar", "Rangpur", "Thakurgaon"};

    private String[] categoryDistrictOfRajshahi={"# " + SplashScreen.dis,"Bogura", "Joypurhat", "'Naogaon'", "Natore",
            "Nawabganj", "Pabna", "Rajshahi", "Sirajganj"};

    private String[] categoryDistrictOfKhulna={"# " + SplashScreen.dis,"Bagerhat", "Chuadanga", "Jashore", "Jhenaidah", "Khulna", "Kushtia", "Magura", "Meherpur", "Narail", "Satkhira"};

    private String[] categoryDistrictOfBarishal={"# " + SplashScreen.dis,"Barguna", "Barishal", "Bhola", "Jhalakati", "Patuakhali", "Pirojpur"};

    private String[] categoryDistrictOfMymensingh={"# " + SplashScreen.dis,"Jamalpur","Mymensingh","Netrokona","Sherpur"};

    private String[] categoryDistrictOfChattogram={"# " + SplashScreen.dis,"Bandarban", "Brahmanbaria", "Chandpur", "Chattogram",
            "Cox’s Bazar", "Cumilla", "Feni", "Khagrachhari", "Lakshmipur", "Noakhali", "Rangamati"};

    private String[] categoryBloodGroup = {"# " + SplashScreen.bg,"A+","B+","O+","AB+","A-","B-","O-","AB-"};
    private String newName,division="",district="",bloodGroup="",pictureLink="";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        name=SplashScreen.name;
        password=SplashScreen.password;
        picLink=SplashScreen.picLink;
        div=SplashScreen.div;
        dis=SplashScreen.dis;
        bg=SplashScreen.bg;
        trimmedMail=SplashScreen.trimmedMail;
        day=SplashScreen.day;
        month=SplashScreen.month;
        year=SplashScreen.year;
        contactNumber=SplashScreen.contactNumber;

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        userName = binding.editProfileUserName;
        userName.setText(name);
        progressBar = binding.editProfileDonorProgressBar;
        divisionSpinner = binding.editProfileSpinnerDivision;
        districtSpinner = binding.editProfileSpinnerDistrict;
        bloodGroupSpinner = binding.editProfileSpinnerBloodGroup;
        imageView = binding.editProfileDonorImage;
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(this).load(picLink).into(imageView);
        progressBar.setVisibility(View.INVISIBLE);
        imageButton = binding.editProfileDonorImageButton;

        updateButton = binding.editProfileUpdateButton;

        adapterDivision = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoryDivision){
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }
        };

        adapterDivision.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        divisionSpinner.setAdapter(adapterDivision);
        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLUE);
                if(i==0){
                    flagDiv = 0;
                    division = adapterView.getItemAtPosition(i).toString().replace("# ","");
                }
                else{
                    TextView selectedText = (TextView) adapterView.getChildAt(0);
                    if (selectedText != null) {
                        selectedText.setTextColor(Color.BLUE);
                    }
                    flagDiv = -1;
                    division = adapterView.getItemAtPosition(i).toString();
                }
                if(division.equals("Dhaka")){
                    adapterDistrict = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoryDistrictOfDhaka){
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    };
                    adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }
                if(division.equals("Sylhet")){
                    adapterDistrict = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoryDistrictOfSylhet){
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    };
                    adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }
                if(division.equals("Barishal")){
                    adapterDistrict = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoryDistrictOfBarishal){
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    };
                    adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }
                if(division.equals("Chattogram")){
                    adapterDistrict = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoryDistrictOfChattogram){
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    };
                    adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }
                if(division.equals("Khulna")){
                    adapterDistrict = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoryDistrictOfKhulna){
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    };
                    adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }
                if(division.equals("Mymensingh")){
                    adapterDistrict = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoryDistrictOfMymensingh){
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    };
                    adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }
                if(division.equals("Rajshahi")){
                    adapterDistrict = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoryDistrictOfRajshahi){
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    };
                    adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }
                if(division.equals("Rangpur")){
                    adapterDistrict = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoryDistrictOfRangpur){
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    };
                    adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }

                districtSpinner.setAdapter(adapterDistrict);

                districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLUE);
                        if(i==0){
                            flagDis = 0;
                            district = adapterView.getItemAtPosition(i).toString().replace("# ","");;
                        }
                        else{
                            TextView selectedText = (TextView) adapterView.getChildAt(0);
                            if (selectedText != null) {
                                selectedText.setTextColor(Color.BLUE);
                            }
                            flagDis = -1;
                            district = adapterView.getItemAtPosition(i).toString();
                        }
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

        adapterBloodGroup = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoryBloodGroup){
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }
        };
        adapterBloodGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSpinner.setAdapter(adapterBloodGroup);

        bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLUE);
                if(i==0){
                    bloodGroup = adapterView.getItemAtPosition(i).toString().replace("# ","");;
                }
                else{
                    TextView selectedText = (TextView) adapterView.getChildAt(0);
                    if (selectedText != null) {
                        selectedText.setTextColor(Color.BLUE);
                    }
                    bloodGroup = adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,code);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newName=userName.getText().toString();
                if(newName.isEmpty()){
                    userName.setError("Can't be empty");
                    userName.requestFocus();
                    return;
                }
                databaseReference.child("donors").child(div).child(dis).child(bg).child(trimmedMail).removeValue();
                name=newName;
                if(flagDiv!=0){
                    if(flagDis==0){
                        Toast.makeText(getContext(), "Select district", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(!division.isEmpty()){
                    div=division;
                }
                if(!district.isEmpty()){
                    dis=district;
                }
                if(!bloodGroup.isEmpty()){
                    bg=bloodGroup;
                }
                if(!pictureLink.isEmpty()){
                    picLink=pictureLink;
                }

                databaseReference.child("users").child(trimmedMail).setValue(new ModelNormalUser(name,password,contactNumber,
                        div,dis,bg,day+"",month+"",year+"",picLink));
                if(!day.isEmpty()){

                    databaseReference.child("donors").child(div).child(dis).child(bg).child(trimmedMail).setValue(new ModelNormalUser(name,password,contactNumber,
                            div,dis,bg,day+"",month+"",year+"",picLink));
                }

                Toast.makeText(getContext(), "Updated !!", Toast.LENGTH_LONG).show();
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
            updateButton.setEnabled(false);
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
                            updateButton.setEnabled(true);
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