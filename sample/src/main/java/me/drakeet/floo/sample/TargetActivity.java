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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class TargetActivity extends AppCompatActivity {

  public static final String KEY_MAIL = "key_mail";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_target);
    Intent intent = getIntent();

    TextView text = (TextView) findViewById(R.id.text);
    text.append("\n\n\nExtras: \n\n");

    Bundle bundle = intent.getExtras();
    assert bundle != null;
    for (String key : bundle.keySet()) {
      text.append(key + "\n" + bundle.get(key) + "\n\n");
    }

    String sourceUrl = intent.getStringExtra("__source__");
    if (sourceUrl != null) {
      setTitle(Uri.parse(sourceUrl).getPath());
    }

    // noinspection ConstantConditions
    String subUrl = intent.getData().getQueryParameter("url");
    if (subUrl != null) {
      text.append("\n\nSUB-URL: ");
      text.append(subUrl);
    }
  }

  public void onBack(View view) {
    onBackPressed();
  }
}
