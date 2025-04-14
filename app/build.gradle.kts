plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.techaid"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.techaid"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // ✅ Firebase BoM (bản mới nhất bạn đang dùng: 33.12.0)
    implementation(platform(libs.firebase.bom))

    // ✅ Analytics (nếu cần theo dõi hành vi người dùng)
    implementation(libs.firebase.analytics)

    // ✅ Auth: Đăng nhập Email, Google, Facebook,...
    implementation(libs.firebase.auth.ktx)

    // ✅ Firestore: Lưu trữ dữ liệu dạng NoSQL
    implementation(libs.firebase.firestore.ktx)

    // ✅ Storage: Upload ảnh, file,...
    implementation(libs.firebase.storage.ktx)
    val nav_version = "2.8.9"

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material.icons.extended)

}