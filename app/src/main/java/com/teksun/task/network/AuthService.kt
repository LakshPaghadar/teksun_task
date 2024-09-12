package com.teksun.task.network

import com.teksun.task.network.response.FundDetails
import com.teksun.task.network.response.SearchFundName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthService {

    @GET("mf/search")
    suspend fun searchApi(@Query("q") search:String):Response<SearchFundName>

    @GET("mf/{code}")
    suspend fun detailsApi(@Path("code") code:String):Response<FundDetails>
}