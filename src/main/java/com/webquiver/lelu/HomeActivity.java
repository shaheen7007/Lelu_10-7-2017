package com.webquiver.lelu;

import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;
import com.tuyenmonkey.mkloader.MKLoader;
import com.viewpagerindicator.CirclePageIndicator;
import com.webquiver.lelu.adapters.CartAdapter;
import com.webquiver.lelu.adapters.SearchResultAdapter;
import com.webquiver.lelu.classes.Config;
import com.webquiver.lelu.classes.ExpandableHeightGridView;
import com.webquiver.lelu.adapters.Banner_Adapter;
import com.webquiver.lelu.classes.SampleSuggestionsBuilder;
import com.webquiver.lelu.classes.SearchResult;
import com.webquiver.lelu.classes.SessionManagement;
import com.webquiver.lelu.fragments.AddressFragment;
import com.webquiver.lelu.fragments.HomeFragment;
import com.webquiver.lelu.fragments.OrderDetFragment2;
import com.webquiver.lelu.fragments.SearchResultFragment;

import org.cryse.widget.persistentsearch.PersistentSearchView;
import org.cryse.widget.persistentsearch.SearchItem;
import org.cryse.widget.persistentsearch.SearchSuggestionsBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {




    public static Activity fa;


    PopupMenu popup;
/*
    public void onBackPressed()
    {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
        alertDialog.setTitle("Exit ?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Add your code for the button here.

                finish();
                System.exit(0);

            }
        });

        alertDialog.setNegativeButton("No",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                dialog.dismiss();

            }
        });

        alertDialog.show();
        }
*/

        boolean doubleBackToExitPressedOnce = false;
        @Override
        public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();

            try {
                HomeActivity.fa.finish();
            }
            catch (Exception e)
            {

            }

            return;
        }

        this.doubleBackToExitPressedOnce = true;
        //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            SuperActivityToast.create(this, new Style(), Style.TYPE_STANDARD)
               //     .setButtonText("Please click BACK again to exit")
               //     .setButtonIconResource(R.drawable.ic_undo)
              //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
               //     .setProgressBarColor(Color.WHITE)
                    .setText("click BACK again to exit")
                    .setDuration(Style.DURATION_LONG)
                    .setFrame(Style.FRAME_STANDARD)
                    .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                    .setAnimations(Style.ANIMATIONS_POP).show();




        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    //search
    private PersistentSearchView mSearchView;
    private SearchResultAdapter mResultAdapter;

    MKLoader mkLoader;

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1023;

    AppBarLayout appBarLayout;

   // ProgressBar progressBar;


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
    public static final String BANNER_URL = "https://api.myjson.com/bins/1f0fmd";
    public static final String TAG_IMAGE_URL = "image";
    //GridView Object
    private ExpandableHeightGridView gridView;

    private ArrayList<String> banimages;
    private ArrayList<String> categnames;
    private ArrayList<String> categids;

    LinearLayout categorylayout;
    DrawerLayout mDrawerLayout;

    SessionManagement sessionManagement;



    public static final String NUM_PREFERENCE="num_pref";
    private SharedPreferences pref_numberss;

    private SharedPreferences sharedPreferences;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor_pref;
    RequestQueue requestQueue_cart;
    public TextView cartnum;
    public static final String BANNER_PREFERENCE = "BANNER_DATA";



    SharedPreferences getPref_numberss;
    SharedPreferences.Editor editor_num_pref;



    private SharedPreferences searchhistory;
    SharedPreferences.Editor search_historyEditor;



    private ArrayList<String> searchnames;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        fa=this;





