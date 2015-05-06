package com.linkin.mtv.digi.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.linkin.mtv.digi.PreferenceManager;
import com.linkin.mtv.digi.R;

/**
 * Created by Anik on 3/21/2015.
 */
public class AuthActivity extends Activity {

	private PreferenceManager manager;
	private ProgressDialog progress;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.auth_view);
		manager = new PreferenceManager(getApplicationContext());

		WebView myWebView = (WebView) findViewById(R.id.login_view);
		myWebView.setWebViewClient(new WebViewClient() {

			public boolean shouldOverrideUrlLoading(final WebView view,
					final String url) {

				// on successful login, we'll get a redirect to
				// http://stagingtv.universetv.net/digi/result.php
				if (url.startsWith("http://stagingtv.universetv.net/digi/result.php")) {

					showSpinner();
					StringTokenizer srt = new StringTokenizer(url, "=");
					Log.i("auth", "srt = " + srt);
					srt.nextToken();

					manager.setAccessToken(srt.nextToken());
					manager.setLoggedIn(true);

					JsonObjectRequest request = new JsonObjectRequest(
							Request.Method.GET,
							"https://connect.staging.telenordigital.com/oauth/userinfo",
							null, new Response.Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) {
									if (response.has("phone_number")) {
										try {
											manager.setPhoneNumber(response
													.getString("phone_number"));
											manager.setUserID(response
													.getString("sub"));
											checkRights(manager.getUserId());

										} catch (JSONException e) {
											Toast.makeText(
													getApplicationContext(),
													"Check internet connection",
													Toast.LENGTH_SHORT).show();
										}
									}
								}
							}, new Response.ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError error) {
									progress.cancel();
									finish(); // start an activity or something
												// else appropriate view .. or
												// whatever.
								}
							}) {
						@Override
						public Map<String, String> getHeaders()
								throws AuthFailureError {
							HashMap<String, String> headers = new HashMap<String, String>();
							headers.put("Content-Type", "application/json");
							headers.put("Authorization",
									"Bearer " + manager.getAccessToken());
							headers.put("Accept", "application/json");
							return headers;
						}
					};

					RequestQueue queue = Volley
							.newRequestQueue(getApplicationContext());
					queue.add(request);

				}

				return false;
			}

		});

		myWebView
				.loadUrl("https://connect.staging.telenordigital.com/oauth/authorize?client_id=hakjapan-banglatv-android&redirect_uri=http://stagingtv.universetv.net/digi/success.php&scope=phone+openid+id.user.right.read+payment.transactions.read+payment.transactions.write+payment.agreements.read+payment.agreements.write&response_type=code&locale=bn-BD");
		myWebView.getSettings().setJavaScriptEnabled(true);
	}

	private void checkRights(String userID) {

		String url = "https://stagingapi.comoyo.com/id/users/" + userID
				+ "/rights?active=now";
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
				url, null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// "right": [],
						// "rights": []
						
						

						if (response.has("rights")) {
							try {
								manager.setHasRights(response.getJSONArray(
										"right").length() != 0);

								if (manager.hasRights()) {
									JSONArray rights = response
											.getJSONArray("rights");
									JSONObject right = rights.getJSONObject(0);
									String interval = right
											.getString("timeInterval");

									StringTokenizer str = new StringTokenizer(
											interval, "/\\");
									// the second token is the end date of
									// validation
									str.nextToken();
									manager.setValidity(str.nextToken());
									manager.setPurchased(true);
									manager.setHasRights(true);
									manager.setLoggedIn(true);
									progress.cancel();
									finish();

								} else {
									startActivity(new Intent(AuthActivity.this,
											PaymentActivity.class));
									progress.cancel();
									finish();
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

					}
				}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Content-Type", "application/json");
				headers.put("Authorization",
						"Bearer " + manager.getAccessToken());
				headers.put("Accept", "application/json");
				return headers;
			}
		};

		RequestQueue queue = Volley.newRequestQueue(this);
		queue.add(request);
	}

	private interface RightListener {

		public void rightUpdate(boolean hasRight);
	}

	private void showSpinner() {
		progress = new ProgressDialog(this);
		progress.setMessage("Please Wait");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setIndeterminate(true);
		progress.show();
		progress.setCanceledOnTouchOutside(false);

		final Thread t = new Thread() {

			@Override
			public void run() {

				int jumpTime = 0;
				while (true) {
					try {
						sleep(100);
						jumpTime += 5;
						progress.setProgress(jumpTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		};
		t.start();

	}
}
