package com.xyz.lehuo.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.xyz.lehuo.R;
import com.xyz.lehuo.global.BaseActivity;
import com.xyz.lehuo.global.Constant;
import com.xyz.lehuo.util.HttpUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by xyz on 15/12/24.
 */
public class RegisterActivity extends BaseActivity {

    ImageView back;
    EditText username;
    EditText pwd;
    EditText pwdAgain;
    Button register;
    /*RadioGroup group;
    Spinner major;

    Spinner grade;*/

    String name = "";
    String password = "";
    /*String sex = "";
    String majorString = Constant.majors[0];
    String gradeString = Constant.grades[0];*/

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        username = (EditText) findViewById(R.id.user_name);
        pwd = (EditText) findViewById(R.id.user_pwd);
        pwdAgain = (EditText) findViewById(R.id.user_pwd_again);
        /*group = (RadioGroup) findViewById(R.id.sex_group);
        major = (Spinner) findViewById(R.id.major);
        grade = (Spinner) findViewById(R.id.grade);*/
        register = (Button) findViewById(R.id.register);
        /*major.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Constant.majors));*/
        /*grade.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Constant.grades));*/
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*grade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gradeString = Constant.grades[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        major.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                majorString = Constant.majors[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.boy:
                        sex = "男";
                        break;
                    case R.id.girl:


                         sex = "女";
                        break;
                }
            }
        });*/
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("") || pwdAgain.getText().toString().equals("") || pwd.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pwd.getText().toString().trim().equals(pwdAgain.getText().toString().trim())) {
                    Toast.makeText(RegisterActivity.this, "前后密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                /*if (majorString.equals("")) {
                    Toast.makeText(RegisterActivity.this, "请选择专业", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (gradeString.equals("")) {
                    Toast.makeText(RegisterActivity.this, "请选择年级", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sex.equals("")) {
                    Toast.makeText(RegisterActivity.this, "请选择性别", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
                if (!pattern.matcher(username.getText().toString().trim()).matches()) {
                    Toast.makeText(RegisterActivity.this, "用户名只能是英文字母或数字", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pattern.matcher(pwd.getText().toString().trim()).matches()) {
                    Toast.makeText(RegisterActivity.this, "密码只能是英文字母或数字", Toast.LENGTH_SHORT).show();
                    return;
                }

                name = username.getText().toString().trim();
                password = pwd.getText().toString().trim();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("pwd", password));
                /*params.add(new BasicNameValuePair("sex", sex.equals("女")?"0":"1"));
                params.add(new BasicNameValuePair("major", Constant.majorNum(majorString)));
                params.add(new BasicNameValuePair("grade", Constant.gradeNum(gradeString)));*/
                new HttpUtil().create(HttpUtil.POST, Constant.REGISTER_API, params, new HttpUtil.HttpCallBallListener() {
                    @Override
                    public void onStart() {
                        pd = ProgressDialog.show(RegisterActivity.this, "提示", "注册中，请稍等");
                    }

                    @Override
                    public void onFinish() {
                        pd.cancel();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(RegisterActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                RegisterActivity.this.finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
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
