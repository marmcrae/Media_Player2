package com.example.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer mediaPlayer;
    private ImageView artistView;
    private ImageView dotIV, dotIV2;
    private TextView songName;
    private TextView artistName;
    private TextView leftTime;
    private TextView rightTime;
    private SeekBar seekbar;
    private Button rewindButton, fwdButton, playButton, repeatButton, shuffleButton;
    private Thread thread;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpUI();
        backgroundGradient();



        seekbar.setMax(mediaPlayer.getDuration());
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser) {
                    mediaPlayer.seekTo(progress);
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat(("mm:ss"));
                int currentPos = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();

                leftTime.setText(dateFormat.format(new Date(currentPos)));
                rightTime.setText(dateFormat.format(new Date(duration - currentPos)));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void backgroundGradient() {

        ConstraintLayout constraintLayout = findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();


    }

    public void setUpUI() {


        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.song);

        artistView = (ImageView) findViewById(R.id.artistImageID);
        dotIV = (ImageView) findViewById(R.id.dotImageViewID);
        dotIV2 = (ImageView) findViewById(R.id.dotImageViewID2);
        leftTime = (TextView) findViewById(R.id.startTimeTV);
        rightTime = (TextView) findViewById(R.id.endTimeTV);
        songName = (TextView) findViewById(R.id.songNameTV);
        artistName = (TextView) findViewById(R.id.artistNameTV);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        rewindButton = (Button) findViewById(R.id.rewindButtonID);
        playButton = (Button) findViewById(R.id.playpauseButtonID);
        fwdButton = (Button) findViewById(R.id.fwdButtonID);
        repeatButton = (Button) findViewById(R.id.repeatButtonID);
        shuffleButton = (Button) findViewById(R.id.shuffleButtonID);



        rewindButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        fwdButton.setOnClickListener(this);
        repeatButton.setOnClickListener(this);
        shuffleButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rewindButtonID:
                backMusic();




                break;

            case R.id.playpauseButtonID:
                if (mediaPlayer.isPlaying()){
                    pauseMusic();
                }else {
                    startMusic();
                }


                break;


            case R.id.fwdButtonID:
                nextMusic();



                break;

            case R.id.repeatButtonID:
                if (mediaPlayer.isLooping()){
                    mediaPlayer.setLooping(false);
                    dotIV.setVisibility(View.INVISIBLE);
                }else{
                    repeatMusic();
                }


                break;
        }


    }

    public void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            playButton.setBackgroundResource(android.R.drawable.ic_media_play);


        }

    }

    public  void startMusic(){

        if (mediaPlayer != null) {
            mediaPlayer.start();
            updateThread();
            playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
        }


    }

    public void backMusic() {

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(0);
        }
    }

    public void nextMusic() {

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(mediaPlayer.getDuration()-100);
        }

    }


    public void repeatMusic() {

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.setLooping(true);
            dotIV.setVisibility(View.VISIBLE);
            updateThread();
        }


    }

    public void updateThread() {

        thread = new Thread() {
            @Override
            public void run() {
                try {

                    while (mediaPlayer !=null && mediaPlayer.isPlaying()) {
                        Thread.sleep(50);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                int newPosition = mediaPlayer.getCurrentPosition();
                                int newMax = mediaPlayer.getDuration();
                                seekbar.setMax(newMax);
                                seekbar.setProgress(newPosition);

                                leftTime.setText(String.valueOf(new java.text.SimpleDateFormat("mm:ss").format(new Date(mediaPlayer.getCurrentPosition()))));
                                rightTime.setText(String.valueOf(new java.text.SimpleDateFormat("mm:ss").format(new Date(mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition()))));
                            }
                        });

                    }

                }catch (InterruptedException e) {

                }
            }
        };
        thread.start();

    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer =  null;

        }
        thread.interrupt();
        thread = null;

        super.onDestroy();
    }
}
