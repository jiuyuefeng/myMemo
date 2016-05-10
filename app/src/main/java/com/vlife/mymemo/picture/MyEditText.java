package com.vlife.mymemo.picture;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/5/9 0009.
 */
public class MyEditText extends EditText {

    public MyEditText(Context context){
        super(context);
    }
    public MyEditText(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    public void insertDrawable(int id){
        final SpannableString ss=new SpannableString("easy");
        //得到Drawable对象，即所有插入的图片
        //Drawable d=getResources().getDrawable(id,null);
        Drawable d= ContextCompat.getDrawable(getContext(),id);
        d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
        //用Drawable对象代替字符串easy
        ImageSpan span=new ImageSpan(d,ImageSpan.ALIGN_BASELINE);
        //包括0，但是不包括"easy".length()
        ss.setSpan(span,0,"easy".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    }

}
