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

package me.drakeet.floo.extensions;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import me.drakeet.floo.TargetNotFoundHandler;

/**
 * @author drakeet
 */
public class OpenDirectlyHandler implements TargetNotFoundHandler {

    @Override
    public boolean onTargetNotFound(
        @NonNull Context context,
        @NonNull Uri uri,
        @NonNull Bundle bundle,
        @Nullable Integer intentFlags) {

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtras(bundle);
        if (intentFlags != null) {
            intent.setFlags(intentFlags);
        }
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }
}
