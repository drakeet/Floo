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

package com.drakeet.floo.sample;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.drakeet.floo.Floo;
import com.drakeet.floo.StackCallback;
import com.drakeet.floo.Urls;

/**
 * @author drakeet
 */
public class ContainerActivity extends AppCompatActivity implements StackCallback {

  private TextView textView;

  @Nullable @Override
  public String indexKeyForStackTarget() {
    if (getIntent().getData() == null) {
      return null;
    }
    String pageURLOfThis = getIntent().getData().getQueryParameter("url");
    Uri pageUri = Uri.parse(pageURLOfThis);

    return Urls.indexUrl(pageUri);
  }

  @Override
  public void onReceivedResult(@Nullable Object result) {
    textView.append("\n\nReceived data: " + result);
    textView.append("\n(You have passed a data from a page to current page successfully)");
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_container);
    textView = (TextView) findViewById(R.id.url_text);
    if (getIntent().getData() != null) {
      textView.setText(getIntent().getData().toString());
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    if (getIntent().getData() == null) {
      return;
    }
    String subURLOfThis = getIntent().getData().getQueryParameter("url");

    Button setResultButton = (Button) findViewById(R.id.set_result_button);
    if (subURLOfThis.contains("page5")) {
      setResultButton.setVisibility(View.VISIBLE);
    } else {
      setResultButton.setVisibility(View.INVISIBLE);
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
  public void setResultAndBackToPage2(View view) {
    Floo.stack(this)
        .target(Urls.indexUrl("https://chunchun.io/page2"))
        .result("https://play.google.com/store/apps/details?id=com.drakeet.purewriter")
        .start();
  }

  @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
  private void popCount() {
    Floo.stack(this)
        .popCount(2)
        .result("https://play.google.com/store/apps/details?id=com.drakeet.purewriter")
        .start();
  }

  public void back(View view) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
      Floo.stack(this).popCount(1).start();
    } else {
      onBackPressed();
    }
  }
}
