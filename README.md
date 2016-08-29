# SmartKit
一组android常用组件（网络，下载，数据库，缓存，ui组件，utils类库等）
网络库：okHttp
代码位置：com/kit/cn/library/networkkit
一.选型
1. 选型原则
（1）一个库只做一件事，职责单一原则，假如一个库即可以网络请求，同时又可以图片加载，又可以数据存储等等。
（2）不建议使用不维护，大而全的开源网络库，虽然功能强大但依赖太多，项目有可能只用一部分，额外有移除成本。
2.基于上面原则的基础，对比 Volley、OkHttp、Retrofit三个比较强大的网络库
三个库的介绍：
(1) OkHttp
我们知道在 Android 开发中是可以直接使用现成的 api 进行网络请求的，就是使用 HttpClient、HttpUrlConnection 进行操作，目前 HttpClient 已经被废弃，而 android-async-http 是基于 HttpClient 的，我想可能也是因为这个原因作者放弃维护。
 而 OkHttp 是 Square 公司开源的针对 Java 和 Android 程序，封装的一个高性能 http 请求库，所以它的职责跟 HttpUrlConnection 是一样的，支持 spdy、http 2.0、websocket ，支持同步、异步，而且 OkHttp 又封装了线程池，封装了数据转换，封装了参数使用、错误处理等，api 使用起来更加方便。可以把它理解成是一个封装之后的类似 HttpUrlConnection 的一个东西，但是你在使用的时候仍然需要自己再做一层封装，这样才能像使用一个框架一样更加顺手。
OkHttp  Github地址：http://square.github.io/okhttp/
(2) Volley
Volley 是 Google 官方出的一套小而巧的异步请求库，该框架封装的扩展性很强，支持 HttpClient、HttpUrlConnection，甚至支持 OkHttp，而且 Volley 里面也封装了 ImageLoader ，所以如果你愿意你甚至不需要使用图片加载框架，不过这块功能没有一些专门的图片加载框架强大，对于简单的需求可以使用，对于稍复杂点的需求还是需要用到专门的图片加载框架。Volley 也有缺陷，比如不支持 post 大数据，所以不适合上传文件。
关于 Volley GitHub地址：https://github.com/bacy/volley
(3)Retrofit
Retrofit 是 Square 公司出品的默认基于 OkHttp 封装的一套 RESTful 网络请求框架，提下>RESTful 可以说是目前流行的一套 api 设计的风格。Retrofit 的封装可以说是很强大，里面涉及到一堆的设计模式，你可以通过注解直接配置请求，你可以使用不同的 http 客户端，虽然默认是用 http ，可以使用不同 Json Converter 来序列化数据，同时提供对 RxJava 的支持，使用 Retrofit + OkHttp + RxJava + Dagger2 可以说是目前比较好的一套框架，但是有比较高的门槛。
Retrofit Github地址：http://square.github.io/retrofit/
三个库之间对比：
(1)Volley  OkHttp对比
毫无疑问 Volley 的优势在于封装的更好，而使用 OkHttp 你需要有足够的能力再进行一次封装。而 OkHttp 的优势在于性能更高，因为 OkHttp 基于 NIO 和 Okio ，所以性能上要比 Volley更快。这里简单提下 IO 和 NIO 的概念，这两个都是 Java 中的概念，如果我从硬盘读取数据，第一种方式就是程序一直等，数据读完后才能继续操作，这种是最简单的也叫阻塞式 IO，还有一种就是你读你的，我程序接着往下执行，等数据处理完你再来通知我，然后再处理回调。而第二种就是 NIO 的方式，非阻塞式。所以 NIO 当然要比 IO 的性能要好了， 而 Okio 是 Square 公司基于 IO 和 NIO 基础上做的一个更简单、高效处理数据流的一个库。理论上如果 Volley 和 OkHttp 对比的话，我更倾向于使用 Volley，因为 Volley 内部同样支持使用 OkHttp ，这点 OkHttp 的性能优势就没了，而且 Volley 本身封装的也更易用，扩展性更好些。

(2) OkHttp Retrofit对比
Retrofit 默认是基于 OkHttp 而做的封装，这点来说没有可比性，肯定首选 Retrofit。

(3)Volley Retrofit对比
这两个库都做了非常不错的封装，但是 Retrofit 解耦的更彻底，尤其 Retrofit 2.0 出来，Jake 对之前 1.0 设计不合理的地方做了大量重构，职责更细分，而且 Retrofit 默认使用 OkHttp ，性能上也要比 Volley 占优势，再有如果你的项目如果采用了 RxJava ，那更该使用 Retrofit 。
基于综上考虑，一般项目，都需要上传文件，图片这样的大数据，所以采用 OkHttp 稳妥点，不过OkHttp需要自己封装，才能够更好的写网络请求，不然很多重复代码。
二.设计
1.类设计

