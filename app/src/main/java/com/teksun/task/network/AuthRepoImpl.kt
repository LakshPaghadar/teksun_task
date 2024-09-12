package com.teksun.task.network

import com.teksun.task.network.response.FundDetails
import com.teksun.task.network.response.SearchFundName
import retrofit2.Response
import javax.inject.Inject

class AuthRepoImpl @Inject constructor (var authService: AuthService):AuthRepository {
    override suspend fun searchApi(search:String): Response<SearchFundName> {
        return authService.searchApi(search)
    }

    override suspend fun detailsApi(code:String): Response<FundDetails> {
        return authService.detailsApi(code)
    }
}