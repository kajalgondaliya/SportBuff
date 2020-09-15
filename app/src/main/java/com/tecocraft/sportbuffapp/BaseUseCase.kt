package com.tecocraft.sportbuffapp

import android.content.Context

interface BaseUseCase {

    fun getContext(): Context

    fun showLoader()

    fun hideLoader()

    fun onBackPress()

    fun attachView(view: BaseView)

}