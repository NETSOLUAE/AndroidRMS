package rmsllcoman.com.Util;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by macmini on 6/13/17.
 */

public class Encryption {

    public static String toAscii(Context context) {
        String s = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        StringBuilder sb = new StringBuilder();
        String ascString = null;
        for (int i = 0; i < s.length(); i++) {
            sb.append((int) s.charAt(i));
        }
        ascString = sb.toString();
        if (ascString.length() > 16) {
            ascString = ascString.substring(0, 16);
        } else if (ascString.length() < 16) {

            for (int j = 0; j < ascString.length(); j++) {
                ascString = ascString + "0";
            }

        }
        return ascString;
    }
}
