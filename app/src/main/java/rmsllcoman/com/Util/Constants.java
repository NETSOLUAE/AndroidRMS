package rmsllcoman.com.Util;

import android.util.Log;

/**
 * Created by macmini on 6/13/17.
 */

public class Constants {
    public static final String LOG_RMS = "rms_QA_";
    public static final String BASE_URL = "http://netsolintl.net/rmsllc/api.php";
    public static final String BASE_URL_SALARY = "http://netsolintl.net/salarydeduction/api.php";
    public static final String BASE_URL_LINES = "http://netsolintl.net/rmslines/api.php";

    public static String deviceID = "";

    public static boolean isValidMobile(String phone) {
        boolean check=false;
//        if (phone.contains("968") || phone.contains("971") ) {
//            number = phone.substring(3);
//        }
        String countryCode = phone.substring(0,3);
        Log.d("++++COUNTRY_CODE++++", countryCode);
        Log.d("++++LENGTH++++", String.valueOf(phone.length()));
        if (countryCode.equalsIgnoreCase("968") || countryCode.equalsIgnoreCase("971")) {
//            number = number.substring(1);
            int length = phone.length();
            if(length < 11) {
                check = false;
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }
}
