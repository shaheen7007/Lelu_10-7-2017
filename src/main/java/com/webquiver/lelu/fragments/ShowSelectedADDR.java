package com.webquiver.lelu.fragments;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.app.Fragment;
        import android.app.FragmentManager;
        import android.app.FragmentTransaction;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.SharedPreferences;
        import android.graphics.drawable.ColorDrawable;
        import android.os.Bundle;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.util.Log;
        import android.view.KeyEvent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import android.view.Window;
        import android.widget.BaseAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.android.volley.AuthFailureError;
        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.JsonObjectRequest;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;
        import com.github.johnpersano.supertoasts.library.Style;
        import com.github.johnpersano.supertoasts.library.SuperActivityToast;
        import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;
        import com.webquiver.lelu.ItemActivity;
        import com.webquiver.lelu.R;
        import com.webquiver.lelu.classes.AddressItem;
        import com.webquiver.lelu.classes.Config;
        import com.webquiver.lelu.classes.SessionManagement;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

/**
 * Created by WebQuiver 04 on 7/25/2017.
 */

//This fragment is used to show the selected address (selected from all address)


public class ShowSelectedADDR extends android.app.Fragment {

    private static ShowSelectedADDR addressFragment = null;
    int position;

    RequestQueue queue; //for pincode api
    private RequestQueue requestQueue;
    private RequestQueue requestQueue2;
    private RequestQueue requestQueue_editADDR;


    EditText district,state;




    //private ProgressDialog pDialog;
    private List<AddressItem> movieList = new ArrayList<AddressItem>();
    private ListView listView;
    private AddressAdapter adapter;
    AlertDialog.Builder alert;
    AlertDialog alertDialog;

    int i;

    int numofaddres;
    TextView showALL_TXT_id;





    double bigtotal,totalpayable;
    int numofitems;
    TextView bigtotalTXT,numofitemsTXT,totalpayableTXT;




    //sharedpref
    SharedPreferences pref;
    SharedPreferences pref_cid;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editorcaid;

    String PREF_CAID="pref_caid";
    String caidstring;

    String selectedID;             //ca_id of selected address

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();
        View rootView = inflater.inflate(
                R.layout.address_frag, container, false);

        listView = (ListView) rootView.findViewById(R.id.addressList_id);

        pref = this.getActivity().getSharedPreferences(SessionManagement.PREF_NAME,Context.MODE_PRIVATE);
        pref_cid = this.getActivity().getSharedPreferences(PREF_CAID,Context.MODE_PRIVATE);
        editor = pref.edit();
        editorcaid = pref_cid.edit();

        queue=Volley.newRequestQueue(getActivity());

        showALL_TXT_id=(TextView)rootView.findViewById(R.id.show_all_addr_txt_id);


        Bundle b=getArguments();
        String pos =  b.getString("p");
        bigtotal = Double.parseDouble( b.getString("bigtotal"));
        totalpayable = Double.parseDouble( b.getString("totalpayable"));
        numofitems = Integer.parseInt( b.getString("numofitems"));
        position=Integer.parseInt(pos);


        bigtotalTXT=(TextView)rootView.findViewById(R.id.bitotal_id);
        numofitemsTXT=(TextView)rootView.findViewById(R.id.numofitems_id);
        totalpayableTXT=(TextView)rootView.findViewById(R.id.totalpayable_id);

        bigtotalTXT.setText(String.valueOf("\u20B9"+bigtotal));
        numofitemsTXT.setText(String.valueOf(numofitems)+" items");
        totalpayableTXT.setText(String.valueOf("\u20B9"+totalpayable));



        //    Toast.makeText(getActivity(),pos,Toast.LENGTH_LONG).show();

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("test", "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK ) {
                    Log.i("test", "onKey Back listener is working!!!");
                   // getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    Fragment fr = null;
                    FragmentManager fm = null;
                    View selectedView = null;

                    //fragment
                    fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.cart_FL, CartFragment.getInstance());
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    //   fragmentTransaction.addToBackStack("ct");
                    fragmentTransaction.commit();


