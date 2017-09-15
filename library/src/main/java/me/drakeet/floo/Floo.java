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
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;

import static android.util.Log.DEBUG;
import static android.util.Log.ERROR;
import static android.util.Log.INFO;
import static android.util.Log.WARN;
import static java.lang.String.format;

/**
 * @author drakeet
 */
public final class Floo implements Navigation {

    private static final String TAG = "Floo";

    private static final Configuration CONFIGURATION = new Configuration();

    private @NonNull Context context;
    private @NonNull Bundle bundle;
    private @NonNull Map<String, String> queryParams;
    private @NonNull Uri sourceUri;

    private @Nullable Uri targetUri;
    private @Nullable Integer intentFlags;


    private Floo(@NonNull Context context, @NonNull String url) {
        this.context = context;
        this.bundle = new Bundle();
        this.sourceUri = Uri.parse(url);
        this.queryParams = new HashMap<>();
    }


    /**
     * Create a new {@link Navigation} to open a URL.
     *
     * @param context The context.
     * @param url The source URL.
     * @return A reference to the {@link Navigation}.
     */
    @NonNull @CheckResult
    public static Navigation navigation(@NonNull Context context, @NonNull String url) {
        return new Floo(context, url.trim());
    }


    /**
     * Create a new {@link Stack} to back.
     *
     * @param activity The current activity.
     * @return A reference to the {@link Stack}.
     */
    @NonNull @CheckResult
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static Stack stack(@NonNull Activity activity) {
        configuration().initStackObserverIfNeed(activity);
        return new Stack(activity);
    }


    /**
     * Initialize the {@code FlooDelegate}, will load the last rules map.
     *
     * @return The {@link Configuration}.
     */
    @NonNull @CheckResult
    public static Configuration configuration() {
        return CONFIGURATION;
    }


    /**
     * Apply the rules map. It will replace the original map.
     *
     * @param map The rules map.
     */
    public static void apply(@NonNull final Map<String, Target> map) {
        configuration().apply(map);
    }


    /**
     * Apply the rules map from a JSON. It will replace the original map.
     *
     * @param targetMapJson The target map JSON of your rules.
     * @throws JSONException When the JSON's format is error or unexpected.
     */
    public static void apply(@NonNull final String targetMapJson) throws JSONException {
        configuration().apply(new TargetMapParser().fromJson(targetMapJson));
    }


    /**
     * Set special flags controlling how this intent is handled.
     *
     * @param intentFlags The desired intent flags.
     * @return A reference to the {@link Navigation}.
     * @see Intent#setFlags(int)
     */
    @NonNull @Override @CheckResult
    public Navigation setFlags(int intentFlags) {
        this.intentFlags = intentFlags;
        return this;
    }


    /**
     * Inserts all mappings from the given Bundle.
     *
     * @param bundle A Bundle.
     * @return A reference to the {@link Navigation}.
     * @see Navigation
     * @see #putExtra(String, byte)
     * @see #putExtra(String, short)
     * @see #putExtra(String, int)
     * @see #putExtra(String, long)
     * @see #putExtra(String, float)
     * @see #putExtra(String, double)
     * @see #putExtra(String, boolean)
     * @see #putExtra(String, String)
     * @see #putExtra(String, CharSequence)
     * @see #putExtra(String, Parcelable)
     * @see #putExtra(String, Serializable)
     */
    @NonNull @Override @CheckResult
    public Navigation putExtras(@NonNull Bundle bundle) {
        bundle.putAll(bundle);
        return this;
    }


    /**
     * Append a query parameter.
     *
     * @return A reference to the {@link Navigation}.
     */
    @NonNull @Override @CheckResult
    public Navigation appendQueryParameter(@NonNull String key, @NonNull String value) {
        queryParams.put(key, value);
        return this;
    }


    /**
     * Build and start the target URL.
     *
     * @see #getIntent()
     */
    @Override
    public void start() {
        final Intent intent = getIntent();
        if (intent != null) {
            context.startActivity(intent);
        } else {
            log(WARN, "The target Intent is null, " +
                "it may has been intercepted or dispatched to your TargetNotFoundHandlers.");
        }
    }


    /**
     * A convenient method to get the non-null target intent. If the target intent is non-null,
     * it will be sent to the {@link IntentReceiver} so that you can do anything without checking
     * the intent. Otherwise, nothing happens.
     *
     * @param receiver The target intent receiver.
     */
    @Override
    public void ifIntentNonNullSendTo(@NonNull IntentReceiver receiver) {
        Intent intent = getIntent();
        if (intent != null) {
            receiver.onReceived(intent);
        } else {
            log(INFO, "ifIntentNonNullSendTo: intent == null");
        }
    }


