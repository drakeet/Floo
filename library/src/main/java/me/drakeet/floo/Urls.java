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

import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * @author drakeet
 */
public class Urls {

    // TODO: 2017/9/11 Match Case


    @NonNull
    public static String indexUrl(@NonNull final String uriString) {
        return indexUrl(Uri.parse(uriString));
    }


    @NonNull
    public static String indexUrl(@NonNull final Uri uri) {
        return uri.getScheme() != null && uri.getAuthority() != null ?
               uri.getAuthority() + uri.getPath() :
               uri.getPath();
    }


    public static boolean isWebScheme(@NonNull Uri sourceUri) {
        return sourceUri.getScheme() != null &&
            (sourceUri.getScheme().toLowerCase().equals("http") ||
                sourceUri.getScheme().toLowerCase().equals("https"));
    }
}