                    return true;
                } else {
                    return false;
                }
            }

        });

        TextView  viewdeatails_TXT=(TextView)rootView.findViewById(R.id.viewdetails_TXT_ID);
        viewdeatails_TXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    //fragment
                    Fragment fr = null;
                    FragmentManager fm = null;
                    View selectedView = null;

                    //fragment
                    fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.cart_FL, CartFragment.getInstance());
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    fragmentTransaction.commit();

                } catch (Exception e) {
                    //
                }
            }

        });


        ImageView bckBTN=(ImageView)rootView.findViewById(R.id.bckBTN_id);
        bckBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //   getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                Fragment fr = null;
                FragmentManager fm = null;
                View selectedView = null;


                //fragment
                fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.cart_FL, CartFragment.getInstance());
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                //   fragmentTransaction.addToBackStack("ct");
                fragmentTransaction.commit();






            }
        });







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


        TextView showAllADDR=(TextView)rootView.findViewById(R.id.show_all_addr_txt_id);

        showAllADDR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fragment

                Fragment fr = null;
                FragmentManager fm = null;
                View selectedView = null;

                //fragment
                fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.cart_FL, ShowAllSavedADDR.getInstance());
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                //   fragmentTransaction.addToBackStack("cart2");
                //change
                fragmentTransaction.commit();

            }
        });



        TextView PlaceOrderBTN=(TextView)rootView.findViewById(R.id.placeorderBTN_id);

        PlaceOrderBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(getActivity());
                //Creating a view to get the dialog box
                View confirmDialog = li.inflate(R.layout.rmovefromcart, null);
                alert = new AlertDialog.Builder(getActivity());
                alert.setView(confirmDialog);
                alert.setCancelable(true);
                Button buttonSave = (Button) confirmDialog.findViewById(R.id.buttonSave);
                Button buttonNO = (Button) confirmDialog.findViewById(R.id.buttonCancel);
                TextView textView=(TextView)confirmDialog.findViewById(R.id.msgtx);
                textView.setText("Place order ?");
                alertDialog = alert.create();
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSHAKE;

                buttonSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // TODO Add your code for the button here.

                        final String ca_id = pref_cid.getString("caid", "def");

                        final Dialog loading = new Dialog(getActivity());
                        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        loading.setContentView(R.layout.custom_dialog_progress_loggingin);
                        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        loading.setCancelable(false);
                        TextView t = (TextView) loading.findViewById(R.id.txt);
                        t.setText("Placing your order");
                        loading.show();


                        //   final ProgressDialog loading = ProgressDialog.show(getActivity(), "Placing Order", "Please wait...", false, false);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ODR_PLACE_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        loading.dismiss();
                                        alertDialog.dismiss();

                                        try {

                                            JSONObject jsonResponse = new JSONObject(response);
                                            if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                                Bundle bundle = new Bundle();
                                                bundle.putString("p", "999");
                                                bundle.putString("total", String.valueOf(totalpayable));
                                                bundle.putString("numofitems", String.valueOf(numofitems));
                                                OrderPlacedSuccessfuly showSelectedADDR = new OrderPlacedSuccessfuly();
                                                showSelectedADDR.setArguments(bundle);
                                                FragmentManager fm = null;
                                                fm = getFragmentManager();
                                                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                                fragmentTransaction.replace(R.id.cart_FL, showSelectedADDR);
                                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                                fragmentTransaction.addToBackStack("ord");
                                                fragmentTransaction.commit();


                                            } else {

                                                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
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
                                        alertDialog.dismiss();


                                        SuperActivityToast.create(getActivity(), new Style(), Style.TYPE_STANDARD)
                                                //     .setButtonText("Please click BACK again to exit")
                                                //     .setButtonIconResource(R.drawable.ic_undo)
                                                //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                                                //     .setProgressBarColor(Color.WHITE)
                                                .setText("Something went wrong\nPlease retry")
                                                .setDuration(Style.DURATION_LONG)
                                                .setFrame(Style.FRAME_STANDARD)
                                                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                                                .setAnimations(Style.ANIMATIONS_SCALE).show();




                                        //
                                        //     Toast.makeText(getActivity(), "error1", Toast.LENGTH_LONG).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                //Adding the parameters to the request
                                params.put(Config.KEY_PHONE, pref.getString(SessionManagement.KEY_PHONE, ""));
                                params.put(Config.KEY_CA_ID, ca_id);
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
















        return rootView;


    }

    public static ShowSelectedADDR getInstance() {
        if (addressFragment == null) {
            addressFragment = new ShowSelectedADDR();
        }
        return addressFragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        addressFragment = null;
    }


    public void getall() {


        requestQueue = Volley.newRequestQueue(getActivity());


        final Dialog loading = new Dialog(getActivity());
        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading.setContentView(R.layout.custom_dialog_progress_loggingin);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.setCancelable(false);
        TextView t=(TextView)loading.findViewById(R.id.txt);
        t.setText("Loading");
        loading.show();





        // final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADDR_GET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                     //   Toast.makeText(getActivity(), "response", Toast.LENGTH_LONG).show();

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
                params.put(Config.KEY_ADDR_MOBILE,pref.getString(SessionManagement.KEY_PHONE,""));             //change
                return params;
            }
        };

        //Adding request the the queue
        requestQueue.add(stringRequest);


    }


    public void getall2() {

        //fuction when new address is saved

        requestQueue2 = Volley.newRequestQueue(getActivity());



        final Dialog loading = new Dialog(getActivity());
        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading.setContentView(R.layout.custom_dialog_progress_loggingin);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.setCancelable(false);
        TextView t=(TextView)loading.findViewById(R.id.txt);
        t.setText("Loading");
        loading.show();




        //    final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADDR_GET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                      //  Toast.makeText(getActivity(), "response", Toast.LENGTH_LONG).show();

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
                params.put(Config.KEY_ADDR_MOBILE, pref.getString(SessionManagement.KEY_PHONE,""));             //change
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

        numofaddres=arr.length();


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

                if (i==position)
                {
                    //get caid of selected address
                    caidstring = tt.getString("ca_id");
                }

                movieList.add(C_item5);

            } catch (JSONException e) {

                e.printStackTrace();
                Toast.makeText(getActivity(),"json catch",Toast.LENGTH_LONG).show();

            }
        }

        editorcaid.putString("caid",caidstring);
        editorcaid.commit();


        adapter = new AddressAdapter(getActivity(), movieList.subList(position,position+1));         //.subList(i-1, i)
        listView.setAdapter(adapter);
        movieList.get(position).getID();
        adapter.notifyDataSetChanged();

    }







    public void showADDR2(String jsonArray) throws JSONException {

        //fuction when new address is saved



        JSONObject json = new JSONObject(jsonArray);
        JSONArray arr = json.getJSONArray("addr");
        List<AddressItem> movieList2 = new ArrayList<AddressItem>();

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


                caidstring=tt.getString("ca_id");


                movieList2.add(C_item5);

            } catch (JSONException e) {

                e.printStackTrace();
                Toast.makeText(getActivity(),"json catch",Toast.LENGTH_LONG).show();

            }
        }

        editorcaid.putString("caid",caidstring);
        editorcaid.commit();

        adapter = new AddressAdapter(getActivity(), movieList2.subList(i-1,i));         //.subList(i-1, i)
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
            addr_pincode.setText("PIN -"+m.getPINCODE());
            addr_phone.setText("mobile: "+m.getPHONE());


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

                    selectedID=String.valueOf(addrtems.get(position).getID());


                    final AlertDialog alertDialog = alert.create();
                    alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    alertDialog.show();




                 //   Toast.makeText(getActivity(),selectedID,Toast.LENGTH_LONG).show();



                    buttonSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            String namestring=(name.getText().toString());
                            String addrstring=address1.getText().toString();
                            String placestring=place.getText().toString();
                            String pincodestring=pincode.getText().toString();
                            String districtstring=district.getText().toString();
                            String statestring=state.getText().toString();
                            String phonestring=phone.getText().toString();

                            if (namestring.length()<3) {
                                name.setError("Invalid name");
                                address1.setError(null);
                                place.setError(null);
                                pincode.setError(null);
                                district.setError(null);
                                state.setError(null);
                                phone.setError(null);
                            }
                            else if (addrstring.length() < 3) {
                                address1.setError("Invalid address");
                                name.setError(null);
                                place.setError(null);
                                pincode.setError(null);
                                district.setError(null);
                                state.setError(null);
                                phone.setError(null);
                            }
                            else if (placestring.length() < 3) {
                                place.setError("Invalid place");
                                address1.setError(null);
                                address1.setError(null);
                                name.setError(null);
                                pincode.setError(null);
                                district.setError(null);
                                state.setError(null);
                                phone.setError(null);
                            }
                            else  if (pincodestring.length() < 6) {
                                pincode.setError("Invalid pincode");
                                place.setError(null);
                                address1.setError(null);
                                place.setError(null);
                                name.setError(null);
                                district.setError(null);
                                state.setError(null);
                                phone.setError(null);
                            }
                            else if (districtstring.length() < 3) {
                                district.setError("Invalid district");
                                pincode.setError(null);
                                address1.setError(null);
                                place.setError(null);
                                pincode.setError(null);
                                name.setError(null);
                                state.setError(null);
                                phone.setError(null);
                            }
                            else if (statestring.length() < 3) {
                                district.setError(null);
                                state.setError("Invalid state");
                                address1.setError(null);
                                place.setError(null);
                                pincode.setError(null);
                                district.setError(null);
                                name.setError(null);
                                phone.setError(null);

                            }
                            else if (phonestring.length() < 10) {
                                phone.setError("Invalid phone number");
                                state.setError(null);
                                address1.setError(null);
                                place.setError(null);
                                pincode.setError(null);
                                district.setError(null);
                                state.setError(null);
                                name.setError(null);
                            }
                            else {


                                requestQueue_editADDR = Volley.newRequestQueue(getActivity());


                                final Dialog loading = new Dialog(getActivity());
                                loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                loading.setContentView(R.layout.custom_dialog_progress_loggingin);
                                loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                loading.setCancelable(false);
                                TextView t = (TextView) loading.findViewById(R.id.txt);
                                t.setText("Loading");
                                loading.show();


                                //  final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading", "Please wait...", false, false);
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADDR_EDIT_URL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                loading.dismiss();
                                         //       Toast.makeText(getActivity(), "response", Toast.LENGTH_LONG).show();

                                                try {

                                                    JSONObject jsonResponse = new JSONObject(response);

                                                    //   JSONArray jsonArray=new JSONArray(response);

                                                    if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                                        addrtems.get(position).setNAME(name.getText().toString());
                                                        addrtems.get(position).setADDRESS1(address1.getText().toString());
                                                        addrtems.get(position).setPLACE(place.getText().toString());
                                                        addrtems.get(position).setDISTRICT(district.getText().toString());
                                                        addrtems.get(position).setPINCODE(pincode.getText().toString());
                                                        addrtems.get(position).setPHONE(phone.getText().toString());
                                                        addrtems.get(position).setSTATE(state.getText().toString());

                                                        getView(position, finalConvertView, parent);

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
                                        params.put(Config.KEY_CA_ID, selectedID);
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
                    final EditText pincode=(EditText)confirmDialog.findViewById(R.id.ADR_PincodeET);
                    final EditText phone=(EditText)confirmDialog.findViewById(R.id.ADR_PhoneET);
                    district=(EditText)confirmDialog.findViewById(R.id.ADR_DistrictET);
                    state=(EditText)confirmDialog.findViewById(R.id.ADR_StateET);


                    TextView heading=(TextView)confirmDialog.findViewById(R.id.headtx_id);
                    heading.setText("Add Address");


                    final AlertDialog alertDialog = alert.create();
                    alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    alertDialog.show();


                    TextWatcher mTextEditorWatcher = new TextWatcher() {
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            if ((pincode.getText().toString()).length()==6)
                            {

                                final String url = Config.PICODE_URL+pincode.getText().toString();
                                final JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                // display response
                                                Log.d("Response", response.toString());
                                                //  Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();

                                                try {

                                                    if (response.getString("Status").equalsIgnoreCase("Success")) {

                                                        getdiststate(response);

                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener()
                                        {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.d("Error.Response", error.toString());

                                                //   Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();

                                            }
                                        }
                                );

                                queue.add(getRequest);

                            }

                        }

                        public void afterTextChanged(Editable s) {
                        }

                    };

                    pincode.addTextChangedListener(mTextEditorWatcher);




                    buttonSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {

                            String namestring=(name.getText().toString());
                            String addrstring=address1.getText().toString();
                            String placestring=place.getText().toString();
                            String pincodestring=pincode.getText().toString();
                            String districtstring=district.getText().toString();
                            String statestring=state.getText().toString();
                            String phonestring=phone.getText().toString();

                            if (namestring.length()<3) {
                                name.setError("Invalid name");
                                address1.setError(null);
                                place.setError(null);
                                pincode.setError(null);
                                district.setError(null);
                                state.setError(null);
                                phone.setError(null);
                            }
                            else if (addrstring.length() < 3) {
                                address1.setError("Invalid address");
                                name.setError(null);
                                place.setError(null);
                                pincode.setError(null);
                                district.setError(null);
                                state.setError(null);
                                phone.setError(null);
                            }
                            else if (placestring.length() < 3) {
                                place.setError("Invalid place");
                                address1.setError(null);
                                address1.setError(null);
                                name.setError(null);
                                pincode.setError(null);
                                district.setError(null);
                                state.setError(null);
                                phone.setError(null);
                            }
                            else  if (pincodestring.length() < 6) {
                                pincode.setError("Invalid pincode");
                                place.setError(null);
                                address1.setError(null);
                                place.setError(null);
                                name.setError(null);
                                district.setError(null);
                                state.setError(null);
                                phone.setError(null);
                            }
                            else if (districtstring.length() < 3) {
                                district.setError("Invalid district");
                                pincode.setError(null);
                                address1.setError(null);
                                place.setError(null);
                                pincode.setError(null);
                                name.setError(null);
                                state.setError(null);
                                phone.setError(null);
                            }
                            else if (statestring.length() < 3) {
                                district.setError(null);
                                state.setError("Invalid state");
                                address1.setError(null);
                                place.setError(null);
                                pincode.setError(null);
                                district.setError(null);
                                name.setError(null);
                                phone.setError(null);

                            }
                            else if (phonestring.length() < 10) {
                                phone.setError("Invalid phone number");
                                state.setError(null);
                                address1.setError(null);
                                place.setError(null);
                                pincode.setError(null);
                                district.setError(null);
                                state.setError(null);
                                name.setError(null);
                            }
                            else {

                                requestQueue = Volley.newRequestQueue((v.getRootView().getContext()));


                                final Dialog loading = new Dialog(getActivity());
                                loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                loading.setContentView(R.layout.custom_dialog_progress_loggingin);
                                loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                loading.setCancelable(false);
                                TextView t = (TextView) loading.findViewById(R.id.txt);
                                t.setText("Loading");
                                loading.show();


                                //  final ProgressDialog loading = ProgressDialog.show((v.getRootView().getContext()), "Loading", "Please wait...", false, false);
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADDR_SEND_URL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                loading.dismiss();

                                                try {

                                                    JSONObject jsonResponse = new JSONObject(response);
                                                    if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                                        Toast.makeText((v.getRootView().getContext()), "Address added", Toast.LENGTH_LONG).show();

                                                        int i = numofaddres + 1;
                                                        showALL_TXT_id.setText(String.valueOf("You have " + i + " addresses saved"));


                                                        alertDialog.dismiss();

                                                        getall2();

                                                        // notifyDataSetChanged();

                                                        //get the last saved address

                                                /*
                                                final ProgressDialog loading = ProgressDialog.show((v.getRootView().getContext()), "Loading", "Please wait...", false, false);
                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADDR_GET_URL,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                loading.dismiss();
                                                                Toast.makeText((v.getRootView().getContext()), "response", Toast.LENGTH_LONG).show();


                                                                try {


                                                                    JSONObject jsonResponse = new JSONObject(response);


                                                                    //   JSONArray jsonArray=new JSONArray(response);

                                                                    if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {


                                                                        showADDR(response);



                                                                    } else if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("failed")) {

                                                                        Toast.makeText((v.getRootView().getContext()), "Failed", Toast.LENGTH_LONG).show();


                                                                    } else {

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
                                                        params.put(Config.KEY_ADDR_MOBILE, "9020707009");             //change
                                                        return params;
                                                    }
                                                };

                                                //Adding request the the queue
                                                requestQueue.add(stringRequest);

                                                */

                                                    } else if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("failed")) {

                                                        Toast.makeText((v.getRootView().getContext()), "Failed", Toast.LENGTH_LONG).show();

                                                    } else {

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
                                        params.put(Config.KEY_ADDR_MOBILE, pref.getString(SessionManagement.KEY_PHONE, ""));             //change
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

    private void getdiststate(JSONObject response)throws JSONException {

        JSONArray arr = response.getJSONArray("PostOffice");

        JSONObject tt= arr.getJSONObject(0);

        district.setText(tt.getString("District"));
        state.setText(tt.getString("State"));

    }
}






