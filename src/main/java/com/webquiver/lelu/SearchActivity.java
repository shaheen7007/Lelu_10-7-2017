package com.webquiver.lelu;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.kennyc.bottomsheet.BottomSheet;
import com.kennyc.bottomsheet.BottomSheetListener;
import com.webquiver.lelu.classes.Config;
import com.webquiver.lelu.classes.SampleSuggestionsBuilder;
import com.webquiver.lelu.fragments.AddressFragment;
import com.webquiver.lelu.fragments.FilterResultFragment;
import com.webquiver.lelu.fragments.SearchResultFragment;

import org.cryse.widget.persistentsearch.PersistentSearchView;
import org.cryse.widget.persistentsearch.SearchItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class SearchActivity extends AppCompatActivity {




    public static Activity fa;
    @Override
    public void onBackPressed()
    {

        /*
        Intent openFragmentBIntent = new Intent(this, HomeActivity.class);
        openFragmentBIntent.putExtra("OPEN_FRAGMENT_B", "yes");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(openFragmentBIntent);
        finish();
        */
        super.onBackPressed();

    }


    private ArrayList<String> searchnames;

    //search
    private PersistentSearchView mSearchView;

    private SharedPreferences searchhistory;
    SharedPreferences.Editor search_historyEditor;

    TextView filterbtn,sortbtn;
    LinearLayout sortfilter;

    BottomSheetListener bottomSheetListener;

    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchactivitymain);
        filterbtn=(TextView)findViewById(R.id.filterBT_id);
        sortbtn=(TextView)findViewById(R.id.SortBT_id);
        sortfilter=(LinearLayout)findViewById(R.id.sortfil_lyt_id);

//to finish activity
        fa = this;


        EventBus.getDefault().post(new HelloWorldEvent(""));       //search history update


        b=getIntent().getExtras();


        //bottom sheet
        bottomSheetListener=new BottomSheetListener() {
            @Override
            public void onSheetShown(@NonNull BottomSheet bottomSheet) {

              //  sortfilter.setVisibility(View.GONE);

            }

            @Override
            public void onSheetItemSelected(@NonNull BottomSheet bottomSheet, MenuItem menuItem) {

                    Bundle bundle = new Bundle();
               //     bundle.putString("searchterm",String.valueOf(b.getString("searchterm")));
                if (menuItem.getNumericShortcut()=='1') {
                    bundle.putString("rate1", "100");
                    bundle.putString("rate2", "200");
               //     Toast.makeText(SearchActivity.this,"1",Toast.LENGTH_LONG).show();

                }

                if (menuItem.getNumericShortcut()=='2') {
                    bundle.putString("rate1", "200");
                    bundle.putString("rate2", "500");
                }

                if (menuItem.getNumericShortcut()=='3') {
                    bundle.putString("rate1", "500");
                    bundle.putString("rate2", "1000");
                }

                if (menuItem.getNumericShortcut()=='4') {
                    bundle.putString("rate1", "1000");
                    bundle.putString("rate2", "5000");
                }
                if (menuItem.getNumericShortcut()=='5') {
                    bundle.putString("rate1", "5000");
                    bundle.putString("rate2", "200000");
                }

                if (menuItem.getNumericShortcut()=='a') {
                    bundle.putString("rate1", "asc");
                    bundle.putString("rate2", "200000");
                }

                if (menuItem.getNumericShortcut()=='d') {
                    bundle.putString("rate1", "desc");
                    bundle.putString("rate2", "200000");
                }


                    FilterResultFragment showSelectedADDR = new FilterResultFragment();
                    showSelectedADDR.setArguments(bundle);
                    FragmentManager fm = null;
                    fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.add(R.id.search_frag_container, showSelectedADDR);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    //  fragmentTransaction.addToBackStack("cart");
                    fragmentTransaction.commit();


            }

            @Override
            public void onSheetDismissed(@NonNull BottomSheet bottomSheet, @DismissEvent int i) {

             //   sortfilter.setVisibility(View.VISIBLE);
            }
        };



        filterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new BottomSheet.Builder(SearchActivity.this)
                        .setSheet(R.menu.bottomlist_menu)
                        .setTitle("Filter by Price")
                        .setListener(bottomSheetListener)
                        .show();

            }
        });

        sortbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new BottomSheet.Builder(SearchActivity.this)

                        .setSheet(R.menu.bottomlist_sort_menu)
                        .setTitle("Sort")
                        .setListener(bottomSheetListener)
                        .show();

            }
        });

        //fragment
    //    Fragment fr = null;
    //    FragmentManager fm = null;
     //   View selectedView = null;

        //fragment
   //     fm = getFragmentManager();
   //     FragmentTransaction fragmentTransaction = fm.beginTransaction();
   //     fragmentTransaction.add(R.id.search_frag_container, SearchResultFragment.getInstance());
    //    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
      //  fragmentTransaction.commit();


        Bundle bundle = new Bundle();
