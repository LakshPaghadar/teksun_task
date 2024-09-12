package com.teksun.task.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities =  [FundTable::class], version = 1)
abstract class FundDatabase:RoomDatabase() {

    abstract fun fundDao():FundDao

    companion object{
        private var database:FundDatabase?=null

        fun getDatabaseFund(context: Context): FundDatabase {
            if(database!=null){
                return database!!
            } else {
                database=Room.databaseBuilder(context.applicationContext!!,FundDatabase::class.java,"fundDB").build()
                return database!!
            }

        }
    }
}