package com.conware.smimage;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by S on 4/13/2016.
 */
public class Prefs {

    private static boolean isInitialised = false;

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences("myprefs", 0);
    }

    public static String getMyStringPref(Context context, String name) {
        if(isInitialised == false)
        {
            JSONArray templatear = new JSONArray();
            JSONObject temp = new JSONObject();
            JSONArray tempar = new JSONArray();
            try {
                    JSONObject t = new JSONObject();
                    t.put("attr_name", "latitude");
                    t.put("attr_type", "String");
                    tempar.put(t);
                    JSONObject t1 = new JSONObject();
                    t1.put("attr_name", "longitude");
                    t1.put("attr_type", "String");
                    tempar.put(t1);
                    JSONObject t2 = new JSONObject();
                    t2.put("attr_name", "date_time");
                    t2.put("attr_type", "String");
                    tempar.put(t2);

                    temp.put("template_name", "Image");
                    temp.put("extends", "None");
                    temp.put("attribute_list", tempar);
                    templatear.put(temp);
            } catch (JSONException e) {
                e.printStackTrace();
            };
            return(templatear.toString());
        }
        else
        {
            return getPrefs(context).getString(name, "default");
        }
    }

    public static void setMyStringPref(Context context,String name, String value) {
        // perform validation etc..
        getPrefs(context).edit().putString(name, value).commit();
    }

    public static void deletePref(Context context)
    {
        getPrefs(context).edit().clear().commit();
    }

    public static void delPref(Context context, String s)
    {
        getPrefs(context).edit().remove(s).commit();
    }

}
