package com.webquiver.lelu.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.webquiver.lelu.R;
import com.webquiver.lelu.adapters.OrderAdapter;
import com.webquiver.lelu.adapters.ViewDetAdapter;
import com.webquiver.lelu.classes.CartItem;
import com.webquiver.lelu.classes.Config;
import com.webquiver.lelu.classes.ODRItem;
import com.webquiver.lelu.classes.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WebQuiver 04 on 7/24/2017.
 */



//this fragment is not using


public class OrderDetFragment extends Fragment {

    private static OrderDetFragment cartFragment = null;

    private ProgressDialog pDialog;
    private List<CartItem> movieList;
    private ListView listView;
    private ViewDetAdapter adapter;
    private RequestQueue requestQueue;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    TextView ordrID,ordrSTATUS;

    TextView ordrNAME,ordrADDR1,ordrPLACE,ordrDIST,ordrSTATE,ordrPIN;

    int i;
    int poss;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        container.removeAllViews();

        View rootView = inflater.inflate(
                R.layout.order_det_frag, container, false);

        movieList= new ArrayList<CartItem>();
        listView = (ListView) rootView.findViewById(R.id.prod_list);
        listView.setAdapter(adapter);
        pref = this.getActivity().getSharedPreferences(SessionManagement.PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();

        Bundle b=getArguments();

        poss=Integer.parseInt(b.getString("p","0"));

        ordrID=(TextView)rootView.findViewById(R.id.ordrID_id);
        ordrSTATUS=(TextView)rootView.findViewById(R.id.ordrSTATUS_id);

        ordrNAME=(TextView)rootView.findViewById(R.id.ADDR_name_id);
        ordrADDR1=(TextView)rootView.findViewById(R.id.ADDR_address1_id);
        ordrPLACE=(TextView)rootView.findViewById(R.id.ADDR_place_id);
        ordrDIST=(TextView)rootView.findViewById(R.id.ADDR_district_id);
        ordrPIN=(TextView)rootView.findViewById(R.id.ADDR_pin_id);
        ordrSTATE=(TextView)rootView.findViewById(R.id.ADDR_state_id);


    /* DUMMY DATAS
        CartItem C_item = new CartItem();
        C_item.setNAME("Baybee BMW");
        C_item.setIMAGE_URL("http://ecx.images-amazon.com/images/I/5169e67lGUL._SY355_.jpg");
        C_item.setQUANTITY(1);
        C_item.setPRICE("10,200.00");
        C_item.setREALPRICE("10,376.00");
        movieList.add(C_item);

        CartItem C_item3 = new CartItem();
        C_item3.setNAME("Baybee");
        C_item3.setIMAGE_URL("http://ecx.images-amazon.com/images/I/5169e67lGUL._SY355_.jpg");
        C_item3.setQUANTITY(1);
        C_item3.setPRICE("10,200.00");
        C_item3.setREALPRICE("10,376.00");
        movieList.add(C_item3);

        CartItem C_item2 = new CartItem();
        C_item2.setNAME("Baybee BMW");
        C_item2.setIMAGE_URL("http://ecx.images-amazon.com/images/I/5169e67lGUL._SY355_.jpg");
        C_item2.setQUANTITY(1);
        C_item2.setPRICE("10,200.00");
        C_item2.setREALPRICE("10,376.00");
        movieList.add(C_item2);

        CartItem C_item4 = new CartItem();
        C_item4.setNAME("Baybee");
        C_item4.setIMAGE_URL("http://ecx.images-amazon.com/images/I/5169e67lGUL._SY355_.jpg");
        C_item4.setQUANTITY(1);
        C_item4.setPRICE("10,200.00");
        C_item4.setREALPRICE("10,376.00");
        movieList.add(C_item4);

        CartItem C_item5 = new CartItem();
        C_item5.setNAME("Baybee BMW");
        C_item5.setIMAGE_URL("http://ecx.images-amazon.com/images/I/5169e67lGUL._SY355_.jpg");
        C_item5.setQUANTITY(1);
        C_item5.setPRICE("10,200.00");
        C_item5.setREALPRICE("10,376.00");
        movieList.add(C_item5);

        CartItem C_item6 = new CartItem();
        C_item6.setNAME("Baybee BMW");
        C_item6.setIMAGE_URL("http://ecx.images-amazon.com/images/I/5169e67lGUL._SY355_.jpg");
        C_item6.setQUANTITY(1);
        C_item6.setPRICE("10,200.00");
        C_item6.setREALPRICE("10,376.00");
        movieList.add(C_item6);
 */

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

    getall(poss);
    //  adapter.notifyDataSetChanged();

        return rootView;

    }
    public static OrderDetFragment getInstance() {
        if (cartFragment == null) {
            cartFragment = new OrderDetFragment();
        }
        return cartFragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cartFragment = null;
    }


