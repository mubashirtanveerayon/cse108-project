package utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HelperFunctions {

    public static String sha256(String text){
        return sha256(text.getBytes());
    }

    public static String sha256(byte[] data){


        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] digest = messageDigest.digest(data);


        BigInteger bigInt = new BigInteger(1,digest);
        String hashtext = bigInt.toString(16);
        while(hashtext.length() < 32 ){
            hashtext = "0"+hashtext;
        }

        return hashtext ;
    }

    public static float getValue(String str){
        if(str==null || str.isBlank())return -1;
        try{
            return Float.parseFloat(str);
        }catch(Exception e){
            return -1;
        }
    }
}
