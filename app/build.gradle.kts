plugins {
    alias(libs.plugins.android.application)

}

android {
    namespace = "com.example.androidreto2grupo4"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.androidreto2grupo4"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packaging {
        resources {
            excludes.addAll(
                listOf(
                    "META-INF/NOTICE.md",
                    "META-INF/NOTICE.txt",
                    "META-INF/LICENSE.md",
                    "META-INF/LICENSE.txt"
                )
            )
        }
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation ("com.airbnb.android:lottie:6.0.0")
    implementation ("com.google.code.gson:gson:2.10.1") // Json
    implementation ("com.sun.mail:android-mail:1.6.7")
    implementation ("com.sun.mail:android-activation:1.6.7")
    implementation ("com.github.bumptech.glide:glide:4.15.1")

    testImplementation(libs.junit)

    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


}