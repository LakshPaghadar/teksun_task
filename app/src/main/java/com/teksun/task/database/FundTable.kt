package com.teksun.task.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FundTable")
class FundTable(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    var fund_name:String,
    var fund_code:String,
    var date:String,
    var amount:String,

) {
}