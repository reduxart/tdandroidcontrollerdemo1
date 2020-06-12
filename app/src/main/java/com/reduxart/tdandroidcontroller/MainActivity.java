package com.reduxart.tdandroidcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.reduxart.tdandroidcontroller.network.SendMessage;
import com.reduxart.tdandroidcontroller.network.UdpThread;
import com.reduxart.tdandroidcontroller.utils.Constants;

import java.io.IOException;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;
    InetAddress ipAddress = null;

    private TextView currentX, currentY, currentZ, currentIp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        try {
            ipAddress = SendMessage.initilizeBroadcastAddress(getApplicationContext());
            currentIp.setText("Current IP: " + ipAddress.getHostAddress() + " Port: " + Constants.PORT);
            UdpThread newTask = new UdpThread(ipAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            // fai! we dont have an accelerometer!
        }
    }

    @SuppressLint("CutPasteId")
    public void initializeViews() {
        currentX = (TextView) findViewById(R.id.currentX);
        currentY = (TextView) findViewById(R.id.currentY);
        currentZ = (TextView) findViewById(R.id.currentZ);
        currentIp = (TextView) findViewById(R.id.currentIp);
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_STATUS_UNRELIABLE);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void sendPercentageData() {
        String message = "PARAM1:" + deltaX + " PARAM2:" + deltaY + " PARAM3:" + deltaZ;
        UdpThread.SendMesssage(message);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        displayCleanValues();
        displayCurrentValues();
        deltaX = event.values[0];
        deltaY = event.values[1];
        deltaZ = event.values[2];
        sendPercentageData();
    }

    public void displayCleanValues() {
        currentX.setText("0.0");
        currentY.setText("0.0");
        currentZ.setText("0.0");
    }

    public void displayCurrentValues() {
        currentX.setText(Float.toString(deltaX));
        currentY.setText(Float.toString(deltaY));
        currentZ.setText(Float.toString(deltaZ));
    }

}
