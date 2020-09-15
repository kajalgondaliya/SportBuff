package com.tecocraft.sportbuffapp.dagger.component

import com.tecocraft.sportbuffapp.AbstractFragment
import com.tecocraft.sportbuffapp.BaseActivity
import com.tecocraft.sportbuffapp.BaseUseCaseImpl
import com.tecocraft.sportbuffapp.SBApp
import com.tecocraft.sportbuffapp.dagger.module.APIModule
import com.tecocraft.sportbuffapp.dagger.module.AppModule
import com.tecocraft.sportbuffapp.dagger.module.NetModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetModule::class, APIModule::class, AppModule::class])
interface NetComponent {

    fun inject(baseActivity: BaseActivity)
    fun inject(abstractFragment: AbstractFragment)
    fun inject(sbapp: SBApp)
    fun inject(baseUseCaseImpl: BaseUseCaseImpl)
}

