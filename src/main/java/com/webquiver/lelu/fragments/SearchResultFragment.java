package com.webquiver.lelu.fragments;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
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
import com.kennyc.bottomsheet.BottomSheet;
import com.kennyc.bottomsheet.BottomSheetListener;
import com.webquiver.lelu.ItemActivity;
import com.webquiver.lelu.ItemActivity2;
import com.webquiver.lelu.R;
import com.webquiver.lelu.adapters.GridViewAdapter;
import com.webquiver.lelu.classes.Config;
import com.webquiver.lelu.classes.ExpandableHeightGridView;

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

 //   public static final String DATA_URL = "https://api.myjson.com/bins/rpssx";

    public static final String TAG_IMAGE_URL = "i_image";
    public static final String TAG_NAME = "i_name";
    public static final String TAG_COLOR = "categ_name";
  //  public static final String TAG_ID = "inv_id";

    //GridView Object
    private ExpandableHeightGridView gridView;

    private ArrayList<String> images;
    private ArrayList<String> ids;
    private ArrayList<String> names;

    private ArrayList<String> color;
    private ArrayList<String> prices;

    private RequestQueue requestQueue_cart;

    TextView cartnum,filterbtn;
    String search_string,term;

    BottomSheetListener bottomSheetListener;
    public static final String TAG_ID = "inv_id";
    public static final String TAG_PRICE = "i_salesPrice";



    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.searchresult_frag, container, false);

        progressBar=(ProgressBar)rootView.findViewById(R.id.progbar_id);
        sharedPreferences = this.getActivity().getSharedPreferences(HOME_PREFERENCE, MODE_PRIVATE);


        gridView = (ExpandableHeightGridView) rootView.findViewById(R.id.grid);
        filterbtn=(TextView)rootView.findViewById(R.id.filterBT_id);

        gridView.setExpanded(true);


        Bundle b=getArguments();

        if (b!=null) {

             term = String.valueOf(b.getString("searchterm", "null"));
        }

       // TextView search_result_for=(TextView)rootView.findViewById(R.id.searchresultfortxt);
      //  Bundle b=getArguments();
       // search_string=b.getString("search_item","");
        //search_result_for.setText("Search result for: "+search_string);


        images = new ArrayList<>();
        ids = new ArrayList<>();
        names = new ArrayList<>();
        color = new ArrayList<>();
        prices = new ArrayList<>();

        //mPager = (ViewPager) rootView.findViewById(R.id.pager);

        //CirclePageIndicator indicator = (CirclePageIndicator)rootView.findViewById(R.id.indicator);

       // getData();
        getprodet();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

/*
                //fragment
                FragmentManager fm;
                fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.add(R.id.search_frag_container, SearchResultItemFragment.getInstance());
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                   fragmentTransaction.addToBackStack("srchitem");
                fragmentTransaction.commit();
*/

                Intent intent =new Intent(getActivity(),ItemActivity2.class);
                intent.putExtra("id", String.valueOf(ids.get(position)));
                startActivity(intent);



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


    public void getprodet() {

        progressBar.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //Adding our request to the queue

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SEARCH_RESULTS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //   Toast.makeText(getApplicationContext(),response, Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        //Dismissing the progressdialog on response
                        //         loading.dismiss();
                        //Displaying our grid
                        SharedPreferences.Editor prefEdit = sharedPreferences.edit();
                        String jsonstring=response.toString();
                        prefEdit.putString(Config.JSONSTRING,jsonstring);
                        prefEdit.apply();
                        try {
                            JSONArray jsonArray=new JSONArray(response);
                            showGrid(jsonArray);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressBar.setVisibility(View.INVISIBLE);

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put(Config.KEY_SEARCH_TERM,term);
                return params;
            }
        };

        //Adding request the the queue
        requestQueue.add(stringRequest);

    }


    private void showGrid(JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject obj = null;
            try {

                obj = jsonArray.getJSONObject(i);

                images.add(obj.getString(TAG_IMAGE_URL));
                names.add(obj.getString(TAG_NAME));
                ids.add(obj.getString(TAG_ID));
                prices.add(obj.getString(TAG_PRICE));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        GridViewAdapter gridViewAdapter = new GridViewAdapter(getActivity(), images, names, ids,prices);

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





    public interface MyListener {
        // you can define any parameter as per your requirement
        public void callback(View view, String result);
    }




}