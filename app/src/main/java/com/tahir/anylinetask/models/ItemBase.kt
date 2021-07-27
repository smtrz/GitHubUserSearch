package com.tahir.anylinetask.models

data class ItemBase (

    val total_count : Int,
    val incomplete_results : Boolean,
    val items : List<Item>
    )