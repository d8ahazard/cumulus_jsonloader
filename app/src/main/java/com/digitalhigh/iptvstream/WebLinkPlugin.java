package com.digitalhigh.iptvstream;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.felkertech.cumulustv.plugins.CumulusChannel;
import com.felkertech.cumulustv.plugins.CumulusTvPlugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class WebLinkPlugin extends CumulusTvPlugin {
    private static final String TAG = WebLinkPlugin.class.getSimpleName();
    private String JSONinput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setLabel("");
        setProprietaryEditing(false);
    }

    public void importJson() {
        Log.d(TAG, "ImportJSON called.");
        try {
            JSONinput = new JsonTask().execute("http://FOO/IPTV/app.php?JSON=true").get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        JSONObject json;
        try {
            json = new JSONObject(JSONinput);
            JSONArray channels = json.getJSONArray("channels");
            for (int i = 0; i < channels.length(); i++) {
                JSONObject value = channels.getJSONObject(i);
                Log.d(TAG, "Channel: " + value.getString("name"));
                CumulusChannel c = new CumulusChannel.Builder()
                        .setName(value.getString("name"))
                        .setNumber(value.getString("number"))
                        .setMediaUrl(value.getString("url"))
                        .setEpgUrl(value.getString("epgUrl"))
                        .setLogo(value.getString("logo"))
                        .setSplashscreen(value.getString("splashscreen"))
                        .setGenres(value.getString("genres"))
                        .build();
                finish();
                saveChannel(c);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                Log.d(TAG, "Connecting to URL " + url);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
                }

                return buffer.toString();


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String JSONout) {
            super.onPostExecute(JSONout);
        }
    }
}