okhttp类设计可以看源码：github:https://github.com/square/okhttp
2.关键类说明
(1)OkHttpTask:核心类，负责构建get,post等网路请求的模版方法及builder出业务参数和okhttp各初始化参数，比如缓存时间等。
(2)BaseBodyRequest：负责根据不同的请求方式和参数，生成不同的RequestBody
(3)BaseRequest：负责根据当前的请求参数，生成对应的 Call 任务（同步、异步），删除、更新缓存及成功失败回调等。
(4)PostRequest,GetRequest：构建post,get请求的requestBody请求体。
(5)RealCall：okhttp类，负责执行网络请求
(6)HttpHeaders：公共请求头
(7)HttpParams：公共请求参数
 
三.API使用说明
目前对以下需求进行了封装
一般的get请求
一般的post请求
基于Http Post的文件上传（类似表单）
文件下载/加载图片
上传下载的进度回调
支持取消某个请求
支持自定义Callback
支持HEAD、DELETE、PATCH、PUT
支持session的保持
支持自签名网站https的访问，提供方法设置下证书就行
配置OkhttpClient
默认情况下，将直接使用okhttp默认的配置生成OkhttpClient，如果你有任何配置，记得在Application中调用initClient方法进行设置。
public class MyApplication extends Application {
@Override public void onCreate() {
super.onCreate(); OkHttpClient okHttpClient = new OkHttpClient.Builder() //
.addInterceptor(new LoggerInterceptor("TAG"))
.connectTimeout(10000L, TimeUnit.MILLISECONDS)
.readTimeout(10000L, TimeUnit.MILLISECONDS) //其他配置
.build(); OkHttpUtils.initClient(okHttpClient); } }
别忘了在AndroidManifest中设置。
对于Cookie(包含Session)
对于cookie一样，直接通过cookiejar方法配置，参考上面的配置过程。
CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext())); 
OkHttpClient okHttpClient = new OkHttpClient.Builder() .cookieJar(cookieJar) //其他配置 .build(); 
OkHttpUtils.initClient(okHttpClient); 
目前项目中包含：
PersistentCookieStore //持久化cookie
初始化OkhttpClient时，通过设置拦截器实现，框架中提供了一个LoggerInterceptor，当然你可以自行实现一个Interceptor 。
 OkHttpClient okHttpClient = new OkHttpClient.Builder() .addInterceptor(new LoggerInterceptor("TAG")) //其他配置 .build();
 OkHttpUtils.initClient(okHttpClient);
SerializableHttpCookie //持久化cookie
MemoryCookieStore //cookie信息存在内存中
此外，对于持久化cookie还可以使用https://github.com/franmontiel/PersistentCookieJar.
相当于框架中只是提供了几个实现类，你可以自行定制或者选择使用。
对于Log
初始化OkhttpClient时，通过设置拦截器实现，框架中提供了一个LoggerInterceptor，当然你可以自行实现一个Interceptor 。
 OkHttpClient okHttpClient = new OkHttpClient.Builder() .addInterceptor(new LoggerInterceptor("TAG")) //其他配置 .build();
 OkHttpUtils.initClient(okHttpClient);
对于Https
依然是通过配置即可，框架中提供了一个类HttpsUtils
设置可访问所有的https网站
HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
OkHttpClient okHttpClient = new OkHttpClient.Builder() .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager) //其他配置 .build(); 
OkHttpUtils.initClient(okHttpClient); 
设置具体的证书
HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(证书的inputstream, null, null); 
OkHttpClient okHttpClient = new OkHttpClient.Builder() .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)) //其他配置 .build(); 
OkHttpUtils.initClient(okHttpClient); 
双向认证
HttpsUtils.getSslSocketFactory( 证书的inputstream, 本地证书的inputstream, 本地证书的密码) 
同样的，框架中只是提供了几个实现类，你可以自行实现SSLSocketFactory，传入sslSocketFactory即可。
其他用法示例
GET请求
OkHttpUtils.get(Urls.URL_METHOD)
.tag(this)
.headers("header1", "headerValue1")
.params("param1", "paramValue1")
.execute(new MethodCallBack<>(this, RequestInfo.class));
POST请求
OkHttpUtils.post(Urls.URL_METHOD) 
.tag(this) 
.headers("header1", "headerValue1") 
.params("param1", "paramValue1") 
.execute(new MethodCallBack<>(this, RequestInfo.class));
Post String
OkHttpUtils.post(Urls.URL_TEXT_UPLOAD)
 .tag(this)
 .headers("header1", "headerValue1")
 .params("param1", "paramValue1")
 .postString("这是要上传的长文本数据！")
 .execute(new TextCallBack<>(this, RequestInfo.class));
