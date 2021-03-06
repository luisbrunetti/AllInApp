package com.tawa.allinapp.features.pdv

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.DialogTakePhotoBinding
import com.tawa.allinapp.features.init.InitViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class TakePhotoDialogFragment
@Inject constructor(
    private val baseFragment:BaseFragment
): DialogFragment() {

    private lateinit var binding: DialogTakePhotoBinding
    private lateinit var initViewModel: InitViewModel
    var photoFile: File? = null
    val CAPTURE_IMAGE_REQUEST = 1
    var mCurrentPhotoPath: String? = null
    var urlImage = ""

    var params = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.FILL_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    var listener: Callback? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseFragment.appComponent.inject(this)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogTakePhotoBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnTakePhotoPdv.setOnClickListener {
            validatePermissions()
        }
        binding.btnSavePdv.setOnClickListener {
            photoFile?.let {
                listener?.onSave(urlImage)
                dismiss()
            } ?: run {
                MessageDialogFragment.newInstance(baseFragment, "Se tiene que agregar una foto").show(childFragmentManager, "")
            }
        }
        binding.btnCloseModalTakePhoto.setOnClickListener {
            dismiss()
        }

        changeViewsFragment()
    }

    private fun changeViewsFragment() {
        baseFragment.translateObject.apply {
            if(getInstance().isNotEmpty()) {
                binding.btnSavePdv.text = findTranslate("btnSavePdv")
                binding.btnTakePhotoPdv.text = findTranslate("btnTakePhotoPdv")
            }else baseFragment.authViewModel.getTranslate()
        }
    }


    private fun validatePermissions() {
        Dexter.withActivity(requireActivity())
            .withPermissions(android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object: MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    launchCamera()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    TODO("Not yet implemented")
                }
            })
            .check()
    }


    private fun launchCamera() {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create the File where the photo should go
        try {
            photoFile = createImageFile()
            // Continue only if the File was successfully created
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    requireContext(),
                    "com.tawa.captureimage.fileprovider",
                    photoFile!!
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST)
            }
        } catch (ex: Exception) {
            // Error occurred while creating the File
            displayMessage(requireContext(), ex.message.toString())
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    private fun displayMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try{
            if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
                urlImage = photoFile!!.absolutePath
                val imageUri = Uri.fromFile(File(urlImage))
                binding.photoPdv.setImageURI(imageUri,"")

            } else {
                displayMessage(requireContext(), "Request cancelled or something went wrong.")
            }
        }catch (e: Exception){
            Log.d("exception",e.toString())
        }
    }


    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

    interface Callback {
        fun onClick()
        fun onSave(urlImage:String)
        // fun onBack()
    }
}