// Assuming the app follows MVVM architecture

package com.smokedpaprika.tesseractscanner

import ImagePicker
import OCRProcessor
import android.app.Activity
import android.content.Intent
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var BSelectImage: Button
    private lateinit var CropImageButton: Button
    private lateinit var IVPreviewImage: ImageView
    private lateinit var OcrTextView: TextView
    private var SELECT_PICTURE = 200

    private lateinit var tessBaseAPI: TessBaseAPI
    private lateinit var tessDataPath: String
    private lateinit var imagePicker: ImagePicker
    private lateinit var ocrProcessor: OCRProcessor

    private lateinit var pickedImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BSelectImage = findViewById(R.id.BSelectImage)
        IVPreviewImage = findViewById(R.id.IVPreviewImage)
        OcrTextView = findViewById(R.id.OcrTextView)
        CropImageButton = findViewById(R.id.cropImageButton)

        imagePicker = ImagePicker(this)
        ocrProcessor = OCRProcessor()

        BSelectImage.setOnClickListener {
            imagePicker.selectImage()
        }

        CropImageButton.setOnClickListener {
            openImageCropFragment(pickedImageUri)
        }

        initializeTessBaseAPI()
    }

    private fun openImageCropFragment(imageUri: Uri) {
        // Create a new instance of ImageCropFragment and pass the imageUri as an argument
        val imageCropFragment = ImageCropFragment.newInstance(imageUri)
        // Show the ImageCropFragment as a full-screen dialog
        imageCropFragment.show(supportFragmentManager, "ImageCropFragment")
    }

    fun onImageCroppedBitmap(croppedUri: Bitmap?) {
        // Handle the cropped image URI here
        if (croppedUri != null) {
            // Display the cropped image in an ImageView (optional)
            //IVPreviewImage.setImageURI(croppedUri)
            val recognizedText = ocrProcessor.performOCR(this, croppedUri, tessBaseAPI)
            OcrTextView.text = recognizedText
        }
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
                    pickedImageUri = imgUri
                    val recognizedText = ocrProcessor.performOCR(this, imgUri, tessBaseAPI)
                    OcrTextView.text = recognizedText
                }
            } catch (err: Exception) {
                Log.e("MainActivity", "Error: $err")
            }
        }
    }
}
