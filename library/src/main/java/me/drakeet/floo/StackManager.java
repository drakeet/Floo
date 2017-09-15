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
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author drakeet
 */
final class StackManager {

    private @Nullable static String targetIndexKey;
    private @Nullable static Object result;

    private static boolean hasCount;
    private static int currentCount;


    private StackManager() { throw new AssertionError(); }


    static void start(@NonNull Activity activity) {
        consumeACount();
        activity.finish();
    }


    static void onActivityResumed(@NonNull final Activity activity) {
        if (hasCount() && currentCount > 0) {
            consumeACount();
            activity.finish();
        } else if (hasCount() && currentCount <= 0) {
            consumeHasCount();
            if (hasResult()) {
                if (activity instanceof StackCallback) {
                    StackCallback callback = stackCallback(activity);
                    callback.onReceivedResult(result);
                    consumeResult();
                } else {
                    consumeResult();
                }
            }
        } else if (hasTarget()) {
            if (activity instanceof StackCallback) {
                StackCallback callback = stackCallback(activity);

                if (match(callback.indexKeyForStackTarget())) {
                    consumeTarget();
                    if (hasResult()) {
                        callback.onReceivedResult(result);
                        consumeResult();
                    }
                } else {
                    activity.finish();
                }
            } else {
                reset();
            }
        }
    }


    private static void consumeHasCount() {
        StackManager.hasCount = false;
    }


    static void post(@Nullable Object result, @NonNull String toMatcherKey) {
        StackManager.result = result;
        targetIndexKey = toMatcherKey;
    }


    static void postCount(@Nullable Object result, @IntRange(from = 1) int count) {
        StackManager.result = result;
        StackManager.hasCount = true;
        StackManager.currentCount = count;
    }


    private static void reset() {
        targetIndexKey = null;
        result = null;
        result = null;
        hasCount = false;
        currentCount = 0;
    }


    private static boolean hasTarget() {
        return targetIndexKey != null;
    }


    private static boolean match(@Nullable String matcherKey) {
        return equals(matcherKey, StackManager.targetIndexKey);
    }


    private static void consumeTarget() { targetIndexKey = null; }


    private static boolean hasResult() { return result != null; }


    private static void consumeResult() { result = null; }


    private static boolean hasCount() { return hasCount; }


    private static void consumeACount() { currentCount--; }


    @NonNull
    private static StackCallback stackCallback(@NonNull Activity activity) {
        return (StackCallback) activity;
    }


    private static boolean equals(@Nullable Object a, @Nullable Object b) {
        return (a == b) || (a != null && a.equals(b));
    }
}
