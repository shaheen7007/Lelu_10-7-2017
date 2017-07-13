package com.webquiver.lelu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.webquiver.lelu.classes.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class RegisterActivity extends AppCompatActivity {

    EditText name,phone,email,companyname,place,pass;
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
        email=(EditText)findViewById(R.id.emailET_id);
        companyname=(EditText)findViewById(R.id.companyET_id);
        place=(EditText)findViewById(R.id.placeET_id);
        pass=(EditText)findViewById(R.id.passworET_id);
        regbtn=(ImageView)findViewById(R.id.regnow_id);





        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register();

            }
        });









        //code for the redlinr in edittext
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
                place.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                pass.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                companyname.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
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
                place.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                companyname.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                return false;

            }
        });



        place.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                place.setFocusable(true);
                place.setCursorVisible(true);
                //  (usernameET).setCompoundDrawablesWithIntrinsicBounds(R.drawable.txtovr, 0, 0, 0);
                //  (passwordET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                place.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_selected));
                phone.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                pass.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                email.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                name.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                companyname.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
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
                place.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                pass.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                name.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                email.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                companyname.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

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
                place.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                pass.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                name.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                phone.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                companyname.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

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
                place.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                name.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                pass.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                phone.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                email.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

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
                place.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                phone.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                name.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                email.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                companyname.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
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
                place.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                name.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                phone.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                pass.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                companyname.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
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
                place.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                name.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                phone.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                pass.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                email.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                return false;
            }
        });

        companyname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                place.setFocusable(true);
                place.setCursorVisible(true);
                //  (usernameET).setCompoundDrawablesWithIntrinsicBounds(R.drawable.txtovr, 0, 0, 0);
                //  (passwordET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                place.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_selected));
                companyname.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                //place.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                name.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                phone.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                pass.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                email.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                return false;
            }
        });


        place.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
               // place.setFocusable(true);
               // place.setCursorVisible(true);
                //  (usernameET).setCompoundDrawablesWithIntrinsicBounds(R.drawable.txtovr, 0, 0, 0);
                //  (passwordET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                //place.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_selected));
                place.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                //place.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                name.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                phone.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                pass.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                email.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
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
                place.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                //place.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                name.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
              //  phone.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                pass.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                email.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                return false;
            }
        });




    }








    public void onclickhandler(View view) {
        if (view == findViewById(R.id.loginbtn2_id)) {

            Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);

        } else if (view == findViewById(R.id.indoorIMG_iid)) {


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

        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();

        //On the click of the confirm button from alert dialog
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hiding the alert dialog
                alertDialog.dismiss();

                //Displaying a progressbar
                final ProgressDialog loading = ProgressDialog.show(RegisterActivity.this, "Authenticating", "Please wait while we check the entered code", false, false);

                //Getting the user entered otp from edittext
                final String otp = editTextConfirmOtp.getText().toString().trim();

                //Creating an string request
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CONFIRM_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //if the server response is success
                                if (response.equalsIgnoreCase("success")) {
                                    //dismissing the progressbar
                                    loading.dismiss();

                                    //Starting a new activity
                                    Toast.makeText(RegisterActivity.this, "Correct", Toast.LENGTH_LONG).show();

                                } else {
                                    //Displaying a toast if the otp entered is wrong
                                    Toast.makeText(RegisterActivity.this, "Wrong OTP", Toast.LENGTH_LONG).show();

                                   //edit
                                    loading.dismiss();


                                    try {
                                        //Asking user to enter otp again
                                        confirmOtp();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                alertDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        //Adding the parameters otp and username
                        params.put(Config.KEY_OTP, otp);
                        params.put(Config.KEY_USERNAME, username);
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
        final ProgressDialog loading = ProgressDialog.show(this, "Registering", "Please wait...", false, false);


        //Getting user data
        username = name.getText().toString().trim();
        password = pass.getText().toString().trim();
        phonestring = phone.getText().toString().trim();
        emailstring = email.getText().toString().trim();
        companynamestring = companyname.getText().toString().trim();
        placestring = place.getText().toString().trim();


        //Again creating the string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();



                        try {


                          //edit

                            confirmOtp();
                            //Creating the json object from the response
                            JSONObject jsonResponse = new JSONObject(response);

                            //If it is success
                            if(jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")){
                                //Asking user to confirm otp
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
                        loading.dismiss();
                        Toast.makeText(RegisterActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put(Config.KEY_USERNAME, username);
                params.put(Config.KEY_PASSWORD, password);
                params.put(Config.KEY_PHONE, phonestring);
                return params;
            }
        };

        //Adding request the the queue
        requestQueue.add(stringRequest);
    }

}
