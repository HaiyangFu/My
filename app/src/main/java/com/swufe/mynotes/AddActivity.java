package com.swufe.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "AddActivity";

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        editText = findViewById(R.id.add_inp);
        Button btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);    //设置监听
        
    }
    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: aaaaaaaaaaaaaa");
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        String add = editText.getText().toString() ;
        bundle.putString("add",add);
        intent.putExtras(bundle);

//        intent.putExtra("add",add);
        Log.i(TAG, "onCreate: add"+ add);
        setResult(2,intent);
        //返回到调用页面
        finish();
    }
}
