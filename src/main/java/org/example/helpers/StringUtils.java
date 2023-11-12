package org.example.helpers;

public class StringUtils {

    public static String customTrim(String str, String replace) {
        String forRet = str;
        if(forRet.indexOf(replace) == 0) {
            forRet = forRet.substring(1);
        }
        if(forRet.lastIndexOf(replace) == forRet.length()-1) {
            forRet = forRet.substring(0,forRet.length()-1);
        }
        return forRet;
    }
}
