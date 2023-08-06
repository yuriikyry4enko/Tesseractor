// Assuming the app follows MVVM architecture

package com.smokedpaprika.tesseractscanner

import com.smokedpaprika.tesseractscanner.utils.ImagePicker
import com.smokedpaprika.tesseractscanner.utils.OCRProcessor
import TesseractService
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.smokedpaprika.tesseractscanner.fragments.ImageCropFragment
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var BSelectImage: Button
    private lateinit var CropImageButton: Button
    private lateinit var IVPreviewImage: ImageView
    private lateinit var OcrTextView: TextView

    private lateinit var pickedImageUri: Uri

    private val imagePicker = ImagePicker(this)
    private val ocrProcessor = OCRProcessor()
    private val tesseractService = TesseractService(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BSelectImage = findViewById(R.id.BSelectImage)
        IVPreviewImage = findViewById(R.id.IVPreviewImage)
        OcrTextView = findViewById(R.id.OcrTextView)
        CropImageButton = findViewById(R.id.cropImageButton)

        BSelectImage.setOnClickListener {
            imagePicker.selectImage()
        }
        CropImageButton.setOnClickListener {
            openImageCropFragment(pickedImageUri)
        }

        tesseractService.initializeTessBaseAPI()
    }

    private fun openImageCropFragment(imageUri: Uri?) {
        if (imageUri != null) {
            val imageCropFragment = ImageCropFragment.newInstance(imageUri)
            imageCropFragment.show(
                supportFragmentManager,
                "com.smokedpaprika.tesseractscanner.fragments.ImageCropFragment"
            )
        } else {
            showAlertDialog("Error", "Image URI is null.")
        }
    }

    private fun showAlertDialog(title: String, message: String) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()
        alertDialog.show()
    }

    fun onImageCroppedBitmap(croppedUri: Bitmap?) {
        if (croppedUri != null) {
            val recognizedText = ocrProcessor.performOCR( croppedUri, tesseractService.tessBaseAPI)
            OcrTextView.text = recognizedText
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
                    val recognizedText = ocrProcessor.performOCR(this, imgUri, tesseractService.tessBaseAPI)
                    OcrTextView.text = recognizedText
                }
            } catch (err: Exception) {
                Log.e("MainActivity", "Error: $err")
            }
        }
    }
}
