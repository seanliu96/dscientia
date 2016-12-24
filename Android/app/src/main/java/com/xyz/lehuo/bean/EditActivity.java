package com.xyz.lehuo.bean;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.xyz.lehuo.R;
import com.xyz.lehuo.datebase.DatabaseManager;
import com.xyz.lehuo.global.BaseActivity;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class EditActivity extends BaseActivity {

    String single[] = {"日记","便签","学习笔记","读书笔记","旅行游记"};
    String week[] = {"Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun"};
    StringBuilder sb;
    private String singleChoice;

    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button saveButton;
    EditText noteContent;
    EditText noteTitle;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initView();
    }

    private void initView() {
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        saveButton = (Button) findViewById(R.id.save);
        noteContent = (EditText) findViewById(R.id.editText3);
        noteTitle = (EditText) findViewById(R.id.note_title);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = noteTitle.getText().toString();
                String newContent = noteContent.getText().toString();
                Date date = new Date();
                String newDate = DateFormat.getDateTimeInstance().format(date);
                Calendar newCalendar = Calendar.getInstance();
                newCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                String weekNum = String.valueOf(newCalendar.get(Calendar.DAY_OF_WEEK));
                String newWeek = week[Integer.parseInt(weekNum) - 1];
                Note newNote = new Note(newDate, newWeek, newTitle, newContent);
                //finish();
            }
        });




    }
    public void singleChoiceItems(View view) {//单选按钮监听
        Dialog dialog = new AlertDialog.Builder(this).setTitle("Select type").setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                singleChoice = single[0];//默认选择第一项
                Toast.makeText(EditActivity.this, "选择了" + singleChoice, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        })
                //设置单选框监听
                .setSingleChoiceItems(single, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        singleChoice = single[which];//根据which决定选择了哪一个子项
                    }
                }).create();
        dialog.show();
    }
}
