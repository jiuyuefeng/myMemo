package com.vlife.mymemo.mainactivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.mymemo.R;
import com.vlife.mymemo.sqlite.ChangeSqlite;
import com.vlife.mymemo.date.Date;
import com.vlife.mymemo.ui.DrawLine;
import com.vlife.mymemo.adapter.Notepad;
import com.vlife.mymemo.sqlite.SqliteHelper;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
public class EditActivity extends BaseActivity {

    private Button cancelButton;
    private String content;
    private Context context = this;
    private String date;
    private Date dateNow;
    private EditText editText;
    private String id=null;
    private Button saveButton;
    private Button yellowButton;//黄色背景
    private Button blueButton;//蓝色背景
    private Button whiteButton;//白色背景
    private Button greenButton;//绿色背景
    private Button redButton;//红色背景
    private Integer bgId=0;//保存背景图片的ID
    private TextView textView;

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.edit_show);
        this.textView = ((TextView) findViewById(R.id.date_edit));//日期栏
        this.editText = ((DrawLine) findViewById(R.id.content_edit));//文本栏
        this.cancelButton = ((Button) findViewById(R.id.text_cancel_button));
        this.saveButton = ((Button) findViewById(R.id.text_save_button));

        this.yellowButton= ((Button)findViewById(R.id.yellow_button));
        this.blueButton= ((Button)findViewById(R.id.blue_button));
        this.whiteButton= ((Button)findViewById(R.id.white_button));
        this.greenButton= ((Button)findViewById(R.id.green_button));
        this.redButton= ((Button)findViewById(R.id.red_button));


        this.id = getIntent().getStringExtra("idItem");
        //String edittext=editText.getText().toString();
        //编辑新建页
        if(id==null){
            this.dateNow=new Date();
            this.date=this.dateNow.getDate();
            this.textView.setText(this.date);
        }
        //编辑已有页
        else{
            this.date = getIntent().getStringExtra("dateItem");
            this.content = getIntent().getStringExtra("contentItem");
            //this.id = getIntent().getStringExtra("idItem");
            this.bgId = getIntent().getIntExtra("backgroundItem", 0);//获取当前背景ID

            //System.out.println("-----idItem-----id=" + id);
            this.editText.setSelection(this.editText.length());
            this.editText.setText(this.content);
            this.textView.setText(this.date);
            this.dateNow = new Date();
            this.date = this.dateNow.getDate();
            this.textView.setText(this.date);
        }

        //显示编辑前的背景
        if(bgId==1) {
            editText.setBackgroundResource(R.drawable.bg_yellow);
            textView.setBackgroundResource(R.drawable.bg_yellowtop);
        }
        if(bgId==2) {
            editText.setBackgroundResource(R.drawable.bg_blue);
            textView.setBackgroundResource(R.drawable.bg_bluetop);
        }
        if(bgId==3) {
            editText.setBackgroundResource(R.drawable.bg_white);
            textView.setBackgroundResource(R.drawable.bg_whitetop);
        }
        if(bgId==4) {
            editText.setBackgroundResource(R.drawable.bg_green);
            textView.setBackgroundResource(R.drawable.bg_greentop);
        }
        if(bgId==5) {
            editText.setBackgroundResource(R.drawable.bg_red);
            textView.setBackgroundResource(R.drawable.bg_redtop);
        }



        //设置背景
        this.yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setBackgroundResource(R.drawable.bg_yellowtop);
                editText.setBackgroundResource(R.drawable.bg_yellow);
                bgId=1;
            }
        });

        this.blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setBackgroundResource(R.drawable.bg_bluetop);
                editText.setBackgroundResource(R.drawable.bg_blue);
                bgId=2;
            }
        });

        this.whiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setBackgroundResource(R.drawable.bg_whitetop);
                editText.setBackgroundResource(R.drawable.bg_white);
                bgId=3;
            }
        });

        this.greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setBackgroundResource(R.drawable.bg_greentop);
                editText.setBackgroundResource(R.drawable.bg_green);
                bgId=4;
            }
        });

        this.redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setBackgroundResource(R.drawable.bg_redtop);
                editText.setBackgroundResource(R.drawable.bg_red);
                bgId=5;
            }
        });


        //确定按钮
        this.saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //editText.setBackground(R.drawable.shape_);
                //editText.setBackgroundResource(R.drawable.green);
                SQLiteDatabase localSqLiteDatabase = new SqliteHelper(
                        EditActivity.this.context, null, null, 0)
                        .getWritableDatabase();
                Notepad localNotepad = new Notepad();
                ChangeSqlite localChangeSqlite = new ChangeSqlite();
                String strContent = EditActivity.this.editText.getText()
                        .toString();
                if (strContent.equals("")) {
                    Toast.makeText(EditActivity.this.context, "请输入内容",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String strTitle = strContent.length() > 11 ? " "
                        + strContent.substring(0, 11) : strContent;
                localNotepad.setContent(strContent);
                localNotepad.setTitle(strTitle);
                localNotepad.setData(date);
                localNotepad.setId(id);
                localNotepad.setBackground(bgId);
                System.out.println("-----id-----id=" + id);
                if(id==null){
                    localChangeSqlite.add(localSqLiteDatabase, localNotepad);
                }
                else{
                localChangeSqlite.update(localSqLiteDatabase, localNotepad);
                }
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
