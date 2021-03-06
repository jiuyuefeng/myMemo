package com.vlife.mymemo.edit;

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
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.administrator.mymemo.R;
import com.vlife.mymemo.adapter.Notepad;
import com.vlife.mymemo.alarm.AlarmReceiver;
import com.vlife.mymemo.date.Date;
import com.vlife.mymemo.mainactivity.BaseActivity;
import com.vlife.mymemo.sqlite.changeSqlite;
import com.vlife.mymemo.sqlite.sqliteHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *  编辑页面
 * Created by Administrator on 2016/4/27 0027.
 */
public class EditActivity extends BaseActivity {

    private Context context = this;//上下文

    private String content;//文本
    private String date;//日期
    private String id=null;//item的ID
    private Integer bgId=0;//保存背景图片的ID
    private Integer alarmId=0;//设置闹钟标志位

    private TextView textView;//日期栏
    private EditText editText;//文本栏

    private AlarmManager alarmManager;//闹钟管理对象
    private Button alarmView;//闹钟是否设置图标按钮

    private Notepad notepad=null;//传出数据
    //private Boolean mIsSave=false;//判断当前是否已经保存

    private static final int PHOTO_SUCCESS = 2;
    private static final int CAMERA_SUCCESS = 1;

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.edit_show);
        this.textView = ((TextView) findViewById(R.id.date_edit));//日期栏
        this.editText = ((MyEditText) findViewById(R.id.content_edit));//文本栏
        Button cancelButton = ((Button) findViewById(R.id.text_cancel_button));//取消保存按钮
        Button saveButton = ((Button) findViewById(R.id.text_save_button));//确定保存按钮

        Button yellowButton = ((Button) findViewById(R.id.yellow_button));//黄色背景选择按钮
        Button blueButton = ((Button) findViewById(R.id.blue_button));//蓝色背景选择按钮
        Button whiteButton = ((Button) findViewById(R.id.white_button));//白色背景选择按钮
        Button greenButton = ((Button) findViewById(R.id.green_button));//绿色背景选择按钮
        Button redButton = ((Button) findViewById(R.id.red_button));//红色背景选择按钮

        Button alarmButton = (Button) findViewById(R.id.alarm_button);//闹钟按钮
        alarmView=(Button) findViewById(R.id.alarm_view);//闹钟是否设置按钮
        Button shareButton = (Button) findViewById(R.id.share_button);//分享按钮
        Button pictureButton = (Button) findViewById(R.id.picture_button);//图片导入按钮

        this.notepad=new Notepad(); //声明闹钟要传递的数据对象

        /**
         *  闹钟响应与非闹钟响应的分别处理
         *
         */

        // 闹钟响应
        Notepad notepadReturn = (Notepad) getIntent().getSerializableExtra("returnAlarm"); //响应闹钟时传回的数据
        //判断是否有闹钟响应
        if(notepadReturn !=null){//闹钟响应
            alarmView.setVisibility(View.INVISIBLE);//设置闹钟是否设置按钮为隐藏状态
            Date dateNow = new Date();
            this.date = dateNow.getDate();
            this.content= notepadReturn.getContent();
            this.bgId= notepadReturn.getBackground();
            this.id= notepadReturn.getId();
            this.alarmId=notepadReturn.getAlarm();
            Log.d("my","alarmId0"+alarmId);

            //显示详情
            this.textView.setText(this.date); //显示日期时间
            //显示纯文本
            String plainText=deleteUri(this.content);
            this.editText.setSelection(this.editText.length());
            this.editText.setText(plainText);
            //显示图片
            try {
                showImage(this.content);
            } catch (IOException e1) {
                Log.e("EditActivity","io:",e1);
            }
            Log.d("my","id"+id);
            //更新数据库（更新闹钟响应按钮）
            saveEdit();
        }
        else {//非闹钟响应情况
            this.date = getIntent().getStringExtra("dateItem");
            this.content = getIntent().getStringExtra("contentItem");
            this.bgId = getIntent().getIntExtra("backgroundItem", 0);//获取当前背景ID
            this.id = getIntent().getStringExtra("idItem");
            this.alarmId= getIntent().getIntExtra("alarmItem",0);//获取当前闹钟设置标志位
            Log.d("my","alarmId1"+alarmId);

        }
       //判断是否是新建页
        if (id == null) { //编辑新建页
            Date dateNow = new Date();
            this.date = dateNow.getDate();
            this.textView.setText(this.date);
            //显示当前内容
            String strContent=editText.getText().toString();
            this.editText.setSelection(this.editText.length());
            this.editText.setText(strContent);
        }
        else {//编辑已有页
            if(this.alarmId==1){
                alarmView.setVisibility(View.VISIBLE);//设置闹钟标志位按钮为可见
            }else{
                alarmView.setVisibility(View.INVISIBLE);//设置闹钟标志位按钮为可见
            }
            Log.d("my","alarmId2"+alarmId);
            Date dateNow = new Date();
            this.date = dateNow.getDate();
            this.textView.setText(this.date);
            //显示纯文本
            String plainText=deleteUri(this.content);
            this.editText.setSelection(this.editText.length());
            this.editText.setText(plainText);
            //显示图片
            try {
                showImage(this.content);
            } catch (IOException e1) {
                Log.e("EditActivity","io:",e1);
            }
        }

        //显示编辑前的背景
        chooseBackground(bgId);

        /**
         * 设置背景
         *
         */
        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setBackgroundResource(R.drawable.bg_yellowtop);
                editText.setBackgroundResource(R.drawable.bg_yellow);
                bgId=1;
            }
        });
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setBackgroundResource(R.drawable.bg_bluetop);
                editText.setBackgroundResource(R.drawable.bg_blue);
                bgId=2;
            }
        });
        whiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setBackgroundResource(R.drawable.bg_whitetop);
                editText.setBackgroundResource(R.drawable.bg_white);
                bgId=3;
            }
        });
        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setBackgroundResource(R.drawable.bg_greentop);
                editText.setBackgroundResource(R.drawable.bg_green);
                bgId=4;
            }
        });
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setBackgroundResource(R.drawable.bg_redtop);
                editText.setBackgroundResource(R.drawable.bg_red);
                bgId=5;
            }
        });
        Log.d("my","alarmId3"+alarmId);

        /**
         *  闹钟按钮响应
         *
         */
        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置闹钟
                setAlarm();
            }
        });

        /**
         * 闹钟标志位编辑按钮响应
         *
         */
        alarmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(EditActivity.this,view);
                MenuInflater inflater=popupMenu.getMenuInflater();
                //引入菜单资源
                //popupMenu.inflate(R.menu.popup_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.edit_alarm:
                                setAlarm();
                                break;
                            case R.id.delete_alarm:
                                deleteAlarm();
                                alarmView.setVisibility(View.INVISIBLE);//设置闹钟标志位位隐藏状态
                                break;
                        }
                        return false;
                    }
                });
                inflater.inflate(R.menu.popup_menu,popupMenu.getMenu());
                popupMenu.show();
            }
        });

        /**
         *  确定保存按钮响应
         *
         */
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean saveSuccess=saveEdit();//判断是否保存成功
                if(saveSuccess){
                    finish();}
            }
        });

        /**
         *  取消保存按钮响应
         *
         */
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         *  分享功能按钮响应
         *
         */
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean saveSuccess=saveEdit();
                if(saveSuccess) {//分享之前先判断是否保存
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    //判断分享内容是否含图片
                    if (content.equals(deleteUri(content))) {//不包含图片，即纯文本
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, content);
                    } else {//包含图片
                        Bitmap bitmapView = convertViewToBitmap(editText);//将当前文本栏转化成bitmap
                        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmapView, null, null));//获取bitmap的uri
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_STREAM, uri);
                    }
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.Share));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(Intent.createChooser(intent, getTitle()));
                }
            }
        });

        /**
         *  照片导入方式选择（照片导入按钮响应）
         *
         */
        pictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final CharSequence[] items = { getString(R.string.Photo), getString(R.string.Camera) };
                AlertDialog dlg = new AlertDialog.Builder(EditActivity.this).setTitle(R.string.ChoosePicture).setItems(items,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int item) {

                                    //在item数组里定义了两种方式，拍照的数标为1（调用拍照）
                                    if(item==1){
                                        Intent getImageByCamera= new Intent(getString(R.string.CameraCapturePicture));
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
            }
        });
    }


    /**
     * 各方法的封装函数
     */


    /**
     *  设置闹钟
     *
     */
    public void setAlarm(){
        boolean saveSuccess=saveEdit();
        if(saveSuccess){//设置闹钟之前先判断是否保存
            final Calendar currentTime = Calendar.getInstance();//获取当前时间
            currentTime.setTimeInMillis(System.currentTimeMillis());
            //创建一个DatePickerDialog实例，并把它显示出来
            new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener(){
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    currentTime.set(Calendar.YEAR, year);
                    currentTime.set(Calendar.MONTH, monthOfYear);
                    currentTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    //创建一个TimePickerDialog实例，并把它显示出来
                    new TimePickerDialog(EditActivity.this,new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                            //指定启动EditActivity组件
                            Intent intentReceiver = new Intent(EditActivity.this,AlarmReceiver.class);
                            intentReceiver.setAction(getString(R.string.MY_ACTION));

                            //设置闹钟要传递的数据
                            notepad.id=id;
                            //Log.d("my","idsend"+id);
                            notepad.content = content;
                            notepad.background=bgId;
                            notepad.date = date;
                            notepad.alarm=alarmId;
                            //Log.d("my","alarmId4"+alarmId);
                            //传递数据
                            intentReceiver.putExtra("Alarm",notepad);
                            // 创建PendingIntent对象
                            PendingIntent pi = PendingIntent.getBroadcast(EditActivity.this, Integer.parseInt(id), intentReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
                            //根据用户选择时间来设置Calendar对象
                            currentTime.set(Calendar.HOUR_OF_DAY, hour);
                            currentTime.set(Calendar.MINUTE, minute);
                            //获取AlarmManager对象
                            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                            long timeSet=currentTime.getTimeInMillis();//获取设定时间（毫秒）
                            long timeNow=Calendar.getInstance().getTimeInMillis();//获取当前时间（毫秒）
                            Log.d("time","timeSet"+timeSet);
                            Log.d("time","timeNow"+timeNow);
                            if((timeSet+60000)>=timeNow){//判断时间是否设置合理(由于currentTime获取较早，所以需要消除程序运行耗时的影响，即加1秒)
                                // 设置AlarmManager将在Calendar对应时间启动指定组件
                                alarmManager.set(AlarmManager.RTC_WAKEUP, currentTime.getTimeInMillis(), pi);
                                alarmId=1;
                                alarmView.setVisibility(View.VISIBLE);//设置闹钟标志位显示状态
                                //显示闹钟设置成功的提示信息
                                Toast.makeText(EditActivity.this, R.string.AlarmSuccess, Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(EditActivity.this, R.string.AlarmFaild, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE),true).show();
                }
            }, currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    /**
     *  取消闹钟
     *
     */
    public void deleteAlarm(){
        Intent intent = new Intent(EditActivity.this,
                AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(
                EditActivity.this, Integer.parseInt(id), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // And cancel the alarm.
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(sender);
        alarmId=0;
        saveEdit();//更新数据库（即编辑闹钟设置）
    }

    /**
     *   导入图片
     * @param requestCode 通过下标值选择导入图片方式
     * @param resultCode 是否选择了导入方式
     * @param intent 通过intent传入数据
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        ContentResolver resolver = getContentResolver();
        if (resultCode == RESULT_OK) {
            //根据选择的方式导入图片
            switch (requestCode) {
                case PHOTO_SUCCESS:
                    //获得图片的uri
                    Uri originalUri = intent.getData();
                    Bitmap bitmap = null;
                    try {
                        Bitmap originalBitmap = BitmapFactory.decodeStream(resolver.openInputStream(originalUri));//根据uri获取bitmap
                        bitmap = resizeImage(originalBitmap, 800, 800);//设置图片属性
                    } catch (FileNotFoundException e) {
                        Log.e("EditActivity","io:",e);
                    }
                    if(bitmap != null){
                        //根据Bitmap对象创建ImageSpan对象
                        ImageSpan imageSpan = new ImageSpan(EditActivity.this, bitmap);
                        //获取替换字符串长度
                        int oriUriLength=(getString(R.string.local)+originalUri+getString(R.string.local2)).length();
                        //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图片
                        SpannableString spannableString = new SpannableString(getString(R.string.local)+originalUri+getString(R.string.local2));
                        //用ImageSpan对象替换文本
                        spannableString.setSpan(imageSpan, 0,oriUriLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //将选择的图片追加到EditText中光标所在的位置
                        int index = editText.getSelectionStart(); //获取光标所在位置
                        Editable edit_text = editText.getEditableText();
                        if(index <0 || index >= edit_text.length()){
                            edit_text.append(spannableString);
                            //edit_text.append("\r\n");
                        } else{
                            edit_text.insert(index, spannableString);
                            //edit_text.append("\r\n");
                        }
                    }else{
                        Toast.makeText(EditActivity.this, R.string.FailedGetPicture, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CAMERA_SUCCESS:
                    Bundle extras = intent.getExtras();
                    Bitmap originalBitmap1 = (Bitmap) extras.get(getString(R.string.data));
                    if(originalBitmap1 != null){
                        //获取图片uri
                        Uri originalUri1= Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), originalBitmap1, null,null));
                        Bitmap resizeImage = resizeImage(originalBitmap1, 800, 800);//设置图片属性
                        //根据Bitmap对象创建ImageSpan对象
                        ImageSpan imageSpan = new ImageSpan(EditActivity.this, resizeImage);
                        //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图片�
                        SpannableString spannableString = new SpannableString(getString(R.string.local)+originalUri1+getString(R.string.local2));
                        //用ImageSpan对象替换文本字符
                        spannableString.setSpan(imageSpan, 0, (getString(R.string.local)+originalUri1+getString(R.string.local2)).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //将选择的图片追加到EditText光标所在的位置
                        int index = editText.getSelectionStart(); //获取光标所在位置
                        Editable edit_text = editText.getEditableText();
                        if(index <0 || index >= edit_text.length()){
                            edit_text.append(spannableString);
                            //edit_text.append("\r\n");
                        }
                        else{
                            edit_text.insert(index, spannableString);
                            //edit_text.append("\r\n");
                        }
                    }else{
                        Toast.makeText(EditActivity.this,R.string.FailedGetPicture, Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     *   图片缩放(属性设置)
     * @param originalBitmap 原始图片
     * @param newWidth   自定义宽度
     * @param newHeight  自定义高度
     * @return 处理后的Bitmap
     */
    private Bitmap resizeImage(Bitmap originalBitmap, int newWidth, int newHeight){
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        //计算宽高和缩放率
        float zoomWidth = (float)newWidth/width;
        float zoomHeight = (float)newHeight/height;
        //创建操作图片用的matrix对象Matrix
        Matrix matrix = new Matrix();
        //缩放图片动作
        matrix.postScale(zoomWidth,zoomHeight);
        //创建新的图片Bitmap
        return Bitmap.createBitmap(originalBitmap,0,0,width,height,matrix,true);
    }

    /**
     *  保存内容
     * @return 是否成功保存编辑
     */
    public boolean saveEdit(){
        SQLiteDatabase localSqLiteDatabase = new sqliteHelper(
                EditActivity.this.context, null, null, 0)
                .getWritableDatabase();
        Notepad localNotepad = new Notepad();
        changeSqlite localChangeSqlite = new changeSqlite();
        String strContent = EditActivity.this.editText.getText()
                .toString();
        if (strContent.equals("")) {
            Toast.makeText(EditActivity.this.context, R.string.PleaseInput,
                    Toast.LENGTH_SHORT).show();
            return false;
        }else{
            //String strTitle = strContent.length() > 11 ? "       " + strContent.substring(0, 11) : strContent;
            localNotepad.setContent(strContent);
            //localNotepad.setTitle(strTitle);
            localNotepad.setDate(date);
            localNotepad.setId(id);
            localNotepad.setBackground(bgId);
            localNotepad.setAlarm(alarmId);
            Log.d("my","alarmId5"+alarmId);
            if(id==null){
                long idLong = localChangeSqlite.add(localSqLiteDatabase, localNotepad);
                id=Long.toString(idLong);
            }
            else{
                localChangeSqlite.update(localSqLiteDatabase, localNotepad);
            }
            content=strContent;
            bgId=localNotepad.getBackground();
            date=localNotepad.getDate();
            alarmId=localNotepad.getAlarm();
            //Log.d("my","idset"+id);
            return true;
        }
    }

    /**
     *  打开item之后获取并显示图片
     * @param text=content
     * @throws IOException
     */
    public void showImage(String text) throws IOException {
        //通过正则表达式获得图片uri
        Pattern pattern = Pattern.compile(getString(R.string.MyRegex));
        Matcher matcher = pattern.matcher(text);
        //遍历所有的uri
        while (matcher.find()) {
            //Log.d("my", "myContentMatch"+matcher.group());
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
                Log.e("EditActivity","io:",e);
            }

            if (bitmap != null) {
                //根据Bitmap对象创建ImageSpan对象
                ImageSpan imageSpan2 = new ImageSpan(EditActivity.this, bitmap);
                //获取当前要替换的字符串长度
                String locUri="[local]"+strUri+"[/local]";
                int locUriLength=locUri.length();
                //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图片
                SpannableString spannableString = new SpannableString(locUri);
                //用ImageSpan对象替换文本字符
                spannableString.setSpan(imageSpan2, 0, locUriLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                Log.d("my",""+("[local]" + localUri+"[/local]").length());

                int start=text.indexOf(strUri)-7;//得到图片插入的位置
                Log.d("my","pictureLocation"+start);
                Editable edit_text = editText.getEditableText();
                if (start < 0 || start >= edit_text.length()) {
                    edit_text.append(spannableString);
                } else {
                    edit_text.insert(start, spannableString);
                    //edit_text.append("\r\n");
                }
            }
        }
    }

    /**
     *  删除文本中的uri以得到纯文本
     * @param text=content
     * @return 纯文本
     */
    public String deleteUri(String text){
        //通过正则表达式获得图片uri
        Pattern pattern = Pattern.compile(getString(R.string.MyRegex));
        Matcher matcher = pattern.matcher(text);
        while(matcher.find()){
            String strUri = matcher.group();
            //获取uri的长度、首末位置
            int start1=text.indexOf(strUri)-7;
            int length=strUri.length()+15;
            int end1=length+start1;
            //Log.d("my","length"+length);
            text=text.substring(0,start1)+text.substring(end1);
            //Log.d("my","text"+text);
        }
        return text;
    }

    /**
     *  view转成bitmap
     * @param view=context
     * @return Bitmap
     */
    public Bitmap convertViewToBitmap(View view){
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        //保存图片
        File f = new File(Environment.getExternalStorageDirectory().getPath(), String.valueOf(bitmap));
        if (f.exists()) {
            Boolean fd=f.delete();
            Log.i("my", String.valueOf(fd));
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
           // Log.i(TAG, "已经保存");
        } catch (IOException e) {
            Log.e("EditActivity","io:",e);
        }
        return bitmap;
    }

    /**
     *   选择并设置背景
     * @param i=bgId
     */
    private void chooseBackground(int i){
        TextView textView = ((TextView) findViewById(R.id.date_edit));
        EditText editText = ((MyEditText) findViewById(R.id.content_edit));
        switch (i){
            case 1:
                editText.setBackgroundResource(R.drawable.bg_yellow);
                textView.setBackgroundResource(R.drawable.bg_yellowtop);
                break;
            case 2:
                editText.setBackgroundResource(R.drawable.bg_blue);
                textView.setBackgroundResource(R.drawable.bg_bluetop);
                break;
            case 3:
                editText.setBackgroundResource(R.drawable.bg_white);
                textView.setBackgroundResource(R.drawable.bg_whitetop);
                break;
            case 4:
                editText.setBackgroundResource(R.drawable.bg_green);
                textView.setBackgroundResource(R.drawable.bg_greentop);
                break;
            case 5:
                editText.setBackgroundResource(R.drawable.bg_red);
                textView.setBackgroundResource(R.drawable.bg_redtop);
                break;
        }
    }

}
