# Floo

An URL router supporting AOP, stack control, cross-page message, and dynamic routing.

## Getting started

In your `build.gradle`:

```groovy
dependencies {
    compile 'me.drakeet.floo:floo:1.0.2'
}
```

If you are using `com.android.tools.build:gradle:3.+`, use this instead:

```groovy
dependencies {
    implementation 'me.drakeet.floo:floo:1.0.2'
}
```

## Usage

Initialize at first. Example: 

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

Navigation usage example:

```java
Floo.navigation(this, "sdk://m.drakeet.me/home")
    .appendQueryParameter("date", "2017.9.11")
    .appendQueryParameter("user_id", "drakeet")
    .putExtra(TargetActivity.KEY_MAIL, mail)
    .start();
```

Stack usage examples:

```java
Floo.stack(this)
    .target(Urls.indexUrl("https://chunchun.io/page2"))
    .result("https://play.google.com/store/apps/details?id=com.drakeet.purewriter")
    .start();
```

```java
Floo.stack(this)
    .popCount(2)
    .result(anything)
    .start();
```

```java
Floo.stack(this).popCount(1).start();
```

### Interfaces

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