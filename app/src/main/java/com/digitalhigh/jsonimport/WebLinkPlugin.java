package com.digitalhigh.jsonimport;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class WebLinkPlugin extends CumulusTvPlugin {
    private static final String TAG = WebLinkPlugin.class.getSimpleName();
    private String JSONinput;
    private ProgressBar pb;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        setLabel("");
        setProprietaryEditing(false);
        pb = (ProgressBar) findViewById(R.id.progressBar2);
        pb.setVisibility(View.INVISIBLE);
        Button inputButton = (Button) findViewById(R.id.button2);
        final EditText et = (EditText) findViewById(R.id.editText);
        settings = getSharedPreferences("app", 0);
        String lastUrl = settings.getString("url","");
        if (!Objects.equals(lastUrl, "")) et.setText(lastUrl);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0) {

                    SharedPreferences.Editor editor=settings.edit();
                    editor.putString("url", s.toString());
                    editor.apply();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"It's the button, dude.");
                String inUrl = et.getText().toString().trim();
                Log.d(TAG,"URL "+ inUrl);
                if (URLUtil.isValidUrl(inUrl)) {
                    pb.setVisibility(View.VISIBLE);
                    importJson(inUrl);
                } else {
                    Toast.makeText(getApplicationContext(),"Invalid URL provided.",Toast.LENGTH_LONG).show();


                }
            }
        });

    }

    public void importJson(String fetchUrl) {
        Log.d(TAG, "ImportJSON called.");
        try {
            JSONinput = new JsonTask().execute(fetchUrl).get();
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

                CumulusChannel.Builder b = new CumulusChannel.Builder()
                        .setName(value.getString("name"))
                        .setNumber(value.getString("number"))
                        .setMediaUrl(value.getString("url"))
                        .setLogo(value.getString("logo"))
                        .setSplashscreen(value.getString("splashscreen"))
                        .setGenres(value.getString("genres"));
                if (value.has("epgUrl")) b.setEpgUrl(value.getString("epgUrl"));
                CumulusChannel c = b.build();
                finish();
                saveChannel(c);
            }
        } catch (NullPointerException | JSONException e) {
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
            pb.setVisibility(View.INVISIBLE);
        }
    }
}

