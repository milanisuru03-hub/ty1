package com.example.ytplayer;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

/**
 * Simple full-screen YouTube player for old Android devices (API 14 / Android 4.0+).
 * Loads the lightweight mobile YouTube site inside a WebView and supports
 * HTML5 fullscreen video playback.
 */
public class MainActivity extends Activity {

    private static final String YOUTUBE_URL = "https://m.youtube.com";

    private WebView webView;

    // Used to support fullscreen <video> playback
    private View customView;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private FrameLayout fullscreenContainer;
    private int originalOrientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webview);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAppCacheEnabled(true);

        // Present as a mobile browser so YouTube serves its mobile site,
        // which is far lighter and more compatible with an old WebView engine.
        String mobileUA = "Mozilla/5.0 (Linux; Android 4.0.4; Galaxy Nexus) "
                + "AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 "
                + "Mobile Safari/535.19";
        settings.setUserAgentString(mobileUA);

        webView.setWebViewClient(new WebViewClient() {
            // Keep every navigation inside the WebView instead of an external browser
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            // Handles switching the <video> element into fullscreen and back
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                if (customView != null) {
                    onHideCustomView();
                    return;
                }
                customView = view;
                customViewCallback = callback;
                originalOrientation = getRequestedOrientation();

                fullscreenContainer = new FrameLayout(MainActivity.this);
                fullscreenContainer.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                fullscreenContainer.addView(customView);

                ((ViewGroup) getWindow().getDecorView()).addView(fullscreenContainer);
                webView.setVisibility(View.GONE);
            }

            @Override
            public void onHideCustomView() {
                if (customView == null) {
                    return;
                }
                webView.setVisibility(View.VISIBLE);
                ((ViewGroup) getWindow().getDecorView()).removeView(fullscreenContainer);
                fullscreenContainer = null;
                customView = null;
                if (customViewCallback != null) {
                    customViewCallback.onCustomViewHidden();
                    customViewCallback = null;
                }
                setRequestedOrientation(originalOrientation);
            }
        });

        webView.loadUrl(YOUTUBE_URL);
    }

    // Let the hardware back button navigate WebView history before exiting the app
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}
