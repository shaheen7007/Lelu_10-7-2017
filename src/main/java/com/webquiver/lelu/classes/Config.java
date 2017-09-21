package com.webquiver.lelu.classes;

/**
 * Created by WebQuiver 04 on 7/13/2017.
 */


import org.json.JSONArray;

/**
 * Created by Belal on 11/18/2015.
 */
public class Config {


    //BASE URL
    public static final String BASE_URL = "http://192.168.1.7:8000/";
    public static final String BASE_URLwithoutslash = "http://192.168.1.7:8000";




    //URLs to register.php and confirm.php file
    public static final String REGISTER_URL = "http://lelu.webquiver.in/customer_signup";
    public static final String LOGIN_URL = "http://lelu.webquiver.in/customer_login";
    public static final String CONFIRM_URL = "http://lelu.webquiver.in/verify_otp";
    public static final String FORGOT_URL = "http://lelu.webquiver.in/forget_pass";
    public static final String FORGOT2_URL = "http://lelu.webquiver.in/otp_forget_pass";




    //urls ADDRESS

    public static final String ADDR_SEND_URL = BASE_URL+"cust_addr";
    public static final String ADDR_GET_URL = BASE_URL+"get_cust_addr";
    public static final String ADDR_EDIT_URL = BASE_URL+"up_cust_addr";


    //urls CART
    public static final String CART_GET_URL = BASE_URL+"get_cart";
    public static final String CART_ADD_URL = BASE_URL+"add_cart";



    //urls ORDER
    public static final String ODR_PLACE_URL = BASE_URL+"place_order";
    public static final String ODR_GET_URL = BASE_URL+"get_place_order";
    public static final String ODR_REVIEW_URL = BASE_URL+"order_rating"; //same
    public static final String ODR_RATING_URL = BASE_URL+"order_rating"; //same




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






    //keys of order

    public static final String KEY_ORDER_ID = "order_place_id";
    public static final String KEY_ORDER_REVIEW = "op_feedback";
    public static final String KEY_ORDER_RATING = "op_rating";


    //Shared Prefference
    public static final String JSONSTRING="jsonstring";
    public static final String JSONSTRING1="jsonstring1";


    //keys of Number shared pref
    public static final String KEY_NUM_CART = "num_cart";



    public static final String PICODE_URL = "http://postalpincode.in/api/pincode/";


    //wish list urls
    public static final String WISHLIST_ADD_URL = BASE_URL+"add_wish_list";
    public static final String WISHLIST_GET_URL = BASE_URL+"get_wish_list";
    public static final String WISHLIST_REMOVE_URL = BASE_URL+"del_wish_list";




    //search history pref

    public static final String SearchPref= "SearchHistory";
    public static final String first_suggestion= "first";
    public static final String second_suggestion= "second";
    public static final String third_suggestion= "third";
    public static final String forth_suggestion= "fourth";
    public static final String fifth_suggestion= "fifth";
    public static final String numofhistory= "numofhistory";


    public static final String SearchJsonString="searchjsonstring";



    //url item

    public static final String SINGLE_PROD_GET_URL=BASE_URL+"get_single_inventory/";
    public static final String SINGLE_PROD_Id = "inv_id";


    //urls search
    public static final String SEARCH_RESULTS_URL = BASE_URL+"search_all_product/";
    public static final String SEARCH_HISTORY_URL = BASE_URL+"search_product/";


   // products list urls
   public static final String TOP_SELLING_URL=BASE_URL+"get-inventory/";



    public static final String KEY_SEARCH_TERM="term";
    public static final String KEY_FILTER_RATE1="rate1";
    public static final String KEY_FILTER_RATE2="rate2";




    //urls filter
    public static final String SEARCH_FILTER_URL=BASE_URL+"filter_by_price/";

    public static final String SEARCH_SORT_URL=BASE_URL+"order_by_price/";

    public static final String GET_CATEG_URL=BASE_URL+"get_category/";




}


