package com.teksun.task.network

import com.teksun.task.network.response.FundDetails
import com.teksun.task.network.response.SearchFundName
import retrofit2.Response

interface AuthRepository {

    suspend fun searchApi(search:String):Response<SearchFundName>
    suspend fun detailsApi(code:String):Response<FundDetails>
}