package com.webquiver.lelu.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.webquiver.lelu.R;
import com.webquiver.lelu.classes.AppController;
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
 * Created by WebQuiver 04 on 8/2/2017.
 */

public class OrderAdapter extends BaseAdapter

{
    private Activity activity;
    RequestQueue requestQueue;
    SharedPreferences pref;
    private LayoutInflater inflater;
    private List<ODRItem> cartitems;
    private List<CartItem> movieList;
    private ViewDetAdapter adapter;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    TextView WriteReview;
    TextView close;


    ListView listView;
    AlertDialog alertDialog;

    public OrderAdapter(Activity activity, List<ODRItem> cartitems) {
        this.activity = activity;
        this.cartitems = cartitems;

    }

    @Override
    public int getCount() {
        return cartitems.size();
    }

    @Override
    public Object getItem(int location) {
        return cartitems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        requestQueue = Volley.newRequestQueue(activity);            //
        pref = activity.getSharedPreferences(SessionManagement.PREF_NAME, Context.MODE_PRIVATE);


        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.orderitem_layout, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        final NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.cartItemImage_id);
        TextView Order_name = (TextView) convertView.findViewById(R.id.OrderitemName_id);
        TextView Order_ID = (TextView) convertView.findViewById(R.id.OrderitemIDTxt_id);
        TextView Order_Status = (TextView) convertView.findViewById(R.id.statuswithdate_id);
        TextView viewdet = (TextView) convertView.findViewById(R.id.vewdtTXT_id);
        //..TextView Order_Date = (TextView) convertView.findViewById(R.id.OrderitemDate_id);

