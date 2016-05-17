package com.vlife.mymemo.mainactivity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.administrator.mymemo.R;
import com.vlife.mymemo.adapter.Notepad;
import com.vlife.mymemo.adapter.NotepadAdapter;
import com.vlife.mymemo.edit.EditActivity;
import com.vlife.mymemo.sqlite.changeSqlite;
import com.vlife.mymemo.sqlite.sqliteHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 主页面Activity
 *
 */

public class MainActivity extends Activity {
    public NotepadAdapter adapter;
    public ArrayList<Map<String, Object>> itemList;
    public ListView listView;
    public int number;//item计数
    public Button numberButton;//显示item数量
    public Button memoAddButton;//添加item按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.numberButton = ((Button) findViewById(R.id.number_button));
        this.memoAddButton = ((Button) findViewById(R.id.memo_add_button));
        this.listView = ((ListView) findViewById(R.id.memo_list_show));
        this.listView.setDivider(null);
        this.listView.setOnItemClickListener(new ItemClick());
        this.memoAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        EditActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        showUpdate();
    }

    //更新显示
    public void showUpdate() {
        this.itemList = new ArrayList<>();
        SQLiteDatabase localSqLiteDatabase = new sqliteHelper(this, null, null, 1).getReadableDatabase();
        for (Notepad localNotepad : new changeSqlite().query(
                localSqLiteDatabase)) {
            HashMap<String, Object> localHashMap = new HashMap<>();
            localHashMap.put("titleItem", localNotepad.getTitle());
            localHashMap.put("dateItem", localNotepad.getDate());
            localHashMap.put("contentItem", localNotepad.getContent());
            localHashMap.put("idItem", localNotepad.getId());
            localHashMap.put("backgroundItem", localNotepad.getBackground());
            localHashMap.put("alarmItem",localNotepad.getAlarm());
            this.itemList.add(localHashMap);
            this.number = this.itemList.size();
            this.numberButton.setText(String.format("%1$(2d", -this.number));
        }
        Collections.reverse(this.itemList);
        this.adapter = new NotepadAdapter(this, this.itemList);
        this.listView.setAdapter(this.adapter);
        if (this.itemList.size()==0) {
            number=0;
            this.numberButton.setText(String.format("%1$(2d", -this.number));
        }
    }

    //item点击事件响应
    class ItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> paramAdapterView,
                                View paramView, int paramInt, long paramLong) {
            Bundle bundle=new Bundle();
            Map<String, Object> localMap = MainActivity.this.itemList
                    .get(paramInt);
            //设置要传递的参数
            bundle.putString("contentItem", (String)localMap.get("contentItem"));
            bundle.putString("dateItem", (String)localMap.get("dateItem"));
            bundle.putString("idItem", (String)localMap.get("idItem"));
            bundle.putInt("backgroundItem",(Integer)localMap.get("backgroundItem"));
            bundle.putInt("alarmItem",(Integer) localMap.get("alarmItem"));
            Intent intent=new Intent(MainActivity.this,EditActivity.class);
            intent.putExtras(bundle);
            MainActivity.this.startActivity(intent);
        }
    }

}
