package com.tahir.anylinetask.interfaces


import com.tahir.anylinetask.models.ItemBase
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EndpointsInterface {


    @GET("search/users")
    suspend fun searchUser(@Query("q") q: String, @Query("page") page: Int): Response<ItemBase>


}



