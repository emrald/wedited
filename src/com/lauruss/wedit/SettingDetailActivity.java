package com.lauruss.wedit;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class SettingDetailActivity extends Activity{

	public static String FALG_PAGE_ID = "wedit_page_id";
	int pageId = 0;
	Button btnBack;
	TextView tvHeader;
	ProgressDialog pd;

	LinearLayout llShortText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settingdetail);

		btnBack = (Button)findViewById(R.id.settingdetail_btn_back);
		tvHeader = (TextView)findViewById(R.id.settingdetail_tv_header);
		llShortText = (LinearLayout)findViewById(R.id.settingdetail_ll_shortText);

		Bundle bn = getIntent().getExtras();
		pageId = bn.getInt(FALG_PAGE_ID);

		GetSinglePageData exe = new GetSinglePageData();
		exe.execute();

		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

	}
	class GetSinglePageData extends AsyncTask<Void, Void, String>{

		@Override
		protected void onPreExecute() {
			//	pd = ProgressDialog.show(SettingDetailActivity.this, "", "טוען נתונים");
			pd = ProgressDialog.show(SettingDetailActivity.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"pages?action=singlepage");
				HttpParams httpParameters = httppost.getParams();

				int timeoutConnection = 10000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);

				int timeoutSocket = 10000;
				HttpConnectionParams
				.setSoTimeout(httpParameters, timeoutSocket);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("pageid", String.valueOf(pageId)));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));

				HttpResponse response = httpclient.execute(httppost);
				String responseThis = EntityUtils.toString(response.getEntity()).trim();

				return responseThis;

			} catch (Exception e) {
				e.printStackTrace();
				return null;	
			}

		}
		@Override
		protected void onPostExecute(String result) {
			if(result != null && !result.trim().equalsIgnoreCase("")){
				try {
					JSONObject jObj = new JSONObject(result);
					
					tvHeader.setText(jObj.getString("title"));
					TextView tvShortText = new TextView(SettingDetailActivity.this);
					tvShortText.setText(Html.fromHtml(jObj.getString("contenthtml")));
					//Toast.makeText(getApplicationContext(), jObj.getString("title"), Toast.LENGTH_SHORT).show();
					llShortText.addView(tvShortText);

					String url = jObj.getString("image1");
					if(url!= null && !url.trim().equalsIgnoreCase("")){
						LayoutParams ll = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
						ImageView iv = new ImageView(SettingDetailActivity.this);
						llShortText.addView(iv,ll);
						new DownloadImageTask(iv)
						.execute(url);

					}
					
					TextView tvContent = new TextView(SettingDetailActivity.this);
					tvContent.setText(jObj.getString("shorttext"));

					llShortText.addView(tvContent);
					TextView tvDescription = new TextView(SettingDetailActivity.this);
					tvDescription.setText(jObj.getString("description"));
					llShortText.addView(tvDescription);


					url = jObj.getString("image2");
					if(url!= null && !url.trim().equalsIgnoreCase("")){
						ImageView iv = new ImageView(SettingDetailActivity.this);
						new DownloadImageTask(iv)
						.execute(url);
						llShortText.addView(iv);
					}
					url = jObj.getString("image3");
					if(url!= null && !url.trim().equalsIgnoreCase("")){
						ImageView iv = new ImageView(SettingDetailActivity.this);
						new DownloadImageTask(iv)
						.execute(url);
						llShortText.addView(iv);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			pd.dismiss();
			super.onPostExecute(result);
		}

	}
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = "http://wedit.dooble.mobi/"+urls[0];
			Bitmap mIcon11 = null;
			try {
				BitmapFactory.Options bmOptions = new BitmapFactory.Options();
				bmOptions.inSampleSize = 1;
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in,null,bmOptions);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}
}