提交一个Gson字符串到服务器端。
Post File
OkHttpUtils .postFile() .url(url) .file(file) .build() .execute(new MyStringCallback());
将文件作为请求体，发送到服务器。
Post表单形式上传文件
ArrayList<File> files = new ArrayList<>();
if (imageItems != null && imageItems.size() > 0) {
for (int i = 0; i < imageItems.size(); i++) {
files.add(new File(imageItems.get(i).path));
 }
}
//拼接参数
 OkHttpUtils.post(Urls.URL_FORM_UPLOAD)//
 .tag(this)//
 .headers("header1", "headerValue1")//
 .headers("header2", "headerValue2")//
 .params("param1", "paramValue1")//
 .params("param2", "paramValue2")//
// .params("file1",new File("文件路径")) //这种方式为一个key，对应一个文件
// .params("file2",new File("文件路径"))
// .params("file3",new File("文件路径"))
 .addFileParams("file", files) // 这种方式为同一个key，上传多个文件
 .execute(new ProgressUpCallBack<>(this, RequestInfo.class));
支持单个多个文件，addFile的第一个参数为文件的key，即类别表单中<input type="file" name="mFile"/>的name属性。
自定义CallBack
目前内部包含StringCallBack,FileCallBack,BitmapCallback，可以根据自己的需求去自定义Callback，例如希望回调User对象：
public abstract class UserCallback extends Callback<User> {
@Override public User parseNetworkResponse(Response response) throws IOException {
String string = response.body().string();
User user = new Gson().fromJson(string, User.class);
return user;
} }
OkHttpUtils .get(Urls.URL_METHOD)
.params("username", "hyman")
.params("password", "123")
.build() .execute(new UserCallback() {
@Override public void onError(Request request, Exception e) {
mTv.setText("onError:" + e.getMessage());
}
@Override public void onResponse(User response) {
mTv.setText("onResponse:" + response.username);
} });
通过parseNetworkResponse回调的response进行解析，该方法运行在子线程，所以可以进行任何耗时操作，详细参见sample。
下载文件
OkHttpUtils.get(Urls.URL_DOWNLOAD)//
 .tag(this)
 .headers("header1", "headerValue1")
 .params("param1", "paramValue1")
 .execute(new DownloadFileCallBack(Environment.getExternalStorageDirectory() + "/temp", "OkHttpUtils.apk"));
 
private class DownloadFileCallBack extends FileCallback {

public DownloadFileCallBack(String destFileDir, String destFileName) {
    super(destFileDir, destFileName);
 }

@Override
 public void onBefore(BaseRequest request) {
      //正在下载中
 }

@Override
 public void onResponse(boolean isFromCache, File file, Request request, Response response) {
     //下载完成
 }

@Override
 public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
    //下载进度
 }

@Override
 public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
     super.onError(isFromCache, call, response, e);
    //下载出错
 }
}

注意下载文件可以使用FileCallback，需要传入文件需要保存的文件夹以及文件名。
显示图片
OkHttpUtils .get(Urls.URL.IMG) .build() .execute(new BitmapCallback() {
@Override public void onError(Request request, Exception e) {
mTv.setText("onError:" + e.getMessage());
}
@Override public void onResponse(Bitmap bitmap) {
mImageView.setImageBitmap(bitmap);
} });
显示图片，回调传入BitmapCallback即可。
上传下载的进度显示
new Callback<T>() { 
@Override public void inProgress(float progress) {
//use progress: 0 ~ 1
} }
callback回调中有inProgress方法，直接复写即可。
HEAD、DELETE、PUT、PATCH
OkHttpUtils.put(Urls.URL_METHOD) //also can use head,delete, patch
.tag(this) 
.headers("header1", "headerValue1") 
.params("param1", "paramValue1") 
.execute(new MethodCallBack<>(this, RequestInfo.class));
如果需要requestBody，例如：PUT、PATCH，自行构造进行传入。
同步的请求
Response response = OkHttpUtils
.get(url) 
.tag(this)
.build()
.execute(); 
execute方法不传入callback即为同步的请求，返回Response。
取消单个请求
 RequestCall call = OkHttpUtils.get().url(url).build();
 call.cancel();
根据tag取消请求
目前对于支持的方法都添加了最后一个参数Object tag，取消则通过OkHttpUtils.cancelTag(tag)执行。
例如：在Activity中，当Activity销毁取消请求：
OkHttpUtils 
.get() 
.url(url) 
.tag(this) 
.build() 
@Override protected void onDestroy() { 
 super.onDestroy(); //可以取消同一个tag的 
 OkHttpUtils.cancelTag(this);//取消以Activity.this作为tag的请求 
} 
比如，当前Activity页面所有的请求以Activity对象作为tag，可以在onDestory里面统一取消。
