package com.example.projectwork;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterStudents extends AppCompatActivity {
    String host = "https://incredibly-one-flamingo.ngrok-free.app";
    private ImageView imageV;
    private static final int Image_Capture_Code = 1;
    private LinearLayout studentList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_students);
        imageV =findViewById(R.id.image);
        studentList = findViewById(R.id.studentList);
    }

    public void handleRegisterCap(View view) {
        Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cInt,Image_Capture_Code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Image_Capture_Code && resultCode == RESULT_OK){
            Bitmap img=(Bitmap)data.getExtras().get("data");
            getDetectedFaces(img);
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "canceled", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDetectedFaces(Bitmap img) {
        String url = host.concat("/registerFaces");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject responseJson = new JSONObject(response);
                String base64Image=responseJson.optString("base64Image");
                JSONArray faceIds=(JSONArray) responseJson.opt("faceIds");
                if(faceIds == null){
                    Toast.makeText(RegisterStudents.this, "no faces detected", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bitmap bitMappedImage=convertbase64ToBitmap(base64Image);
                setDetectedFaces(bitMappedImage,faceIds);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            Log.println(Log.ERROR,"SERVER ERROR",error.toString());
            Toast.makeText(this, "Fail to get response..", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> headers = new HashMap<>();
                headers.put("Content-Type","application/json");
                headers.put("Accept","application/json");
                return headers;
            }
            @Override
            public byte[] getBody() {
                JSONObject data = new JSONObject();
                try {
                    data.put("image",convertBitmapToBase64(img));
                    return data.toString().getBytes(StandardCharsets.UTF_8);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        queue.add(request);
    }
    public String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream); // Change format if needed (JPEG, etc.)
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    public Bitmap convertbase64ToBitmap(String base64S){
        byte[] byteArray=Base64.decode(base64S,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
    }
    public void setDetectedFaces(Bitmap image, JSONArray results) throws JSONException {
        imageV.setVisibility(View.VISIBLE);
        imageV.setImageBitmap(image);
        LayoutInflater inflater =getLayoutInflater();
        for(int i = 0;i<results.length();i++){
            LinearLayout studentView=(LinearLayout) inflater.inflate(R.layout.components,studentList,false);
            studentView.removeView(studentView.getChildAt(1));
            studentView.setTag(results.get(i));
            ((TextView)studentView.findViewById(R.id.studentNumber)).setText(String.valueOf(i));
            studentList.addView(studentView);
        }
    }
    private void debug(){

    }
    public void sendFaces(View view) throws JSONException {
        if(imageV == null){
            Toast.makeText(this, "please upload an image", Toast.LENGTH_LONG).show();
            return;
        }
        //TO DO student id needs to be saved in database
        String url = host.concat("/registerFaces");
        JSONArray students = new JSONArray();
        JSONObject student = new JSONObject();
        String studentID;
        String studentName;
        String faceID;
        LinearLayout studentView=(LinearLayout)studentList.getChildAt(0);
        for(int i=0;i<studentList.getChildCount();i++){
            studentName = ((EditText)((LinearLayout)((LinearLayout)((LinearLayout)studentView.getChildAt(0))).getChildAt(1)).getChildAt(1)).getText().toString();
            faceID = studentView.getTag().toString();
            student.put("id",faceID);
            student.put("name",studentName);
            students.put(student);
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    if(!Objects.isNull(responseJson.optString("message"))){
                        Toast.makeText(RegisterStudents.this, "success!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RegisterStudents.this, "not saved", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.println(Log.ERROR,"SERVERERROR",error.toString());
            }
        }) {
            @Override
            public byte[] getBody() {
                JSONObject body = new JSONObject();
                try {
                    body.put("faceInfos", students);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                return body.toString().getBytes(StandardCharsets.UTF_8);
            }
            @Override
            public Map<String, String> getHeaders(){
                Map<String,String> Headers = new HashMap<>();
                Headers.put("content-type","application/json");
                Headers.put("accept","application/json");
                return Headers;
            }
        };
        queue.add(request);
    }
}