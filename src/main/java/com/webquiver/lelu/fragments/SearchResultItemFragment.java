package com.webquiver.lelu.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.webquiver.lelu.ItemActivity;
import com.webquiver.lelu.R;
import com.webquiver.lelu.SearchActivity;
import com.webquiver.lelu.adapters.GridViewAdapter;
import com.webquiver.lelu.classes.Config;
import com.webquiver.lelu.classes.ExpandableHeightGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by WebQuiver 04 on 7/12/2017.
 */

public class SearchResultItemFragment extends android.app.Fragment {


            private static SearchResultItemFragment homeFragment = null;


    //sharedprefference
    private SharedPreferences sharedPreferences;
    SharedPreferences pref;
    public static final String HOME_PREFERENCE = "HOME_DATA";


    ProgressBar progressBar;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    public static final String DATA_URL = "https://api.myjson.com/bins/xyser";


    public static final String TAG_IMAGE_URL = "image";
    public static final String TAG_NAME = "name";
    public static final String TAG_COLOR = "color";

    //GridView Object
    private ExpandableHeightGridView gridView;


    private ArrayList<String> images;
    private ArrayList<String> names;

    private ArrayList<String> color;

    private RequestQueue requestQueue_cart;

    TextView cartnum;
    String search_string;



    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.itemfrag_srchlyt, container, false);

//hide search view
        ((SearchActivity)getActivity()).searchhide();




        return rootView;


    }




    public static SearchResultItemFragment getInstance() {
        if (homeFragment == null) {
            homeFragment = new SearchResultItemFragment();
        }
            return homeFragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        homeFragment = null;
    }


    //functions for getting and displaying number of items in cart(green circle)



}