package com.tahir.anylinetask.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tahir.anylinetask.R
import com.tahir.anylinetask.UIHelper
import com.tahir.anylinetask.adapters.AppartmentsAdapter
import com.tahir.anylinetask.adapters.callData
import com.tahir.anylinetask.helpers.Utils
import com.tahir.anylinetask.interfaces.RVClickCallback
import com.tahir.anylinetask.models.Item
import com.tahir.anylinetask.viewstate.DataState
import com.tahir.anylinetask.viewstate.SubmitStatus
import com.tahir.anylinetask.vm.MainActivityViewModel
import com.tahir.anylinetask.vm.MyViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), RVClickCallback, View.OnClickListener, callData {
    lateinit var mainactVm: MainActivityViewModel
    lateinit var userAdapter: AppartmentsAdapter
    val appartments: MutableList<Item> = mutableListOf()
    private val RECORD_REQUEST_CODE = 101
    var LAUNCH_SCAN_ACTIVITY = 1

    companion object {
        var pageNumber: Int = 1


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    fun init() {
        // initial configrations.

        mainactVm =
            ViewModelProvider(this, MyViewModelFactory())
                .get(MainActivityViewModel::class.java)
        btn_search.setOnClickListener(this)
        scan.setOnClickListener(this)


        setupApptList(appartments)
        subscribeObservers()
    }

    /**
     * Subscribe to DataState Live data
     */
    private fun subscribeObservers() {
        mainactVm.dataState.observe(this, { dataState ->
// observing data state changes

            when (dataState) {


                is DataState.Success<List<Item>> -> {
                    displayProgressBar(false)
                    //appartments
                    appartments.addAll(dataState.data)
                    userAdapter.loadItems(appartments, this)
                    userAdapter.notifyDataSetChanged()


                }
                is DataState.Error -> {
                    displayProgressBar(false)
                    displayError(dataState.exception)
                }
                is DataState.Loading -> {

                    displayProgressBar(true)
                }
            }
        })


    }

    /*
      setupApptList will be used to setup appartment list
       */
    fun setupApptList(appartments: List<Item>) {

        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        rv_users.layoutManager = layoutManager
        // Create a DividerItemDecoration whose orientation is vertical


        val vItemDecoration = DividerItemDecoration(
            this,
            DividerItemDecoration.VERTICAL
        )
        // Set the drawable on it
        vItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)
        // add decoration for list
        rv_users.addItemDecoration(
            vItemDecoration
        )

        rv_users.addOnScrollListener(
            AppartmentsAdapter.OnScrollListener(
                layoutManager,
                this
            )
        )

        // setting up recyclerview and also binding activity with the view-model
        userAdapter = AppartmentsAdapter(this, appartments, this)
        rv_users?.adapter = userAdapter


    }

    /**
     * This method will be used to show the error messages if any
     */
    private fun displayError(message: String?) {

        var msg: String?
        msg = message
        if (message == null) {

            msg = "unknown error"
        }
        UIHelper.showShortToastInCenter(this, msg!!)


    }


    /**
     * Display progressbar
     */
    private fun displayProgressBar(isDisplayed: Boolean) {
        progress_bar.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }


    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()

        } else {

            // do any line work here
            startOCr()

        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            RECORD_REQUEST_CODE
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
// requesting user to grant permission.

                    UIHelper.showAlertDialog(
                        "In order to scan characters, we need to access your camera.",
                        "Permission Request",
                        this
                    ).setPositiveButton(
                        "Enable"
                    ) { dialog, _ ->
                        makeRequest()

                    }.setCancelable(true).show()


                } else {
                    startOCr()


                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)



        when (requestCode) {
            LAUNCH_SCAN_ACTIVITY -> {
                //coming back from the location settings
                val result: String = data?.getStringExtra("result")!!

                et_username.setText(result)

                pageNumber = 1
                appartments.clear()
                mainactVm.setStateEvent(
                    SubmitStatus.Search,
                    pageNumber, et_username.text.toString()
                )

            }
        }
    }

    // all the click events are handled from here.
    override fun onClick(p0: View?) {
        when (p0?.id) {

            R.id.btn_search -> {


                if (et_username.text.toString().length == 0) {
                    UIHelper.showShortToastInCenter(this, "invalid input")
                    return

                }
                // performing search operation.

                pageNumber = 1
                appartments.clear()
                mainactVm.setStateEvent(
                    SubmitStatus.Search,
                    pageNumber, et_username.text.toString()
                )

                Utils.hideSoftKeyBoard(this, p0)
            }

            R.id.scan -> {
                setupPermissions()


            }


        }
    }

    override fun onItemClick(pos: Int) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(appartments.get(pos).html_url))
        startActivity(browserIntent)
    }

    override fun getMoreData() {
        pageNumber++
        mainactVm.setStateEvent(
            SubmitStatus.Search,
            pageNumber, et_username.text.toString()
        )
    }

    fun startOCr() {

        val i = Intent(this, ScannerActivity::class.java)
        startActivityForResult(i, LAUNCH_SCAN_ACTIVITY);


    }
}