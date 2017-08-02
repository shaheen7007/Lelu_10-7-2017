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

import org.json.JSONException;
import org.json.JSONObject;

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
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        public OrderAdapter(Activity activity, List < ODRItem > cartitems) {
        this.activity = activity;
        this.cartitems = cartitems;

    }

        @Override
        public int getCount () {
        return cartitems.size();
    }

        @Override
        public Object getItem ( int location){
        return cartitems.get(location);
    }

        @Override
        public long getItemId ( int position){
        return position;
    }

        @Override
        public View getView ( final int position, View convertView,final ViewGroup parent){

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

        if (getCount() == 0) {

            Toast.makeText(activity, "You haven't placed any orders yet", Toast.LENGTH_LONG).show();

        }


        // getting ccart data for the row
        ODRItem m = cartitems.get(position);

        // thumbnail image
     //   thumbNail.setImageUrl(m.getIMAGE_URL(), imageLoader);

            if (m.getNumberOfProducts()==1)
            {

                Order_name.setText("P Name");
                thumbNail.setImageUrl("http://ecx.images-amazon.com/images/I/5169e67lGUL._SY355_.jpg", imageLoader);

            }

            else {
                viewdet.setVisibility(View.VISIBLE);
                Order_name.setText(String.valueOf(m.getNumberOfProducts()) + " items");
                thumbNail.setImageUrl("http://www.ajrockguitar.com/images/order.jpg",imageLoader);
            }
             Order_ID.setText("Order id #"+m.getODR_ID());
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
 }
