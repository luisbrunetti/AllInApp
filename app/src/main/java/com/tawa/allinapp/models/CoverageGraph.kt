package com.tawa.allinapp.models

data class CoverageGraph(
    val user: UserDashboard,
    val coverage: CoverageDashboard,
    val visits: VisitsDashboard,
    val reports: ReportsDashboard,
)

data class UserDashboard (
    val title: String,
    val active: Double,
    val activeReported: Double,
)

data class CoverageDashboard (
    val title: String,
    val infoChain: List<InfoChainDashboard>,
)

data class VisitsDashboard (
    val title: String,
    val total: Double,
    val concluded: Double,
    val pending: Double,
)

data class ReportsDashboard (
    val title: String,
    val totalTasksToDo: Double,
    val totalTasksFinished: Double,
    val totalTasksPending: Double,
)

data class InfoChainDashboard (
    val chainName: String,
    val idChain: String,
    val tasksToDo: Double,
    val tasksFinished: Double,
)
