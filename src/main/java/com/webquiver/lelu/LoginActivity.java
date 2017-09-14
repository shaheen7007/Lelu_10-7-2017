package com.webquiver.lelu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
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

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;
import com.webquiver.lelu.classes.Config;
import com.webquiver.lelu.classes.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.keyboardsurfer.android.widget.crouton.Crouton;
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
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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

                if (isOnline()) {

                    LayoutInflater li = LayoutInflater.from(LoginActivity.this);

                    //Creating a view to get the dialog box
                    View confirmDialog = li.inflate(R.layout.otpdialogforgotpass_layout, null);

                    AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    alert.setView(confirmDialog);
                    alert.setCancelable(true);
                    AppCompatButton buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonreset1);
                    forgot_phonenumberET = (EditText) confirmDialog.findViewById(R.id.forg_editTextphone_id);
                    final AlertDialog alertDialog = alert.create();
                    alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    alertDialog.show();

                    buttonConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            phonenum = forgot_phonenumberET.getText().toString();
                            if (isPhoneValid(phonenum)) {
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.FORGOT_URL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                alertDialog.dismiss();

                                                try {
                                                    JSONObject jsonResponse = new JSONObject(response);
                                                    if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                                        forgotpass();
                                                        // Toast.makeText(LoginActivity.this,jsonResponse.getString(Config.KEY_OTP), Toast.LENGTH_LONG).show();

                                                    } else {
                                                        //  Toast.makeText(LoginActivity.this, "Phone number not registered", Toast.LENGTH_LONG).show();

                                                        SuperActivityToast.create(LoginActivity.this, new Style(), Style.TYPE_STANDARD)
                                                                //     .setButtonText("Please click BACK again to exit")
                                                                //     .setButtonIconResource(R.drawable.ic_undo)
                                                                //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                                                                //     .setProgressBarColor(Color.WHITE)
                                                                .setText("Mobile number not registered")
                                                                .setDuration(Style.DURATION_LONG)
                                                                .setFrame(Style.FRAME_STANDARD)
                                                                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                                                                .setAnimations(Style.ANIMATIONS_POP).show();


                                                        //   confirmotp();
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
                                        params.put(Config.KEY_PHONE, phonenum);
                                        return params;
                                    }
                                };
                                //Adding the request to the queue
                                requestQueue.add(stringRequest);
                            }

                            else
                                forgot_phonenumberET.setError("Invalid phone number");

                        }

                    });

                }
            }
        });




        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = phoneET.getText().toString();
                password = passwordET.getText().toString();
                final String password = passwordET.getText().toString();

                if (isOnline()) {


                    if (!(phone.length() == 10))
                    {
                        //  phoneET.setError("Invalid phone number");

                        Crouton.makeText(LoginActivity.this, "Please enter a valid phone number", de.keyboardsurfer.android.widget.crouton.Style.INFO).show();

                    /*    SuperActivityToast.create(LoginActivity.this, new Style(), Style.TYPE_STANDARD)
                                //     .setButtonText("Please click BACK again to exit")
                                //     .setButtonIconResource(R.drawable.ic_undo)
                                //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                                //     .setProgressBarColor(Color.WHITE)
                                .setText("Invalid phonenumber")
                                .setDuration(Style.DURATION_VERY_SHORT)
                                .setFrame(Style.FRAME_STANDARD)
                                .setGravity(Gravity.CENTER)
                                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                                .setAnimations(Style.ANIMATIONS_FADE).show();

*
                    }

                    else if(!(password.length() >= 8))
                    {
                      //  passwordET.setError("Invalid password");


                        Crouton.makeText(LoginActivity.this, "Please enter a valid password", de.keyboardsurfer.android.widget.crouton.Style.INFO).show();


/*
                        SuperActivityToast.create(LoginActivity.this, new Style(), Style.TYPE_STANDARD)
                                //     .setButtonText("Please click BACK again to exit")
                                //     .setButtonIconResource(R.drawable.ic_undo)
                                //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                                //     .setProgressBarColor(Color.WHITE)
                                .setText("Invalid password")
                                .setDuration(Style.DURATION_VERY_SHORT)
                                .setFrame(Style.FRAME_STANDARD)
                                .setGravity(Gravity.CENTER)
                                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                                .setAnimations(Style.ANIMATIONS_FADE).show();
*/





                    }

                    else if (phone.length() == 10 & password.length() >= 8) {


                        final Dialog progressDialog = new Dialog(LoginActivity.this);
                        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        progressDialog.setContentView(R.layout.custom_dialog_progress_loggingin);
                        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        //   final ProgressDialog loading = ProgressDialog.show(LoginActivity.this, "Logging In", "Please wait...", false, false);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        //           loading.dismiss();
                                        progressDialog.dismiss();

                                        try {

                                            JSONObject jsonResponse = new JSONObject(response);
                                            if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                                JSONArray det=jsonResponse.getJSONArray("details");
                                                JSONObject usr=det.getJSONObject(0);
                                                String name=usr.getString("cust_username");
                                                //wallet amount
                                                //customer type - 2 rate

                                                session.createLoginSession(password, phone,name);
                                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                                startActivity(intent);
                                                finish();

                                            } else if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("not verified")) {

                                                // if user not verified phonenumber

                                                otpnew = jsonResponse.getString(Config.KEY_OTP);
                                                confirmotp();


                                            } else {
                                                //If not successful user may already have registered


                                                SuperActivityToast.create(LoginActivity.this, new Style(), Style.TYPE_STANDARD)
                                                        //     .setButtonText("Please click BACK again to exit")
                                                        //     .setButtonIconResource(R.drawable.ic_undo)
                                                        //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                                                        //     .setProgressBarColor(Color.WHITE)
                                                        .setText("Invalid credentials")
                                                        .setDuration(Style.DURATION_LONG)
                                                        .setFrame(Style.FRAME_STANDARD)
                                                        .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                                                        .setAnimations(Style.ANIMATIONS_POP).show();

                                                //  Crouton.makeText(LoginActivity.this, "Invalid credentials", de.keyboardsurfer.android.widget.crouton.Style.ALERT).show();


                                                // Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // loading.dismiss();
                                        progressDialog.dismiss();
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

                        /*
                        SuperActivityToast.create(LoginActivity.this, new Style(), Style.TYPE_STANDARD)
                                //     .setButtonText("Please click BACK again to exit")
                                //     .setButtonIconResource(R.drawable.ic_undo)
                                //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                                //     .setProgressBarColor(Color.WHITE)
                                .setText("Invalid username or password")
                                .setDuration(Style.DURATION_LONG)
                                .setFrame(Style.FRAME_STANDARD)
                                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                                .setAnimations(Style.ANIMATIONS_POP).show();
                                */

                        Crouton.makeText(LoginActivity.this, "Please enter a valid password", de.keyboardsurfer.android.widget.crouton.Style.INFO).show();

                    }
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
                passwordET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_shape_rounded));
                return false;

            }
        });

        passwordET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                passwordET.setFocusable(true);
                passwordET.setCursorVisible(true);
                passwordET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_selected));
                phoneET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_shape_rounded));


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
                phoneET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_shape_rounded));

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
                passwordET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_shape_rounded));
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
        alert.setCancelable(false);
        AppCompatButton buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.resetbtn2_id);
        final AlertDialog alertDialog = alert.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
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

                    if (passwordvalid(f_pass)) {
                        if (!f_pass.equals(f_confpass)) {
                            forgot_ConfET.setError("Passwords doesn't match");
                            //       Toast.makeText(LoginActivity.this,"Passwords doesn't match",Toast.LENGTH_LONG).show();
                        } else {

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.FORGOT2_URL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            try {
                                                JSONObject jsonResponse = new JSONObject(response);
                                                if (jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")) {

                                                    alertDialog.dismiss();
                                                    //   Toast.makeText(LoginActivity.this, "Password Reset Successfull", Toast.LENGTH_LONG).show();

                                                    SuperActivityToast.create(LoginActivity.this, new Style(), Style.TYPE_STANDARD)
                                                            //     .setButtonText("Please click BACK again to exit")
                                                            //     .setButtonIconResource(R.drawable.ic_undo)
                                                            //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                                                            //     .setProgressBarColor(Color.WHITE)
                                                            .setText("Password reset successful")
                                                            .setDuration(Style.DURATION_LONG)
                                                            .setFrame(Style.FRAME_STANDARD)
                                                            .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREEN))
                                                            .setAnimations(Style.ANIMATIONS_POP).show();


                                                } else {
                                                    SuperActivityToast.create(LoginActivity.this, new Style(), Style.TYPE_STANDARD)
                                                            //     .setButtonText("Please click BACK again to exit")
                                                            //     .setButtonIconResource(R.drawable.ic_undo)
                                                            //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                                                            //     .setProgressBarColor(Color.WHITE)
                                                            .setText("Wrong OTP")
                                                            .setDuration(Style.DURATION_LONG)
                                                            .setFrame(Style.FRAME_STANDARD)
                                                            .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                                                            .setAnimations(Style.ANIMATIONS_POP).show();
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
                else
                {
                    forgot_PassET.setError("Invalid password");
                }
            }
        });
    }







    private boolean passwordvalid(String f_pass) {

            Pattern p = Pattern.compile("[^A-Za-z0-9]");
            Matcher m = p.matcher(f_pass);
            // boolean b = m.matches();
            boolean b = m.find();

            if (f_pass.length()<8 || b ) {

                return false;

            }

            return true;
    }


    // if user not confirmed phonenumber
    private void confirmotp()  throws JSONException {

        //Toast.makeText(LoginActivity.this,otpnew,Toast.LENGTH_LONG).show();

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
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
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

                                        JSONArray det=jsonResponse.getJSONArray("details");
                                        JSONObject usr=det.getJSONObject(0);
                                        String name=usr.getString("cust_username");
                                        //wallet amount
                                        //customer type - 2 rate

                                        session.createLoginSession(password, phone,name);
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        startActivity(intent);
                                        finish();


                                    } else {
                                        // Toast.makeText(LoginActivity.this, "Wrong OTP", Toast.LENGTH_LONG).show();

                                        SuperActivityToast.create(LoginActivity.this, new Style(), Style.TYPE_STANDARD)
                                                //     .setButtonText("Please click BACK again to exit")
                                                //     .setButtonIconResource(R.drawable.ic_undo)
                                                //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                                                //     .setProgressBarColor(Color.WHITE)
                                                .setText("Wrong OTP")
                                                .setDuration(Style.DURATION_LONG)
                                                .setFrame(Style.FRAME_STANDARD)
                                                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                                                .setAnimations(Style.ANIMATIONS_POP).show();

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

            if (isOnline()) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent);
            }

        } else if (view == findViewById(R.id.indoorIMG_iid)) {


        }
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            //  Toast.makeText(HomeActivity.this, "No Internet Connection!", Toast.LENGTH_LONG).show();

            SuperActivityToast.create(LoginActivity.this, new Style(), Style.TYPE_STANDARD)
                    //     .setButtonText("Please click BACK again to exit")
                    //     .setButtonIconResource(R.drawable.ic_undo)
                    //      .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
                    //     .setProgressBarColor(Color.WHITE)
                    .setText("No Internet connection available")
                    .setDuration(Style.DURATION_LONG)
                    .setFrame(Style.FRAME_STANDARD)
                    .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                    .setAnimations(Style.ANIMATIONS_FADE).show();


            return false;
        }
        return true;
    }



}


