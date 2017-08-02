package com.webquiver.lelu;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import com.viewpagerindicator.CirclePageIndicator;
import com.webquiver.lelu.classes.Config;
import com.webquiver.lelu.classes.ExpandableHeightGridView;
import com.webquiver.lelu.adapters.Banner_Adapter;
import com.webquiver.lelu.classes.SessionManagement;
import com.webquiver.lelu.fragments.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ProgressBar progressBar;

//fragment
    Fragment fr = null;
    FragmentManager fm = null;
    View selectedView = null;



// banners
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    //api
    public static final String DATA_URL = "https://api.myjson.com/bins/xyser";
    public static final String BANNER_URL = "https://api.myjson.com/bins/15eqh3";
    public static final String TAG_IMAGE_URL = "image";
    //GridView Object
    private ExpandableHeightGridView gridView;

    private ArrayList<String> banimages;


    LinearLayout categorylayout;
    DrawerLayout mDrawerLayout;

    SessionManagement sessionManagement;

    private SharedPreferences sharedPreferences;
    public static final String BANNER_PREFERENCE = "BANNER_DATA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



       sharedPreferences = getSharedPreferences(BANNER_PREFERENCE, MODE_PRIVATE);



        //check if logged in or not
        sessionManagement=new SessionManagement(getApplicationContext());
        sessionManagement.checkLogin();



         final ImageView search = (ImageView) findViewById(R.id.search);

        progressBar=(ProgressBar)findViewById(R.id.prog_id);

        banimages = new ArrayList<>();

    //fragment
        fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frag_container, HomeFragment.getInstance());
        fragmentTransaction.commit();


        categorylayout=(LinearLayout)findViewById(R.id.categorylyt_id);

        //navigation drawer
        mDrawerLayout = (DrawerLayout)findViewById(R.id.dl_drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        //custom toolbar
        ActionBar ab = getSupportActionBar();
        LayoutInflater li = LayoutInflater.from(this);
        View customView = li.inflate(R.layout.custombar, null);
        ab.setCustomView(customView);
        ab.setDisplayShowCustomEnabled(true);

        ImageView menu = (ImageView) customView.findViewById(R.id.menuitem);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDrawerLayout.openDrawer(Gravity.LEFT);

            }
        });

        //show and hide category layout when wxpanded and collapsed

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {

                   categorylayout.setVisibility(View.INVISIBLE);
                    search.setVisibility(View.INVISIBLE);

                   // LinearLayout layout = (LinearLayout)findViewById(R.id.adlyt_id);
                   // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                  ///  params.setMargins(30, 20, 30, 0);
                  //  layout.setLayoutParams(params);


                } else if (verticalOffset == 0) {

                   // LinearLayout layout = (LinearLayout)findViewById(R.id.adlyt_id);
                   // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                  //  params.setMargins(30, 80, 30, 0);
                  //  layout.setLayoutParams(params);

                    categorylayout.setVisibility(View.VISIBLE);
                    search.setVisibility(View.VISIBLE);
                }
               else {
                    categorylayout.setVisibility(View.VISIBLE);
                    search.setVisibility(View.INVISIBLE);

                }

            }
        });




//show items in grid view
/*
        gridView = (ExpandableHeightGridView) findViewById(R.id.grid);
        gridView.setExpanded(true);

            images = new ArrayList<>();
            names = new ArrayList<>();
            banimages = new ArrayList<>();
            color = new ArrayList<>();







            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    Toast.makeText(HomeActivity.this, String.valueOf(names.get(position)), Toast.LENGTH_SHORT).show();

                }
            });

*/
        getData();
        }



        private void getData(){
            //Showing a progress dialog while our app fetches the data from url
           //final ProgressDialog loading = ProgressDialog.show(this, "Please wait...","Fetching data...",false,false);
            progressBar.setVisibility(View.VISIBLE);

            //get banner json
            JsonArrayRequest bannerjsonArrayRequest = new JsonArrayRequest(BANNER_URL,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //Dismissing the progressdialog on response
             //                       loading.dismiss();

                            progressBar.setVisibility(View.INVISIBLE);

                           SharedPreferences.Editor prefEdit = sharedPreferences.edit();
                            String jsonstring=response.toString();
                           prefEdit.putString(Config.JSONSTRING,jsonstring);
                            prefEdit.apply();
                            //Displaying banner
                            showbanner(response);
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(HomeActivity.this,"No response from api",Toast.LENGTH_LONG).show();
                            //set from preference
                            JSONArray jsonArray = null;
                            try {
                               jsonArray = new JSONArray(sharedPreferences.getString(Config.JSONSTRING, "NULL"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                           }
                            progressBar.setVisibility(View.INVISIBLE);
                            showbanner(jsonArray);

                        }
                    }
            );


            //Creating a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            //Adding our request to the queue
            requestQueue.add(bannerjsonArrayRequest);
        }



        private void showbanner(JSONArray jsonArray){

             for(int i = 0; i<jsonArray.length(); i++){

                 JSONObject obj = null;
                 try {

                    obj = jsonArray.getJSONObject(i);
                     banimages.add(obj.getString(TAG_IMAGE_URL));
                    } catch (JSONException e) {
                         e.printStackTrace();
                    }
        }
        init();

         }


    //banner
        private void init() {

            mPager = (ViewPager) findViewById(R.id.pager);
             mPager.setAdapter(new Banner_Adapter(HomeActivity.this,banimages));


             CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);

            indicator.setViewPager(mPager);

            final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(3 * density);

        NUM_PAGES =banimages.size();

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
    }, 5000, 5000);

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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {


           sessionManagement.logoutUser();
            finish();

        } else if (id == R.id.nav_gallery) {

            Intent intent=new Intent(HomeActivity.this,MyOrdersActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dl_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }



    public void onclickhandler(View view) {
        if (view == findViewById(R.id.indoorTXT_id)) {

            Toast.makeText(HomeActivity.this,"INDOOR",Toast.LENGTH_SHORT).show();

        } else if (view == findViewById(R.id.indoorIMG_iid)) {
            Toast.makeText(HomeActivity.this,"INDOOR",Toast.LENGTH_SHORT).show();

        }
    }

}
