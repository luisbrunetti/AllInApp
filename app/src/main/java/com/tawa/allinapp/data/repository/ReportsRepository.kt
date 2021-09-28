package com.tawa.allinapp.data.repository

import android.annotation.SuppressLint
import android.util.Log
import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.functional.NetworkHandler
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.data.local.datasource.QuestionsDataSource
import com.tawa.allinapp.data.local.datasource.ReportsDataSource
import com.tawa.allinapp.data.local.models.*
import com.tawa.allinapp.data.remote.entities.ReportsSkuRemote
import com.tawa.allinapp.data.remote.entities.SynReportStandardRemote
import com.tawa.allinapp.data.remote.entities.UpdateStatusRemote
import com.tawa.allinapp.data.remote.service.ReportsService
import com.tawa.allinapp.models.AudioReport
import com.tawa.allinapp.models.PhotoReport
import com.tawa.allinapp.models.Report
import com.tawa.allinapp.models.ReportStatus
import com.tawa.allinapp.models.*
import java.util.*
import javax.inject.Inject

interface ReportsRepository {
    fun setReports(company: String): Either<Failure, Boolean>
    fun listReports(idCompany: String): Either<Failure, List<Report>>
    fun saveLocalPhotoReport(report:PhotoReport?,state:String,type: String): Either<Failure, Boolean>
    fun getReports(): Either<Failure,List<Report>>
    fun getSkuDetail(idSku:String): Either<Failure,List<SkuDetail>>
    fun getSku(): Either<Failure,List<Sku>>
    fun getStateSku(idPv:String): Either<Failure,String>
    fun syncPhotoReports(latitude: String,longitude: String): Either<Failure,Boolean>
    fun syncOnePhoto(): Either<Failure,Boolean>
    fun savePhotoReport(): Either<Failure, Boolean>
    fun getReportStatus(): Either<Failure, List<ReportStatus>>
    fun updateStatus(latitude:String,longitude:String,battery:String): Either<Failure, Boolean>
    fun saveLocalAudioReport(report: AudioReport): Either<Failure, Boolean>
    fun getReportsSku(company: String):Either<Failure, Boolean>
    fun insertSkuObservation(skuObservation: SkuObservation):Either<Failure, Boolean>
    fun getSkuObservation(idSkuDetail: String):Either<Failure, List<SkuObservation>>
    fun syncSku(idSku: String):Either<Failure, Boolean>
    fun syncSkuMassive(latitude: String,longitude: String):Either<Failure, Boolean>
    fun updateSkuDetail(idSkuDetail: String,stock:Boolean,exhibition:Boolean,price:Float):Either<Failure, Boolean>
    fun updateStateReport(idReport:String,state:String,type:String):Either<Failure, Boolean>
    fun updateStateSku(idSku:String,state:String,type:String,latitude: String,longitude: String):Either<Failure, Boolean>
    fun getUserType():Either<Failure, String>
    fun syncReportStandard(idReport: String,latitude: String,longitude: String):Either<Failure, Boolean>
    fun syncReportStandardMassive(latitude: String,longitude: String):Either<Failure, Boolean>
    fun syncReportAudio():Either<Failure, Boolean>
    fun getLocalPhotoReport(): Either<Failure, PhotoReport>
    fun getStateReport(idReport: String): Either<Failure,String>
    fun getStatePhotoReport(): Either<Failure, String>
    fun getTypePhotoReport(): Either<Failure, String>
    fun getCountSku(): Either<Failure, Int>
    fun updateReportPv(idReport: String,state: String,type: String,time:String,latitude: String,longitude: String):Either<Failure, Boolean>
    fun updateReportPvSync(idReport: String,state: String,type: String):Either<Failure, Boolean>
    fun deletePhotoReports(): Either<Failure, Boolean>
    fun getTypeSku(): Either<Failure,String>
    fun setSession(value: Boolean) :Either<Failure,Boolean>
    fun getAudioReport(idPv:String, idUser: String): Either<Failure, AudioReport>
    fun updateAudioReport(idPv: String, idUser: String, selected: String, selectedName: String, selectedPath:String, record:String, recordPath: String, recordSent:String) : Either<Failure, Boolean>
    class Network
    @Inject constructor(private val networkHandler: NetworkHandler,
                        private val reportsDataSource: ReportsDataSource,
                        private val questionsDataSource: QuestionsDataSource,
                        private val prefs: Prefs,
                        private val service: ReportsService,
    ): ReportsRepository{

        override fun setReports(company:String): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        Log.d("setReports", "token -> ${prefs.token!!} company -> ${company}")
                        val response = service.getReports("Bearer ${prefs.token!!}",company).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        body.data.map {

                                            for(type in it.reports)
                                            {
                                                Log.d("type",type.reportName.toString())
                                                reportsDataSource.insertReports(ReportModel(type.id?:"",type.reportName?:"",it.idCompany.id?:"",it.idCompany.nameCompany?:"",it.userAsig.id?:"",it.idUserMod?:"",it.feMod?:"","","No iniciado","0",""))
                                            }
                                           // reportsDataSource.insertReports(it.toModel())
                                            Log.d("reportes",it.toString())
                                        }
                                        Either.Right(true)
                                    }
                                    else Either.Left(Failure.DefaultError(body.message))
                                }?: Either.Left(Failure.DefaultError(""))
                            }
                            false -> Either.Left(Failure.ServerError)
                        }
                    } catch (e: Exception) {
                        Either.Left(Failure.DefaultError(e.message!!))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun syncOnePhoto(): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val request = reportsDataSource.getPhotoReports(prefs.pvId?:"",prefs.companyId?:"",prefs.idUser?:"").map {it.toRemoteOne() }
                        val response = service.syncOnePhotoReport("Bearer ${prefs.token!!}", request.first()).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Log.d("syncOnePhoto",body.message.toString())
                                        Either.Right(true)
                                    }
                                    else {
                                        Log.d("errorOnePhoto",body.message.toString())
                                        Either.Left(Failure.DefaultError(body.message))
                                    }
                                }?: Either.Left(Failure.DefaultError(""))
                            }
                            false -> Either.Left(Failure.ServerError)
                        }
                    } catch (e: Exception) {
                        Either.Left(Failure.DefaultError(e.message!!))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }

        }

        override fun syncPhotoReports(latitude: String,longitude: String): Either<Failure,Boolean>{
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val request = reportsDataSource.getAllPhotoReports(prefs.idUser?:"").map { it.toRemote(longitude.toDouble(),latitude.toDouble(),Calendar.getInstance().toInstant().toString()) }
                        val response = service.syncPhotoReports("Bearer ${prefs.token!!}", request).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        reportsDataSource.updatePhotoReports("Enviado","terminado")
                                        Log.d("syncMassiveReport",body.message.toString())
                                        Either.Right(true)
                                    }
                                    else {
                                        Log.d("errorMassiveReport",body.message.toString())
                                        Either.Left(Failure.DefaultError(body.message))
                                    }
                                }?: Either.Left(Failure.DefaultError(""))
                            }
                            false -> Either.Left(Failure.ServerError)
                        }
                    } catch (e: Exception) {
                        Either.Left(Failure.DefaultError(e.message!!))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun getStatePhotoReport(): Either<Failure, String> {
            return try {
                Either.Right(reportsDataSource.getStatePhoto(prefs.companyId?:"",prefs.pvId!!, prefs.idUser!!)?:"No iniciado")
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getTypePhotoReport(): Either<Failure, String> {
            return try {
                Either.Right(reportsDataSource.getTypePhoto(prefs.companyId?:"",prefs.pvId?:"", prefs.idUser?:"")?:"0")
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun deletePhotoReports(): Either<Failure, Boolean> {
            return try {
                reportsDataSource.deletePhotos()
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun saveLocalPhotoReport(report:PhotoReport?,state:String,type:String): Either<Failure, Boolean> {
            if (prefs.pvId!!.isEmpty())
                return Either.Left(Failure.DefaultError("Debe seleccionar hacer Checkin en un Punto de Venta"))
            else
                return try {
                    report?.let {
                        val before = report.before.size
                        val after = report.after.size
                        reportsDataSource.insertPhotoReport(
                            PhotoReportModel(
                                prefs.companyId?:"",
                                prefs.pvId?:"",
                                prefs.idUser?:"",
                                if (before >0) report.before[0] else "",
                                if (before >1) report.before[1] else "",
                                if (before >2) report.before[2] else "",
                                if (before >3) report.before[3] else "",
                                if (before >4) report.before[4] else "",
                                if (after >0) report.after[0] else "",
                                if (after >1) report.after[1] else "",
                                if (after >2) report.after[2] else "",
                                if (after >3) report.after[3] else "",
                                if (after >4) report.after[4] else "",
                                report.comments,
                                report.createAt,
                                state,
                                report.longitude,
                                report.latitude,
                                type
                            )
                        )
                        Log.d("savePhotoMODEL",PhotoReportModel(
                            prefs.companyId?:"",
                            prefs.pvId?:"",
                            prefs.idUser?:"",
                            if (before >0) report.before[0] else "",
                            if (before >1) report.before[1] else "",
                            if (before >2) report.before[2] else "",
                            if (before >3) report.before[3] else "",
                            if (before >4) report.before[4] else "",
                            if (after >0) report.after[0] else "",
                            if (after >1) report.after[1] else "",
                            if (after >2) report.after[2] else "",
                            if (after >3) report.after[3] else "",
                            if (after >4) report.after[4] else "",
                            report.comments,
                            report.createAt,
                            state,
                            report.longitude,
                            report.latitude,
                            type
                        ).toString())
                    } ?: kotlin.run {
                        reportsDataSource.insertPhotoReport(
                            PhotoReportModel(
                                prefs.companyId?:"",
                                prefs.pvId?:"",
                                prefs.idUser ?:"",
                                "", "", "", "", "", "", "", "", "", "",
                                null,
                                null,
                                state,
                                null, null,type
                            )
                        )
                    }
                    Either.Right(true)
                }catch (e:Exception){
                    Either.Left(Failure.DefaultError(e.message!!))
                }
        }

        override fun getLocalPhotoReport(): Either<Failure, PhotoReport> {
            return try {
                val response = reportsDataSource.getPhotoReports(prefs.pvId?:"",prefs.companyId?:"",prefs.idUser ?: "")
                if(response.isEmpty())
                    Either.Right( PhotoReport(emptyList(), emptyList(),"","",0.0,0.0) )
                else
                    Either.Right( response.first().toView() )
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun savePhotoReport(): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val reports = reportsDataSource.getPhotoReports(prefs.pvId?:"",prefs.companyId?:"", prefs.idUser ?: "")
                        var regs = 0
                        reports.map {
                            val response = service.setPhotoReports(
                                it.before1?:"",it.before2?:"",it.before3?:"",it.before4?:"",it.before5?:"",
                                it.after1?:"",it.after2?:"",it.after3?:"",it.after4?:"",it.after5?:"",it.comments?:""
                            ).execute()
                            if (response.isSuccessful) regs++
                        }
                        if (regs == reports.size) Either.Right(true) else Either.Left(Failure.DefaultError("No se insertaron algunos registros"))
                    } catch (e: Exception) {
                        Either.Left(Failure.DefaultError(e.message!!))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun getReportStatus(): Either<Failure, List<ReportStatus>> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getReportStatus("Bearer ${prefs.token!!}").execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Either.Right(body.data.map {
                                            it.toView()
                                        })
                                    }
                                    else Either.Left(Failure.DefaultError(body.message))
                                }?: Either.Left(Failure.DefaultError(""))
                            }
                            false -> Either.Left(Failure.ServerError)
                        }
                    } catch (e: Exception) {
                        Either.Left(Failure.DefaultError(e.message!!))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }

        }

        override fun updateStatus(latitude: String,longitude: String,battery: String): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.updateStatus("Bearer ${prefs.token!!}",UpdateStatusRemote.Request(latitude,longitude,battery)).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Either.Right(true)
                                    }
                                    else Either.Left(Failure.DefaultError(body.message))
                                }?: Either.Left(Failure.DefaultError(""))
                            }
                            false -> Either.Left(Failure.ServerError)
                        }
                    } catch (e: Exception) {
                        Either.Left(Failure.DefaultError(e.message!!))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun getReportsSku(company: String): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getReportsSku("Bearer ${prefs.token!!}",company).execute()

                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    body.data.map {
                                        reportsDataSource.insertSku(SkuModel(it.id,it.idReportPdv.idPuntoVenta.id,it.idCompany.id,it.idUserAsg.id,"No iniciado","0","","",""))
                                        for(products in it.idReportPdv.lineas)
                                        {
                                            products.idProducto.nombreProducto?.let { it1 ->
                                                products.idProducto.idSubsegmentoProd?.idSegmentoProd?.idSubcategoriaProd?.nombreSubcategoria?.let { it2 ->
                                                    products.idProducto.idSubsegmentoProd.idSegmentoProd.idSubcategoriaProd.idCategoriaProd?.nombreCategoria?.let { it3 ->
                                                        SkuDetailModel(products.id,products.idProducto.feCreacion,products.idProducto.id,
                                                            it1,
                                                            it3,
                                                            it2,products.inventario,products.precio,false,false,0.0f,it.id)
                                                    }
                                                }
                                            }?.let { it2 -> reportsDataSource.insertSkuDetail(it2) }
                                        }
                                    }
                                    Either.Right(true)
                                }?: Either.Left(Failure.DefaultError(""))
                            }
                            false -> Either.Left(Failure.ServerError)
                        }
                    } catch (e: Exception) {
                        Either.Left(Failure.DefaultError(e.message!!))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun getReports(): Either<Failure, List<Report>> {
            return try {
                val idPv = prefs.pvId!!
                Log.d("idPv", idPv)
                if(idPv.isEmpty())
                    Either.Right(reportsDataSource.getReports(prefs.companyId?:"",prefs.idUser?:"").map { it.toView() })
                else{
                    val count = reportsDataSource.getReportPvCount(prefs.companyId?:"",prefs.pvId?:"",prefs.idUser?:"")
                    val reports = reportsDataSource.getReports(prefs.companyId?:"",prefs.idUser?:"").map { it.toView() }
                    for(report in reports)
                    {
                        reportsDataSource.insertReportPv(ReportPvModel(prefs.pvId?:"",report.id,prefs.idUser?:"","No iniciado","0","","",""))
                    }
                    Log.d("reportStandard",reportsDataSource.getReportsPv(prefs.companyId?:"",prefs.pvId?:"",prefs.idUser?:"").map { it.toView() }.toString())
                    Either.Right(reportsDataSource.getReportsPv(prefs.companyId?:"",prefs.pvId?:"",prefs.idUser?:"").map { it.toView() })
                }
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun saveLocalAudioReport(report: AudioReport): Either<Failure, Boolean> {
            return try {
                reportsDataSource.insertAudioReport(report.toModel())
                Log.d("Audio Insert","Audio Insertado con exito ---> " + report.toModel())
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getSkuDetail(idSku: String): Either<Failure, List<SkuDetail>> {
            return try {
                Either.Right(reportsDataSource.getSkuDetail(idSku).map { it.toView() })
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun insertSkuObservation(skuObservation: SkuObservation): Either<Failure, Boolean> {
            return try {
                reportsDataSource.insertSkuObservation(skuObservation.toModel())
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getSkuObservation(idSkuDetail: String): Either<Failure, List<SkuObservation>> {
            return try {
                Either.Right(reportsDataSource.getSkuObservation(idSkuDetail).map { it.toView() })
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getSku(): Either<Failure, List<Sku>> {
            return try {
                Either.Right(reportsDataSource.getSku(prefs.pvId?:"",prefs.companyId?:"",prefs.idUser?:"").map { it.toView() })
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun syncSku(idSku:String): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        var id = ""
                        var idPv = ""
                        var idCompany = ""
                        var latitude = ""
                        var longitude = ""
                        val listSku = mutableListOf<ReportsSkuRemote>()
                        val linesAnswer = mutableListOf<Lines>()
                        val skus = reportsDataSource.getSku(idSku,prefs.pvId?:"",prefs.companyId?:"",prefs.idUser?:"").map { it.toView() }
                        for (sku in skus)
                        {
                            id = sku.id
                            idPv = sku.idPv
                            idCompany = sku.idCompany
                            latitude = sku.latitude
                            longitude = sku.longitude
                            val skuDetails = reportsDataSource.getSkuDetail(sku.id).map { it.toView() }
                            for(skuDetail in skuDetails)
                            {
                                val observationSku = reportsDataSource.getSkuObservation(skuDetail.id).map { it.toView() }
                                val observation = arrayListOf<String>()
                                for(obs in observationSku)
                                {
                                    observation.add(obs.observation)
                                }
                                linesAnswer.add(Lines(skuDetail.id,skuDetail.stock,skuDetail.exhibition,skuDetail.newPrice,observation))
                            }
                        }
                        Log.d("lineas",linesAnswer.toString())
                        val response = service.syncSku("Bearer ${prefs.token!!}",ReportsSkuRemote.Request(id,idPv,idCompany,"COMPLETADO",linesAnswer.map { it.toRequest() },longitude,latitude)).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Log.d("message",body.message.toString())
                                        reportsDataSource.updateStateSku(idSku,"Enviado","Terminado")
                                        Either.Right(true)
                                    }
                                    else {
                                        Log.d("errorSku",body.message.toString())
                                        Either.Left(Failure.DefaultError(body.message))
                                    }

                                }?: Either.Left(Failure.DefaultError(""))
                            }
                            false -> Either.Left(Failure.ServerError)
                        }
                    } catch (e: Exception) {
                        Either.Left(Failure.DefaultError(e.message!!))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun syncSkuMassive(latitude: String,longitude: String): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val listIdSku = arrayListOf<String>()
                        val listSku = mutableListOf<ReportsSkuRemote.RequestMassive>()
                        val linesAnswer = mutableListOf<Lines>()
                        val skus = reportsDataSource.getSkuReady(prefs.idUser?:"").map { it.toView() }
                        for (sku in skus)
                        {
                            val skuDetails = reportsDataSource.getSkuDetail(sku.id).map { it.toView() }
                            for(skuDetail in skuDetails)
                            {
                                val observationSku = reportsDataSource.getSkuObservation(skuDetail.id).map { it.toView() }
                                val observation = arrayListOf<String>()
                                for(obs in observationSku)
                                {
                                    observation.add(obs.observation)
                                }
                                linesAnswer.add(Lines(skuDetail.id,skuDetail.stock,skuDetail.exhibition,skuDetail.newPrice,observation))
                            }
                            listIdSku.add(sku.id)
                            listSku.add(ReportsSkuRemote.RequestMassive(sku.id,sku.idPv,sku.idCompany,linesAnswer.map { it.toRequest() },"COMPLETADO",sku.dateCreation,sku.longitude,sku.latitude,longitude,latitude,Calendar.getInstance().toInstant().toString()))
                        }
                        Log.d("lineas Sku Standard",linesAnswer.toString())
                        val response = service.syncSkuMassive("Bearer ${prefs.token!!}",listSku).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Log.d("message",body.message.toString())
                                        for(id in listIdSku)
                                            reportsDataSource.updateStateSku(id,"Enviado","Terminado")
                                        Either.Right(true)
                                    }
                                    else {
                                        Log.d("errorSkuMassive",body.message.toString())
                                        Either.Left(Failure.DefaultError(body.message))
                                    }

                                }?: Either.Left(Failure.DefaultError(""))
                            }
                            false -> Either.Left(Failure.ServerError)
                        }
                    } catch (e: Exception) {
                        Either.Left(Failure.DefaultError(e.message!!))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun updateSkuDetail(
            idSkuDetail: String,
            stock: Boolean,
            exhibition: Boolean,
            price: Float
        ): Either<Failure, Boolean> {
            return try {
                reportsDataSource.updateSkuDetail(idSkuDetail,stock,exhibition,price)
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun updateStateReport(idReport: String, state: String,type:String): Either<Failure, Boolean> {
            return if (prefs.pvId!!.isEmpty())
                Either.Left(Failure.DefaultError("Debe seleccionar hacer Checkin en un Punto de Venta"))
            else {
                try {
                    reportsDataSource.updateStateReports(idReport, state, type, prefs.pvId ?: "")
                    Either.Right(true)
                } catch (e: Exception) {
                    Either.Left(Failure.DefaultError(e.message!!))
                }
            }
        }

        override fun getUserType(): Either<Failure, String> {
            return try {
                Either.Right(prefs.role?:"")
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun syncReportStandard(idReport: String,latitude: String,longitude: String): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val reportData = mutableListOf<ReportStandard>()
                        val report = reportsDataSource.getReportPv(prefs.companyId?:"",prefs.pvId?:"",prefs.idUser?:"",idReport)
                        Log.d("syncReportStandard", "Obteniendo reportes -->  $report")
                        val questions  = questionsDataSource.getQuestionsByIdReport(idReport).map { it.toView() }
                        Log.d("syncReportStandard", "Questions ->>>  $questions")
                        for(question in questions)
                        {
                               val answerData= mutableListOf<AnswerStandard>()
                               val answers = questionsDataSource.getAnswersPv(question.id,report.idPv,report.idUser).map { it.toView() }
                               for(answer in answers)
                               {
                                   answerData.add(AnswerStandard(answer.id,answer.answerName,answer.data))
                               }
                               reportData.add(ReportStandard(question.id,question.questionName,answerData))
                        }
                        Log.d("report", "$reportData")
                        val response = service.synStandardReports("Bearer ${prefs.token!!}",SynReportStandardRemote.Request(idReport,report.idPv,report.idCompany,reportData.map { it.toRequestRemote() },"COMPLETADO",longitude,latitude)).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Log.d("success  ---->> ",body.message.toString())
                                        updateReportPvSync(idReport,"Enviado","Terminado")
                                        Either.Right(true)
                                    }
                                    else{
                                        Log.d("errorsincro --->> ",body.message.toString())
                                        Either.Left(Failure.DefaultError(body.message))}

                                }?: Either.Left(Failure.DefaultError(""))
                            }
                            false -> Either.Left(Failure.ServerError)
                        }
                    } catch (e: Exception) {
                        Either.Left(Failure.DefaultError(e.message!!))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun syncReportAudio(): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val reportData = mutableListOf<ReportStandard>()
                        var image =""
                        val report = reportsDataSource.getReportsPvReady(prefs.idUser ?: "")

                        val audio = reportsDataSource.getAudioReports(prefs.pvId ?: "", prefs.idUser ?: "")
                        //Log.d("syncReportAudio", report.toString())
                        //Log.d ("syncReportAudios ", audio.toString())

                        val questions  = questionsDataSource.getQuestionsByIdReport("").map { it.toView() }
                        for(question in questions) {
                                val answerData= mutableListOf<AnswerStandard>()
                                val answers = questionsDataSource.getReadyAnswers(question.id).map { it.toView() }
                                for(answer in answers)
                                {
                                    answerData.add(AnswerStandard(answer.idAnswer,answer.nameAnswer,"")) //NO SE SABE A QUE SE REFIERE CON INPUT
                                    if(answer.img.isNotEmpty())
                                    {
                                        image = answer.img
                                    }
                                }
                                reportData.add(ReportStandard(question.id,question.questionName,answerData))

                        }
                        Log.d("reportAudio", "$reportData - $image")
                        val response = service.synStandardReports("Bearer ${prefs.token!!}",SynReportStandardRemote.Request("",prefs.pvId?:"",prefs.companyId?:"",reportData.map { it.toRequestRemote() },"","","")).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Log.d("successAudio",body.message.toString())
                                        updateStateReport("60dc7d0c11bb190a40e28e91","Enviado","Terminado")
                                        Either.Right(true)
                                    }
                                    else Either.Left(Failure.DefaultError(body.message))
                                }?: Either.Left(Failure.DefaultError(""))
                            }
                            false -> Either.Left(Failure.ServerError)
                        }
                    } catch (e: Exception) {
                        Either.Left(Failure.DefaultError(e.message!!))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun getStateSku(idPv: String): Either<Failure, String> {
            return try {
                Either.Right(reportsDataSource.getStateSku(prefs.pvId?:"",prefs.companyId?:"",prefs.idUser?:""))
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getTypeSku(): Either<Failure, String> {
            return try {
                Either.Right(reportsDataSource.getSTypeSku(prefs.pvId?:"",prefs.companyId?:"",prefs.idUser?:""))
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }


        override fun updateStateSku(idSku: String, state: String,type:String,latitude: String,longitude: String): Either<Failure, Boolean> {
            return try {
                reportsDataSource.updateStateSku(idSku,state,type,Calendar.getInstance().toInstant().toString(),latitude,longitude)
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun listReports(idCompany: String): Either<Failure, List<Report>> {
            return try {
                Either.Right(reportsDataSource.listReports(idCompany).map { it.toView() })
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getStateReport(idReport: String): Either<Failure, String> {
            return try {
                val count = reportsDataSource.getReportPvCount(prefs.companyId?:"",prefs.pvId?:"",prefs.idUser?:"")
                if(count==0)
                    Either.Right("0")
                else
                    Either.Right(reportsDataSource.getStateReport(idReport,prefs.pvId?:"",prefs.idUser?:""))
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun updateReportPvSync(idReport: String, state: String,type:String): Either<Failure, Boolean> {
            return try {
                reportsDataSource.updateReportPvSync(idReport,prefs.pvId?:"",state,type,prefs.idUser?:"")
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun updateReportPv(idReport: String, state: String,type:String,time:String,latitude: String,longitude: String): Either<Failure, Boolean> {
            return try {
                reportsDataSource.updateReportPv(idReport,prefs.pvId?:"",prefs.idUser?:"",state,type,time,latitude,longitude)
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getCountSku(): Either<Failure, Int> {
            return try {
                Either.Right(reportsDataSource.getCountSku(prefs.pvId?:"",prefs.idUser?:""))
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun syncReportStandardMassive(latitude: String, longitude: String): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val reportMassive = mutableListOf<ReportStandardMassive>()
                        val reportData = mutableListOf<ReportStandard>()
                        val listIdStandard = arrayListOf<String>()
                        val reports = reportsDataSource.getReportsPvReady(prefs.idUser?:"")
                        for (report in reports)
                        {
                            val stateReport = reportsDataSource.getReportsPvState(report.id,report.idPv,prefs.idUser?:"")
                            val questions  = questionsDataSource.getQuestionsByIdReport(report.id).map { it.toView() }
                            for(question in questions)
                            {
                                val answerData= mutableListOf<AnswerStandard>()
                                val answers = questionsDataSource.getAnswersPv(question.id,report.idPv,report.idUser).map { it.toView() }
                                for(answer in answers)
                                {
                                    answerData.add(AnswerStandard(answer.id,answer.answerName,answer.data))
                                }
                                reportData.add(ReportStandard(question.id,question.questionName,answerData))

                            }
                            listIdStandard.add(report.id)
                            reportMassive.add(ReportStandardMassive(report.id,report.idPv,report.idCompany,reportData,"COMPLETADO",stateReport.time,stateReport.longitude,stateReport.latitude,longitude,latitude,Calendar.getInstance().toInstant().toString()))
                        }

                        Log.d("reportStandarMassive", "$reportData")
                        val response = service.synStandardReportsMassive("Bearer ${prefs.token!!}",reportMassive.map { it.toRequestRemote() }).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Log.d("syncStandardMassive",body.message.toString())
                                        for(id in listIdStandard)
                                            updateReportPvSync(id,"Enviado","Terminado")
                                        Either.Right(true)
                                    }
                                    else{
                                        Log.d("errorStandarMassive",body.message.toString())
                                        Either.Left(Failure.DefaultError(body.message))}

                                }?: Either.Left(Failure.DefaultError(""))
                            }
                            false -> Either.Left(Failure.ServerError)
                        }
                    } catch (e: Exception) {
                        Either.Left(Failure.DefaultError(e.message!!))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }

        }

        override fun setSession(value: Boolean): Either<Failure, Boolean> {
            prefs.session = true
            return Either.Right(true)
        }
        override fun getAudioReport(idPv: String, idUser:String): Either<Failure,AudioReport> {
            return try {
                val audioReport = reportsDataSource.getAudioReports(idPv, idUser)
                if(audioReport != null){
                    Either.Right(audioReport.toView())
                }else{
                    Either.Right(AudioReport("","","","","","","",""))
                }
            } catch (e: Exception) {
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun updateAudioReport(
            idPv: String,
            idUser:String,
            selected: String,
            selectedName: String,
            selectedPath:String,
            record: String,
            recordPath: String,
            recordSent: String,
        ): Either<Failure, Boolean> {
            return try{
                reportsDataSource.updateAudioReport(idPv,idUser, selected,selectedName,selectedPath,record,recordPath,recordSent)
                Either.Right(true)
            }catch (e : Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }
    }
}