    /**
     * Get the result intent. Return null if the intent has been intercepted.
     *
     * @return The Intent, null if the intent has been intercepted.
     * @see #ifIntentNonNullSendTo(IntentReceiver)
     */
    @Nullable @Override @CheckResult
    public Intent getIntent() {
        sourceUri = appendSourceUri(sourceUri, queryParams);
        Chain chain = interceptRequest(sourceUri);
        sourceUri = chain.request();
        if (chain.isProceed()) {
            Target target = configuration().getTarget(getIndexUrl());
            if (target != null) {
                targetUri = createTargetUri(sourceUri, target);
            } else {
                log(ERROR, getIndexUrl() + " target not found");
                onTargetNotFound(sourceUri, bundle);
                chain = chain.abort();
            }
        }
        if (chain.isProceed()) {
            assert targetUri != null;
            chain = interceptTarget(targetUri);
            targetUri = chain.request();
            if (chain.isProceed()) {
                return createIntent();
            }
        }
        return null;
    }


    @NonNull
    private Intent createIntent() {
        final Intent intent = new Intent(Intent.ACTION_VIEW, targetUri);
        if (configuration().isDebugEnabled()) {
            if (intent.getStringExtra("__source__") == null) {
                intent.putExtra("__source__", sourceUri.toString());
            }
            if (intent.getStringExtra("__target__") == null) {
                assert targetUri != null;
                intent.putExtra("__target__", targetUri.toString());
            }
        }
        intent.putExtras(bundle);
        if (intentFlags != null) {
            intent.setFlags(intentFlags);
        }
        return intent;
    }


    /**
     * Intercept the request URI and dispatch the {@link Chain} to all of registered {@link Interceptor}s
     * one by one.
     *
     * @param uri The request URI.
     * @return The result chain.
     * @see Configuration#addRequestInterceptor(Interceptor)
     */
    @NonNull
    private Chain interceptRequest(@NonNull final Uri uri) {
        Chain chain = new Chain(uri);
        for (Interceptor interceptor : configuration().getRequestInterceptors()) {
            chain = interceptor.intercept(chain);
            if (!chain.isProceed()) {
                log(INFO, "The source URI has been passed to your " +
                    interceptor.getClass().getName() +
                    ", and been aborted by the interceptor.");
                break;
            }
        }
        return chain;
    }


    /**
     * Intercept the target URI and dispatch the {@link Chain} to all of registered {@link Interceptor}s
     * one by one.
     *
     * @param target The target URI.
     * @return The result chain.
     * @see Configuration#addTargetInterceptor(Interceptor)
     */
    private Chain interceptTarget(@NonNull final Uri target) {
        Chain chain = new Chain(target);
        for (Interceptor interceptor : configuration().getTargetInterceptors()) {
            chain = interceptor.intercept(chain);
            if (!chain.isProceed()) {
                log(DEBUG, "The target URI has been passed to your " +
                    interceptor.getClass().getName() +
                    ", and been aborted by the interceptor.");
                break;
            }
        }
        return chain;
    }


    private void onTargetNotFound(@NonNull Uri sourceUri, @NonNull Bundle extras) {
        log(DEBUG, format("No target URI link to he source URI(%s)", sourceUri));
        dispatchTargetNotFoundEvent(sourceUri, extras);
    }


    private void dispatchTargetNotFoundEvent(@NonNull Uri sourceUri, @NonNull Bundle extras) {
        boolean handled;
        for (TargetNotFoundHandler observer : configuration().getTargetNotFoundHandlers()) {
            handled = observer.onTargetNotFound(context, sourceUri, extras, intentFlags);
            if (handled) {
                log(DEBUG, "The TargetNotFoundEvent has been handled by " + observer.getClass().getName());
                break;
            }
        }
    }


    @NonNull
    private Uri appendSourceUri(@NonNull final Uri base, @NonNull final Map<String, String> queryParams) {
        final Uri.Builder sourceBuilder = base.buildUpon();
        for (Map.Entry<String, String> query : queryParams.entrySet()) {
            sourceBuilder.appendQueryParameter(query.getKey(), query.getValue());
        }
        return sourceBuilder.build();
    }


    @NonNull
    private Uri createTargetUri(@NonNull final Uri sourceUri, @NonNull final Target target) {
        final String targetUrl = target.toTargetUrl();
        final Uri sessionUri = Uri.parse(targetUrl);
        final String mergedEncodedQuery = mergeEncodedQuery(sourceUri, sessionUri);
        return sourceUri.buildUpon()
            .scheme(sessionUri.getScheme())
            .authority(sessionUri.getAuthority())
            .path(sessionUri.getPath())
            .encodedQuery(mergedEncodedQuery)
            .fragment(sessionUri.getFragment())
            .build();
    }


    @NonNull
    private String mergeEncodedQuery(@NonNull Uri sourceUri, @NonNull Uri sessionUri) {
        final Map<String, String> map = new HashMap<>();
        map.putAll(encodedQueryParameters(sourceUri));
        map.putAll(encodedQueryParameters(sessionUri));
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> query : map.entrySet()) {
            builder.append(query.getKey()).append("=").append(query.getValue()).append("&");
        }
        String result = builder.toString();
        if (result.endsWith("&")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }


