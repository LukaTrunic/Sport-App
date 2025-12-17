plugins {
    alias(libs.plugins.android.application)
    // apply the plugin by its id, **not** via the BOM alias
    id("com.google.gms.google-services")
}

android {
    namespace = "rs.ac.singidunum"
    compileSdk = 34

    defaultConfig {
        applicationId = "rs.ac.singidunum"           // ← must match Firebase
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    dependencies {
        // ––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––
        // Firebase (managed by the BOM, so you only list the KTX artifacts)
        implementation(platform("com.google.firebase:firebase-bom:32.2.0"))
        implementation("com.google.firebase:firebase-auth-ktx")
        implementation("com.google.firebase:firebase-firestore-ktx")
        implementation("com.google.firebase:firebase-storage-ktx")
        implementation("com.google.firebase:firebase-messaging-ktx")

        // ––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––
        // Image loading
        implementation("com.github.bumptech.glide:glide:4.15.1")
        annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

        // ––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––
        // Networking
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation("com.squareup.okhttp3:okhttp:4.11.0")

        // ––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––
        // Location
        implementation ("org.osmdroid:osmdroid-android:6.1.14")

        // ––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––
        // AndroidX and other utilities
        implementation(libs.appcompat)
        implementation(libs.material)
        implementation(libs.constraintlayout)
        implementation(libs.cardview)
        implementation(libs.swiperefreshlayout)
        implementation(libs.activity)         // androidx.activity:activity-ktx
        implementation(libs.gson)

        // ––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––––
        // Testing
        testImplementation(libs.junit)
        androidTestImplementation(libs.ext.junit)
        androidTestImplementation(libs.espresso.core)
    }
}
