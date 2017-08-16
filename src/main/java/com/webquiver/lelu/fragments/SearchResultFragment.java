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

public class SearchResultFragment extends android.app.Fragment {


            private static SearchResultFragment homeFragment = null;


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
                R.layout.searchresult_frag, container, false);

        progressBar=(ProgressBar)rootView.findViewById(R.id.progbar_id);
        sharedPreferences = this.getActivity().getSharedPreferences(HOME_PREFERENCE, MODE_PRIVATE);


        gridView = (ExpandableHeightGridView) rootView.findViewById(R.id.grid);
        gridView.setExpanded(true);


        TextView search_result_for=(TextView)rootView.findViewById(R.id.searchresultfortxt);
        Bundle b=getArguments();
        search_string=b.getString("search_item","");
        search_result_for.setText("Search result for: "+search_string);



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

                Intent intent=new Intent(getActivity(), ItemActivity.class);
                intent.putExtra("name",String.valueOf(names.get(position)));
                intent.putExtra("color",String.valueOf(color.get(position)));
                intent.putStringArrayListExtra("images",images);
                startActivity(intent);
                getActivity().finish();


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

//                        Toast.makeText(getActivity(),"No response from api",Toast.LENGTH_LONG).show();

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


    public static SearchResultFragment getInstance() {
        if (homeFragment == null) {
            homeFragment = new SearchResultFragment();
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