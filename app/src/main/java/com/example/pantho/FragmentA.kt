package com.example.pantho

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.pantho.databinding.LayoutFragmentABinding
import com.example.pantho.databinding.LayoutFragmentBBinding
import java.io.File

class FragmentA : Fragment() {
    // constants / statics
    companion object {
        const val TAG = "FRAGMENT_A"
    }
    // variables

    private var _binding: LayoutFragmentABinding? = null
    private val binding: LayoutFragmentABinding get() = _binding!!


//    private var _binding: LayoutFragmentBBinding? = null
//    private val binding: LayoutFragmentBBinding get() = _binding!!
    // overrride methods


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutFragmentABinding.inflate(inflater, container, false)
        binding.root.setOnClickListener {  }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tv1.text = "hello there 2"
        binding.tv1.setOnClickListener {
            checkCameraPermissionAndDoTheNextThing()
        }

    }

    // private, public methods...

    private val cameraActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                activityResult ->
            // val value = activityResult.data?.getBundleExtra("some key")
        if(activityResult.resultCode == Activity.RESULT_OK) {
            handleImage(photoFileUri)
        }
    }

    private fun handleImage(uri: Uri) {
        binding.imageView.setImageURI(uri)
    }


    fun createImageUri(context: Context): Uri {
        try {
            val folder = File("${context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)}")
            folder.mkdirs()

            val file = File(folder, "temp_camera_photo.jpg")
            if (file.exists())
                file.delete()
            file.createNewFile()

            val APPLICATION_ID = context.applicationContext.packageName  // todo: how to get application_id in flavoured apps? o.O

            // relative path otar moto relative uri
            val imageUri = FileProvider.getUriForFile(
                context,
                APPLICATION_ID + "."+ CameraFileProvider.PROVIDER_NAME,
                file
            )
            val imgPath = file.absolutePath
            Log.d("tag", "imgPath = $imgPath")
            return imageUri!!
        } catch (x: Exception) {
            x.printStackTrace()
            throw x
        }
    }


    private val cameraPermissionResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) { result ->

        var allAreGranted = true
        for(b in result.values) {
            allAreGranted = allAreGranted && b
        }

        if(allAreGranted) {
            Log.d(TAG, "all camera permissions are granted, open camera")
            openCameraIntent()
        } else {
            Log.e(TAG, "all camera permissions are NOT granted, DON'T open camera")
            Log.i(TAG, "result = $result")
            for(e in result) {
                Log.d(TAG, "e.key = ${e.key}, e.value = ${e.value}")
            }

        }
    }

    private fun checkCameraPermissionAndDoTheNextThing() {
        Log.d(TAG, "Camera clicked")
        val appPerms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            arrayOf(
                Manifest.permission.CAMERA
            )
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        }
        cameraPermissionResultLauncher.launch(appPerms)
    }

    var photoFileUri: Uri = Uri.EMPTY

    private fun openCameraIntent() {
        try {
            val cameraIntent = Intent()
            photoFileUri = createImageUri(requireContext())
            cameraIntent.action = MediaStore.ACTION_IMAGE_CAPTURE
             cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFileUri)
//            requireActivity().startActivity(cameraIntent)
             cameraActivityResultLauncher.launch(cameraIntent)
        } catch (x: java.lang.Exception) {
            x.printStackTrace()
            Toast.makeText(requireContext(), "Failed to open the camera", Toast.LENGTH_LONG).show()
        }
    }

}