package com.tecocraft.sportbuffapp

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.tecocraft.sportbuffapp.common.CommonUtils
import com.tecocraft.sportbuffapp.common.SharedPref
import com.tecocraft.sportbuffapp.rest.SBAppInterface

import retrofit2.Retrofit
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), BaseView {

    @Inject
    lateinit var sbAppInterface: SBAppInterface

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var commonUtils: CommonUtils

    @Inject
    lateinit var sharedPref: SharedPref



    @Inject
    lateinit var retrofit: Retrofit


    // main method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as SBApp).getmNetComponent().inject(this)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, 0)
    }

    override fun getContext(): Context {
        return this
    }

    override fun showLoader() {
    }

    override fun hideLoader() {
    }

    override fun showError(msg: String) {
    }

}