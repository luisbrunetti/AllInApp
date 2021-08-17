package com.tawa.allinapp.data.repository

import android.util.Log
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.functional.NetworkHandler
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.data.local.datasource.QuestionsDataSource
import com.tawa.allinapp.data.local.datasource.ReportsDataSource
import com.tawa.allinapp.data.local.models.*
import com.tawa.allinapp.data.remote.MovieDetailEntity
import com.tawa.allinapp.data.remote.entities.ReportsSkuRemote
import com.tawa.allinapp.data.remote.entities.SynReportStandardRemote
import com.tawa.allinapp.data.remote.entities.UpdateStatusRemote
import com.tawa.allinapp.data.remote.service.ReportsService
import com.tawa.allinapp.models.AudioReport
import com.tawa.allinapp.models.PhotoReport
import com.tawa.allinapp.models.Report
import com.tawa.allinapp.models.ReportStatus
import com.tawa.allinapp.features.init.usecase.GetIdCompany
import com.tawa.allinapp.models.*
import retrofit2.Call
import javax.inject.Inject
import kotlin.math.log

interface ReportsRepository {
    fun setReports(company: String): Either<Failure, Boolean>
    fun listReports(idCompany: String): Either<Failure, List<Report>>
    fun saveLocalPhotoReport(report:PhotoReport,state:String): Either<Failure, Boolean>
    fun getReports(): Either<Failure,List<Report>>
    fun getSkuDetail(idSku:String): Either<Failure,List<SkuDetail>>
    fun getSku(): Either<Failure,List<Sku>>
    fun getStateSku(idPv:String): Either<Failure,String>
    fun syncPhotoReports(): Either<Failure,Boolean>
    fun savePhotoReport(): Either<Failure, Boolean>
    fun getReportStatus(): Either<Failure, List<ReportStatus>>
    fun updateStatus(latitude:String,longitude:String,battery:String): Either<Failure, Boolean>
    fun saveLocalAudioReport(report: AudioReport): Either<Failure, Boolean>
    fun getReportsSku(company: String):Either<Failure, Boolean>
    fun insertSkuObservation(skuObservation: SkuObservation):Either<Failure, Boolean>
    fun getSkuObservation(idSkuDetail: String):Either<Failure, List<SkuObservation>>
    fun syncSku():Either<Failure, Boolean>
    fun updateSkuDetail(idSkuDetail: String,stock:Boolean,exhibition:Boolean,price:Float):Either<Failure, Boolean>
    fun updateStateReport(idReport:String,state:String,type:String):Either<Failure, Boolean>
    fun updateStateSku(idSku:String,state:String,type:String):Either<Failure, Boolean>
    fun getUserType():Either<Failure, String>
    fun syncReportStandard():Either<Failure, Boolean>
    fun syncReportAudio():Either<Failure, Boolean>
    fun getLocalPhotoReport(): Either<Failure, PhotoReport>
    fun getStateReport(idReport: String): Either<Failure,String>
    fun getStatePhotoReport(): Either<Failure, String>
    fun getCountSku(): Either<Failure, Int>
    fun updateReportPv(idReport: String,state: String,type: String):Either<Failure, Boolean>

