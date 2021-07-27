package com.tahir.anylinetask.db


import android.content.Context
import com.tahir.anylinetask.app.App
import com.tahir.anylinetask.interfaces.EndpointsInterface
import com.tahir.anylinetask.models.Item
import com.tahir.anylinetask.models.ItemBase
import com.tahir.anylinetask.viewstate.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*
import javax.inject.Inject


class DbRepository {

    @Inject
    lateinit var c: Context

    @Inject
    lateinit var retrofit: Retrofit

    init {
        // injecting Dagger component
        App.app.appLevelComponent.inject(this@DbRepository)


    }


    // method for searching user:
    suspend fun searchGithubUser(q: String, page: Int): Flow<DataState<List<Item>>> =

        flow {


            var apiResponse:
                    Response<ItemBase>

            val endpoints = retrofit.create(EndpointsInterface::class.java)

            emit(DataState.Loading)

            try {

                apiResponse = endpoints.searchUser(q, page)//code()


                when (apiResponse.code()) {
                    200 -> {


                        emit(DataState.Success(apiResponse.body()!!.items))
                    }
                    400 -> {
                        emit(DataState.Error("400 - Bad request"))

                    }
                    404 -> {
                        emit(DataState.Error("404 - Not found"))

                    }
                    500 -> {
                        emit(DataState.Error("500 - Internal server error"))

                    }
                    403 -> {
                        emit(DataState.Error("500 - Forbidden"))

                    }
                    401 -> {
                        emit(DataState.Error("401 -  Unauthorized"))

                    }
                    else -> {
                        emit(DataState.Error("Error occured with response code " + apiResponse))
                    }

                }


            } catch (e: Exception) {
                emit(DataState.Error(e.message.toString()))
            }

        }


}

