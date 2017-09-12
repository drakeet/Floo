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
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

/**
 * @author drakeet
 */
@RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public abstract class ActivityOnResumeCallback implements Application.ActivityLifecycleCallbacks {

    @Override
    public abstract void onActivityResumed(Activity activity);


    @Override
    public final void onActivityCreated(Activity activity, Bundle savedInstanceState) {}


    @Override
    public final void onActivityStarted(Activity activity) {}


    @Override
    public final void onActivityPaused(Activity activity) {}


    @Override
    public final void onActivityStopped(Activity activity) {}


    @Override
    public final void onActivitySaveInstanceState(Activity activity, Bundle outState) {}


    @Override
    public final void onActivityDestroyed(Activity activity) {}
}
