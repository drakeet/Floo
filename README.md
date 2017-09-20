# Floo

[![Build Status](https://travis-ci.org/drakeet/Floo.svg)](https://travis-ci.org/drakeet/Floo)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/drakeet/Floo/blob/master/LICENSE)
![maven-central](https://img.shields.io/maven-central/v/me.drakeet.floo/floo.svg)

English Version | [中文版](README_CN.md)

An URL router supporting AOP, stack control, cross-page message, and dynamic routing.

<a href='https://play.google.com/store/apps/details?id=me.drakeet.floo.sample&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get sample on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width=200 height=77/></a>

## Getting started

In your `build.gradle`:

```groovy
dependencies {
    compile 'me.drakeet.floo:floo:1.0.5'
}
```

If you are using `com.android.tools.build:gradle:3.+`, use this instead:

```groovy
dependencies {
    implementation 'me.drakeet.floo:floo:1.0.5'
}
```

## Usage & Examples

```java
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Map<String, Target> mappings = new HashMap<>();
        mappings.put("m.drakeet.me/home", new Target("floo://drakeet.sdk/target"));
        mappings.put("m.drakeet.me/link", new Target("floo://drakeet.sdk/target"));
        mappings.put("m.drakeet.me/web", new Target("floo://drakeet.sdk/web"));
        mappings.put("m.drakeet.me/container", new Target("demo://m.drakeet.me/container"));

        Floo.configuration()
            .setDebugEnabled(BuildConfig.DEBUG)
            .addRequestInterceptor(new LogInterceptor("Request"))
            .addTargetInterceptor(new LogInterceptor("Target"))
            .addTargetNotFoundHandler(new WebHandler())
            .addTargetNotFoundHandler(new OpenDirectlyHandler())
            .addTargetNotFoundHandler(new TargetNotFoundToaster());

        Floo.apply(mappings);
    }
}
```

```java
Floo.navigation(this, "sdk://m.drakeet.me/home")
    .appendQueryParameter("date", "2017.9.11")
    .appendQueryParameter("user_id", "drakeet")
    .putExtra(TargetActivity.KEY_MAIL, mail)
    .start();
```

```java
// Allow incomplete URLs
Floo.navigation(this, "PureWriter").start();
```

```java
Floo.stack(this)
    .target(Urls.indexUrl("https://chunchun.io/page2"))
    .result("abc")
    .start();
```

```java
Floo.stack(this)
    .popCount(2)
    .result(anything)
    .start();
```

```java
Floo.stack(this).popCount(3).start();
```

## Principle

For example, if we call the following code:

```java
Floo.navigation(context, "https://play.google.com/store/apps/details")
    .appendQueryParameter("id", "com.drakeet.purewriter")
    .start();
```

At the beginning, Floo will build the URL and parameters to a full URL, like as: [https://play.google.com/store/apps/details?id=com.drakeet.purewriter](https://play.google.com/store/apps/details?id=com.drakeet.purewriter), and ask your registered `RequestInterceptor`s: _"Do you want to intercept and handle the URL?"_

Every your registered `RequestInterceptor` will receive the full URL one by one. If someone deals with it and returns `true`, the link ends.

Otherwise, Floo will use the `authority(host:port)` + `path` to get an **index key**. For this example, it is `play.google.com` + `/store/apps/details` -> `play.google.com/store/apps/details`.

![url-parts.png](url-parts.png)

Then, Floo uses the **index key** to find a registered target URL / URI. If Floo finds it, Floo will transfer or merge the parameters of the original URL to the new URL. Otherwise, Floo will create a TargetNotFound event, and dispatch it to all of your registered `TargetNotFoundHandler`s one by one. If someone deals with it and returns `true`, the link ends. If nobody deals with it, the link also ends.

So what if Floo finds a target and generates a new URL? 

At this point, Floo will send the new URL one by one to your registered `TargetInterceptor`s. If someone deals with it and returns `true`, the link ends. 

Otherwise, Floo comes to the last step, it will use this new URL to create an Intent, and start the Intent. This new URL may be associated with an `Activity`, so the `Activity` will be opened.


## More Interfaces

```java
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
```

```java
public interface StackStates {

    interface Target {
        @NonNull @CheckResult Flow popCount(@IntRange(from = 1) int count);
        @NonNull @CheckResult Flow target(@NonNull String indexKey);
    }


    interface Flow {
        @NonNull @CheckResult End result(@NonNull Object result);
        void start();
    }


    interface End {
        void start();
    }
}
```

## Some built-in extensions

- `LogInterceptor`
- `OpenDirectlyHandler`


License
-------

    Copyright 2017 drakeet. https://github.com/drakeet

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
