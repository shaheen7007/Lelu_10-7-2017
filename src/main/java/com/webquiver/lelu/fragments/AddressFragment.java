package com.webquiver.lelu.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.webquiver.lelu.R;
import com.webquiver.lelu.adapters.AddressAdapter;
import com.webquiver.lelu.adapters.GridViewAdapter;
import com.webquiver.lelu.classes.AddressItem;
import com.webquiver.lelu.classes.Config;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WebQuiver 04 on 7/25/2017.
 */

public class AddressFragment extends android.app.Fragment {

    private static AddressFragment addressFragment = null;

    private RequestQueue requestQueue;


    private static final String url = "https://api.myjson.com/bins/15eqh3";
    //private ProgressDialog pDialog;
    private List<AddressItem> movieList = new ArrayList<AddressItem>();
    private ListView listView;
    private AddressAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.address_frag, container, false);

        listView = (ListView) rootView.findViewById(R.id.addressList_id);

        requestQueue = Volley.newRequestQueue(getActivity());


        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADDR_GET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(getActivity(), "response", Toast.LENGTH_LONG).show();


                        try {


                            JSONObject jsonResponse = new JSONObject(response);

                            JSONArray jsonMainArr = jsonResponse.getJSONArray("addr");


                            //   JSONArray jsonArray=new JSONArray(response);

                            if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                showADDR(response);

                            } else if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("failed")) {

                                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();


                            } else {

                                Toast.makeText(getActivity(), "Invalid user", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getActivity(), "error1", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put(Config.KEY_ADDR_MOBILE, "9020707009");             //change
                return params;
            }
        };

        //Adding request the the queue
        requestQueue.add(stringRequest);


        AddressItem C_item = new AddressItem();
        C_item.setNAME("shaheen Shabib");
        C_item.setADDRESS1("dasdf sfsd dfds");
        C_item.setPLACE("Othayi");
        C_item.setDISTRICT("Malappuram");
        C_item.setSTATE("Kerala");
        C_item.setPINCODE("676541");
        C_item.setPHONE("9020707009");
        C_item.setID(1);

        movieList.add(C_item);

        AddressItem C_item2 = new AddressItem();
        C_item2.setNAME("shaheen Shabib");
        C_item2.setADDRESS1("Webquiver");
        C_item2.setPLACE("Eranjipalam");
        C_item2.setDISTRICT("Calicut");
        C_item2.setSTATE("Kerala");
        C_item2.setPINCODE("6768574");
        C_item2.setPHONE("9020707009");
        C_item.setID(2);
        movieList.add(C_item2);

        AddressItem C_item3 = new AddressItem();
        C_item3.setNAME("shaheen BSV");
        C_item3.setADDRESS1("dasdf sfsd dfds");
        C_item3.setPLACE("Othayi");
        C_item3.setDISTRICT("Malappuram");
        C_item3.setSTATE("Kerala");
        C_item3.setPINCODE("676541");
        C_item3.setPHONE("8606198378");
        C_item3.setID(3);

        movieList.add(C_item3);

        // Parsing json
                       /*
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    CartItem movie1 = new CartItem();
                                    movie.setNAME(obj.getString("color"));
                                    movie.setIMAGE_URL(obj.getString("image"));
//                                    movie.setQUANTITY(((Integer) obj.get("name")));

                                    Toast.makeText(CartActivity.this,obj.getString("name"),Toast.LENGTH_LONG).show();

                                    // Genre is json array
                                    movieList.add(movie);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            */

        adapter = new AddressAdapter(getActivity(), movieList.subList(0, 1));
        listView.setAdapter(adapter);


        adapter.notifyDataSetChanged();


        return rootView;


    }

    public static AddressFragment getInstance() {
        if (addressFragment == null) {
            addressFragment = new AddressFragment();
        }
        return addressFragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        addressFragment = null;
    }


    private void showADDR(String jsonArray) {

        JSONObject jsonResponse = null;
        try {
            jsonResponse = new JSONObject(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonMainArr = jsonResponse.getJSONArray("addr");


            for (int i = 0; i < jsonMainArr.length(); i++) {

                JSONObject obj = null;
                try {

                    obj = jsonMainArr.getJSONObject(i);
                    Toast.makeText(getActivity(),obj.getString("name"),Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



}
