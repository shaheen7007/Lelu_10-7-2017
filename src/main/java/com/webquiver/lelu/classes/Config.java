package com.webquiver.lelu.classes;

/**
 * Created by WebQuiver 04 on 7/13/2017.
 */


import org.json.JSONArray;

/**
 * Created by Belal on 11/18/2015.
 */
public class Config {
    //URLs to register.php and confirm.php file
    public static final String REGISTER_URL = "http://lelu.webquiver.in/customer_signup";
    public static final String LOGIN_URL = "http://lelu.webquiver.in/customer_login";
    public static final String CONFIRM_URL = "http://lelu.webquiver.in/verify_otp";
    public static final String FORGOT_URL = "http://lelu.webquiver.in/forget_pass";
    public static final String FORGOT2_URL = "http://lelu.webquiver.in/otp_forget_pass";

    //Keys to send username, password, phone and otp
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PHONE = "mobile";
    public static final String KEY_COMPANY = "company";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PLACE = "place";
    public static final String KEY_OTP = "otp";

    //JSON Tag from response from server
    public static final String TAG_RESPONSE= "message";





    //Shared Prefference
    public static final String JSONSTRING="jsonstring";


}