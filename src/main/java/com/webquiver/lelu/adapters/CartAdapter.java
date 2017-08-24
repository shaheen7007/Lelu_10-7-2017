package com.webquiver.lelu.adapters;
/**
 * Created by WebQuiver 04 on 7/21/2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.webquiver.lelu.CartActivity;
import com.webquiver.lelu.HomeActivity;
import com.webquiver.lelu.ItemActivity;
import com.webquiver.lelu.R;
import com.webquiver.lelu.classes.AppController;
import com.webquiver.lelu.classes.CartItem;
import com.webquiver.lelu.classes.Config;
import com.webquiver.lelu.classes.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartAdapter extends BaseAdapter {

    AppCompatButton buttonSHOPNOW;

    private Activity activity;
    RequestQueue requestQueue;
    SharedPreferences pref;
    TextView item_qty;
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

        requestQueue= Volley.newRequestQueue(activity);            //
        pref = activity.getSharedPreferences(SessionManagement.PREF_NAME,Context.MODE_PRIVATE);


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
        item_qty = (TextView) convertView.findViewById(R.id.cartItemQtytxt_id);

        TextView item_realprice = (TextView) convertView.findViewById(R.id.cartItemRealprice_id);
        TextView item_price = (TextView) convertView.findViewById(R.id.cartItemPriceTxt_id);
        TextView plus = (TextView) convertView.findViewById(R.id.cartplustx_id);
        TextView minus = (TextView) convertView.findViewById(R.id.cartminus_id);
        TextView remove = (TextView) convertView.findViewById(R.id.removeBTN_id);

        //strike price
        item_realprice.setPaintFlags(item_realprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (getCount()==0) {

            Toast.makeText(activity, "CART IS EMPTY", Toast.LENGTH_LONG).show();

        }


            // getting ccart data for the row
            CartItem m = cartitems.get(position);

            // thumbnail image
            thumbNail.setImageUrl(m.getIMAGE_URL(), imageLoader);

            item_name.setText(m.getNAME());
            item_price.setText("\u20B9" + " " + m.getPRICE());
            item_qty.setText(String.valueOf(m.getQUANTITY()));
            item_realprice.setText("\u20B9" + " " + m.getREALPRICE());


        final View finalConvertView = convertView;
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(activity, cartitems.get(position).getPRODUCT_ID(), Toast.LENGTH_LONG).show();

                final Dialog loading = new Dialog(activity);
                loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                loading.setContentView(R.layout.custom_dialog_progress_loggingin);
                loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                loading.setCancelable(false);
                TextView t=(TextView)loading.findViewById(R.id.txt);
                t.setText("Updating Quantity");
                loading.show();
            //    final ProgressDialog loading = ProgressDialog.show(activity, "Adding to cart", "Please wait...", false, false);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CART_ADD_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                loading.dismiss();

                                try {

                                    JSONObject jsonResponse = new JSONObject(response);
                                    if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                        cartitems.get(position).setQUANTITY(cartitems.get(position).getQUANTITY()+1);
                                        getView(position, finalConvertView,parent);
                                //        Toast.makeText(activity,"Quantity updated",Toast.LENGTH_SHORT).show();

                                    }

                                    else {
                                        Toast.makeText(activity, "Failed to update quantity", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    loading.dismiss();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                loading.dismiss();
                                //
                                Toast.makeText(activity, "error1", Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        //Adding the parameters to the request
                        params.put(Config.KEY_PHONE,pref.getString(SessionManagement.KEY_PHONE,""));
                        params.put(Config.KEY_CART_ProdId,cartitems.get(position).getPRODUCT_ID());
                        params.put(Config.KEY_CART_ProdQty, String.valueOf(cartitems.get(position).getQUANTITY()+1));
                        return params;
                    }
                };

                //Adding request the the queue
                requestQueue.add(stringRequest);


            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog loading = new Dialog(activity);
                loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                loading.setContentView(R.layout.custom_dialog_progress_loggingin);
                loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                loading.setCancelable(false);
                TextView t=(TextView)loading.findViewById(R.id.txt);
                t.setText("Updating Quantity");
                loading.show();
               // final ProgressDialog loading = ProgressDialog.show(activity, "Adding to cart", "Please wait...", false, false);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CART_ADD_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                loading.dismiss();

                                try {

                                    JSONObject jsonResponse = new JSONObject(response);
                                    if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                        cartitems.get(position).setQUANTITY(cartitems.get(position).getQUANTITY()-1);
                                        getView(position, finalConvertView,parent);
                                        //        Toast.makeText(activity,"Quantity updated",Toast.LENGTH_SHORT).show();

                                    }

                                    else {
                                        Toast.makeText(activity, "Failed to update quantity", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    loading.dismiss();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                loading.dismiss();
                                //
                                Toast.makeText(activity, "error1", Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        //Adding the parameters to the request
                        params.put(Config.KEY_PHONE,pref.getString(SessionManagement.KEY_PHONE,""));
                        params.put(Config.KEY_CART_ProdId,cartitems.get(position).getPRODUCT_ID());
                        params.put(Config.KEY_CART_ProdQty, String.valueOf(cartitems.get(position).getQUANTITY()-1));
                        return params;
                    }
                };

                //Adding request the the queue
                requestQueue.add(stringRequest);


            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                alertDialog.setTitle("Remove item ?");

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Add your code for the button here.

                        final Dialog loading = new Dialog(activity);
                        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        loading.setContentView(R.layout.custom_dialog_progress_loggingin);
                        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        loading.setCancelable(false);
                        TextView t=(TextView)loading.findViewById(R.id.txt);
                        t.setText("Removing item from your Cart");
                        loading.show();
                     //   final ProgressDialog loading = ProgressDialog.show(activity, "Removing item", "Please wait...", false, false);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CART_ADD_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                loading.dismiss();

                                try {

                                    JSONObject jsonResponse = new JSONObject(response);
                                    if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                        //change
                                        Toast.makeText(activity,"Item removed",Toast.LENGTH_SHORT).show();


                                        //cartitems.get(position).setQUANTITY(cartitems.get(position).getQUANTITY()-1);

                                     //   cartitems.remove(position-1);             //


                                        cartitems.remove(position);
                                        notifyDataSetChanged();
                                  //     getView(position, finalConvertView,parent); //

                                        if (cartitems.size()==0)
                                        {

                                            LayoutInflater li = LayoutInflater.from(activity);
                                            //Creating a view to get the dialog box
                                            View confirmDialog = li.inflate(R.layout.cartempty_layout, null);

                                            //Initizliaing confirm button fo dialog box and edittext of dialog box
                                            buttonSHOPNOW = (AppCompatButton) confirmDialog.findViewById(R.id.buttonShop);

                                            //Creating an alertdialog builder
                                            AlertDialog.Builder alert = new AlertDialog.Builder(activity);

                                            //Adding our dialog box to the view of alert dialog
                                            alert.setView(confirmDialog);
                                            alert.setCancelable(false);

                                            //Creating an alert dialog
                                            final AlertDialog alertDialog = alert.create();

                                            //Displaying the alert dialog
                                            alertDialog.show();

                                            //On the click of the confirm button from alert dialog
                                            buttonSHOPNOW.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //Hiding the alert dialog
                                                    alertDialog.dismiss();

                                                    Intent intent=new Intent(activity,HomeActivity.class);
                                                    activity.startActivity(intent);
                                                    activity.finish();


                                                }

                                            });




                                        }



                                        // Toast.makeText(activity,"Quantity updated",Toast.LENGTH_SHORT).show();

                                    }

                                    else {

                                        Toast.makeText(activity, "Failed to update quantity", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    loading.dismiss();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                loading.dismiss();
                                //
                                Toast.makeText(activity, "error1", Toast.LENGTH_LONG).show();
                            }
                        }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        //Adding the parameters to the request
                        params.put(Config.KEY_PHONE,pref.getString(SessionManagement.KEY_PHONE,""));
                        params.put(Config.KEY_CART_ProdId,cartitems.get(position).getPRODUCT_ID());
                        params.put(Config.KEY_CART_ProdQty, String.valueOf(0));
                        return params;
                    }
                };

                //Adding request the the queue
                requestQueue.add(stringRequest);


            }
        });

                alertDialog.setNegativeButton("No",new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        dialog.dismiss();

                    }
                });

                alertDialog.show();

                 */



                FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(activity)
                     //   .setImageRecourse(R.drawable.banner2)
                        .setTextSubTitle("Remove item")
                        .setBody("Are you sure you want to remove \n this item ?")
                        .setNegativeColor(R.color.colorgreen)
                        .setNegativeButtonText("Cancel")
                        .setButtonsGravity(FancyAlertDialog.PanelGravity.CENTER)
                        .setOnNegativeClicked(new FancyAlertDialog.OnNegativeClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButtonText("Remove")
                        .setPositiveColor(R.color.colorred)
                        .setOnPositiveClicked(new FancyAlertDialog.OnPositiveClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog) {

                                dialog.dismiss();

                                final Dialog loading = new Dialog(activity);
                                loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                loading.setContentView(R.layout.custom_dialog_progress_loggingin);
                                loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                loading.setCancelable(false);
                                TextView t=(TextView)loading.findViewById(R.id.txt);
                                t.setText("Removing item from your Cart");
                                loading.show();
                                //   final ProgressDialog loading = ProgressDialog.show(activity, "Removing item", "Please wait...", false, false);
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CART_ADD_URL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                loading.dismiss();

                                                try {

                                                    JSONObject jsonResponse = new JSONObject(response);
                                                    if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                                        //change
                                                        Toast.makeText(activity,"Item removed",Toast.LENGTH_SHORT).show();


                                                        //cartitems.get(position).setQUANTITY(cartitems.get(position).getQUANTITY()-1);

                                                        //   cartitems.remove(position-1);             //


                                                        cartitems.remove(position);
                                                        notifyDataSetChanged();
                                                        //     getView(position, finalConvertView,parent); //

                                                        if (cartitems.size()==0)
                                                        {

                                                            LayoutInflater li = LayoutInflater.from(activity);
                                                            //Creating a view to get the dialog box
                                                            View confirmDialog = li.inflate(R.layout.cartempty_layout, null);

                                                            //Initizliaing confirm button fo dialog box and edittext of dialog box
                                                            buttonSHOPNOW = (AppCompatButton) confirmDialog.findViewById(R.id.buttonShop);

                                                            //Creating an alertdialog builder
                                                            AlertDialog.Builder alert = new AlertDialog.Builder(activity);

                                                            //Adding our dialog box to the view of alert dialog
                                                            alert.setView(confirmDialog);
                                                            alert.setCancelable(false);

                                                            //Creating an alert dialog
                                                            final AlertDialog alertDialog = alert.create();

                                                            //Displaying the alert dialog
                                                            alertDialog.show();

                                                            //On the click of the confirm button from alert dialog
                                                            buttonSHOPNOW.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    //Hiding the alert dialog
                                                                    alertDialog.dismiss();

                                                                    Intent intent=new Intent(activity,HomeActivity.class);
                                                                    activity.startActivity(intent);
                                                                    activity.finish();


                                                                }

                                                            });




                                                        }



                                                        // Toast.makeText(activity,"Quantity updated",Toast.LENGTH_SHORT).show();

                                                    }

                                                    else {

                                                        Toast.makeText(activity, "Failed to update quantity", Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    loading.dismiss();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                loading.dismiss();
                                                //
                                                Toast.makeText(activity, "error1", Toast.LENGTH_LONG).show();
                                            }
                                        }) {

                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<>();
                                        //Adding the parameters to the request
                                        params.put(Config.KEY_PHONE,pref.getString(SessionManagement.KEY_PHONE,""));
                                        params.put(Config.KEY_CART_ProdId,cartitems.get(position).getPRODUCT_ID());
                                        params.put(Config.KEY_CART_ProdQty, String.valueOf(0));
                                        return params;
                                    }
                                };

                                //Adding request the the queue
                                requestQueue.add(stringRequest);


                            }
                        })
                       /* .setAutoHide(true)*/
                        .build();
                alert.show();












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
                        final EditText new_QTY = (EditText) confirmDialog.findViewById(R.id.qtyET_DLG);

                        Toast.makeText(activity, cartitems.get(position).getPRODUCT_ID(), Toast.LENGTH_LONG).show();

                        if (new_QTY.getText().toString().equals("")||Integer.parseInt(new_QTY.getText().toString())>100)
                        {

                            new_QTY.setError("Enter valid number");

                        }
                        else {

                            final ProgressDialog loading = ProgressDialog.show(activity, "Adding to cart", "Please wait...", false, false);
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CART_ADD_URL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            loading.dismiss();

                                            try {

                                                JSONObject jsonResponse = new JSONObject(response);
                                                if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                                    cartitems.get(position).setQUANTITY(Integer.parseInt(new_QTY.getText().toString()));
                                                    getView(position, finalConvertView,parent);
                                                    alertDialog.dismiss();
                                                    Toast.makeText(activity,"Quantity updated",Toast.LENGTH_SHORT).show();

                                                }

                                                else {
                                                    alertDialog.dismiss();
                                                    Toast.makeText(activity, "Failed to add", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                alertDialog.dismiss();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            loading.dismiss();
                                            //
                                            Toast.makeText(activity, "error1", Toast.LENGTH_LONG).show();
                                        }
                                    }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    //Adding the parameters to the request
                                    params.put(Config.KEY_PHONE,pref.getString(SessionManagement.KEY_PHONE,""));
                                    params.put(Config.KEY_CART_ProdId,cartitems.get(position).getPRODUCT_ID());
                                    params.put(Config.KEY_CART_ProdQty,new_QTY.getText().toString());
                                    return params;
                                }
                            };

                            //Adding request the the queue
                            requestQueue.add(stringRequest);

                        }
                    }
                });

                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        alertDialog.dismiss();
                    }
                });





            }
        });



        /*
        item_qty.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
*/

        return convertView;
    }

}