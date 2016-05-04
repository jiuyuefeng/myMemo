package com.example.administrator.mymemo;

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

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
public class NotepadAdapter extends BaseAdapter {

    public Context context;
    public Context activity;
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
        if (!boo)
        {
            arg1 = inflater.inflate(R.layout.showtypes, arg2, false);
            //arg1.setBackground(list.get(arg0).get("backgroundItem"));

            setShow.contentView = (TextView) arg1
                    .findViewById(R.id.contentTextView);
            setShow.dateView = (TextView) arg1.findViewById(R.id.dateTextView);

            String str = (String) list.get(arg0).get("titleItem");
            String dateStr = (String) list.get(arg0).get("dateItem");
            bg_id= (Integer) list.get(arg0).get("backgroundItem");


            if(bg_id==1)
            {
                setShow.contentView.setBackgroundResource(R.drawable.yellow);
                setShow.dateView.setBackgroundResource(R.drawable.yellowtop);
            }
            if(bg_id==2)
            {
                setShow.contentView.setBackgroundResource(R.drawable.blue);
                setShow.dateView.setBackgroundResource(R.drawable.bluetop);
            }
            if(bg_id==3)
            {
                setShow.contentView.setBackgroundResource(R.drawable.white);
                setShow.dateView.setBackgroundResource(R.drawable.whitetop);
            }
            if(bg_id==4)
            {
                setShow.contentView.setBackgroundResource(R.drawable.green);
                setShow.dateView.setBackgroundResource(R.drawable.greentop);
            }
            if(bg_id==5)
            {
                setShow.contentView.setBackgroundResource(R.drawable.red);
                setShow.dateView.setBackgroundResource(R.drawable.redtop);
            }

            setShow.contentView.setText("   " + str);
            setShow.dateView.setText(dateStr);
            setShow.showButtonWrite = (Button) arg1
                    .findViewById(R.id.smallbutton1);
            setShow.showButtonDelete = (Button) arg1
                    .findViewById(R.id.smallbutton2);
            setShow.showButtonWrite.setOnClickListener(new WriteButtonListener(
                    arg0));
            setShow.showButtonDelete
                    .setOnClickListener(new DeleteButtonListener(arg0));
        }
        else {
            arg1 = inflater.inflate(R.layout.style, arg2, false);
            setShow.cContentView = (TextViewLine) arg1
                    .findViewById(R.id.changecontentview);
            setShow.cDateView = (TextView) arg1
                    .findViewById(R.id.changedateview);
            String str = (String) list.get(arg0).get("contentItem");
            String dateStr = (String) list.get(arg0).get("dateItem");
            bg_id= (Integer) list.get(arg0).get("backgroundItem");

            if(bg_id==1)
            {
                setShow.cContentView.setBackgroundResource(R.drawable.yellow);
                setShow.cDateView.setBackgroundResource(R.drawable.yellowtop);
            }
            if(bg_id==2)
            {
                setShow.cContentView.setBackgroundResource(R.drawable.blue);
                setShow.cDateView.setBackgroundResource(R.drawable.bluetop);
            }
            if(bg_id==3)
            {
                setShow.cContentView.setBackgroundResource(R.drawable.white);
                setShow.cDateView.setBackgroundResource(R.drawable.whitetop);
            }
            if(bg_id==4)
            {
                setShow.cContentView.setBackgroundResource(R.drawable.green);
                setShow.cDateView.setBackgroundResource(R.drawable.greentop);
            }
            if(bg_id==5)
            {
                setShow.cContentView.setBackgroundResource(R.drawable.red);
                setShow.cDateView.setBackgroundResource(R.drawable.redtop);
            }

            setShow.cContentView.setText("" + str);
            setShow.cDateView.setText(dateStr);
            setShow.styleButtonWrite = (Button) arg1
                    .findViewById(R.id.stylebutton1);
            setShow.styleButtonWrite
                    .setOnClickListener(new WriteButtonListener(arg0));
            setShow.styleButtonDelete = (Button) arg1
                    .findViewById(R.id.stylebutton2);
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


            Bundle b = new Bundle();
            b.putString("contentItem",
                    (String) list.get(position).get("contentItem"));
            b.putString("dateItem", (String) list.get(position).get("dateItem"));
            b.putString("idItem", (String) list.get(position).get("idItem"));
            b.putInt("backgroundItem",(Integer) list.get(position).get("backgroundItem"));
            //Log.d("bg",String.valueOf(bg_id));
            Intent intent = new Intent((MainActivity) context,
                    EditActivity.class);
            intent.putExtras(b);
            ((MainActivity) context).startActivity(intent);


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
                            notepad.setid((String) list.get(position).get(
                                    "idItem"));
                            change.delete(dataBase, notepad);
                            ((MainActivity) context).showUpdate();
                            // a.showUpdate();

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
