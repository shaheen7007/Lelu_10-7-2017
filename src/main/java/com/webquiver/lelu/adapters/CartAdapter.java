package com.webquiver.lelu.adapters;
/**
 * Created by WebQuiver 04 on 7/21/2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    public CartAdapter(Activity activity, List<CartItem> cartitems) {
        this.activity = activity;
        this.cartitems = cartitems;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.cartitem_layout, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        final NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.cartItemImage_id);
        TextView item_name = (TextView) convertView.findViewById(R.id.cartItemName_id);
        final TextView item_qty = (TextView) convertView.findViewById(R.id.cartItemQtytxt_id);

        TextView item_realprice = (TextView) convertView.findViewById(R.id.cartItemRealprice_id);
        TextView item_price = (TextView) convertView.findViewById(R.id.cartItemPriceTxt_id);
        TextView plus = (TextView) convertView.findViewById(R.id.cartplustx_id);
        TextView minus = (TextView) convertView.findViewById(R.id.cartminus_id);

        //strike price
        item_realprice.setPaintFlags(item_realprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        // getting movie data for the row
        CartItem m = cartitems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getIMAGE_URL(), imageLoader);

        item_name.setText(m.getNAME());
        item_price.setText("\u20B9"+" "+m.getPRICE());
        item_qty.setText(String.valueOf(m.getQUANTITY()));
      item_realprice.setText("\u20B9"+" "+m.getREALPRICE());


        final View finalConvertView = convertView;
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cartitems.get(position).setQUANTITY(cartitems.get(position).getQUANTITY()+1);
                getView(position, finalConvertView,parent);


            }
        });


        //alert dialog to enter quantity
        item_qty.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

              //  cartitems.get(position).setQUANTITY(cartitems.get(position).getQUANTITY()+1);
               // getView(position, finalConvertView,parent);


                LayoutInflater li = LayoutInflater.from(v.getRootView().getContext());

                //Creating a view to get the dialog box
                final View confirmDialog = li.inflate(R.layout.quantity_dlg_layout, null);

                AlertDialog.Builder alert = new AlertDialog.Builder(v.getRootView().getContext());
                alert.setView(confirmDialog);
                alert.setCancelable(true);
                Button buttonSave = (Button) confirmDialog.findViewById(R.id.buttonSave);
                final Button buttonCancel = (Button) confirmDialog.findViewById(R.id.buttonCancel);

                final AlertDialog alertDialog = alert.create();
                alertDialog.show();

                buttonSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText new_QTY = (EditText) confirmDialog.findViewById(R.id.qtyET_DLG);

                        if (new_QTY.getText().toString().equals("")||Integer.parseInt(new_QTY.getText().toString())>100)
                        {

                            new_QTY.setError("Enter valid number");

                        }
                        else {
                            cartitems.get(position).setQUANTITY(Integer.parseInt(new_QTY.getText().toString()));
                            alertDialog.dismiss();
                        }
                    }
                });

                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EditText new_QTY = (EditText) confirmDialog.findViewById(R.id.qtyET_DLG);
                        alertDialog.dismiss();
                    }
                });





            }
        });


        return convertView;
    }

}