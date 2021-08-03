package com.tawa.allinapp.data.remote.service



import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.api.ReportsApi
import com.tawa.allinapp.data.remote.entities.ReportsPhotoRemote
import com.tawa.allinapp.data.remote.entities.ReportsSkuRemote
import com.tawa.allinapp.data.remote.entities.SynReportStandardRemote
import com.tawa.allinapp.data.remote.entities.UpdateStatusRemote
import com.tawa.allinapp.models.PhotoReport
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ReportsService
@Inject constructor(retrofit: Retrofit) : ReportsApi {
    private val reportsApi by lazy { retrofit.create(ReportsApi::class.java) }

    override fun getReports(company:String) = reportsApi.getReports(company)
    override fun getReportStatus(token:String) = reportsApi.getReportStatus(token)
    override fun updateStatus(token: String, request: UpdateStatusRemote.Request) = reportsApi.updateStatus(token,request)
    override fun getReportsSku(token: String) = reportsApi.getReportsSku(token)
    override fun syncSku(token: String, request: ReportsSkuRemote.Request) = reportsApi.syncSku(token,request)
    override fun setPhotoReports(
        before1:String,before2:String,before3:String,before4:String,before5:String,
        after1:String,after2:String,after3:String,after4:String,after5:String,comment:String
    ) = reportsApi.setPhotoReports(
        before1,before2,before3,before4,before5,
        after1,after2,after3,after4,after5,comment
    )

    override fun syncPhotoReports(token:String,request:List<ReportsPhotoRemote.Request>) = reportsApi.syncPhotoReports(token,request)
    override fun synStandardReports(token: String, request: SynReportStandardRemote.Request) =  reportsApi.synStandardReports(token,request)

}