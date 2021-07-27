package com.tahir.anylinetask.modules

import com.tahir.anylinetask.activities.MainActivity

import com.tahir.anylinetask.helpers.DatePickerHelper
import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Singleton

@Module
class DateModule {

    @Provides
    @Singleton
    fun getDate(): Date {

        return Date()
    }


    @Provides
    @Singleton
    fun getdatePickerHelper(activity: MainActivity): DatePickerHelper {

        return DatePickerHelper(activity)
    }


}

