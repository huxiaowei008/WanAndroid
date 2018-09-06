# WanAndroid
[![Download](https://api.bintray.com/packages/huxiaowei008/maven/FrameCore/images/download.svg) ](https://bintray.com/huxiaowei008/maven/FrameCore/_latestVersion)
[![License](http://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square) ](http://www.apache.org/licenses/LICENSE-2.0)

玩Android项目练习

## 下载
```gradle
implementation 'com.hxw.mycore:<latestVersion>'
```
## 使用
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