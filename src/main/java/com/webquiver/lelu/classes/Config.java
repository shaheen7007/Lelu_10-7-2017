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




    //urls ADDRESS

    public static final String ADDR_SEND_URL = "http://192.168.1.5:1111/cust_addr";
    public static final String ADDR_GET_URL = "http://192.168.1.5:1111/get_cust_addr";
    public static final String ADDR_EDIT_URL = "http://192.168.1.5:1111/up_cust_addr";


    //urls CART
    public static final String CART_GET_URL = "http://192.168.1.5:1111/get_cart";
    public static final String CART_ADD_URL = "http://192.168.1.5:1111/add_cart";








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




    //keys of address
    public static final String KEY_ADDR_MOBILE = "mobile";
    public static final String KEY_ADDR_HOUSE = "house";
    public static final String KEY_ADDR_PIN = "pin";
    public static final String KEY_ADDR_STREET = "street";
    public static final String KEY_ADDR_NAME = "name";
    public static final String KEY_ADDR_DISTRICT = "dist";
    public static final String KEY_ADDR_STATE = "state";
    public static final String KEY_ADDR_PHONE = "phone";
    public static final String KEY_CA_ID = "ca_id";





    //keys of cart
    public static final String KEY_CART_ProdId = "prod_code";
    public static final String KEY_CART_ProdQty = "qty";














    //Shared Prefference
    public static final String JSONSTRING="jsonstring";
    public static final String JSONSTRING1="jsonstring1";


}


