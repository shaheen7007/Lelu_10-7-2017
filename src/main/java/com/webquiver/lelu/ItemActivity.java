package com.webquiver.lelu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.viewpagerindicator.CirclePageIndicator;
import com.webquiver.lelu.adapters.Banner_Adapter;
import com.webquiver.lelu.adapters.GridViewAdapter;
import com.webquiver.lelu.classes.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ItemActivity extends AppCompatActivity {


    //images slider
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    ProgressBar progressBar;
    private ArrayList<String> images;

    public static final String ITEM_URL="https://api.myjson.com/bins/ujdqn";

   //sharedpreference
    public static final String ITEM_PREFERENCE="item_pref";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setCollapsible(false);
        setSupportActionBar(toolbar);

        //initialisation
        images = new ArrayList<>();
        progressBar=(ProgressBar)findViewById(R.id.progbr_id);
        sharedPreferences = this.getSharedPreferences(ITEM_PREFERENCE, MODE_PRIVATE);


        //getintent
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {

            Toast.makeText(this, String.valueOf(b.get("name")), Toast.LENGTH_LONG).show();
        }


        //custom toolbar
        ActionBar ab = getSupportActionBar();
        LayoutInflater li = LayoutInflater.from(this);
        View customView = li.inflate(R.layout.custombar2_itemactivity, null);
        ab.setCustomView(customView);
        ab.setDisplayShowCustomEnabled(true);



        //image slider
        getimgs();



    }

    //show image
    private void init(ArrayList<String> test) {
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new Banner_Adapter(ItemActivity.this, test));

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(3 * density);

        NUM_PAGES = test.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
       /*
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 5000);
        */

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }

//get image json
    public void getimgs() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(ITEM_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                         progressBar.setVisibility(View.INVISIBLE);
                        //ismissing the progressdialog on response
                                //loading.dismiss();


                        SharedPreferences.Editor prefEdit = sharedPreferences.edit();
                         String jsonstring=response.toString();
                        prefEdit.putString(Config.JSONSTRING1,jsonstring);
                        prefEdit.apply();
                        showimg(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                         progressBar.setVisibility(View.INVISIBLE);
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(sharedPreferences.getString(Config.JSONSTRING1, "NULL"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                          showimg(jsonArray);
                          Toast.makeText(ItemActivity.this,"No response from api",Toast.LENGTH_LONG).show();
                    }
                }
        );
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding our request to the queue
        requestQueue.add(jsonArrayRequest);
    }

//create arraylist of image urls
    private void showimg(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject obj = null;
            try {

                obj = jsonArray.getJSONObject(i);

                images.add(obj.getString("image1"));
                images.add(obj.getString("image2"));
                images.add(obj.getString("image3"));
                images.add(obj.getString("image4"));
                images.add(obj.getString("image5"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            init(images);
        }
    }
}






