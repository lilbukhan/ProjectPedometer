package com.example.pedometer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pedometer.databinding.ActivityCalendarBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class calendar extends AppCompatActivity {

    private ActivityCalendarBinding binding;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCalendarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SimpleDateFormat dateFormat = new SimpleDateFormat("M-d");
        Date currentDate = new Date();
        date = dateFormat.format(currentDate);


        binding.btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(calendar.this, MainActivity.class));
            }
        });

        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = String.valueOf(month+1)+"-"+String.valueOf(dayOfMonth);
                loadUserInfo(date);


            }
        });


    }
    public void loadUserInfo(String date2){

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("data").child(date2).getValue() == null) {
                            binding.textView19.setText("Clear");
                            binding.textView21.setText("Clear");

                        }else {
                            binding.textView19.setText((snapshot.child("data").child(date2).getValue().toString()));
                            binding.textView21.setText(String.valueOf(Float.valueOf(snapshot.child("data").child(date2).getValue().toString())*0.03));


                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


    }
}