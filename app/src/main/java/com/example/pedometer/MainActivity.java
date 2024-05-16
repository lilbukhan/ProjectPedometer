package com.example.pedometer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pedometer.databinding.ActivityLoginBinding;
import com.example.pedometer.databinding.ActivityMainBinding;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements  SensorEventListener{

    private ActivityMainBinding binding;

    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private int stepCount =0;
    private int progress;

    private boolean isPause = false;
    private long timePaused=0;
    private float stepLengthInMeters = 0.762f;

    private long startTime;
    private int stepCountTarget = 10000;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");
    Date currentDate = new Date();
    private String date;
    public String temp ;

    public ArrayList<String> dataTask = new ArrayList<String>();
//    int i ;
    public String data;
    public ArrayList<String> dataLineChart = new ArrayList<String>();

    public ArrayList barArraylist;


    protected void onStop(){
        super.onStop();
        if(stepCounterSensor!= null){
            sensorManager.unregisterListener((SensorEventListener) this);
        }

    }


    @Override
    protected void onResume(){
        super.onResume();

        if(stepCounterSensor!= null){
            sensorManager.registerListener(this,stepCounterSensor,SensorManager.SENSOR_DELAY_NORMAL);

        }

    }

    private ArrayList<Entry> dataValue(){
        ArrayList<Entry> dataVal = new ArrayList<Entry>();
//        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                        .addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                if(snapshot.child("data").getValue().toString().isEmpty()) {
//
//                                }else {
//                                    String[] d = snapshot.child("data").getValue().toString().replace("{", "")
//                                            .replace("}", "").split(",");
//
//                                    for (int i = 0; i < d.length; i++) {
//                                        String[] tt = d[i].split("=");
//                                        dataLineChart.add(tt[1]);
//
//                                    }
//                                    for(int i =15;i>=9;i--){
//                                        dataTask.add(snapshot.child("data").child("5-"+String.valueOf(i)).getValue().toString());
//                                    }
//                                }
//
//                            }
//
//                            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//            });


//        for(int i =0;i< dataLineChart.size();i++){
//            dataVal.add(new Entry(i,Integer.valueOf(dataLineChart.get(i))));
//        }

//        for(int i =0;i< 5;i++){
//            dataVal.add(new Entry(i,Integer.valueOf(dataTask.get(i))));
//        }


        dataVal.add(new Entry(0,8000));
        dataVal.add(new Entry(1,9000));
        dataVal.add(new Entry(2,8500));
        dataVal.add(new Entry(3,12500));
        dataVal.add(new Entry(4,10850));
        dataVal.add(new Entry(5,10000));
        dataVal.add(new Entry(6,14550));
        return dataVal;

    }

    private ArrayList<Entry> dataValue2(){
        ArrayList<Entry> dataVal = new ArrayList<Entry>();
//        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                        .addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                if(snapshot.child("data").getValue().toString().isEmpty()) {
//
//                                }else {
//                                    String[] d = snapshot.child("data").getValue().toString().replace("{", "")
//                                            .replace("}", "").split(",");
//
//                                    for (int i = 0; i < d.length; i++) {
//                                        String[] tt = d[i].split("=");
//                                        dataLineChart.add(tt[1]);
//
//                                    }
//                                    for(int i =15;i>=9;i--){
//                                        dataTask.add(snapshot.child("data").child("5-"+String.valueOf(i)).getValue().toString());
//                                    }
//                                }
//
//                            }
//
//                            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//            });


//        for(int i =0;i< dataLineChart.size();i++){
//            dataVal.add(new Entry(i,Integer.valueOf(dataLineChart.get(i))));
//        }

//        for(int i =0;i< 5;i++){
//            dataVal.add(new Entry(i,Integer.valueOf(dataTask.get(i))));
//        }


        dataVal.add(new Entry(0,8000/100*35));
        dataVal.add(new Entry(1,9000/100*35));
        dataVal.add(new Entry(2,8500/100*35));
        dataVal.add(new Entry(3,12500/100*35));
        dataVal.add(new Entry(4,10850/100*35));
        dataVal.add(new Entry(5,10000/100*35));
        dataVal.add(new Entry(6,14550/100*35));
        return dataVal;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(MainActivity.this, activity_login.class));
        }



        binding.btnProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Profile.class));
            }
        });

        binding.calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,calendar.class));
            }
        });




        loadUserInfo();

        LineDataSet lds = new LineDataSet(dataValue(),"Steps");
        LineDataSet lds2 = new LineDataSet(dataValue2(),"Kal");
        lds2.setColor(Color.RED);
        lds2.setCircleColor(Color.RED);
        ArrayList<ILineDataSet> dataSet = new ArrayList<>();

        dataSet.add(lds);
        LineData dt = new LineData(dataSet);
        binding.lineChart.setData(dt);

        dataSet.add(lds2);
        LineData dt2 = new LineData(dataSet);
        binding.lineChart.setData(dt2);

        binding.lineChart.invalidate();








        startTime = System.currentTimeMillis();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        binding.pb.setMax(stepCountTarget);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            stepCount=(int) event.values[0];
            binding.textSteps.setText(stepCount);
            binding.pb.setProgress(stepCount);


            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("data").child(date)
                    .setValue(String.valueOf(stepCount));


        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public void loadUserInfo(){


        SimpleDateFormat dateFormat = new SimpleDateFormat("M-d");
        Date currentDate = new Date();
        date = dateFormat.format(currentDate);
        String sss = date.toString();
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("data").getValue() == null){
                            binding.textSteps.setText("");
                            binding.pb.setProgress(0);
                            binding.textView4.setText("");
                            binding.textView5.setText("");
                        }else {
                            DataSnapshot ft = snapshot.child("data").child(date);
                            temp = ft.getValue().toString();
                            data = snapshot.child("data").getValue().toString();

                            for(int i =15;i>=9;i--){
                                dataTask.add(snapshot.child("data").child("5-"+String.valueOf(i)).getValue().toString());
                            }

                            binding.textSteps.setText(temp);
                            binding.pb.setProgress(Integer.valueOf(temp));
                            binding.textView4.setText(String.valueOf(Float.valueOf(temp) * 0.03));
                            binding.textView5.setText(String.valueOf(Math.round(Float.valueOf(temp)*stepLengthInMeters/1000)));
                        }

//                        binding.textSteps.setText(temp);
//                        binding.pb.setProgress(Integer.valueOf(temp));
//                        binding.textView4.setText(String.valueOf(Float.valueOf(temp) * 0.03));
//                        binding.textView5.setText(String.valueOf(Math.round(Float.valueOf(temp)*stepLengthInMeters/1000)));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


    }

}