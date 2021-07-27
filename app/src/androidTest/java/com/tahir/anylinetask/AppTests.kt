package com.tahir.anylinetask

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tahir.anylinetask.models.Item
import com.tahir.anylinetask.viewstate.DataState
import com.tahir.anylinetask.viewstate.SubmitStatus
import com.tahir.anylinetask.vm.MainActivityViewModel
import org.junit.*
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)

class AppTests {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    var users: List<Item> = emptyList()
    lateinit var data_observer: Observer<DataState<List<Item>>>
    lateinit var viewModel: MainActivityViewModel


    @Before
    @Throws(Exception::class)
    fun setUp() {

        //creating instance of viewmodel

        viewModel = MainActivityViewModel()

    }


    @After
    @Throws(java.lang.Exception::class)
    fun tearDown() {
        viewModel.dataState.removeObserver(data_observer)
    }

    // Testing ViewModel
    @Test
    @Throws(InterruptedException::class)
    fun UsersListNotEmpty() {

        viewModel.setStateEvent(SubmitStatus.Search, 1, "tahir")
        val signal = CountDownLatch(1)

        data_observer = Observer<DataState<List<Item>>> { datastate ->
// once the data change has occured , latch should be decremented
            when (datastate) {


                is DataState.Success<List<Item>> -> {
                    users = datastate.data
                    signal.countDown()

                    Assert.assertTrue(users.count() > 0)
                }


            }


        }
        viewModel.dataState.observeForever(data_observer)
        signal.await()
    }
}