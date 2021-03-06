package com.tawa.allinapp.features.init.ui


import android.Manifest
import android.content.Context
import android.content.Intent
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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tawa.allinapp.R
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.extensions.failure
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.databinding.FragmentInitBinding
import com.tawa.allinapp.features.HomeActivity
import com.tawa.allinapp.features.auth.ui.LoginActivity
import com.tawa.allinapp.features.init.InitViewModel
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.*


class InitFragment : BaseFragment() {

    private lateinit var initViewModel: InitViewModel
    private lateinit var binding: FragmentInitBinding
    private lateinit var locationManager:LocationManager
    private var checkOutDialog: CheckOutDialogFragment? = null
    private var checkIn:Boolean = true
    private var _user = ""
    private var _pvId = ""
    private var role = ""
    private var _battery = ""
    private var _pv: String = ""
    private lateinit var _lat: String
    private lateinit var _long: String
    private var companySelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentInitBinding.inflate(inflater)
        showProgressDialog()
        initViewModel = viewModel(viewModelFactory) {
            observe(dayState, { it?.let { if(it) {
                getLastLocation()
                val currentDay = getString(R.string.current_day, getDayWeek(),getDayMonth(),getMonth(),getYear())
                binding.currentDay.text  = currentDay
            }}})
            observe(checkInMode, { it?.let {
                Log.d("checkMode",it.toString())
                if(it.idUser.isNotEmpty()){
                    checkIn = it.pending != "PENDING"
                    binding.tvCheckIn.text = it.pvName
                    //changeStateStartCheckIn(it)
                    //if(!checkIn) binding.tvCheckIn.text = _pv
                }else{
                    checkIn = true
                }
                setCheckModeBtn(checkIn)
                hideProgressDialog()
            }})
            observe(idUser, { it?.let {
                _user = it
            }})
            observe(pvDesc, { it?.let {
                if(it != ""){
                    initViewModel.changeStatePvDesc("")
                    _pv = it
                    Log.d("_pv",_pv.toString())
                    initViewModel.getIdPV()
                }
            }})
            observe(pvId, { it?.let {
                ///bug
                if(it != ""){
                    initViewModel.changeStatePv("")
                    _pvId = it
                    Log.d("_pvId",_pvId)
                    showCheckOut()
                }
            }})
            observe(successCheckIn, {
                it?.let {
                    if (it) {
                        //getActualLocation()
                        getLastLocation()

                        changeStateSuccessCheckIn(false)
                        initViewModel.getCheckMode()
                        Log.d("successCheckin", "last -> $latitude  long -> $longitude")
                        //initViewModel.updateStatus(_lat,_long,_battery,0)
                        initViewModel.updateStatus(latitude, longitude, _battery, 0)
                        notify(activity, translateObject.findTranslate("tvSuccessfulRegistration") ?: "Registro exitoso")
                    }
                }
            })
            observe(successSendCheck, { it?.let {
                if(it.isNotEmpty()){
                    initViewModel.changeCheckState("")
                    if(it == "CREADO SATISFACTORIAMENTE") notify(activity,translateObject.findTranslate("tvMessageSentSuccessNotify") ?: "La informaci??n fue guardada y enviada correctamente")
                    else if(it =="YA REALIZO UN INGRESO EN ESTE PUNTO DE VENTA EL DIA DE HOY") notify(activity,"Informaci??n registrada y no enviada")
                    else if(it=="YA REALIZO UN SALIDA EN ESTE PUNTO DE VENTA EL DIA DE HOY") notify(activity,"Informaci??n registrada y no enviada")
                }
            } })
            observe(successUpdate, { it?.let {
                //bug
                if(it){
                    initViewModel.changeSuccessUpdate(false)
                    //initViewModel.sendCheck(_lat, _long, type.value!!)
                    Log.d("latlng", "$latitude $longitude")
                    initViewModel.sendCheck(latitude,longitude, type.value!!)
                    //initViewModel.changeSuccessUpdate(false)
                   // Toast.makeText(context,"Se env??o actualizacion",Toast.LENGTH_SHORT).show()
                    }
            } })
            observe(logoCompany,{it?.let{
                if(it.isNotEmpty())
                    setLogoCompany(it)
                else
                    binding.imageView16.isVisible= false
            }})
            observe(successCheckOut,{ it?.let {
                if(it) {
                    //getLastLocation()
                    //getActualLocation()
                    initViewModel.changeStateSuccessCheckout(false)
                    initViewModel.getCheckMode()
                    getNewLatitudeNLongitude()
                    initViewModel.updateStatus(latitude,longitude,_battery,1)
                    notify(activity, "Registro exitoso")
                }
            }})
            observe(successSyncChecks, {
                it?.let {
                    if (it) {
                        changeStateSyncChecks(false)
                        initViewModel.syncPhotoReportMassive(latitude, longitude)
                    }
                }
            })
            observe(successSyncPhotoReports, { it?.let {
                getActualLocation()
                if(it) {
                    changeStateSuccessSyncPhotoReports(false)
                    initViewModel.syncStandardReportsMassive(latitude, longitude)
                }
            } })
            observe(successSyncSku, {
                it?.let {
                    //Log.d(TAG, "SuccessSyncSku se realizado correctamente")
                    if (it) {
                        initViewModel.changeStateSyncSku(false)
                    }
                    //if (it) initViewModel.syncAudio()
                }
                //changeStateSyncSku(false)
            })
            observe(successSyncReportStandard, { it?.let {
                getActualLocation()
                if (it){
                    changeStateSuccesSyncReportStandard(false)
                    initViewModel.syncSkuMassive(latitude,longitude)
                }
            }})
            observe(descPV, { it?.let {
                if (it.isNotEmpty()){ binding.tvCheckIn.text = it }
            }})
            observe(successSyncAudio, { it?.let {
                if(it){
                   // Log.d(TAG,"// se realizado correctamente")
                    hideProgressDialog()
                    MessageDialogFragment.newInstance(
                        this@InitFragment,
                        message = "",
                        title = translateObject.findTranslate("tvSuccessSyncFragment") ?: "Sincronizaci??n exitosa",
                        icon = R.drawable.ic_checkin).show(childFragmentManager, "dialog")
                }
            }})
            observe(userName, {
                it?.let {
                    binding.tvHeaderName.text = it
                    binding.btUser.text =
                        if (it.isNotEmpty()) getFirstLetters(it.toUpperCase()) else ""
                }
            })
            observe(successGetRole,{
                it?.let {
                    if(it.isNotEmpty()) {
                        role = it
                        if(it=="supervisor".toUpperCase()) {
                            (activity as HomeActivity).showInforms()
                            (activity as HomeActivity).showRoutes()
                            binding.viewBtnInforms.isVisible = true
                            binding.imageViewInforms.isVisible= true
                            binding.tvInformsInitFragment.isVisible = true
                            binding.viewBtnRoutes.isVisible = true
                            binding.imageViewRoutes.isVisible= true
                            binding.tvRoutesInitFragment.isVisible = true
                        }
                    }
                }
            })
            observe(successGetCompanyId, {
                it?.let {
                    if(it.isNotEmpty()){
                        initViewModel.getLogoCompany()
                        initViewModel.getCheckMode()
                    }

                }
            })
            observe(setIdCompanySuccess,{it?.let {
                     initViewModel.getIdCompany()
            }})
            observe(getPvIdf,{
                it?.let {
                    Log.d("pvId",_pvId.toString())
                    _pvId = it
                }
            })
            observe(selector,{
                it?.let {
                    companySelected = it
                }
            })
            observe(updateNotify,{it?.let{
                if(it) initViewModel.getCountNotify()
            }})
            observe(countNotify,{it?.let{
                if(it>0)
                {
                    binding.edCountNotify.isVisible = true
                    binding.vNotifyCount.isVisible = true
                    binding.edCountNotify.text = it.toString()
                }
            }})
            observe(clearCountNotify,{it?.let{
                if(it)
                {
                    binding.edCountNotify.isVisible = false
                    binding.vNotifyCount.isVisible = false
                }
            }})
            observe(companies,{it?.let{
                    if(it.isNotEmpty()){
                        val count = it.count()
                        if(typeCompany.value==0){
                            if(count>1){
                                if(binding.btCheckIn.text != translateObject.findTranslate("btCheckOutInitFragment") ?: "Marcar salida")
                                    showSelectorSync()
                            }
                        }
                        else{
                            if(count==1){
                                showProgressDialog()
                                authViewModel.getCompaniesRemoteSync(0,it[0])
                            }
                            else
                                showSelector()
                        }
                    }
            }})
            failure(failure, ::handleFailure)
        }
        authViewModel = viewModel(viewModelFactory){
            observe(successGetCompanies,{it?.let{
                if(it){
                    if(typeSync.value==1){
                        hideProgressDialog()
                        showSuccessSyncDialog()
                    }
                    else{
                        initViewModel.setIdCompany(listCompany.value!!.id,listCompany.value!!.image)
                        initViewModel.getReportsSku(listCompany.value!!.id)
                        initViewModel.getPdvRemote(listCompany.value!!.id)
                        hideProgressDialog()
                    }

                }
            }})
        }

