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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.Map;

/**
 * url - Target
 *
 * @author drakeet
 */
final class TargetMap {

    private @NonNull Map<String, Target> map;


    TargetMap(@NonNull Map<String, Target> map) {
        this.map = map;
    }


    void set(@NonNull Map<String, Target> map) {
        this.map = map;
    }


    @NonNull
    Map<String, Target> get() {
        return map;
    }


    @Nullable
    Target getTarget(@NonNull String url) {
        // TODO: 2017/8/23 case?
        return map.get(url);
    }
}
