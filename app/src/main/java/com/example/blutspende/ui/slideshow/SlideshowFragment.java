package com.example.blutspende.ui.slideshow;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.blutspende.SignUpAsDonor;
import com.example.blutspende.SplashScreen;
import com.example.blutspende.databinding.FragmentSlideshowBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SlideshowFragment extends Fragment {
    private TextView textView;
    private DatabaseReference databaseReference;
    private DatePicker datePicker;
    private Button button;
    private FragmentSlideshowBinding binding;
    private String trimmedMail, name, password, contactNumber, day, month, year, div, dis, bg, picLink;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        name = SplashScreen.name;
        picLink = SplashScreen.picLink;
        div = SplashScreen.div;
        dis = SplashScreen.dis;
        bg = SplashScreen.bg;
        trimmedMail = SplashScreen.trimmedMail;
        day = SplashScreen.day;
        month = SplashScreen.month;
        year = SplashScreen.year;
        contactNumber = SplashScreen.contactNumber;
        textView = binding.updateDonationDateTextView;
        datePicker = binding.updateDonationDateDatePicker;
        button = binding.updateDonationDateButton;


        datePicker.setMaxDate(System.currentTimeMillis());


        if (Integer.parseInt(year) == 1800) {
            textView.setText("You haven't entered the date of donation yet.");
//            datePicker.init(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth(),null);
        } else {
            datePicker.init(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day), null);

        }
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                textView.setText("Selected date : " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("donors").child(div).child(dis).child(bg).child(trimmedMail).
                        child("day").setValue(datePicker.getDayOfMonth() + "");
                databaseReference.child("users").child(trimmedMail).child("day").setValue(datePicker.getDayOfMonth() + "");
                databaseReference.child("donors").child(div).child(dis).child(bg).child(trimmedMail).
                        child("month").setValue(datePicker.getMonth() + 1 + "");
                databaseReference.child("users").child(trimmedMail).child("month").setValue(datePicker.getMonth() + 1 + "");
                databaseReference.child("donors").child(div).child(dis).child(bg).child(trimmedMail).
                        child("year").setValue(datePicker.getYear() + "");
                databaseReference.child("users").child(trimmedMail).child("year").setValue(datePicker.getYear() + "");

                Toast.makeText(getContext(), "Updated !", Toast.LENGTH_SHORT).show();
                textView.setText("Donation Date : " + datePicker.getDayOfMonth() + "/" + (datePicker.getMonth() + 1) + "" + "/" + datePicker.getYear());
                startActivity(new Intent(getContext(), SplashScreen.class));
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