plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.samgye.orderapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.samgye.orderapp"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // gson
    implementation("com.google.code.gson:gson:2.11.0")

    // Retrofit2 라이브러리
    implementation("com.squareup.retrofit2:retrofit:2.11.0")

    // Gson converter 라이브러리
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // Scalars converter 라이브러리
    implementation("com.squareup.retrofit2:converter-scalars:2.11.0")

    // logging-interceptor는 반환된 모든 응답에 대해 로그 문자열을 생성합니다.
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
}