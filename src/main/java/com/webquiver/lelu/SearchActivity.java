package com.webquiver.lelu;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.webquiver.lelu.classes.Config;
import com.webquiver.lelu.classes.SampleSuggestionsBuilder;
import com.webquiver.lelu.fragments.CartFragment;
import com.webquiver.lelu.fragments.SearchResultFragment;

import org.cryse.widget.persistentsearch.PersistentSearchView;
import org.cryse.widget.persistentsearch.SearchItem;

public class SearchActivity extends AppCompatActivity {



    @Override
    public void onBackPressed()
    {

        Intent openFragmentBIntent = new Intent(this, HomeActivity.class);
        openFragmentBIntent.putExtra("OPEN_FRAGMENT_B", "yes");
        startActivity(openFragmentBIntent);
        finish();
    }


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
        mSearchView.setSuggestionBuilder(new SampleSuggestionsBuilder(this,searchhistory.getString(Config.first_suggestion,"NULL"),searchhistory.getString(Config.second_suggestion,"NULL"),searchhistory.getString(Config.third_suggestion,"NULL")));

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

                }

                @Override
                public void onSearchTermChanged(String term) {

                }

                @Override
                public void onSearchEditClosed() {

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






}