    @NonNull
    public static Map<String, Target> getTargetMap() {
        return Collections.unmodifiableMap(configuration().getTargetMap());
    }


    /**
     * Get the unmodifiable target list.
     *
     * @return The target list.
     */
    @NonNull
    public static List<Target> getTargets() {
        return Collections.unmodifiableList(new ArrayList<>(configuration().getTargetMap().values()));
    }


    /**
     * Check if exist the target for the navigation.
     *
     * @return If exist, true, otherwise false.
     */
    @Override
    public boolean hasTarget() {
        return configuration().getTarget(getIndexUrl()) != null;
    }


    /**
     * Inserts an int value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key a String, or null
     * @param value an int
     */
    @NonNull @Override @CheckResult
    public Navigation putExtra(@NonNull String key, int value) {
        bundle.putInt(key, value);
        return this;
    }


    @NonNull @Override @CheckResult
    public Navigation putExtra(@NonNull String key, long value) {
        bundle.putLong(key, value);
        return this;
    }


    @NonNull @Override @CheckResult
    public Navigation putExtra(@NonNull String key, float value) {
        bundle.putFloat(key, value);
        return this;
    }


    @NonNull @Override @CheckResult
    public Navigation putExtra(@NonNull String key, double value) {
        bundle.putDouble(key, value);
        return this;
    }


    @NonNull @Override @CheckResult
    public Navigation putExtra(@NonNull String key, boolean value) {
        bundle.putBoolean(key, value);
        return this;
    }


    @NonNull @Override @CheckResult
    public Navigation putExtra(@NonNull String key, byte value) {
        bundle.putByte(key, value);
        return this;
    }


    @NonNull @Override @CheckResult
    public Navigation putExtra(@NonNull String key, short value) {
        bundle.putShort(key, value);
        return this;
    }


    @NonNull @Override @CheckResult
    public Navigation putExtra(@NonNull String key, @Nullable String value) {
        bundle.putString(key, value);
        return this;
    }


    @NonNull @Override @CheckResult
    public Navigation putExtra(@NonNull String key, @Nullable CharSequence value) {
        bundle.putCharSequence(key, value);
        return this;
    }


    /**
     * Put {@link Parcelable} with key and value.
     *
     * @return A reference to the {@link Navigation}.
     * @see #putExtra(String, Serializable)
     */
    @NonNull @Override @CheckResult
    public Navigation putExtra(@NonNull String key, @Nullable Parcelable value) {
        bundle.putParcelable(key, value);
        return this;
    }


    /**
     * Put {@link Serializable} with key and value.
     *
     * @return A reference to the {@link Navigation}.
     * @see #putExtra(String, Parcelable)
     */
    @NonNull @Override @CheckResult
    public Navigation putExtra(@NonNull String key, @Nullable Serializable value) {
        bundle.putSerializable(key, value);
        return this;
    }


    @NonNull @Override @CheckResult
    public Navigation putExtras(@NonNull Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            return putExtras(extras);
        }
        return this;
    }


    @NonNull @Override @CheckResult
    public Navigation putIntegerArrayListExtra(@NonNull String name, @NonNull ArrayList<Integer> value) {
        bundle.putIntegerArrayList(name, value);
        return this;
    }


    @NonNull @Override @CheckResult
    public Navigation putStringArrayListExtra(@NonNull String name, @NonNull ArrayList<String> value) {
        bundle.putStringArrayList(name, value);
        return this;
    }


    @NonNull @Override @CheckResult
    public Navigation putCharSequenceArrayListExtra(@NonNull String name, @NonNull ArrayList<CharSequence> value) {
        bundle.putCharSequenceArrayList(name, value);
        return this;
    }


    @NonNull @Override @CheckResult
    public Navigation putParcelableArrayListExtra(@NonNull String name, @NonNull ArrayList<? extends Parcelable> value) {
        bundle.putParcelableArrayList(name, value);
        return this;
    }


    private static void log(int priority, String message) {
        if (configuration().isDebugEnabled()) {
            Log.println(priority, TAG, message);
        }
    }


    /**
     * Returns a map of the unique names of all query parameters. Iterating
     * over the set will return the names in order of their first occurrence.
     *
     * @param uri The URI to transform.
     * @return A map of decoded names.
     */
    @NonNull
    private static Map<String, String> encodedQueryParameters(@NonNull final Uri uri) {
        final HashMap<String, String> map = new HashMap<>();
        final String query = uri.getEncodedQuery();
        if (query != null) {

            String[] ands = query.split("&");
            for (String and : ands) {
                int splitIndex = and.indexOf("=");
                if (splitIndex != -1) {
                    map.put(and.substring(0, splitIndex), and.substring(splitIndex + 1));
                } else {
                    Log.w("ParametersParseError", "query: " + query);
                }
            }
        }
        return map;
    }


    @NonNull
    private String getIndexUrl() {
        return Urls.indexUrl(sourceUri);
    }
}
