package com.conware.smimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by Krishnan R on 4/17/2016.
 */
public class Base64Utilities {

    //Convert Bitmap to Base64 String
    public static String bitmapToBase64(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] byteArrayImage = baos.toByteArray();
        return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
    }

    //Convert JSONObject to Base64String
    public static String jsonToBase64(JSONObject jo){
        String jsonString = jo.toString();
        return Base64.encodeToString(jsonString.getBytes(), Base64.DEFAULT);
    }

    //Append two Base64 Strings
    public static String appendBase64Stings(String str1, String str2){
        return str1+str2;
    }

    //Convert Base64 to Bitmap
    public static Bitmap convertBase64toBitmap(String encodedImage){
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

}
