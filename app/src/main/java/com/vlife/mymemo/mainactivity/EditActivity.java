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
import android.provider.MediaStore;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private Button shareButton;//分享按钮
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

        shareButton=(Button) findViewById(R.id.share_button);//分享按钮
        b = (Button) findViewById(R.id.picture_button);//图片导入按钮
        e = (MyEditText) findViewById(R.id.content_edit);//图片编辑框

        this.notepad=new Notepad();

        //this.id = getIntent().getStringExtra("idItem");

        //响应闹钟
        this.notepadReturn=(Notepad) getIntent().getSerializableExtra("returnAlarm");
        //Log.d("my","notepadreturn"+notepadReturn);
        if(this.notepadReturn!=null){
            this.dateNow = new Date();
            this.date = this.dateNow.getDate();
            this.content=notepadReturn.getContent();
            this.bgId=notepadReturn.getBackground();
            this.id=notepadReturn.getId();

            //显示详情
            //this.editText.setSelection(this.editText.length());
            //this.editText.setText(this.content);
            this.textView.setText(this.date);
            //this.content=deleteUri(this.content);

            //显示纯文本
            String plainText=deleteUri(this.content);

            this.editText.setSelection(this.editText.length());
            this.editText.setText(plainText);
            //显示图片
            try {
                showImage(this.content);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

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


            Log.d("my","contentlocal1"+content);
            this.dateNow = new Date();
            this.date = this.dateNow.getDate();
            this.textView.setText(this.date);
            //this.content=deleteUri(this.content);
            //this.editText.setSelection(this.editText.length());
            //this.editText.setText(this.content);
            //显示纯文本
            String plainText=deleteUri(this.content);
            Log.d("my","plaintext"+plainText);
            this.editText.setSelection(this.editText.length());
            this.editText.setText(plainText);
            //显示图片
            try {
                showImage(this.content);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

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
        Log.d("my","contentlocal2"+content);

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
                                //Log.d("my","mIsSave"+mIsSave);
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
                                //Log.d("my","shijian"+c);
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


        //分享功能
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!mIsSave){//判断是否已经保存
                    saveEdit();
                    mIsSave=true;
                }
                Intent intent=new Intent(Intent.ACTION_SEND);
                if(content==deleteUri(content)){
                    intent.setType("text/plain");
                    intent.putExtra(intent.EXTRA_TEXT,content);
                }
                else{
                    Bitmap bitmapView=convertViewToBitmap(editText);

                    //Bundle extras = intent.getExtras();
                    //Bitmap bitmap = (Bitmap) extras.get("data");
                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmapView, null,null));
                    intent.setType("image/*");
                    intent.putExtra(intent.EXTRA_STREAM,uri);
                }

                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));
            }
        });


        //照片导入方式选择
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
                                    Intent getImage = new Intent(Intent.ACTION_OPEN_DOCUMENT);
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
                        bitmap = resizeImage(originalBitmap, 800, 800);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if(bitmap != null){
                        //根据Bitmap对象创建ImageSpan对象
                        ImageSpan imageSpan = new ImageSpan(EditActivity.this, bitmap);
                        //获取替换字符串长度
                        int oriuriLength=("[local]1[local]"+originalUri).length();
                        //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图片
                        SpannableString spannableString = new SpannableString("[local]"+originalUri+"[/local]");
                        //用ImageSpan对象替换文本
                        spannableString.setSpan(imageSpan, 0,oriuriLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //将选择的图片追加到EditText中光标所在的位置
                        int index = e.getSelectionStart(); //获取光标所在位置
                        Editable edit_text = e.getEditableText();
                        if(index <0 || index >= edit_text.length()){
                            edit_text.append(spannableString);
                            //edit_text.insert("\r\n");
                        } else{
                            edit_text.insert(index, spannableString);
                            //edit_text.append("\r\n");
                        }
                    }else{
                        Toast.makeText(EditActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CAMERA_SUCCESS:
                    Bundle extras = intent.getExtras();
                    Bitmap originalBitmap1 = (Bitmap) extras.get("data");
                    if(originalBitmap1 != null){
                        //Uri originalUri1=intent.getData();//获取图片uri
                        Uri originalUri1= Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), originalBitmap1, null,null));
                        bitmap = resizeImage(originalBitmap1, 800, 800);
                        //根据Bitmap对象创建ImageSpan对象
                        ImageSpan imageSpan = new ImageSpan(EditActivity.this, bitmap);
                        //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图片�
                        SpannableString spannableString = new SpannableString("[local]"+originalUri1+"[/local]");
                        //用ImageSpan对象替换face
                        spannableString.setSpan(imageSpan, 0, ("[local]"+originalUri1+"[/local]").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //将选择的图片追加到EditText光标所在的位置
                        int index = e.getSelectionStart(); //获取光标所在位置
                        Editable edit_text = e.getEditableText();
                        if(index <0 || index >= edit_text.length()){
                            edit_text.append(spannableString);
                            //edit_text.append("\r\n");
                        }
                        else{
                            edit_text.insert(index, spannableString);
                            //edit_text.append("\r\n");
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


    //图片缩放
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

    //打开item之后获取图片
    public void showImage(String text) throws IOException {
        //通过正则表达式获得图片uri
        Pattern pattern = Pattern.compile("content://((\\w){3,10}\\.)*((\\w){3,10}/)*((\\w){3,10}%)?(\\w){0,2}(\\d){6}");
        Matcher matcher = pattern.matcher(text);

        //int numIndex=0;//记录插入位置
        //int numLength=0;//记录图片uri的总长度
        while (matcher.find()) {
            //Log.d("my", "mycontentmatch"+matcher.group());
            String strUri = matcher.group();
            ContentResolver resolver = getContentResolver();
            //得到当前图片的uri
            Uri localUri = Uri.parse(matcher.group());
            // 读取uri所在的图片
            Bitmap bitmap = null;
            try {
                Bitmap originalBitmap = BitmapFactory.decodeStream(resolver.openInputStream(localUri));
                bitmap = resizeImage(originalBitmap, 800, 800);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if (bitmap != null) {
                //根据Bitmap对象创建ImageSpan对象
                ImageSpan imageSpan2 = new ImageSpan(EditActivity.this, bitmap);
                //获取当前要替换的字符串长度
                String locUri="[local]"+strUri+"[/local]";
                int locUriLength=locUri.length();
                //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图片
                SpannableString spannableString = new SpannableString(locUri);
                //用ImageSpan对象替换face
                spannableString.setSpan(imageSpan2, 0, locUriLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                Log.d("my",""+("[local]" + localUri+"[/local]").length());

                int start=text.indexOf(strUri)-7;

                //numIndex=start;//实际插入图片位置为start-numLength
                //获得当前图片uri所在的首下标,即图片插入位置
                //int index = text.indexOf(strUri)-7;
                Log.d("my","图片位置"+start);
                Editable edit_text = e.getEditableText();
                if (start < 0 || start >= edit_text.length()) {
                    edit_text.append(spannableString);
                } else {
                    edit_text.insert(start, spannableString);
                    //edit_text.append("\r\n");
                }
                //numLength=numLength+length;//更新uri总长度
            }
        }
    }

    public String deleteUri(String text){
        //通过正则表达式获得图片uri
        Pattern pattern = Pattern.compile("content://((\\w){3,10}\\.)*((\\w){3,10}/)*((\\w){3,10}%)?(\\w){0,2}(\\d){6}");
        Matcher matcher = pattern.matcher(text);
        while(matcher.find()){
            String strUri = matcher.group();
            //获取uri的长度
            int start1=text.indexOf(strUri)-7;
            int length=strUri.length()+15;
            int end1=length+start1;
            Log.d("my","length"+length);
            text=text.substring(0,start1)+text.substring(end1);
            Log.d("my","text"+text);

        }
        return text;
    }


    //view转成bitmap
    public Bitmap convertViewToBitmap(View view){
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        //对图片进行缩放处理
        //bitmap = resizeImage(bitmap, 800, 800);
        //保存图片
        File f = new File("/sdcard/camera", String.valueOf(bitmap));
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
           // Log.i(TAG, "已经保存");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
