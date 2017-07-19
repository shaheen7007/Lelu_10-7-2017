package com.webquiver.lelu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;
import com.webquiver.lelu.adapters.Banner_Adapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ItemActivity extends AppCompatActivity {


    //images slider
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setCollapsible(false);
        setSupportActionBar(toolbar);


        //custom toolbar
        ActionBar ab = getSupportActionBar();
        LayoutInflater li = LayoutInflater.from(this);
        View customView = li.inflate(R.layout.custombar2_itemactivity, null);
        ab.setCustomView(customView);
        ab.setDisplayShowCustomEnabled(true);

        Intent iin = getIntent();

        //image slider
        ArrayList<String> test = iin.getStringArrayListExtra("images");
        init(test);


        Bundle b = iin.getExtras();

        if (b != null) {
            //   String j =(String) b.get("name");

            Toast.makeText(this, String.valueOf(b.get("name")) + "\n" + String.valueOf(b.get("color")) + "\n" + String.valueOf(b.get("image")), Toast.LENGTH_LONG).show();

        }

    }

    private void init(ArrayList<String> test) {



        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new Banner_Adapter(ItemActivity.this,test));


        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(3 * density);

        NUM_PAGES =test.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
       /*
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 5000);
        */

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }
            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }




}
