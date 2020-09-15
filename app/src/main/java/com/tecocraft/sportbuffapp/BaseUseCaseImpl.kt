package com.tecocraft.sportbuffapp

import android.content.Context
import com.google.gson.Gson

import com.tecocraft.sportbuffapp.common.CommonUtils
import com.tecocraft.sportbuffapp.common.SharedPref
import com.tecocraft.sportbuffapp.rest.SBAppInterface
import javax.inject.Inject

class BaseUseCaseImpl(val mBaseView: BaseView) : BaseUseCase {

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var commonUtils: CommonUtils

    @Inject
    lateinit var sbAppInterface: SBAppInterface

    @Inject
    lateinit var sharePref: SharedPref

    init {
        val injector = SBApp.instance.getmNetComponent()
        injector.inject(this@BaseUseCaseImpl)
    }


    override fun getContext(): Context {
        return mBaseView.getContext()
    }

    override fun showLoader() {
        mBaseView.showLoader()
    }

    override fun hideLoader() {
        mBaseView.hideLoader()
    }

    override fun onBackPress() {
    }

    override fun attachView(view: BaseView) {
    }


}
