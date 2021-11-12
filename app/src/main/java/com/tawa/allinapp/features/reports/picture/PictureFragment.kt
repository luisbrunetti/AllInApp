package com.tawa.allinapp.features.reports.picture

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
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
import com.tawa.allinapp.features.init.InitViewModel
import com.tawa.allinapp.features.reports.standard.ConfirmSyncDialogFragment
import com.tawa.allinapp.models.PhotoReport
import java.io.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*
import javax.inject.Inject

class PictureFragment : BaseFragment() {

    private lateinit var binding: FragmentPictureBinding
    private lateinit var pictureViewModel: PictureViewModel
    private lateinit var initViewModel: InitViewModel

    @Inject
    lateinit var pictureBeforeAdapter: PictureBeforeAdapter

    @Inject
    lateinit var pictureAfterAdapter: PictureAfterAdapter

    private val before = 200
    private val after = 300
    private var state = true
    private var type = ""

    private var photoReport: PhotoReport? = null
    private var balloon: Balloon? = null

    private var idUser: String? = null

    var photoFile: File? = null
    var mCurrentPhotoPath: String? = null
    val CAPTURE_IMAGE_REQUEST = 1

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
                    if (it.isNotEmpty()) MessageDialogFragment.newInstance(this@PictureFragment,it)
                        .show(childFragmentManager, "dialog")
                }
            })
            observe(successSyncOne, {
                it?.let {
                    hideProgressDialog()
                    if (it) {
                        pictureViewModel.saveReport(photoReport, "Enviado","terminado")
                        activity?.onBackPressed()
                    }
                }
            })
            observe(successType,{it?.let{
                Log.d("successType",it.toString())
                if(it.isNotEmpty()) {
                    //disableButtons()
                    type = it
                }
            }})
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
                        is Failure.NetworkConnection -> notify(activity, "La informacion se guardo correctamente, pero no se subio a internet")
                        is Failure.ServerError -> MessageDialogFragment.newInstance(this@PictureFragment,getString(R.string.error_network))
                            .show(childFragmentManager, "dialog")
                        else -> MessageDialogFragment.newInstance(this@PictureFragment,getString(R.string.error_unknown))
                            .show(childFragmentManager, "dialog")
                    }
                }
            })
        }

        pictureViewModel.getTypePicture()

        binding.iHeader.ivHeader.setOnClickListener { activity?.onBackPressed() }
        binding.btnPhotoBeforePicture.setOnClickListener {
            if (pictureBeforeAdapter.collection.size < 5) {
                checkCameraPermissions(before)
            } else {
                MessageDialogFragment.newInstance(this@PictureFragment,getString(R.string.error_photo_limit))
                    .show(childFragmentManager, "dialog")
            }
        }
        binding.btnPhotoAfterPicture.setOnClickListener {
            if (pictureAfterAdapter.collection.size < 5) {
                checkCameraPermissions(after)
            } else {
                MessageDialogFragment.newInstance(this@PictureFragment,getString(R.string.error_photo_limit))
                    .show(childFragmentManager, "dialog")
            }
        }

        binding.btnSavePictures.setOnClickListener {
            if(type!="terminado")
            {
                showProgressDialog()
                getLastLocation()
                val report = PhotoReport(
                    pictureBeforeAdapter.collection,
                    pictureAfterAdapter.collection,
                    binding.tvComments.text.toString(),
                    ZonedDateTime.now(ZoneId.of("America/Lima")).toLocalDateTime().toInstant(ZoneOffset.UTC).toString(),
                    longitude.toDouble(), latitude.toDouble(),
                )
                pictureViewModel.saveReport(report, "En proceso","borrador")
                activity?.onBackPressed()
            }
            else
                notify(activity, translateObject.findTranslate("tvAlreadyRegistered") ?: "Ya se registro")
        }
        binding.btnSendPictures.setOnClickListener {
            if(type!="terminado")
            {
                showProgressDialog()
                getLastLocation()
                val report = PhotoReport(
                    pictureBeforeAdapter.collection,
                    pictureAfterAdapter.collection,
                    binding.tvComments.text.toString(),
                    ZonedDateTime.now(ZoneId.of("America/Lima")).toLocalDateTime().toInstant(ZoneOffset.UTC).toString(),
                    longitude.toDouble(), latitude.toDouble(),
                )
                pictureViewModel.saveReport(report, "En proceso","terminado")

                val confirm = ConfirmSyncDialogFragment(this)
                confirm.listener = object : ConfirmSyncDialogFragment.Callback {
                    override fun onClick() {

                        showProgressDialog()
                        pictureViewModel.syncOnePicture()
                    }

                    override fun onBack() {
                        activity?.onBackPressed()
                    }
                }
                confirm.show(childFragmentManager, "dialog")
            }
            else
                notify(activity,"Ya se registró")


        }
        changeViewsFragment()
        return binding.root
    }

    private fun disableButtons(){
        Log.d("Terminado", type.toString())
        val disabled = type == "terminado"
        binding.btnSavePictures.apply{
            isEnabled = disabled
            alpha=0.5f
        }
        binding.btnPhotoAfterPicture.apply{
            isEnabled = disabled
            alpha = 0.5f
        }
        binding.btnPhotoBeforePicture.apply{
            isEnabled = disabled
            alpha=0.5f
        }
        binding.btnSendPictures.apply {
            isEnabled = disabled
            alpha=0.5f
        }
        binding.tvComments.apply{
            isEnabled = disabled
            alpha=0.5f
        }
    }

    override fun changeViewsFragment() {
        translateObject.apply {
            if(getInstance().isNotEmpty()){
                binding.tvPVPicture.text = findTranslate("tvPVPicture")
                binding.btnPhotoBeforePicture.text = findTranslate("btnPhotoBeforePicture")
                binding.btnPhotoAfterPicture.text = findTranslate("btnPhotoAfterPicture")
                binding.tvTtitleComments.text = findTranslate("tvTtitleComments")
                binding.contentLayout.hint = findTranslate("tvComments")
                binding.btnSavePictures.text = findTranslate("btnSavePictures")
                binding.btnSendPictures.text = findTranslate("btnSendPictures")
                binding.iHeader.title = findTranslate("tvTitlePictureFragment")
                binding.iHeader.sub = findTranslate("tvSubPictureFragment")
            }else{
                authViewModel.getTranslate()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            when (requestCode) {
                before -> if (resultCode == Activity.RESULT_OK) {
                    convertBase64(photoFile!!.absolutePath)?.let {
                        pictureBeforeAdapter.collection.add(it)
                        pictureBeforeAdapter.notifyDataSetChanged()
                    }
                }
                after -> if (resultCode == Activity.RESULT_OK) {
                    convertBase64(photoFile!!.absolutePath)?.let {
                        pictureAfterAdapter.collection.add(it)
                        pictureAfterAdapter.notifyDataSetChanged()
                    }
                }
            }
        } catch (e : Exception){
            Log.d("Error", e.toString())
            MessageDialogFragment.newInstance(this@PictureFragment,"Ha ocurrido un error\nVuelvalo a intentar").show(childFragmentManager,"dialog")
        }
    }

    private fun launchCamera2(origin: Int) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            photoFile = createImageFile()
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    requireContext(),
                    "com.tawa.captureimage.fileprovider",
                    photoFile!!
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, origin)
            }
        } catch (ex: Exception) {
            Log.d("Error", ex.toString())
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
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

    private fun openImage(image: String) {
        val uri = getUriImage(requireContext(), decodeBase64(image))
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(uri, "image/*")
        activity?.startActivity(intent)
    }

    private fun checkCameraPermissions(origin: Int) {
        Dexter.withActivity(activity)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    launchCamera2(origin)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    private fun decodeBase64(base64:String):Bitmap{
        val encodedString = "data:image/jpg;base64, $base64"
        val pureBase64Encoded: String = encodedString.substring(encodedString.indexOf(",") + 1)
        val decodedString: ByteArray = Base64.decode(pureBase64Encoded, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    private fun convertBase64(path: String): String? {
        val imageFile = File(path)
        var fis: FileInputStream? = null
        try {
            fis = FileInputStream(imageFile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        val bm = BitmapFactory.decodeStream(fis)
        val bb =Bitmap.createScaledBitmap(bm, 400, 600, false)
        val outputStream = ByteArrayOutputStream()
        bb.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    private fun getUriImage(context: Context, inImage: Bitmap): Uri? {
        val path = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            inImage,
            "IMG_" + System.currentTimeMillis(),
            null
        )
        return Uri.parse(path)
    }

    private fun setUpBinding() {
        binding.viewModel = pictureViewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }

    private fun disableComponents() {
        binding.btnPhotoBeforePicture.apply {
            isEnabled = false
            alpha = 0.5F
        }
        binding.btnPhotoAfterPicture.apply {
            isEnabled = false
            alpha = 0.5F
        }
        binding.btnSendPictures.apply {
            isEnabled = false
            alpha = 0.5F
        }
        binding.btnSavePictures.apply {
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
                .setText(translateObject.findTranslate("tvFivePhotosPictureFragment") ?: "Máximo solo cinco fotos")
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