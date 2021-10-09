package com.example.madclassproject;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class JukeboxActivity extends AppCompatActivity implements OnBufferingUpdateListener, OnCompletionListener {

    public Context JukeboxActivityContext;
    public MediaPlayer player;

    public TextView txt_singer, txt_song, txt_link, txt_status;
    public ImageButton btn_play, btn_pause, btn_next;
    public String token = MainActivity.token;

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        /** Method which updates the SeekBar secondary progress by current song loading from URL position*/
//        seekBarProgress.setSecondaryProgress(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        /** MediaPlayer onCompletion event handler. Method which calls then song playing is complete*/
//        buttonPlayPause.setImageResource(R.drawable.button_play);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jukebox);

        //Locators
        JukeboxActivityContext = this;
        txt_singer = findViewById(R.id.txt_singer);
        txt_song = findViewById(R.id.txt_song);
        txt_link = findViewById(R.id.txt_link);
        txt_status = findViewById(R.id.txt_status);
        btn_play = findViewById(R.id.btn_play);
        btn_pause = findViewById(R.id.btn_pause);
        btn_next = findViewById(R.id.btn_next);

        //Play and Pause buttons are initially disabled
        btn_play.setEnabled(false);
        btn_play.setBackgroundColor(Color.LTGRAY);
        btn_pause.setEnabled(false);
        btn_pause.setBackgroundColor(Color.LTGRAY);

        player = new MediaPlayer();
        player.setOnBufferingUpdateListener(this);
        player.setOnCompletionListener(this);

        //When user clicks the button Next, app requests song from CTower
        btn_next.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Disable Next Button
                        btn_next.setEnabled(false);
                        btn_next.setBackgroundColor(Color.LTGRAY);
                        //Change Status
                        txt_status.setText(R.string.txt_requesting);
                        //Stop player if it is active
                        if (player.isPlaying()) {
                            //player.seekTo(0);
                            //player.pause();
                            player.stop();
                            //player.release();
                            player.reset();
                        }
                            //Request song from CTower
                            RemoteContent MADMywork = new RemoteContent(JukeboxActivityContext);
                            String url = "http://mad.mywork.gr/get_song.php?t=" + token;
                            MADMywork.execute(url);
                    }
                }
        );

        //When user clicks the button Pause
        btn_pause.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Disable Pause Button
                        btn_pause.setEnabled(false);
                        btn_pause.setBackgroundColor(Color.LTGRAY);
                        //Enable Play Button
                        btn_play.setEnabled(true);
                        btn_play.setBackgroundColor(Color.GREEN);
                        //Change status
                        txt_status.setText(R.string.txt_stopped);
                        //Stop player if it is active
                        player.pause();
                    }
                }
        );

        //When user clicks the button Play
        btn_play.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Disable Play Button
                        btn_play.setEnabled(false);
                        btn_play.setBackgroundColor(Color.LTGRAY);
                        //Enable Pause Button
                        btn_pause.setEnabled(true);
                        btn_pause.setBackgroundColor(Color.RED);
                        //Change status
                        txt_status.setText(R.string.txt_playing);
                        //Play player
                        player.start();
                    }
                }
        );
    }

    //When the activity gets paused, stopped, or destroyed, the player must stop playing
    @Override
    protected void onStop() {
        super.onStop();
        if (player.isPlaying()) {
            //Enable Play button
            btn_play.setEnabled(true);
            btn_play.setBackgroundColor(Color.RED);
            //Disable Pause Button
            btn_pause.setEnabled(false);
            btn_pause.setBackgroundColor(Color.LTGRAY);
            //Change status
            txt_status.setText(R.string.txt_stopped);
            //Stop song
            player.stop();
        }

    }


}


