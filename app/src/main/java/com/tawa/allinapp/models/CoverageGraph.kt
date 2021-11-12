package com.tawa.allinapp.models

import android.os.Parcel
import android.os.Parcelable

data class CoverageGraph(
    val user: UserDashboard,
    val coverage: CoverageDashboard,
    val visits: VisitsDashboard,
    val reports: ReportsDashboard,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(UserDashboard::class.java.classLoader)!!,
        parcel.readParcelable(CoverageDashboard::class.java.classLoader)!!,
        parcel.readParcelable(VisitsDashboard::class.java.classLoader)!!,
        parcel.readParcelable(ReportsDashboard::class.java.classLoader)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(user, flags)
        parcel.writeParcelable(coverage, flags)
        parcel.writeParcelable(visits, flags)
        parcel.writeParcelable(reports, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CoverageGraph> {
        override fun createFromParcel(parcel: Parcel): CoverageGraph {
            return CoverageGraph(parcel)
        }

        override fun newArray(size: Int): Array<CoverageGraph?> {
            return arrayOfNulls(size)
        }
    }
}

data class UserDashboard (
    val title: String,
    val active: Double,
    val noActive: Double,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeDouble(active)
        parcel.writeDouble(noActive)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserDashboard> {
        override fun createFromParcel(parcel: Parcel): UserDashboard {
            return UserDashboard(parcel)
        }

        override fun newArray(size: Int): Array<UserDashboard?> {
            return arrayOfNulls(size)
        }
    }
}

data class CoverageDashboard (
    val title: String,
    val infoChain: List<InfoChainDashboard>,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.createTypedArrayList(InfoChainDashboard)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeTypedList(infoChain)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CoverageDashboard> {
        override fun createFromParcel(parcel: Parcel): CoverageDashboard {
            return CoverageDashboard(parcel)
        }

        override fun newArray(size: Int): Array<CoverageDashboard?> {
            return arrayOfNulls(size)
        }
    }
}

data class VisitsDashboard (
    val title: String,
    val total: Double,
    val concluded: Double,
    val pending: Double,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeDouble(total)
        parcel.writeDouble(concluded)
        parcel.writeDouble(pending)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VisitsDashboard> {
        override fun createFromParcel(parcel: Parcel): VisitsDashboard {
            return VisitsDashboard(parcel)
        }

        override fun newArray(size: Int): Array<VisitsDashboard?> {
            return arrayOfNulls(size)
        }
    }
}

data class ReportsDashboard (
    val title: String,
    val total: Double,
    val concluded: Double,
    val pending: Double,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeDouble(total)
        parcel.writeDouble(concluded)
        parcel.writeDouble(pending)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReportsDashboard> {
        override fun createFromParcel(parcel: Parcel): ReportsDashboard {
            return ReportsDashboard(parcel)
        }

        override fun newArray(size: Int): Array<ReportsDashboard?> {
            return arrayOfNulls(size)
        }
    }
}

data class InfoChainDashboard (
    val chainName: String,
    val idChain: String,
    val tasksToDo: Double,
    val tasksFinished: Double,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(chainName)
        parcel.writeString(idChain)
        parcel.writeDouble(tasksToDo)
        parcel.writeDouble(tasksFinished)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InfoChainDashboard> {
        override fun createFromParcel(parcel: Parcel): InfoChainDashboard {
            return InfoChainDashboard(parcel)
        }

        override fun newArray(size: Int): Array<InfoChainDashboard?> {
            return arrayOfNulls(size)
        }
    }
}
