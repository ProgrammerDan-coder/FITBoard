package com.example.fitboard;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class NewsActivity extends AppCompatActivity {

    private EditText logo_field;
    private Button logo_btn;
    private TextView result_text_logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        logo_btn = findViewById(R.id.logo_btn);
        logo_field = findViewById(R.id.user_URL);
        result_text_logo = findViewById(R.id.result_info);
        logo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(logo_field.getText().toString().trim().equals(""))
                    Toast.makeText(NewsActivity.this, R.string.no_logo_input, Toast.LENGTH_SHORT).show();
                else {
                    String city = logo_field.getText().toString();
                    String key = "04da0f6b525f45f4cf44d315216d062e";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q="+ city +"&appid=" + key + "&units=metric"; //&lang=ru

                    new GetURLData().execute(url);
                }
            }
        });
    }
    private class GetURLData extends AsyncTask<String, String, String>{
        protected void onPreExecute(){
            super.onPreExecute();
            result_text_logo.setText("Wait");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                while((line = reader.readLine()) != null)
                    buffer.append(line).append("\n");
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(connection != null)
                    connection.disconnect();

                try {
                    if (reader != null)
                        reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            return null;
        }
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                result_text_logo.setText("Temperature: " + jsonObject.getJSONObject("main").getDouble("temp"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
