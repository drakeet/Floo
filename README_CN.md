# Floo

[![Build Status](https://travis-ci.org/drakeet/Floo.svg)](https://travis-ci.org/drakeet/Floo)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/drakeet/Floo/blob/master/LICENSE)
![maven-central](https://img.shields.io/maven-central/v/me.drakeet.floo/floo.svg)

一个支持 AOP、栈控制、跨页面带信、和动态变更映射的 URL 路由库。

<a href='https://play.google.com/store/apps/details?id=me.drakeet.floo.sample&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get sample on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width=200 height=77/></a>

## 快速开始

在你的 `build.gradle` 文件里添加:

```groovy
dependencies {
    compile 'me.drakeet.floo:floo:1.0.5'
}
```

如果你使用 `com.android.tools.build:gradle:3.+`, 改用:

```groovy
dependencies {
    implementation 'me.drakeet.floo:floo:1.0.5'
}
```

## 用法用例

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
// 允许不完整的 URL
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

## 原理

例如我们调用了下列代码：

```java
Floo.navigation(context, "https://play.google.com/store/apps/details")
    .appendQueryParameter("id", "com.drakeet.purewriter")
    .start();
```

在一开始，Floo 会将其中的 URL 和参数组装成一个完整的 URL：[https://play.google.com/store/apps/details?id=com.drakeet.purewriter](https://play.google.com/store/apps/details?id=com.drakeet.purewriter)，然后询问你那些注册过的  `RequestInterceptor` 们：_“你们要需要拦截和处理这个 URL 吗？”_

那些你注册过的 `RequestInterceptor` 们将会依序收到这个完整的 URL。如果其中某一个 `RequestInterceptor` 处理并返回了 `true`，则传播就直接结束没有后续了。

否则，Floo 将会使用 `authority(host:port)` + `path` 来获得一个 **index key** 。对于上面我们的例子，就是 `play.google.com` 和 `/store/apps/details` 构成了 `play.google.com/store/apps/details`。

![url-parts.png](url-parts.png)

接下来，Floo 将使用这个 **index key** 来寻找一个已注册过的目标 URL / URI。如果 Floo 找到了，它会将目标 URL 与源 URL 进行参数转移、合并形成新的 URL。否则，Foo 将产生一个 TargetNotFound 事件，然后依序分发给所有已经注册的 `TargetNotFoundHandler` 们。如果其中某一个 `TargetNotFoundHandler` 处理并返回了 `true`，则传播就直接结束没有后续了，如果没有任何 Handler 来处理它，传播也会结束没有后续。

那么如果 Floo 找到了一个目标并且生成了一个新的 URL 呢？

这时候，Floo 会将这个新的 URL 依序发送给注册过的 `TargetInterceptor` 们。如果某一个 `TargetInterceptor` 处理并返回了 `true`，则传播结束没有后续了。

否则，Floo 将会执行最后一步操作，即通过那个新的 URL 创建出 Intent 对象，打开这个 Intent。这个新的 URL 与某个 `Activity` 是关联的，因此 `Activity` 被会被启动与打开。


## 更多接口

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

## 一些正在完善中的扩展

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