        //Seleccionando empresa
        initViewModel.getPVDesc()
        initViewModel.getLogoCompany()
        initViewModel.getIdUser()
        initViewModel.getUserName()
        initViewModel.getPvIdFirstTime()
        initViewModel.getCheckMode()
        initViewModel.getIdCompanyPreferences().let { if(it.isEmpty()) {
            initViewModel.getCompanies(1)
            //showSelector()
        } }

        binding.btCheckIn.setOnClickListener{
            if(isLocationEnabled()){
                if(checkIn) showSelectorCheckIn()
                else initViewModel.getDescPV()
            }else MessageDialogFragment.newInstance(
                this,
                translateObject.findTranslate("tvGpsActivatedMessageFrag")
                    ?: "Se tiene que activar el GPS para usar esta funcionalidad"
            ).show(childFragmentManager, "")
        }
        binding.btUser.setOnClickListener {
            val frag = UserMenuDialogFragment.newInstance(this)
            frag.listener = object : UserMenuDialogFragment.Callback {
                override fun onAccept() {
                    logout()
                    //initViewModel.setPv("","","")
                    showLogin(context)
                }
                override fun onNotify() {
                    initViewModel.clearCountNotify()
                    showMessagesDialog()
                }
            }
            frag.show(childFragmentManager, "participant")
        }
        binding.btSync.setOnClickListener {
            showProgressDialog()
            authViewModel.getCompaniesRemoteSync(1,null)
            if(isLocationEnabled()){
                getActualLocation()
                initViewModel.syncCheck(latitude,longitude)
            }else{
                MessageDialogFragment.newInstance(this,
                    translateObject.findTranslate("tvGpsActivatedMessageFrag")
                        ?: "Se tiene que activar el GPS para usar esta funcionalidad"
                ).show(childFragmentManager,"")
                hideProgressDialog()
            }
           // initViewModel.syncStandardReportsMassive("12","10")
        }
        binding.viewBtnRoutes.setOnClickListener { findNavController().navigate(InitFragmentDirections.actionNavigationInitToNavigationRoutes(role)) }
        binding.viewBtnPV.setOnClickListener {
            showSelectPdvDialog()
            //findNavController().navigate(InitFragmentDirections.actionNavigationInitToPdvFragment())
        }
        binding.viewBtnCalendar.setOnClickListener { findNavController().navigate(InitFragmentDirections.actionNavigationInitToNavigationCalendar()) }
        binding.viewBtnReports.setOnClickListener {
            //initViewModel.getPVId()
            //findNavController().navigate(InitFragmentDirections.actionNavigationInitToNavigationReports())
            if(_pvId.isNotEmpty()){
                findNavController().navigate(InitFragmentDirections.actionNavigationInitToNavigationReports())

            }
            else MessageDialogFragment.newInstance(this,
                translateObject.findTranslate("tvErrorDoCheckInMessageFrag")
                    ?: "Debes seleccionar o hacer chekIn en un punto de venta").show(childFragmentManager, "errorDialog")
        }
        binding.viewBtnInforms.setOnClickListener{ findNavController().navigate(InitFragmentDirections.actionNavigationInitToNavigationInforms()) }
        binding.viewBtnMessages.setOnClickListener { findNavController().navigate(InitFragmentDirections.actionNavigationInitToNavigationMessages()) }
        binding.viewBtnTasks.setOnClickListener { findNavController().navigate(InitFragmentDirections.actionNavigationInitToNavigationTasks()) }

