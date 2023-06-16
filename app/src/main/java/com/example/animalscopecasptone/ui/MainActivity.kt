package com.example.animalscopecasptone.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.animalscopecasptone.AnimalScopeApp.Companion.context
import com.example.animalscopecasptone.R
import com.example.animalscopecasptone.databinding.ActivityMainBinding
import com.example.animalscopecasptone.ml.ModelSsd
import com.example.animalscopecasptone.response.login.LoginRequest
import com.example.animalscopecasptone.viewModel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val authViewModel: AuthViewModel by viewModel()
    private lateinit var imageView: ImageView
    private lateinit var buttonCapture: Button
    private lateinit var buttonLoad: Button
    private lateinit var tvOutput: TextView
    private val GALLERY_REQUEST_CODE = 123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        imageView = binding.previewImageView
        buttonCapture = binding.btnCameraX
        buttonLoad = binding.btnGallery
        tvOutput = binding.output

        buttonCapture.setOnClickListener{
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            {
                takePicturePreview.launch(null)
            }
            else{
                requestPermission.launch(android.Manifest.permission.CAMERA)
            }
        }

        buttonLoad.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            val mimeType = arrayOf("image/jpeg", "image/png", "image/jpg")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            onResult.launch(intent)
        }

        // Redirect user to google search for the bird
        tvOutput.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=${tvOutput.text}"))
            startActivity(intent)
        }

        // Save image in storage
        /*imageView.setOnLongClickListener{
            requestPermissionLaunch.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return@setOnLongClickListener true
        }*/

        checkSession()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                showAlertDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val requestPermissionLaunch = registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted:Boolean ->
        if (isGranted){
            AlertDialog.Builder(this).setTitle("Download Imgae?")
                .setMessage("Do you want to download this image to yout device?")
                .setPositiveButton("Yes"){_, _ ->
                    val drawable: BitmapDrawable = imageView.drawable as BitmapDrawable
                    val bitmap = drawable.bitmap
                    downloadImage(bitmap)
                }
                .setNegativeButton("No"){dialog, _ ->
                    dialog.dismiss()
                }.show()
        }else{
            Toast.makeText(this,"Please allow permission to download image", Toast.LENGTH_LONG).show()
        }
    }

    // Fun that takes a bitmap and store
    private fun downloadImage(mBitmap: Bitmap):Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME,"Birds_Images" + System.currentTimeMillis()/1000)
            put(MediaStore.Images.Media.MIME_TYPE,"Image/png")
        }
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        if (uri != null){
            contentResolver.insert(uri, contentValues)?.also {
                contentResolver.openOutputStream(it).use {outputStream ->
                    if (!mBitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream)){
                        throw  IOException("Couldn't save the bitmap")
                    }else{
                        Toast.makeText(applicationContext,"Image Saved", Toast.LENGTH_LONG).show()
                    }
                }
                return it
            }
        }
        return null
    }

    //Request Camera permission
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){ granted->
        if (granted){
            takePicturePreview.launch(null)
        }else{
            Toast.makeText(this, "Permission Denied!!! Try again",Toast.LENGTH_SHORT).show()
        }
    }

    //Active Camera And Take a shot
    private val takePicturePreview = registerForActivityResult(ActivityResultContracts.TakePicturePreview()){ bitmap->
        if (bitmap!=null){
            imageView.setImageBitmap(bitmap)
            outputGenerator(bitmap)
        }
    }

    //take image from storage
    private val onResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        Log.i("TAG","This is the result: ${result.data} ${result.resultCode}")
        onResultReceived(GALLERY_REQUEST_CODE,result)
    }

    private fun onResultReceived(requestCode: Int, result: ActivityResult?){
        when(requestCode){
            GALLERY_REQUEST_CODE->{
                if (result?.resultCode == Activity.RESULT_OK){
                    result.data?.data?.let{uri ->
                        Log.i("TAG","onResultReceived: $uri")
                        val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
                        imageView.setImageBitmap(bitmap)
                        outputGenerator(bitmap)
                    }
                }else{
                    Log.e("Tag","onActivityResult: error in selecting image")
                }
            }
        }
    }



    private fun outputGenerator(bitmap: Bitmap) {
        val byteBuffer = convertBitmapToByteBuffer(bitmap)

        // Load the TFLite model
        val model = ModelSsd.newInstance(context)

        // Create inputs for reference
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)

        // Load data from byte buffer to input tensor buffer
        inputFeature0.loadBuffer(byteBuffer)

        // Run model inference and get the result
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        // Perform post-processing or further operations on the output values as needed
        val outputLabels = arrayOf("Label 1", "Label 2", "Label 3") // Example labels
        val probabilities = outputFeature0.floatArray // Example probabilities

        // Find the index with the highest probability
        var maxProbIndex = 0
        var maxProb = probabilities[0]
        for (i in 1 until probabilities.size) {
            if (probabilities[i] > maxProb) {
                maxProb = probabilities[i]
                maxProbIndex = i
            }
        }


        if (maxProbIndex >= 0 && maxProbIndex < outputLabels.size) {
            val predictedLabel = outputLabels[maxProbIndex]
            // Lanjutkan dengan penggunaan predictedLabel di sini
            println("Predicted label: $predictedLabel")
            // Tampilkan hasil klasifikasi kepada pengguna
            tvOutput.text = "Hasil klasifikasi: $predictedLabel"
        } else {
            // Lakukan penanganan kesalahan jika indeks tidak valid
            println("Invalid index: $maxProbIndex")
            // Tampilkan pesan kesalahan kepada pengguna
            tvOutput.text = "Tidak dapat memprediksi hasil klasifikasi"
            // atau lakukan tindakan lain yang sesuai
        }

        // Close the model to release resources
        model.close()
    }



    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(1 * 256 * 256 * 3 * 4) // Multiply by 4 because each float value occupies 4 bytes
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(256 * 256)

        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 256, 256, true)
        scaledBitmap.getPixels(intValues, 0, scaledBitmap.width, 0, 0, scaledBitmap.width, scaledBitmap.height)

        var pixel = 0
        for (i in 0 until 256) {
            for (j in 0 until 256) {
                val value = intValues[pixel++]
                byteBuffer.putFloat((value shr 16 and 0xFF) / 255.0f)
                byteBuffer.putFloat((value shr 8 and 0xFF) / 255.0f)
                byteBuffer.putFloat((value and 0xFF) / 255.0f)
            }
        }

        byteBuffer.rewind() // Kembalikan posisi ByteBuffer ke awal

        return byteBuffer
    }

    private fun checkSession() {
        val sharedPref = getSharedPreferences("credentials", Context.MODE_PRIVATE)
        val email = sharedPref.getString("email", null)
        val password = sharedPref.getString("password", null)

        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            authViewModel.loginUser(LoginRequest(email, password))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun clearSharedPreferences() {
        val sharedPref = getSharedPreferences("credentials", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }
        Toast.makeText(this, "Log out successful", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
    }
    private fun showAlertDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        val alert = builder.create()
        builder.setTitle(getString(R.string.logout)).setMessage(getString(R.string.logout_confirm))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                clearSharedPreferences()
            }.setNegativeButton(getString(R.string.no)) { _, _ ->
                alert.cancel()
            }.show()
    }

}