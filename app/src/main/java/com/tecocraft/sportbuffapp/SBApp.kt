package com.tecocraft.sportbuffapp


import android.app.Application
import com.tecocraft.sportbuffapp.dagger.component.DaggerNetComponent
import com.tecocraft.sportbuffapp.dagger.component.NetComponent
import com.tecocraft.sportbuffapp.dagger.module.APIModule
import com.tecocraft.sportbuffapp.dagger.module.AppModule
import com.tecocraft.sportbuffapp.dagger.module.NetModule
import com.tecocraft.sportbuffapp.rest.ApiClient

open class SBApp : Application() {

    companion object {
        lateinit var instance: SBApp
    }

    lateinit var instance: SBApp

    lateinit var mNetComponent: NetComponent


    fun getmNetComponent(): NetComponent {
        return mNetComponent
    }

    override fun onCreate() {
        super.onCreate()
        init()
        Companion.instance = this
        instance = this

    }

    //    private fun init() {
    fun init() {

        mNetComponent = DaggerNetComponent.builder()
            .appModule(AppModule(this))
            .netModule(NetModule(ApiClient.BASE_URL_APP))
            .aPIModule(APIModule())
            .build()

        mNetComponent.inject(this)


    }

}
