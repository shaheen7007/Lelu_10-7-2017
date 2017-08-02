package com.webquiver.lelu.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.webquiver.lelu.R;
import com.webquiver.lelu.classes.AddressItem;
import com.webquiver.lelu.classes.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WebQuiver 04 on 7/27/2017.
 */

public class ShowAllSavedADDR extends android.app.Fragment {

    private static ShowAllSavedADDR showAllSavedADDR = null;

    private RequestQueue requestQueue;
    private RequestQueue requestQueue2;
    private RequestQueue requestQueue_editADDR;


    //private ProgressDialog pDialog;
    private List<AddressItem> movieList = new ArrayList<AddressItem>();
    private ListView listView;
    private AddressAdapter adapter;

    String selectedID;

    TextView showALL_TXT_id;

    int i;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.address_frag_all, container, false);

        listView = (ListView) rootView.findViewById(R.id.addressListall_id);
        showALL_TXT_id=(TextView)rootView.findViewById(R.id.numaddrTXT_id);


        getall();

/*
        AddressItem C_item = new AddressItem();
        C_item.setNAME("shaheen Shabib");
        C_item.setADDRESS1("dasdf sfsd dfds");
        C_item.setPLACE("Othayi");
        C_item.setDISTRICT("Malappuram");
        C_item.setSTATE("Kerala");
        C_item.setPINCODE("676541");
        C_item.setPHONE("9020707009");
        C_item.setID(1);

        movieList.add(C_item);

        AddressItem C_item2 = new AddressItem();
        C_item2.setNAME("shaheen Shabib");
        C_item2.setADDRESS1("Webquiver");
        C_item2.setPLACE("Eranjipalam");
        C_item2.setDISTRICT("Calicut");
        C_item2.setSTATE("Kerala");
        C_item2.setPINCODE("6768574");
        C_item2.setPHONE("9020707009");
        C_item.setID(2);
        movieList.add(C_item2);

        AddressItem C_item3 = new AddressItem();
        C_item3.setNAME("shaheen BSV");
        C_item3.setADDRESS1("dasdf sfsd dfds");
        C_item3.setPLACE("Othayi");
        C_item3.setDISTRICT("Malappuram");
        C_item3.setSTATE("Kerala");
        C_item3.setPINCODE("676541");
        C_item3.setPHONE("8606198378");
        C_item3.setID(3);

        movieList.add(C_item3);

        */

        // Parsing json
                       /*
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    CartItem movie1 = new CartItem();
                                    movie.setNAME(obj.getString("color"));
                                    movie.setIMAGE_URL(obj.getString("image"));
//                                    movie.setQUANTITY(((Integer) obj.get("name")));

                                    Toast.makeText(CartActivity.this,obj.getString("name"),Toast.LENGTH_LONG).show();

                                    // Genre is json array
                                    movieList.add(movie);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            */



        return rootView;


    }

    public static ShowAllSavedADDR getInstance() {
        if (showAllSavedADDR == null) {
            showAllSavedADDR = new ShowAllSavedADDR();
        }
        return showAllSavedADDR;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        showAllSavedADDR = null;
    }


    public void getall() {


        requestQueue = Volley.newRequestQueue(getActivity());

        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADDR_GET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(getActivity(), "response", Toast.LENGTH_LONG).show();

                        try {


                            JSONObject jsonResponse = new JSONObject(response);

                            JSONArray jsonMainArr = jsonResponse.getJSONArray("addr");


                            //   JSONArray jsonArray=new JSONArray(response);

                            if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {


                                showADDR(response);


                            } else if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("failed")) {

                                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();


                            } else {

                                Toast.makeText(getActivity(), "Invalid user", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getActivity(), "error1", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put(Config.KEY_ADDR_MOBILE, "9020707009");             //change
                return params;
            }
        };

        //Adding request the the queue
        requestQueue.add(stringRequest);


    }


    public void getall2() {

        //fuction when new address is saved

        requestQueue2 = Volley.newRequestQueue(getActivity());

        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADDR_GET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(getActivity(), "response", Toast.LENGTH_LONG).show();

                        try {


                            JSONObject jsonResponse = new JSONObject(response);

                            JSONArray jsonMainArr = jsonResponse.getJSONArray("addr");


                            //   JSONArray jsonArray=new JSONArray(response);

                            if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {


                                showADDR2(response);


                            } else if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("failed")) {

                                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();


                            } else {

                                Toast.makeText(getActivity(), "Invalid user", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getActivity(), "error1", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put(Config.KEY_ADDR_MOBILE, "9020707009");             //change
                return params;
            }
        };

        //Adding request the the queue
        requestQueue2.add(stringRequest);


    }


    public void showADDR(String jsonArray) throws JSONException {

        JSONObject json = new JSONObject(jsonArray);
        JSONArray arr = json.getJSONArray("addr");

        showALL_TXT_id.setText("You have "+arr.length()+" addresses saved");


        for (i = 0; i < arr.length(); i++) {

            try {

                JSONObject tt = arr.getJSONObject(i);

                AddressItem C_item5 = new AddressItem();
                C_item5.setNAME(tt.getString("ca_name"));
                C_item5.setADDRESS1(tt.getString("ca_house"));
                C_item5.setPINCODE(tt.getString("ca_pin"));
                C_item5.setPHONE(tt.getString("ca_phone"));
                C_item5.setDISTRICT(tt.getString("ca_dist"));
                C_item5.setSTATE(tt.getString("ca_state"));
                C_item5.setPLACE(tt.getString("ca_street"));
                C_item5.setID(Integer.parseInt(tt.getString("ca_id")));

                movieList.add(C_item5);

            } catch (JSONException e) {

                e.printStackTrace();
                Toast.makeText(getActivity(), "json catch", Toast.LENGTH_LONG).show();

            }
        }

        adapter = new AddressAdapter(getActivity(), movieList);         //.subList(i-1, i)
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    public void showADDR2(String jsonArray) throws JSONException {

        //fuction when new address is saved


        JSONObject json = new JSONObject(jsonArray);
        JSONArray arr = json.getJSONArray("addr");
        List<AddressItem> movieList2 = new ArrayList<AddressItem>();

        for (i = 0; i < arr.length(); i++) {

            try {

                JSONObject tt = arr.getJSONObject(i);

                AddressItem C_item5 = new AddressItem();
                C_item5.setNAME(tt.getString("ca_name"));
                C_item5.setADDRESS1(tt.getString("ca_house"));
                C_item5.setPINCODE(tt.getString("ca_pin"));
                C_item5.setPHONE(tt.getString("ca_phone"));
                C_item5.setDISTRICT(tt.getString("ca_dist"));
                C_item5.setSTATE(tt.getString("ca_state"));
                C_item5.setPLACE(tt.getString("ca_street"));
                C_item5.setID(Integer.parseInt(tt.getString("ca_id")));

                movieList2.add(C_item5);


            } catch (JSONException e) {

                e.printStackTrace();
                Toast.makeText(getActivity(), "json catch", Toast.LENGTH_LONG).show();

            }
        }

        adapter = new AddressAdapter(getActivity(), movieList2);        //.subList(i-1, i)
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }







    //Adapter is done as inner class to call function inside fragment

    public class AddressAdapter extends BaseAdapter {
        private Activity activity;
        private LayoutInflater inflater;
        private List<AddressItem> addrtems;
        private RequestQueue requestQueue;
        private RequestQueue requestQueue2;

        int i;

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
                convertView = inflater.inflate(R.layout.address_layout2, null);

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

                    selectedID=String.valueOf(addrtems.get(position).getID());

                    Toast.makeText(getActivity(),selectedID,Toast.LENGTH_LONG).show();

                    final AlertDialog alertDialog = alert.create();
                    alertDialog.show();

                    buttonSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            requestQueue_editADDR = Volley.newRequestQueue(getActivity());


                            final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading", "Please wait...", false, false);
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADDR_EDIT_URL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            loading.dismiss();
                                            Toast.makeText(getActivity(), "response", Toast.LENGTH_LONG).show();

                                            try {

                                                JSONObject jsonResponse = new JSONObject(response);

                                                //  JSONArray jsonArray=new JSONArray(response);

                                                Toast.makeText(getActivity(),jsonResponse.getString(Config.TAG_RESPONSE),Toast.LENGTH_LONG).show();

                                                if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                                    addrtems.get(position).setNAME(name.getText().toString());
                                                    addrtems.get(position).setADDRESS1(address1.getText().toString());
                                                    addrtems.get(position).setPLACE(place.getText().toString());
                                                    addrtems.get(position).setDISTRICT(district.getText().toString());
                                                    addrtems.get(position).setPINCODE("PIN - "+pincode.getText().toString());
                                                    addrtems.get(position).setPHONE("Mobile: "+phone.getText().toString());
                                                    addrtems.get(position).setSTATE(state.getText().toString());

                                                    getView(position, finalConvertView,parent);

                                                    alertDialog.dismiss();


                                                } else if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("failed")) {

                                                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();
                                                    alertDialog.dismiss();

                                                } else {
                                                    alertDialog.dismiss();
                                                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                Toast.makeText(getActivity(),"catch0",Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            loading.dismiss();
                                            //
                                            Toast.makeText(getActivity(), "error1", Toast.LENGTH_LONG).show();
                                        }
                                    }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    //Adding the parameters to the request
                                    // params.put(Config.KEY_ADDR_MOBILE,  pref.getString(SessionManagement.KEY_PHONE,""));
                                    params.put("ca_id", selectedID);
                                    params.put(Config.KEY_ADDR_NAME, name.getText().toString());
                                    //  params.put(Config.KEY_ADDR_MOBILE,  pref.getString(SessionManagement.KEY_PHONE,""));             //change
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
                            requestQueue_editADDR.add(stringRequest);

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


            TextView selectADDR_BTN=(TextView)convertView.findViewById(R.id.ADDR_selectBTN_id);
            selectADDR_BTN.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {




                    Bundle bundle = new Bundle();
                    bundle.putString("p",String.valueOf(position));
                    ShowSelectedADDR showSelectedADDR=new ShowSelectedADDR();
                    showSelectedADDR.setArguments(bundle);

                    FragmentManager fm = null;
                    fm = getFragmentManager();

                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.cart_FL, showSelectedADDR);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

                    //change
                    fragmentTransaction.commit();



                }
            });


            return convertView;
        }





/*
    private void showADDR(String jsonArray) throws JSONException {

        JSONObject json = new JSONObject(jsonArray);
        JSONArray arr = json.getJSONArray("addr");

        for (i = 0; i < arr.length(); i++) {

            try {

                JSONObject tt= arr.getJSONObject(i);

                AddressItem C_item5 = new AddressItem();
                C_item5.setNAME(tt.getString("ca_name"));
                C_item5.setADDRESS1(tt.getString("ca_house"));
                C_item5.setPINCODE(tt.getString("ca_pin"));
                C_item5.setPHONE(tt.getString("ca_phone"));
                C_item5.setDISTRICT(tt.getString("ca_dist"));
                C_item5.setSTATE(tt.getString("ca_state"));
                C_item5.setPLACE(tt.getString("ca_street"));
                C_item5.setID(Integer.parseInt(tt.getString("ca_id")));

                movieList.add(C_item5);

            } catch (JSONException e) {

                e.printStackTrace();

            }
        }

        adapter = new AddressAdapter(getActivity(), movieList.subList(i-1, i));
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }





*/




    }










}
