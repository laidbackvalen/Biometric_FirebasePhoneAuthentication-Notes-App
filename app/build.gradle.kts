plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.valenpatel.notes"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.valenpatel.notes"
        minSdk = 24
        targetSdk = 34
        versionCode = 5
        versionName = "1.5.2"

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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-storage:21.0.0")
    implementation("com.google.firebase:firebase-messaging:24.0.0")
    implementation("androidx.activity:activity-ktx:1.9.1")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.activity:activity:1.9.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    //Firebase Dependency
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-database")
    //FCM
    implementation("com.google.firebase:firebase-messaging:24.0.0")

    //LottieFiles
    implementation("com.airbnb.android:lottie:6.0.1")

    //COUNTRY CODE PICKER
    implementation("com.hbb20:ccp:2.5.0")

//    //PIN VIEW
    implementation("io.github.chaosleung:pinview:1.4.4")

    //GLide Dependency
    implementation("com.github.bumptech.glide:glide:4.16.0")
    //ImageZommInOut
    implementation("com.github.MikeOrtiz:TouchImageView:1.4.1") // last SupportLib version

    //LOTTIEFILES
    implementation("com.airbnb.android:lottie:6.0.1")

    implementation("androidx.recyclerview:recyclerview:1.3.2")

    implementation("com.google.android.material:material:1.12.0")


    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")


    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")

}