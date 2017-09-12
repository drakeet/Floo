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

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author drakeet
 */
public interface Navigation {

    @NonNull @CheckResult Navigation setFlags(int intentFlags);
    @NonNull @CheckResult Navigation appendQueryParameter(@NonNull String key, @NonNull String value);

    @NonNull @CheckResult Navigation putExtras(@NonNull Bundle bundle);
    @NonNull @CheckResult Navigation putExtras(@NonNull Intent intent);
    @NonNull @CheckResult Navigation putExtra(@NonNull String key, int value);
    @NonNull @CheckResult Navigation putExtra(@NonNull String key, long value);
    @NonNull @CheckResult Navigation putExtra(@NonNull String key, float value);
    @NonNull @CheckResult Navigation putExtra(@NonNull String key, double value);
    @NonNull @CheckResult Navigation putExtra(@NonNull String key, boolean value);
    @NonNull @CheckResult Navigation putExtra(@NonNull String key, byte value);
    @NonNull @CheckResult Navigation putExtra(@NonNull String key, short value);
    @NonNull @CheckResult Navigation putExtra(@NonNull String key, @Nullable String value);
    @NonNull @CheckResult Navigation putExtra(@NonNull String key, @Nullable CharSequence value);
    @NonNull @CheckResult Navigation putExtra(@NonNull String key, @Nullable Parcelable value);
    @NonNull @CheckResult Navigation putExtra(@NonNull String key, @Nullable Serializable value);
    @NonNull @CheckResult Navigation putIntegerArrayListExtra(@NonNull String name, @NonNull ArrayList<Integer> value);
    @NonNull @CheckResult Navigation putStringArrayListExtra(@NonNull String name, @NonNull ArrayList<String> value);
    @NonNull @CheckResult Navigation putCharSequenceArrayListExtra(@NonNull String name, @NonNull ArrayList<CharSequence> value);
    @NonNull @CheckResult Navigation putParcelableArrayListExtra(@NonNull String name, @NonNull ArrayList<? extends Parcelable> value);

    @Nullable @CheckResult Intent getIntent();

    boolean hasTarget();
    void ifIntentNonNullSendTo(@NonNull IntentReceiver receiver);
    void start();
}
