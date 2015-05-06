package com.linkin.mtv.digi.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.linkin.mtv.digi.NumberConvertor;
import com.linkin.mtv.digi.PreferenceManager;
import com.linkin.mtv.digi.R;

/**
 * Created by sonnet on 3/27/15.
 */
@SuppressLint("SimpleDateFormat")
public class PaymentActivity extends Activity {

	private PreferenceManager manager;
	private ProgressDialog progress;
	private WebView webView;

	private boolean inPayment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auth_view);
		manager = new PreferenceManager(this);
		inPayment = false;
		webView = (WebView) findViewById(R.id.login_view);
		showSpinner();
		checkRights(manager.getUserId());

	}

	public void loadWebView(String paymentURL) {
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				if (url.equals("http://stagingtv.universetv.net/digi/payment.php?status=success")) {
					// manager.setPurchased(true);
					inPayment = true;
					checkRights(manager.getUserId());
				} else if (url
						.equals("http://stagingtv.universetv.net/digi/payment.php?status=failed")) {
					showFailedPage();
				}

				return false;
			}
		});

		webView.loadUrl(paymentURL);

	}

	private void showFailedPage() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.layout_success);
		layout.setVisibility(View.VISIBLE);
		webView.setVisibility(View.GONE);

		ImageView banner = (ImageView) findViewById(R.id.banner);
		banner.setVisibility(View.GONE);

		TextView ok = (TextView) layout.findViewById(R.id.ok);
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void checkRights(String userID) {

		String url = "https://stagingapi.comoyo.com/id/users/" + userID
				+ "/rights?active=now";
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
				url, null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						if (response.has("rights")) {
							try {

								manager.setHasRights(response.getJSONArray(
										"rights").length() != 0);

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

									if (inPayment) {
										showSuccessPage();
									}

								} else {
									sendPostRequest();
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

	private void showSuccessPage() {
		try {
			LinearLayout layout = (LinearLayout) findViewById(R.id.layout_success);
			layout.setVisibility(View.VISIBLE);
			webView.setVisibility(View.GONE);

			ImageView banner = (ImageView) findViewById(R.id.banner);
			banner.setVisibility(View.GONE);

			TextView meyad = (TextView) layout.findViewById(R.id.meyad);

			SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");

			Date dt = sdf.parse(manager.getValidity());

			Calendar c = Calendar.getInstance();
			c.setTime(dt);
			Log.i("auth", "manager.getValidity() = " + manager.getValidity());
			int day = c.get(Calendar.DAY_OF_MONTH);
			int month = c.get(Calendar.MONTH);
			int year = c.get(Calendar.YEAR);

			NumberConvertor convertor = new NumberConvertor(day + "");
			meyad.setText(meyad.getText() + " " + convertor.getBanglaNumber());
			meyad.setText(meyad.getText() + " " + getMonth(month));

			convertor = new NumberConvertor(year + "");
			meyad.setText(meyad.getText() + " " + convertor.getBanglaNumber());

			TextView ok = (TextView) layout.findViewById(R.id.ok);
			ok.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private String getMonth(int month) {
		switch (month) {
		case 1:
			return "জানুয়ারী";
		case 2:
			return "ফেব্রুয়ারী";
		case 3:
			return "মার্চ";
		case 4:
			return "এপ্রিল";
		case 5:
			return "মে";
		case 6:
			return "জুন";
		case 7:
			return "জুলাই";
		case 8:
			return "অগাস্ট";
		case 9:
			return "সেপ্টেম্বর";
		case 10:
			return "অক্টোবর";
		case 11:
			return "নভেম্বর";
		case 12:
			return "ডিসেম্বর";
		}

		return "";
	}

	private void sendPostRequest() throws JSONException {

		JSONObject object = new JSONObject();
		object.put("orderId", "234324");
		object.put("purchaseDescription", "Bangla TV Weekly");
		object.put("amount", "MYR 2.00'");
		object.put("vatRate", "0.0");
		object.put("successRedirect",
				"http://stagingtv.universetv.net/digi/payment.php?status=success");
		object.put("cancelRedirect",
				"http://stagingtv.universetv.net/digi/payment.php?status=failed");
		object.put("merchantName", "hakjapan-banglatv-android");
		object.put("locale", "bn-BD");
		object.put("allowedPaymentMethods", new JSONArray().put(0, "DOB"));
		object.put("connectId", manager.getUserId());

		JSONObject o = new JSONObject();
		o.put("name", "Bangla TV Weekly");
		o.put("price", "MYR 2.00");
		o.put("vatRate", "0.0");
		o.put("sku", "MY-TV-TVBANGLA-FULL-W");
		o.put("timeSpec", "P7D");

		object.put("products", new JSONArray().put(0, o));

		JsonObjectRequest request = new JsonObjectRequest(
				"http://stagingtv.universetv.net/digi/transaction.php?token="
						+ manager.getAccessToken() + "&sub="
						+ manager.getUserId(), null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						try {
							JSONObject jsonObject = response.getJSONArray(
									"links").getJSONObject(0);
							String url = jsonObject.getString("href");
							progress.cancel();
							loadWebView(url);

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getApplicationContext(),
								"check internet connection", Toast.LENGTH_SHORT)
								.show();
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