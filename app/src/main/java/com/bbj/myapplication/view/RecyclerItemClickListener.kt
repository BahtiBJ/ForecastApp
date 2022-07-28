package com.bbj.myapplication.view

import android.view.View

interface RecyclerItemClickListener {
    fun onClick(value : String, position: Int)
}