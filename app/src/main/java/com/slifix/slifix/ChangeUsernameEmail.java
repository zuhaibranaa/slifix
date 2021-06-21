package com.slifix.slifix;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.slifix.slifix.app.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeUsernameEmail extends AppCompatActivity {
    Button btnSave;
    EditText name,email;
    TextView phone;
    ImageView backBtn,userProfilePic;
    RequestQueue queue;
    StringRequest req;
    ByteArrayOutputStream byteArrayOutputStream;
    private String upload_URL = "https://slifixfood.herokuapp.com/edit-profile/";
    JSONObject jsonObject;
    RequestQueue rQueue;
    Bitmap bitmap;
    String encodedImage = null;
    private final int GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username_email);
        userProfilePic = findViewById (R.id.uProfile);
        Picasso.get()
                .load(DataManager.getUserImage ())
                .into(userProfilePic);

        btnSave = findViewById(R.id._save_);
        name = findViewById(R.id.shaker_muhammed__);
        email = findViewById(R.id.slifix_gmail_com_);
        phone = findViewById(R.id.numString);
        backBtn = findViewById(R.id.icon_ionic_ios_arrow_back);
        backBtn.setOnClickListener(v -> finish());
        name.setText(DataManager.getUserName());
        phone.setText(DataManager.getPhoneNumber());
        email.setText(DataManager.getUserEmail());
        userProfilePic.setOnClickListener (v -> {
            requestMultiplePermissions();
            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
            galleryIntent.setType ("image/*");
            startActivityForResult(Intent.createChooser (galleryIntent,"Select Profile Picture"),GALLERY);
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText ().toString ().length () >= 15){
                    Toast.makeText (ChangeUsernameEmail.this, "Name Cannot Be More Than 14 Characters", Toast.LENGTH_SHORT).show ();
                }else{
                    DataManager.setUserName(name.getText().toString());
                }
                DataManager.setUserEmail(email.getText().toString());
                DataManager.setPhoneNumber(phone.getText().toString());
                changeUserData();
                finish ();
            }
        });
    }

    private void changeUserData() {
            String url = "https://slifixfood.herokuapp.com/edit-profile/";
            queue = VolleySingleton.getInstance(this).getRequestQueue();
            req = new StringRequest(Request.Method.POST, url, response -> {
                if (Integer.parseInt(response) == 200){
                    Toast.makeText(getApplicationContext (), "Saved", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText (ChangeUsernameEmail.this, response, Toast.LENGTH_SHORT).show ();
                }
            }, error -> Log.e("Error",String.valueOf(error))){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("name", DataManager.getUserName());
                    params.put("phone", DataManager.getPhoneNumber());
                    params.put("email", DataManager.getUserEmail());
                    if (encodedImage != null) {
                        Log.e ("Error",encodedImage);
                        params.put ("image", encodedImage);
                    }
                    return params;
                }
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer "+DataManager.getAuthToken());
                    return params;
                }
            };
            queue.add(req);
    }
    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(

                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener () {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (!(report.areAllPermissionsGranted())) {
                            Toast.makeText(getApplicationContext(), "Permission Not Granted", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(error -> Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    InputStream inputStream = getContentResolver ().openInputStream (contentURI);
                    bitmap = BitmapFactory.decodeStream (inputStream);
                    userProfilePic.setImageBitmap(bitmap);
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    encodedImage = android.util.Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                    changeUserData ();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ChangeUsernameEmail.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

//    private void uploadImage(Bitmap bitmap){
//        Toast.makeText (this, "Upload Image Is Called", Toast.LENGTH_SHORT).show ();
//
//        try {
//            jsonObject = new JSONObject();
//            Toast.makeText (this, encodedImage, Toast.LENGTH_SHORT).show ();
//            jsonObject.put("image", encodedImage);
//            // jsonObject.put("aa", "aa");
//        } catch (JSONException e) {
//            Log.e("JSONObject Here", e.toString());
//        }
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, upload_URL, jsonObject,
//                jsonObject -> {
//                    Log.e("aaaaaaa", jsonObject.toString());
//                    rQueue.getCache().clear();
//                    Toast.makeText(getApplication(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
//                }, volleyError -> Log.e ("aaaaaaa", volleyError.toString ())){
//            @Override
//            public Map<String, String> getHeaders() {
//                Toast.makeText (ChangeUsernameEmail.this, "Get Headers is Called", Toast.LENGTH_SHORT).show ();
//                HashMap<String, String> params = new HashMap<String, String>();
//                params.put("Authorization", "Bearer "+DataManager.getAuthToken());
//                return params;
//            }
//        };
//
//        rQueue = Volley.newRequestQueue(ChangeUsernameEmail.this);
//        rQueue.add(jsonObjectRequest);
//
//    }

}