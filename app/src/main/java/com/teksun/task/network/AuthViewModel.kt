package com.teksun.task.network

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teksun.task.network.response.FundDetails
import com.teksun.task.network.response.SearchFundName
import com.teksun.task.network.response.SearchFundNameItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor (val repository: AuthRepository):ViewModel() {


    var searchApiResponse= MutableLiveData<Response<SearchFundName>>()
    var detailsApiResponse= MutableLiveData<Response<FundDetails>>()

    fun searchApi(search:String,callbackError:(String)->Unit){
        try {
            viewModelScope.launch {
                var response=repository.searchApi(search)
                searchApiResponse.postValue(response)
            }
        } catch (e: SocketTimeoutException){
            callbackError.invoke("Timeout")
        }
        catch (e: UnknownHostException){
            callbackError.invoke("Please check your internet connection")
        }catch (e:Exception){
            if (e.message==null){
                callbackError.invoke("Something went wrong")
            } else {
                callbackError.invoke(e.message.toString())
            }
        }
    }

    fun detailsApi(code:String,callbackError:(String)->Unit){
        try {
            viewModelScope.launch {
                var response=repository.detailsApi(code)
                detailsApiResponse.postValue(response)
            }
        } catch (e: SocketTimeoutException){
            callbackError.invoke("Timeout")
        } catch (e: UnknownHostException){
            callbackError.invoke("Please check your internet connection")
        }catch (e:Exception){
            if (e.message==null){
                callbackError.invoke("Something went wrong")
            } else {
                callbackError.invoke(e.message.toString())
            }
        }
    }
}