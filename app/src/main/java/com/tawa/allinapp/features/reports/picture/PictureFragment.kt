package com.tawa.allinapp.features.reports.picture

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
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
                it
            }})
        }
        binding.btTakePhotoBefore.setOnClickListener {
            checkCameraPermissions()
        }

        return binding.root
    }

    private fun checkCameraPermissions(){
        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object: PermissionListener{
                override fun onPermissionGranted(response: PermissionGrantedResponse?) { launchCamera2() }
                override fun onPermissionDenied(response: PermissionDeniedResponse) {}
                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    private fun launchCamera2(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent,200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            200 -> if(resultCode==Activity.RESULT_OK) {
                val bitmap = data!!.extras?.get("data") as Bitmap
                pictureBeforeAdapter.collection.add(bitmap)
            }
        }
    }
}