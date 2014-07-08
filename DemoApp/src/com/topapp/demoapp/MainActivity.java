package com.topapp.demoapp;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;

/**
 * 登录界面
 * 
 * @author Lolzorz
 * 
 */
public class MainActivity extends Activity {

	private EditText et_user;
	private EditText et_psw;
	private Button btn_login;
	private Button btn_new;
	private String login_info;

	private String user_name;
	private String password;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		et_user = (EditText) findViewById(R.id.et_user);
		et_psw = (EditText) findViewById(R.id.et_psw);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_new = (Button) findViewById(R.id.btn_new);

		et_user.setText(sp.getString("USER_NAME", ""));
		et_psw.setText(sp.getString("PASSWORD", ""));

		btn_login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				MainActivity.this.user_name = et_user.getText().toString();
				MainActivity.this.password = et_psw.getText().toString();
				MainActivity.this.login(user_name, password);
			}
		});

		btn_new.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, RegActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
			}
		});

	}

	public void login(String id, String password) {
		AsyncHttpClient client = new AsyncHttpClient();
		String url = "http://192.168.23.1:8080/test/deallogin";
		RequestParams param = new RequestParams();
		param.put("id", id);
		param.put("password", password);

		client.get(url, param, new JsonHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_LONG)
						.show();
			}

			// 返回JSONArray对象 | JSONObject对象
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (statusCode == 200) {
					try {
						MainActivity.this.login_info = response.getString(
								"information").trim();
						Toast.makeText(MainActivity.this,
								MainActivity.this.login_info, Toast.LENGTH_LONG)
								.show();
						if (MainActivity.this.login_info.equals("success")) {
							MainActivity.this.saveInfo(
									MainActivity.this.user_name,
									MainActivity.this.password);
						}
					} catch (JSONException e) {
						Toast.makeText(MainActivity.this, "error",
								Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void saveInfo(String user, String psw) {
		Editor editor = sp.edit();
		editor.putString("USER_NAME", user);
		editor.putString("PASSWORD", psw);
		editor.commit();
	}
}