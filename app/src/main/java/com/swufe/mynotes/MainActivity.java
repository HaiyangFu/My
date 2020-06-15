package com.swufe.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Runnable,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener, View.OnClickListener {
    private String TAG = "MainActivity";
    Handler handler;
    ListView listView;
    ListAdapter adapter;
    Button btn;
    String course;
    SharedPreferences sp;
    String save;
    List<String> list2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        btn = findViewById(R.id.btn_clear);

         //sp取出数据
        Log.i(TAG, "onCreate: content ................................."+course);
        sp = getSharedPreferences("list", Activity.MODE_PRIVATE);
        String listGson = sp.getString("course","");
        Log.i(TAG, "onCreate: listGson"+listGson);
        if(!listGson.equals("")) {
            Gson gson = new Gson();
            list2 = gson.fromJson(listGson, new TypeToken<List<String>>() {
            }.getType());
            Log.i(TAG, "onCreate:sp保存数据"+course);

        }

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        btn.setOnClickListener(this);

        Thread t = new Thread(this);
        t.start();
        Log.i(TAG, "onCreate: 开启线程................");
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 5) {

                    list2 = (List<String>) msg.obj;
                    save = list2.toString();
                    adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, list2);
                    listView.setAdapter(adapter);
                    Log.i(TAG, "handleMessage: list2" + list2);
                }
                super.handleMessage(msg);
            }
        };

        //数据保存
        sp = this.getSharedPreferences("list", Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(list2);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("course", strJson);
        editor.commit();
        Toast.makeText(MainActivity.this, "课表已保存", Toast.LENGTH_SHORT);
    }

    @Override
    public void run() {
            List<String> list = new ArrayList<String>();
            Bundle bundle = new Bundle();
            Log.i("thread", "run.....");
            try {
                Document doc = Jsoup.connect("http://10.9.10.210:81/(31egus3owkjqzq555nqesf45)/xs_main.aspx?xh=41811018").get();
                Log.i(TAG, "run: " + doc.title());
                Elements tables = doc.getElementsByClass("nav");
                Element tb = tables.get(0);
                Elements tds = tb.getElementsByTag("li");

                for (int i = 10; i < 20; i++) {
                    Element td1 = tds.get(i);
                    Log.i(TAG, "run: text = " + i + td1.text());
                    course = td1.text().toString();
                    list.add(td1.text().toString());
                    Log.i("for循环输出", String.valueOf(course));
                }

            } catch (MalformedURLException e) {
                Log.e("www", e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("www", e.toString());
                e.printStackTrace();
            }
            Message msg = handler.obtainMessage(5);
            msg.obj = list;
            handler.sendMessage(msg);
            Log.i("thread", "sendMessage.....");
        }


    @Override
    public void onItemClick(AdapterView<?> listv, View view, int position, long id) {
        Log.i(TAG, "onItemClick: parent=" + listv);
        Log.i(TAG, "onItemClick: view=" + view);
        Log.i(TAG, "onItemClick: position=" + position);
        Log.i(TAG, "onItemClick: id=" + id);

        //获取数据
        String listStr = (String) listv.getItemAtPosition(position);

        //打开新的页面，传入参数
        Intent course = new Intent(this, SecondActivity.class);
        course.putExtra("course", listStr);
        startActivity(course);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            //点击后的事件处理
            Intent add = new Intent(this, AddActivity.class);
            Log.i(TAG, "onItemClick: 1234564684864");
            startActivityForResult(add, 1);
        }
        return super.onOptionsItemSelected(item);
    }

           @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 2) {
            Bundle bundle = data.getExtras();
            Log.i(TAG, "onActivityResult: 回来了回来了回来了。。。。。。。。。。。");
            String task = bundle.getString("add", "啥都没有");
            Log.i(TAG, "onActivityResult: task..............." + task);
            list2.add(task);
            listView.setAdapter(adapter);

            //数据保存
            Log.i(TAG, "onClick: list"+list2.toString());
            sp = this.getSharedPreferences("list", Activity.MODE_PRIVATE);
            Gson gson = new Gson();
            //转换成json数据，再保存
            String strJson = gson.toJson(list2);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("course", strJson);
            editor.commit();
            Toast.makeText(MainActivity.this, "课表已保存", Toast.LENGTH_SHORT);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
        Log.i(TAG, "onItemLongClick: 长按事件处理........................" + position);
//        //删除操作
//        //构造对话框进行确认操作
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请确认是否删除当前数据").setPositiveButton("是", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "onClick: 对话框事件处理");
                list2.remove(position);
                listView.setAdapter(adapter);
            }
        }).setNegativeButton("否", null);
        builder.create().show();


        //sp内进行数据删除
        sp = this.getSharedPreferences("list", Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(list2);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("course");
        editor.putString("course", strJson);
        editor.commit();
        Toast.makeText(MainActivity.this, "课表已保存", Toast.LENGTH_SHORT);
        Log.i(TAG, "onClick: splalallalallalaal"+course);
        Log.i(TAG, "onClick: spppppppppppp"+sp.toString());
        return true;
    }
    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: clear.............................");
        list2.clear();
        listView.setAdapter(adapter);
        //sp内进行数据删除
        sp = this.getSharedPreferences("list", Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(list2);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.putString("course", strJson);
        editor.commit();
        Toast.makeText(MainActivity.this, "课表已清除", Toast.LENGTH_SHORT);

    }
}






