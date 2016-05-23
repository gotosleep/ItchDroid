package io.itch.activities;

import io.itch.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class NewsActivity extends BaseActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        WebView webview = (WebView) findViewById(R.id.webViewNews);
        progressBar = (ProgressBar) findViewById(R.id.progressBarWeb);

        webview.getSettings().setJavaScriptEnabled(true);

        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);

                // remove some of the tumblr crap that we don't want
                view.loadUrl("javascript:(function(){ " +
                        "try {" +
                        "  var header = document.getElementById('header');" +
                        "  header.style.display = 'none';" +
                        "  var hider = function(name) {" +
                        "    var a = document.getElementsByClassName(name); " +
                        "    var item;" +
                        "    for (var i = 0; i < a.length; ++i) {" +
                        "      item = a[i];" +
                        "      item.style.display = 'none';" +
                        "    }" +
                        "  };" +
                        "  hider('nav-menu-wrapper');" +
                        "  hider('nav-menu-bg');" +
                        "} catch (e) { " +
                        "}" +
                        "})();");
            }
        });

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                webView.loadUrl(url);
                return true;
            }
        });

        webview.loadUrl("http://itchio.tumblr.com");
    }

    @Override
    protected String getScreenPath() {
        return "News";
    }
}
