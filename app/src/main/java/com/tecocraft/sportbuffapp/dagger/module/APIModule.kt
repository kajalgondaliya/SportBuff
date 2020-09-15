package com.tecocraft.sportbuffapp.dagger.module


import com.tecocraft.sportbuffapp.rest.SBAppInterface

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton


@Module(includes = [NetModule::class])
open class APIModule {
    @Provides
    @Singleton
    open fun provideSbAppInterface(retrofit: Retrofit): SBAppInterface {
        return retrofit.create(SBAppInterface::class.java)
    }

}