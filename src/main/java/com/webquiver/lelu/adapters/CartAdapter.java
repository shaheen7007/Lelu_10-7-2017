package com.webquiver.lelu.adapters;
/**
 * Created by WebQuiver 04 on 7/21/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.webquiver.lelu.R;
import com.webquiver.lelu.classes.AppController;
import com.webquiver.lelu.classes.CartItem;

import java.util.List;

public class CartAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<CartItem> cartitems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CartAdapter(Activity activity, List<CartItem> movieItems) {
        this.activity = activity;
        this.cartitems = movieItems;
    }

    @Override
    public int getCount() {
        return cartitems.size();
    }

    @Override
    public Object getItem(int location) {
        return cartitems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.cartitem_layout, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.cartItemImage_id);
        TextView title = (TextView) convertView.findViewById(R.id.cartItemName_id);
        TextView rating = (TextView) convertView.findViewById(R.id.cartItemQtytxt_id);
        TextView genre = (TextView) convertView.findViewById(R.id.cartItemRealprice_id);
        TextView year = (TextView) convertView.findViewById(R.id.cartItemPriceTxt_id);

        // getting movie data for the row
        CartItem m = cartitems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getIMAGE_URL(), imageLoader);

        // title
        title.setText(m.getNAME());

        // rating
//        rating.setText((m.getQUANTITY()));

        // genre

      //  genre.setText(m.getPRICE());

        // release year
    //    year.setText(m.getPRICE());

        return convertView;
    }

}