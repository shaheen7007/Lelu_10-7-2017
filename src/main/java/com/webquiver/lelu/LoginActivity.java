package com.webquiver.lelu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.webquiver.lelu.classes.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    EditText phoneET,passwordET,editTextConfirmOtp,forgot_phonenumberET;
    ImageView loginbtn;
    SessionManagement session;
    String phone,otpnew,password,phonenum;
    TextView forgotpassbtn;
    private RequestQueue requestQueue;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManagement(getApplicationContext());
        requestQueue = Volley.newRequestQueue(this);


        //rediret if logged in
        if (session.isLoggedIn())
        {
            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }


        //apply font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/latoregular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


        phoneET = (EditText) findViewById(R.id.username_id);
        passwordET = (EditText) findViewById(R.id.password_id);
        loginbtn = (ImageView) findViewById(R.id.loginbtn_id);
        forgotpassbtn=(TextView)findViewById(R.id.forgotpassbtn_id);


//forgot password/reset password
        forgotpassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LayoutInflater li = LayoutInflater.from(LoginActivity.this);

                //Creating a view to get the dialog box
                View confirmDialog = li.inflate(R.layout.otpdialogforgotpass_layout, null);

                AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                alert.setView(confirmDialog);
                alert.setCancelable(true);
                AppCompatButton buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonreset1);
                forgot_phonenumberET = (EditText) confirmDialog.findViewById(R.id.forg_editTextphone_id);
                final AlertDialog alertDialog = alert.create();
                alertDialog.show();

                buttonConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                      phonenum=forgot_phonenumberET.getText().toString();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.FORGOT_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        alertDialog.dismiss();

                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                                forgotpass();
                                                Toast.makeText(LoginActivity.this,jsonResponse.getString(Config.KEY_OTP), Toast.LENGTH_LONG).show();

                                            } else {
                                                Toast.makeText(LoginActivity.this, "Phone number not registered", Toast.LENGTH_LONG).show();
                                                //   confirmotp();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        //loading.dismiss();

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
                                        alertDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "error2", Toast.LENGTH_LONG).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                //Adding the parameters otp and username
                                params.put(Config.KEY_PHONE, phonenum);
                                return params;
                            }
                        };
                        //Adding the request to the queue
                        requestQueue.add(stringRequest);
                    }

                });

            }
        });




        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = phoneET.getText().toString();
                password = passwordET.getText().toString();
                final String password = passwordET.getText().toString();
                if (phone.length() == 10 & password.length() >= 8) {


                    final ProgressDialog loading = ProgressDialog.show(LoginActivity.this, "Logging In", "Please wait...", false, false);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    loading.dismiss();


                                    try {

                                        JSONObject jsonResponse = new JSONObject(response);
                                        if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {


                                            session.createLoginSession(password, phone);
                                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                            finish();

                                        }
                                        else if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("not verified"))
                                        {

                                            // if user not verified phonenumber

                                            otpnew=jsonResponse.getString(Config.KEY_OTP);
                                            confirmotp();




                                        }


                                        else {
                                            //If not successful user may already have registered
                                            Toast.makeText(LoginActivity.this, "Invalid phonenumber or password", Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(LoginActivity.this, "error1", Toast.LENGTH_LONG).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            //Adding the parameters to the request
                            params.put(Config.KEY_PHONE, phone);
                            params.put(Config.KEY_PASSWORD, password);
                            return params;
                        }
                    };

                    //Adding request the the queue
                    requestQueue.add(stringRequest);
                } else {
                    Snackbar snackbar = Snackbar
                            .make(loginbtn, "Invalid email or password", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
            }
        });



        //code for the redlinr in edittext

        phoneET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                phoneET.setFocusable(true);
                phoneET.setCursorVisible(true);
                //  (usernameET).setCompoundDrawablesWithIntrinsicBounds(R.drawable.txtovr, 0, 0, 0);
                //  (passwordET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                phoneET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_selected));
                passwordET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                return false;

            }
        });

        passwordET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                passwordET.setFocusable(true);
                passwordET.setCursorVisible(true);
                passwordET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_selected));
                phoneET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));


                if (isPhoneValid(phoneET.getText().toString()))
                {
                    (phoneET).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.smalltick, 0);


                }
                else
                    (phoneET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);


                return false;


            }
        });


        phoneET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                passwordET.setCursorVisible(true);
                passwordET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_selected));
                phoneET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));

                if (isPhoneValid(phoneET.getText().toString()))
                {
                    (phoneET).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.smalltick, 0);


                }
                else
                    (phoneET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);



                return false;


            }
        });

        passwordET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                passwordET.setCursorVisible(false);
                passwordET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.regedittext_shape_rounded));
                if (passwordET.getText().toString().length()>=8)
                {
                    (passwordET).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.smalltick, 0);


                }
                else
                    (passwordET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);




                return false;
            }
        });



