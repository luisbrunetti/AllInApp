package com.tawa.allinapp.features.pdv

import android.content.Context
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
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.FileNotFoundException
import android.provider.MediaStore
import androidx.navigation.fragment.findNavController
import com.tawa.allinapp.features.init.ui.InitFragmentDirections
import com.tawa.allinapp.features.init.ui.SelectPdvDialogFragment


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
                    Toast.makeText(context, "Se actualizo correctamente", Toast.LENGTH_SHORT).show()
                    activity?.onBackPressed() }
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
        return binding.root
    }

    fun showPdvSelectDialog(){
        val dialog = SelectPdvDialogFragment(this)
        dialog.listener = object : SelectPdvDialogFragment.Callback{
            override fun onAccept(id:String) {
                findNavController().navigate(PdvFragmentDirections.actionNavigationPdvSelf(id))
            }
        }
        dialog.show(childFragmentManager,"")
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
        val outputStream = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    private fun getImageUri(context: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            inImage,
            "Title",
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
}