        WriteReview = (TextView) convertView.findViewById(R.id.writereviewTxt_id);
        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingbar_id);

        movieList=new ArrayList<CartItem>();


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                final String rateValue = String.valueOf(ratingBar.getRating());
                System.out.println("Rate for Module is" + rateValue);


                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ODR_RATING_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {

                                    JSONObject jsonResponse = new JSONObject(response);
                                    if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                        Toast.makeText(activity, "Thank you for Rating", Toast.LENGTH_SHORT).show();

                                    } else {

                                        Toast.makeText(activity, "Failed to submit rating", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(activity, "error1", Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        //Adding the parameters to the request
                        params.put(Config.KEY_PHONE, pref.getString(SessionManagement.KEY_PHONE, ""));
                        params.put(Config.KEY_ORDER_ID, cartitems.get(position).getODR_ID());
                        params.put(Config.KEY_ORDER_RATING, rateValue);
                        return params;
                    }
                };

                //Adding request the the queue
                requestQueue.add(stringRequest);

            }
        });

        viewdet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                movieList=new ArrayList<CartItem>();

                LayoutInflater li = LayoutInflater.from(v.getRootView().getContext());
                final View confirmDialog = li.inflate(R.layout.viewdet_dlg_layout, null);

                AlertDialog.Builder alert = new AlertDialog.Builder(v.getRootView().getContext());
                alert.setView(confirmDialog);
                alert.setCancelable(true);
                listView = (ListView) confirmDialog.findViewById(R.id.listview_id);
                close=(TextView)confirmDialog.findViewById(R.id.closeTXT_id);

                alertDialog = alert.create();

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();

                    }
                });


                getall(position);


            }
        });

        WriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LayoutInflater li = LayoutInflater.from(v.getRootView().getContext());
                final View confirmDialog = li.inflate(R.layout.feedback_dlg_layout, null);

                AlertDialog.Builder alert = new AlertDialog.Builder(v.getRootView().getContext());
                alert.setView(confirmDialog);
                alert.setCancelable(true);
                Button buttonSave = (Button) confirmDialog.findViewById(R.id.buttonSave);
                final Button buttonCancel = (Button) confirmDialog.findViewById(R.id.buttonCancel);

                final AlertDialog alertDialog = alert.create();
               // alertDialog.setTitle("Order Details");
                alertDialog.show();

                buttonSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final EditText new_QTY = (EditText) confirmDialog.findViewById(R.id.qtyET_DLG);

                        if (new_QTY.getText().toString().equals("")) {

                            new_QTY.setError("please write review and submit");

                        } else {

                            final ProgressDialog loading = ProgressDialog.show(activity, "Submitting your review", "Please wait...", false, false);
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ODR_REVIEW_URL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            loading.dismiss();

                                            try {

                                                JSONObject jsonResponse = new JSONObject(response);
                                                if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {
                                                    alertDialog.dismiss();
                                                    Toast.makeText(activity, "Review submitted", Toast.LENGTH_SHORT).show();

                                                } else {

                                                    alertDialog.dismiss();
                                                    Toast.makeText(activity, "Failed to submit your review", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                alertDialog.dismiss();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            loading.dismiss();
                                            //
                                            Toast.makeText(activity, "error1", Toast.LENGTH_LONG).show();
                                        }
                                    }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    //Adding the parameters to the request
                                    params.put(Config.KEY_PHONE, pref.getString(SessionManagement.KEY_PHONE, ""));
                                    params.put(Config.KEY_ORDER_ID, cartitems.get(position).getODR_ID());
                                    params.put(Config.KEY_ORDER_REVIEW, new_QTY.getText().toString());
                                    return params;
                                }
                            };

                            //Adding request the the queue
                            requestQueue.add(stringRequest);

                        }
                    }
                });

                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();

                    }
                });


            }
        });


        if (getCount() == 0) {

            Toast.makeText(activity, "You haven't placed any orders yet", Toast.LENGTH_LONG).show();

        }


        // getting ccart data for the row
        ODRItem m = cartitems.get(position);

        // thumbnail image
        //   thumbNail.setImageUrl(m.getIMAGE_URL(), imageLoader);

        if (m.getNumberOfProducts() == 1) {

            Order_name.setText("P Name");
            thumbNail.setImageUrl("http://ecx.images-amazon.com/images/I/5169e67lGUL._SY355_.jpg", imageLoader);

        } else {
            viewdet.setVisibility(View.VISIBLE);
            Order_name.setText(String.valueOf(m.getNumberOfProducts()) + " items");
            thumbNail.setImageUrl("http://www.ajrockguitar.com/images/order.jpg", imageLoader);
        }
        Order_ID.setText("Order id #" + m.getODR_ID());
        Order_Status.setText(m.getSTATUS());
        // Order_Status.setText(m.getDATE());

        final View finalConvertView = convertView;

   /*     plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(activity, cartitems.get(position).getPRODUCT_ID(), Toast.LENGTH_LONG).show();

                final ProgressDialog loading = ProgressDialog.show(activity, "Adding to cart", "Please wait...", false, false);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CART_ADD_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                loading.dismiss();

                                try {

                                    JSONObject jsonResponse = new JSONObject(response);
                                    if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                        cartitems.get(position).setQUANTITY(cartitems.get(position).getQUANTITY() + 1);
                                        getView(position, finalConvertView, parent);
                                        //        Toast.makeText(activity,"Quantity updated",Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(activity, "Failed to update quantity", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    loading.dismiss();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                loading.dismiss();
                                //
                                Toast.makeText(activity, "error1", Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        //Adding the parameters to the request
                        params.put(Config.KEY_PHONE, pref.getString(SessionManagement.KEY_PHONE, ""));
                        params.put(Config.KEY_CART_ProdId, cartitems.get(position).getPRODUCT_ID());
                        params.put(Config.KEY_CART_ProdQty, String.valueOf(cartitems.get(position).getQUANTITY() + 1));
                        return params;
                    }
                };

                //Adding request the the queue
                requestQueue.add(stringRequest);


            }
        });

     */


        /*
        item_qty.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
*/


        return convertView;
    }


    public void getall(int pos) {
        final int posi=pos;

        requestQueue = Volley.newRequestQueue(activity);

     //   final ProgressDialog loading = ProgressDialog.show(activity, "Loading", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ODR_GET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    //    loading.dismiss();
                        Toast.makeText(activity, "response", Toast.LENGTH_LONG).show();

                        try {

                            JSONObject jsonResponse = new JSONObject(response);

                            //   JSONArray jsonArray=new JSONArray(response);

                            if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {


                                showODR(response,posi);


                            } else if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("failed")) {

                                Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();

                            } else {

                                Toast.makeText(activity, "Invalid user", Toast.LENGTH_LONG).show();
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

                        Toast.makeText(activity, "error1", Toast.LENGTH_LONG).show();

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
                //  ODRItem C_item5 = new ODRItem();
                //  C_item5.setODR_ID(tt.getString("order_place_id"));
                //  C_item5.setNAME(tt.getString("order_place_id"));
                //  C_item5.setSTATUS(tt.getString("op_status"));


                JSONArray prod = tt.getJSONArray("products");
                // C_item5.setNumberOfProducts(prod.length());



                 //C_item5.setODR_ID(tt.getString("order_place_id"));
                //  C_item5.setNAME(tt.getString("order_place_id"));

                for (int j = 0; j < prod.length(); j++) {
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


                adapter = new ViewDetAdapter(activity, movieList);
                listView.setAdapter(adapter);
                alertDialog.show();


            } catch (JSONException e) {

                e.printStackTrace();
                Toast.makeText(activity, "json catch", Toast.LENGTH_LONG).show();

            }
      //  }


    }


}