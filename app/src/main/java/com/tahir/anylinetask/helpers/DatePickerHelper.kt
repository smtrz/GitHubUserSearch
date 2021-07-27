package com.tahir.anylinetask.helpers

import android.app.DatePickerDialog
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.tahir.anylinetask.R
import java.util.*

class DatePickerHelper(context: Context, isSpinnerType: Boolean = false) : Parcelable {
    private var dialog: DatePickerDialog
    private var callback: Callback? = null
    private val listener =
        DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
            callback?.onDateSelected(dayOfMonth, monthOfYear, year)
        }

    constructor(parcel: Parcel) : this(
        TODO("context"),
        TODO("isSpinnerType")
    ) {

    }

    init {
        val style = if (isSpinnerType) R.style.SpinnerDatePickerDialog else 0
        val cal = Calendar.getInstance()
        dialog = DatePickerDialog(context, style, listener,
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
    }
    fun showDialog(dayofMonth: Int, month: Int, year: Int, callback: Callback?) {
        this.callback = callback
        dialog.datePicker.init(year, month, dayofMonth, null)
        dialog.show()
    }
    fun setMinDate(minDate: Long) {
        dialog.datePicker.minDate = minDate
    }
    fun setMaxDate(maxDate: Long) {
        dialog.datePicker.maxDate = maxDate
    }
    interface Callback {
        fun onDateSelected(dayofMonth: Int, month: Int, year: Int)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DatePickerHelper> {
        override fun createFromParcel(parcel: Parcel): DatePickerHelper {
            return DatePickerHelper(parcel)
        }

        override fun newArray(size: Int): Array<DatePickerHelper?> {
            return arrayOfNulls(size)
        }
    }
}