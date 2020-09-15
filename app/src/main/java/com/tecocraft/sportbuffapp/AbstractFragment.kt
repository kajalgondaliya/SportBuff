package com.tecocraft.sportbuffapp

import android.os.Bundle
import androidx.fragment.app.Fragment

import com.google.gson.Gson

import com.tecocraft.sportbuffapp.common.CommonUtils
import com.tecocraft.sportbuffapp.common.SharedPref
import com.tecocraft.sportbuffapp.rest.SBAppInterface

import retrofit2.Retrofit
import javax.inject.Inject

abstract class AbstractFragment : Fragment() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as SBApp).getmNetComponent().inject(this)
    }
}