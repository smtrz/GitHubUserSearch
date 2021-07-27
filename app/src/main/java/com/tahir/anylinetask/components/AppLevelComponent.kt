package com.tahir.anylinetask.components

import com.tahir.anylinetask.db.DbRepository
import com.tahir.anylinetask.modules.ContextModule
import com.tahir.anylinetask.modules.DateModule
import com.tahir.anylinetask.modules.DbRepoModule
import com.tahir.anylinetask.modules.NetModule
import com.tahir.anylinetask.vm.MainActivityViewModel
import dagger.Component
import javax.inject.Singleton


@Component(modules = [ContextModule::class, DbRepoModule::class, DateModule::class, NetModule::class])
@Singleton
interface AppLevelComponent {
    fun inject(dr: DbRepository)
    fun inject(mv: MainActivityViewModel)

}
