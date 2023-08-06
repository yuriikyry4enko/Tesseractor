import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class TesseractService(private val context: Context) {

    lateinit var tessBaseAPI: TessBaseAPI
    lateinit var tessDataPath: String

    fun initializeTessBaseAPI() {
        try {
            val assetManager: AssetManager = context.assets

            val tesseractFolder = File(context.filesDir, "tesseract")
            tessDataPath = tesseractFolder.absolutePath
            val tessdataDir = File(tessDataPath, "tessdata")
            val languageDataFileName = "eng.traineddata"
            val languageDataFile = File(tessdataDir, languageDataFileName)

            if (!tessdataDir.exists()) {
                tessdataDir.mkdirs()
            }

            if (!languageDataFile.exists()) {
                val inputStream = assetManager.open("eng.traineddata")
                val outputStream = FileOutputStream(languageDataFile)
                inputStream.copyTo(outputStream)
                inputStream.close()
                outputStream.close()
            }

            tessBaseAPI = TessBaseAPI()
            tessBaseAPI.init(tessDataPath, "eng")

        } catch (err: Exception) {
            Log.e("TesseractService", "Error: $err")
        }
    }
}
