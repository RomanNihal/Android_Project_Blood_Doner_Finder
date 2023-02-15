package com.example.blutspende.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.blutspende.ListOfDonors;
import com.example.blutspende.ModelNormalUser;
import com.example.blutspende.NavigationActivity;
import com.example.blutspende.SplashScreen;
import com.example.blutspende.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    String s;
    private Fragment fragment;

    private String division="",district="",bloodGroup="";
    private Spinner divisionSpinner,districtSpinner,bloodGroupSpinner;
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
    private FirebaseAuth mAuth;
    private Button searchButton;
    private String trimmedMail;
    private int flag=1;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();
        s = mAuth.getCurrentUser().getEmail();

        divisionSpinner = binding.homeDivision;
        districtSpinner = binding.homeDistrict;
        bloodGroupSpinner = binding.homeBloodGroup;
        searchButton = binding.homeSearchButton;

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


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(division.isEmpty() || district.isEmpty() || bloodGroup.isEmpty() )
                {
                    Toast.makeText(getContext(), "Please enter every information", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE}, NavigationActivity.REQUEST_CALL);
                } else {
                    Intent intent=new Intent(getContext(), ListOfDonors.class);
                    intent.putExtra("division",division);
                    intent.putExtra("district",district);
                    intent.putExtra("bloodGroup",bloodGroup);
                    startActivity(intent);
                }
            }
        });

        return root;
    }







    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}