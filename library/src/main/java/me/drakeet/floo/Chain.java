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
public class Chain {

    private @NonNull final Uri requestUri;
    private boolean proceed = true;


    public Chain(@NonNull Uri requestUri) {
        this.requestUri = requestUri;
    }


    private Chain(@NonNull Uri requestUri, boolean proceed) {
        this.requestUri = requestUri;
        this.proceed = proceed;
    }


    @NonNull
    public Uri request() {
        return requestUri;
    }


    @NonNull
    public Chain proceed(@NonNull final Uri requestUri) {
        return new Chain(requestUri, true);
    }


    @NonNull
    public Chain abort() {
        return new Chain(requestUri, false);
    }


    public boolean isProceed() {
        return proceed;
    }
}