//check email format correct or not

        /*
        usernameET.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){

                if (isEmailValid(usernameET.getText().toString()))
                {
                    (usernameET).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.smalltick, 0);


                }
                else
                    (usernameET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            }
        });

*/
    }

    private void forgotpass() {

        LayoutInflater li = LayoutInflater.from(LoginActivity.this);

        //Creating a view to get the dialog box
        final View confirmDialog = li.inflate(R.layout.otpdialogforgotpass_layout2, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
        alert.setView(confirmDialog);
        alert.setCancelable(true);
        AppCompatButton buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.resetbtn2_id);
        final AlertDialog alertDialog = alert.create();
        alertDialog.show();

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText forgot_OTPET = (EditText) confirmDialog.findViewById(R.id.forg_editextotp_id);
                EditText forgot_PassET = (EditText) confirmDialog.findViewById(R.id.forg_editTextpass_id);
                EditText forgot_ConfET = (EditText) confirmDialog.findViewById(R.id.forg_editTextconfpass_id);

                final String f_otp = forgot_OTPET.getText().toString();
                String f_pass = forgot_PassET.getText().toString();
                final String f_confpass = forgot_ConfET.getText().toString();

                if (passwordvalid(f_pass)) {
                    if (!f_pass.equals(f_confpass)) {
                        forgot_ConfET.setError("Passwords doesn't match");
                        Toast.makeText(LoginActivity.this,"Passwords doesn't match",Toast.LENGTH_LONG).show();
                    } else {

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.FORGOT2_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                                alertDialog.dismiss();
                                                Toast.makeText(LoginActivity.this, "Password Reset Successfull", Toast.LENGTH_LONG).show();

                                            } else {
                                                Toast.makeText(LoginActivity.this, "Wrong OTP", Toast.LENGTH_LONG).show();

                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        alertDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "error2", Toast.LENGTH_LONG).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                //Adding the parameters otp and username
                                params.put(Config.KEY_OTP, f_otp);
                                params.put(Config.KEY_PASSWORD, f_confpass);
                                params.put(Config.KEY_PHONE, phonenum);

                                return params;
                            }
                        };

                        //Adding the request to the queue
                        requestQueue.add(stringRequest);
                    }
                }
            }
        });
    }







    private boolean passwordvalid(String f_pass) {

        if (f_pass.length()>8)
        {
            return  true;
        }
        else
            return false;

    }


    // if user not confirmed phonenumber
    private void confirmotp()  throws JSONException {

        Toast.makeText(LoginActivity.this,otpnew,Toast.LENGTH_LONG).show();

        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(this);

        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.otpdialog2_layout, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(confirmDialog);
        alert.setCancelable(false);
        AppCompatButton buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);
        editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.editTextOtp);

        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                final String otp = editTextConfirmOtp.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CONFIRM_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {
                                        //Asking user to confirm otp

                                        session.createLoginSession(password, phone);
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();


                                    } else {
                                        Toast.makeText(LoginActivity.this, "Wrong OTP", Toast.LENGTH_LONG).show();
                                        //   confirmotp();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //loading.dismiss();

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
                                alertDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "error2", Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        //Adding the parameters otp and username
                        params.put(Config.KEY_OTP, otp);
                        params.put(Config.KEY_PHONE, phone);
                        return params;
                    }
                };

                //Adding the request to the queue
                requestQueue.add(stringRequest);
            }
        });
    }


    public static boolean isPhoneValid(String phone) {
            if (phone.length()==10)
            {
                return true;
            }
            else
                return false;

        }



    public void onclickhandler(View view) {
        if (view == findViewById(R.id.regbtn_id)) {

            Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);

        } else if (view == findViewById(R.id.indoorIMG_iid)) {


        }
    }




}
