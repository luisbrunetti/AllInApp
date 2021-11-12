package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.models.*

class GraphRemote {
    data class Response(
        @SerializedName("usuario") val user: UserResponse,
        @SerializedName("cobertura") val coverage: CoverageResponse,
        @SerializedName("visitas") val visits: VisitsResponse,
        @SerializedName("reportes") val reports: ReportsResponse,
    ){
        fun toModel() = CoverageGraph(user.toModel(),coverage.toModel(),visits.toModel(),reports.toModel())
    }

    data class UserResponse (
        @SerializedName("titulo") val title: String,
        @SerializedName("activos") val active: Double,
        @SerializedName("noActivos") val noActive: Double,
    ){
        fun toModel() = UserDashboard(title,active,noActive)
    }

    data class CoverageResponse (
        @SerializedName("titulo") val title: String,
        @SerializedName("infoCadenas") val infoChain: List<InfoChainResponse>,
    ){
        fun toModel() = CoverageDashboard(title,infoChain.map { it.toModel() })
    }

    data class VisitsResponse (
        @SerializedName("titulo") val title: String,
        @SerializedName("total") val total: Double,
        @SerializedName("concluidas") val concluded: Double,
        @SerializedName("pendientes") val pending: Double,
    ){
        fun toModel() = VisitsDashboard(title,total,concluded,pending)
    }

    data class ReportsResponse (
        @SerializedName("titulo") val title: String,
        @SerializedName("totalTareasHacer") val totalTasksToDo: Double,
        @SerializedName("tareasCompletadas") val totalTasksFinished: Double,
        @SerializedName("tareasPendientes") val totalTasksPending: Double,
    ){
        fun toModel() = ReportsDashboard(title,totalTasksToDo,totalTasksFinished,totalTasksPending)
    }

    data class InfoChainResponse (
        @SerializedName("nombre_cadena") val chainName: String,
        @SerializedName("id_cadena") val idChain: String,
        @SerializedName("tareasPorHacer") val tasksToDo: Double,
        @SerializedName("tareasCompletadas") val tasksFinished: Double,
    ){
        fun toModel() = InfoChainDashboard(chainName,idChain,tasksToDo,tasksFinished)
    }
}