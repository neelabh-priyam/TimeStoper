package com.e.timestoper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private TextView timeView;

    private Button startButton;
    private Button stopButton;
    private Button playPauseButton;

    private long miliSecondsPassed;

    private boolean running;
    private boolean wasRunning;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeView = (TextView) findViewById(R.id.timeLable);

        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        playPauseButton = (Button) findViewById(R.id.playPauseButton);

        if (savedInstanceState == null) {
            miliSecondsPassed = 0;
            running = false;
            wasRunning = false;
        } else {
            miliSecondsPassed = savedInstanceState.getLong("miliSecondsPasses");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }

        runTimer();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong("miliSecondsPassed", miliSecondsPassed);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putBoolean("wasRunning", wasRunning);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    protected void onResume() {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }

    public void onClickStart(View view)  {
        startButton.setVisibility(View.INVISIBLE);
        playPauseButton.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.VISIBLE);
        miliSecondsPassed = 0;
        running = true;
    }

    public void onClickPlayPause(View view) {
        if(running == true) {
            running = false;
            playPauseButton.setText("RESUME");
        } else {
            running = true;
            playPauseButton.setText("PAUSE");
        }
    }

    public void onClickStop(View view) {
        startButton.setVisibility(View.VISIBLE);
        playPauseButton.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.INVISIBLE);
        running = false;
        miliSecondsPassed = 0;
    }

    private void runTimer()  {
        final Handler handler = new Handler();

        handler.post(new Runnable() {

            @Override
            public void run() {
                long miliSeconds = miliSecondsPassed % 10;
                long seconds = (miliSecondsPassed / 10) % 60;
                long minutes = (seconds / 60) % 60;

                String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", minutes, seconds, miliSeconds);
                timeView.setText(time);

                if (running) {
                    miliSecondsPassed++;
                }

                handler.postDelayed(this, 100);
            }
        });
    }

    public void onClickInfo(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        alert.setTitle("Info");
        alert.setMessage("Created By: Neelabh Priyam Jha\nRepo: TimeStoper at github.com\nhttps://github.com/neelabh-priyam");
        alert.setCancelable(true);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

}
