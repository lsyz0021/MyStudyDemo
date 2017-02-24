# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/lcw/android_develop_tools/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

# 极光推送混淆
-dontoptimize
-dontpreverify
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
#==================gson&protobuf==========================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}
-keep class com.google.protobuf.** {*;}

# 极光短息验证
-keep class cn.jpush.sms.SMSSDK {*;}
-keep class cn.jpush.sms.listener.** {*;}
-keep class cn.jpush.sms.utils.DeviceInfo {*;}
#========================gson & protobuf================================
-dontwarn com.google.**
-keep class com.google.gson.jsms.annotations.Until {*;}
#Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep public abstract class com.google.gson.jsms.internal.UnsafeAllocator { *; }