package com.webquiver.lelu.classes;

/**
 * Created by WebQuiver 04 on 7/10/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.webquiver.lelu.R;

import java.util.ArrayList;


public class GridViewAdapter extends BaseAdapter {

    //Imageloader to load images
    private ImageLoader imageLoader;

    //Context
    private Context context;

    //Array List that would contain the urls and the titles for the images
    private ArrayList<String> images;
    private ArrayList<String> names;
    private ArrayList<String> color;

    public GridViewAdapter (Context context, ArrayList<String> images, ArrayList<String> names, ArrayList<String> color){
        //Getting all the values
        this.context = context;
        this.images = images;
        this.names = names;
        this.color = color;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Creating a linear layout
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View grid;
        if (convertView == null) {

            grid = new View(context);
            grid = inflater.inflate(R.layout.grid_single, null);
            TextView name = (TextView) grid.findViewById(R.id.grid_text1);
            TextView clr = (TextView) grid.findViewById(R.id.grid_text2);
            NetworkImageView imageView = (NetworkImageView) grid.findViewById(R.id.grid_image);
            imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
            imageLoader.get(images.get(position), ImageLoader.getImageListener(imageView, R.drawable.blank_image,R.drawable.blank_image));
            imageView.setImageUrl(images.get(position),imageLoader);
            name.setText(names.get(position));
            clr.setText(color.get(position));
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
