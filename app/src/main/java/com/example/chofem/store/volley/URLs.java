package com.example.chofem.store.volley;

public class URLs {
    private static final String ROOT_URL = "https://chofem.000webhostapp.com/api/store/";
    //auth
    public static final String URL_REGISTER = ROOT_URL + "register.php";
    public static final String URL_LOGIN= ROOT_URL + "login.php";
    //otp
    public static final String URL_VERIFY_OTP= ROOT_URL + "verify_otp.php";
    public static final String URL_SEND_OTP= ROOT_URL + "send_otp.php";
    //profile
    public static final String URL_ADD_LOCATION= ROOT_URL + "add_location.php";
    public static final String URL_UPDATE_PROFILE= ROOT_URL + "update_info.php";
    public static final String URL_UPDATE_PROFILE_PICTURE= ROOT_URL + "update_profile.php";
    public static final String URL_UPDATE_PASSWORD= ROOT_URL + "update_password.php";

    //product
    public static final String URL_ADD_PRODUCT= ROOT_URL + "add_product.php";
    public static final String URL_UPDATE_PRODUCT= ROOT_URL + "update_product.php";
    public static final String URL_UPDATE_PRODUCT_IMAGE= ROOT_URL + "update_images.php";
    public static final String URL_DELETE_PRODUCT= ROOT_URL + "delete_product.php";


}
