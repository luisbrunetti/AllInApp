package com.tawa.allinapp.features.init.ui


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.tawa.allinapp.R
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.extensions.failure
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentInitBinding
import com.tawa.allinapp.features.HomeActivity
import com.tawa.allinapp.features.init.InitViewModel
import java.io.ByteArrayOutputStream


class InitFragment : BaseFragment() {

    private lateinit var initViewModel: InitViewModel
    private lateinit var binding: FragmentInitBinding
    private lateinit var locationManager:LocationManager

    private var checkOutDialog: CheckOutDialogFragment? = null
    private var checkIn:Boolean = true
    private var _user = ""
    private var _pvId = ""
    private var _battery = ""
    private var _pv: String = ""
    private lateinit var _lat: String
    private lateinit var _long: String
    private var title  = "Check In"
    private var selector = false

    companion object {
        val TAG = "Init_Fragment"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentInitBinding.inflate(inflater)
        showProgressDialog()
        //Log.d("asd", activity?.intent?.getBooleanExtra("selector",false).toString())
        //selector = activity?.intent?.getBooleanExtra("selector",false)?:false
        initViewModel = viewModel(viewModelFactory) {
            observe(dayState, { it?.let { if(it) {
                val currentDay = getString(R.string.current_day, getDayWeek(),getDayMonth(),getMonth(),getYear())
                binding.currentDay.text  = currentDay
            } }})
            observe(checkInMode, { it?.let {
                checkIn = it
                //if(!checkIn) binding.tvCheckIn.text = _pv
                hideProgressDialog()
            }})
            observe(idUser, { it?.let {
                _user = it
            }})
            observe(pvDesc, { it?.let {
                _pv = it
                initViewModel.getIdPV()
            }})
            observe(pvId, { it?.let {
                ///bug
                if(it.isNotEmpty()){
                    _pvId = it
                    initViewModel.changeStatePv("")
                    showCheckOut()
                }
            }})
            observe(successCheckIn, { it?.let {
                    if(it)
                    { getActualLocation()
                        initViewModel.getCheckMode()
                        initViewModel.updateStatus(_lat,_long,_battery,0)
                        notify(activity,R.string.checkoout_successful)
                    }
            } })
            observe(successSendCheck, { it?.let {
                if(it.isNotEmpty()){
                    if(it == "CREADO SATISFACTORIAMENTE")
                        notify(activity,R.string.notify_sended)
                    if(it=="YA REALIZO UN INGRESO EN ESTE PUNTO DE VENTA EL DIA DE HOY")
                        notify(activity,R.string.notify_sended_error)
                    if(it=="YA REALIZO UN SALIDA EN ESTE PUNTO DE VENTA EL DIA DE HOY")
                        notify(activity,R.string.notify_sended_error)
                    initViewModel.changeCheckState("")
                }
            } })
            observe(successUpdate, { it?.let {
                if(it){
                    initViewModel.sendCheck(_lat, _long, type.value!!)
                   // Toast.makeText(context,"Se envío actualizacion",Toast.LENGTH_SHORT).show()
                    }
            } })
            observe(logoCompany,{it?.let{
                if(it.isNotEmpty())
                    setLogoCompany(it)
                else
                    binding.ivCompanyLogo.setActualImageResource(R.drawable.ic_img)
            }})
            observe(successCheckOut, { it?.let {
                if(it)
                {
                    getActualLocation()
                    initViewModel.getCheckMode()
                    initViewModel.updateStatus(_lat,_long,_battery,1)
                    notify(activity,R.string.checkoout_successful)
                }
            } })
            observe(successSyncChecks, { it?.let {
                if (it)
                {
                    initViewModel.syncPhotoReportMassive(_lat,_long)
                }
            } })
            observe(successSyncPhotoReports, { it?.let {
                getActualLocation()
                if(it)
                    initViewModel.syncStandardReportsMassive(_lat,_long)
            } })
            observe(successSyncSku, { it?.let {
                Log.d(TAG,"SuccessSyncSku se realizado correctamente")
                if(it){
                    hideProgressDialog()
                    MessageDialogFragment.newInstance(message = "", title = R.string.end_sync,icon = R.drawable.ic_checkin).show(childFragmentManager, "dialog")
                }
                //if (it) initViewModel.syncAudio()
            } })
            observe(successSyncReportStandard, { it?.let {
                getActualLocation()
                if (it) initViewModel.syncSkuMassive(_lat,_long)
            }})
            observe(descPV, { it?.let {
                if (it.isNotEmpty()){
                    binding.tvCheckIn.text = it
                    }
            }})
            observe(idPV, { it?.let {
                it.let {
                    _pvId = it
                }
                //if(it.isNotEmpty()) findNavController().navigate(InitFragmentDirections.actionNavigationInitToNavigationReports())
                //else MessageDialogFragment.newInstance("Debes seleccionar o hacer chekIn en un punto de venta").show(childFragmentManager, "errorDialog")
            }})
            observe(successSyncAudio, { it?.let {
                if(it){
                    Log.d(TAG,"SuccessSSyncAudio se realizado correctamente")
                    hideProgressDialog()
                    MessageDialogFragment.newInstance(message = "", title = R.string.end_sync,icon = R.drawable.ic_checkin).show(childFragmentManager, "dialog")
                }
            }})
            observe(userName, { it?.let {
                binding.tvHeaderName.text = it
                binding.btUser.text = if(it.isNotEmpty()) it.first().toString() else ""
            } })
            /*observe(idPv,{it?.let{
              /*  if(it)
                    findNavController().navigate(InitFragmentDirections.actionNavigationInitToPdvFragment())*/

            }})*/
            observe(successGetRole,{
                it?.let {
                    if(it.isNotEmpty()) {
                        if(it=="SUPERVISOR") {
                            (activity as HomeActivity).showInforms()
                            (activity as HomeActivity).showRoutes()
                            binding.viewBtnInforms.isVisible = true
                            binding.imageViewInforms.isVisible= true
                            binding.textViewInforms.isVisible = true
                            binding.viewBtnRoutes.isVisible = true
                            binding.imageViewRoutes.isVisible= true
                            binding.textRoutes.isVisible = true
                        }
                    }
                }
            })
            observe(successGetCompanyId, {
                Log.d("SucessCompanyId",it.toString())
                it?.let {
                    if(it.isEmpty()) {
                        showSelector()
                    }
                }
            })
            observe(getPvIdf,{
                it?.let {
                    _pvId = it
                }
            })
            failure(failure, ::handleFailure)
        }
        //Seleccionando empresa
        //if(selector) showSelector()
        initViewModel.getIdCompany()
        initViewModel.getPVDesc()
        initViewModel.getLogoCompany()
        initViewModel.getIdUser()
        initViewModel.getUserName()
        initViewModel.getCheckMode()
        initViewModel.getPvIdFirstTime()

        binding.btCheckIn.setOnClickListener{
            if(checkIn) showSelectorCheckIn()
            else initViewModel.getDescPV()
        }
        binding.btUser.setOnClickListener {
            val frag = UserMenuDialogFragment.newInstance()
            frag.listener = object : UserMenuDialogFragment.Callback {
                override fun onAccept() {
                    initViewModel.setSession(false)
                    initViewModel.setIdCompany("","")
                    showLogin(context)
                }
                override fun onSendPassword() {
                    findNavController().navigate(InitFragmentDirections.actionNavigationInitToSendPasswordFragment())
                }
            }
            frag.show(childFragmentManager, "participant")
        }
        binding.btSync.setOnClickListener {
            showProgressDialog()
            getActualLocation()
            initViewModel.syncCheck(_lat,_long)
           // initViewModel.syncStandardReportsMassive("12","10")
        }
        binding.viewBtnRoutes.setOnClickListener {
            findNavController().navigate(InitFragmentDirections.actionNavigationInitToNavigationRoutes())
        }
        binding.viewBtnPV.setOnClickListener {
            showSelectPdvDialog()
        }
        binding.viewBtnCalendar.setOnClickListener {
            findNavController().navigate(InitFragmentDirections.actionNavigationInitToNavigationCalendar())
        }
        binding.viewBtnReports.setOnClickListener {
            //initViewModel.getPVId()
            if(_pvId.isNotEmpty()) findNavController().navigate(InitFragmentDirections.actionNavigationInitToNavigationReports())
            else MessageDialogFragment.newInstance("Debes seleccionar o hacer chekIn en un punto de venta").show(childFragmentManager, "errorDialog")
        }
        binding.viewBtnInforms.setOnClickListener{
            findNavController().navigate(InitFragmentDirections.actionNavigationInitToNavigationInforms())
        }
        binding.viewBtnMessages.setOnClickListener {
            showMessagesDialog()
        }
        return binding.root
    }

    private fun showMessagesDialog(){
        val dialog = MessagesDialogFragment(this)
        dialog.show(childFragmentManager,"dialogMessages")
    }
    private fun showSelector(){
        val dialog = SelectorDialogFragment(this)
        dialog.listener = object : SelectorDialogFragment.Callback{
            override fun onAccept() {
                initViewModel.getLogoCompany()
                initViewModel.getPVSaved()
            }
        }
        dialog.show(childFragmentManager, "dialog")
    }

    private fun showSelectPdvDialog(){
        val dialog = SelectPdvDialogFragment(this)
        dialog.listener = object : SelectPdvDialogFragment.Callback{
            override fun onAccept(id:String,pv:String,description: String) {
                initViewModel.setPv(id,pv,description)
                findNavController().navigate(InitFragmentDirections.actionNavigationInitToPdvFragment())
            }
        }
        dialog.show(childFragmentManager,"")
    }

    private fun showSelectorCheckIn(){
        val dialog = CheckInDialogFragment(this)
        dialog.listener = object : CheckInDialogFragment.Callback {
            override fun onAccept(idUser:String,pvId:String, pv:String,lat:String, long:String,description: String,battery:String) {
                getActualLocation()
                _pv = pv;_pvId = pvId;_battery = battery
                initViewModel.setCheckIn(idUser,pvId,_lat,_long)
                binding.tvCheckIn.text = description
            }
            override fun onSnack(snack: Boolean) {
                if (snack) notify(activity,R.string.notify_already)
            }

            override fun onClose() {
                initViewModel.getPVDesc()
            }
        }
        dialog.show(childFragmentManager, "dialog")
    }

    private fun setLogoCompany(image:String){
        binding.ivCompanyLogo.isVisible = true
        binding.ivCompanyLogo.setImageURI(
            getImageUri(
                requireContext(),
                decodeBase64(image)
            ), ""
        )
    }

    private fun decodeBase64(base64:String): Bitmap {
        val encodedString = "data:image/jpg;base64, $base64"
        val pureBase64Encoded: String = encodedString.substring(encodedString.indexOf(",") + 1)
        val decodedString: ByteArray = Base64.decode(pureBase64Encoded, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
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

    private fun showCheckOut(){
        checkOutDialog = CheckOutDialogFragment.newInstance(_pv, _user)
        checkOutDialog?.listener = object : CheckOutDialogFragment.Callback {
            override fun onAccept() {
                showProgressDialog()
                getActualLocation()
                initViewModel.setCheckOut(_user,_pvId,_lat,_long)
                //initViewModel.sendCheck(_lat,_long,1)
               // notify(requireActivity(), R.string.checkoout_successful)
            }
        }
        checkOutDialog?.show(childFragmentManager, "checkOutDialog")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBinding()
        hideProgressDialog()
    }

    private fun getActualLocation() {
        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        _lat = location?.latitude.toString()
        _long = location?.longitude.toString()
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).hideNavBar()
        initViewModel.getRoleUser()
    }

    private fun setUpBinding() {
        binding.viewModel = initViewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }
}



