package com.webquiver.lelu.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.webquiver.lelu.R;
import com.webquiver.lelu.adapters.CartAdapter;
import com.webquiver.lelu.classes.CartItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WebQuiver 04 on 7/24/2017.
 */

public class CartFragment extends android.app.Fragment {

    private static CartFragment cartFragment = null;
    private static final String url = "https://api.myjson.com/bins/15eqh3";
    private ProgressDialog pDialog;
    private List<CartItem> movieList = new ArrayList<CartItem>();
    private ListView listView;
    private CartAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.cart_frag, container, false);

        listView = (ListView) rootView.findViewById(R.id.cartlist);
        adapter = new CartAdapter(getActivity(), movieList);
        listView.setAdapter(adapter);



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


        TextView continu=(TextView)rootView.findViewById(R.id.continueBTN_id);

      continu.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              //fragment
              Fragment fr = null;
              FragmentManager fm = null;
              View selectedView = null;

              //fragment
              fm = getFragmentManager();
              FragmentTransaction fragmentTransaction = fm.beginTransaction();
              fragmentTransaction.replace(R.id.cart_FL, AddressFragment.getInstance());
              fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
              fragmentTransaction.addToBackStack("cart");
              //change
              fragmentTransaction.commit();
          }
      });


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
}
