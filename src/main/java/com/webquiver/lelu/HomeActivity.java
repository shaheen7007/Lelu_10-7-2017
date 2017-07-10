package com.webquiver.lelu;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import com.webquiver.lelu.classes.ExpandableHeightGridView;
import com.webquiver.lelu.classes.GridViewAdapter;
import com.webquiver.lelu.classes.SlidingImage_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {

    //
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES= {R.drawable.banner2,R.drawable.bannertest,R.drawable.tick_login,R.drawable.notification};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();


















    public static final String DATA_URL = "https://api.myjson.com/bins/xyser";


    public static final String TAG_IMAGE_URL = "image";
    public static final String TAG_NAME = "name";
    public static final String TAG_COLOR = "color";

    //GridView Object
    private ExpandableHeightGridView gridView;


    private ArrayList<String> images;
    private ArrayList<String> names;
    private ArrayList<String> color;


    CollapsingToolbarLayout collapsingToolbarLayout;
    LinearLayout categorylayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
         ImageView search = (ImageView) findViewById(R.id.search);



        //
        init();






        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        categorylayout=(LinearLayout)findViewById(R.id.categorylyt_id);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();



                collapsingToolbarLayout.setBackgroundResource(R.drawable.banner2);

            }
        });


//custom toolbar
        ActionBar ab = getSupportActionBar();
        LayoutInflater li = LayoutInflater.from(this);
        View customView = li.inflate(R.layout.custombar, null);
        ab.setCustomView(customView);
        ab.setDisplayShowCustomEnabled(true);


        //show and hide category layout when wxpanded and collapsed

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {

                   categorylayout.setVisibility(View.INVISIBLE);

                    //LinearLayout layout = (LinearLayout)findViewById(R.id.adlyt_id);
                 //   LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                  //  params.setMargins(30, 20, 30, 0);
                  //  layout.setLayoutParams(params);


                } else if (verticalOffset == 0) {

                   // LinearLayout layout = (LinearLayout)findViewById(R.id.adlyt_id);
                   // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                  //  params.setMargins(30, 80, 30, 0);
                  //  layout.setLayoutParams(params);

                    categorylayout.setVisibility(View.VISIBLE);
                }
               else {
                    categorylayout.setVisibility(View.VISIBLE);

                }

            }
        });











//show items in grid view

        gridView = (ExpandableHeightGridView) findViewById(R.id.grid);
        gridView.setExpanded(true);

            images = new ArrayList<>();
            names = new ArrayList<>();
            color = new ArrayList<>();


            getData();


            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    Toast.makeText(HomeActivity.this, String.valueOf(names.get(position)), Toast.LENGTH_SHORT).show();

                }
            });


        }

        private void getData(){
            //Showing a progress dialog while our app fetches the data from url
         //   final ProgressDialog loading = ProgressDialog.show(this, "Please wait...","Fetching data...",false,false);

            //Creating a json array request to get the json from our api
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(DATA_URL,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //Dismissing the progressdialog on response
                   //         loading.dismiss();

                            //Displaying our grid
                            showGrid(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            );

            //Creating a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            //Adding our request to the queue
            requestQueue.add(jsonArrayRequest);
        }


        private void showGrid(JSONArray jsonArray){
            //Looping through all the elements of json array
            for(int i = 0; i<jsonArray.length(); i++){
                //Creating a json object of the current index
                JSONObject obj = null;
                try {
                    //getting json object from current index
                    obj = jsonArray.getJSONObject(i);

                    //getting image url and title from json object
                    images.add(obj.getString(TAG_IMAGE_URL));
                    names.add(obj.getString(TAG_NAME));
                    color.add(obj.getString(TAG_COLOR));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //Creating GridViewAdapter Object
            GridViewAdapter gridViewAdapter = new GridViewAdapter(this,images,names,color);

            //Adding adapter to gridview
            gridView.setAdapter(gridViewAdapter);
        }






//
private void init() {
    for(int i=0;i<IMAGES.length;i++)
        ImagesArray.add(IMAGES[i]);

    mPager = (ViewPager) findViewById(R.id.banners);


    mPager.setAdapter(new SlidingImage_Adapter(HomeActivity.this,ImagesArray));


    CirclePageIndicator indicator = (CirclePageIndicator)
            findViewById(R.id.indicator);

    indicator.setViewPager(mPager);

    final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
    indicator.setRadius(5 * density);

    NUM_PAGES =IMAGES.length;

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
    Timer swipeTimer = new Timer();
    swipeTimer.schedule(new TimerTask() {
        @Override
        public void run() {
            handler.post(Update);
        }
    }, 3000, 3000);

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







}
