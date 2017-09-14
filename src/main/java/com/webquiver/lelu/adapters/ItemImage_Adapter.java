package com.webquiver.lelu.adapters;

/**
 * Created by WebQuiver 04 on 7/10/2017.
 */

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.webquiver.lelu.R;
import com.webquiver.lelu.classes.Config;

import java.util.ArrayList;


public class ItemImage_Adapter extends PagerAdapter {


    Context context;
    LayoutInflater layoutInflater;
    ArrayList<String> arrayList;

    public ItemImage_Adapter(Context context, ArrayList<String> IMAGES) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList = IMAGES;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public int getCount() {
        if(arrayList != null){
            return arrayList.size();
        }
        return 0;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(R.layout.banner1_layout, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.image);

    //    Picasso.with(context).load(arrayList.get(position))
     //           .into(imageView);

        Glide.with(context).load(Config.BASE_URLwithoutslash+arrayList.get(position)).into(imageView);


        container.addView(itemView);

        return itemView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {

    }

    @Override
    public Parcelable saveState() {
        return null;
    }







}