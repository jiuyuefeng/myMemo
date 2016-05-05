package com.example.administrator.mymemo;

import android.content.Context;
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
public class EditActivity extends BaseActivity {

    private Button cancelButton;
    private String content;
    private Context context = this;
    private String date;
    private Date dateNow;
    private EditText editText;
    private String id=null;
    private Button sureButton;
    private Button yellowButton;//黄色背景
    private Button blueButton;//蓝色背景
    private Button whiteButton;//白色背景
    private Button greenButton;//绿色背景
    private Button redButton;//红色背景
    private Integer bg_id=0;//保存背景图片的ID
    private TextView textView;

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.showedit);
        this.textView = ((TextView) findViewById(R.id.editdate));//日期栏
        this.editText = ((DrawLine) findViewById(R.id.edittexttwo));//文本栏
        this.cancelButton = ((Button) findViewById(R.id.editbutton));
        this.sureButton = ((Button) findViewById(R.id.editbutton2));

        this.yellowButton= ((Button)findViewById(R.id.yellowbutton));
        this.blueButton= ((Button)findViewById(R.id.bluebutton));
        this.whiteButton= ((Button)findViewById(R.id.whitebutton));
        this.greenButton= ((Button)findViewById(R.id.greenbutton));
        this.redButton= ((Button)findViewById(R.id.redbutton));


        this.id = getIntent().getStringExtra("idItem");
        //String edittext=editText.getText().toString();

        if(id==null)//编辑新建页
        {
            this.dateNow=new Date();
            this.date=this.dateNow.getDate();
            this.textView.setText(this.date);
        }

        else //编辑已有页
        {
            this.date = getIntent().getStringExtra("dateItem");
            this.content = getIntent().getStringExtra("contentItem");
            //this.id = getIntent().getStringExtra("idItem");
            this.bg_id = getIntent().getIntExtra("backgroundItem", 0);//获取当前背景ID

            //System.out.println("-----idItem-----id=" + id);
            this.editText.setSelection(this.editText.length());
            this.editText.setText(this.content);
            this.textView.setText(this.date);
            this.dateNow = new Date();
            this.date = this.dateNow.getDate();
            this.textView.setText(this.date);
        }

        //显示编辑前的背景
        if(bg_id==1)
        {
            editText.setBackgroundResource(R.drawable.yellow);
            textView.setBackgroundResource(R.drawable.yellowtop);
        }
        if(bg_id==2)
        {
            editText.setBackgroundResource(R.drawable.blue);
            textView.setBackgroundResource(R.drawable.bluetop);
        }
        if(bg_id==3)
        {
            editText.setBackgroundResource(R.drawable.white);
            textView.setBackgroundResource(R.drawable.whitetop);
        }
        if(bg_id==4)
        {
            editText.setBackgroundResource(R.drawable.green);
            textView.setBackgroundResource(R.drawable.greentop);
        }
        if(bg_id==5)
        {
            editText.setBackgroundResource(R.drawable.red);
            textView.setBackgroundResource(R.drawable.redtop);
        }



        //设置背景
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
                bg_id=4;
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


        //确定按钮
        this.sureButton.setOnClickListener(new View.OnClickListener() {

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
                localNotepad.setdata(date);
                localNotepad.setid(id);
                localNotepad.setBackground(bg_id);
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
