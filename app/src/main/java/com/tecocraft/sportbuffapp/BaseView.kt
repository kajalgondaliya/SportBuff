package com.tecocraft.sportbuffapp

import android.content.Context

interface BaseView {

    fun getContext(): Context

    fun showLoader()

    fun hideLoader()

    fun showError(msg: String)

}