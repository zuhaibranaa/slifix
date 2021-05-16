package com.slifix.slifix.login;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.slifix.slifix.R;
import com.slifix.slifix.app.VolleySingleton;
import com.slifix.slifix.DataManager;
import com.slifix.slifix.dashboard;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class SetName extends AppCompatActivity {
TextView no;
EditText name;
CheckBox agree;
Spinner gender;
ImageButton btn;
JSONObject res;
RequestQueue queue;
StringRequest req;
int status;
String gndr,nme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name);
        no = (TextView) findViewById(R.id.no);
        name = (EditText) findViewById(R.id.name);
        agree = (CheckBox) findViewById(R.id.AcceptTerms);
        gender = (Spinner) findViewById(R.id.genderSpinnner);
        btn = (ImageButton) findViewById(R.id.btnsetname);
        no.setText("+"+ DataManager.getPhoneNumber());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (agree.isChecked()){
                    nme = name.getText().toString();
                    gndr =  gender.getSelectedItem().toString();
                    setName();
                }else{
                    Toast.makeText(SetName.this, "Please Agree To Our Terms Before Login", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setName() {
            String url = "https://slifixfood.herokuapp.com/Set-Name/";
            queue = VolleySingleton.getInstance(this).getRequestQueue();
            req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String jsonString =response ;
                    try {
                        res = new JSONObject(jsonString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }try {
                        status = res.getInt("status");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (status == 200){
                        Toast.makeText(getApplicationContext(), "Data Saved", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), dashboard.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("phone","+"+DataManager.getPhoneNumber());
                    params.put("name",nme);
                    params.put("gender",gndr);
                    params.put("role_id","1");
                    return params;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer " + DataManager.getAuthToken());
                    return params;
                }
            };
            queue.add(req);
    }
}