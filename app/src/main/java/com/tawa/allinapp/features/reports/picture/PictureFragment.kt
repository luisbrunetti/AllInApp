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
import android.widget.LinearLayout
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.tawa.allinapp.R
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.extensions.failure
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentPictureBinding
import com.tawa.allinapp.features.reports.sku.ConfirmDialogFragment
import com.tawa.allinapp.models.PhotoReport
import java.util.*
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
        setUpBinding()
        checkPermissions()
        binding.rvPhotoBefore.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.rvPhotoBefore.adapter = pictureBeforeAdapter
        pictureBeforeAdapter.clickListener = { openImage(it) }
        pictureBeforeAdapter.deleteListener = {
            pictureBeforeAdapter.collection.remove(it)
            pictureBeforeAdapter.notifyDataSetChanged()
        }
        binding.rvPhotoAfter.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.rvPhotoAfter.adapter = pictureAfterAdapter
        pictureAfterAdapter.clickListener = { openImage(it) }
        pictureAfterAdapter.deleteListener = {
            pictureAfterAdapter.collection.remove(it)
            pictureAfterAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPictureBinding.inflate(inflater)
        pictureViewModel = viewModel(viewModelFactory){
            observe(successReport,{ it?.let {
                if(it){
                    hideProgressDialog()
                    ConfirmDialogFragment().show(childFragmentManager, "dialog")
                    //MessageDialogFragment.newInstance("", title = R.string.ok_save_report, icon = R.drawable.ic_checkin).show(childFragmentManager, "dialog")
                }
            }})
            observe(errorMessage, { it?.let {
                if (it.isNotEmpty()) MessageDialogFragment.newInstance(it).show(childFragmentManager, "dialog")
            }})
            failure(failure, { it?.let {
                hideProgressDialog()
                when(it){
                    is Failure.DefaultError         -> pictureViewModel.setError(it.message ?: getString(R.string.error_unknown))
                    is Failure.NetworkConnection    -> MessageDialogFragment.newInstance(getString(R.string.error_network)).show(childFragmentManager, "dialog")
                    is Failure.ServerError          -> MessageDialogFragment.newInstance(getString(R.string.error_network)).show(childFragmentManager, "dialog")
                    else                            -> MessageDialogFragment.newInstance(getString(R.string.error_unknown)).show(childFragmentManager, "dialog")
                }
            }})
        }
        binding.btTakePhotoBefore.setOnClickListener {
            checkCameraPermissions(before)
        }
        binding.btTakePhotoAfter.setOnClickListener {
            checkCameraPermissions(after)
        }
        binding.btSavePictures.setOnClickListener {
            showProgressDialog()

            val report = PhotoReport(
                pictureBeforeAdapter.collection,
                pictureAfterAdapter.collection,
                binding.tvComments.text.toString(),
                Calendar.getInstance().time.toString()
            )
            pictureViewModel.saveReport(report)
        }

        return binding.root
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

    private fun openImage(image:String){
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(image.toUri(), "image/*")
        activity?.startActivity(intent)
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

    private fun getImageUri(inContext: Context, inImage: Bitmap?): Uri? {
        val image = Bitmap.createScaledBitmap(inImage!!, 300, 300, true)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, image, "picture", null)
        return Uri.parse(path)
    }

    private fun setUpBinding() {
        binding.viewModel = pictureViewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }
}