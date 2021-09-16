package com.tawa.allinapp.features.reports.picture

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.skydoves.balloon.*
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
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

class PictureFragment : BaseFragment() {

    private lateinit var binding: FragmentPictureBinding
    private lateinit var pictureViewModel: PictureViewModel

    @Inject
    lateinit var pictureBeforeAdapter: PictureBeforeAdapter

    @Inject
    lateinit var pictureAfterAdapter: PictureAfterAdapter

    private val before = 200
    private val after = 300
    private var state = true

    private var photoReport: PhotoReport? = null
    private var balloon: Balloon? = null

    private var idUser: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBinding()
        checkPermissions()
        binding.rvPhotoBefore.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPhotoBefore.adapter = pictureBeforeAdapter
        pictureBeforeAdapter.clickListener = { openImage(it) }
        pictureBeforeAdapter.deleteListener = {
            pictureBeforeAdapter.collection.remove(it)
            pictureBeforeAdapter.notifyDataSetChanged()
        }
        binding.rvPhotoAfter.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPhotoAfter.adapter = pictureAfterAdapter
        pictureAfterAdapter.clickListener = { openImage(it) }
        pictureAfterAdapter.deleteListener = {
            pictureAfterAdapter.collection.remove(it)
            pictureAfterAdapter.notifyDataSetChanged()
        }
        pictureViewModel.getPhotoReport()
        binding.ivInformationPointSale.setOnClickListener { setTextToolTip() }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPictureBinding.inflate(inflater)
        if (arguments?.getString("state") == "Enviado") {
            disableComponents()
        }
        arguments?.getString("idUser")?.let {
            idUser = it
        }
        pictureViewModel = viewModel(viewModelFactory) {
            observe(successReport, {
                it?.let {
                    if (it) hideProgressDialog()
                }
            })
            observe(errorMessage, {
                it?.let {
                    if (it.isNotEmpty()) MessageDialogFragment.newInstance(it)
                        .show(childFragmentManager, "dialog")
                }
            })
            observe(successSyncPhotoReports, {
                it?.let {
                    hideProgressDialog()
                    if (it) {
                        pictureViewModel.saveReport(photoReport, "Enviado")
                        activity?.onBackPressed()
                    }
                }
            })
            observe(successDeletePhotoReports, {
                it?.let {
                    hideProgressDialog()
                }
            })
            observe(report, {
                it?.let {
                    pictureAfterAdapter.setData(it.after)
                    pictureBeforeAdapter.setData(it.before)
                    binding.tvComments.setText(it.comments)
                    getLastLocation()
                    it.apply {
                        before = pictureBeforeAdapter.collection
                        after = pictureAfterAdapter.collection
                        comments = binding.tvComments.text.toString()
                        syncLongitude = longitude
                        syncLatitude = latitude
                        syncAt = Calendar.getInstance().time.toString()
                        syncLatitude = longitude
                    }
                    photoReport = it
                }
            })
            failure(failure, {
                it?.let {
                    hideProgressDialog()
                    when (it) {
                        is Failure.DefaultError -> pictureViewModel.setError(
                            it.message ?: getString(R.string.error_unknown)
                        )
                        is Failure.NetworkConnection -> notify(activity, R.string.error_network_ok)
                        is Failure.ServerError -> MessageDialogFragment.newInstance(getString(R.string.error_network))
                            .show(childFragmentManager, "dialog")
                        else -> MessageDialogFragment.newInstance(getString(R.string.error_unknown))
                            .show(childFragmentManager, "dialog")
                    }
                }
            })
        }
        binding.iHeader.ivHeader.setOnClickListener { activity?.onBackPressed() }
        binding.btTakePhotoBefore.setOnClickListener {
            if (pictureBeforeAdapter.collection.size < 5) {
                checkCameraPermissions(before)
            } else {
                MessageDialogFragment.newInstance(getString(R.string.error_photo_limit))
                    .show(childFragmentManager, "dialog")
            }
        }
        binding.btTakePhotoAfter.setOnClickListener {
            if (pictureAfterAdapter.collection.size < 5) {
                checkCameraPermissions(after)
            } else {
                MessageDialogFragment.newInstance(getString(R.string.error_photo_limit))
                    .show(childFragmentManager, "dialog")
            }
        }
        binding.btSavePictures.setOnClickListener {
            showProgressDialog()
            getLastLocation()
            val report = PhotoReport(
                pictureBeforeAdapter.collection,
                pictureAfterAdapter.collection,
                binding.tvComments.text.toString(),
                Calendar.getInstance().time.toString(),
                longitude.toDouble(), latitude.toDouble(),
                longitude.toDouble(), latitude.toDouble(),
                Calendar.getInstance().time.toString(),
            )
            pictureViewModel.saveReport(report, "En proceso")
            activity?.onBackPressed()
        }
        binding.btSendPictures.setOnClickListener {
            val confirm = ConfirmDialogFragment()
            confirm.listener = object : ConfirmDialogFragment.Callback {
                override fun onClick() {
                    showProgressDialog()
                    pictureViewModel.syncPhotoReport()
                }
            }
            confirm.show(childFragmentManager, "dialog")
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            when (requestCode) {
                before -> if (resultCode == Activity.RESULT_OK) {
                    val bitmap = data!!.extras?.get("data") as Bitmap
                    //val uri = getImageUri(activity?.applicationContext!!, bitmap)
                    pictureBeforeAdapter.collection.add(convertBase64(bitmap)!!)
                    pictureBeforeAdapter.notifyDataSetChanged()
                }
                after -> if (resultCode == Activity.RESULT_OK) {
                    val bitmap = data!!.extras?.get("data") as Bitmap
                    //val uri = getImageUri(activity?.applicationContext!!, bitmap)
                    pictureAfterAdapter.collection.add(convertBase64(bitmap)!!)
                    pictureAfterAdapter.notifyDataSetChanged()
                }
            }
        } catch (e : Exception){
            Log.d("Error", e.toString())
            MessageDialogFragment.newInstance("Ha ocurrido un error\nVuelvalo a intentar").show(childFragmentManager,"dialog")
        }
    }

    private fun openImage(image: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(image.toUri(), "image/*")
        activity?.startActivity(intent)
    }

    private fun checkCameraPermissions(origin: Int) {
        Dexter.withActivity(activity)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    launchCamera(origin)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    private fun convertBase64(bitmap: Bitmap): String? {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    private fun launchCamera(origin: Int) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, origin)
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap?): Uri? {
        val image = Bitmap.createScaledBitmap(inImage!!, 300, 300, true)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, image, "documents", null)
        return Uri.parse(path)
    }

    private fun setUpBinding() {
        binding.viewModel = pictureViewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }

    private fun disableComponents() {
        binding.btTakePhotoBefore.apply {
            isEnabled = false
            alpha = 0.5F
        }
        binding.btTakePhotoAfter.apply {
            isEnabled = false
            alpha = 0.5F
        }
        binding.btSendPictures.apply {
            isEnabled = false
            alpha = 0.5F
        }
        binding.btSavePictures.apply {
            isEnabled = false
            alpha = 0.5F
        }
        binding.tvComments.apply {
            isEnabled = false
            alpha = 0.5F
        }
    }

    private fun setTextToolTip() {
        balloon?.dismiss()
        balloon = createBalloon(requireContext()) {
            setArrowSize(10).setWidth(BalloonSizeSpec.WRAP).setHeight(BalloonSizeSpec.WRAP)
                .setArrowPosition(0.5f)
                .setArrowOrientation(ArrowOrientation.LEFT)
                .setCornerRadius(10f)
                .setPaddingTop(5)
                .setPaddingBottom(5)
                .setPaddingLeft(10)
                .setPaddingRight(10)
                .setText("Máximo solo cinco fotos")
                .setTextColorResource(R.color.white)
                .setBackgroundColorResource(R.color.colorLayoutTopGradient)
                .setBalloonAnimation(BalloonAnimation.FADE).setLifecycleOwner(lifecycleOwner)
                .build()
        }
        balloon?.showAlignRight(binding.ivInformationPointSale, 8, 0)
        balloon?.dismissWithDelay(3000L)
        balloon?.show(binding.ivInformationPointSale)
    }
}