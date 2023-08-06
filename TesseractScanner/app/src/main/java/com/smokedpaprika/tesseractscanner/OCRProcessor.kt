import android.app.Activity
import android.graphics.BitmapFactory
import android.net.Uri
import com.googlecode.tesseract.android.TessBaseAPI

class OCRProcessor {
    fun performOCR(activity: Activity, imageUri: Uri, tessBaseAPI: TessBaseAPI): String? {
        val inputStream = activity.contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        tessBaseAPI.setImage(bitmap)

        val recognizedText = tessBaseAPI.utF8Text

        return recognizedText
    }
}
