package com.webquiver.lelu;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.webquiver.lelu.classes.Config;
import com.webquiver.lelu.classes.SampleSuggestionsBuilder;
import com.webquiver.lelu.fragments.SearchResultFragment;

import org.cryse.widget.persistentsearch.PersistentSearchView;
import org.cryse.widget.persistentsearch.SearchItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {



    @Override
    public void onBackPressed()
    {

        Intent openFragmentBIntent = new Intent(this, HomeActivity.class);
        openFragmentBIntent.putExtra("OPEN_FRAGMENT_B", "yes");
        startActivity(openFragmentBIntent);
        finish();
    }


    private ArrayList<String> searchnames;

    //search
    private PersistentSearchView mSearchView;

    private SharedPreferences searchhistory;
    SharedPreferences.Editor search_historyEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchactivitymain);

        //fragment
        Fragment fr = null;
        FragmentManager fm = null;
        View selectedView = null;

        //fragment
        fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.search_frag_container, SearchResultFragment.getInstance());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();


        searchhistory = this.getSharedPreferences(Config.SearchPref, Context.MODE_PRIVATE);
        search_historyEditor=searchhistory.edit();


        mSearchView = (PersistentSearchView) findViewById(R.id.searchview);
       // mSearchView.setSuggestionBuilder(new SampleSuggestionsBuilder(this,searchhistory.getString(Config.first_suggestion,"NULL"),searchhistory.getString(Config.second_suggestion,"NULL"),searchhistory.getString(Config.third_suggestion,"NULL")));
        searchnames = new ArrayList<>();
        getSearchData();


     //   mSearchView.openSearch();
        mSearchView.clearSuggestions();

        mSearchView.setHomeButtonListener(new PersistentSearchView.HomeButtonListener() {

            @Override
            public void onHomeButtonClick() {
                //Hamburger has been clicked
               mSearchView.setVisibility(View.GONE);
                Intent intent=new Intent(SearchActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();

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
        JsonArrayRequest bannerjsonArrayRequest = new JsonArrayRequest("https://api.myjson.com/bins/1fgsw9",
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
                searchnames.add(obj.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
      //  init();

        mSearchView.setSuggestionBuilder(new SampleSuggestionsBuilder(this,searchhistory.getString(Config.first_suggestion,"NULL"),searchhistory.getString(Config.second_suggestion,"NULL"),searchhistory.getString(Config.third_suggestion,"NULL"), searchnames));



    }



}
