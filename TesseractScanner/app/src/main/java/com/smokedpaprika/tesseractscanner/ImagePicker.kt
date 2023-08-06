import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ImagePicker(private val activity: Activity) {
    companion object {
        const val REQUEST_IMAGE_CODE = 100
    }

    fun selectImage() {
        if (hasStoragePermission()) {
            openImagePicker()
        } else {
            requestStoragePermission()
        }
    }

    private fun hasStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
            REQUEST_IMAGE_CODE
        )
    }

    private fun openImagePicker() {
        val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        activity.startActivityForResult(pickImg, REQUEST_IMAGE_CODE)
    }
}
