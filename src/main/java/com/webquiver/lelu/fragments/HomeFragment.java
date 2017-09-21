package com.webquiver.lelu.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.webquiver.lelu.HomeActivity;
import com.webquiver.lelu.ItemActivity;
import com.webquiver.lelu.R;
import com.webquiver.lelu.adapters.CartAdapter;
import com.webquiver.lelu.classes.CartItem;
import com.webquiver.lelu.classes.Config;
import com.webquiver.lelu.classes.ExpandableHeightGridView;
import com.webquiver.lelu.adapters.GridViewAdapter;
import com.webquiver.lelu.classes.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by WebQuiver 04 on 7/12/2017.
 */

public class HomeFragment extends android.app.Fragment {


            private static HomeFragment homeFragment = null;


    //sharedprefference
    private SharedPreferences sharedPreferences;
    public static final String HOME_PREFERENCE = "HOME_DATA";


    ProgressBar progressBar;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

 //   public static final String DATA_URL = "http://192.168.1.9:8000/get-inventory";


    public static final String TAG_IMAGE_URL = "i_image";
    public static final String TAG_NAME = "i_name";
    public static final String TAG_COLOR = "categ_name";
    public static final String TAG_ID = "inv_id";
    public static final String TAG_PRICE_RETAIL = "i_salesPrice";
    public static final String TAG_PRICE_WHOLESALE = "i_retailPrice";

    //GridView Object
    private ExpandableHeightGridView gridView;


    private ArrayList<String> images;
    private ArrayList<String> names;

    private ArrayList<String> color;
    private ArrayList<String> ids;
    private ArrayList<String> prices;

    private RequestQueue requestQueue_cart;

    TextView cartnum;


    private SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.home_frag, container, false);

        progressBar=(ProgressBar)rootView.findViewById(R.id.progbar_id);
        sharedPreferences = this.getActivity().getSharedPreferences(HOME_PREFERENCE, MODE_PRIVATE);

        pref = getActivity().getSharedPreferences(SessionManagement.PREF_NAME, Context.MODE_PRIVATE);


        gridView = (ExpandableHeightGridView) rootView.findViewById(R.id.grid);
        gridView.setExpanded(true);

        images = new ArrayList<>();
        names = new ArrayList<>();
        color = new ArrayList<>();
        ids = new ArrayList<>();
        prices = new ArrayList<>();


        //mPager = (ViewPager) rootView.findViewById(R.id.pager);

        //CirclePageIndicator indicator = (CirclePageIndicator)rootView.findViewById(R.id.indicator);


        getData();



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {


                if (isOnline()) {
               //     Toast.makeText(getActivity(), String.valueOf(names.get(position)), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(), ItemActivity.class);
                    //  intent.putExtra("name",String.valueOf(names.get(position)));
                    intent.putExtra("id", String.valueOf(ids.get(position)));
                    // intent.putStringArrayListExtra("images",images);
                    startActivity(intent);
               //     getActivity().finish();


                }
                else
                {

                }
            }
        });

        return rootView;
    }

    private void getData() {
        //Showing a progress dialog while our app fetches the data from url
        //   final ProgressDialog loading = ProgressDialog.show(this, "Please wait...","Fetching data...",false,false);
        progressBar.setVisibility(View.VISIBLE);

        //Creating a json array request to get the json from our api
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.TOP_SELLING_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        //Dismissing the progressdialog on response
                        //         loading.dismiss();
                        //Displaying our grid
                        SharedPreferences.Editor prefEdit = sharedPreferences.edit();
                        String jsonstring=response.toString();
                        prefEdit.putString(Config.JSONSTRING,jsonstring);
                        prefEdit.apply();
                        try {
                            showGrid(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(sharedPreferences.getString(Config.JSONSTRING, "NULL"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                        if(!(sharedPreferences.getString(Config.JSONSTRING, "NULL").equals("NULL")))
                            try {
                                showGrid(jsonArray);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

//                        Toast.makeText(getActivity(),"No response from api",Toast.LENGTH_LONG).show();

                    }
                }
        );

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //Adding our request to the queue
        requestQueue.add(jsonArrayRequest);
     }


    private void showGrid(JSONArray jsonArray) throws JSONException{

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject obj = null;
            try {

                obj = jsonArray.getJSONObject(i);

                images.add(obj.getString(TAG_IMAGE_URL));
                names.add(obj.getString(TAG_NAME));
                ids.add(obj.getString(TAG_ID));
                if (pref.getString("pricetype","null").equals("retail")||pref.getString("pricetype","null").equals("null")) {
                    prices.add(obj.getString(TAG_PRICE_RETAIL));
                }
                else
                    prices.add(obj.getString(TAG_PRICE_WHOLESALE));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        GridViewAdapter gridViewAdapter = new GridViewAdapter(getActivity(), images, names, ids,prices);

        gridView.setAdapter(gridViewAdapter);

    }

    public static HomeFragment getInstance() {
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
            return homeFragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        homeFragment = null;
    }

    //functions for getting and displaying number of items in cart(green circle)


    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            //  Toast.makeText(HomeActivity.this, "No Internet Connection!", Toast.LENGTH_LONG).show();

            SuperActivityToast.create(getActivity(), new Style(), Style.TYPE_STANDARD)
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


}