//        bundle.putString("searchterm",String.valueOf(b.getString("searchterm")));
        SearchResultFragment showSelectedADDR = new SearchResultFragment();
        showSelectedADDR.setArguments(bundle);
        FragmentManager fm = null;
        fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.search_frag_container, showSelectedADDR);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
      //  fragmentTransaction.addToBackStack("cart");
        fragmentTransaction.commit();


        searchhistory = this.getSharedPreferences(Config.SearchPref, Context.MODE_PRIVATE);
        search_historyEditor=searchhistory.edit();


        mSearchView = (PersistentSearchView) findViewById(R.id.searchview);
        // mSearchView.setSuggestionBuilder(new SampleSuggestionsBuilder(this,searchhistory.getString(Config.first_suggestion,"NULL"),searchhistory.getString(Config.second_suggestion,"NULL"),searchhistory.getString(Config.third_suggestion,"NULL")));
        searchnames = new ArrayList<>();
        getSearchData();

        mSearchView.populateEditText(String.valueOf(searchhistory.getString("searchterm","null")));

        // mSearchView.openSearch();
        mSearchView.clearSuggestions();

        mSearchView.setHomeButtonListener(new PersistentSearchView.HomeButtonListener() {

            @Override
            public void onHomeButtonClick() {
                //Hamburger has been clicked
                /*
                mSearchView.setVisibility(View.GONE);
                Intent intent=new Intent(SearchActivity.this,HomeActivity.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent);
                finish();
                */
                SearchActivity.super.onBackPressed();

            }

        });

        if (searchhistory.getString(Config.numofhistory, "NULL").equals("NULL")) {

            search_historyEditor.putString(Config.numofhistory,String.valueOf(0));
            search_historyEditor.commit();
        }
        else
        {

            mSearchView.setSearchListener(new PersistentSearchView.SearchListener() {

                @Override
                public boolean onSuggestion(SearchItem searchItem) {
                    Log.d("onSuggestion", searchItem.getTitle());
                    mSearchView.setSearchString(searchItem.getTitle(), true);
                    onSearch(searchItem.getTitle());
                    return false;
                }

                @Override
                public void onSearchCleared() {

                    mSearchView.clearSuggestions();
                }

                @Override
                public void onSearchTermChanged(String term) {

                    //  mSearchView.clearSuggestions();

                }

                @Override
                public void onSearchEditClosed() {

                    mSearchView.clearSuggestions();
                }

                @Override
                public boolean onSearchEditBackPressed() {
                    return false;
                }

                @Override
                public void onSearchExit() {

                }

                @Override
                public void onSearch(String string) {

                    //      Toast.makeText(HomeActivity.this, string +" Searched", Toast.LENGTH_LONG).show();


                    //fragment


                    //appBarLayout.setExpanded(false,true);

                    mSearchView.clearSuggestions();
                    mSearchView.closeSearch();

                     /*
                        Bundle bundle = new Bundle();
                        bundle.putString("search_item",string);
                        SearchResultFragment showSelectedADDR=new SearchResultFragment();
                        showSelectedADDR.setArguments(bundle);
                        FragmentManager fm = null;
                        fm = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        fragmentTransaction.replace(R.id.frag_container, showSelectedADDR);
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        fragmentTransaction.commit();

*/

                    if (string.equals(searchhistory.getString(Config.first_suggestion, "NULL"))) {
                        //nothing
                    } else if (string.equals(searchhistory.getString(Config.second_suggestion, "NULL"))) {
                        String temp;
                        temp = searchhistory.getString(Config.second_suggestion, "NULL");
                        search_historyEditor.putString(Config.second_suggestion, searchhistory.getString(Config.first_suggestion, "NULL"));
                        search_historyEditor.putString(Config.first_suggestion, temp);
                        search_historyEditor.commit();

                    } else {

                        search_historyEditor.putString(Config.third_suggestion, searchhistory.getString(Config.second_suggestion, "NULL"));
                        search_historyEditor.putString(Config.second_suggestion, searchhistory.getString(Config.first_suggestion, "NULL"));
                        search_historyEditor.putString(Config.first_suggestion, string);
                        search_historyEditor.commit();
                    }


                    Bundle bundle = new Bundle();
                  //  bundle.putString("searchterm",string);
                    search_historyEditor.putString("searchterm",string);
                    search_historyEditor.commit();
                    SearchResultFragment showSelectedADDR = new SearchResultFragment();
                    showSelectedADDR.setArguments(bundle);
                    FragmentManager fm = null;
                    fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.add(R.id.search_frag_container, showSelectedADDR);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    //  fragmentTransaction.addToBackStack("cart");
                    fragmentTransaction.commit();


                }

                @Override
                public void onSearchEditOpened() {

                }
            });
        }
    }



    public void searchhide()
    {
        mSearchView.setVisibility(View.GONE);
    }

    public void searchshow()
    {
        mSearchView.setVisibility(View.VISIBLE);
    }





    private void getSearchData(){
        //Showing a progress dialog while our app fetches the data from url
        //final ProgressDialog loading = ProgressDialog.show(this, "Please wait...","Fetching data...",false,false);
        //  progressBar.setVisibility(View.VISIBLE);

        //get banner json
        JsonArrayRequest bannerjsonArrayRequest = new JsonArrayRequest(Config.SEARCH_HISTORY_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing the progressdialog on response
                        //                       loading.dismiss();

                        //         progressBar.setVisibility(View.INVISIBLE);

                        //        SharedPreferences.Editor prefEdit = sharedPreferences.edit();
                        String jsonstring=response.toString();
                        //      prefEdit.putString(Config.JSONSTRING,jsonstring);
                        //       prefEdit.apply();
                        //Displaying banner
                        showsearchdata(response);
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SearchActivity.this,"No response from api",Toast.LENGTH_LONG).show();
                        //set from preference
                        JSONArray jsonArray = null;

                        //  progressBar.setVisibility(View.INVISIBLE);
                        // showbanner(jsonArray);

                    }
                }
        );


        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding our request to the queue
        requestQueue.add(bannerjsonArrayRequest);
    }



    private void showsearchdata(JSONArray jsonArray){

        for(int i = 0; i<jsonArray.length(); i++){

            JSONObject obj = null;
            try {

                obj = jsonArray.getJSONObject(i);
                searchnames.add(obj.getString("i_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //  init();

        mSearchView.setSuggestionBuilder(new SampleSuggestionsBuilder(this,searchhistory.getString(Config.first_suggestion,"NULL"),searchhistory.getString(Config.second_suggestion,"NULL"),searchhistory.getString(Config.third_suggestion,"NULL"), searchnames));



    }


    public class HelloWorldEvent {             //event to update cartnum
        public final String message;

        public HelloWorldEvent(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }




}
