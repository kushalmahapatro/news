package app.km.in.dailybharat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


/**
 * Created by Sexy_Virus on 03/02/18.
 */

public class WebActivity extends Activity {

    public static final String BUNDLE_IMAGE_ID = "BUNDLE_IMAGE_ID";
    private Context mContext;
    private Activity mActivity;

    private RelativeLayout mRelativeLayout;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    SharedPreferences sp;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        sp = getSharedPreferences("NightMode", MODE_PRIVATE);
        String nightValue = sp.getString("value", "off");
        if (nightValue.equalsIgnoreCase("off")) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        } else if (nightValue.equalsIgnoreCase("on")) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else if (nightValue.equalsIgnoreCase("auto")) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_AUTO);
        }
        super.onCreate(savedInstanceState);
        newWebView();

    }

    private void newWebView() {
        setContentView(R.layout.activity_web_view);
        final String smallResId = getIntent().getStringExtra(BUNDLE_IMAGE_ID);
        // Get the application context
        mContext = getApplicationContext();
        // Get the activity
        mActivity = WebActivity.this;

        // Change the action bar color
       /* getActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#FFFF0000"))
        );*/

        // Get the widgets reference from XML layout
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
        mWebView = (WebView) findViewById(R.id.web_view);
        mProgressBar = (ProgressBar) findViewById(R.id.pb);
        renderWebPage(smallResId);
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void renderWebPage(String urlToRender) {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // Do something on page loading started
                // Visible the progressbar
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // Do something when page loading finished
                //   Toast.makeText(mContext,"Page Loaded.",Toast.LENGTH_SHORT).show();
            }

        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            /*
                public void onProgressChanged (WebView view, int newProgress)
                    Tell the host application the current progress of loading a page.

                Parameters
                    view : The WebView that initiated the callback.
                    newProgress : Current page loading progress, represented by an integer
                        between 0 and 100.
            */
            public void onProgressChanged(WebView view, int newProgress) {
                // Update the progress bar with page loading progress
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    // Hide the progressbar
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

        // Enable the javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // Render the web page
        mWebView.loadUrl(urlToRender);
    }
}