//check wheather cartactivity is running or not
        SharedPreferences sp = getSharedPreferences("OURINFO", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", false);
        ed.commit();





       sharedPreferences = getSharedPreferences(BANNER_PREFERENCE, MODE_PRIVATE);
        pref_numberss = this.getSharedPreferences(NUM_PREFERENCE, MODE_PRIVATE);
        editor_num_pref=pref_numberss.edit();


        searchnames = new ArrayList<>();
        mSearchView = (PersistentSearchView) findViewById(R.id.searchview);


        pref_numberss = this.getSharedPreferences(NUM_PREFERENCE, MODE_PRIVATE);


        //check if logged in or not
        sessionManagement=new SessionManagement(getApplicationContext());
        sessionManagement.checkLogin();

        requestQueue_cart=Volley.newRequestQueue(getApplicationContext());
        pref = this.getSharedPreferences(SessionManagement.PREF_NAME, Context.MODE_PRIVATE);
        editor_pref=pref.edit();

        final ImageView search = (ImageView) findViewById(R.id.search);

      //  progressBar=(ProgressBar)findViewById(R.id.prog_id);

        banimages = new ArrayList<>();
        categnames = new ArrayList<>();
        categids = new ArrayList<>();

        searchhistory = this.getSharedPreferences(Config.SearchPref, Context.MODE_PRIVATE);
        search_historyEditor=searchhistory.edit();



        //eventbus

        EventBus.getDefault().register(this);

        getSearchData();



        //
        if (getIntent().hasExtra("OPEN_FRAGMENT_B"))
        {
            Fragment fragment = new HomeFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frag_container, fragment).commit();
        }


        //fragment
        fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frag_container, HomeFragment.getInstance(),"homefrag");
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

        ImageView settingsbtn=(ImageView)customView.findViewById(R.id.optionsitem);
        settingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showsettings();
            }
        });

        editor_pref.putString("pricetype",pref.getString("pricetype","null"));
        editor_pref.commit();

        popup = new PopupMenu(this,settingsbtn);
        popup.getMenuInflater().inflate(R.menu.actions, popup.getMenu());


        cartnum=(TextView)findViewById(R.id.numbercart_home_id) ;

        if (isOnline()) {
            getall(); //get number of items in cart
        }
   /*
        String nn=pref_numberss.getString(Config.KEY_NUM_CART,"0");
        if (nn.equals("0")) {

        }
        else {
            cartnum.setVisibility(View.VISIBLE);
            cartnum.setText(nn);
        }

*/

        LinearLayout menu = (LinearLayout) customView.findViewById(R.id.menuitem);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDrawerLayout.openDrawer(Gravity.LEFT);

            }
        });

        ImageView menu_img = (ImageView) customView.findViewById(R.id.menuitem_img_id);
        menu_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDrawerLayout.openDrawer(Gravity.LEFT);

            }
        });

        //show and hide category layout when wxpanded and collapsed

       appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
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

        getcategories();




    }

    private void getData(){
            //Showing a progress dialog while our app fetches the data from url
           //final ProgressDialog loading = ProgressDialog.show(this, "Please wait...","Fetching data...",false,false);
    //        progressBar.setVisibility(View.VISIBLE);

            //get banner json
            JsonArrayRequest bannerjsonArrayRequest = new JsonArrayRequest(BANNER_URL,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //Dismissing the progressdialog on response
             //                       loading.dismiss();

          //                  progressBar.setVisibility(View.INVISIBLE);

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
                      //      Toast.makeText(HomeActivity.this,"No response from api",Toast.LENGTH_LONG).show();
                            //set from preference
                            JSONArray jsonArray = null;
                            try {
                               jsonArray = new JSONArray(sharedPreferences.getString(Config.JSONSTRING, "NULL"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                           }
                //            progressBar.setVisibility(View.INVISIBLE);
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

            /*
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
    }, 7000, 7000);

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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {


           sessionManagement.logoutUser();
            finish();

        } else if (id == R.id.nav_orders) {

            Intent intent=new Intent(getApplicationContext(),MyOrdersActivity.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);


        } else if (id == R.id.nav_wishlist) {

            Intent intent=new Intent(getApplicationContext(),WishListActivity.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);
       //     finish();

        } else if (id == R.id.nav_cart) {

            Intent intent = new Intent(HomeActivity.this, CartActivity.class);

            intent.putExtra("home","home");
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);
      //      finish();
        }
        else if (id == R.id.nav_account) {


            Intent intent = new Intent(HomeActivity.this, MyAccountActivity.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);





        }







        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dl_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }



    public void onclickhandler(View view) {
        if (view == findViewById(R.id.indoorTXT_id)) {

          //  Toast.makeText(HomeActivity.this,"INDOOR",Toast.LENGTH_SHORT).show();

            String itemname="Indoor";

            Intent intent=new Intent(HomeActivity.this,SearchActivity.class);
            intent.putExtra("searchterm",itemname);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);





        }

        if (view == findViewById(R.id.notificationitem)) {

            Intent intent=new Intent(HomeActivity.this,NotificationActivity.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);

        }







        else if (view == findViewById(R.id.indoorIMG_iid)) {
         //   Toast.makeText(HomeActivity.this,"INDOOR",Toast.LENGTH_SHORT).show();

            String itemname="Indoor";
            Intent intent=new Intent(HomeActivity.this,SearchActivity.class);
            intent.putExtra("searchterm",itemname);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);

        }

        if (view == findViewById(R.id.outdoorTXT_id)) {

            //  Toast.makeText(HomeActivity.this,"INDOOR",Toast.LENGTH_SHORT).show();

            String itemname="Outdoor";

            Intent intent=new Intent(HomeActivity.this,SearchActivity.class);
            intent.putExtra("searchterm",itemname);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);





        } else if (view == findViewById(R.id.outdoorIMG_id)) {
            //   Toast.makeText(HomeActivity.this,"INDOOR",Toast.LENGTH_SHORT).show();

            String itemname="Outdoor";
            Intent intent=new Intent(HomeActivity.this,SearchActivity.class);
            intent.putExtra("searchterm",itemname);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);

        }

        if (view == findViewById(R.id.newbornIMG_id)) {

            //  Toast.makeText(HomeActivity.this,"INDOOR",Toast.LENGTH_SHORT).show();

            String itemname="NewBorn";

            Intent intent=new Intent(HomeActivity.this,SearchActivity.class);
            intent.putExtra("searchterm",itemname);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);





        } else if (view == findViewById(R.id.newbornTXT_id)) {
            //   Toast.makeText(HomeActivity.this,"INDOOR",Toast.LENGTH_SHORT).show();

            String itemname="NewBorn";
            Intent intent=new Intent(HomeActivity.this,SearchActivity.class);
            intent.putExtra("searchterm",itemname);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);

        }







        else if (view == findViewById(R.id.homenav_id)) {

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dl_drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        }



        else if (view == findViewById(R.id.search)) {

            fun();
            fun();

            }

        else  if (view == findViewById(R.id.cartitem)) {


          Intent intent = new Intent(HomeActivity.this, CartActivity.class);

            intent.putExtra("home","home");
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);
           // finish();
        }
    }



    public void fun()
    {
        final ImageView s=(ImageView)findViewById(R.id.search);
        s.setVisibility(View.INVISIBLE);


        // mSearchView.setSuggestionBuilder(new SampleSuggestionsBuilder(this,searchhistory.getString(Config.first_suggestion,"NULL"),searchhistory.getString(Config.second_suggestion,"NULL"),searchhistory.getString(Config.third_suggestion,"NULL")));

        mSearchView.openSearch();


        if (searchhistory.getString(Config.numofhistory, "NULL").equals("NULL")) {

            search_historyEditor.putString(Config.numofhistory,String.valueOf(0));
            search_historyEditor.commit();

        }
        else
        {

            mSearchView.setSearchListener(new PersistentSearchView.SearchListener() {


                @Override
                public boolean onSuggestion(SearchItem searchItem) {
                    Log.d("onSuggestion", searchItem.getTitle());
                    mSearchView.setSearchString(searchItem.getTitle(),true);
                    onSearch(searchItem.getTitle());
                    return false;
                }

                @Override
                public void onSearchCleared() {

                }

                @Override
                public void onSearchTermChanged(String term) {

                }

                @Override
                public void onSearchEditClosed() {

                }

                @Override
                public boolean onSearchEditBackPressed() {
                    return false;
                }

                @Override
                public void onSearchExit() {

                }

                @Override
                public void onSearch(String string) {

                    //      Toast.makeText(HomeActivity.this, string +" Searched", Toast.LENGTH_LONG).show();

                    //fragment

                    //appBarLayout.setExpanded(false,true);

                    mSearchView.clearSuggestions();
                    mSearchView.closeSearch();

                     /*
                        Bundle bundle = new Bundle();
                        bundle.putString("search_item",string);
                        SearchResultFragment showSelectedADDR=new SearchResultFragment();
                        showSelectedADDR.setArguments(bundle);
                        FragmentManager fm = null;
                        fm = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        fragmentTransaction.replace(R.id.frag_container, showSelectedADDR);
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        fragmentTransaction.commit();

*/

                    Intent intent=new Intent(HomeActivity.this,SearchActivity.class);
                    //intent.putExtra("searchterm",string);
                    search_historyEditor.putString("searchterm",string);
                    search_historyEditor.commit();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    startActivity(intent);
                 //   finish();
                    int num=Integer.parseInt(searchhistory.getString(Config.numofhistory,"NULL"));

                    if (string.equals(searchhistory.getString(Config.first_suggestion,"NULL")))
                    {
                        //nothing
                    }
                    else if (string.equals(searchhistory.getString(Config.second_suggestion,"NULL")))
                    {
                        String temp;
                        temp=searchhistory.getString(Config.second_suggestion,"NULL");
                        search_historyEditor.putString(Config.second_suggestion,searchhistory.getString(Config.first_suggestion,"NULL"));
                        search_historyEditor.putString(Config.first_suggestion,temp);
                        search_historyEditor.commit();

                    }
                    else {
                        search_historyEditor.putString(Config.third_suggestion, searchhistory.getString(Config.second_suggestion, "NULL"));
                        search_historyEditor.putString(Config.second_suggestion, searchhistory.getString(Config.first_suggestion, "NULL"));
                        search_historyEditor.putString(Config.first_suggestion, string);
                        search_historyEditor.commit();
                    }


                }



                private void fillResultToRecyclerView(String query) {
                    List<SearchResult> newResults = new ArrayList<>();
                    for(int i =0; i< 10; i++) {
                        SearchResult result = new SearchResult(query, query + Integer.toString(i), "");
                        newResults.add(result);
                    }
                    mResultAdapter.replaceWith(newResults);
                }



                @Override
                public void onSearchEditOpened() {

                }


            });

        }

    }

    public void getall() {

        // requestQueue_cart = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CART_GET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                //        Toast.makeText(getApplicationContext(), "response", Toast.LENGTH_LONG).show();

                        try {

                            JSONObject jsonResponse = new JSONObject(response);

                            //   JSONArray jsonArray=new JSONArray(response);

                            if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                showNUM(response);

                            } else if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("failed")) {

                        //        Toast.makeText(getApplicationContext(), "Failed_num", Toast.LENGTH_LONG).show();

                                cartnum.setVisibility(View.INVISIBLE);

                            } else {

                                Toast.makeText(getApplicationContext(), "Invalid user", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //
                 //       Toast.makeText(getApplicationContext(), "error1", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put(Config.KEY_ADDR_MOBILE,  pref.getString(SessionManagement.KEY_PHONE,""));
                return params;
            }
        };

        //Adding request the the queue
        requestQueue_cart.add(stringRequest);

    }

    public void showNUM(String jsonArray) throws JSONException {

        JSONObject json = new JSONObject(jsonArray);
        JSONArray arr = json.getJSONArray("products");
        editor_num_pref.putString(Config.KEY_NUM_CART,String.valueOf(arr.length()) );
        editor_num_pref.commit();

        String nn=pref_numberss.getString(Config.KEY_NUM_CART,"0");
        if (nn.equals("0"))
        {

            cartnum.setVisibility(View.INVISIBLE);

        }
        else {

            cartnum.setVisibility(View.VISIBLE);
            cartnum.setText(nn);

        }
    }


    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
          //  Toast.makeText(HomeActivity.this, "No Internet Connection!", Toast.LENGTH_LONG).show();

            SuperActivityToast.create(HomeActivity.this, new Style(), Style.TYPE_STANDARD)
                    //     .setButtonText("Please click BACK again to exit")
                    //     .setButtonIconResource(R.drawable.ic_undo)
                    //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                    //     .setProgressBarColor(Color.WHITE)
                    .setText("No Internet connection available")
                    .setDuration(Style.DURATION_LONG)
                    .setFrame(Style.FRAME_STANDARD)
                    .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                    .setAnimations(Style.ANIMATIONS_FADE).show();


            return false;
        }
        return true;
    }




    //search



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mSearchView.populateEditText(matches);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    private void getSearchData(){
        //Showing a progress dialog while our app fetches the data from url
        //final ProgressDialog loading = ProgressDialog.show(this, "Please wait...","Fetching data...",false,false);
        //  progressBar.setVisibility(View.VISIBLE);

        //get banner json
        JsonArrayRequest bannerjsonArrayRequest = new JsonArrayRequest(Config.SEARCH_HISTORY_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing the progressdialog on response
                        //                       loading.dismiss();

                        //         progressBar.setVisibility(View.INVISIBLE);

                        //        SharedPreferences.Editor prefEdit = sharedPreferences.edit();
                        //Displaying banner
                        String searchnamesjsonstring=response.toString();
                        search_historyEditor.putString(Config.SearchJsonString,searchnamesjsonstring);
                        search_historyEditor.apply();

                        showsearchdata(response);
                    }

                },
                new Response.ErrorListener() {
                    @Override

                    public void onErrorResponse(VolleyError error) {
                    //    Toast.makeText(HomeActivity.this,"No response from api",Toast.LENGTH_LONG).show();
                        //set from preference
                        JSONArray jsonArray = null;
                        //  progressBar.setVisibility(View.INVISIBLE);
                        // showbanner(jsonArray);

                    }
                }
        );


        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding our request to the queue
        requestQueue.add(bannerjsonArrayRequest);
    }



    private void showsearchdata(JSONArray jsonArray){

        for(int i = 0; i<jsonArray.length(); i++){

            JSONObject obj = null;
            try {

                obj = jsonArray.getJSONObject(i);
                searchnames.add(obj.getString("i_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //  init();

        searchsugg();

    }

    public void searchsugg() {
        mSearchView.setSuggestionBuilder(new SampleSuggestionsBuilder(this, searchhistory.getString(Config.first_suggestion, "NULL"), searchhistory.getString(Config.second_suggestion, "NULL"), searchhistory.getString(Config.third_suggestion, "NULL"), searchnames));
    }



    public void showsettings() {

        if (pref.getString("usertype","null").equals("null")) {        //change

            if (pref.getString("pricetype",null).equals("retail"))
                popup.getMenu().getItem(1).setChecked(true);
            if (pref.getString("pricetype",null).equals("wholesale"))
                popup.getMenu().getItem(0).setChecked(true);

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {

                    String itemname = item.getTitle().toString();
                    if (itemname.equals("Retail Price")) {

                        editor_pref.putString("pricetype","retail");
                        editor_pref.commit();

                        //fragment

                        Fragment frg = null;
                        frg = getFragmentManager().findFragmentByTag("homefrag");
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(frg);
                        ft.attach(frg);
                        ft.commit();

                        if (item.isChecked())
                            item.setChecked(false);
                        else
                            item.setChecked(true);

                        Toast.makeText(HomeActivity.this, "Retail Price", Toast.LENGTH_SHORT).show();

                    }

                    if (itemname.equals("Wholesale Price")) {

                        editor_pref.putString("pricetype","wholesale");
                        editor_pref.commit();



                        //fragment
                        Fragment frg = null;
                        frg = getFragmentManager().findFragmentByTag("homefrag");
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(frg);
                        ft.attach(frg);
                        ft.commit();


                        if (item.isChecked())
                            item.setChecked(false);
                        else
                            item.setChecked(true);

                        Toast.makeText(HomeActivity.this, "Wholesale Price", Toast.LENGTH_SHORT).show();

                    }

                    return true;
                }
            });

            popup.show();//showing popup menu

        }


    }


    public void getcategories() {

        categnames.clear();
        categids.clear();

        JsonArrayRequest bannerjsonArrayRequest = new JsonArrayRequest(Config.GET_CATEG_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing the progressdialog on response
                        //                       loading.dismiss();

                        //                  progressBar.setVisibility(View.INVISIBLE);

                        addcateg(response);


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //      Toast.makeText(HomeActivity.this,"No response from api",Toast.LENGTH_LONG).show();
                        //set from preference

                    }
                }
        );

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding our request to the queue
        requestQueue.add(bannerjsonArrayRequest);


    }


    public void showcategories(View v) {

        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(HomeActivity.this, v);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.categories, popup.getMenu());
        popup.getMenuInflater().inflate(R.menu.categories, popup.getMenu());




        /*

        JsonArrayRequest bannerjsonArrayRequest = new JsonArrayRequest(Config.GET_CATEG_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing the progressdialog on response
                        //                       loading.dismiss();

                        //                  progressBar.setVisibility(View.INVISIBLE);

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
                        //      Toast.makeText(HomeActivity.this,"No response from api",Toast.LENGTH_LONG).show();
                        //set from preference
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(sharedPreferences.getString(Config.JSONSTRING, "NULL"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //            progressBar.setVisibility(View.INVISIBLE);
                        showbanner(jsonArray);

                    }
                }
        );

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding our request to the queue
        requestQueue.add(bannerjsonArrayRequest);

*/


    Menu menu=popup.getMenu();

        for (int i=0;i<categnames.size();i++) {

            menu.add(categnames.get(i));

        }



        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                String itemname=item.getTitle().toString();

                Intent intent=new Intent(HomeActivity.this,SearchActivity.class);
                intent.putExtra("searchterm",itemname);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent);














                return true;
            }
        });

        popup.show();//showing popup menu
    }



    public void onEvent(CartAdapter.HelloWorldEvent event){


        getall();


    }
    public void onEvent(ItemActivity.HelloWorldEvent event){


        getall();
        searchsugg();


    }
    public void onEvent(ItemActivity2.HelloWorldEvent event){


        getall();
        searchsugg();


    }

    public void onEvent(AddressFragment.HelloWorldEvent event){


        getall();


    }

    public void onEvent(SearchActivity.HelloWorldEvent event){


        searchsugg();


    }



    public void addcateg(JSONArray jsonArray)
    {

        for(int i = 0; i<jsonArray.length(); i++){

            JSONObject obj = null;
            try {

                obj = jsonArray.getJSONObject(i);
                categnames.add(obj.getString("categ_name"));
                categids.add(obj.getString("product_id"));
            } catch (JSONException e) {

                e.printStackTrace();

            }
        }



    }







}
