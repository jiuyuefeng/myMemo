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
import com.vlife.mymemo.mainactivity.EditActivity;
import com.vlife.mymemo.mainactivity.MainActivity;
import com.vlife.mymemo.sqlite.ChangeSqlite;
import com.vlife.mymemo.sqlite.SqliteHelper;
import com.vlife.mymemo.ui.TextViewLine;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
public class NotepadAdapter extends BaseAdapter {

    public Context context;
    public LayoutInflater inflater;
    public ArrayList<Map<String, Object>> list;
    public Integer bg_id=0;

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
        Map<String, Object> map = list.get(arg0);
        boolean boo = (Boolean) map.get("EXPANDED");
        if (!boo) {
            arg1 = inflater.inflate(R.layout.types_show, arg2, false);

            setShow.contentView = (TextView) arg1
                    .findViewById(R.id.content_types_show);
            setShow.dateView = (TextView) arg1.findViewById(R.id.date_types_show);

            String str = (String) list.get(arg0).get("titleItem");
            String dateStr = (String) list.get(arg0).get("dateItem");
            bg_id= (Integer) list.get(arg0).get("backgroundItem");


            //设置背景
            if(bg_id==1) {
                setShow.contentView.setBackgroundResource(R.drawable.bg_yellow);
                setShow.dateView.setBackgroundResource(R.drawable.bg_yellowtop);
            }
            if(bg_id==2) {
                setShow.contentView.setBackgroundResource(R.drawable.bg_blue);
                setShow.dateView.setBackgroundResource(R.drawable.bg_bluetop);
            }
            if(bg_id==3) {
                setShow.contentView.setBackgroundResource(R.drawable.bg_white);
                setShow.dateView.setBackgroundResource(R.drawable.bg_whitetop);
            }
            if(bg_id==4) {
                setShow.contentView.setBackgroundResource(R.drawable.bg_green);
                setShow.dateView.setBackgroundResource(R.drawable.bg_greentop);
            }
            if(bg_id==5) {
                setShow.contentView.setBackgroundResource(R.drawable.bg_red);
                setShow.dateView.setBackgroundResource(R.drawable.bg_redtop);
            }

            setShow.contentView.setText("   " + str);
            setShow.dateView.setText(dateStr);
            setShow.showButtonWrite = (Button) arg1
                    .findViewById(R.id.types_edit_button);
            setShow.showButtonDelete = (Button) arg1
                    .findViewById(R.id.types_delete_button);
            setShow.showButtonWrite.setOnClickListener(new WriteButtonListener(
                    arg0));
            setShow.showButtonDelete
                    .setOnClickListener(new DeleteButtonListener(arg0));
        }
        else {
            arg1 = inflater.inflate(R.layout.style_show_listview, arg2, false);
            setShow.cContentView = (TextViewLine) arg1
                    .findViewById(R.id.content_list_show);
            setShow.cDateView = (TextView) arg1
                    .findViewById(R.id.date_list_show);
            String str = (String) list.get(arg0).get("contentItem");
            String dateStr = (String) list.get(arg0).get("dateItem");
            bg_id= (Integer) list.get(arg0).get("backgroundItem");

            //设置背景
            if(bg_id==1) {
                setShow.cContentView.setBackgroundResource(R.drawable.bg_yellow);
                setShow.cDateView.setBackgroundResource(R.drawable.bg_yellowtop);
            }
            if(bg_id==2) {
                setShow.cContentView.setBackgroundResource(R.drawable.bg_blue);
                setShow.cDateView.setBackgroundResource(R.drawable.bg_bluetop);
            }
            if(bg_id==3) {
                setShow.cContentView.setBackgroundResource(R.drawable.bg_white);
                setShow.cDateView.setBackgroundResource(R.drawable.bg_whitetop);
            }
            if(bg_id==4) {
                setShow.cContentView.setBackgroundResource(R.drawable.bg_green);
                setShow.cDateView.setBackgroundResource(R.drawable.bg_greentop);
            }
            if(bg_id==5) {
                setShow.cContentView.setBackgroundResource(R.drawable.bg_red);
                setShow.cDateView.setBackgroundResource(R.drawable.bg_redtop);
            }

            setShow.cContentView.setText("" + str);
            setShow.cDateView.setText(dateStr);
            setShow.styleButtonWrite = (Button) arg1
                    .findViewById(R.id.content_edit_button);
            setShow.styleButtonWrite
                    .setOnClickListener(new WriteButtonListener(arg0));
            setShow.styleButtonDelete = (Button) arg1
                    .findViewById(R.id.content_delete_button);
            setShow.styleButtonDelete
                    .setOnClickListener(new DeleteButtonListener(arg0));
        }
        return arg1;
    }

    class WriteButtonListener implements View.OnClickListener {
        private int position;

        public WriteButtonListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            //传递数据
            Bundle b = new Bundle();
            b.putString("contentItem",
                    (String) list.get(position).get("contentItem"));
            b.putString("dateItem", (String) list.get(position).get("dateItem"));
            b.putString("idItem", (String) list.get(position).get("idItem"));
            b.putInt("backgroundItem",(Integer) list.get(position).get("backgroundItem"));
            //Log.d("bg",String.valueOf(bg_id));
            Intent intent = new Intent(context,
                    EditActivity.class);
            intent.putExtras(b);
            (context).startActivity(intent);

        }

    }

    class DeleteButtonListener implements View.OnClickListener {
        private int position;

        public DeleteButtonListener(int position) {
            this.position = position;

        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            // TODO Auto-generated method stub

            //listview的item删除
            android.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("确定删除？");
            builder.setPositiveButton("删除",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            // TODO Auto-generated method stub
                            SqliteHelper sql = new SqliteHelper(context, null,
                                    null, 1);
                            SQLiteDatabase dataBase = sql.getWritableDatabase();
                            ChangeSqlite change = new ChangeSqlite();
                            Notepad notepad = new Notepad();
                            notepad.setId((String) list.get(position).get(
                                    "idItem"));
                            change.delete(dataBase, notepad);
                            ((MainActivity) context).showUpdate();

                        }
                    });
            builder.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                        }
                    });
            builder.create();
            builder.show();
        }

    }

    class SetShow {
        public TextView contentView;
        public TextView dateView;
        public TextViewLine cContentView;
        public TextView cDateView;
        public Button styleButtonWrite;
        public Button styleButtonDelete;
        public Button showButtonWrite;
        public Button showButtonDelete;

    }

}
