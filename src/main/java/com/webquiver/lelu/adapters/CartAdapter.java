package com.webquiver.lelu.adapters;
/**
 * Created by WebQuiver 04 on 7/21/2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;
import com.webquiver.lelu.CartActivity;
import com.webquiver.lelu.HomeActivity;
import com.webquiver.lelu.ItemActivity;
import com.webquiver.lelu.R;
import com.webquiver.lelu.classes.AppController;
import com.webquiver.lelu.classes.CartItem;
import com.webquiver.lelu.classes.Config;
import com.webquiver.lelu.classes.SessionManagement;
import com.webquiver.lelu.fragments.AddressFragment;
import com.webquiver.lelu.fragments.CartFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class CartAdapter extends BaseAdapter {

    AppCompatButton buttonSHOPNOW;

    private Activity activity;
    RequestQueue requestQueue;
    SharedPreferences pref;
    RequestQueue requestQueue_cart;
    TextView item_qty;
    private LayoutInflater inflater;
    private List<CartItem> cartitems;
    AlertDialog.Builder alert;
    AlertDialog alertDialog;
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


        requestQueue_cart=Volley.newRequestQueue(activity);



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

          //  Toast.makeText(activity, "CART IS EMPTY", Toast.LENGTH_LONG).show();


            SuperActivityToast.create(activity, new Style(), Style.TYPE_STANDARD)
                    //     .setButtonText("Please click BACK again to exit")
                    //     .setButtonIconResource(R.drawable.ic_undo)
                    //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                    //     .setProgressBarColor(Color.WHITE)
                    .setText("Yur cart is empty")
                    .setDuration(Style.DURATION_SHORT)
                    .setFrame(Style.FRAME_STANDARD)
                    .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                    .setAnimations(Style.ANIMATIONS_FADE).show();

        }

            // getting ccart data for the row
            CartItem m = cartitems.get(position);

            // thumbnail image
            thumbNail.setImageUrl(Config.BASE_URLwithoutslash+m.getIMAGE_URL(), imageLoader);
        
            item_name.setText(m.getNAME());
            item_price.setText("\u20B9" + " " + m.getPRICE());
            item_qty.setText(String.valueOf(m.getQUANTITY()));
            item_realprice.setText("\u20B9" + " " + m.getREALPRICE());


        final View finalConvertView = convertView;
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //   Toast.makeText(activity, cartitems.get(position).getPRODUCT_ID(), Toast.LENGTH_LONG).show();

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

                                        EventBus.getDefault().post(new HelloWorldEvent(String.valueOf(cartitems.get(position).getQUANTITY()+1)));

                                //        Toast.makeText(activity,"Quantity updated",Toast.LENGTH_SHORT).show();

                                        SuperActivityToast.create(activity, new Style(), Style.TYPE_STANDARD)
                                                //     .setButtonText("Please click BACK again to exit")
                                                //     .setButtonIconResource(R.drawable.ic_undo)
                                                //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                                                //     .setProgressBarColor(Color.WHITE)
                                                .setText("Quantity updated")
                                                .setDuration(Style.DURATION_SHORT)
                                                .setFrame(Style.FRAME_STANDARD)
                                                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREEN))
                                                .setAnimations(Style.ANIMATIONS_POP).show();




                                    }

                                    else {
                                       // Toast.makeText(activity, "Failed to update quantity", Toast.LENGTH_SHORT).show();

                                        SuperActivityToast.create(activity, new Style(), Style.TYPE_STANDARD)
                                                //     .setButtonText("Please click BACK again to exit")
                                                //     .setButtonIconResource(R.drawable.ic_undo)
                                                //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                                                //     .setProgressBarColor(Color.WHITE)
                                                .setText("Failed to update quantity\nPlease retry")
                                                .setDuration(Style.DURATION_LONG)
                                                .setFrame(Style.FRAME_STANDARD)
                                                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                                                .setAnimations(Style.ANIMATIONS_POP).show();


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
                        params.put(Config.KEY_CART_ProdQty, String.valueOf(1));
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
                                        EventBus.getDefault().post(new HelloWorldEvent(String.valueOf(cartitems.get(position).getQUANTITY()+1)));


//     Toast.makeText(activity,"Quantity updated",Toast.LENGTH_SHORT).show();

                                    }

                                    else {
                                       // Toast.makeText(activity, "Failed to update quantity", Toast.LENGTH_SHORT).show();

                                        SuperActivityToast.create(activity, new Style(), Style.TYPE_STANDARD)
                                                //     .setButtonText("Please click BACK again to exit")
                                                //     .setButtonIconResource(R.drawable.ic_undo)
                                                //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                                                //     .setProgressBarColor(Color.WHITE)
                                                .setText("Failed to update quantity\nPlease retry")
                                                .setDuration(Style.DURATION_LONG)
                                                .setFrame(Style.FRAME_STANDARD)
                                                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                                                .setAnimations(Style.ANIMATIONS_POP).show();


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
                        params.put(Config.KEY_CART_ProdQty, String.valueOf(-1));
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

                LayoutInflater li = LayoutInflater.from(activity);

                //Creating a view to get the dialog box
                View confirmDialog = li.inflate(R.layout.rmovefromcart, null);
                alert = new AlertDialog.Builder(activity);
                alert.setView(confirmDialog);
                alert.setCancelable(true);
                Button buttonSave = (Button) confirmDialog.findViewById(R.id.buttonSave);
                Button buttonNO = (Button) confirmDialog.findViewById(R.id.buttonCancel);
                alertDialog = alert.create();
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSHAKE;

                buttonSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Dialog loading = new Dialog(activity);
                        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        loading.setContentView(R.layout.custom_dialog_progress_loggingin);
                        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        loading.setCancelable(false);
                        TextView t=(TextView)loading.findViewById(R.id.txt);
                        t.setText("Removing item from your Cart");
                        loading.show();
                        //   final ProgressDialog loading = ProgressDialog.show(activity, "Removing item", "Please wait...", false, false);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CART_DEL_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        loading.dismiss();
                                        alertDialog.dismiss();

                                        try {

                                            JSONObject jsonResponse = new JSONObject(response);
                                            if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                                //change
                                             //   Toast.makeText(activity,"Item removed",Toast.LENGTH_SHORT).show();

                                                EventBus.getDefault().post(new HelloWorldEvent(""));



                                                SuperActivityToast.create(activity, new Style(), Style.TYPE_STANDARD)
                                                        //     .setButtonText("Please click BACK again to exit")
                                                        //     .setButtonIconResource(R.drawable.ic_undo)
                                                        //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                                                        //     .setProgressBarColor(Color.WHITE)
                                                        .setText("Item removed")
                                                        .setDuration(Style.DURATION_LONG)
                                                        .setFrame(Style.FRAME_STANDARD)
                                                        .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREEN))
                                                        .setAnimations(Style.ANIMATIONS_POP).show();



                                                //cartitems.get(position).setQUANTITY(cartitems.get(position).getQUANTITY()-1);

                                                //   cartitems.remove(position-1);             //


                                                cartitems.remove(position);
                                                notifyDataSetChanged();


                                                //     getView(position, finalConvertView,parent); //

                                                if (cartitems.size()==0)
                                                {

                                                    EventBus.getDefault().post(new HelloWorldEvent(""));

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
                                                     //       Toast.makeText(activity, "ctch", Toast.LENGTH_SHORT).show();

                                                            try {
                                                                HomeActivity.fa.finish();
                                                            }
                                                            catch (Exception e)
                                                            {
                                                         //       Toast.makeText(activity, "ctch", Toast.LENGTH_SHORT).show();
                                                            }
                                                           Intent intent=new Intent(activity,HomeActivity.class);
                                                           activity.startActivity(intent);
                                                            activity.finish();


                                                        }

                                                    });




                                                }



                                                // Toast.makeText(activity,"Quantity updated",Toast.LENGTH_SHORT).show();

                                            }

                                            else {

                                             //   Toast.makeText(activity, "Failed to update quantity", Toast.LENGTH_SHORT).show();

                                                SuperActivityToast.create(activity, new Style(), Style.TYPE_STANDARD)
                                                        //     .setButtonText("Please click BACK again to exit")
                                                        //     .setButtonIconResource(R.drawable.ic_undo)
                                                        //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                                                        //     .setProgressBarColor(Color.WHITE)
                                                        .setText("Failed\nPlease retry")
                                                        .setDuration(Style.DURATION_LONG)
                                                        .setFrame(Style.FRAME_STANDARD)
                                                        .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                                                        .setAnimations(Style.ANIMATIONS_POP).show();




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
                                        alertDialog.dismiss();
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
                            //    params.put(Config.KEY_CART_ProdQty, String.valueOf(0));
                                return params;
                            }
                        };

                        //Adding request the the queue
                        requestQueue.add(stringRequest);




                    }
                });




                buttonNO.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        alertDialog.dismiss();


                    }
                });





                alertDialog.show();



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

                    //    Toast.makeText(activity, cartitems.get(position).getPRODUCT_ID(), Toast.LENGTH_LONG).show();

                        if (new_QTY.getText().toString().equals("")||Integer.parseInt(new_QTY.getText().toString())>100)
                        {

                            new_QTY.setError("Enter valid number");

                        }
                        else {



                            final Dialog loading = new Dialog(activity);
                            loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            loading.setContentView(R.layout.custom_dialog_progress_loggingin);
                            loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            loading.setCancelable(false);
                            TextView t=(TextView)loading.findViewById(R.id.txt);
                            t.setText("Adding to cart");
                            loading.show();



                            //  final ProgressDialog loading = ProgressDialog.show(activity, "Adding to cart", "Please wait...", false, false);
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CART_UPDATE_URL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            loading.dismiss();

                                            try {

                                                JSONObject jsonResponse = new JSONObject(response);
                                                if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                                    cartitems.get(position).setQUANTITY(Integer.parseInt(new_QTY.getText().toString()));
                                                    getView(position, finalConvertView,parent);
                                                    EventBus.getDefault().post(new HelloWorldEvent(String.valueOf(cartitems.get(position).getQUANTITY()+1)));
                                                    alertDialog.dismiss();
                                                //    Toast.makeText(activity,"Quantity updated",Toast.LENGTH_SHORT).show();

                                                    SuperActivityToast.create(activity, new Style(), Style.TYPE_STANDARD)
                                                            //     .setButtonText("Please click BACK again to exit")
                                                            //     .setButtonIconResource(R.drawable.ic_undo)
                                                            //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                                                            //     .setProgressBarColor(Color.WHITE)
                                                            .setText("Quantity updated")
                                                            .setDuration(Style.DURATION_LONG)
                                                            .setFrame(Style.FRAME_STANDARD)
                                                            .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREEN))
                                                            .setAnimations(Style.ANIMATIONS_POP).show();


                                                }

                                                else {
                                                    alertDialog.dismiss();
                                                  //  Toast.makeText(activity, "Failed to add", Toast.LENGTH_SHORT).show();

                                                    SuperActivityToast.create(activity, new Style(), Style.TYPE_STANDARD)
                                                            //     .setButtonText("Please click BACK again to exit")
                                                            //     .setButtonIconResource(R.drawable.ic_undo)
                                                            //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                                                            //     .setProgressBarColor(Color.WHITE)
                                                            .setText("Failed\nPlease retry")
                                                            .setDuration(Style.DURATION_LONG)
                                                            .setFrame(Style.FRAME_STANDARD)
                                                            .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                                                            .setAnimations(Style.ANIMATIONS_POP).show();

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


    public class HelloWorldEvent {             //event to update total,bigtotal in the cart fragment
        public final String message;

        public HelloWorldEvent(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }


    //eventbus
    public class CARTNUMEvent {
        private final String message;

        public CARTNUMEvent(String message) {
            this.message = message;

        }

        public String getMessage() {
            return message;
        }
    }


/*
    public void getall() {

        // requestQueue_cart = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CART_GET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(activity, "response", Toast.LENGTH_LONG).show();

                        try {

                            JSONObject jsonResponse = new JSONObject(response);

                            //   JSONArray jsonArray=new JSONArray(response);

                            if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                showNUM(response);

                            } else if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("failed")) {

                                Toast.makeText(activity, "Failed_num", Toast.LENGTH_LONG).show();

                            } else {

                                Toast.makeText(activity, "Invalid user", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //
                        //       Toast.makeText(getApplicationContext(), "error1", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put(Config.KEY_ADDR_MOBILE,  pref.getString(SessionManagement.KEY_PHONE,""));
                return params;
            }
        };

        //Adding request the the queue
        requestQueue_cart.add(stringRequest);

    }


    public void showNUM(String jsonArray) throws JSONException {

        JSONObject json = new JSONObject(jsonArray);
        JSONArray arr = json.getJSONArray("products");
        editor_num_pref.putString(Config.KEY_NUM_CART,String.valueOf(arr.length()) );
        editor_num_pref.commit();
        String nn=pref_numberss.getString(Config.KEY_NUM_CART,"0");


    }

*/











}