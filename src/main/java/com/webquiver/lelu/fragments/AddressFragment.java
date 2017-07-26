package com.webquiver.lelu.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.webquiver.lelu.R;
import com.webquiver.lelu.adapters.AddressAdapter;
import com.webquiver.lelu.classes.AddressItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WebQuiver 04 on 7/25/2017.
 */

public class AddressFragment extends android.app.Fragment {

private static AddressFragment addressFragment = null;
private static final String url = "https://api.myjson.com/bins/15eqh3";
//private ProgressDialog pDialog;
private List<AddressItem> movieList = new ArrayList<AddressItem>();
private ListView listView;
private AddressAdapter adapter;


@Override
public View onCreateView(LayoutInflater inflater,
                         ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
        R.layout.address_frag, container, false);

        listView = (ListView) rootView.findViewById(R.id.addressList_id);



        adapter = new AddressAdapter(getActivity(), movieList);
        listView.setAdapter(adapter);



        AddressItem C_item = new AddressItem();
        C_item.setNAME("shaheen Shabib");
        C_item.setADDRESS1("dasdf sfsd dfds");
        C_item.setPLACE("Othayi");
        C_item.setDISTRICT("Malappuram");
        C_item.setSTATE("Kerala");
        C_item.setPINCODE("676541");
        C_item.setPHONE("9020707009");
        movieList.add(C_item);

    AddressItem C_item2 = new AddressItem();
    C_item2.setNAME("shaheen Shabib");
    C_item2.setADDRESS1("Webquiver");
    C_item2.setPLACE("Eranjipalam");
    C_item2.setDISTRICT("Calicut");
    C_item2.setSTATE("Kerala");
    C_item2.setPINCODE("6768574");
    C_item2.setPHONE("9020707009");
    movieList.add(C_item2);

    AddressItem C_item3 = new AddressItem();
    C_item3.setNAME("shaheen BSV");
    C_item3.setADDRESS1("dasdf sfsd dfds");
    C_item3.setPLACE("Othayi");
    C_item3.setDISTRICT("Malappuram");
    C_item3.setSTATE("Kerala");
    C_item3.setPINCODE("676541");
    C_item3.setPHONE("8606198378");
    movieList.add(C_item3);

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

        adapter.notifyDataSetChanged();






    TextView addr_newBTN=(TextView)rootView.findViewById(R.id.change);//change
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
                public void onClick(View v) {

                    AddressItem C_item3 = new AddressItem();
                    C_item3.setNAME(name.getText().toString());
                    C_item3.setADDRESS1(address1.getText().toString());
                    C_item3.setPLACE(place.getText().toString());
                    C_item3.setDISTRICT(district.getText().toString());
                    C_item3.setSTATE(state.getText().toString());
                    C_item3.setPINCODE(pincode.getText().toString());
                    C_item3.setPHONE(phone.getText().toString());
                    movieList.add(0,C_item3);
                    listView.smoothScrollToPosition(0);

                    adapter.notifyDataSetChanged();
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


        return rootView;


        }
public static AddressFragment getInstance() {
        if ( addressFragment== null) {
        addressFragment = new AddressFragment();
        }
        return addressFragment;
        }

@Override
public void onDestroy() {
        super.onDestroy();
        addressFragment = null;
        }
        }
