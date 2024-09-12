package com.teksun.task.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FundDao {

    @Insert
    suspend fun insertFund(fundTable: FundTable)

    @Query("SELECT * FROM FundTable")
    fun getFunds():LiveData<List<FundTable>>
}