buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // Google Services plugin
        classpath("com.google.gms:google-services:4.3.15")
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
