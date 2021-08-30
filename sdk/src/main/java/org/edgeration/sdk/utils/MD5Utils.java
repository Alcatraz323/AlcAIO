package org.edgeration.sdk.utils;

import java.security.MessageDigest;

public class MD5Utils {
    public static String digest(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                int c = b & 0xff;
                String result = Integer.toHexString(c);
                if(result.length()<2){
                    sb.append(0);
                }
                sb.append(result);
            }
            return sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
}