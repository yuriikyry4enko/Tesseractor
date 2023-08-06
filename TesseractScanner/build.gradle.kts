plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
}

android {
    // Your existing Android configurations...
}

dependencies {
    // Other dependencies you might have...

    // Add the Tesseract OCR dependency here
    implementation 'com.rmtheis:tess-two:9.0.0'
}
