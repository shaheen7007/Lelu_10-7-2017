package com.webquiver.lelu.fragments;

import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.webquiver.lelu.R;
import com.webquiver.lelu.classes.Config;
import com.webquiver.lelu.classes.ExpandableHeightGridView;
import com.webquiver.lelu.adapters.GridViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    DrawerLayout mDrawerLayout;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.home_frag, container, false);

        progressBar=(ProgressBar)rootView.findViewById(R.id.progbar_id);
        sharedPreferences = this.getActivity().getSharedPreferences(HOME_PREFERENCE, MODE_PRIVATE);


        gridView = (ExpandableHeightGridView) rootView.findViewById(R.id.grid);
        gridView.setExpanded(true);

        images = new ArrayList<>();
        names = new ArrayList<>();

        color = new ArrayList<>();


        //mPager = (ViewPager) rootView.findViewById(R.id.pager);

        //CirclePageIndicator indicator = (CirclePageIndicator)rootView.findViewById(R.id.indicator);


        getData();



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Toast.makeText(getActivity(), String.valueOf(names.get(position)), Toast.LENGTH_SHORT).show();

            }
        });


        return rootView;


    }

    private void getData() {
        //Showing a progress dialog while our app fetches the data from url
        //   final ProgressDialog loading = ProgressDialog.show(this, "Please wait...","Fetching data...",false,false);

        progressBar.setVisibility(View.VISIBLE);

        //Creating a json array request to get the json from our api
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(DATA_URL,
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
                        showGrid(response);
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
                        showGrid(jsonArray);

                        Toast.makeText(getActivity(),"No response from api",Toast.LENGTH_LONG).show();

                    }
                }
        );



        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //Adding our request to the queue
        requestQueue.add(jsonArrayRequest);
     }


    private void showGrid(JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject obj = null;
            try {

                obj = jsonArray.getJSONObject(i);

                images.add(obj.getString(TAG_IMAGE_URL));
                names.add(obj.getString(TAG_NAME));
                color.add(obj.getString(TAG_COLOR));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //Creating GridViewAdapter Object
        GridViewAdapter gridViewAdapter = new GridViewAdapter(getActivity(), images, names, color);

        //Adding adapter to gridview
        gridView.setAdapter(gridViewAdapter);
    }



    //banner





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

}