package com.vlife.mymemo.mainactivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.administrator.mymemo.R;
import com.vlife.mymemo.adapter.Notepad;
import com.vlife.mymemo.alarm.AlarmReceiver;
import com.vlife.mymemo.date.Date;
import com.vlife.mymemo.picture.MyEditText;
import com.vlife.mymemo.sqlite.ChangeSqlite;
import com.vlife.mymemo.sqlite.SqliteHelper;

import java.io.FileNotFoundException;
import java.util.Calendar;

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
    private Button alarmButton;//闹钟设置按钮
    private AlarmManager alarmManager;
    private Notepad notepad=null;//传出数据
    private Notepad notepadReturn=null;
    private Boolean mIsSave=false;//判断当前是否已经保存

    private Button b;//导入照片按钮
    private MyEditText e;//编辑图片框
    private static final int PHOTO_SUCCESS = 2;
    private static final int CAMERA_SUCCESS = 1;

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        mIsSave=false;
        setContentView(R.layout.edit_show);
        this.textView = ((TextView) findViewById(R.id.date_edit));//日期栏
        this.editText = ((MyEditText) findViewById(R.id.content_edit));//文本栏
        this.cancelButton = ((Button) findViewById(R.id.text_cancel_button));
        this.saveButton = ((Button) findViewById(R.id.text_save_button));

        this.alarmButton=(Button) findViewById(R.id.alarm_button);//闹钟按钮

        this.yellowButton= ((Button)findViewById(R.id.yellow_button));
        this.blueButton= ((Button)findViewById(R.id.blue_button));
        this.whiteButton= ((Button)findViewById(R.id.white_button));
        this.greenButton= ((Button)findViewById(R.id.green_button));
        this.redButton= ((Button)findViewById(R.id.red_button));

        this.notepad=new Notepad();

        //this.id = getIntent().getStringExtra("idItem");

        //响应闹钟
        this.notepadReturn=(Notepad) getIntent().getSerializableExtra("returnAlarm");
        Log.d("my","notepadreturn"+notepadReturn);
        if(this.notepadReturn!=null){
            this.dateNow = new Date();
            this.date = this.dateNow.getDate();
            this.content=notepadReturn.getContent();
            this.bgId=notepadReturn.getBackground();
            this.id=notepadReturn.getId();

            //显示详情
            this.editText.setSelection(this.editText.length());
            this.editText.setText(this.content);
            this.textView.setText(this.date);

        }
        //非闹钟响应情况的
        else {
            this.date = getIntent().getStringExtra("dateItem");
            //Notepad notepad=(Notepad) getIntent().getSerializableExtra("Alarm");
            this.content = getIntent().getStringExtra("contentItem");
            //this.id = getIntent().getStringExtra("idItem");
            this.bgId = getIntent().getIntExtra("backgroundItem", 0);//获取当前背景ID
            this.id = getIntent().getStringExtra("idItem");
        }
        //编辑新建页
        if (id == null) {
            this.dateNow = new Date();
            this.date = this.dateNow.getDate();
            //this.content= editText.getText().toString();
            String strContent=editText.getText().toString();

            Log.d("my","content"+content);
            this.editText.setSelection(this.editText.length());
            this.editText.setText(strContent);
            this.textView.setText(this.date);

            //设置闹钟要传递的数据
            //this.notepad.content = this.content;
            //this.notepad.date = this.date;
        }
        //编辑已有页
        else {
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

        //this.notepad.background=bgId;

        this.alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar currentTime = Calendar.getInstance();

                //创建一个TimePickerDialog实例，并把它显示出来
                new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        currentTime.setTimeInMillis(System.currentTimeMillis());
                        currentTime.set(Calendar.YEAR, year);
                        currentTime.set(Calendar.MONTH, monthOfYear);
                        currentTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                new TimePickerDialog(EditActivity.this,new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                //指定启动EditActivity组件
                                Intent intentReceiver = new Intent(EditActivity.this,AlarmReceiver.class);
                                intentReceiver.setAction("com.MyBroadcast.alarm");
                                //设置闹钟要传递的数据
                                if(!mIsSave){//判断是否已经保存
                                    saveEdit();
                                    mIsSave=true;
                                }
                                Log.d("my","mIsSave"+mIsSave);
                                notepad.id=id;
                                notepad.content = content;
                                notepad.background=bgId;
                                notepad.date = date;
                                //Log.d("my","id"+notepad.background);
                                intentReceiver.putExtra("Alarm",notepad);

                                //intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                                // 创建PendingIntent对象
                                PendingIntent pi = PendingIntent.getBroadcast(EditActivity.this, 0, intentReceiver, PendingIntent.FLAG_CANCEL_CURRENT);
                                Calendar c = Calendar.getInstance();
                                c.setTimeInMillis(System.currentTimeMillis());
                                //根据用户选择时间来设置Calendar对象
                                c.set(Calendar.HOUR_OF_DAY, hour);
                                c.set(Calendar.MINUTE, minute);
                                // c.set(Calendar.MONTH,c.get(Calendar.MONTH)+1);
                                //获取AlarmManager对象
                                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                //设置AlarmManager将在Calendar对应时间启动指定组件
                                alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
                                //显示闹钟设置成功的提示信息
                                Toast.makeText(EditActivity.this, "闹钟设置成功", Toast.LENGTH_SHORT).show();
                                Log.d("my","shijian"+c);
                            }
                        }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE),true).show();
                    }
                }, currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //确定按钮
        this.saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //editText.setBackground(R.drawable.shape_);
                //editText.setBackgroundResource(R.drawable.green);
                if(!mIsSave){
                    saveEdit();
                    mIsSave=true;
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


        //照片导入方式选择
        b = (Button) findViewById(R.id.picture_button);
        e = (MyEditText) findViewById(R.id.content_edit);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final CharSequence[] items = { "手机相册", "相机拍摄" };
                AlertDialog dlg = new AlertDialog.Builder(EditActivity.this).setTitle("选择图片").setItems(items,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int item) {
                                //这里item是根据选择的方式
                                //在item数组里定义了两种方式，拍照的小标为1（调用拍照）
                                if(item==1){
                                    Intent getImageByCamera= new Intent("android.media.action.IMAGE_CAPTURE");
                                    startActivityForResult(getImageByCamera, CAMERA_SUCCESS);
                                }else{
                                    Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
                                    getImage.addCategory(Intent.CATEGORY_OPENABLE);
                                    getImage.setType("image/*");
                                    startActivityForResult(getImage, PHOTO_SUCCESS);
                                }
                            }
                        }).create();
                dlg.show();
                //e.insertDrawable(R.drawable.easy);
            }
        });

    }

    //导入图片
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        ContentResolver resolver = getContentResolver();
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_SUCCESS:
                    //获得图片的uri
                    Uri originalUri = intent.getData();
                    Bitmap bitmap = null;
                    try {
                        Bitmap originalBitmap = BitmapFactory.decodeStream(resolver.openInputStream(originalUri));
                        bitmap = resizeImage(originalBitmap, 200, 200);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if(bitmap != null){
                        //根据Bitmap对象创建ImageSpan对象
                        ImageSpan imageSpan = new ImageSpan(EditActivity.this, bitmap);
                        //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图片
                        SpannableString spannableString = new SpannableString("[local]"+1+"[/local]");
                        //用ImageSpan对象替换face
                        spannableString.setSpan(imageSpan, 0, "[local]1[local]".length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //将选择的图片追加到EditText中光标所在的位置
                        int index = e.getSelectionStart(); //获取光标所在位置
                        Editable edit_text = e.getEditableText();
                        if(index <0 || index >= edit_text.length()){
                            edit_text.append(spannableString);
                        }else{
                            edit_text.insert(index, spannableString);
                        }
                    }else{
                        Toast.makeText(EditActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CAMERA_SUCCESS:
                    Bundle extras = intent.getExtras();
                    Bitmap originalBitmap1 = (Bitmap) extras.get("data");
                    if(originalBitmap1 != null){
                        bitmap = resizeImage(originalBitmap1, 200, 200);
                        //根据Bitmap对象创建ImageSpan对象
                        ImageSpan imageSpan = new ImageSpan(EditActivity.this, bitmap);
                        //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图片�
                        SpannableString spannableString = new SpannableString("[local]"+1+"[/local]");
                        //用ImageSpan对象替换face
                        spannableString.setSpan(imageSpan, 0, "[local]1[local]".length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //将选择的图片追加到EditText光标所在的位置
                        int index = e.getSelectionStart(); //获取光标所在位置
                        Editable edit_text = e.getEditableText();
                        if(index <0 || index >= edit_text.length()){
                            edit_text.append(spannableString);
                        }else{
                            edit_text.insert(index, spannableString);
                        }
                    }else{
                        Toast.makeText(EditActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }
    /**
     * 图片缩放
     * @param originalBitmap 原始的Bitmap
     * @param newWidth 自定义宽度
     * @param newHeight 自定义高度
     * @return 缩放后的Bitmap
     */
    private Bitmap resizeImage(Bitmap originalBitmap, int newWidth, int newHeight){
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        //定义转换高度
        //int newWidth = 200;
        //int newHeight = 200;
        //计算宽高和缩放率
        float scanleWidth = (float)newWidth/width;
        float scanleHeight = (float)newHeight/height;
        //创建操作图片用的matrix对象Matrix
        Matrix matrix = new Matrix();
        //缩放图片动作
        matrix.postScale(scanleWidth,scanleHeight);
        //旋转图片动作
        //matrix.postRotate(45);
        //创建新的图片Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(originalBitmap,0,0,width,height,matrix,true);
        return resizedBitmap;
    }



    //保存内容
    public void saveEdit(){
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
        localNotepad.setDate(date);
        localNotepad.setId(id);
        localNotepad.setBackground(bgId);
        if(id==null){
            localChangeSqlite.add(localSqLiteDatabase, localNotepad);
        }
        else{
            localChangeSqlite.update(localSqLiteDatabase, localNotepad);

        }
        id=localNotepad.id;
        content=strContent;
        bgId=localNotepad.background;
        date=localNotepad.date;
    }

}
