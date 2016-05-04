package com.example.administrator.mymemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
public class WriteActivity extends BaseActivity {

    private Button cancelButton;
    private Context context = this;
    private String date;
    private EditText editText;
    private Date getDate;
    private Button sureButton;
    private TextView textView;
    private SharedPreferences mysp;

    private Button yellowButton;//黄色背景
    private Button blueButton;//蓝色背景
    private Button whiteButton;//白色背景
    private Button greenButton;//绿色背景
    private Button redButton;//红色背景
    private Integer bg_id=0;//保存背景图片的ID
    //private Setbgcolor setbgcolor;//改变背景

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.writedown);
        this.textView = ((TextView)findViewById(R.id.writedate));
        this.editText = ((DrawLine)findViewById(R.id.edittext));
        this.cancelButton = ((Button)findViewById(R.id.button));
        this.sureButton = ((Button)findViewById(R.id.button2));

        this.yellowButton= ((Button)findViewById(R.id.yellowbutton1));
        this.blueButton= ((Button)findViewById(R.id.bluebutton1));
        this.whiteButton= ((Button)findViewById(R.id.whitebutton1));
        this.greenButton= ((Button)findViewById(R.id.greenbutton1));
        this.redButton= ((Button)findViewById(R.id.redbutton1));

        this.getDate = new Date();
        this.date = this.getDate.getDate();
        this.textView.setText(this.date);

        //保存背景

        //mysp=getSharedPreferences("background", Context.MODE_PRIVATE);
        //final SharedPreferences.Editor editor=mysp.edit();//获取编辑器

        this.yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setBackgroundResource(R.drawable.yellowtop);
                editText.setBackgroundResource(R.drawable.yellow);
                bg_id=1;
            }
        });

        this.blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setBackgroundResource(R.drawable.bluetop);
                editText.setBackgroundResource(R.drawable.blue);
                bg_id=2;
            }
        });

        this.whiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setBackgroundResource(R.drawable.whitetop);
                editText.setBackgroundResource(R.drawable.white);
                bg_id=3;
            }
        });

        this.greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setBackgroundResource(R.drawable.greentop);
                editText.setBackgroundResource(R.drawable.green);
                bg_id=5;
            }
        });

        this.redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setBackgroundResource(R.drawable.redtop);
                editText.setBackgroundResource(R.drawable.red);
                bg_id=5;
            }
        });


        this.sureButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SQLiteDatabase localSqLiteDatabase = new SqliteHelper(WriteActivity.this.context, null, null, 1).getWritableDatabase();
                Notepad localNotepad = new Notepad();
                ChangeSqlite localChangeSqlite = new ChangeSqlite();
                String strContent = WriteActivity.this.editText.getText().toString();
                if (strContent.equals("")) {
                    Toast.makeText(WriteActivity.this.context, "请输入内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                String strTitle=strContent.length()>11?" "+strContent.substring(0, 11):strContent;
                localNotepad.setContent(strContent);
                localNotepad.setTitle(strTitle);
                localNotepad.setdata(date);
                localNotepad.setBackground(bg_id);
                localChangeSqlite.add(localSqLiteDatabase, localNotepad);
                finish();
            }
        });
        this.cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
