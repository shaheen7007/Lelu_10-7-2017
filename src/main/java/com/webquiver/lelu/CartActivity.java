package com.webquiver.lelu;

        import android.app.Fragment;
        import android.app.FragmentManager;
        import android.app.FragmentTransaction;
        import android.app.ProgressDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.ListView;
        import android.widget.Toast;

        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.VolleyLog;
        import com.android.volley.toolbox.JsonArrayRequest;
        import com.webquiver.lelu.adapters.CartAdapter;
        import com.webquiver.lelu.classes.AppController;
        import com.webquiver.lelu.classes.CartItem;
        import com.webquiver.lelu.fragments.AddressFragment;
        import com.webquiver.lelu.fragments.CartFragment;
        import com.webquiver.lelu.fragments.HomeFragment;

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

        //fragment
        Fragment fr = null;
        FragmentManager fm = null;
        View selectedView = null;



        //fragment
        fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.cart_FL, CartFragment.getInstance());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        //change
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