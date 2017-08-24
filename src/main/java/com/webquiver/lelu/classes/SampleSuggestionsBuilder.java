package com.webquiver.lelu.classes;

import android.content.Context;

import org.cryse.widget.persistentsearch.SearchItem;
import org.cryse.widget.persistentsearch.SearchSuggestionsBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SampleSuggestionsBuilder implements SearchSuggestionsBuilder {
    private Context mContext;
    private List<SearchItem> mHistorySuggestions = new ArrayList<SearchItem>();;
    private List<SearchItem> mHistorySuggestions1 = new ArrayList<SearchItem>();;
    Set<SearchItem> items1 = new HashSet<>();


    Set<SearchItem> listsetsug = new HashSet<>();


    ArrayList<String> sug = new ArrayList<>();


    public SampleSuggestionsBuilder(Context context,String f,String s,String t) {
        this.mContext = context;
        createHistorys(f,s,t);
    }




    public SampleSuggestionsBuilder(Context context,String f,String s,String t,ArrayList<String> sugg) {
        this.mContext = context;
        sug=sugg;


        createHistorys(f,s,t);



        for (int i=0;i<sug.size();i++)
        {
            SearchItem itemsug = new SearchItem(
                    sug.get(i),
                    sug.get(i),
                    SearchItem.TYPE_SEARCH_ITEM_HISTORY
            );
            listsetsug.add(itemsug);
        }


    }

    private void createHistorys(String f,String s,String t) {

        if (!f.equals("NULL")) {
            SearchItem item1 = new SearchItem(
                    f,
                    f,
                    SearchItem.TYPE_SEARCH_ITEM_HISTORY
            );
            mHistorySuggestions.add(item1);
        }

        if (!s.equals("NULL")) {
            SearchItem item2 = new SearchItem(
                    s,
                    s,
                    SearchItem.TYPE_SEARCH_ITEM_HISTORY
            );
            mHistorySuggestions.add(item2);
        }
        if (!t.equals("NULL")) {
            SearchItem item3 = new SearchItem(
                    t,
                    t,
                    SearchItem.TYPE_SEARCH_ITEM_HISTORY
            );
            mHistorySuggestions.add(item3);
        }
    }

    @Override
    public Collection<SearchItem> buildEmptySearchSuggestion(int maxCount) {
        List<SearchItem> items = new ArrayList<SearchItem>();
        items.addAll(mHistorySuggestions);
        return items;
    }

    @Override
    public Collection<SearchItem> buildSearchSuggestion(int maxCount, String query) {
        List<SearchItem> items = new ArrayList<SearchItem>();
        /*
        if(query.startsWith("@")) {
            SearchItem peopleSuggestion = new SearchItem(
                    "Search People: " + query.substring(1),
                    query,
                    SearchItem.TYPE_SEARCH_ITEM_SUGGESTION
            );
            items.add(peopleSuggestion);
        } else if(query.startsWith("#")) {
            SearchItem toppicSuggestion = new SearchItem(
                    "Search Topic: " + query.substring(1),
                    query,
                    SearchItem.TYPE_SEARCH_ITEM_SUGGESTION
            );
            items.add(toppicSuggestion);
        }
         */
         //else {
            SearchItem peopleSuggestion = new SearchItem(
                    query,
                      query,
                    SearchItem.TYPE_SEARCH_ITEM_SUGGESTION
            );
            items.add(peopleSuggestion);
           // SearchItem toppicSuggestion = new SearchItem(
          //          "Search Topic: " + query,
           //         "#" + query,
           //         SearchItem.TYPE_SEARCH_ITEM_SUGGESTION
         //   );
         //   items.add(toppicSuggestion);
    //    }

      for(SearchItem item :listsetsug) {

          if(item.getValue().toLowerCase().contains(query.toLowerCase())) {
             // items.add(item);

              SearchItem peopleSuggestion1 = new SearchItem(
                      item.getValue(),
                      item.getValue(),
                      SearchItem.TYPE_SEARCH_ITEM_SUGGESTION
              );
              items.add(peopleSuggestion1);

        }
    }
        return items;
    }

}

