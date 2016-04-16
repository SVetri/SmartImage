package com.conware.smimage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Krishnan R on 4/17/2016.
 */
public class ImageUtilities {
    static final String ATTRIBUTE_USED = "UserComment";
    static final String EXTRACT_ERROR = "Error";

    //Save image to SD Card with filename as date and time of image capture
    public static String saveImage(Bitmap bm){
        String root = Environment.getExternalStorageDirectory().toString();
        String baseDirectory = root + "/SmImage";
        File myDir = new File(baseDirectory);
        if (!myDir.isDirectory()){
            myDir.mkdirs();
        }

        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        String fname = Integer.toString(seconds)+".jpg";

        File file = new File (myDir, fname);
        if (file.exists ()){
            file.delete ();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return baseDirectory+"/"+fname;
    }

    //Adds the JSONObject of context data into the JPEG file as an exif tag's data
    public static void setExifData(JSONObject exifData, String filePath){
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);
            exif.setAttribute(ATTRIBUTE_USED, exifData.toString());
            exif.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Extracts context string from image
    public static String getExifData(String filePath){
        ExifInterface exif;
        String res;
        try {
            exif = new ExifInterface(filePath);
            return exif.getAttribute(ATTRIBUTE_USED);
        } catch (IOException e) {
            e.printStackTrace();
            return EXTRACT_ERROR;
        }
    }

    //Adds Image To Android Gallery
    public static void galleryAddPic(String mCurrentPhotoPath, Context con) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        con.sendBroadcast(mediaScanIntent);
    }
}
