package com.tawa.allinapp.features.pdv

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Base64OutputStream
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.facebook.drawee.drawable.ScalingUtils
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentPdvBinding
import com.tawa.allinapp.features.init.InitViewModel
import com.tawa.allinapp.features.init.ui.SelectPdvDialogFragment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


class PdvFragment : BaseFragment() {

    private lateinit var pdvViewModel: PdvViewModel
    private lateinit var initViewModel: InitViewModel
    private lateinit var binding: FragmentPdvBinding
    var idPdv = ""
    var latitudePdv = ""
    var longitudePdv = ""
    var namePdv = ""
    var img64 = ""


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
                idPdv = it.id
                binding.tvNamePdv.text = it.pdvDescription.toString()
                binding.edNameContact.setText(it.nameUser?:"")
                binding.edPhoneContact.setText(it.phoneUser?:"")
                binding.edRuc.setText(it.ruc?:"")
                namePdv = it.pdvDescription?:""
                latitudePdv = it.latitude.toString()
                longitudePdv = it.longitude.toString()
                if(it.image!!.isNotEmpty()) {
                    img64 = it.image.toString()
                    binding.headerPhotoPdv.setImageURI(
                        getImageUri(
                            requireContext(),
                            decodeBase64(it.image.toString())
                        ), ""
                    )
                }
            }})
            observe(successUpdatePdvLocal,{it?.let {
                if(it){
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
            }})
            observe(successUpdatePdv,{it?.let {
                if(it){
                    pdvViewModel.updatePdv(idPdv,"2")
                }
            }})
            observe(successUpdatePdvState,{it?.let {
                if(it) {
                    notify(activity, "Actualización exitosa")
                    activity?.onBackPressed()
                }
            }})

        }
        initViewModel = viewModel(viewModelFactory){
            observe(idPv,{
                it?.let{
                //Cambiar nombre del punto de venta ?
                //if(it) findNavController().navigate(PdvFragmentDirections.actionNavigationPdvSelf())
            }})
         }

        pdvViewModel.getPdv()
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
            notify(activity, "Se actualizó la ubicación")
        }
        binding.btnShowModalPhotoPdv.setOnClickListener {
            showDialogTakePhoto()
        }
        binding.tvNamePdv.setOnClickListener {
            showPdvSelectDialog()
        }
        binding.btnUpdatePdvRemote.setOnClickListener {
            pdvViewModel.updatePdv(
                idPdv,
                binding.edNameContact.text.toString(),
                binding.edPhoneContact.text.toString(),
                binding.edRuc.text.toString(),
                latitudePdv,
                longitudePdv,
                img64,
                "1"
            )
        }
        changeViewsFragment()
        return binding.root
    }

    private fun showPdvSelectDialog(){
        val dialog = SelectPdvDialogFragment(this)
        dialog.listener = object : SelectPdvDialogFragment.Callback{
            override fun onAccept(id:String,pv:String,description: String) {
                initViewModel.setPv(id,pv,description)
                findNavController().navigate(PdvFragmentDirections.actionNavigationPdvSelf())
            }
        }
        dialog.show(childFragmentManager,"")
    }

    private fun showMapDialogPdv(latitude:String,longitude:String,name:String){
        val dialog = PdvMapDialogFragment.newInstance(latitude,longitude,name)
        dialog.show(childFragmentManager,"")
    }

    private fun showDialogTakePhoto(){
        val dialog = TakePhotoDialogFragment(this)
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
        val bb =Bitmap.createScaledBitmap(bm, 400, 800, false)
        val outputStream = ByteArrayOutputStream()
        bb.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    private fun getImageUri(context: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            inImage,
            "IMG_" + System.currentTimeMillis(),
            null
        )
        return Uri.parse(path)
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
    override fun changeViewsFragment() {
        translateObject.apply {
            if(getInstance().isNotEmpty()) {
                binding.tvTitlePdvFragment.text = findTranslate("tvTitlePdvFragment")
                binding.tvTitlteInformPdvFragment.text = findTranslate("tvTitlteInformPdvFragment")
                binding.tvGeolocationPdvFragment.text = findTranslate("tvGeolocationPdvFragment")
                binding.showLocationPdv.text = findTranslate("showLocationPdv")
                binding.btnUpdateLocation.text = findTranslate("btnUpdateLocation")
                binding.tvNamePdvFragment.text = findTranslate("tvNamePdvFragment")
                binding.edNameContact.hint = findTranslate("edNameContact")
                binding.tvPhonePdvFragment.text = findTranslate("tvPhonePdvFragment")
                binding.edPhoneContact.hint = findTranslate("edPhoneContact")
                binding.edRuc.hint = findTranslate("edRucPdvFragment")
                binding.btnUpdatePdvRemote.text = findTranslate("btnUpdatePdvRemote")
            } else authViewModel.getTranslate()
        }
    }
}