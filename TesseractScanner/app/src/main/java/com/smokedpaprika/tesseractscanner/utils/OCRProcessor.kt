package com.smokedpaprika.tesseractscanner.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.googlecode.tesseract.android.TessBaseAPI

class OCRProcessor {
    fun performOCR(activity: Activity, imageUri: Uri, tessBaseAPI: TessBaseAPI): String? {
        activity.contentResolver.openInputStream(imageUri)?.use { inputStream ->
            val bitmap = BitmapFactory.decodeStream(inputStream)
            return performOCRInternal(bitmap, tessBaseAPI)
        }
        return null
    }

    fun performOCR(imageBitmap: Bitmap, tessBaseAPI: TessBaseAPI): String? {
        return performOCRInternal(imageBitmap, tessBaseAPI)
    }

    private fun performOCRInternal(bitmap: Bitmap, tessBaseAPI: TessBaseAPI): String? {
        tessBaseAPI.setImage(bitmap)
        return tessBaseAPI.utF8Text
    }
}
