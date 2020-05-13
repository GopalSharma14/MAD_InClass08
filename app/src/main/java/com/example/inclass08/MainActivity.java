package com.example.inclass08;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private Button signup_btn;
    private EditText email_ed;
    private EditText password_ed;
    private Button login_btn;
    User user=new User();
    String email=null;
    String password=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Mailer");
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        String userInfoListJsonString = sharedPreferences.getString("UserDetails", "");
        Log.d("email&pass", userInfoListJsonString);
        if (!userInfoListJsonString.equals("")) {
            Intent emailIntent = new Intent(MainActivity.this, InboxActivity.class);
            startActivity(emailIntent);
            finish();
        } else {

            signup_btn = findViewById(R.id.signup_btn);
            email_ed = findViewById(R.id.email_ed);
            password_ed = findViewById(R.id.password_ed);
            login_btn = findViewById(R.id.login_btn);

            //String email="gsharma@abb.com";
            //String password="123456";
       /* final OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("email","gsharma@abb.com")
                .add("password","123456")
                .build();
        Request request = new Request.Builder()
                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/login")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        Log.d("demo1", "onResponse: "+responseHeaders.name(i)+" "+responseHeaders.value(i));
                    }
                    Log.d("demo2", "onResponse: "+responseBody.string());
                }
            }
        });*/
       /* Request request1 = new Request.Builder()
                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/signup")
                .header("Authorization","BEARER "+getResources().getString(R.string.apiKey))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        Log.d("demo3", "onResponse: "+responseHeaders.name(i)+" "+responseHeaders.value(i));
                    }
                    Log.d("demo4", "onResponse: "+responseBody.string());
                }
            }
        });*/

            login_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    email = email_ed.getText().toString();
                    password = password_ed.getText().toString();
                    final OkHttpClient client = new OkHttpClient();
                    RequestBody formBody = new FormBody.Builder()
                            .add("email", email)
                            .add("password", password)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/login")
                            .post(formBody)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try (ResponseBody responseBody = response.body()) {
                                if (!response.isSuccessful())
                                    throw new IOException("Unexpected code " + response);

                                JSONObject root = new JSONObject(response.body().string());
                                user.status = root.getString("status");
                                user.token = root.getString("token");
                                user.user_id = root.getString("user_id");
                                user.user_email = root.getString("user_email");
                                user.user_fname = root.getString("user_fname");
                                user.user_lname = root.getString("user_lname");
                                user.user_role = root.getString("user_role");
                                Log.d("user", user.toString());
                                Log.d("token", user.token);

                                Gson gson = new Gson();
                                String userInfoListJsonString = gson.toJson(user);
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sharedPreferences", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("UserDetails", userInfoListJsonString);
                                editor.commit();

                                Intent emailIntent = new Intent(MainActivity.this, InboxActivity.class);
                                startActivity(emailIntent);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });

            signup_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent sendData = new Intent(MainActivity.this, SignUp.class);
                    sendData.putExtra("myKey", (boolean[]) null);
                    startActivity(sendData);
                }
            });

        }
    }
}

