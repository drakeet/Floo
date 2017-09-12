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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import java.net.URLEncoder;
import java.util.Map;
import me.drakeet.floo.Floo;
import me.drakeet.floo.Target;
import me.drakeet.floo.sample.entity.Mail;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView text = (TextView) findViewById(R.id.text);
        Map<String, Target> targetMap = Floo.getTargetMap();
        for (String url : targetMap.keySet()) {
            text.append(url);
            text.append("\t\t <-> \t\t");
            text.append(targetMap.get(url).toTargetUrl());
            text.append("\n");
        }
    }


    public void onStartHomeWithExtraDataAndParams(View view) {
        Mail mail = new Mail();
        mail.content = "Hello World";
        mail.from = "drakeet";
        mail.to = "Xiaoai";
        Floo.navigation(this, "sdk://m.drakeet.me/home")
            .appendQueryParameter("date", "2017.9.11")
            .appendQueryParameter("user_id", "drakeet")
            .putExtra(TargetActivity.KEY_MAIL, mail)
            .start();
    }


    public void onStartHomeWithParams(View view) {
        Floo.navigation(this, "sdk://m.drakeet.me/home")
            .appendQueryParameter("tag", "just")
            .appendQueryParameter("tab", "top")
            .appendQueryParameter("user_id", "drakeet")
            .start();
    }


    public void onStartHomeWithoutScheme(View view) {
        Floo.navigation(this, "m.drakeet.me/home").start();
    }


    public void onStartWebViewWhenUrlNotFound(View view) {
        Floo.navigation(this, "http://drakeet.me").start();
    }


    public void onStartH5WithPageURL(View view) {
        Floo.navigation(this, "https://m.drakeet.me/web")
            .appendQueryParameter("url", "https://github.com/drakeet")
            .start();
    }


    public void onStartPagesForResult(View view) {
        for (int i = 1; i <= 5; i++) {
            String pageURL = URLEncoder.encode("https://chunchun.io/page" + i);
            Floo.navigation(this, "https://m.drakeet.me/container?url=" + pageURL).start();
        }
    }


    public void onStartNoRegisteredPage(View view) {
        Floo.navigation(this, "demo://m.drakeet.me/no_registered").start();
    }


    public void onStartError(View view) {
        final String invalidUrl = "BS08PTQ9FS85K34E";
        Floo.navigation(this, invalidUrl)
            .appendQueryParameter("tab", "profile")
            .appendQueryParameter("user_id", "drakeet")
            .start();
    }
}
