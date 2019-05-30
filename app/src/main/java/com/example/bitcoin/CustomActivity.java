package com.example.bitcoin;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomActivity extends AppCompatActivity {
    TextView textView;
    ListView listView;

    ArrayList<String> Time;
    ArrayList<String> Usd;
    ArrayList<String> Rate;
    ArrayList<String> Bpi;

    //String table="monday";
    Context ctx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        textView=(TextView)findViewById(R.id.textfetch);

        ctx = this;

        listView = (ListView) findViewById(R.id.listview);

        //table="monday";
        //getDay();

        //acday=new String();



        getMessages();

    }


    class CustomAdapter extends ArrayAdapter<String>
    {
        Context context;
        ArrayList<String> time;
        ArrayList<String> usd;
        ArrayList<String> rate;
        ArrayList<String> bpi;
        //ArrayList<String> lec;
        //String acday;


        CustomAdapter(Context c, ArrayList<String> time,ArrayList<String> usd, ArrayList<String> rate,ArrayList<String> bpi){
            // , ArrayList<String> MsgType
            super(c, R.layout.inflater, time );
            this.context = c;
            this.time= time;
            this.usd = usd;
            this.rate = rate;
            this.bpi =bpi;
            //this.lec=lec;
            //this.acday=acday;


        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row  = inflater.inflate(R.layout.inflater, parent, false);

            TextView txtTime = (TextView) row.findViewById(R.id.time);
            TextView txtUsd = (TextView) row.findViewById(R.id.usd);
            TextView txtRate = (TextView) row.findViewById(R.id.rate);
            TextView txtBpi = (TextView) row.findViewById(R.id.bpi);


            txtTime.setText(time.get(position));
            txtUsd.setText(usd.get(position));
            txtRate.setText(rate.get(position));
            txtBpi.setText(bpi.get(position));
            //txtlec.setText(lec.get(position));

            return row;
        }
    }


    private void getMessages(){

        Time = new ArrayList<>();
        Usd = new ArrayList<>();
        Rate = new ArrayList<>();
        Bpi = new ArrayList<>();
        //lec=new ArrayList<>();

        CustomAdapter adapter = new CustomAdapter(ctx, Time, Usd, Rate, Bpi);
        listView.setAdapter(adapter);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Refreshing ........");
        dialog.setCancelable(false);
        dialog.show();
        Toast.makeText(CustomActivity.this, "Welcome", Toast.LENGTH_SHORT).show();


        String url ="https://api.coindesk.com/v1/bpi/currentprice.json";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {
                        dialog.dismiss();
                        //Toast.makeText(CustomActivity.this,res,Toast.LENGTH_SHORT).show();


                        try {
                            JSONArray response = new JSONArray(res);

                            for (int i = 0; i <= response.length(); i++) {

                                try {

                                    JSONArray array = response;
                                    JSONObject object = array.getJSONObject(i);
                                    Time.add(object.getString("time"));
                                    Usd.add(object.getString("bpi"));
                                    Rate.add(object.getString("USD"));
                                    Bpi.add(object.getString("rate"));
                                    //lec.add(object.getString("lecturer"));


                                    CustomAdapter adapter = new CustomAdapter(ctx, Time, Usd, Bpi, Rate);
                                    //Timestp, Admin, MsgType
                                    listView.setAdapter(adapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CustomActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(CustomActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
             //   params.put("table", table);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}



