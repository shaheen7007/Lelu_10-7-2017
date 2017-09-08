package com.webquiver.lelu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;

import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.widget.Button;
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


public class ItemActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //sharedpref
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor_num_pref;

    SessionManagement sessionManagement;



    AlertDialog.Builder alert;
    AlertDialog alertDialog;


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

    public static final String TAG_IMAGE_URL = "image";
    public static final String TAG_NAME = "i_name";
    public static final String TAG_PRICE = "i_retailPrice";
    public static final String TAG_REALPRICE = "i_salesPrice";
    public static final String TAG_IMAGE = "i_image";
    public static final String TAG_COLOR = "categ_name";
    public static final String TAG_PROD_ID = "inv_id";
    String prod_id;
    TextView name,price,realprice,description;










    //search
    private PersistentSearchView mSearchView;
    private SearchResultAdapter mResultAdapter;

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1023;
    private SharedPreferences searchhistory;
    SharedPreferences.Editor search_historyEditor;
    private ArrayList<String> searchnames;

    String pro_id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setCollapsible(false);
        setSupportActionBar(toolbar);


        qty=(TextView)findViewById(R.id.qtytxt_id);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.dl_drawer_layout);


        pref = this.getSharedPreferences(SessionManagement.PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();

        requestQueue = Volley.newRequestQueue(this);
        requestQueue_cart=Volley.newRequestQueue(this);



        //search history
        searchhistory = this.getSharedPreferences(Config.SearchPref, Context.MODE_PRIVATE);
        search_historyEditor=searchhistory.edit();
        searchnames = new ArrayList<>();
        mSearchView = (PersistentSearchView) findViewById(R.id.searchview);



        realprice = (TextView) findViewById(R.id.realprice_id);
        //  description = (TextView) findViewById(R.id.realprice_id);
        price = (TextView) findViewById(R.id.pricetxt_id);
        name = (TextView) findViewById(R.id.pronametxt_id);



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

            //Toast.makeText(this, String.valueOf(b.get("name")), Toast.LENGTH_LONG).show();

            pro_id=String.valueOf(b.get("id"));

        }


        //custom toolbar
        ActionBar ab = getSupportActionBar();
        LayoutInflater li = LayoutInflater.from(this);
        View customView = li.inflate(R.layout.custombar2_itemactivity, null);
        ab.setCustomView(customView);
        ab.setDisplayShowCustomEnabled(true);



        //image slider
      //  getimgs();

        getall();

        LayoutInflater lii = LayoutInflater.from(ItemActivity2.this);

        //Creating a view to get the dialog box
        View confirmDialog = lii.inflate(R.layout.something_went_wrong, null);
        alert = new AlertDialog.Builder(ItemActivity2.this);
        alert.setView(confirmDialog);
        alert.setCancelable(false);
        Button buttonSave = (Button) confirmDialog.findViewById(R.id.buttonretry);
        alertDialog = alert.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSHAKE;

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getprodet();

            }
        });


        getprodet();






    }

    //show image
    private void init(ArrayList<String> test) {
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new Banner_Adapter(ItemActivity2.this, test));

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
                        Toast.makeText(ItemActivity2.this,"No response from api",Toast.LENGTH_LONG).show();
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

            Intent intent=new Intent(ItemActivity2.this,CartActivity.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);
            finish();

        }


        else if (view == findViewById(R.id.cartminus_id))
        {
            qty.setText(String.valueOf(Integer.parseInt(qty.getText().toString())-1));
        }

        else if (view == findViewById(R.id.back_id))
        {
            /*
            Intent openFragmentBIntent = new Intent(this, HomeActivity.class);
            openFragmentBIntent.putExtra("OPEN_FRAGMENT_B", "yes");
            startActivity(openFragmentBIntent);
            finish();
            */
            super.onBackPressed();
        }

        else if (view == findViewById(R.id.cartplus_id))
        {

            qty.setText(String.valueOf(Integer.parseInt(qty.getText().toString())+1));

        }


        else if (view == findViewById(R.id.addtowishlist_id))
        {

            final Dialog loading = new Dialog(ItemActivity2.this);
            loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
            loading.setContentView(R.layout.custom_dialog_progress_loggingin);
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.setCancelable(false);
            TextView t=(TextView)loading.findViewById(R.id.txt);
            t.setText("Adding item to your Wishlist");
            loading.show();

          //  final ProgressDialog loading = ProgressDialog.show(ItemActivity2.this, "Adding to wish list", "Please wait...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.WISHLIST_ADD_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loading.dismiss();
                            try {

                                JSONObject jsonResponse = new JSONObject(response);
                                if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                    SuperActivityToast.create(ItemActivity2.this, new Style(), Style.TYPE_STANDARD)
                                            //     .setButtonText("Please click BACK again to exit")
                                            //     .setButtonIconResource(R.drawable.ic_undo)
                                            //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                                            //     .setProgressBarColor(Color.WHITE)
                                            .setText("Item added to your wishlist")
                                            .setDuration(Style.DURATION_LONG)
                                            .setFrame(Style.FRAME_STANDARD)
                                            .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREEN))
                                            .setAnimations(Style.ANIMATIONS_POP).show();

                                    getall();

                                }

                                else if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("exist"))
                                {
                                    SuperActivityToast.create(ItemActivity2.this, new Style(), Style.TYPE_STANDARD)
                                            //     .setButtonText("Please click BACK again to exit")
                                            //     .setButtonIconResource(R.drawable.ic_undo)
                                            //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                                            //     .setProgressBarColor(Color.WHITE)
                                            .setText("Item is already in your wishlist")
                                            .setDuration(Style.DURATION_LONG)
                                            .setFrame(Style.FRAME_STANDARD)
                                            .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                                            .setAnimations(Style.ANIMATIONS_POP).show();
                                }

                                else {

                                    SuperActivityToast.create(ItemActivity2.this, new Style(), Style.TYPE_STANDARD)
                                            //     .setButtonText("Please click BACK again to exit")
                                            //     .setButtonIconResource(R.drawable.ic_undo)
                                            //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                                            //     .setProgressBarColor(Color.WHITE)
                                            .setText("Adding item to your wishlist failed\nPlease retry")
                                            .setDuration(Style.DURATION_LONG)
                                            .setFrame(Style.FRAME_STANDARD)
                                            .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                                            .setAnimations(Style.ANIMATIONS_POP).show();
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

                            SuperActivityToast.create(ItemActivity2.this, new Style(), Style.TYPE_STANDARD)
                                    //     .setButtonText("Please click BACK again to exit")
                                    //     .setButtonIconResource(R.drawable.ic_undo)
                                    //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                                    //     .setProgressBarColor(Color.WHITE)
                                    .setText("Adding item to your wishlist failed\nPlease retry")
                                    .setDuration(Style.DURATION_LONG)
                                    .setFrame(Style.FRAME_STANDARD)
                                    .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                                    .setAnimations(Style.ANIMATIONS_POP).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding the parameters to the request
                    params.put(Config.KEY_PHONE, pref.getString(SessionManagement.KEY_PHONE,""));
                    params.put(Config.KEY_CART_ProdId, prod_id);
                    return params;
                }
            };

            //Adding request the the queue
            requestQueue.add(stringRequest);

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

                        SearchActivity.fa.finish();

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




                        Intent intent=new Intent(ItemActivity2.this,SearchActivity.class);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        intent.putExtra("searchterm",string);
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


        else if (view == findViewById(R.id.addtocartbtn_id))
        {

            final Dialog loading = new Dialog(ItemActivity2.this);
            loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
            loading.setContentView(R.layout.custom_dialog_progress_loggingin);
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.setCancelable(false);
            TextView t=(TextView)loading.findViewById(R.id.txt);
            t.setText("Adding item to your Wishlist");
            loading.show();


          //  final ProgressDialog loading = ProgressDialog.show(ItemActivity2.this, "Adding to cart", "Please wait...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CART_ADD_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loading.dismiss();
                            try {

                                JSONObject jsonResponse = new JSONObject(response);
                                if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                    SuperActivityToast.create(ItemActivity2.this, new Style(), Style.TYPE_STANDARD)
                                            //     .setButtonText("Please click BACK again to exit")
                                            //     .setButtonIconResource(R.drawable.ic_undo)
                                            //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                                            //     .setProgressBarColor(Color.WHITE)
                                            .setText("Item added to your cart")
                                            .setDuration(Style.DURATION_LONG)
                                            .setFrame(Style.FRAME_STANDARD)
                                            .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREEN))
                                            .setAnimations(Style.ANIMATIONS_POP).show();

                                    getall();

                                }

                                else {

                                    SuperActivityToast.create(ItemActivity2.this, new Style(), Style.TYPE_STANDARD)
                                            //     .setButtonText("Please click BACK again to exit")
                                            //     .setButtonIconResource(R.drawable.ic_undo)
                                            //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                                            //     .setProgressBarColor(Color.WHITE)
                                            .setText("Adding item to your cart failed \nPlease retry")
                                            .setDuration(Style.DURATION_LONG)
                                            .setFrame(Style.FRAME_STANDARD)
                                            .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                                            .setAnimations(Style.ANIMATIONS_POP).show();
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
                            SuperActivityToast.create(ItemActivity2.this, new Style(), Style.TYPE_STANDARD)
                                    //     .setButtonText("Please click BACK again to exit")
                                    //     .setButtonIconResource(R.drawable.ic_undo)
                                    //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                                    //     .setProgressBarColor(Color.WHITE)
                                    .setText("Adding item to your cart failed \nPlease retry")
                                    .setDuration(Style.DURATION_LONG)
                                    .setFrame(Style.FRAME_STANDARD)
                                    .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                                    .setAnimations(Style.ANIMATIONS_POP).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding the parameters to the request
                    params.put(Config.KEY_PHONE, pref.getString(SessionManagement.KEY_PHONE,""));
                    params.put(Config.KEY_CART_ProdId,prod_id);
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
/*
        if (id == R.id.nav_camera) {


            sessionManagement.logoutUser();
            finish();

        } else if (id == R.id.nav_gallery) {

            Intent intent=new Intent(getApplicationContext(),MyOrdersActivity.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);


        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }
        */

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



    public void getprodet() {

        final Dialog loading = new Dialog(ItemActivity2.this);
        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading.setContentView(R.layout.custom_dialog_progress_loggingin);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.setCancelable(false);
        TextView t=(TextView)loading.findViewById(R.id.txt);
        t.setText("Loading");
        loading.show();

        requestQueue_cart = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SINGLE_PROD_GET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //   Toast.makeText(getApplicationContext(),response, Toast.LENGTH_LONG).show();

                        loading.dismiss();
                        alertDialog.dismiss();
                        try {

                            //   JSONObject jsonResponse = new JSONObject(response);

                            //   JSONArray jsonArray=new JSONArray(response);

                            //      if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                            //   Toast.makeText(getApplicationContext(),response, Toast.LENGTH_LONG).show();

                            showPRO(response);


                            //  } else if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("failed")) {

                            //      Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();

                            //    } else {

                            //       Toast.makeText(getApplicationContext(), "Invalid user", Toast.LENGTH_LONG).show();

                             //    }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //    Toast.makeText(getApplicationContext(), "catch", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        loading.dismiss();
                        alertDialog.show();
                        //
                        Toast.makeText(getApplicationContext(), "error1", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put(Config.SINGLE_PROD_Id,pro_id);
                return params;
            }
        };

        //Adding request the the queue
        requestQueue_cart.add(stringRequest);

    }

    public void showPRO(String jsonArray) throws JSONException {

        //     Toast.makeText(getApplicationContext(), "show prod", Toast.LENGTH_LONG).show();
        images = new ArrayList<>();

        JSONArray jsonArr=new JSONArray(jsonArray);
        JSONObject obj = null;
        obj = jsonArr.getJSONObject(0);
        prod_id=obj.getString(TAG_PROD_ID);
        name.setText(obj.getString(TAG_NAME));
        price.setText("\u20B9 "+obj.getString(TAG_PRICE));
        realprice.setText("\u20B9 "+obj.getString(TAG_REALPRICE));
        images.add(obj.getString(TAG_IMAGE));

        // images.add(obj.getString(TAG_IMAGE));

        init(images);



        //     description.setText(json.getString(TAG_DESCRIPTION));

    }



}


