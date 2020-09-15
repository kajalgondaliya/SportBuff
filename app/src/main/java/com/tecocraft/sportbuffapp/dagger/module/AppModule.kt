package com.tecocraft.sportbuffapp.dagger.module

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class AppModule(application:Application) {
    private var mApplication: Application = application

    @Provides
    @Singleton
    internal open fun provideApplication():Application {
        return mApplication
    }
}