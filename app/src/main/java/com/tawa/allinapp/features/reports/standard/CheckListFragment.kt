package com.tawa.allinapp.features.reports.standard

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
import android.widget.*
import androidx.core.content.ContextCompat
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
import com.tawa.allinapp.data.repository.QuestionsRepository
import com.tawa.allinapp.databinding.FragmentChecklistBinding
import com.tawa.allinapp.models.Answer
import com.tawa.allinapp.models.Question
import java.io.File
import javax.inject.Inject


class CheckListFragment: BaseFragment() {

    private lateinit var binding: FragmentChecklistBinding
    private val TAKE_PHOTO_REQUEST = 101
    private var mCurrentPhotoPath: String = ""
    private lateinit var checkListViewModel: CheckListViewModel
    private  lateinit var  listRadiosButton:List<RadioButton>
    private  var statePhoto = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChecklistBinding.inflate(inflater)
        val id = arguments?.getString("id")
        //Toast.makeText(context,id,Toast.LENGTH_SHORT).show()

        checkListViewModel = viewModel(viewModelFactory) {
            observe(questions, { it?.let {
                for(list in it)
                    showQuestions(list.objectType,list.id,list.questionName,list.order)
            } })

            observe(answersRadio, { it?.let {
                if(order.value!! >0)
                {
                    addAnswersRadio(it,binding.contentCheckList,nameQuestion.value!!,order.value!!)
                }

            } })

            observe(answersCheck, { it?.let {
                if(order1.value!! >0)
                {
                    addAnswersCheck(it,binding.contentCheckList,nameQuestion.value!!,order1.value!!)
                }

            } })
        }

        checkListViewModel.getQuestions()
        binding.btnTakePhoto.setOnClickListener{
           validatePermissions()
        }
        binding.deletePhoto.setOnClickListener{
            binding.checkListPhoto.visibility = View.GONE
            binding.deletePhoto.visibility = View.GONE
            statePhoto = 0
        }
        return binding.root
    }

    private fun showQuestions(type:String,id:String,questionName:String,order:Int){
                if(type=="CHECKBOX")
                {
                    checkListViewModel.getAnswersRadio(id,questionName,order)
                }
                if(type=="CHECK BUTTON")
                {
                    checkListViewModel.getAnswersCheck(id,questionName,order)
                }
    }

    private fun addAnswersRadio(listAnswers:List<Answer>, linear: LinearLayout, nameQ:String,order: Int){
            val linearL = LinearLayout(context)
            linearL.orientation = LinearLayout.VERTICAL
            val textView  = TextView(context)
            textView.text = nameQ
            textView.textSize = 16f
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))
            linearL.addView(textView)

            val radioGroup = RadioGroup(context)
            for(list in listAnswers)
            {
                val radioButton  = RadioButton(context)
                radioButton.text = list.answerName
                radioGroup.addView(radioButton)
            }
           linearL.addView(radioGroup)

           linear.addView(linearL,order)

    }

    private fun addAnswersCheck(listAnswers:List<Answer>,linear: LinearLayout,nameQ: String,order: Int){
            val linearL = LinearLayout(context)
            linearL.orientation = LinearLayout.VERTICAL
            val textView  = TextView(context)
            textView.text = nameQ
            textView.textSize = 16f
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))
            linearL.addView(textView)

            for(list in listAnswers)
            {
                val checkBox  = CheckBox(context)
                checkBox.text = list.answerName
                linearL.addView(checkBox)
            }

            linear.addView(linearL,order)

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
            binding.deletePhoto.visibility = View.VISIBLE
            statePhoto = 1
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