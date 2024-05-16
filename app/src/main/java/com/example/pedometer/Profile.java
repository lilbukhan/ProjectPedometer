package com.example.pedometer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pedometer.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Profile extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private String name=null;
    private String weight =null;
    private String height=null;
    private int TotalSteps=0;
    private float TotalKalor=0;

    public ArrayList<String> data = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadUserInfo();


        binding.imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, MainActivity.class));
            }
        });

        binding.exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Profile.this, activity_login.class));
            }
        });


    }





    public void loadUserInfo(){

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                            name = snapshot.child("Name").getValue().toString();
                            weight = snapshot.child("Weight").getValue().toString();
                            height = snapshot.child("Height").getValue().toString();



                        if(snapshot.child("data").getValue().toString().isEmpty()) {

                        }else {
                            String[] d = snapshot.child("data").getValue().toString().replace("{", "")
                                    .replace("}", "").split(",");

                            for (int i = 0; i < d.length; i++) {
                                String[] tt = d[i].split("=");
                                data.add(tt[1]);
                            }

                            for (int i = 0; i < data.size(); i++) {
                                TotalSteps += Integer.valueOf(data.get(i));
                            }

                            TotalKalor = (float) (TotalSteps * 0.03);
                            binding.textView16.setText((String.valueOf(TotalSteps)));
                            binding.textView17.setText((String.valueOf(TotalKalor)));
                        }




                        binding.tvName.setText(name);
                        binding.textView9.setText(height);
                        binding.textView14.setText(weight);




                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


    }



}