        initNotify()
        initViewModel.getCountNotify()

        return binding.root
    }

    private fun getFirstLetters(text: String): String {
        var text = text
        var firstLetters = ""
        text = text.replace("[.,]".toRegex(), "") // Replace dots, etc (optional)
        for (s in text.split(" ").toTypedArray()) {
            firstLetters += s[0]
        }
        return firstLetters
    }

    private fun setCheckModeBtn(value: Boolean){
        value.let { enable ->
            binding.btCheckIn.apply{
                background =
                    if(enable) ResourcesCompat.getDrawable(resources, R.drawable.bg_button_check_in, null)
                    else ResourcesCompat.getDrawable(resources, R.drawable.bg_button_check_out, null)
                text =
                    if (enable) translateObject.findTranslate("btCheckInInitFragment") ?: "Marcar ingreso"
                    else translateObject.findTranslate("btCheckOutInitFragment") ?: "Marcar salida"
            }
        }
    }

    private fun initNotify(){
        val socketHandler = SocketHandler(Prefs(requireContext()))
        socketHandler.setSock()
        val socket  = socketHandler.getSock()
        socket.connect()
        socket.on("notify"){ args ->
            activity?.runOnUiThread {
                val message = JSONObject(args[0].toString()).getString("message")
                showNotification(message, "ch1")
                initViewModel.updateCountNotify()
            }
        }
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
                initViewModel.getCheckMode()
                //initViewModel.getPVSaved()
            }
            override fun onReject() {
                val msg = MessageDialogFragment.newInstance(this@InitFragment,"Ocurrio un error con el servidor.\n Por favor intentelo de nuevo")
                msg.listener = object : MessageDialogFragment.Callback{
                    override fun onAccept() {
                        logout()
                        val intent = Intent(requireActivity(), LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
                msg.show(childFragmentManager, "dialog")
            }
        }
        dialog.show(childFragmentManager, "dialog")
    }

    private fun showSelectorSync(){
        val dialog = SelectorSyncDialogFragment(this)
        dialog.listener = object : SelectorSyncDialogFragment.Callback{
            override fun onAccept() {
                initViewModel.getLogoCompany()
                initViewModel.getCheckMode()
                //initViewModel.getPVSaved()
            }
            override fun onReject() {
                val msg = MessageDialogFragment.newInstance(this@InitFragment,"Ocurrio un error con el servidor.\n Por favor intentelo de nuevo")
                msg.listener = object : MessageDialogFragment.Callback{
                    override fun onAccept() {
                        logout()
                        val intent = Intent(requireActivity(), LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
                msg.show(childFragmentManager, "dialog")
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

    private fun showSuccessSyncDialog(){
        val dialog  = MessageDialogFragment.newInstance(
            this@InitFragment,
            message = "",
            title = translateObject.findTranslate("tvSuccessSyncFragment") ?: "Sincronizaci??n exitosa",
            icon = R.drawable.ic_checkin
        )
        dialog.listener = object : MessageDialogFragment.Callback{
            override fun onAccept() {
               initViewModel.getCompanies(0)
            }
        }
        dialog.show(childFragmentManager, "dialog")
    }

    private fun showSelectorCheckIn(){
        val dialog = CheckInDialogFragment(this)
        dialog.listener = object : CheckInDialogFragment.Callback {
            override fun onAccept(
                idUser: String,
                pvId: String,
                pv: String,
                lat: String,
                long: String,
                description: String,
                battery: String
            ) {
                //getActualLocation()
                getLastLocation()
                _pv = pv;_pvId = pvId;_battery = battery
                Log.d("PV", _pv + _pvId)
                if (latitude != "" && longitude != "") {
                    initViewModel.setCheckIn(idUser, pvId, _pv, lat, long)
                    binding.tvCheckIn.text = description
                } else {
                    val dialog =
                        MessageDialogFragment.newInstance(this@InitFragment,translateObject.findTranslate("tvErrorGLocationsMessageFrag")
                            ?: "Ha ocurrido un error al capturar tu ubiaci??n. Vuelvo a intentar por favor.")
                    dialog.show(childFragmentManager, "")
                }

            }

            override fun onSnack(snack: Boolean) {
                if (snack) notify(activity, "Ya realizaste check-in en el punto de venta")
            }

            override fun onClose() {
                initViewModel.getPVDesc()
            }
        }
        dialog.show(childFragmentManager, "dialog")
    }

    override fun onStart() {
        super.onStart()
        changeViewsFragment()
    }

    private fun setLogoCompany(image:String){
        binding.imageView16.isVisible = true
        Log.d("image",image.toString())
        decodeBase64(image)?.let {
            binding.imageView16.setImageBitmap(it)
        }
    }

    private fun decodeBase64(base64:String): Bitmap? {
        return try{
            //val encodedString = "data:image/jpg;base64, $base64"
            val pureBase64Encoded: String = base64.substring(base64.indexOf(",") + 1)
            val decodedString: ByteArray = Base64.decode(pureBase64Encoded, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        }catch (e : Exception){
            Log.d("error",e.toString())
            null
        }
    }

    private fun getImageUri(context: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            inImage,
            "LOGO_COMPANY",
            null
        )
        return Uri.parse(path)
    }

    private fun showCheckOut(){
        checkOutDialog = CheckOutDialogFragment.newInstance(_pv, _user)
        checkOutDialog?.listener = object : CheckOutDialogFragment.Callback {
            override fun onAccept() {
                showProgressDialog()
                //getActualLocation()
                getLastLocation()
                getNewLatitudeNLongitude()
                //initViewModel.setPv("","","")
                Log.d("pv_id_checkout", _pvId.toString())
                Log.d("data ", latitude.toString() + longitude.toString())
                initViewModel.setCheckOut(_user,_pvId,_pv,latitude,longitude)
                //_pvId = ""
                //initViewModel.sendCheck(_lat,_long,1)
               // notify(requireActivity(), R.string.checkoout_successful)
            }
        }
        checkOutDialog?.show(childFragmentManager, "checkOutDialog")
    }

    private fun getNewLatitudeNLongitude(){
        val lng = LatLng(latitude.toDouble(),longitude.toDouble())
        val newLat = convertLatLngInMeter(lng)
        latitude = newLat.latitude.toString()
        longitude = newLat.longitude.toString()
        Log.d("newLat", newLat.toString())
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
        location?.let { location ->
            _lat = location.latitude.toString()
            _long = location.longitude.toString()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).hideNavBar()
        initViewModel.getRoleUser()
    }

    private fun logout(){
        initViewModel.setSession(false)
        initViewModel.setIdCompany("","")
        initViewModel.deletePvId()
    }

    override fun changeViewsFragment() {
        val navView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        translateObject.apply {
            if(getInstance().isNotEmpty()){
                Log.d("vistas",findTranslate("tvWelcomeInitFragment").toString())
                binding.tvWelcomeInitFragment.text = findTranslate("tvWelcomeInitFragment")?:"Buenos Dias"
                binding.tvPvInitFragment.text = findTranslate("tvPvInitFragment") ?: "Puntos de venta"
                binding.tvReportInitFragment.text = findTranslate("tvReportInitFragment") ?: "Reporte"
                binding.tvMessageInitFragment.text = findTranslate("tvMessageInitFragment") ?: "Mensajes"
                binding.tvInformsInitFragment.text = findTranslate("tvInformsInitFragment") ?: "Informes"
                binding.tvSyncInitFragment.text = findTranslate("tvSyncInitFragment") ?: "Sincronizar"
                binding.tvDocumentsInitFragment.text = findTranslate("tvDocumentsInitFragment") ?: "Documentos"
                binding.tvRoutesInitFragment.text = findTranslate("tvRoutesInitFragment") ?: "Rutas"
                binding.tvCalendarInitFragment.text = findTranslate("tvCalendarInitFragment") ?: "Calendario"
                binding.tvTaskInitFragment.text = findTranslate("tvTaskInitFragment") ?: "Tareas"
                navView?.menu?.findItem(R.id.navigation_informs)?.title =findTranslate("navigation_informs") ?: "Informes"
                navView?.menu?.findItem(R.id.navigation_routes)?.title = findTranslate("navigation_routes") ?: "Rutas"
                navView?.menu?.findItem(R.id.navigation_reports)?.title = findTranslate("navigation_reports") ?: "Reportes"
                navView?.menu?.findItem(R.id.navigation_pdv)?.title = findTranslate("navigation_pdv") ?: "Pdv"
                navView?.menu?.findItem(R.id.navigation_init)?.title = findTranslate("navigation_init") ?: "Inicio"
                setCheckModeBtn(checkIn)
            }else authViewModel.getTranslate()
        }

    }

        private fun setUpBinding() {
            binding.viewModel = initViewModel
            binding.lifecycleOwner = this
            binding.executePendingBindings()
        }
    }



