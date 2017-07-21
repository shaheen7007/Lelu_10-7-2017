package com.webquiver.lelu;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.webquiver.lelu.adapters.CartAdapter;
import com.webquiver.lelu.classes.AppController;
import com.webquiver.lelu.classes.CartItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {


    private static final String TAG = CartActivity.class.getSimpleName();

    // Movies json url
    private static final String url = "https://api.myjson.com/bins/15eqh3";
    private ProgressDialog pDialog;
    private List<CartItem> movieList = new ArrayList<CartItem>();
    private ListView listView;
    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

            listView = (ListView) findViewById(R.id.cartlist);
            adapter = new CartAdapter(this, movieList);
            listView.setAdapter(adapter);

            pDialog = new ProgressDialog(this);
            // Showing progress dialog before making http request
            pDialog.setMessage("Loading...");
            pDialog.show();
            // Creating volley request obj
            JsonArrayRequest movieReq = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, response.toString());
                            hidePDialog();

                            Toast.makeText(CartActivity.this,"respone",Toast.LENGTH_LONG).show();

                            CartItem movie = new CartItem();
                            movie.setNAME("gdhfh");
                            movie.setIMAGE_URL("http://webmeup.com/upload/blog/mc_banner.jpg");
                            movie.setQUANTITY(1);
                            movieList.add(movie);


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

                            adapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    Toast.makeText(CartActivity.this,"no respone",Toast.LENGTH_LONG).show();

                    hidePDialog();

                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(movieReq);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            hidePDialog();
        }

        private void hidePDialog() {
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }
        }



}

