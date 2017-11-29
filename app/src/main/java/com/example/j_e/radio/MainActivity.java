package com.example.j_e.radio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends AppCompatActivity {

    Button btnPlay;
    MediaPlayer mediaPlayer;
    String stream = "http://stream.radioreklama.bg:80/radiorock128";

    boolean prepared = false;
    boolean started = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = (Button)findViewById(R.id.btnPlay);
        btnPlay.setEnabled(false);
        btnPlay.setText("LOADING");

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        new PlayerTask().execute(stream);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (started)
                {
                    started = false;
                    mediaPlayer.pause();
                    btnPlay.setText("PLAY");
                }
                else
                    {
                     started = true;
                        mediaPlayer.start();
                        btnPlay.setText("PAUSE");
                    }
            }
        });
    }

    private class PlayerTask extends AsyncTask<String, Void, Boolean>
    {

        @Override
        protected Boolean doInBackground(String... strings)
        {
            try
            {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.prepare();
                prepared = true;
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            btnPlay.setEnabled(true);
            btnPlay.setText("PLAY");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (started)
        {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (prepared)
        {
            mediaPlayer.release();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (started)
        {
            mediaPlayer.start();
        }
    }
}
