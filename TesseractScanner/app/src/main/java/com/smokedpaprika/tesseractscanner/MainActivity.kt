// Assuming the app follows MVVM architecture

package com.smokedpaprika.tesseractscanner

import ImagePicker
import OCRProcessor
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var BSelectImage: Button
    private lateinit var IVPreviewImage: ImageView
    private lateinit var OcrTextView: TextView
    private var SELECT_PICTURE = 200

    private lateinit var tessBaseAPI: TessBaseAPI
    private lateinit var tessDataPath: String
    private lateinit var imagePicker: ImagePicker
    private lateinit var ocrProcessor: OCRProcessor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BSelectImage = findViewById(R.id.BSelectImage)
        IVPreviewImage = findViewById(R.id.IVPreviewImage)
        OcrTextView = findViewById(R.id.OcrTextView)

        imagePicker = ImagePicker(this)
        ocrProcessor = OCRProcessor()

        BSelectImage.setOnClickListener {
            imagePicker.selectImage()
        }

        initializeTessBaseAPI()
    }

    private fun initializeTessBaseAPI() {
        try {
            val assetManager: AssetManager = assets

            val tesseractFolder = File(filesDir, "tesseract")
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
            Log.e("MainActivity", "Error: $err")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ImagePicker.REQUEST_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            val imgUri = data?.data
            IVPreviewImage.setImageURI(imgUri)

            try {
                if (imgUri != null) {
                    val recognizedText = ocrProcessor.performOCR(this, imgUri, tessBaseAPI)
                    OcrTextView.text = recognizedText
                }
            } catch (err: Exception) {
                Log.e("MainActivity", "Error: $err")
            }
        }
    }
}
