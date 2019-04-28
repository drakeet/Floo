/*
 * Copyright 2017 drakeet. https://github.com/drakeet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.drakeet.floo.sample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class WebActivity extends AppCompatActivity {

  private static final String URL = "url";
  private TextView loading;

  @Override
  @SuppressLint("SetJavaScriptEnabled")
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_web);
    WebView webView = (WebView) findViewById(R.id.web_view);
    String url = getIntent().getStringExtra(URL);
    if (url == null && getIntent().getData() != null) {
      url = getIntent().getData().getQueryParameter(URL);
    }
    if (url == null) {
      finish();
    }
    webView.setWebViewClient(new InnerWebViewClient());
    webView.getSettings().setJavaScriptEnabled(true);
    webView.loadUrl(url);

    loading = (TextView) findViewById(R.id.loading);
    Animation animation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    animation.setRepeatMode(Animation.REVERSE);
    animation.setRepeatCount(Animation.INFINITE);
    animation.setDuration(500);
    loading.startAnimation(animation);
    setTitle(url);
  }

  public class InnerWebViewClient extends WebViewClient {

    @Override @SuppressWarnings("deprecation")
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      view.loadUrl(url);
      return true;
    }

    @Override
    public void onPageCommitVisible(WebView view, String url) {
      super.onPageCommitVisible(view, url);
      loading.clearAnimation();
      loading.setVisibility(View.GONE);
      view.setVisibility(View.VISIBLE);
      Animation animation = new AlphaAnimation(0.1f, 1.0f);
      animation.setDuration(800);
      view.setAnimation(animation);
      setTitle(view.getTitle());
    }
  }
}
