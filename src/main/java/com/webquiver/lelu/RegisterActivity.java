package com.webquiver.lelu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.webquiver.lelu.classes.Config;
import com.webquiver.lelu.place.PlaceJSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class RegisterActivity extends AppCompatActivity {
    EditText name,phone,email,companyname,pass;
    AutoCompleteTextView placeET;
    PlacesTask placesTask;
    ParserTask parserTask;
    ImageView regbtn;
    private AppCompatButton buttonConfirm;
    private RequestQueue requestQueue;
    private EditText editTextConfirmOtp;
    private String username;
    private String password;
    private String phonestring,emailstring,companynamestring,placestring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestQueue = Volley.newRequestQueue(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //apply font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/latoregular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        //initialise components
        name=(EditText)findViewById(R.id.nameET_id);
        phone=(EditText)findViewById(R.id.phoneET_id);
        email=(EditText) findViewById(R.id.emailET_id);
        companyname=(EditText) findViewById(R.id.companyET_id);
        placeET =(AutoCompleteTextView) findViewById(R.id.placeET_id);
        placeET.setThreshold(1);
        pass=(EditText)findViewById(R.id.passworET_id);
        regbtn=(ImageView)findViewById(R.id.regnow_id);



        placeET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });











        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                register();

            }
        });









        //code for the redlinr and tick in edittext
        name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                name.setFocusable(true);
                name.setCursorVisible(true);
                //  (usernameET).setCompoundDrawablesWithIntrinsicBounds(R.drawable.txtovr, 0, 0, 0);
                //  (passwordET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                name.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_selected));
                phone.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                email.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                placeET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                pass.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                companyname.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                smalltick();
                return false;

            }
        });

        pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                pass.setFocusable(true);
                pass.setCursorVisible(true);
                //  (usernameET).setCompoundDrawablesWithIntrinsicBounds(R.drawable.txtovr, 0, 0, 0);
                //  (passwordET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                pass.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_selected));
                phone.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                name.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                email.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                placeET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                companyname.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                smalltick();
                return false;

            }
        });

        placeET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                placeET.setFocusable(true);
               placeET.setCursorVisible(true);
                //  (usernameET).setCompoundDrawablesWithIntrinsicBounds(R.drawable.txtovr, 0, 0, 0);
                //  (passwordET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                placeET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_selected));
                phone.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                pass.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                email.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                name.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                companyname.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                smalltick();

                return false;

            }
        });



        phone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                phone.setFocusable(true);
                phone.setCursorVisible(true);
                //  (usernameET).setCompoundDrawablesWithIntrinsicBounds(R.drawable.txtovr, 0, 0, 0);
                //  (passwordET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                phone.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_selected));
                placeET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                pass.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                name.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                email.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                companyname.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                smalltick();

                return false;


            }
        });
        email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                email.setFocusable(true);
                email.setCursorVisible(true);
                //  (usernameET).setCompoundDrawablesWithIntrinsicBounds(R.drawable.txtovr, 0, 0, 0);
                //  (passwordET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                email.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_selected));
                placeET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                pass.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                name.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                phone.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                companyname.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                smalltick();

                return false;


            }
        });
        companyname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                companyname.setFocusable(true);
                companyname.setCursorVisible(true);
                //  (usernameET).setCompoundDrawablesWithIntrinsicBounds(R.drawable.txtovr, 0, 0, 0);
                //  (passwordET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                companyname.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_selected));
                placeET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                name.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                pass.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                phone.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                email.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                smalltick();

                return false;

            }
        });


        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                pass.setFocusable(true);
                pass.setCursorVisible(true);
                //  (usernameET).setCompoundDrawablesWithIntrinsicBounds(R.drawable.txtovr, 0, 0, 0);
                //  (passwordET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                pass.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_selected));
                placeET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                phone.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                name.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                email.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                companyname.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                smalltick();

                return false;


            }
        });


        phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                email.setFocusable(true);
                email.setCursorVisible(true);
                //  (usernameET).setCompoundDrawablesWithIntrinsicBounds(R.drawable.txtovr, 0, 0, 0);
                //  (passwordET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                email.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_selected));
                placeET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                name.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                phone.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                pass.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                companyname.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                smalltick();

                return false;
            }
        });

        email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                companyname.setFocusable(true);
                companyname.setCursorVisible(true);
                //  (usernameET).setCompoundDrawablesWithIntrinsicBounds(R.drawable.txtovr, 0, 0, 0);
                //  (passwordET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                companyname.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_selected));
                placeET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                name.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                phone.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                pass.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                email.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                smalltick();

                return false;
            }
        });

        companyname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
               // placeET.setFocusable(true);
               // placeET.setCursorVisible(true);
                //  (usernameET).setCompoundDrawablesWithIntrinsicBounds(R.drawable.txtovr, 0, 0, 0);
                //  (passwordET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                placeET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_selected));
                companyname.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                //placeET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                name.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                phone.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                pass.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                email.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                smalltick();

                return false;
            }
        });


        placeET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
               // placeET.setFocusable(true);
               // placeET.setCursorVisible(true);
                //  (usernameET).setCompoundDrawablesWithIntrinsicBounds(R.drawable.txtovr, 0, 0, 0);
                //  (passwordET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                //placeET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_selected));
                placeET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                //placeET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                name.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                phone.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                pass.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                email.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                smalltick();

                return false;
            }
        });

        pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                 phone.setFocusable(true);
                phone.setCursorVisible(true);
                //  (usernameET).setCompoundDrawablesWithIntrinsicBounds(R.drawable.txtovr, 0, 0, 0);
                //  (passwordET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                phone.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_selected));
                placeET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                //placeET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                name.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
              //  phone.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                pass.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                email.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                smalltick();

                return false;
            }
        });




    }


    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception ", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyBKC5T0UivrnygT6wwuwHXJ4K9ix7u8_bA";

            String input="";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }


            // place type to be searched
            String types = "radius=1000";

            // Sensor enabled
            String sensor = "sensor=false";

            String location="location=9.931233,76.267303"; //


            // Building the parameters to the web service
            String parameters = input+"&"+location+"&"+types+"&"+key;

            // Output format
            String output = "json";



            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

            try{
                // Fetching the data from web service in background
                data = downloadUrl(url);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }


    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[] { "description"};
            int[] to = new int[] { android.R.id.text1 };

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_2, from, to);

            // Setting the adapter
            placeET.setAdapter(adapter);

        }
    }


    public void onclickhandler(View view) {
        if (view == findViewById(R.id.loginbtn2_id)) {

            Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);

        } else if (view == findViewById(R.id.indoorIMG_iid)) {


        }
        else if (view == findViewById(R.id.backtext_id)) {
                    onBackPressed();

        } else if (view == findViewById(R.id.backPic)) {
                    onBackPressed();
        }
    }


    private void confirmOtp() throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(this);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.otpdialog_layout, null);

        buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);


        //Initizliaing confirm button fo dialog box and edittext of dialog box
        buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);
        editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.editTextOtp);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);
        alert.setCancelable(false);

        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.show();

        //On the click of the confirm button from alert dialog
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hiding the alert dialog
                alertDialog.dismiss();

                //Displaying a progressbar
            //    final ProgressDialog loading = ProgressDialog.show(RegisterActivity.this, "Authenticating", "Please wait while we check the entered code", false, false);


                final Dialog progressDialog = new Dialog(RegisterActivity.this);
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressDialog.setContentView(R.layout.custom_dialog_progress_register);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.setCancelable(false);
                progressDialog.show();


                //Getting the user entered otp from edittext
                final String otp = editTextConfirmOtp.getText().toString().trim();

                //Creating an string request
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CONFIRM_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {
                                        //Asking user to confirm otp
                                        Toast.makeText(RegisterActivity.this, "Registration Successfull", Toast.LENGTH_LONG).show();
                                        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Wrong OTP", Toast.LENGTH_LONG).show();
                                        confirmOtp();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                progressDialog.dismiss();

                                //if the server response is success
                           //     if (response.equalsIgnoreCase("success")) {
                                    //dismissing the progressbar
                                  //  loading.dismiss();

                                    //Starting a new activity


                                   //edit

                                }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "error2", Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        //Adding the parameters otp and username
                        params.put(Config.KEY_OTP, otp);
                        params.put(Config.KEY_PHONE, phonestring);
                        return params;
                    }
                };

                 //Adding the request to the queue
                requestQueue.add(stringRequest);
            }
        });
    }





    //this method will register the user
    private void register() {

        //Displaying a progress dialog



        //Getting user data
        username = name.getText().toString().trim();
        password = pass.getText().toString().trim();
        phonestring = phone.getText().toString().trim();
        emailstring = email.getText().toString().trim();
        companynamestring = companyname.getText().toString().trim();
        placestring = placeET.getText().toString().trim();

        if(namevalidation(username))
        {
            if(passwordvalidation(password))
            {
                if(phonevalidation(phonestring))
                {
                  if(companyvalidation(companynamestring))
                  {

              //        final ProgressDialog loading = ProgressDialog.show(this, "Registering", "Please wait...", false, false);

                      final Dialog progressDialog = new Dialog(RegisterActivity.this);
                      progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                      progressDialog.setContentView(R.layout.custom_dialog_progress_register);
                      progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                      progressDialog.setCancelable(false);
                      progressDialog.show();



                      StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.REGISTER_URL,
                              new Response.Listener<String>() {
                                  @Override
                                  public void onResponse(String response) {
                            //          loading.dismiss();



                                      try {
                                          //Creating the json object from the response
                                          JSONObject jsonResponse = new JSONObject(response);
                                          //If it is success
                                          if(jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")){
                                              //Asking user to confirm otp
                                              //   Toast.makeText(RegisterActivity.this,jsonResponse.getString(Config.KEY_OTP), Toast.LENGTH_LONG).show();

                                              confirmOtp();
                                          }else{
                                              //If not successful user may already have registered
                                              Toast.makeText(RegisterActivity.this, "Username or Phone number already registered", Toast.LENGTH_LONG).show();
                                          }
                                      } catch (JSONException e) {
                                          e.printStackTrace();
                                      }
                                  }
                              },
                              new Response.ErrorListener() {
                                  @Override
                                  public void onErrorResponse(VolleyError error) {
                                   //   loading.dismiss();
                                      //
                                      Toast.makeText(RegisterActivity.this,"error1",Toast.LENGTH_LONG).show();
                                  }
                              }) {
                          @Override
                          protected Map<String, String> getParams() throws AuthFailureError {
                              Map<String, String> params = new HashMap<>();
                              //Adding the parameters to the request
                              params.put(Config.KEY_USERNAME, username);
                              params.put(Config.KEY_PASSWORD, password);
                              params.put(Config.KEY_PHONE, phonestring);
                              params.put(Config.KEY_COMPANY, companynamestring);
                              params.put(Config.KEY_EMAIL, emailstring);
                              params.put(Config.KEY_PLACE, placestring);
                              return params;
                          }
                      };

                      //Adding request the the queue
                      requestQueue.add(stringRequest);



                  }
                }
            }
        }
        else
        {
            Toast.makeText(this, "Please fill all fields and retry", Toast.LENGTH_SHORT).show();
        }



    }


    //validations


    public static boolean emailvalidation(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean phonevalidation(String phone) {

        if (phone.length()==10)
        {
            return true;
        }
        else
            return false;
    }

    public static boolean passwordvalidation(String pass) {
        if (pass.length()<8)
            return false;

       return true;
    }


    public static boolean companyvalidation(String company) {

        if (company.length()>=2)
        {
            return true;
        }
        else
            return false;
    }

    public static boolean placevalidation(String company) {

        if (company.length()>=2)
        {
            return true;
        }
        else
            return false;
    }

    public static boolean namevalidation(String company) {

        if (company.length()>=2)
        {
            return true;
        }
        else
            return false;
    }


public void smalltick()
{

    if (emailvalidation(email.getText().toString()))
    {


        (email).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.smalltick, 0);


    }
    else
        (email).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);


    if (phonevalidation(phone.getText().toString()))
    {


        (phone).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.smalltick, 0);


    }
    else
        (phone).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);


    if (passwordvalidation(pass.getText().toString()))
    {


        (pass).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.smalltick, 0);


    }
    else
        (pass).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);


    if (companyvalidation(companyname.getText().toString()))
    {


        (companyname).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.smalltick, 0);


    }
    else
        (companyname).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

    if (placevalidation(placeET.getText().toString()))
    {


        (placeET).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.smalltick, 0);


    }
    else
        (placeET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

    if (namevalidation(name.getText().toString()))
    {


        (name).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.smalltick, 0);


    }
    else
        (name).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);


}

}
