package com.example.madclassproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class RemoteContent extends AsyncTask<String, Void, String> {
    private String ResponseStatus;
    private String ResponseMessage;
    private String txt_singer, txt_song, txt_link;
    private Context CallingContext;

    RemoteContent(Context ct) {
        super();
        CallingContext = ct;
    }

    /// Background download of the page.
    @Override
    protected String doInBackground(String... urls) {
        String response = "";
        try {
            URL url = new URL(urls[0]);
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    response = readStream(in);
                } finally {
                    urlConnection.disconnect();
                }
            } catch (java.io.IOException ioex) {
                System.out.println("Exception Catched: Java IO");
            }
        } catch (MalformedURLException muex) {
            System.out.println("Exception Catched : Malformed URL");
        }
        return response;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Determine the application's actions according to the data sent by the control tower ///////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onPostExecute(String result) {
        ResponseStatus = getXMLContent(result, "<status>", "</status>", 0);
        ResponseMessage = getXMLContent(result, "<msg>", "</msg>", 0);

        if (ResponseStatus.equals("0-FAIL")) {
            System.out.println("HERE HERE");
            Toast.makeText(CallingContext, R.string.auth_fail, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CallingContext, LoginActivity.class);
            CallingContext.startActivity(intent);

        } else if (ResponseStatus.equals("0-OK")) {
            Toast.makeText(CallingContext, R.string.auth_ok, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CallingContext, MenuActivity.class);
            Bundle b = new Bundle();
            b.putString("msg", ResponseMessage);
            intent.putExtras(b);
            CallingContext.startActivity(intent);

        } else if (ResponseStatus.equals("1-FAIL")) {
            Toast.makeText(CallingContext, R.string.auth_fail, Toast.LENGTH_SHORT).show();
            ((LoginActivity)CallingContext).txt_auth.setText(ResponseMessage);
            ((LoginActivity)CallingContext).txt_auth.setVisibility(View.VISIBLE);

        } else if (ResponseStatus.equals("1-OK")) {
            // ((LoginActivity)CallingContext).text_auth_fail.setVisibility(View.INVISIBLE);
            ((LoginActivity) CallingContext).txt_auth.setText(ResponseMessage);
            ((LoginActivity) CallingContext).txt_auth.setVisibility(View.VISIBLE);
            ((LoginActivity) CallingContext).txt_login.setVisibility(View.INVISIBLE);
            ((LoginActivity) CallingContext).edit_email.setVisibility(View.INVISIBLE);
            ((LoginActivity) CallingContext).btn_submit.setVisibility(View.INVISIBLE);

        //Part 2 - Jukebox Activity
        } else if (ResponseStatus.equals("2-OK")) {

            try {
                //Call Player
                MediaPlayer player = ((JukeboxActivity)CallingContext).player;
                player.pause();
                //Call Song from CTower
                txt_singer = getXMLContent(ResponseMessage, "<artist>", "</artist>", 0);
                txt_song = getXMLContent(ResponseMessage, "<title>", "</title>", 0);
                txt_link = getXMLContent(ResponseMessage, "<url>", "</url>", 0);
                //Import text to placeholders
                ((JukeboxActivity)CallingContext).txt_singer.setText(txt_singer);
                ((JukeboxActivity)CallingContext).txt_song.setText(txt_song);
                ((JukeboxActivity)CallingContext).txt_link.setText(txt_link);
                //Import url to Player
                player.setDataSource(txt_link);
                //player.prepareAsync();
                player.prepare();
                player.start();
                //Play the song
                //Change Status
                ((JukeboxActivity)CallingContext).txt_status.setText(R.string.txt_playing);
                //Enable Next Button
                ((JukeboxActivity)CallingContext).btn_next.setEnabled(true);
                ((JukeboxActivity)CallingContext).btn_next.setBackgroundColor(Color.BLUE);
                //Enable Pause Button
                ((JukeboxActivity)CallingContext).btn_pause.setEnabled(true);
                ((JukeboxActivity)CallingContext).btn_pause.setBackgroundColor(Color.RED);
                //Disable Play Button
                ((JukeboxActivity)CallingContext).btn_play.setEnabled(false);
                ((JukeboxActivity)CallingContext).btn_play.setBackgroundColor(Color.LTGRAY);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }

    private String getXMLContent(String str, String XMLOpen, String XMLClose, int start) {
        String content = "";

        /// Get the message
        int p1 = str.indexOf(XMLOpen, start);
        int p2 = str.indexOf(XMLClose, p1);
        if (p1 > 0 && p2 > p1) {
            p1 += XMLOpen.length();
            content = str.substring(p1, p2);
        }
        return content;
    }


}
