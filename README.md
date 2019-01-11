# WanAndroid
[![Download](https://api.bintray.com/packages/huxiaowei008/maven/FrameCore/images/download.svg) ](https://bintray.com/huxiaowei008/maven/FrameCore/_latestVersion)
[![License](http://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square) ](http://www.apache.org/licenses/LICENSE-2.0)

玩Android项目练习

## 下载
在项目的build.gradle里加上
```
buildscript {
    ...
    dependencies {
        ...
        classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.2'
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        ...
        mavenCentral()
        maven { url 'https://dl.bintray.com/kodein-framework/Kodein-DI/' }
    }
}
    
    
```
再在项目的build.gradle里添加依赖和插件
```
apply plugin: 'android-aspectjx'
```
```
implementation 'com.hxw.mycore:core:<latestVersion>'
```
```
android{
  compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
  }
}
```
## 使用
>注意:由于使用了谷歌的[Material](https://github.com/material-components/material-components-android),
所以在主题上要继承Material的主题。示例:
```
<style name="Theme.MyApp" parent="Theme.MaterialComponents.Light.NoActionBar">
    <!-- ... -->
</style>
```
继承上面的主题会更改一些控件样式(现在是只有按钮),如果不想改变样式就继承bridge,样式由自己控制,详细按钮参数可以查看
[MaterialButton](https://github.com/material-components/material-components-android/blob/master/docs/components/MaterialButton.md)
```
<style name="Theme.MyApp" parent="Theme.MaterialComponents.Light.NoActionBar.Bridge">
    <!-- ... -->
</style>
```
新建一个module继承ConfigModule接口,实现各个方法
```
class GlobalConfigModule : ConfigModule {
    override fun configGson(context: Context, builder: GsonBuilder) {
        builder.setDateFormat("yyyy-MM-dd HH:mm:ss")
    }

    override fun configOkHttp(context: Context, builder: OkHttpClient.Builder) {

    }

    override fun configRetrofit(context: Context, builder: Retrofit.Builder) {
        builder.baseUrl(WanApi.BASEURL)
    }

    override fun applyGlideOptions(context: Context, builder: GlideBuilder) {

    }
}
```
新建一个Application继承AbstractApplication,实现kodein,然后再Manifest中注册Application
>[Kodein-DI](https://github.com/Kodein-Framework/Kodein-DI) 一个kotlin方式的依赖注入
```
class WanKodeinApplication : AbstractApplication() {
    override val kodein: Kodein = Kodein.lazy {
        import(coreModule(this@WanKodeinApplication, GlobalConfigModule()))

        bind<WanApi>() with singleton { instance<Retrofit>().create(WanApi::class.java) }
    }
    
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
```

## 三方库(索引)
[KTX](https://github.com/android/android-ktx)

[kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines)

[Anko](https://github.com/Kotlin/anko)

[Gson](https://github.com/google/gson)

[Retrofit](https://github.com/square/retrofit)

[OkHttp](https://github.com/square/okhttp)

[Timber](https://github.com/JakeWharton/timber)

[Glide](https://github.com/bumptech/glide)

[RxKotlin](https://github.com/ReactiveX/RxKotlin)

[RxJava](https://github.com/ReactiveX/RxJava/tree/2.x)

[RxAndroid](https://github.com/ReactiveX/RxAndroid/tree/2.x)

[RxRelay](https://github.com/JakeWharton/RxRelay)

[RxBinding](https://github.com/JakeWharton/RxBinding)

~~[Kodein-DI](https://github.com/Kodein-Framework/Kodein-DI)~~

[koin](https://github.com/InsertKoinIO/koin)

[AutoDispose](https://github.com/uber/AutoDispose)

[RxDocs](https://github.com/mcxiaoke/RxDocs)

[Dagger2](https://github.com/google/dagger)

## License
```
Copyright huxiaowei008

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```