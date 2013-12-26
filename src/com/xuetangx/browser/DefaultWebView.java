package com.xuetangx.browser;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
/**
 * default webview, which is used to load the webpage of usage help.
 * @author gaoyansansheng@gmail.com
 *
 */
public class DefaultWebView {
	private WebView browser;
	private Context context;
	private String startPage;
	private View mark;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(mark != null) {
				if(((String)msg.obj).equals("start")){
					mark.setVisibility(View.VISIBLE);
				}
				if(((String)msg.obj).equals("finish")) {
					mark.setVisibility(View.GONE);
				}
			}
		}
	};
	public DefaultWebView(WebView w, Context c) {
		browser = w;
		context = c;
		startPage = "";
	}
	public DefaultWebView(WebView w, Context c, String s) {
		browser = w;
		context = c;
		startPage = s;
		browser.loadUrl(s);
	}
	public DefaultWebView(WebView w, Context c, String s, View mark) {
		browser = w;
		context = c;
		startPage = s;
		browser.loadUrl(s);
		this.mark = mark;
	}
	/**
	 * set the webview.
	 * @param inner true the url would be handle in this webview, or not open the system browser.
	 */
	public void setBrowser(boolean inner, boolean key, boolean zoom, WebViewClient client) {
		WebSettings setting = browser.getSettings();
		setting.setUseWideViewPort(zoom);
		setting.setLoadWithOverviewMode(zoom);
		if(client != null) {
			browser.setWebViewClient(client);
		} else {
			if(inner) {
				browser.setWebViewClient(new DefaultWebClient());
			}
		}
	}
	public void addJsInterface(Object obj, String description) {
		browser.addJavascriptInterface(obj, description);
	}
	private class DefaultWebClient extends WebViewClient {
		@Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url)
	    {
			view.loadUrl(url);
			return true;
	    }
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Message msg = Message.obtain();
			msg.obj = "start";
			handler.sendMessage(msg);
			super.onPageStarted(view, url, favicon);
		}
		@Override
		public void onPageFinished(WebView view, String url) {
	        /*Log.d("WebView","onPageFinished ");
	        view.loadUrl("javascript:window." + TsinghuaWebView.flag + ".showSource('<head>'+" +
	                "document.getElementsByTagName('html')[0].innerHTML+'</head>');");*/
			Message msg = Message.obtain();
			msg.obj = "finish";
			handler.sendMessage(msg);
	        super.onPageFinished(view, url);
	    }
	}

}

