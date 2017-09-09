package com.webquiver.lelu;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.webquiver.lelu.fragments.OrderFragment;
import com.webquiver.lelu.fragments.WishListFragment;

public class WishListActivity extends AppCompatActivity {


    @Override
    public void onBackPressed() {

        //   if (getIntent().hasExtra("home")) {
/*
        Intent openFragmentBIntent = new Intent(this, HomeActivity.class);
      //  openFragmentBIntent.putExtra("OPEN_FRAGMENT_B", "yes");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(openFragmentBIntent);
        */
        finish();

        //     } else {
        //    Intent openFragmentBIntent = new Intent(this, ItemActivity.class);
        //   openFragmentBIntent.putExtra("cart", "cart");
        //   startActivity(openFragmentBIntent);
        //   finish();

        //    }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        Fragment fr = null;
        FragmentManager fm = null;
        View selectedView = null;
        //fragment
        fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.WISH_FL, WishListFragment.getInstance());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ImageView backbtn=(ImageView)findViewById(R.id.bk_id);

        //change



        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Intent openFragmentBIntent = new Intent(WishListActivity.this, HomeActivity.class);
                //  openFragmentBIntent.putExtra("OPEN_FRAGMENT_B", "yes");
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(openFragmentBIntent);
                finish();
                */
                finish();

            }
        });

        fragmentTransaction.commit();

    }
}
