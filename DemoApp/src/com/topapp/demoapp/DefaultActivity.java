package com.topapp.demoapp;

import java.io.File;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 日记界面
 * 
 * @author Lolzorz
 * 
 */
public class DefaultActivity extends Activity {
	private LinearLayout content;
	private LinearLayout ll_note;
	private LinearLayout ll_send;
	private LinearLayout ll_empty;
	private String sid;
	//==================================测试按钮 可去除============================================================
	private Button btn_testt;
	private Button btn_testq;
	//==================================测试按钮 可去除============================================================
	
	private Button btn_write;
	private int pageStatue = 100;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.default_layout);
		sid = getIntent().getExtras().getString("id");
		//==================================测试按钮 可去除========================================================
		btn_testt = (Button) findViewById(R.id.btn_testt);
		btn_testq = (Button) findViewById(R.id.btn_testq);
		//==================================测试按钮 可去除========================================================
		
		
		btn_write = (Button) findViewById(R.id.btn_write);
		content = (LinearLayout) findViewById(R.id.ll_content);
		ll_note = (LinearLayout) findViewById(R.id.ll_note);
		ll_send = (LinearLayout) findViewById(R.id.ll_send);
		ll_empty = (LinearLayout) findViewById(R.id.ll_empty);
		ll_send.setVisibility(View.GONE);
		ll_empty.setVisibility(View.GONE);

		for (int i = 0; i < 50; i++) {
			TextView btn_tmp = new TextView(this);
			btn_tmp.setText("1月"+i+"日");
			TextView btn_tmp2 = new TextView(this);
			btn_tmp2.setText("aosdifoasdfansofdausndoiufnaosiufdnaosiufdaoisuuaosidnufaosiufansoifunsaof");
			btn_tmp2.setTextSize(20);
			content.addView(btn_tmp);
			content.addView(btn_tmp2);
		}
		btn_testt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
		
		btn_write.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("statue", "note");
				intent.putExtra("id", DefaultActivity.this.sid);
				intent.setClass(DefaultActivity.this, EditActivity.class);
				startActivity(intent);
				DefaultActivity.this.finish();
			}
		});
		
		btn_testq.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(pageStatue == 100) {
				// TODO Auto-generated method stub
				ll_note.setVisibility(View.GONE);
				ll_send.setVisibility(View.VISIBLE);
				pageStatue = 010;
				} else if(pageStatue == 010) {
					ll_send.setVisibility(View.GONE);
					ll_note.setVisibility(View.VISIBLE);
					pageStatue = 100;
				}
			}
		});
		
	}

	
}
