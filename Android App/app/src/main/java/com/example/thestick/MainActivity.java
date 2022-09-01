package com.example.thestick;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    static String TAG = "TEST_CASE";
    private ImageView image;
    private AnimatedVectorDrawable animation;
    private float degree = 0;
    private boolean doubleBackToExitPressedOnce = false;


    String address = "98:D3:21:F7:3D:E3";  // Hardcoded address - unique to each HC-05
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    MediaPlayer rightSound, leftSound, stopSound, normalSound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = findViewById(R.id.main_button);
        Drawable d = image.getDrawable();
        if(d instanceof  AnimatedVectorDrawable)
        {
            animation = (AnimatedVectorDrawable) d;
            animation.start();
        }
        image.setRotation(degree);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartDirecting();
            }
        });


        leftSound = MediaPlayer.create(this, R.raw.left);
        rightSound = MediaPlayer.create(this, R.raw.right);
        stopSound = MediaPlayer.create(this, R.raw.stop);
        normalSound = MediaPlayer.create(this, R.raw.normal);


    }

    // go to Direction Activity
    private void StartDirecting()
    {

        new MainActivity.ConnectBT().execute();

        Intent intent = new Intent(this, Direction.class);
        startActivity(intent);
    }

    // double button press to exit
    @Override
    public void onBackPressed()
    {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);


        Disconnect();

    }


    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected  void onPreExecute () {
            progress = ProgressDialog.show(MainActivity.this, "Connecting...", "Please Wait!!!");
        }

        @Override
        protected Void doInBackground (Void... devices) {
            try {
                if ( btSocket==null || !isBtConnected ) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute (Void result) {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                System.out.println("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            } else {
                System.out.println("Connected");
                isBtConnected = true;
            }
            sampleDistance();
            progress.dismiss();
        }
    }

    private void sampleDistance() {
        Thread readThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if ( btSocket != null ) {
                    int rotations=0;
                    boolean compass = true;
                    while(true) {
                        try {
                            Thread.sleep(100);
                            //sendSignal("1");
                            System.out.println("SENT :"+1);
                            char c = (char) btSocket.getInputStream().read();
                            Log.d(TAG, "run: the  value of C received: " + c);
                            System.out.println("here1");
                            //char r = (char) btSocket.getInputStream().read();
                            System.out.println("here2");
                            char d = 'a';
                            //if(compass) d = (char) btSocket.getInputStream().read();

                            if( c=='S'){
                                stopSound.start();
                                Direction.degree = 0;
                                Direction.dir = "Stop";
                            }
                            else if( c=='L' ) {
                                leftSound.start();
                                Direction.degree = -90;
                                Direction.dir = "Turn Left";
                            }
                            else if( c=='R' ) {
                                rightSound.start();
                                Direction.degree = 90;
                                Direction.dir = "Turn Right";
                            }
                            else if( c=='F') {
                                Direction.degree = 0;
                                Direction.dir = "Move Forward";
                            }
                            else if(c=='N'){
                                normalSound.start();
                                Direction.degree = 0;
                                Direction.dir = "Move Forward";
                            }

                            System.out.println(c);
                            //if( r=='1' ) rotations++;

                        } catch (Exception e) {
                            System.out.println("Error in Receiving");
                        }
                    }
                }
            }
        }); readThread.start();
    }

    private void sendSignal ( String number ) {
        if ( btSocket != null ) {
            try {
                btSocket.getOutputStream().write(number.toString().getBytes());
            } catch (IOException e) {
                System.out.println("Error in Sending");
            }
        }
    }

    private void Disconnect () {
        if ( btSocket!=null ) {
            try {
                btSocket.close();
            } catch(IOException e) {
                System.out.println("Error in Disconnecting");
            }
        }
        finish();
    }

    // res/raw/left.mp3
    // res/raw/right.mp3

}