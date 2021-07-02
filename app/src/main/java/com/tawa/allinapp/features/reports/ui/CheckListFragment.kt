package com.tawa.allinapp.features.reports.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentChecklistBinding
import com.tawa.allinapp.features.reports.ReportsViewModel
import java.io.File


class CheckListFragment : BaseFragment() {

    private lateinit var reportsViewModel: ReportsViewModel
    private lateinit var binding: FragmentChecklistBinding
    private val TAKE_PHOTO_REQUEST = 101
    private var mCurrentPhotoPath: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChecklistBinding.inflate(inflater)

        reportsViewModel = viewModel(viewModelFactory) {
            observe(text, {
                it?.let {

                }
            })
        }
        binding.btnTakePhoto.setOnClickListener{
           validatePermissions()
        }
        return binding.root
    }

    private fun validatePermissions() {
        Dexter.withActivity(requireActivity())
            .withPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object: PermissionListener {
                override fun onPermissionGranted(
                    response: PermissionGrantedResponse?) {
                    launchCamera()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?) {
                    AlertDialog.Builder(context)
                        .setTitle(
                            "Probando")
                        .setMessage(
                            "Hola")
                        .setNegativeButton(
                            android.R.string.cancel
                        ) { dialog, _ ->
                            dialog.dismiss()
                            token?.cancelPermissionRequest()
                        }
                        .setPositiveButton(android.R.string.ok
                        ) { dialog, _ ->
                            dialog.dismiss()
                            token?.continuePermissionRequest()
                        }
                        .setOnDismissListener {
                            token?.cancelPermissionRequest()
                        }
                        .show()
                }

                override fun onPermissionDenied(
                    response: PermissionDeniedResponse?) {
                    Toast.makeText(context,"error" + response,Toast.LENGTH_SHORT).show()
                }
            })
            .check()
    }


    private fun launchCamera() {
        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        val fileUri = requireActivity().contentResolver
            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(requireActivity().packageManager) != null) {
            mCurrentPhotoPath = fileUri.toString()
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, TAKE_PHOTO_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        if (resultCode == Activity.RESULT_OK
            && requestCode == TAKE_PHOTO_REQUEST) {
            processCapturedPhoto()
            binding.checkListPhoto.visibility = View.VISIBLE
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun processCapturedPhoto() {
        val cursor = requireActivity().contentResolver.query(
            Uri.parse(mCurrentPhotoPath),
            Array(1) {android.provider.MediaStore.Images.ImageColumns.DATA},
            null, null, null)
        cursor?.moveToFirst()
        val photoPath = cursor?.getString(0)
        cursor?.close()
        val file = File(photoPath)
        val uri = Uri.fromFile(file)

        val height =200
        val width = 300

        val request = ImageRequestBuilder.newBuilderWithSource(uri)
            .setResizeOptions(ResizeOptions(width, height))
            .build()
        val controller = Fresco.newDraweeControllerBuilder()
            .setOldController(binding.checkListPhoto?.controller)
            .setImageRequest(request)
            .build()
        binding.checkListPhoto?.controller = controller
    }


}