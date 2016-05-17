package com.vlife.mymemo.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.mymemo.R;
import com.vlife.mymemo.edit.EditActivity;
import com.vlife.mymemo.mainactivity.MainActivity;
import com.vlife.mymemo.sqlite.changeSqlite;
import com.vlife.mymemo.sqlite.sqliteHelper;
import com.vlife.mymemo.ui.TextViewLine;

import java.util.ArrayList;
import java.util.Map;

/**
 * 自定义listView适配器
 * Created by Administrator on 2016/4/27 0027.
 */
public class NotepadAdapter extends BaseAdapter {

    public Context context;
    public LayoutInflater inflater;
    public ArrayList<Map<String, Object>> list;
    public Integer bgId=0;//背景ID
    public Integer alarmId=0;//闹钟设置标志位

    public NotepadAdapter(Activity activity, ArrayList<Map<String, Object>> list) {

        this.context = activity;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {

        SetShow setShow = new SetShow();
        arg1 = inflater.inflate(R.layout.style_show_listview, arg2, false);
        setShow.contentListShow = (TextViewLine) arg1
                .findViewById(R.id.content_list_show);
        setShow.dateListShow = (TextView) arg1
                .findViewById(R.id.date_list_show);
        String str = (String) list.get(arg0).get("contentItem");
        String dateStr = (String) list.get(arg0).get("dateItem");
        bgId= (Integer) list.get(arg0).get("backgroundItem");
        alarmId=(Integer) list.get(arg0).get("alarmItem");

        //设置背景
        if(bgId==1) {
            setShow.contentListShow.setBackgroundResource(R.drawable.bg_yellow);
            setShow.dateListShow.setBackgroundResource(R.drawable.bg_yellowtop);
        }
        if(bgId==2) {
            setShow.contentListShow.setBackgroundResource(R.drawable.bg_blue);
            setShow.dateListShow.setBackgroundResource(R.drawable.bg_bluetop);
        }
        if(bgId==3) {
            setShow.contentListShow.setBackgroundResource(R.drawable.bg_white);
            setShow.dateListShow.setBackgroundResource(R.drawable.bg_whitetop);
        }
        if(bgId==4) {
            setShow.contentListShow.setBackgroundResource(R.drawable.bg_green);
            setShow.dateListShow.setBackgroundResource(R.drawable.bg_greentop);
        }
        if(bgId==5) {
            setShow.contentListShow.setBackgroundResource(R.drawable.bg_red);
            setShow.dateListShow.setBackgroundResource(R.drawable.bg_redtop);
        }

        setShow.contentListShow.setText(String.format("%s", str));
        setShow.dateListShow.setText(dateStr);
        setShow.contentEditButton = (Button) arg1
                .findViewById(R.id.content_edit_button);
        setShow.contentEditButton
                .setOnClickListener(new WriteButtonListener(arg0));
        setShow.contentDeleteButton = (Button) arg1
                .findViewById(R.id.content_delete_button);
        setShow.contentDeleteButton
                .setOnClickListener(new DeleteButtonListener(arg0));
        return arg1;
    }

    //写事件响应
    class WriteButtonListener implements View.OnClickListener {
        private int position;
        public WriteButtonListener(int position) {
            this.position = position;
        }
        @Override
        public void onClick(View v) {
            //传递数据
            Bundle b = new Bundle();
            b.putString("contentItem",
                    (String) list.get(position).get("contentItem"));
            b.putString("dateItem", (String) list.get(position).get("dateItem"));
            b.putString("idItem", (String) list.get(position).get("idItem"));
            b.putInt("backgroundItem",(Integer) list.get(position).get("backgroundItem"));
            b.putInt("alarmItem",(Integer) list.get(position).get("alarmItem"));
            //Log.d("bg",String.valueOf(bg_id));
            Intent intent = new Intent(context,
                    EditActivity.class);
            intent.putExtras(b);
            (context).startActivity(intent);
        }
    }

    //删除事件响应
    class DeleteButtonListener implements View.OnClickListener {
        private int position;
        public DeleteButtonListener(int position) {
            this.position = position;
        }
        @Override
        public void onClick(View v) {

            android.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.SureDelete);
            builder.setPositiveButton(R.string.Delete,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            sqliteHelper sql = new sqliteHelper(context, null,
                                    null, 1);
                            SQLiteDatabase dataBase = sql.getWritableDatabase();
                            changeSqlite change = new changeSqlite();
                            Notepad notepad = new Notepad();
                            notepad.setId((String) list.get(position).get(
                                    "idItem"));
                            change.delete(dataBase, notepad);
                            ((MainActivity) context).showUpdate();
                        }
                    });
            builder.setNegativeButton(R.string.Cancel1,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create();
            builder.show();
        }
    }

    class SetShow {
        public TextViewLine contentListShow;
        public TextView dateListShow;
        public Button contentEditButton;
        public Button contentDeleteButton;
    }

}
