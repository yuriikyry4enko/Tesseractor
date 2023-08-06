package com.smokedpaprika.tesseractscanner.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.canhub.cropper.CropImageView
import com.smokedpaprika.tesseractscanner.MainActivity
import com.smokedpaprika.tesseractscanner.R

class ImageCropFragment : DialogFragment() {

    private lateinit var cropImageView: CropImageView
    private lateinit var confirmButton: Button
    private lateinit var imageUri: Uri

    companion object {
        private const val ARG_IMAGE_URI = "arg_image_uri"

        fun newInstance(imageUri: Uri): ImageCropFragment {
            return ImageCropFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_IMAGE_URI, imageUri)
                }
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_image_crop, container, false).apply {
            cropImageView = findViewById(R.id.cropImageView)
            confirmButton = findViewById(R.id.confirmButton)

            imageUri = arguments?.getParcelable(ARG_IMAGE_URI) ?: Uri.EMPTY

            cropImageView.setImageUriAsync(imageUri)

            confirmButton.setOnClickListener {
                performImageCrop()
            }
        }
    }

    private fun performImageCrop() {
        val croppedImageBitmap = cropImageView.getCroppedImage()
        (activity as? MainActivity)?.onImageCroppedBitmap(croppedImageBitmap)
        dismiss()
    }
}
