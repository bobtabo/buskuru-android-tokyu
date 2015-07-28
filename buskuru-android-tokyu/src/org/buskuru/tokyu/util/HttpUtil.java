/*
 * BusKuru is a Busnavi program developed by BobTabo.
 * 
 * Copyright (c) 2011 BobTabo. All Rights Reserved.
 */
package org.buskuru.tokyu.util;

/* $Id: HttpUtil.java 187 2014-05-26 15:58:55Z nagashiba $ */

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.buskuru.tokyu.exceptions.ServerConnectionException;

import android.util.Log;

public final class HttpUtil {

	private static String TAG = "Buskuru";

	/**
	 * POSTします。
	 * 
	 * @param url
	 *            URL
	 * @return サーバからのレスポンス
	 */
	public static String doPost(String url) {
		return doPost(url, null);
	}

	/**
	 * POSTします。
	 * 
	 * @param url
	 *            URL
	 * @param params
	 *            パラメータ
	 * @return サーバからのレスポンス
	 */
	public static String doPost(String url, Map<String, String> params) {
		return doPost(url, params, null);
	}

	/**
	 * POSTします。
	 * 
	 * @param url
	 *            URL
	 * @param params
	 *            パラメータ
	 * @param userAgent
	 *            ユーザーエージェント
	 * @return サーバからのレスポンス
	 */
	public static String doPost(String url, Map<String, String> params, String userAgent) {
		String result = null;
		try {
			HttpPost method = new HttpPost(url);
			DefaultHttpClient client = new DefaultHttpClient();
			HttpParams httpParams = client.getParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
			HttpConnectionParams.setSoTimeout(httpParams, 10000);

			if (StringUtil.isNotEmpty(userAgent)) {
				httpParams.setParameter("http.useragent", userAgent);
			}

			// POST データの設定
			if (MapUtil.isNotEmpty(params)) {
				StringBuilder builder = new StringBuilder();
				for (Entry<String, String> entry : params.entrySet()) {
					if (builder.toString().length() > 0) {
						builder.append("&");
					}
					builder.append(entry.getKey()).append("=").append(entry.getValue());
				}

				StringEntity paramEntity = new StringEntity(builder.toString());
				paramEntity.setChunked(false);
				paramEntity.setContentType("application/x-www-form-urlencoded");
				method.setEntity(paramEntity);
			}

			HttpResponse response = client.execute(method);
			int status = response.getStatusLine().getStatusCode();
			if (status != HttpStatus.SC_OK) {
				throw new ServerConnectionException("ステータスエラー");
			}

			result = EntityUtils.toString(response.getEntity(), "Shift_JIS");
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new ServerConnectionException(e);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new ServerConnectionException(e);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			throw new ServerConnectionException(e);
		}
		return result;
	}

	/**
	 * GETします。
	 * 
	 * @param url
	 *            URL
	 * @return サーバからのレスポンス
	 */
	public static String doGet(String url) {
		return doGet(url, null);
	}

	/**
	 * GETします。
	 * 
	 * @param url
	 *            URL
	 * @param params
	 *            パラメータ
	 * @return サーバからのレスポンス
	 */
	public static String doGet(String url, Map<String, String> params) {
		return doGet(url, params, null);
	}

	/**
	 * GETします。
	 * 
	 * @param url
	 *            URL
	 * @param params
	 *            パラメータ
	 * @param userAgent
	 *            ユーザーエージェント
	 * @return サーバからのレスポンス
	 */
	public static String doGet(String url, Map<String, String> params, String userAgent) {
		String result = null;
		try {
			HttpGet method = new HttpGet(url);
			DefaultHttpClient client = new DefaultHttpClient();
			HttpParams httpParams = client.getParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
			HttpConnectionParams.setSoTimeout(httpParams, 10000);

			if (StringUtil.isNotEmpty(userAgent)) {
				httpParams.setParameter("http.useragent", userAgent);
			}

			// POST データの設定
			if (MapUtil.isNotEmpty(params)) {
				StringBuilder builder = new StringBuilder();
				for (Entry<String, String> entry : params.entrySet()) {
					if (builder.toString().length() > 0) {
						builder.append("&");
					}
					builder.append(entry.getKey()).append("=").append(entry.getValue());
				}

				StringEntity paramEntity = new StringEntity(builder.toString());
				paramEntity.setChunked(false);
				paramEntity.setContentType("application/x-www-form-urlencoded");
				((HttpResponse) method).setEntity(paramEntity);
			}

			HttpResponse response = client.execute(method);
			int status = response.getStatusLine().getStatusCode();
			if (status != HttpStatus.SC_OK) {
				throw new ServerConnectionException("ステータスエラー");
			}

			result = EntityUtils.toString(response.getEntity(), "Shift_JIS");
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new ServerConnectionException(e);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new ServerConnectionException(e);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			throw new ServerConnectionException(e);
		}
		return result;
	}

	/**
	 * 対象URLのHTMLを取得します。
	 * 
	 * @param url
	 *            対象URL
	 * @return HTMLソース
	 */
	public static String getHtml(String url) {
		try {
			HttpPost post = new HttpPost(url);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = null;
			response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String html = EntityUtils.toString(entity);
			html = html.trim();
			return html;
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new ServerConnectionException(e);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new ServerConnectionException(e);
		}
	}

	/**
	 * 対象URLのHTMLを取得します。
	 * 
	 * @param url
	 *            対象URL
	 * @return HTMLソース
	 */
	public static String getHtmlEx(String url) {
		try {
			HttpGet post = new HttpGet(url);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = null;
			response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String html = new String(EntityUtils.toByteArray(entity), "Shift-JIS");
			html = html.trim();
			return html;
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new ServerConnectionException(e);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new ServerConnectionException(e);
		}
	}

	/**
	 * URLのクエリ文字列を取得します。
	 * 
	 * @param url
	 *            対象URL
	 * @param excludeKey
	 *            除外するクエリキー
	 * @return クエリ文字列
	 */
	public static String getQuery(String url, String... excludeKey) {
		List<String> keys = ArrayUtil.toList(excludeKey);
		// try {
		StringBuilder result = new StringBuilder();

		// URI uri = new URI(url);
		// List<NameValuePair> list = URLEncodedUtils.parse(uri, "UTF-8");
		// for (NameValuePair nv : list) {
		// if (keys.indexOf(nv.getName()) > -1) {
		// continue;
		// }
		// if (StringUtil.isNotEmpty(result.toString())) {
		// result.append("&");
		// }
		// result.append(nv.getName()).append("=").append(nv.getValue());
		// }

		String[] uri = url.split("\\?");
		String query = uri[1].replaceFirst("\\?", "");
		String[] queries = query.split("&");
		for (String q : queries) {
			String[] kv = q.split("=");
			if (keys.indexOf(kv[0]) > -1) {
				continue;
			}
			if (StringUtil.isNotEmpty(result.toString())) {
				result.append("&");
			}
			result.append(kv[0]).append("=");
			if (kv.length == 2) {
				result.append(kv[1]);
			}
		}
		return result.toString();

		// } catch (URISyntaxException e) {
		// Log.e(TAG, e.getMessage(), e);
		// }
		// return null;
	}
}
