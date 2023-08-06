import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.googlecode.tesseract.android.TessBaseAPI

class OCRProcessor {
    fun performOCR(activity: Activity, imageUri: Uri, tessBaseAPI: TessBaseAPI): String? {
        val inputStream = activity.contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        tessBaseAPI.setImage(bitmap)

        return tessBaseAPI.utF8Text
    }

    fun performOCR(activity: Activity, imageBitmap: Bitmap, tessBaseAPI: TessBaseAPI): String? {

        tessBaseAPI.setImage(imageBitmap)

        return tessBaseAPI.utF8Text
    }
}
