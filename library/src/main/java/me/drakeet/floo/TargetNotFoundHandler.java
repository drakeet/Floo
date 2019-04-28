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

package me.drakeet.floo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Interface definition for a handler to be invoked when a target is not found.
 * The handler will be invoked one by one following the registered order.
 *
 * @author drakeet
 */
public interface TargetNotFoundHandler {

  /**
   * Called when a target not found event occurred. This allows handlers to
   * get a chance to respond before error.
   *
   * @param context The context.
   * @param sourceUri The source URI.
   * @param extras The bundle extras.
   * @param intentFlags The desired flags.
   * @return True if the handler has consumed the event, false otherwise.
   */
  boolean onTargetNotFound(
      @NonNull Context context,
      @NonNull Uri sourceUri,
      @NonNull Bundle extras,
      @Nullable Integer intentFlags
  );
}
