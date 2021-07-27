package com.tahir.anylinetask.modules

import android.content.Context
import com.tahir.anylinetask.db.DbRepository

import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DbRepoModule {


    @Provides
    @Singleton
    fun getRepository(): DbRepository {

        return DbRepository()

    }



}