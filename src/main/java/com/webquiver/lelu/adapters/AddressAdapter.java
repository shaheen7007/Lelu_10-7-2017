package com.webquiver.lelu.adapters;
/**
 * Created by WebQuiver 04 on 7/21/2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.webquiver.lelu.HomeActivity;
import com.webquiver.lelu.LoginActivity;
import com.webquiver.lelu.R;
import com.webquiver.lelu.classes.AddressItem;
import com.webquiver.lelu.classes.AppController;
import com.webquiver.lelu.classes.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<AddressItem> addrtems;
     private RequestQueue requestQueue;

    public AddressAdapter(Activity activity, List<AddressItem> addrtems) {
        this.activity = activity;
        this.addrtems = addrtems;


    }

    @Override
    public int getCount() {
        return addrtems.size();
    }

    @Override
    public Object getItem(int location) {
        return addrtems.get(location);
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
            convertView = inflater.inflate(R.layout.address_layout, null);

        TextView addr_name = (TextView) convertView.findViewById(R.id.ADDR_name_id);
         TextView addr_address1 = (TextView) convertView.findViewById(R.id.ADDR_address1_id);
         TextView addr_place = (TextView) convertView.findViewById(R.id.ADDR_place_id);
         TextView addr_district = (TextView) convertView.findViewById(R.id.ADDR_district_id);
         TextView addr_state = (TextView) convertView.findViewById(R.id.ADDR_state_id);
         TextView addr_pincode = (TextView) convertView.findViewById(R.id.ADDR_pin_id);
         TextView addr_phone = (TextView) convertView.findViewById(R.id.ADDR_phone_id);

         TextView addr_editBTN = (TextView) convertView.findViewById(R.id.ADDR_editBTN_id);
         TextView addr_selectBTN = (TextView) convertView.findViewById(R.id.ADDR_editBTN_id); // change


        // getting address data for the row
        AddressItem m = addrtems.get(position);


        addr_name.setText(m.getNAME());
        addr_address1.setText(m.getADDRESS1());
        addr_place.setText(m.getPLACE());
        addr_district.setText(m.getDISTRICT());
        addr_state.setText(m.getSTATE());
        addr_pincode.setText(m.getPINCODE());
        addr_phone.setText(m.getPHONE());



        final View finalConvertView = convertView;

        addr_selectBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




            }
        });


        //alert dialog to enter quantity
        addr_editBTN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

              //  addrtems.get(position).setQUANTITY(addrtems.get(position).getQUANTITY()+1);
               // getView(position, finalConvertView,parent);


                LayoutInflater li = LayoutInflater.from(v.getRootView().getContext());

                //Creating a view to get the dialog box
                final View confirmDialog = li.inflate(R.layout.address_edit_dlg_layout, null);

                AlertDialog.Builder alert = new AlertDialog.Builder(v.getRootView().getContext());
                alert.setView(confirmDialog);
                alert.setCancelable(true);
                Button buttonSave = (Button) confirmDialog.findViewById(R.id.buttonSave);
                final Button buttonCancel = (Button) confirmDialog.findViewById(R.id.buttonCancel);

                final EditText name=(EditText)confirmDialog.findViewById(R.id.ADR_nameET);
                final EditText address1=(EditText)confirmDialog.findViewById(R.id.ADR_Address1ET);
                final EditText place=(EditText)confirmDialog.findViewById(R.id.ADR_PlaceET);
                final EditText district=(EditText)confirmDialog.findViewById(R.id.ADR_DistrictET);
                final EditText pincode=(EditText)confirmDialog.findViewById(R.id.ADR_PincodeET);
                final EditText state=(EditText)confirmDialog.findViewById(R.id.ADR_StateET);
                final EditText phone=(EditText)confirmDialog.findViewById(R.id.ADR_PhoneET);

                name.setText(addrtems.get(position).getNAME());
                address1.setText(addrtems.get(position).getADDRESS1());
                place.setText(addrtems.get(position).getPLACE());
                district.setText(addrtems.get(position).getDISTRICT());
                pincode.setText(addrtems.get(position).getPINCODE());
                phone.setText(addrtems.get(position).getPHONE());
                state.setText(addrtems.get(position).getSTATE());

                final AlertDialog alertDialog = alert.create();
                alertDialog.show();

                buttonSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        addrtems.get(position).setNAME(name.getText().toString());
                        addrtems.get(position).setADDRESS1(address1.getText().toString());
                        addrtems.get(position).setPLACE(place.getText().toString());
                        addrtems.get(position).setDISTRICT(district.getText().toString());
                        addrtems.get(position).setPINCODE("PIN - "+pincode.getText().toString());
                        addrtems.get(position).setPHONE("Mobile: "+phone.getText().toString());
                        addrtems.get(position).setSTATE(state.getText().toString());

                        getView(position, finalConvertView,parent);

                        alertDialog.dismiss();

                    //
                        //   EditText new_QTY = (EditText) confirmDialog.findViewById(R.id.qtyET_DLG);

                      //  if (new_QTY.getText().toString().equals("")||Integer.parseInt(new_QTY.getText().toString())>100)
                      //  {

                     //       new_QTY.setError("Enter valid number");

                     //   }
                      //  else {
                            //addrtems.get(position).setQUANTITY(Integer.parseInt(new_QTY.getText().toString()));
                       //     alertDialog.dismiss();
                     //
                    }
                });

                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                     //   EditText new_QTY = (EditText) confirmDialog.findViewById(R.id.qtyET_DLG);
                        alertDialog.dismiss();
                    }
                });

            }
        });





        TextView addr_newBTN=(TextView)convertView.findViewById(R.id.ADDR_addnewBTN_id);
        addr_newBTN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //  addrtems.get(position).setQUANTITY(addrtems.get(position).getQUANTITY()+1);
                // getView(position, finalConvertView,parent);
                LayoutInflater li = LayoutInflater.from(v.getRootView().getContext());
                //Creating a view to get the dialog box
                final View confirmDialog = li.inflate(R.layout.address_edit_dlg_layout, null);

                AlertDialog.Builder alert = new AlertDialog.Builder(v.getRootView().getContext());
                alert.setView(confirmDialog);
                alert.setCancelable(true);
                Button buttonSave = (Button) confirmDialog.findViewById(R.id.buttonSave);
                final Button buttonCancel = (Button) confirmDialog.findViewById(R.id.buttonCancel);

                final EditText name=(EditText)confirmDialog.findViewById(R.id.ADR_nameET);
                final EditText address1=(EditText)confirmDialog.findViewById(R.id.ADR_Address1ET);
                final EditText place=(EditText)confirmDialog.findViewById(R.id.ADR_PlaceET);
                final EditText district=(EditText)confirmDialog.findViewById(R.id.ADR_DistrictET);
                final EditText pincode=(EditText)confirmDialog.findViewById(R.id.ADR_PincodeET);
                final EditText state=(EditText)confirmDialog.findViewById(R.id.ADR_StateET);
                final EditText phone=(EditText)confirmDialog.findViewById(R.id.ADR_PhoneET);

                final AlertDialog alertDialog = alert.create();
                alertDialog.show();

                buttonSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {


                        requestQueue = Volley.newRequestQueue((v.getRootView().getContext()));




                        final ProgressDialog loading = ProgressDialog.show((v.getRootView().getContext()), "Loading", "Please wait...", false, false);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADDR_SEND_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        loading.dismiss();

                                        try {

                                            JSONObject jsonResponse = new JSONObject(response);
                                            if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                                Toast.makeText((v.getRootView().getContext()),"Address added",Toast.LENGTH_LONG).show();

                                            }
                                            else if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("failed"))
                                            {

                                                Toast.makeText((v.getRootView().getContext()),"Failed",Toast.LENGTH_LONG).show();


                                            }


                                            else {

                                                Toast.makeText((v.getRootView().getContext()), "Invalid user", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        loading.dismiss();
                                        //
                                        Toast.makeText((v.getRootView().getContext()), "error1", Toast.LENGTH_LONG).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                //Adding the parameters to the request
                                params.put(Config.KEY_ADDR_NAME, name.getText().toString());
                                params.put(Config.KEY_ADDR_MOBILE, "9020707009");             //change
                                params.put(Config.KEY_ADDR_HOUSE, address1.getText().toString());
                                params.put(Config.KEY_ADDR_STREET, place.getText().toString());
                                params.put(Config.KEY_ADDR_PHONE, phone.getText().toString());
                                params.put(Config.KEY_ADDR_DISTRICT, district.getText().toString());
                                params.put(Config.KEY_ADDR_PIN, pincode.getText().toString());
                                params.put(Config.KEY_ADDR_STATE, state.getText().toString());
                                return params;
                            }
                        };

                        //Adding request the the queue
                        requestQueue.add(stringRequest);


                     /*

                        AddressItem C_item3 = new AddressItem();
                        C_item3.setNAME(name.getText().toString());
                        C_item3.setADDRESS1(address1.getText().toString());
                        C_item3.setPLACE(place.getText().toString());
                        C_item3.setDISTRICT(district.getText().toString());
                        C_item3.setSTATE(state.getText().toString());
                        C_item3.setPINCODE("PIN - "+pincode.getText().toString());
                        C_item3.setPHONE("Mobile: "+phone.getText().toString());
                        addrtems.add(0,C_item3);
                       // addrtems.smoothScrollToPosition(0);
                       // adapter.notifyDataSetChanged();
                        getView(position, finalConvertView,parent);

                        alertDialog.dismiss();

                        //
                        //   EditText new_QTY = (EditText) confirmDialog.findViewById(R.id.qtyET_DLG);

                        //  if (new_QTY.getText().toString().equals("")||Integer.parseInt(new_QTY.getText().toString())>100)
                        //  {

                        //       new_QTY.setError("Enter valid number");

                        //   }
                        //  else {
                        //addrtems.get(position).setQUANTITY(Integer.parseInt(new_QTY.getText().toString()));
                        //     alertDialog.dismiss();
                        //

                        */
                    }
                });


                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //   EditText new_QTY = (EditText) confirmDialog.findViewById(R.id.qtyET_DLG);
                        alertDialog.dismiss();
                    }
                });



            }
        });














        return convertView;
    }

}