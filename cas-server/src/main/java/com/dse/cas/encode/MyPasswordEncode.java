package com.dse.cas.encode;

import java.security.MessageDigest;

import org.jasig.cas.authentication.handler.PasswordEncoder;


/**
 * Created by zhoujc on 2016/10/10.
 */
public class MyPasswordEncode implements PasswordEncoder {

    private String inStr;

    private String outStr;

    private MessageDigest md5;

    @Override
    public String encode(String data) {

        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        byte[] byteArray;
        try {
            byteArray = data.getBytes("UTF-8");
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString().toUpperCase();


        /*// TODO Auto-generated method stub
        this.inStr = inStr;
        try {
            this.md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        inStr= getSixteenBitsString();
        return inStr;*/
    }

    /**
     * Computes the MD5 fingerprint of a string.
     *
     * @return the MD5 digest of the input <code>String</code>
     */
    public String compute() {
        char[] charArray = this.inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = this.md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        outStr=hexValue.toString();
        return outStr;
    }

    public String getSixteenBitsString(){
        this.compute();
        return outStr.substring(8,24);
    }

}

