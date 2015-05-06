/*
 * 同步的html
 */
package com.linkin.mtv.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class SynHtmlUtil {
	private static final String TAG = "HttpDemo";
	private static HttpClient httpClient;

	static {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);

		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
		httpClient = new DefaultHttpClient(conMgr, params);
	}

	public static String get(String url) {
		return get(url, "UTF_8");
	}

	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static String get(String url, String charset) {
		InputStream in = null;
		HttpGet get = new HttpGet();
		try {
			get.setURI(new URI(url));
			HttpResponse response = httpClient.execute(get);
			if (response.getStatusLine().getStatusCode() != 200) {
				get.abort();
				return null;
			}
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				in = entity.getContent();
				String result = convertStreamToString(in);
				return result;
			}
			return "";
		} catch (Exception e) {
			get.abort();
			e.printStackTrace();
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String post(String url, String parm) {
		return post(url, parm, "UTF_8");
	}

	private static String post(String url, String parm, String charset) {
		InputStream in = null;
		HttpPost post = new HttpPost();
		try {
			post.setURI(new URI(url));
			StringEntity s = new StringEntity(parm, HTTP.UTF_8); 
			s.setContentType("text/xml charset="+charset);
			post.setEntity(s);
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() != 200) {
				post.abort();
				return null;
			}
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				in = entity.getContent();
				String result = convertStreamToString(in);
				return result;
			}
			return "";
		} catch (Exception e) {
			post.abort();
			e.printStackTrace();
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
