package com.webquiver.lelu;

        import android.app.Activity;
        import android.app.Fragment;
        import android.app.FragmentManager;
        import android.app.FragmentTransaction;
        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.ListView;

        import com.webquiver.lelu.adapters.CartAdapter;
        import com.webquiver.lelu.classes.CartItem;
        import com.webquiver.lelu.fragments.CartFragment;

        import java.util.ArrayList;
        import java.util.List;

public class CartActivity extends AppCompatActivity {

    public static Activity fa;

    @Override
    public void onBackPressed() {
/*
     //   if (getIntent().hasExtra("home")) {

            Intent openFragmentBIntent = new Intent(this, HomeActivity.class);
            openFragmentBIntent.putExtra("OPEN_FRAGMENT_B", "yes");
            overridePendingTransition(android.R.anim.fade_out,android.R.anim.fade_in);
            startActivity(openFragmentBIntent);

        try {
            SearchActivity.fa.finish();
        }
        catch (Exception e)
        {
            //
        }
            finish();


   //     } else {

           //    Intent openFragmentBIntent = new Intent(this, ItemActivity.class);
          //   openFragmentBIntent.putExtra("cart", "cart");
         //   startActivity(openFragmentBIntent);
        //   finish();

        //    }

        */
        super.onBackPressed();
    }

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



        fa=this;




        //fragment
        Fragment fr = null;
        FragmentManager fm = null;
        View selectedView = null;



        //fragment
        fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.cart_FL, CartFragment.getInstance());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
     //   fragmentTransaction.addToBackStack("ct");
        fragmentTransaction.commit();


        // Creating volley request here


        //dummy data


    }

    // Adding request to request queue

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