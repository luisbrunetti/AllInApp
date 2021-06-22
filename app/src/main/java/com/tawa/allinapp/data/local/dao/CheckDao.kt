package com.tawa.allinapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.tawa.allinapp.data.local.models.CheckInModel

@Dao
interface CheckDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCheckIn(checkIn: CheckInModel)
}