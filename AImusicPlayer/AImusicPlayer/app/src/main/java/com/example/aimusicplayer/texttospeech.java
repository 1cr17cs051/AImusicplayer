package com.example.aimusicplayer;

import android.os.Build;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Locale;

public class texttospeech extends AppCompatActivity {

    private TextToSpeech myTTS;
    private SpeechRecognizer mySpeechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texttospeech);
    }

    private void speak(String message){
        if(Build.VERSION.SDK_INT>=21){
            myTTS.addSpeech(message,TextToSpeech.QUEUE_FLUSH,null,null);
        }
    }

    private void initializeTextToSpeech(){
        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(texttospeech.this,"There is no tts engine in your device",Toast.LENGTH_LONG
                    ).show();
                    finish();
                }else{
                    myTTS.setLanguage(Locale.US);
                    speak("Hello: i am ready.");
                }
            }
        });
    }
}
