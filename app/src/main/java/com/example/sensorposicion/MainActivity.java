package com.example.sensorposicion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText position;
    private SensorManager sm;
    private Sensor sa;
    private SensorEventListener SEL;
    private int latigo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        position=findViewById(R.id.et_position);
        sm=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sa = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if(sa==null){
            Toast.makeText(this, "No hay sensor", Toast.LENGTH_SHORT).show();
        }else{
            SEL = new SensorEventListener() {
                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
                @Override
                public void onSensorChanged(SensorEvent event) {
                    float x = event.values[0];
                    position.setText(Float.toString(x));
                    if(x > -5 && latigo == 0){
                        latigo++;
                        getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                    }else if(x < 5 && latigo == 1) {
                        latigo++;
                        getWindow().getDecorView().setBackgroundColor(Color.RED);
                    }

                    if(latigo == 2){
                        sonido();
                        latigo = 0;
                    }
                }

            };
        }
    }
    private void sonido(){
        MediaPlayer mp = MediaPlayer.create(this, R.raw.latigo);
        mp.start();
    }

    public void iniciar(){
        sm.registerListener(SEL, sa, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onResume(){
        iniciar();
        super.onResume();
    }

    public void Detener(){
        sm.unregisterListener(SEL);
    }

    @Override
    protected void onStop(){
        Detener();
        super.onStop();
    }
}