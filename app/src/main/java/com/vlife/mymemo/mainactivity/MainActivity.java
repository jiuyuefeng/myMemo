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
import com.vlife.mymemo.sqlite.ChangeSqlite;
import com.vlife.mymemo.adapter.Notepad;
import com.vlife.mymemo.adapter.NotepadAdapter;
import com.vlife.mymemo.sqlite.SqliteHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends Activity {

    public String EXPANDED = "EXPANDED";
    public NotepadAdapter adapter;
    public ArrayList<Map<String, Object>> itemList;
    public ListView listView;
    public int number;
    public Button numberButton;
    public Button memoAddButton;
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

    public void showUpdate() {

        this.itemList = new ArrayList<Map<String, Object>>();
        SQLiteDatabase localSqLiteDatabase = new SqliteHelper(this, null, null, 1).getReadableDatabase();
        Iterator<Notepad> localIterator = new ChangeSqlite().query(
                localSqLiteDatabase).iterator();
        while (true) {
            if (!localIterator.hasNext()) {
                Collections.reverse(this.itemList);
                this.adapter = new NotepadAdapter(this, this.itemList);
                this.listView.setAdapter(this.adapter);
                if (this.itemList.size()==0) {
                    number=0;
                    this.numberButton.setText("(" + this.number + ")");
                }
                return;
            }
            Notepad localNotepad = localIterator.next();
            HashMap<String, Object> localHashMap = new HashMap<>();
            localHashMap.put("titleItem", localNotepad.getTitle());
            localHashMap.put("dateItem", localNotepad.getDate());
            localHashMap.put("contentItem", localNotepad.getContent());
            localHashMap.put("idItem", localNotepad.getId());
            localHashMap.put("backgroundItem",localNotepad.getBackground());
            localHashMap.put("EXPANDED", true);
            this.itemList.add(localHashMap);
            this.number = this.itemList.size();
            this.numberButton.setText("(" + this.number + ")");
        }

    }


    class ItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> paramAdapterView,
                                View paramView, int paramInt, long paramLong) {
            //System.out.println("item----------click");

            Bundle bundle=new Bundle();
            Map<String, Object> localMap = MainActivity.this.itemList
                    .get(paramInt);
            bundle.putString("contentItem", (String)localMap.get("contentItem"));
            bundle.putString("dateItem", (String)localMap.get("dateItem"));
            bundle.putString("idItem", (String)localMap.get("idItem"));
            bundle.putInt("backgroundItem",(Integer)localMap.get("backgroundItem"));

            Intent intent=new Intent(MainActivity.this,EditActivity.class);
            intent.putExtras(bundle);
            MainActivity.this.startActivity(intent);
        }

    }

}
