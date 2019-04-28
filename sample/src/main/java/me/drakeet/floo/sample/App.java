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

import android.app.Application;
import androidx.annotation.NonNull;
import com.drakeet.floo.sample.R;
import java.util.HashMap;
import java.util.Map;
import me.drakeet.floo.Chain;
import me.drakeet.floo.Floo;
import me.drakeet.floo.Interceptor;
import me.drakeet.floo.Target;
import me.drakeet.floo.extensions.LogInterceptor;
import me.drakeet.floo.extensions.OpenDirectlyHandler;

/**
 * @author drakeet
 */
public class App extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    Map<String, Target> mappings = new HashMap<>();
    mappings.put("m.drakeet.me/home", new Target("floo://drakeet.sdk/target"));
    mappings.put("m.drakeet.me/link", new Target("floo://drakeet.sdk/target"));
    mappings.put("m.drakeet.me/web", new Target("floo://drakeet.sdk/web"));
    mappings.put("m.drakeet.me/container", new Target("floo://m.drakeet.me/container"));
    mappings.put("mosaic.chunchun.io:8080", new Target("https://play.google.com/store/apps/details?id=me.drakeet.puremosaic"));
    mappings.put("PureWriter", new Target("https://play.google.com/store/apps/details?id=com.drakeet.purewriter"));

    Floo.configuration()
        .setDebugEnabled(true)
        .addRequestInterceptor(new PureSchemeInterceptor(getString(R.string.scheme)))
        .addRequestInterceptor(new LogInterceptor("Request"))
        .addTargetInterceptor(new PureSchemeInterceptor(getString(R.string.scheme)))
        .addTargetInterceptor(new LogInterceptor("Target"))
        .addTargetNotFoundHandler(new WebHandler())
        .addTargetNotFoundHandler(new OpenDirectlyHandler())
        .addTargetNotFoundHandler(new TargetNotFoundToaster());

    Floo.apply(mappings);
  }

  private static class PureSchemeInterceptor implements Interceptor {

    private @NonNull final String scheme;

    private PureSchemeInterceptor(@NonNull String scheme) {
      this.scheme = scheme;
    }

    @NonNull
    @Override
    public Chain intercept(@NonNull Chain chain) {
      if (BuildConfig.DEBUG && URLs.scheme().equals(chain.request().getScheme())) {
        chain = new Chain(chain.request().buildUpon().scheme(scheme).build());
      }
      return chain;
    }
  }
}
