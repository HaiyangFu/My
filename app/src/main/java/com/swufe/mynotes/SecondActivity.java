package com.swufe.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemLongClickListener {
    private String TAG = "SecondActivity";
    EditText inp;
    List<String> list = new ArrayList<String>();
    ListAdapter adapter; // 适配器
    ListView lv;
    String content ;
    Button button;
    String course=" ";
    String save ;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        //从上个页面取出数据
        course = getIntent().getStringExtra("course");
        Log.i(TAG, "onCreate: course = " + course);
        ((TextView) findViewById(R.id.output)).setText(course);
        inp = findViewById(R.id.input);
        content = inp.getText().toString();
        button = findViewById(R.id.btn_config);
        button.setOnClickListener(this);
        lv = findViewById(R.id.secondLv);
        lv.setOnItemLongClickListener(this);

         //数据库
//        DBManager dbManager = new DBManager(SecondActivity.this);
//        for(CourseItem courseItem : dbManager.listAll()){
//            list.add(courseItem.getContent());
//        }
        //sp取出数据
        sharedPreferences = getSharedPreferences("mylist", Activity.MODE_PRIVATE);
        String listGson = sharedPreferences.getString("content","");
        Log.i(TAG, "onCreate: listGson"+listGson);
        if(!listGson.equals("")) {
            Gson gson = new Gson();
            list = gson.fromJson(listGson, new TypeToken<List<String>>() {
            }.getType());
            Log.i(TAG, "onCreate: sp保存数据"+content);

        }
        adapter = new ArrayAdapter<String>(SecondActivity.this, android.R.layout.simple_list_item_1,list);
        lv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        Intent intent = getIntent();
        content = inp.getText().toString();
        Log.i(TAG, "onClick: SecondActivity的按钮事件处理" + String.valueOf(content));
        list.add(String.valueOf(content));
        lv.setAdapter(adapter);
        inp.setText("");
//        DBManager dbManager = new DBManager(SecondActivity.this);
//        list.add(String.valueOf(inp.getText()));
//        for(CourseItem courseItem : dbManager.listAll()){
//            list.add(courseItem.getContent());
//        }
          //数据保存
        Log.i(TAG, "onClick: list"+list.toString());
        sharedPreferences = this.getSharedPreferences("mylist", Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(list);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("content", strJson);
        editor.commit();
        Toast.makeText(SecondActivity.this, "课表已保存", Toast.LENGTH_SHORT);


    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
        Log.i(TAG, "onItemLongClick: 长按事件处理........................" + position);
        //删除操作
        //构造对话框进行确认操作
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请确认是否删除当前数据").setPositiveButton("是",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "onClick: 对话框事件处理");
                list.remove(position);
                lv.setAdapter(adapter);


            }
        }).setNegativeButton("否",null);
        builder.create().show();
        Log.i(TAG, "onClick: 删除监听list"+list);


        //sp内进行数据删除
        sharedPreferences = this.getSharedPreferences("mylist", Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(list);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(content);
        editor.commit();

        Log.i(TAG, "onClick: 删除监听list"+list);
        return true;
    }


}
