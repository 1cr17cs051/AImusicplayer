package com.example.aimusicplayer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class SmartPlayerActivity extends AppCompatActivity {


    private RelativeLayout parentRelativeLayout;

    private ImageView pausePlayBtn,nextBtn,previousBtn;

    private TextView songNameTxt;

    private ImageView imageView;

    private RelativeLayout lowerRelativeLayout;

    private Button voiceEnabledBtn;

    private SpeechRecognizer speechRecognizer;

    private Intent speechRecogniserIntent;
    private String keeper = "";

    private String mode = "ON";

    private MediaPlayer myMediaPlayer;

    private int position;
    private ArrayList<File> mySongs;
    private String mSongsName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_player);

        checkVoiceCommandPermission();


        pausePlayBtn = findViewById(R.id.play_pause_btn);
        nextBtn = findViewById(R.id.next_btn);
        previousBtn = findViewById(R.id.previous_btn);
        imageView = findViewById(R.id.logo);

        lowerRelativeLayout = findViewById(R.id.lower);
        voiceEnabledBtn = findViewById(R.id.voice_enable_btn);
        songNameTxt = findViewById(R.id.songName);

        parentRelativeLayout = findViewById(R.id.parentRelativeLayout);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(SmartPlayerActivity.this);
        speechRecogniserIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecogniserIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecogniserIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());


        validateRecieveValuesStartPlaying();
        imageView.setBackgroundResource(R.drawable.logo);


        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {

                ArrayList<String> matchesFound = results.getStringArrayList(speechRecognizer.RESULTS_RECOGNITION);

                if(matchesFound != null){

                    if(mode.equals("ON")){

                        keeper = matchesFound.get(0);


                        if(keeper.equals("stop the song")){

                            playPauseSong();
                            Toast.makeText(SmartPlayerActivity.this, "Command = "+keeper, Toast.LENGTH_LONG).show();

                        }
                        else if(keeper.equals("play the song")){

                            playPauseSong();
                            Toast.makeText(SmartPlayerActivity.this, "Command = "+keeper, Toast.LENGTH_LONG).show();

                        }

                        else if(keeper.equals("play next song")){

                            playNextSong();
                            Toast.makeText(SmartPlayerActivity.this, "Command = "+keeper, Toast.LENGTH_LONG).show();

                        }

                        else if(keeper.equals("play previous song")){

                            playPreviousSong();
                            Toast.makeText(SmartPlayerActivity.this, "Command = "+keeper, Toast.LENGTH_LONG).show();

                        }

                    }
                }

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });



        parentRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){

                    case MotionEvent.ACTION_DOWN:
                        speechRecognizer.startListening(speechRecogniserIntent);
                        keeper = "";
                        break;

                    case MotionEvent.ACTION_UP:

                        speechRecognizer.stopListening();
                        break;

                }

                return false;

            }
        });

        voiceEnabledBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode.equals("ON")){

                    mode = "OFF";
                    voiceEnabledBtn.setText("Voice Enable MOde - OFF");
                    lowerRelativeLayout.setVisibility(View.VISIBLE);
                }
                else{

                    mode = "ON";
                    voiceEnabledBtn.setText("Voice Enable MOde - ON");
                    lowerRelativeLayout.setVisibility(View.GONE);
                }
            }
        });


        pausePlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPauseSong();
            }
        });


        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myMediaPlayer.getCurrentPosition()>0){

                    playPreviousSong();
                }


            }
        });



        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myMediaPlayer.getCurrentPosition()>0){

                    playNextSong();
                }


            }
        });

    }


    private void validateRecieveValuesStartPlaying(){

        if(myMediaPlayer != null){
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        mySongs = (ArrayList) bundle.getParcelableArrayList("song");
        mSongsName = mySongs.get(position).getName();
        String songName = intent.getStringExtra("name");

        songNameTxt.setText(songName);
        songNameTxt.setSelected(true);


        position = bundle.getInt("position", 0);
        Uri uri = Uri.parse(mySongs.get(position).toString());



        myMediaPlayer = MediaPlayer.create(SmartPlayerActivity.this, uri);
        myMediaPlayer.start();



    }



    private void checkVoiceCommandPermission(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if(!(ContextCompat.checkSelfPermission(SmartPlayerActivity.this, Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED)){

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" +getPackageName()));
                startActivity(intent);
                finish();

            }

        }

    }



    private void playPauseSong(){

        imageView.setBackgroundResource(R.drawable.four);

        if(myMediaPlayer.isPlaying()){

            pausePlayBtn.setImageResource(R.drawable.play);
            myMediaPlayer.pause();
        }
        else{

            pausePlayBtn.setImageResource(R.drawable.pause);
            myMediaPlayer.start();

            imageView.setBackgroundResource(R.drawable.five);
        }
    }


    private void playNextSong(){

        myMediaPlayer.pause();
        myMediaPlayer.stop();
        myMediaPlayer.release();

        position = ((position+1)%mySongs.size());


        Uri uri =  Uri.parse(mySongs.get(position).toString());

        myMediaPlayer = MediaPlayer.create(SmartPlayerActivity.this,uri);

        mSongsName = mySongs.get(position).toString();
        songNameTxt.setText(mSongsName);
        myMediaPlayer.start();

        imageView.setBackgroundResource(R.drawable.three);

        if(myMediaPlayer.isPlaying()){

            pausePlayBtn.setImageResource(R.drawable.pause);
        }
        else{

            pausePlayBtn.setImageResource(R.drawable.play);

            imageView.setBackgroundResource(R.drawable.five);
        }

    }


    private void playPreviousSong(){

        myMediaPlayer.pause();
        myMediaPlayer.stop();
        myMediaPlayer.release();

        position = ((position-1)<0 ?(mySongs.size()-1) : (position-1));

        Uri uri =  Uri.parse(mySongs.get(position).toString());

        myMediaPlayer = MediaPlayer.create(SmartPlayerActivity.this,uri);

        mSongsName = mySongs.get(position).toString();
        songNameTxt.setText(mSongsName);
        myMediaPlayer.start();

        imageView.setBackgroundResource(R.drawable.two);

        if(myMediaPlayer.isPlaying()){

            pausePlayBtn.setImageResource(R.drawable.pause);
        }
        else{

            pausePlayBtn.setImageResource(R.drawable.play);

            imageView.setBackgroundResource(R.drawable.five);
        }

    }

}
