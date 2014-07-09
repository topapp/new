package com.topapp.demoapp;

import java.io.File;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends Activity {
	private Button btn_backToDefault;
	private Button btn_editFinish;
	private Button btn_photo;
	private Button btn_addImg;
	private EditText et_edit;
	private TextView tv_picNum;
	private String sid;
	private String pics[] = new String[3];
	private int picNum = 0;
	private final String IMAGE_TYPE = "image/*";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit_layout);
		this.sid = getIntent().getExtras().getString("id");
		btn_backToDefault = (Button) findViewById(R.id.btn_backToDefault);
		btn_editFinish = (Button) findViewById(R.id.btn_editFinish);
		btn_photo = (Button) findViewById(R.id.btn_photo);
		btn_addImg = (Button) findViewById(R.id.btn_addImg);
		et_edit = (EditText) findViewById(R.id.et_edit);
		tv_picNum = (TextView) findViewById(R.id.tv_picNum);

		et_edit.setText("麻口麻口捞逼捞逼");

		btn_addImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (EditActivity.this.picNum < 3) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType(IMAGE_TYPE);
					startActivityForResult(intent, 2);
				} else {
					Toast.makeText(EditActivity.this, "只能上传3张图片",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		btn_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (EditActivity.this.picNum < 3) {
					saveFullImage();
				} else {
					Toast.makeText(EditActivity.this, "只能上传3张图片",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		btn_backToDefault.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("statue", "back");
				intent.setClass(EditActivity.this, DefaultActivity.class);
				startActivity(intent);
				EditActivity.this.finish();
			}
		});

		btn_editFinish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String content = EditActivity.this.et_edit.getText().toString();
				EditActivity.this.upLoad(content, EditActivity.this.pics);
			}
		});
	}

	private void saveFullImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 文件夹aaaa
		String path = Environment.getExternalStorageDirectory().toString()
				+ "/tmppic";
		File path1 = new File(path);
		if (!path1.exists()) {
			path1.mkdirs();
		}
		String filename = "/" + path + System.currentTimeMillis() + ".jpg";
		File file = new File(filename);
		Uri mOutPutFileUri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);
		this.pics[this.picNum] = filename;
		this.picNum++;
		this.tv_picNum.setText("还可以插入"+(3-this.picNum)+"张图片\n一共可以输入140字符");
		startActivityForResult(intent, 1);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (data != null) {
				if (data.hasExtra("data")) {
					// 处理缩略图
				}
			} else {
				// 处理mOutPutFileUri中的完整图像
			}
		}
		if (requestCode == 2) {
			Uri originalUri = data.getData();
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = managedQuery(originalUri, proj, null, null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String path = cursor.getString(column_index);
			this.pics[this.picNum] = path;
			this.picNum++;
			this.tv_picNum.setText("还可以插入"+(3-this.picNum)+"张图片\n一共可以输入140字符");
		}
	}

	/**
	 * 上传文件
	 * 
	 * @param path
	 */
	public void upLoad(String content, String[] path) {
		File[] files = new File[path.length];
		AsyncHttpClient client = new AsyncHttpClient();

		String url = "http://192.168.23.1:8080/test/UploadDiary";

		// String path = editText.getText().toString().trim();
		// File file = new File(path);
		RequestParams params = new RequestParams();
		try {
			for (int i = 0; i < EditActivity.this.picNum; i++) {
				files[i] = new File(path[i]);
				params.put("profile_picture" + i, files[i]);
			}
			params.put("id", EditActivity.this.sid);
			params.put("diary", content);
			params.put("picturenum", this.picNum+"");
		} catch (Exception e) {
			e.printStackTrace();
		}

		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				Toast.makeText(EditActivity.this, "上传成功", Toast.LENGTH_SHORT)
						.show();

				Intent intent = new Intent();
				intent.putExtra("statue", "send");
				intent.putExtra("id", EditActivity.this.sid);
				intent.setClass(EditActivity.this, DefaultActivity.class);
				startActivity(intent);
				EditActivity.this.finish();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				Toast.makeText(EditActivity.this, "上传失败", Toast.LENGTH_SHORT)
						.show();

			}
		});
	}
}