    class Network
    @Inject constructor(private val networkHandler: NetworkHandler,
                        private val reportsDataSource: ReportsDataSource,
                        private  val questionsDataSource: QuestionsDataSource,
                        private val prefs: Prefs,
                        private val service: ReportsService,
    ): ReportsRepository{

        override fun setReports(company:String): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getReports("Bearer ${prefs.token!!}",company).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        body.data.map {

                                            for(type in it.reports)
                                            {
                                                Log.d("type",type.reportName.toString())
                                                reportsDataSource.insertReports(ReportModel(type.id?:"",type.reportName?:"",it.idCompany.id?:"",it.idCompany.nameCompany?:"",it.idUser?:"",it.idUserMod?:"",it.feMod?:"",it.feCreate?:"","No iniciado","0",""))
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

        override fun syncPhotoReports(): Either<Failure,Boolean>{
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val request = reportsDataSource.getAllPhotoReports().map { it.toRemote() }
                        val response = service.syncPhotoReports("Bearer ${prefs.token!!}", request).execute()
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

        override fun getStatePhotoReport(): Either<Failure, String> {
            return try {
                Either.Right(reportsDataSource.getStatePhoto(prefs.pvId!!)?:"No iniciado")
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun saveLocalPhotoReport(report:PhotoReport,state:String): Either<Failure, Boolean> {
            if (prefs.pvId!!.isEmpty())
                return Either.Left(Failure.DefaultError("Debe seleccionar hacer Checkin en un Punto de Venta"))
            else
                return try {
                    val before = report.before.size
                    val after = report.after.size
                    reportsDataSource.insertPhotoReport(
                        PhotoReportModel(
                            prefs.companyId?:"",
                            prefs.pvId?:"",
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
                            state
                        )
                    )
                    Either.Right(true)
                }catch (e:Exception){
                    Either.Left(Failure.DefaultError(e.message!!))
                }
        }

        override fun getLocalPhotoReport(): Either<Failure, PhotoReport> {
            return try {
                val response = reportsDataSource.getPhotoReports(prefs.pvId?:"",prefs.companyId?:"")
                if(response.isEmpty())
                    Either.Right( PhotoReport(emptyList(), emptyList(),"","") )
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
                        val reports = reportsDataSource.getPhotoReports(prefs.pvId?:"",prefs.companyId?:"")
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
                                        reportsDataSource.insertSku(SkuModel(it.id,it.idReportPdv.idPuntoVenta.id,it.idCompany.id,"No iniciado","0"))
                                        for(products in it.idReportPdv.lineas)
                                        {
                                            products.idProducto.nombreProducto?.let { it1 ->
                                                products.idProducto.idSubsegmentoProd?.idSegmentoProd?.idSubcategoriaProd?.nombreSubcategoria?.let { it2 ->
                                                    products.idProducto.idSubsegmentoProd?.idSegmentoProd?.idSubcategoriaProd?.idCategoriaProd?.nombreCategoria?.let { it3 ->
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

                if(idPv.isEmpty())
                    Either.Right(reportsDataSource.getReports(prefs.companyId?:"").map { it.toView() })
                else{
                    val count = reportsDataSource.getReportPvCount(prefs.companyId?:"",prefs.pvId?:"")
                    if(count==0)
                    {
                        val reports = reportsDataSource.getReports(prefs.companyId?:"").map { it.toView() }
                        for(report in reports)
                        {
                            reportsDataSource.insertReportPv(ReportPvModel(0,prefs.pvId?:"",report.id,"No iniciado","0"))
                        }
                    }
                    Either.Right(reportsDataSource.getReportsPv(prefs.companyId?:"",prefs.pvId?:"").map { it.toView() })
                }

            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun saveLocalAudioReport(report: AudioReport): Either<Failure, Boolean> {
            return try {
                reportsDataSource.insertAudioReport(report.toModel())
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
                Either.Right(reportsDataSource.getSku("60fb181d8b978fb259e4acb8").map { it.toView() })
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun syncSku(): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        var id = ""
                        var idPv = ""
                        var idCompany = ""
                        val linesAnswer = mutableListOf<Lines>()
                        val skus = reportsDataSource.getSku("60fb181d8b978fb259e4acb8").map { it.toView() }
                        for (sku in skus)
                        {
                            id = sku.id
                            idPv = sku.idPv
                            idCompany = sku.idCompany
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
                        val response = service.syncSku("Bearer ${prefs.token!!}",ReportsSkuRemote.Request(id,idPv,idCompany,linesAnswer.map { it.toRequest() })).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Log.d("message",body.message.toString())
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

        override fun syncReportStandard(): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {

                        val reportData = mutableListOf<ReportStandard>()
                        var image =""
                        val questions  = questionsDataSource.getQuestionsByIdReport("60fb1c398b978f3c44e4ad11").map { it.toView() }
                        for(question in questions)
                        {
                           if(question.questionName!="SI TIENE EXHIBIDOR COLOQUE FOTO")
                           {
                               val answerData= mutableListOf<AnswerStandard>()
                               val answers = questionsDataSource.getReadyAnswers(question.id).map { it.toView() }
                               for(answer in answers)
                               {
                                   answerData.add(AnswerStandard(answer.idAnswer,"",answer.nameAnswer))
                                   if(answer.img.isNotEmpty())
                                   {
                                       image = answer.img
                                   }
                               }
                               reportData.add(ReportStandard(question.id,question.questionName,answerData))
                           }
                        }
                        Log.d("report", "$reportData - $image")
                        val response = service.synStandardReports("Bearer ${prefs.token!!}",SynReportStandardRemote.Request("60fb1c398b978f3c44e4ad11",prefs.pvId?:"",prefs.companyId?:"",reportData.map { it.toRequestRemote() },image)).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Log.d("success",body.message.toString())
                                        updateStateReport("60dc7d0c11bb190a40e28e87","Enviado","Terminado")
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

        override fun syncReportAudio(): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {

                        val reportData = mutableListOf<ReportStandard>()
                        var image =""
                        val questions  = questionsDataSource.getQuestionsByIdReport("61080334ad6bca97e82d94a9").map { it.toView() }
                        for(question in questions)
                        {
                                val answerData= mutableListOf<AnswerStandard>()
                                val answers = questionsDataSource.getReadyAnswers(question.id).map { it.toView() }
                                for(answer in answers)
                                {
                                    answerData.add(AnswerStandard(answer.idAnswer,"INGRESE EL AUDIO",answer.nameAnswer))
                                    if(answer.img.isNotEmpty())
                                    {
                                        image = answer.img
                                    }
                                }
                                reportData.add(ReportStandard(question.id,question.questionName,answerData))

                        }
                        Log.d("reportAudio", "$reportData - $image")
                        val response = service.synStandardReports("Bearer ${prefs.token!!}",SynReportStandardRemote.Request("61080334ad6bca97e82d94a9",prefs.pvId?:"",prefs.companyId?:"",reportData.map { it.toRequestRemote() },image)).execute()
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
                Either.Right(reportsDataSource.getStateSku(prefs.pvId?:""))
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun updateStateSku(idSku: String, state: String,type:String): Either<Failure, Boolean> {
            return try {
                reportsDataSource.updateStateSku(idSku,state,type)
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
                val count = reportsDataSource.getReportPvCount(prefs.companyId?:"",prefs.pvId?:"")
                if(count==0)
                    Either.Right("0")
                else
                    Either.Right(reportsDataSource.getStateReport(idReport,prefs.pvId?:""))
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun updateReportPv(idReport: String, state: String,type:String): Either<Failure, Boolean> {
            return try {
                reportsDataSource.updateReportPv(idReport,prefs.pvId?:"",state,type)
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getCountSku(): Either<Failure, Int> {
            return try {
                Either.Right(reportsDataSource.getCountSku(prefs.pvId?:""))
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

    }
}