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

import android.app.Activity;
import android.support.annotation.CheckResult;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author drakeet
 */
public class Stack implements StackStates.Target, StackStates.Flow, StackStates.End {

    private @NonNull final Activity activity;

    private @Nullable Object result;
    private @Nullable String indexKey;

    private int count = 1;


    public Stack(@NonNull Activity activity) { this.activity = activity; }


    @NonNull @CheckResult
    public StackStates.Flow popCount(@IntRange(from = 1) int count) {
        this.count = count;
        return this;
    }


    @NonNull @CheckResult
    public StackStates.Flow target(@NonNull String indexKey) {
        this.indexKey = indexKey;
        return this;
    }


    @NonNull @CheckResult
    public StackStates.End result(@NonNull Object result) {
        this.result = result;
        return this;
    }


    public void start() {
        if (indexKey != null) {
            StackManager.post(result, indexKey);
        } else {
            StackManager.postCount(result, count);
        }
        StackManager.start(activity);
    }
}
