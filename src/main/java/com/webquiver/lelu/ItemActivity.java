package com.webquiver.lelu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;

import android.os.Bundle;
import android.os.Handler;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.View;
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
import com.viewpagerindicator.CirclePageIndicator;
import com.webquiver.lelu.adapters.Banner_Adapter;
import com.webquiver.lelu.adapters.SearchResultAdapter;
import com.webquiver.lelu.classes.Config;
import com.webquiver.lelu.classes.SampleSuggestionsBuilder;
import com.webquiver.lelu.classes.SearchResult;
import com.webquiver.lelu.classes.SessionManagement;

import org.cryse.widget.persistentsearch.PersistentSearchView;
import org.cryse.widget.persistentsearch.SearchItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ItemActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    @Override
    public void onBackPressed()
    {

        Intent openFragmentBIntent = new Intent(this, HomeActivity.class);
        openFragmentBIntent.putExtra("OPEN_FRAGMENT_B", "yes");
        startActivity(openFragmentBIntent);
        finish();
    }



    //search
    private PersistentSearchView mSearchView;
    private SearchResultAdapter mResultAdapter;

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1023;
    private SharedPreferences searchhistory;
    SharedPreferences.Editor search_historyEditor;
    private ArrayList<String> searchnames;







    //sharedpref
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor_num_pref;

    SessionManagement sessionManagement;

    //images slider
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    ProgressBar progressBar;
    private ArrayList<String> images;

    public static final String ITEM_URL="https://api.myjson.com/bins/ujdqn";

   //sharedpreference
    public static final String ITEM_PREFERENCE="item_pref";
    public static final String NUM_PREFERENCE="num_pref";  //pref to store all numbers (items in cart,num of notific etc)
    private SharedPreferences sharedPreferences;
    private SharedPreferences pref_numbers;

    TextView cartnum;

    DrawerLayout mDrawerLayout;

    RequestQueue requestQueue;
    RequestQueue requestQueue_cart;

    TextView qty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setCollapsible(false);
        setSupportActionBar(toolbar);



        //search history
        searchhistory = this.getSharedPreferences(Config.SearchPref, Context.MODE_PRIVATE);
        search_historyEditor=searchhistory.edit();
        searchnames = new ArrayList<>();
        mSearchView = (PersistentSearchView) findViewById(R.id.searchview);







        qty=(TextView)findViewById(R.id.qtytxt_id);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.dl_drawer_layout);


        pref = this.getSharedPreferences(SessionManagement.PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();

        requestQueue = Volley.newRequestQueue(this);
        requestQueue_cart=Volley.newRequestQueue(this);



        //check if logged in or not
        sessionManagement=new SessionManagement(getApplicationContext());
        sessionManagement.checkLogin();



        //price text strike and ruppee symbol
        TextView realprice = (TextView) findViewById(R.id.realprice_id);
        TextView price = (TextView) findViewById(R.id.pricetxt_id);
        realprice.setText(realprice.getText().toString()+" "+"14,500.00");
        price.setText(price.getText().toString()+" "+"10,500.00");
        realprice.setPaintFlags(realprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);








        //initialisation
        images = new ArrayList<>();
        progressBar=(ProgressBar)findViewById(R.id.progbr_id);
        sharedPreferences = this.getSharedPreferences(ITEM_PREFERENCE, MODE_PRIVATE);
        pref_numbers = this.getSharedPreferences(NUM_PREFERENCE, MODE_PRIVATE);

        editor_num_pref=pref_numbers.edit();


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


        getall();





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

    public void onclickhandler_itemactivity(View view) {
        if (view == findViewById(R.id.cartitem)) {

            Intent intent=new Intent(ItemActivity.this,CartActivity.class);
            startActivity(intent);
            finish();

        }


        else if (view == findViewById(R.id.cartminus_id))
        {
            qty.setText(String.valueOf(Integer.parseInt(qty.getText().toString())-1));
        }

        else if (view == findViewById(R.id.back_id))
        {
            Intent openFragmentBIntent = new Intent(this, HomeActivity.class);
            openFragmentBIntent.putExtra("OPEN_FRAGMENT_B", "yes");
            startActivity(openFragmentBIntent);
            finish();
        }

        else if (view == findViewById(R.id.search_itemactivity_id))
        {

            String searchjson=searchhistory.getString(Config.SearchJsonString,"NULL");




            mSearchView.openSearch();
            try {
                showsearchdata(searchjson);
            } catch (JSONException e) {
                e.printStackTrace();
            }


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

                        Intent intent=new Intent(ItemActivity.this,SearchActivity.class);
                        startActivity(intent);
                        finish();

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



        else if (view == findViewById(R.id.cartplus_id))
        {

            qty.setText(String.valueOf(Integer.parseInt(qty.getText().toString())+1));

        }


        else if (view == findViewById(R.id.addtowishlist_id))
        {
            final ProgressDialog loading = ProgressDialog.show(ItemActivity.this, "Adding to wish list", "Please wait...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.WISHLIST_ADD_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loading.dismiss();
                            try {

                                JSONObject jsonResponse = new JSONObject(response);
                                if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                    Toast.makeText(ItemActivity.this,"Item added to your Wish List",Toast.LENGTH_SHORT).show();

                                    getall();

                                }

                                else if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("exist"))
                                {
                                    Toast.makeText(ItemActivity.this,"Item is already in your wish list ",Toast.LENGTH_SHORT).show();

                                }

                                else {

                                    Toast.makeText(ItemActivity.this, "Failed \nPlease Retry", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.dismiss();
                            //
                            Toast.makeText(ItemActivity.this, "error1", Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding the parameters to the request
                    params.put(Config.KEY_PHONE, pref.getString(SessionManagement.KEY_PHONE,""));
                    params.put(Config.KEY_CART_ProdId, "2");
                    return params;
                }
            };

            //Adding request the the queue
            requestQueue.add(stringRequest);

        }


        else if (view == findViewById(R.id.addtocartbtn_id))
        {

            final ProgressDialog loading = ProgressDialog.show(ItemActivity.this, "Adding to cart", "Please wait...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CART_ADD_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loading.dismiss();
                            try {

                                JSONObject jsonResponse = new JSONObject(response);
                                if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                    Toast.makeText(ItemActivity.this,"Item added to cart",Toast.LENGTH_SHORT).show();


                                    getall();

                                }

                                else {

                                    Toast.makeText(ItemActivity.this, "Failed to add", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.dismiss();
                            //
                            Toast.makeText(ItemActivity.this, "error1", Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding the parameters to the request
                    params.put(Config.KEY_PHONE, pref.getString(SessionManagement.KEY_PHONE,""));
                    params.put(Config.KEY_CART_ProdId, "2");
                    params.put(Config.KEY_CART_ProdQty,qty.getText().toString());
                    return params;
                }
            };

            //Adding request the the queue
            requestQueue.add(stringRequest);

        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {


            sessionManagement.logoutUser();
            finish();

        } else if (id == R.id.nav_gallery) {

            Intent intent=new Intent(getApplicationContext(),MyOrdersActivity.class);
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






    public void getall() {

        requestQueue_cart = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CART_GET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "response", Toast.LENGTH_LONG).show();

                        try {

                            JSONObject jsonResponse = new JSONObject(response);

                            //   JSONArray jsonArray=new JSONArray(response);

                            if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {


                                showNUM(response);


                            } else if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("failed")) {

                                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();

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
                        Toast.makeText(getApplicationContext(), "error1", Toast.LENGTH_LONG).show();
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

        cartnum=(TextView)findViewById(R.id.cartnumTXT_itemactivity_id);
        String nn=pref_numbers.getString(Config.KEY_NUM_CART,"0");
        if (nn.equals("0"))
        {

        }
        else {
            cartnum.setVisibility(View.VISIBLE);
            cartnum.setText(nn);
            }
    }


    private void showsearchdata(String jsonstring) throws JSONException {

        JSONArray jsonArray=new JSONArray(jsonstring);

        for(int i = 0; i<jsonArray.length(); i++){

            JSONObject obj = null;
            try {

                obj = jsonArray.getJSONObject(i);
                searchnames.add(obj.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //  init();

        mSearchView.setSuggestionBuilder(new SampleSuggestionsBuilder(this,searchhistory.getString(Config.first_suggestion,"NULL"),searchhistory.getString(Config.second_suggestion,"NULL"),searchhistory.getString(Config.third_suggestion,"NULL"), searchnames));



    }






}


