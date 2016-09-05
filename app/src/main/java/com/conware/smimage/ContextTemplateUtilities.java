package com.conware.smimage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Krishnan R on 4/17/2016.
 */
public class ContextTemplateUtilities {

    final static String SHARED_PREF_KEY = "Templates";
    final static String DEFAULT_PREF_VALUE = "Error";
    final static String ATTR_LIST_TAG = "attribute_list";

    public static JSONArray getContextDataFromSharedPrefs(Activity act){
        SharedPreferences sharedPref = act.getPreferences(Context.MODE_PRIVATE);
        try {
            return new JSONArray(sharedPref.getString(SHARED_PREF_KEY, DEFAULT_PREF_VALUE));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Pair> getAttributeList(String tName, JSONArray contextTemplates){

        ArrayList<String> attrNames = new ArrayList<String>();
        ArrayList<String> attrTypes = new ArrayList<String>();

        //String contextTemplatesString = getContextDataFromSharedPrefs(act);
        try {
            //JSONArray contextTemplates = new JSONArray(contextTemplatesString);
            JSONObject matchedContextTemplate = getMatchedContextTemplate(tName, contextTemplates);
            JSONArray attrList = matchedContextTemplate.getJSONArray(ATTR_LIST_TAG);
            for (int i=0;i<attrList.length();i++){
                attrNames.add(attrList.getJSONObject(i).getString("attr_name"));
                attrTypes.add(attrList.getJSONObject(i).getString("attr_type"));
            }

            //From Ancestors
            if(!matchedContextTemplate.getString("extends").equals("none")){
                ArrayList<Pair> parentData = getAttributeList(matchedContextTemplate.getString("extends"),contextTemplates);
                for (int i=0;i<parentData.size();i++){
                    attrNames.add(parentData.get(i).getLeft());
                    attrTypes.add(parentData.get(i).getRight());
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Set Pairs
        ArrayList<Pair> result = new ArrayList<Pair>();
        for (int i=0;i<attrNames.size();i++){
            Pair temp = new Pair(attrNames.get(i),attrTypes.get(i));
            result.add(temp);
        }

        return result;
    }

    private static JSONObject getMatchedContextTemplate(String tName, JSONArray contextTemplates) {
        for (int i=0; i<contextTemplates.length(); i++){
            try {
                if (contextTemplates.getJSONObject(i).getString("template_name").equals(tName))
                    return contextTemplates.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
