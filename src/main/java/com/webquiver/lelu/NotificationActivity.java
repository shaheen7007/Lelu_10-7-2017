package com.webquiver.lelu;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.webquiver.lelu.adapters.NotificationAdapter;
import com.webquiver.lelu.classes.Config;
import com.webquiver.lelu.classes.NotificationItem;
import com.webquiver.lelu.classes.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {


    SharedPreferences pref;
    SharedPreferences.Editor editor_pref;
    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        pref = this.getSharedPreferences(SessionManagement.PREF_NAME, Context.MODE_PRIVATE);
        editor_pref=pref.edit();
        listView=(ListView) findViewById(R.id.listview);


        try {
            callNotificList();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ImageView bck=(ImageView)findViewById(R.id.bk_id);
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }

    private void callNotificList() throws JSONException{

        JsonArrayRequest bannerjsonArrayRequest = new JsonArrayRequest("https://api.myjson.com/bins/1cly9t",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<NotificationItem> NotifList = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject myobj = null;
                            try {
                                myobj = response.getJSONObject(i);
                                NotificationItem meM = new NotificationItem();
                                meM.notifDate = myobj.getString("notif_date");
                                meM.notifSub = myobj.getString("notif_sub");
                                meM.notifDesc = myobj.getString("notif_des");
                                NotifList.add(meM);
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }


                        }
                        NotificationAdapter listAdapter = new NotificationAdapter(NotificationActivity.this,NotifList);
                        listView.setAdapter(listAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NotificationActivity.this, "Please check your internet connection", Toast.LENGTH_LONG).show();

//                        Toast.makeText(MemoActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                    });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding our request to the queue
        requestQueue.add(bannerjsonArrayRequest);









    }

}
