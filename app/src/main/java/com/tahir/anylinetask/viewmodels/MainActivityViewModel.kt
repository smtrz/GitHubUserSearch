package com.tahir.anylinetask.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tahir.anylinetask.app.App
import com.tahir.anylinetask.db.DbRepository
import com.tahir.anylinetask.models.Item
import com.tahir.anylinetask.viewstate.DataState
import com.tahir.anylinetask.viewstate.SubmitStatus
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel : ViewModel() {

    @Inject
    lateinit var dbrepo: DbRepository
    private val _dataState: MutableLiveData<DataState<List<Item>>> = MutableLiveData()
    val dataState: MutableLiveData<DataState<List<Item>>>
        get() = _dataState

    init {
        //dagger initialization
        App.app.appLevelComponent.inject(this)

    }

    /**
     * Method for setting up state
     * @param mainStateEvent - instance of sealed  class - MainActSM.kt file.
     */


    fun setStateEvent(mainStateEvent: SubmitStatus, page: Int, q: String) {

        viewModelScope.launch {
            when (mainStateEvent) {

                is SubmitStatus.Search -> {
                    //appartment search.
                    dbrepo.searchGithubUser(q, page)
                        .onEach { dataState ->
                            _dataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
                SubmitStatus.getData -> {
                    // do nothing for now
                }

                SubmitStatus.None -> {
                    // For now nothing...
                }


            }
        }
    }
}