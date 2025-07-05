-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

#-keepattributes SourceFile,LineNumberTable

-dontwarn **
-dontnote **

# 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 5
 
# 混合时不使用大小写混合，混合后的类名为小写
-dontusemixedcaseclassnames
 
# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses
 
# 指定不去忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers
 
# 这句话能够使我们的项目混淆后产生映射文件
# 包含有类名->混淆后类名的映射关系
-verbose
 
# 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-dontpreverify
 
# 保留Annotation不混淆 这在JSON实体映射时非常重要，比如fastJson
-keepattributes *Annotation*,InnerClasses
 
# 避免混淆泛型
-keepattributes Signature
 
# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable
 
# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/cast,!field/*,!class/merging/*
 
# 忽略警告
-ignorewarnings
 
# 设置是否允许改变作用域
-allowaccessmodification
 
# 把混淆类中的方法名也混淆了
-useuniqueclassmembernames
 
# apk 包内所有 class 的内部结构
-dump class_files.txt
 
# 未混淆的类和成员
-printseeds seeds_txt
 
# 列出从apk中删除的代码
-printusage unused.txt
 
# 混淆前后的映射
-printmapping mapping.txt
#不能使用混淆
 
#1、反射中使用的元素，需要保证类名、方法名、属性名不变，否则反射会有问题。
 
#2、最好不让一些bean 类混淆
 
#3、四大组件不能混淆，四大组件必须在 manifest 中注册声明，而混淆后类名会发生更改，这样不符合四大组件的注册机制。
 
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgent
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Fragment
-keep public class * extends android.view.view
-keep public class com.android.vending.licensing.ILicensingService
-keepattributes *Annotation*
 
-keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
}
-keepclasseswithmembernames class * {
  native <methods>;
}
 
-keepattributes *JavascriptInterface*
 
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
  public *;
}
 
-keepclassmembers class * extends android.webkit.WebViewClient {
  public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
  public boolean *(android.webkit.WebView, java.lang.String);
}
 
-keepclassmembers class * extends android.webkit.WebViewClient {
  public void *(android.webkit.WebView, java.lang.String);
}
-keep class * implements android.os.Parcelable {
 public static final android.os.Parcelable$Creator *;
}
 
-keep class * implements java.io.Serializable {
  public *;
}
 
-keepclassmembers class * implements java.io.Serializable {
  static final long serialVersionUID;
  private static final java.io.ObjectStreamField[] serialPersistentFields;
  !static !transient <fields>;
  private void writeObject(java.io.ObjectOutputStream);
  private void readObject(java.io.ObjectInputStream);
  java.lang.Object writeReplace();
  java.lang.Object readResolve();
}
 
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
 
# For using GSON @Expose annotation
-keepattributes *Annotation*
 
# Gson specific classes
-dontwarn sun.misc.**
-keep class com.google.gson.stream.** { *; }
 
# Application classes that will be serialized/deserialized over Gson
 
# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
 
# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
#-assumenosideeffects class android.util.Log {
 
#public static boolean isLoggable(java.lang.String, int);
#
#public static int v(...);
#
#public static int i(...);
#
#public static int w(...);
#
#public static int d(...);
#
#public static int e(...);
#
#}
 
# 保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
 public void *(android.view.View);
}
-dontwarn org.apache.**                        #不警告此包
-keep class org.apache.** {*;}
 
-keep class org.apache.commons.logging.** { *; }
-keep class org.java_websocket.** { *; }
 
#-------------- okhttp3 start-------------
# OkHttp3
# https://github.com/square/okhttp
# okhttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.* { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
 
# okhttp 3
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
 
# Okio
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }
 
-keep class de.greenrobot.event.** {*;}
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}
#EventBus
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
 
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
 
#-----------------------------------------------------------
 
-keep class com.yc.toollib.** { *; }
-keep class de.hdodenhof.** { *; }
-keep class io.reactivex.rxjava2.** { *; }
-keep class com.xtc.** { *; }
#-keep class android.** { *; }