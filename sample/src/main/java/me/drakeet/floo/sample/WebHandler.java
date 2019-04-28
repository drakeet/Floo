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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.drakeet.floo.TargetNotFoundHandler;
import me.drakeet.floo.Urls;

/**
 * @author drakeet
 */
public class WebHandler implements TargetNotFoundHandler {

  @Override
  public boolean onTargetNotFound(
      @NonNull Context context,
      @NonNull Uri sourceUri,
      @NonNull Bundle extras,
      @Nullable Integer intentFlags) {

    if (Urls.isWebScheme(sourceUri)) {
      Intent intent = new Intent(context, WebActivity.class);
      intent.putExtra("url", sourceUri.toString());
      if (intentFlags != null) {
        intent.setFlags(intentFlags);
      }
      context.startActivity(intent);
      return true;
    }
    return false;
  }
}
