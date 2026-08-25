plugins {
    id("com.android.application")
}

android {
    namespace = "com.company.tdybtrfid"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.company.tdybtrfid"
        minSdk = 21
        targetSdk = 28
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(files("libs\\ModuleAPI_Android_BT.jar"))
    implementation(files("libs\\jxl.jar"))
    implementation(files("libs\\poi.jar"))
    implementation(files("libs\\xUtils-2.5.5.jar"))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.squareup.picasso:picasso:2.8")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    // implementation 'androidx.palette:palette:1.1.0'

    implementation ("com.android.support:palette-v7:28.0.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.1")
    // For control over item selection of both touch and mouse driven selection
    implementation ("androidx.recyclerview:recyclerview-selection:1.1.0")
}