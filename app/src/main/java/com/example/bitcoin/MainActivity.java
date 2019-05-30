package com.example.bitcoin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String BPI_ENDPOINT="https://api.coindesk.com/v1/bpi/currentprice.json";
    private OkHttpClient okHttpClient=new OkHttpClient();
    private ProgressDialog progressDialog;
    private TextView displayText;
    private Button btnLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayText=(TextView)findViewById(R.id.txt);
        btnLoad=(Button)findViewById(R.id.btnload);



        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if connection available then load price
                if(checkInternetConnection()){
                    Intent i=new Intent(MainActivity.this,CustomActivity.class);
                    startActivity(i);
                    // loadPrice();
            }
            }
        });
        }

        private boolean checkInternetConnection(){
            ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            //if no info received
            if(netInfo==null){
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getResources().getString(R.string.app_name))
                        .setMessage("No internet Connection")
                        .setPositiveButton("ÖK",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.dismiss();
                            }
                           }).show();
                            return false;
                        }
                        else {
                            return true;

                    }}

        public  void loadPrice(){
            final Request request = new Request.Builder().url(BPI_ENDPOINT).build();

                progressDialog.show();

                new OkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(MainActivity.this, "Ërror loading BPI:" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String body=response.body().string();
                         runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                 progressDialog.dismiss();
            //                     parseBPIResponse(body);
                    }
                });
                    }
                });
            }
          /*private void parseBPIResponse(String body){
            StringBuilder builder= new StringBuilder();
              try{
                    JSONObject jsonObject=null;

                    JSONObject timeObject = jsonObject.getJSONObject("time");
                    builder.append(timeObject.getString("updated")).append("/n/n");

                    JSONObject BpiObject = jsonObject.getJSONObject("bpi");
                    JSONObject usdObject = BpiObject.getJSONObject("USD");
                    builder.append(usdObject.getString("rate")).append("$").append("/n");

                    JSONObject gbpObject = BpiObject.getJSONObject("GBP");
                    builder.append(gbpObject.getString("rate")).append("£").append("/n");

                    JSONObject euroObject = BpiObject.getJSONObject("EUR");
                    builder.append(euroObject.getString("rate")).append("€").append("/n");

                    displayText.setText(builder.toString());

                }
                catch (JSONException e){
                     e.printStackTrace();
                        }
                    }*/
}