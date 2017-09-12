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
import android.support.annotation.Keep;
import android.support.annotation.NonNull;

/**
 * @author drakeet
 */
@Keep
public class Target {

    private @NonNull final String url;


    public Target(@NonNull String url) {
        this.url = url;
    }


    @NonNull
    public String toTargetUrl() {
        return Uri.parse(url).buildUpon().build().toString();
    }


    @NonNull
    public String getUrl() {
        return url;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Target target = (Target) o;
        return url.equals(target.url);
    }


    @Override
    public int hashCode() {
        return url.hashCode();
    }
}
