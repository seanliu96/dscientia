package com.xyz.lehuo.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xyz.lehuo.R;
import com.xyz.lehuo.bean.User;
import com.xyz.lehuo.global.BaseActivity;
import com.xyz.lehuo.global.Conf;
import com.xyz.lehuo.global.Constant;
import com.xyz.lehuo.global.MyApplication;
import com.xyz.lehuo.util.HttpUtil;
import com.xyz.lehuo.util.SPUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by xyz on 15/12/24.
 */
public class LoginActivity extends BaseActivity {

    public static final int LOGIN_SUCCESS = 2;

    ImageView back;
    TextView register;
    EditText username;
    EditText password;
    Button login;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        register = (TextView) findViewById(R.id.register);
        username = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.user_pwd);
        login = (Button) findViewById(R.id.login);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("") || password.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "信息填写不完整", Toast.LENGTH_SHORT).show();
                    return;
                }
                Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
                if (!pattern.matcher(username.getText().toString().trim()).matches()) {
                    Toast.makeText(LoginActivity.this, "用户名只能是英文字母或数字", Toast.LENGTH_SHORT);
                    return;
                }
                if (!pattern.matcher(password.getText().toString().trim()).matches()) {
                    Toast.makeText(LoginActivity.this, "密码只能是英文字母或数字", Toast.LENGTH_SHORT);
                    return;
                }
                String name = username.getText().toString().trim();
                String pwd = password.getText().toString().trim();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("pwd", pwd));
                new HttpUtil().create(HttpUtil.POST, Constant.LOGIN_API, params, new HttpUtil.HttpCallBallListener() {
                    @Override
                    public void onStart() {
                        progressDialog = ProgressDialog.show(LoginActivity.this, "提示", "登录中，请稍等");
                    }

                    @Override
                    public void onFinish() {
                        progressDialog.cancel();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(LoginActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                Conf.isLogin = true;
                                JSONObject object = jsonObject.getJSONObject("data");
                                User user = new User();
                                user.setName(object.getString("name"));
                                /*user.setMajor(Constant.majors[Integer.parseInt(object.getString("major"))]);
                                user.setGrade(Constant.grades[Integer.parseInt(object.getString("grade"))]);
                                user.setSex(object.getString("sex").equals("1") ? "男" : "女");*/
                                user.setUid(object.getString("_id").substring(9, object.getString("_id").length() - 2));
                                user.setAvatar(object.getString("avatar_url"));
                                JSONArray array = object.getJSONArray("notebooks");

                                for (int i = 0; i < array.length(); i++) {
                                    user.addCollection(array.getString(i));
                                }

                                /*array = object.getJSONArray("like");
                                for (int i = 0; i < array.length(); i++) {
                                    user.addFocus(array.getString(i));
                                }*/

                                User.save(LoginActivity.this, user);
                                ((MyApplication)getApplication()).setUser(user);
                                SPUtil.put(LoginActivity.this, "isLogin", Conf.isLogin);
                                setResult(LOGIN_SUCCESS);
                                LoginActivity.this.finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "用户名或密码出错", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }
}
