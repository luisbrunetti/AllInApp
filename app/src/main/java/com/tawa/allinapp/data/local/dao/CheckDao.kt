package com.tawa.allinapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.tawa.allinapp.data.local.models.CheckModel

@Dao
interface CheckDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCheck(checkIn: CheckModel)
}