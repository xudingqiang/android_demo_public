// src/main/cpp/native-lib.cpp
#include <jni.h>
#include <string>
#include <android/log.h>

#define LOG_TAG "JNI_DEMO"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT jstring JNICALL
Java_com_bella_android_demo_public_utils_JniUtils_stringFromJNI(JNIEnv* env, jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jint JNICALL
Java_com_bella_android_demo_public_JniUtils_add(JNIEnv* env, jobject /* this */, jint a, jint b) {
    return a + b;
}

extern "C" JNIEXPORT void JNICALL
Java_com_bella_android_demo_public_JniUtils_printHelloWorld(JNIEnv* env, jobject /* this */) {
LOGD("Hello World from JNI!");
}
