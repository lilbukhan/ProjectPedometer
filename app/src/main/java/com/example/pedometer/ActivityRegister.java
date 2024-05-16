package com.example.pedometer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pedometer.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ActivityRegister extends AppCompatActivity {


    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());


        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.emailReg.getText().toString().isEmpty() || binding.RegPass.getText().toString().isEmpty()
                        ||binding.RegName.getText().toString().isEmpty() || binding.RegHeight.getText().toString().isEmpty() ||
                        binding.RegWeight.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Fill in the blanks", Toast.LENGTH_SHORT).show();
                }else{
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.emailReg.getText().toString(),binding.RegPass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){

                                        HashMap<String, String> userInfo = new HashMap<>();
                                        userInfo.put("email",binding.emailReg.getText().toString());
                                        userInfo.put("Name", binding.RegName.getText().toString());
                                        userInfo.put("Weight", binding.RegWeight.getText().toString());
                                        userInfo.put("Height", binding.RegHeight.getText().toString());
                                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(userInfo);

                                        startActivity(new Intent(ActivityRegister.this,MainActivity.class));
                                    }
                                }
                            });
                }

            }
        });



        binding.goRoLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityRegister.this, activity_login.class));
            }
        });



    }
}