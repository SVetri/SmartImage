package com.conware.smimage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private static final int CAMERA_REQUEST = 1888;
    Button captureImage;
    ImageView capturedImage;
    TemplateFormat tf;
    String fname;
    String i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tf = new TemplateFormat(MainActivity.this);
        captureImage = (Button) this.findViewById(R.id.captureImage);
        capturedImage = (ImageView) this.findViewById(R.id.capturedImage);

        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImageFromCamera();
            }
        });

    }

    public void captureImageFromCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            i = Base64Utilities.bitmapToBase64(mphoto);
            capturedImage.setImageBitmap(mphoto);
            fname = ImageUtilities.saveImage(mphoto, getApplicationContext());
            ImageUtilities.galleryAddPic(fname, MainActivity.this);
            create_dialog();
        }
    }

    public ArrayList<String> captureImplicitContext(){
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        ArrayList<String> res = new ArrayList<String>();
        res.add(String.valueOf(latitude));
        res.add(String.valueOf(longitude));
        res.add(currentDateTimeString);
        return res;
    }

    public void create_dialog()
    {
        // custom dialog
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.setTitle("New Template");

        Button dialogButton = (Button) dialog.findViewById(R.id.confirm_button);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // set the custom dialog components - text and button
                EditText template_name = (EditText) dialog.findViewById(R.id.template_name);
                EditText template_extends = (EditText) dialog.findViewById(R.id.template_extends);
                EditText template_attribute_name = (EditText) dialog.findViewById(R.id.template_attribute_name);
                EditText template_attribute_type = (EditText) dialog.findViewById(R.id.template_attribute_type);

                AttributeList af = new AttributeList();
                List<AttributeList> al = new ArrayList<AttributeList>();


                af.setAttr_name(template_attribute_name.getText().toString());
                af.setAttr_type(template_attribute_type.getText().toString());
                al.add(af);

                tf.setTemplate_name(template_name.getText().toString());
                tf.setExt(template_extends.getText().toString());
                tf.setAttribute_list(al);
                JSONObject obj = tf.convertJsonStore();
                //String j = Base64Utilities.jsonToBase64(obj);
                //String appendedstr = Base64Utilities.appendBase64Stings(i,j);
                //capturedImage.setImageBitmap(Base64Utilities.convertBase64toBitmap(i));

                ImageUtilities.setExifData(obj,fname);
                String readexif = ImageUtilities.getExifData(fname);
                Log.d("output", readexif);
                dialog.dismiss();
            }
        });

        dialog.show();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
