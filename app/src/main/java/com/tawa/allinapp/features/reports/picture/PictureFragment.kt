package com.tawa.allinapp.features.reports.picture

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentPictureBinding
import javax.inject.Inject

class PictureFragment : BaseFragment() {

    private lateinit var binding:FragmentPictureBinding
    private lateinit var pictureViewModel: PictureViewModel

    @Inject lateinit var pictureBeforeAdapter: PictureBeforeAdapter
    @Inject lateinit var pictureAfterAdapter: PictureAfterAdapter

    private val before = 200
    private val after = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvPhotoBefore.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.rvPhotoBefore.adapter = pictureBeforeAdapter
        binding.rvPhotoAfter.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.rvPhotoAfter.adapter = pictureAfterAdapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPictureBinding.inflate(inflater)
        pictureViewModel = viewModel(viewModelFactory){
            observe(text,{ it?.let {

            }})
        }
        binding.btTakePhotoBefore.setOnClickListener {
            checkCameraPermissions(before)
        }
        binding.btTakePhotoAfter.setOnClickListener {
            checkCameraPermissions(after)
        }

        return binding.root
    }

    private fun checkCameraPermissions(origin:Int){
        Dexter.withActivity(activity)
            .withPermissions(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object: MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) { launchCamera(origin) }
                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    private fun launchCamera(origin:Int){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, origin)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            before -> if(resultCode==Activity.RESULT_OK) {
                val bitmap = data!!.extras?.get("data") as Bitmap
                val uri = getImageUri(activity?.applicationContext!!, bitmap)
                pictureBeforeAdapter.collection.add(uri.toString())
            }
            after -> if(resultCode==Activity.RESULT_OK) {
                val bitmap = data!!.extras?.get("data") as Bitmap
                val uri = getImageUri(activity?.applicationContext!!, bitmap)
                pictureAfterAdapter.collection.add(uri.toString())
            }
        }
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap?): Uri? {
        val image = Bitmap.createScaledBitmap(inImage!!, 300, 300, true)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, image, "picture", null)
        return Uri.parse(path)
    }
}