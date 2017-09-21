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
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.drakeet.floo.Preconditions.checkNotNull;

/**
 * A delegate for floo to hold the configuration fields
 *
 * @author drakeet
 */
public class Configuration {

    private @NonNull final TargetMap targetMap;
    private @NonNull final List<TargetNotFoundHandler> targetNotFoundHandlers;
    private @NonNull final List<Interceptor> requestInterceptors;
    private @NonNull final List<Interceptor> targetInterceptors;
    private @NonNull IntentHandler intentHandler = new DefaultIntentHandler();
    private boolean debug = false;
    private boolean stackObserverInitialized = false;


    Configuration() {
        targetMap = new TargetMap(Collections.<String, Target>emptyMap());
        targetNotFoundHandlers = new ArrayList<>();
        requestInterceptors = new ArrayList<>();
        targetInterceptors = new ArrayList<>();
    }


    void apply(@NonNull final Map<String, Target> map) {
        checkNotNull(map);
        targetMap.set(new HashMap<>(map));
    }


    @NonNull
    public Configuration addRequestInterceptor(@NonNull Interceptor requestInterceptor) {
        checkNotNull(requestInterceptor);
        requestInterceptors.add(requestInterceptor);
        return this;
    }


    @NonNull
    public Configuration addTargetInterceptor(@NonNull Interceptor responseInterceptor) {
        checkNotNull(responseInterceptor);
        targetInterceptors.add(responseInterceptor);
        return this;
    }


    @NonNull
    public Configuration addTargetNotFoundHandler(@NonNull TargetNotFoundHandler handler) {
        checkNotNull(handler);
        targetNotFoundHandlers.add(handler);
        return this;
    }


    /**
     * Set debug enabled for logging including interceptors, warnings, and errors.
     * By default the debug enabled is false.
     *
     * @param debug if true {@link Floo} will print some logs if need, otherwise false
     * @return {@link Configuration} for more configuring
     */
    @NonNull
    public Configuration setDebugEnabled(boolean debug) {
        this.debug = debug;
        return this;
    }


    public boolean isDebugEnabled() { return debug; }


    public void setIntentHandler(@NonNull IntentHandler intentHandler) {
        checkNotNull(intentHandler);
        this.intentHandler = intentHandler;
    }


    @NonNull
    public IntentHandler getIntentHandler() {
        return intentHandler;
    }


    @NonNull
    public List<TargetNotFoundHandler> getTargetNotFoundHandlers() {
        return targetNotFoundHandlers;
    }


    @NonNull
    public List<Interceptor> getRequestInterceptors() {
        return requestInterceptors;
    }


    @NonNull
    public List<Interceptor> getTargetInterceptors() {
        return targetInterceptors;
    }


    @Nullable
    public Target getTarget(@NonNull String url) {
        checkNotNull(url);
        return targetMap.getTarget(url);
    }


    @NonNull
    public Map<? extends String, ? extends Target> getTargetMap() {
        return targetMap.get();
    }


    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    void initStackObserverIfNeed(@NonNull Activity activity) {
        if (!stackObserverInitialized) {
            activity.getApplication().registerActivityLifecycleCallbacks(
                new ActivityOnResumeCallback() {

                    @Override
                    public void onActivityResumed(@NonNull Activity activity) {
                        StackManager.onActivityResumed(activity);
                    }
                });
            stackObserverInitialized = true;
        }
    }
}
