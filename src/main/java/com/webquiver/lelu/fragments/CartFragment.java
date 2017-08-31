package com.webquiver.lelu.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.webquiver.lelu.CartActivity;
import com.webquiver.lelu.HomeActivity;
import com.webquiver.lelu.ItemActivity;
import com.webquiver.lelu.LoginActivity;
import com.webquiver.lelu.R;
import com.webquiver.lelu.RegisterActivity;
import com.webquiver.lelu.adapters.CartAdapter;
import com.webquiver.lelu.classes.AddressItem;
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

import de.greenrobot.event.EventBus;

/**
 * Created by WebQuiver 04 on 7/24/2017.
 */

public class CartFragment extends android.app.Fragment {

    private static CartFragment cartFragment = null;
    private static final String url = "https://api.myjson.com/bins/15eqh3";
    private ProgressDialog pDialog;
    private List<CartItem> movieList;
    private ListView listView;
    private CartAdapter adapter;
    private RequestQueue requestQueue;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    AppCompatButton buttonSHOPNOW;

    LinearLayout lyt;

    TextView continu;

    int numofitems;


    TextView BIGTOTAL,BAGDISCOUNT,TOTALPAYABLE;

    int i;

    double bigtotal=0,total=0,bagdiscount;



    AlertDialog.Builder alert;
    AlertDialog alertDialog;



    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        container.removeAllViews();


        View rootView = inflater.inflate(
                R.layout.cart_frag, container, false);

        movieList= new ArrayList<CartItem>();
        listView = (ListView) rootView.findViewById(R.id.cartlist);
        adapter = new CartAdapter(getActivity(), movieList);
        listView.setAdapter(adapter);
lyt=(LinearLayout)rootView.findViewById(R.id.lyt);
        pref = this.getActivity().getSharedPreferences(SessionManagement.PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();

        BAGDISCOUNT=(TextView)rootView.findViewById(R.id.bagdicount_id);
        BIGTOTAL=(TextView)rootView.findViewById(R.id.bigtotaltxt_id);
        TOTALPAYABLE=(TextView)rootView.findViewById(R.id.totalpayable_id);

try {
    EventBus.getDefault().register(this);
}
catch (Exception e)
{
    //
}

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Intent intent=new Intent(getActivity(),ItemActivity.class);
                startActivity(intent);
                getActivity().finish();


            }
        });



