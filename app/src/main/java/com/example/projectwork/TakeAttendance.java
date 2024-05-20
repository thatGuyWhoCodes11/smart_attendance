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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
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

public class TakeAttendance extends AppCompatActivity {
    String host = "https://incredibly-one-flamingo.ngrok-free.app";
    ImageView imageV;
    int Image_Capture_Code = 1;
    LinearLayout studentList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_attendance);
        imageV = findViewById(R.id.imageAttendace);
        studentList = findViewById(R.id.studentListAttendance);
    }
    public void handleTakingPicture(View view) {
        Intent cintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cintent,Image_Capture_Code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Image_Capture_Code && resultCode == RESULT_OK){
            Bitmap img=(Bitmap)data.getExtras().get("data");
            recognizeFaces(img);
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "canceled", Toast.LENGTH_SHORT).show();
        }
    }

    private void recognizeFaces(Bitmap img) {
        String url = host.concat("/recognizeFaces");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Bitmap image;
                JSONObject results;
                JSONObject responseJson = null;
                try {
                    responseJson = new JSONObject(response);
                    Bitmap bitmappedImage = convertbase64ToBitmap(responseJson.optString("base64Image"));
                    setAttendanceImage(bitmappedImage,responseJson.optJSONArray("results"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SERVER ERROR", "onErrorResponse: ", error);
            }
        }){
            @Override
            public byte[] getBody() {
                JSONObject body = new JSONObject();
                try {
                    body.put("image",convertBitmapToBase64(img).toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                return body.toString().getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> headers = new HashMap<>();
                headers.put("content-type","application/json");
                headers.put("accept","application/json");
                return headers;
            }
        };
        queue.add(request);
    }

    public void handleTakeAttendance(View view) {
        if(imageV == null){
            Toast.makeText(this, "please upload an image", Toast.LENGTH_LONG).show();
            return;
        }
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
    public void setAttendanceImage(Bitmap image, JSONArray results){
        imageV.setImageBitmap(image);
        imageV.setVisibility(View.VISIBLE);
        LayoutInflater inflater =getLayoutInflater();
        Log.d("TAG", "setAttendanceImage: "+results.toString());
        for(int i = 0;i<results.length();i++){
            LinearLayout components= (LinearLayout)inflater.inflate(R.layout.components,studentList,false);
            components.removeView(components.getChildAt(0));
            LinearLayout studentList_c=(LinearLayout)components.getChildAt(0);
            TextView studentIndex = (TextView) studentList_c.getChildAt(0);
            TextView studentName = (TextView) studentList_c.getChildAt(1);
            studentIndex.setText(String.valueOf(i));
            studentName.setText(results.optJSONObject(i).optString("name"));
            studentList.addView(components);
        }
    }
}
