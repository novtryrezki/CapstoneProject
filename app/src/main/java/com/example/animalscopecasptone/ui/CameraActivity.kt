package com.example.animalscopecasptone.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.animalscopecasptone.R
import com.example.animalscopecasptone.databinding.ActivityCameraBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var imageCapture: ImageCapture
    private lateinit var outputDirectory: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermissions()

        outputDirectory = getOutputDirectory()


        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

                preview.setSurfaceProvider(binding.previewView.surfaceProvider)
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))

        setClickListeners()
    }

    private fun setClickListeners() {
        binding.captureImageButton.setOnClickListener {
            captureImage()
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }

        binding.switchCamera.setOnClickListener {
            cameraSelector = if (cameraSelector.equals(CameraSelector.DEFAULT_BACK_CAMERA)) CameraSelector.DEFAULT_FRONT_CAMERA
            else CameraSelector.DEFAULT_BACK_CAMERA

            captureImage()
        }
    }


    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    /*private fun detectAndPassResult(file: File) {
        diseaseDetectionViewModel.detectDisease(file)
    }*/

    private fun captureImage() {
        val filename =
            SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg"
        val photoFile = File(outputDirectory, filename)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)

                    val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                    intent.data = savedUri
                    sendBroadcast(intent)

                    val compressedPhotoFile = compressImage(photoFile)
//                    detectAndPassResult(compressedPhotoFile)
                }
            })
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            val selectedImageUri = data?.data
            if (selectedImageUri == null) {
                Toast.makeText(this, "No image was selected.", Toast.LENGTH_SHORT).show()
                return
            }

            val selectedImageFile = uriToFile(selectedImageUri)
//            detectAndPassResult(selectedImageFile)
        }
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)
        val file = File(cacheDir, "file")
        file.createNewFile()

        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)

        return file
    }

    private fun requestPermissions() {
        if (allPermissionsGranted()) {
            return
        }
        ActivityCompat.requestPermissions(
            this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                // start camera
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun compressImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        val compressedFile = File(outputDirectory, "COMP_" + file.name)

        var quality = 100
        var size = (file.length() / 1024).toInt()

        while (size > 200) {
            val outputStream = FileOutputStream(compressedFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.flush()
            outputStream.close()

            size = (compressedFile.length() / 1024).toInt()
            quality = (quality * 200) / size
            if (quality > 100) quality = 100
            if (quality < 0) quality = 0
        }

        return compressedFile
    }


    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    companion object {
        private const val TAG = "CameraActivity"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val SELECT_PHOTO_REQUEST_CODE = 100
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
    }

}