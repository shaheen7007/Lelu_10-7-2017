package com.webquiver.lelu.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
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

public class OrderPlacedSuccessfuly extends Fragment {

    private static OrderPlacedSuccessfuly cartFragment = null;

    private ProgressDialog pDialog;
    private List<CartItem> movieList;
    private ViewDetAdapter adapter;
    private RequestQueue requestQueue;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    LinearLayout lyt;

    TextView ordrID,orderDetails;
    View line;
    ImageView secondtick;



    int i;
    int poss;
    String total,numofitems;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        container.removeAllViews();

        View rootView = inflater.inflate(
                R.layout.order_placeed_success_lyt, container, false);

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


        pref = this.getActivity().getSharedPreferences(SessionManagement.PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();

        Bundle b=getArguments();

        poss=Integer.parseInt(b.getString("p","999"));
        total=b.getString("total","defualt");
        numofitems=b.getString("numofitems","defualt");


        orderDetails=(TextView)rootView.findViewById(R.id.itemsnpricetxt_id);

        if (Integer.parseInt(numofitems)>1)

             orderDetails.setText("Total price for "+numofitems+" items: "+"\u20B9 "+total);

        else

            orderDetails.setText("Total price for "+numofitems+" item: "+"\u20B9 "+total);


    //  adapter.notifyDataSetChanged();

        return rootView;

    }
    public static OrderPlacedSuccessfuly getInstance() {
        if (cartFragment == null) {
            cartFragment = new OrderPlacedSuccessfuly();
        }
        return cartFragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cartFragment = null;
    }



}
