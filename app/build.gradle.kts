plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.talabalarniroyxatgaolish"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.talabalarniroyxatgaolish"
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
        viewBinding = true
    }
}

dependencies {
    //retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    //okhttp3
    implementation(libs.okhttp)
    //interceptor
    implementation(libs.logging.interceptor)
    //picasso
    implementation(libs.picasso)
    implementation(libs.google.firebase.storage.ktx)
    //chucker
    debugImplementation(libs.library)
    releaseImplementation(libs.library.no.op)
    //navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    //viewmodelscope
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // https://mavenlibs.com/maven/dependency/tech.thdev/flow-call-adapter-factory
    implementation("tech.thdev:flow-call-adapter-factory:1.0.0")
    //viewmodel
    implementation (libs.androidx.lifecycle.viewmodel.ktx.v220)
    implementation (libs.kotlinx.coroutines.core)
    //picasso
    implementation("com.squareup.picasso:picasso:2.8")
    //glide
    implementation("com.github.bumptech.glide:glide:4.15.0")
    //calendar
    implementation("com.github.afsalkodasseri:KalendarView:2.7")
    //lottie animation
    implementation("com.airbnb.android:lottie:5.2.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}