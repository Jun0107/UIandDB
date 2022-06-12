package com.example.botany.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.botany.R;
import com.example.botany.utils.RequestUtil;
import com.example.botany.utils.ToastUtils;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LoginActivity extends MyBaseActivity implements View.OnClickListener {
    private TextInputLayout nameTIL;
    private TextInputLayout passTIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nameTIL = findViewById(R.id.name);
        passTIL = findViewById(R.id.pass);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.zhuce).setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                String usernameStr = Objects.requireNonNull(nameTIL.getEditText()).getText().toString().trim();
                String passwordStr = Objects.requireNonNull(passTIL.getEditText()).getText().toString().trim();

                if (TextUtils.isEmpty(usernameStr) || TextUtils.isEmpty(passwordStr)) {
                    ToastUtils.showShortToast(LoginActivity.this, "계정 또는 비밀번호는 비워둘 수 없습니다");
                    return;
                }
                login(usernameStr,passwordStr);
//                ToastUtils.showShortToast(LoginActivity.this, "성공");
//
//
//
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                break;
            case R.id.zhuce:
                Intent intent1 = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }





    public void login(final String s1, final String s2){
        String url = RequestUtil.BASE_URL+"user/login?username=%s&password=%s";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(String.format(url,s1,s2))
                .build();
        final Call call = okHttpClient.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    String string = response.body().string();
                    final JSONObject jsonObject=new JSONObject(string);
                    if(jsonObject.getBoolean("success")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                                ToastUtils.showShortToast(LoginActivity.this, "성공");
                                          startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();
                            }
                        });
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Toast.makeText(LoginActivity.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }
}
