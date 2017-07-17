package com.webquiver.lelu.classes;

/**
 * Created by WebQuiver 04 on 7/13/2017.
 */


/**
 * Created by Belal on 11/18/2015.
 */
public class Config {
    //URLs to register.php and confirm.php file
    public static final String REGISTER_URL = "http://192.168.1.5:1111/customer_signup";
    public static final String LOGIN_URL = "http://192.168.1.5:1111/customer_login";
    public static final String CONFIRM_URL = "http://192.168.1.5:1111/verify_otp";
    public static final String FORGOT_URL = "http://192.168.1.5:1111/forget_pass";
    public static final String FORGOT2_URL = "http://192.168.1.5:1111/otp_forget_pass";

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


}