/* DUMMY DATAS
        CartItem C_item = new CartItem();
        C_item.setNAME("Baybee BMW");
        C_item.setIMAGE_URL("http://ecx.images-amazon.com/images/I/5169e67lGUL._SY355_.jpg");
        C_item.setQUANTITY(1);onclic
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



        ImageView bckBTN=(ImageView)rootView.findViewById(R.id.bckBTTN_cartfrag_id);
        bckBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent openFragmentBIntent = new Intent(getActivity(), HomeActivity.class);
                    openFragmentBIntent.putExtra("OPEN_FRAGMENT_B", "yes");
                    startActivity(openFragmentBIntent);
                    getActivity().finish();

            }
        });

        continu=(TextView)rootView.findViewById(R.id.continueBTN_id);
        continu.setVisibility(View.INVISIBLE);

    //    adapter.notifyDataSetChanged();

              continu.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              //fragment
            //  Fragment fr = null;
           //   FragmentManager fm = null;
            //  View selectedView = null;


              //fragment
            //  fm = getFragmentManager();
         //     FragmentTransaction fragmentTransaction = fm.beginTransaction();
           //   fragmentTransaction.replace(R.id.cart_FL, AddressFragment.getInstance());
          //    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
           //   fragmentTransaction.addToBackStack("cart");
           //   fragmentTransaction.commit();



              Bundle bundle = new Bundle();
              bundle.putString("bigtotal",String.valueOf(total));
              bundle.putString("totalpayable",String.valueOf(bigtotal));
              bundle.putString("numofitems",String.valueOf(numofitems));
              AddressFragment showSelectedADDR = new AddressFragment();
              showSelectedADDR.setArguments(bundle);
              FragmentManager fm = null;
              fm = getFragmentManager();
              FragmentTransaction fragmentTransaction = fm.beginTransaction();
              fragmentTransaction.replace(R.id.cart_FL, showSelectedADDR);
              fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
              fragmentTransaction.addToBackStack("cart");
              fragmentTransaction.commit();



          }
      });


        LayoutInflater li = LayoutInflater.from(getActivity());

        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.something_went_wrong, null);
        alert = new AlertDialog.Builder(getActivity());
        alert.setView(confirmDialog);
        alert.setCancelable(false);
        Button buttonSave = (Button) confirmDialog.findViewById(R.id.buttonretry);
        alertDialog = alert.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSHAKE;

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getall();

            }
        });


        getall();

        return rootView;

    }
    public static CartFragment getInstance() {
        if (cartFragment == null) {
            cartFragment = new CartFragment();
        }
        return cartFragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cartFragment = null;
    }


    public void getall() {

        requestQueue = Volley.newRequestQueue(getActivity());

    //    final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading", "Please wait...", false, false);
        final Dialog loading = new Dialog(getActivity());
        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading.setContentView(R.layout.custom_dialog_progress_loggingin);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.setCancelable(false);
        TextView t=(TextView)loading.findViewById(R.id.txt);
        t.setText("Loading");
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CART_GET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        lyt.setVisibility(View.VISIBLE);
                        continu.setVisibility(View.VISIBLE);
                        loading.dismiss();
                        alertDialog.dismiss();
                        Toast.makeText(getActivity(), "response", Toast.LENGTH_LONG).show();

                        try {

                            JSONObject jsonResponse = new JSONObject(response);

                            //   JSONArray jsonArray=new JSONArray(response);

                            if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                        showCART(response);

                            } else if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("failed")) {

                              // Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();

                                lyt.setVisibility(View.INVISIBLE);

                                LayoutInflater li = LayoutInflater.from(getActivity());
                                //Creating a view to get the dialog box
                                View confirmDialog = li.inflate(R.layout.cartempty_layout, null);

                                //Initizliaing confirm button fo dialog box and edittext of dialog box
                                buttonSHOPNOW = (AppCompatButton) confirmDialog.findViewById(R.id.buttonShop);

                                //Creating an alertdialog builder
                                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                                //Adding our dialog box to the view of alert dialog
                                alert.setView(confirmDialog);
                                alert.setCancelable(false);

                                //Creating an alert dialog
                                final AlertDialog alertDialog = alert.create();

                                //Displaying the alert dialog
                                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSHAKE;
                                alertDialog.show();

                                //On the click of the confirm button from alert dialog
                                buttonSHOPNOW.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //Hiding the alert dialog
                                        alertDialog.dismiss();
                                        Intent intent=new Intent(getActivity(),HomeActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }

                                });

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
                        alertDialog.show();

                        Toast.makeText(getActivity(), "error1", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put(Config.KEY_ADDR_MOBILE,  pref.getString(SessionManagement.KEY_PHONE,""));
                return params;
            }
        };

        //Adding request the the queue
        requestQueue.add(stringRequest);

    }

    public void showCART(String jsonArray) throws JSONException {
        JSONObject json = new JSONObject(jsonArray);
        JSONArray arr = json.getJSONArray("products");
        movieList= new ArrayList<CartItem>();
        bigtotal=0;total=0;bagdiscount=0;
        numofitems=arr.length();

        for (i = 0; i < arr.length(); i++) {

            try {

                JSONObject tt = arr.getJSONObject(i);

                CartItem C_item5 = new CartItem();
                C_item5.setNAME(tt.getString("i_name"));
                //C_item5.setDESC("default");
                C_item5.setQUANTITY(Integer.parseInt(tt.getString("cp_qty")));
                C_item5.setREALPRICE(tt.getString("i_retailPrice"));
                C_item5.setPRICE(tt.getString("i_salesPrice"));
                C_item5.setIMAGE_URL(tt.getString("i_image"));
                C_item5.setPRODUCT_ID(tt.getString("cp_code"));

                bigtotal= bigtotal + Double.parseDouble(tt.getString("i_salesPrice"))*Double.parseDouble(tt.getString("cp_qty"));             //change
                total=total + Double.parseDouble(tt.getString("i_retailPrice"))*Double.parseDouble(tt.getString("cp_qty"));

                movieList.add(C_item5);



            } catch (JSONException e) {

                e.printStackTrace();
                Toast.makeText(getActivity(), "json catch", Toast.LENGTH_LONG).show();

            }
        }

        bagdiscount = bigtotal - total;

        BIGTOTAL.setText("\u20B9 "+String.valueOf(total));
        BAGDISCOUNT.setText("\u20B9 "+String.valueOf(bagdiscount));
        TOTALPAYABLE.setText("\u20B9 "+String.valueOf(bigtotal));

        adapter = new CartAdapter(getActivity(), movieList);         //.subList(i-1, i)
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }




    public void onEvent(CartAdapter.HelloWorldEvent event){


        getall();


    }





}
