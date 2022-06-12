package com.example.botany.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.botany.R;
import com.example.botany.utils.RequestUtil;
import com.example.botany.utils.ToastUtils;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends MyBaseActivity implements View.OnClickListener {


    private TextInputLayout user;
    private EditText userid;
    private TextInputLayout psd;
    private EditText pwd;
    private EditText pwdAgain;
    private TextInputLayout grade;
    private EditText gradeEt;
    private Button res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        res.setOnClickListener(this);

    }

    private String getStr(TextInputLayout textInputLayout) {
        if (null == textInputLayout || null == textInputLayout.getEditText()) {
            return "";
        }
        return textInputLayout.getEditText().getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.res:
//                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                        startActivity(intent);
                String id=userid.getText().toString();
                String pwd1=pwd.getText().toString();
                String pwd2=pwdAgain.getText().toString();
                String g=gradeEt.getText().toString();


                if(TextUtils.isEmpty(id)||TextUtils.isEmpty(pwd1)||TextUtils.isEmpty(pwd2)){
                    ToastUtils.showShortToast(getApplicationContext(), "가입 성공");

                    return;
                }
                if(!pwd1.equals(pwd2)){
                    ToastUtils.showShortToast(getApplicationContext(), "两次的密码不一致");

                    return;

                }


                register(id,pwd1,g);








                break;
        }
    }
    public void register(final String s1, String s2, String s3){
        String url = RequestUtil.BASE_URL+"user/regist?username=%s&password=%s&studentno=%s";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(String.format(url,s1,s2,s3))
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
                                ToastUtils.showShortToast(getApplicationContext(), "가입 성공");
                                finish();
                            }
                        });
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Toast.makeText(RegisterActivity.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
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

    private void initView() {
        user = (TextInputLayout) findViewById(R.id.user);
        userid = (EditText) findViewById(R.id.userid);
        psd = (TextInputLayout) findViewById(R.id.psd);
        pwd = (EditText) findViewById(R.id.pwd);
        pwdAgain = (EditText) findViewById(R.id.pwd_again);
        grade = (TextInputLayout) findViewById(R.id.grade);
        gradeEt = (EditText) findViewById(R.id.grade_et);
        res = (Button) findViewById(R.id.res);
    }
}
