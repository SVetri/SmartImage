package com.conware.smimage;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by S on 4/13/2016.
 */
public class TemplateFormat {

    String template_name;
    String ext;
    List<AttributeList> attribute_list;
    Context cont;

    TemplateFormat(Context c)
    {
        template_name = new String();
        ext = new String();
        attribute_list = new ArrayList<AttributeList>();
        cont = c;
    }

    public String getTemplate_name() {
        return template_name;
    }

    public void setTemplate_name(String template_name) {
        this.template_name = template_name;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public List<AttributeList> getAttribute_list() {
        return attribute_list;
    }

    public void setAttribute_list(List<AttributeList> attribute_list) {
        this.attribute_list = attribute_list;
    }

    public JSONObject convertJsonStore(){

        JSONObject parent = new JSONObject();
        JSONArray attr_list = new JSONArray();
        try {

                for(int i = 0; i<attribute_list.size(); i++)
                {
                    JSONObject temp = new JSONObject();
                    temp.put("attr_name", attribute_list.get(i).getAttr_name());
                    temp.put("attr_type", attribute_list.get(i).getAttr_type());
                    attr_list.put(temp);
                }
                parent.put("template_name", template_name);
                parent.put("extends", ext);
                parent.put("attribute_list", attr_list);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String curTemplates = Prefs.getMyStringPref(cont, "Templates");
        try {
            JSONArray arr = new JSONArray(curTemplates);
            Log.d("output", arr.toString());
            //curTemplates = curTemplates + parent.toString();
            arr.put(parent);
            Prefs.setMyStringPref(cont, "Templates", arr.toString());
            Log.d("outputnew", arr.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parent;

    }

}

class AttributeList{

    String attr_name;
    String attr_type;

    public String getAttr_name() {
        return attr_name;
    }

    public void setAttr_name(String attr_name) {
        this.attr_name = attr_name;
    }

    public String getAttr_type() {
        return attr_type;
    }

    public void setAttr_type(String attr_type) {
        this.attr_type = attr_type;
    }

}
