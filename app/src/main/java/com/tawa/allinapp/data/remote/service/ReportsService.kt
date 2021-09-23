package com.tawa.allinapp.data.remote.service



import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.api.ReportsApi
import com.tawa.allinapp.data.remote.entities.*
import com.tawa.allinapp.models.PhotoReport
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ReportsService
@Inject constructor(retrofit: Retrofit) : ReportsApi {
    private val reportsApi by lazy { retrofit.create(ReportsApi::class.java) }

    override fun getReports(token:String,company:String) = reportsApi.getReports(token,company)
    override fun getReportStatus(token:String) = reportsApi.getReportStatus(token)
    override fun updateStatus(token: String, request: UpdateStatusRemote.Request) = reportsApi.updateStatus(token,request)
    override fun getReportsSku(token: String,company: String) = reportsApi.getReportsSku(token,company)
    override fun syncSku(token: String, request: ReportsSkuRemote.Request) = reportsApi.syncSku(token,request)
    override fun syncSkuMassive(token: String, request: List<ReportsSkuRemote.RequestMassive>) = reportsApi.syncSkuMassive(token,request)
    override fun setPhotoReports(
        before1:String,before2:String,before3:String,before4:String,before5:String,
        after1:String,after2:String,after3:String,after4:String,after5:String,comment:String
    ) = reportsApi.setPhotoReports(
        before1,before2,before3,before4,before5,
        after1,after2,after3,after4,after5,comment
    )

    override fun syncPhotoReports(token:String,request:List<ReportsPhotoRemote.Request>) = reportsApi.syncPhotoReports(token,request)
    override fun syncOnePhotoReport(token:String,request:ReportsPhotoRemote.RequestOne) = reportsApi.syncOnePhotoReport(token,request)
    override fun synStandardReports(token: String, request: SynReportStandardRemote.Request) =  reportsApi.synStandardReports(token,request)
    override fun synStandardReportsMassive(token: String, request: List<SynReportStandardMassiveRemote.Request>) =  reportsApi.synStandardReportsMassive(token,request)

}