    public void getall(int pos) {
        final int posi=pos;

        requestQueue = Volley.newRequestQueue(getActivity());

        //   final ProgressDialog loading = ProgressDialog.show(activity, "Loading", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ODR_GET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //    loading.dismiss();
                        Toast.makeText(getActivity(), "response", Toast.LENGTH_LONG).show();

                        try {

                            JSONObject jsonResponse = new JSONObject(response);

                            //   JSONArray jsonArray=new JSONArray(response);

                            if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {


                                showODR(response,posi);


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
                        //        loading.dismiss();

                        Toast.makeText(getActivity(), "error1", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put(Config.KEY_ADDR_MOBILE, pref.getString(SessionManagement.KEY_PHONE, ""));

                return params;
            }
        };

        //Adding request the the queue
        requestQueue.add(stringRequest);

    }


    public void showODR(String jsonArray,int pos) throws JSONException {

        JSONObject json = new JSONObject(jsonArray);
        JSONArray arr = json.getJSONArray("orders");
        //  for (int i = 0; i < arr.length(); i++) {

        try {

            JSONObject tt = arr.getJSONObject(pos);

            ordrID.setText("Order id #"+tt.getString("order_place_id"));
            ordrSTATUS.setText(tt.getString("op_status"));
            ordrNAME.setText(tt.getString("ca_name"));
            ordrADDR1.setText(tt.getString("ca_house"));
            ordrPLACE.setText(tt.getString("ca_street"));
            ordrDIST.setText(tt.getString("ca_dist"));
            ordrPIN.setText(tt.getString("ca_pin"));
            ordrSTATE.setText(tt.getString("ca_state"));


            //  ODRItem C_item5 = new ODRItem();
            //  C_item5.setODR_ID(tt.getString("order_place_id"));
            //  C_item5.setNAME(tt.getString("order_place_id"));
            //  C_item5.setSTATUS(tt.getString("op_status"));


            JSONArray prod = tt.getJSONArray("products");
            // C_item5.setNumberOfProducts(prod.length());



            //C_item5.setODR_ID(tt.getString("order_place_id"));
            //  C_item5.setNAME(tt.getString("order_place_id"));


            for (int j = 0; j < prod.length(); j++) {           //list products
                CartItem Item_item = new CartItem();
                JSONObject ss = prod.getJSONObject(j);
                Item_item.setNAME("default");                         //change
                Item_item.setPRICE("4,600.00");
                Item_item.setQUANTITY(ss.getInt("opp_qty"));
                Item_item.setIMAGE_URL("http://ecx.images-amazon.com/images/I/5169e67lGUL._SY355_.jpg");
                movieList.add(Item_item);
            }


             /*

                for (int j=0;j<prod.length();j++)
                {

                    JSONObject p=prod.getJSONObject(j);
                    Toast.makeText(getActivity(),p.getString("opp_code"),Toast.LENGTH_SHORT).show();

                }

            */


            adapter = new ViewDetAdapter(getActivity(), movieList);
            listView.setAdapter(adapter);



        } catch (JSONException e) {

            e.printStackTrace();
            Toast.makeText(getActivity(), "json catch", Toast.LENGTH_LONG).show();

        }


        //  }


    }


}
