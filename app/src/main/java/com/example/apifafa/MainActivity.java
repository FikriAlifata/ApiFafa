package com.example.apifafa;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.apifafa.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText kotaEdt;
    TextView resultTv;
    Button btnOk;
    String namaKota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        kotaEdt = findViewById(R.id.kota);
        resultTv = findViewById(R.id.result_tv);
        btnOk = findViewById(R.id.btn_ok);

        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btn_ok){
            namaKota = kotaEdt.getText().toString();
            try {
                getData();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

    }
    public void getData() throws MalformedURLException {
        Uri uri = Uri.parse("https://https://datausa.io/api/data?drilldowns=State&measures=Population&year=latest")
                .buildUpon().build();
        URL url = new URL(uri.toString());
        new DoTask().execute(url);

    }
    class DoTask extends AsyncTask<URL,Void,String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String data = null;
            try {
                data = NetworkUtils.makeHTTPRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s){
            try {
                parseJson(s);
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
        public void parseJson(String data) throws JSONException {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(data);
            }catch (JSONException e){
                e.printStackTrace();
            }
            JSONArray kotaArray = jsonObject.getJSONArray("data");

            for (int i=0; i<kotaArray.length();i++){
                JSONObject kota1 = kotaArray.getJSONObject(i);
                String kotan = kota1.get("State").toString();
                if (kotan.equals(namaKota)){
                    String population = kota1.get("Population").toString();
                    resultTv.setText(population);
                    break;
                }
                else {
                    resultTv.setText("Tidak Ditemukan");
                }

            }

        }
    }
}