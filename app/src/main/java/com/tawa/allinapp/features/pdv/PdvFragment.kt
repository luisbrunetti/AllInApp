package com.tawa.allinapp.features.pdv

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.drawee.drawable.ScalingUtils
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentPdvBinding
import java.io.File
import android.graphics.Bitmap

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Base64OutputStream
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.FileNotFoundException


class PdvFragment : BaseFragment() {

    private lateinit var pdvViewModel: PdvViewModel
    private lateinit var binding: FragmentPdvBinding
    var idPdv = ""
    var latitudePdv = ""
    var longitudePdv = ""
    var namePdv = ""
    var img64 = ""
    var stateLocation = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPdvBinding.inflate(inflater)


        pdvViewModel = viewModel(viewModelFactory) {
            observe(text, {
                it?.let {
                    getLastLocation()
                }
            })
            observe(successGetPdv,{it?.let{
                binding.tvNamePdv.text = it.pdvDescription.toString()
                binding.edNameContact.setText(it.nameUser?:"")
                binding.edPhoneContact.setText(it.phoneUser?:"")
                binding.edRuc.setText(it.ruc?:"")
                namePdv = it.pdvDescription?:""
                latitudePdv = it.latitude.toString()
                longitudePdv = it.longitude.toString()
                if(it.image!!.isNotEmpty())
                {
                   /* val imgFresco:SimpleDraweeView = binding.headerPhotoPdv
                    imgFresco.hierarchy.setPlaceholderImage(R.drawable.ic_img)
                    imgFresco.hierarchy.actualImageScaleType = ScalingUtils.ScaleType.CENTER*/
                }
            }})
        }
        arguments?.getString("id")?.let {
            idPdv = it
            pdvViewModel.getPdv(idPdv)
        }
        binding.btnBackPdv.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.showLocationPdv.setOnClickListener {
                showMapDialogPdv(latitudePdv,longitudePdv,namePdv)
        }
        binding.btnUpdateLocation.setOnClickListener {
            getLastLocation()
            latitudePdv= latitude
            longitudePdv = longitude
        }
        binding.btnShowModalPhotoPdv.setOnClickListener {
            showDialogTakePhoto()
        }
        binding.btnUpdatePdvRemote.setOnClickListener {
            pdvViewModel.updatePdvRemote(
                idPdv,
                binding.edNameContact.text.toString(),
                binding.edPhoneContact.text.toString(),
                binding.edRuc.text.toString(),
                latitudePdv,
                longitudePdv,
                img64
            )
        }
        return binding.root
    }

    private fun showMapDialogPdv(latitude:String,longitude:String,name:String){
        val dialog = PdvMapDialogFragment.newInstance(latitude,longitude,name)
        dialog.show(childFragmentManager,"")
    }

    private fun showDialogTakePhoto(){
        val dialog = TakePhotoDialogFragment()
        dialog.listener = object :TakePhotoDialogFragment.Callback{
            override fun onSave(urlImage: String) {
                val imageUri = Uri.fromFile(File(urlImage))

                binding.headerPhotoPdv.setImageURI(imageUri)
                img64 =convertBase64(urlImage)?:""
                binding.headerPhotoPdv.hierarchy.actualImageScaleType = ScalingUtils.ScaleType.FOCUS_CROP
            }

            override fun onClick() {
                TODO("Not yet implemented")
            }
        }
        dialog.show(childFragmentManager,"")
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
        val outputStream = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    private fun convertImageFileToBase64(file: File): String {
        return ByteArrayOutputStream().use { outputStream ->
            Base64OutputStream(outputStream, Base64.NO_PADDING).use { base64FilterStream ->
                file.inputStream().use { inputStream ->
                    inputStream.copyTo(base64FilterStream)
                }
            }
            return@use outputStream.toString()
        }
    }
}