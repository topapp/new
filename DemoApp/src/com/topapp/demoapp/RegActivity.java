package com.topapp.demoapp;

import org.json.JSONException;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONObject;
import org.apache.http.Header;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;

/**
 * 注册界面
 * 
 * @author Lolzorz
 * 
 */
public class RegActivity extends Activity {
	private Button btn_reg;
	private Button btn_back;
	private EditText et_phone_number;
	private EditText et_psw;
	private EditText et_psw_again;
	private EditText et_user;
	private EditText et_mail;
	private RadioGroup gendergroup;
	private String gender_Select = "";
	private String regInfo;
	private SharedPreferences sp;
	private String phone_number;
	private String psw_correct;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.reg_layout);
		sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		btn_reg = (Button) findViewById(R.id.btn_reg);
		btn_back = (Button) findViewById(R.id.btn_back);
		et_phone_number = (EditText) findViewById(R.id.et_phone_number);
		et_psw = (EditText) findViewById(R.id.et_psw);
		et_psw_again = (EditText) findViewById(R.id.et_psw_again);
		et_user = (EditText) findViewById(R.id.et_user);
		et_mail = (EditText) findViewById(R.id.et_mail);
		gendergroup = (RadioGroup) findViewById(R.id.gendergroup);

		gendergroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// 根据ID判断选择的按钮
				if (checkedId == R.id.girl) {
					RegActivity.this.gender_Select = "女";
				} else {
					RegActivity.this.gender_Select = "男";
				}
			}
		});

		btn_reg.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				phone_number = et_phone_number.getText().toString().trim();
				String psw = et_psw.getText().toString().trim();
				String psw_again = et_psw_again.getText().toString().trim();
				String user_name = et_user.getText().toString().trim();
				String mail_add = et_mail.getText().toString().trim();
				if (!checkInfo(phone_number, psw, psw_again, user_name,
						mail_add)) {
					Toast.makeText(RegActivity.this,
							gender_Select + "注册失败,请检查格式", Toast.LENGTH_LONG)
							.show();
				} else {
					RegActivity.this.register(phone_number, psw, mail_add,
							gender_Select, user_name);
				}
			}
		});

		btn_back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				Intent intent = new Intent();
				intent.setClass(RegActivity.this, MainActivity.class);
				startActivity(intent);
				RegActivity.this.finish();
			}
		});
	}

	/**
	 * 检查注册信息是否符合规范
	 * 
	 * @param phone_n
	 *            手机
	 * @param psword
	 *            密码
	 * @param psword_a
	 *            密码确认
	 * @param user_n
	 *            昵称
	 * @param mail_a
	 *            邮箱
	 * @return
	 */
	private boolean checkInfo(String phone_n, String psword, String psword_a,
			String user_n, String mail_a) {
		if (this.gender_Select != "男" && this.gender_Select != "女") {
			return false;
		}
		if (!phone_n.matches("\\d+")) {
			return false;
		}
		if (!psword.equals(psword_a)) {
			return false;
		}
		this.psw_correct = psword;
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		if (!mail_a.matches(regex)) {
			return false;
		}
		return true;
	}

	public void register(String id, String password, String email, String sex,
			String name) {
		AsyncHttpClient client = new AsyncHttpClient();
		String url = "http://192.168.23.1:8080/test/dealregister";
		RequestParams param = new RequestParams();
		param.put("id", id);
		param.put("password", password);
		param.put("sex", sex);
		param.put("name", name);
		param.put("email", email);

		client.get(url, param, new JsonHttpResponseHandler() {
			// 返回JSONArray对象 | JSONObject对象
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (statusCode == 200) {
					try {
						RegActivity.this.regInfo = response.getString(
								"information").trim();
						Toast.makeText(RegActivity.this,
								RegActivity.this.regInfo, Toast.LENGTH_LONG)
								.show();
						if (RegActivity.this.regInfo.equals("success")) {
							RegActivity.this.saveInfo(
									RegActivity.this.phone_number,
									RegActivity.this.psw_correct);
							Intent intent = new Intent();
							intent.setClass(RegActivity.this,
									MainActivity.class);
							startActivity(intent);
							RegActivity.this.finish();
						}
					} catch (JSONException e) {
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
