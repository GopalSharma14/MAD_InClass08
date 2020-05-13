package com.example.inclass08;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SignUp extends AppCompatActivity {

    private Button signUp2_btn;
    private Button cancel_btn;
    private EditText fname_ed;
    private EditText lname_ed;
    private EditText emailSignUp_ed;
    private EditText passwordSignup1_ed;
    private EditText passwordSignup2_ed;
    String token=null;
    User newUser = new User();
    ArrayList<User> globalUserList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
       /* if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }*/

        setTitle("Sign Up");
        signUp2_btn=findViewById(R.id.signUp2_btn);
        cancel_btn=findViewById(R.id.cancel_btn);
        fname_ed=findViewById(R.id.fname_ed);
        lname_ed=findViewById(R.id.lname_ed);
        emailSignUp_ed=findViewById(R.id.emailSignUp_ed);
        passwordSignup1_ed=findViewById(R.id.passwordSignup1_ed);
        passwordSignup2_ed=findViewById(R.id.passwordSignup2_ed);

        signUp2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final OkHttpClient client = new OkHttpClient();
                String fname=fname_ed.getText().toString();
                String lname=lname_ed.getText().toString();
                String email=emailSignUp_ed.getText().toString();
                String password1=passwordSignup1_ed.getText().toString();
                String password2=passwordSignup2_ed.getText().toString();

                if(fname.isEmpty()||lname.isEmpty()||email.isEmpty()||password1.isEmpty()||password2.isEmpty()){
                    Toast.makeText(SignUp.this, "Enter all details!!", Toast.LENGTH_SHORT).show();
                }
               /* if(!password1.equals(password2)){
                    Toast.makeText(SignUp.this, "Password does not match!", Toast.LENGTH_SHORT).show();
                }*/

                RequestBody formBody = new FormBody.Builder()
                        .add("email",email)
                        .add("password",password1)
                        .add("fname",fname)
                        .add("lname",lname)
                        .build();
                Request request = new Request.Builder()
                        .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/signup")
                        .post(formBody)
                        //.header("Authorization","BEARER "+getResources().getString(R.string.apiKey))
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

JSONObject root=new JSONObject(responseBody.string());
                            newUser.status = root.getString("status");
                            newUser.token = root.getString("token");
                            newUser.user_id = root.getString("user_id");
                            newUser.user_email = root.getString("user_email");
                            newUser.user_fname = root.getString("user_fname");
                            newUser.user_lname = root.getString("user_lname");
                            newUser.user_role = root.getString("user_role");
                            globalUserList.add(newUser);
                            Log.d("globalUserList", globalUserList.toString());
                            Log.d("token in signup Activity", newUser.token);

                            Gson gson = new Gson();
                            String userInfoListJsonString = gson.toJson(newUser);
                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sharedPreferences", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("UserDetails", userInfoListJsonString);
                            editor.commit();

                            Intent emailIntent = new Intent(SignUp.this, InboxActivity.class);
                            startActivity(emailIntent);
                            finish();

                        } /*catch (JSONException e) {
                            e.printStackTrace();
                        }*/ catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
