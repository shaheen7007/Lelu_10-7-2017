package com.webquiver.lelu.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;
import com.webquiver.lelu.HomeActivity;
import com.webquiver.lelu.ItemActivity;
import com.webquiver.lelu.ItemActivity2;
import com.webquiver.lelu.R;
import com.webquiver.lelu.SearchActivity;
import com.webquiver.lelu.adapters.ViewDetAdapter;
import com.webquiver.lelu.classes.CartItem;
import com.webquiver.lelu.classes.Config;
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

public class OrderDetFragment2 extends Fragment {

    private static OrderDetFragment2 cartFragment = null;

    private ProgressDialog pDialog;
    private List<CartItem> movieList;
    private ViewDetAdapter adapter;
    private RequestQueue requestQueue;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    LinearLayout lyt;

    TextView ordrID,ordrSTATUS,ordrPlaceDate,currentSTATUS,currentstatus_date;
    View line;
    ImageView secondtick;



    int i;
    int poss;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        container.removeAllViews();

        View rootView = inflater.inflate(
                R.layout.track_order_frag, container, false);


        lyt=(LinearLayout)rootView.findViewById(R.id.lyt);

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("test", "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK ) {

                    Toast.makeText(getActivity(), "bck", Toast.LENGTH_SHORT).show();

                    if (poss==999) {


                     //   try {

                  //          HomeActivity.fa.finish();

                    //    } catch (Exception e) {

                  //          Toast.makeText(getActivity(), "ctchhhhh", Toast.LENGTH_SHORT).show();

                 //       }
                        try {
                            ItemActivity.fa.finish();
                        } catch (Exception e) {
                            // Toast.makeText(getActivity(), "ctch", Toast.LENGTH_SHORT).show();
                        }
                        try {

                            ItemActivity2.fa.finish();
                        } catch (Exception e) {
                            //   Toast.makeText(getActivity(), "ctch", Toast.LENGTH_SHORT).show();
                        }
                        try {
                            SearchActivity.fa.finish();
                        } catch (Exception e) {
                            //   Toast.makeText(getActivity(), "ctch", Toast.LENGTH_SHORT).show();
                        }

                     //   Intent intent = new Intent(getActivity(), HomeActivity.class);
                    //    getActivity().startActivity(intent);
                        getActivity().finish();
                    }

                    else
                    {
                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    }


                    return true;
                } else {
                    return false;
                }
            }
        });

        ImageView bckBTN=(ImageView)rootView.findViewById(R.id.bk_id);
        bckBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           //     getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);


                if (poss==999) {


                    /*
                    Intent intent =new Intent(getActivity(), HomeActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().finish();
*/
                  //  getActivity().finish();


                    try {
                        HomeActivity.fa.finish();
                        ItemActivity.fa.finish();
                        ItemActivity2.fa.finish();
                        SearchActivity.fa.finish();
                    }
                    catch (Exception e)
                    {
                    //    Toast.makeText(getActivity(), "ctch", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent=new Intent(getActivity(),HomeActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().finish();




                }

                else
                {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                }





            }
        });






        movieList= new ArrayList<CartItem>();
      //  listView = (ListView) rootView.findViewById(R.id.prod_list);
    //    listView.setAdapter(adapter);
        pref = this.getActivity().getSharedPreferences(SessionManagement.PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();

        Bundle b=getArguments();

        poss=Integer.parseInt(b.getString("p","999"));

        ordrID=(TextView)rootView.findViewById(R.id.TRCK_OID);
        ordrSTATUS=(TextView)rootView.findViewById(R.id.TRCK_STATUS);
        ordrPlaceDate=(TextView)rootView.findViewById(R.id.TRCK_OrderPlaceDate);
        currentSTATUS=(TextView)rootView.findViewById(R.id.maintxt2);
        currentstatus_date=(TextView)rootView.findViewById(R.id.txt1);
        line=(View) rootView.findViewById(R.id.line_id);
        secondtick=(ImageView) rootView.findViewById(R.id.img2);
     //   ordrNAME=(TextView)rootView.findViewById(R.id.ADDR_name_id);
     //   ordrADDR1=(TextView)rootView.findViewById(R.id.ADDR_address1_id);
     //   ordrPLACE=(TextView)rootView.findViewById(R.id.ADDR_place_id);
     //   ordrDIST=(TextView)rootView.findViewById(R.id.ADDR_district_id);
      //  ordrPIN=(TextView)rootView.findViewById(R.id.ADDR_pin_id);
     //   ordrSTATE=(TextView)rootView.findViewById(R.id.ADDR_state_id);


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
    public static OrderDetFragment2 getInstance() {
        if (cartFragment == null) {
            cartFragment = new OrderDetFragment2();
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
                        //    loading.dismissr();
                    //   Toast.makeText(getActivity(),response, Toast.LENGTH_LONG).show();

                        lyt.setVisibility(View.VISIBLE);

                        try {

                            JSONObject jsonResponse = new JSONObject(response);

                            //   JSONArray jsonArray=new JSONArray(response);

                            if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {


                                showODR(response,posi);


                            } else if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("failed")) {

                          //      Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();



                                SuperActivityToast.create(getActivity(), new Style(), Style.TYPE_STANDARD)
                                        //     .setButtonText("Please click BACK again to exit")
                                        //     .setButtonIconResource(R.drawable.ic_undo)
                                        //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                                        //     .setProgressBarColor(Color.WHITE)
                                        .setText("You haven't placed any orders yet")
                                        .setDuration(Style.DURATION_LONG)
                                        .setFrame(Style.FRAME_STANDARD)
                                        .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                                        .setAnimations(Style.ANIMATIONS_POP).show();



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

            JSONObject tt;

            if (pos!=999) {
                 tt = arr.getJSONObject(pos);
            }
            else
            {
                tt = arr.getJSONObject(arr.length()-1);
            }

            String sts=tt.getString("op_status");


            String[] sp = sts.split(" ");
         //   System.out.println("second word is " + sp[1]);
//            System.out.println("third word is " + sp[2]);


            if (!sts.equals("")) {

                ordrSTATUS.setText(sp[0] + " on " + sp[1]);
                currentSTATUS.setText(sp[0]);
                currentstatus_date.setText("Order " + sp[0] + " on " + sp[1]);
            }

            else
            {

                ordrSTATUS.setText("Order placed on "+tt.getString("op_date"));
                currentSTATUS.setVisibility(View.INVISIBLE);
                currentstatus_date.setVisibility(View.INVISIBLE);
                line.setVisibility(View.INVISIBLE);
                secondtick.setVisibility(View.INVISIBLE);


            }

                ordrID.setText("Order id #"+tt.getString("order_place_id"));
                ordrPlaceDate.setText("Order placed on "+tt.getString("op_date"));


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


        } catch (JSONException e) {

            e.printStackTrace();
            Toast.makeText(getActivity(), "json catch", Toast.LENGTH_LONG).show();

        }


        //  }


    }


}
