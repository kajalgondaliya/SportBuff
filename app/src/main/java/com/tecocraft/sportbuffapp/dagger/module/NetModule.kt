package com.tecocraft.sportbuffapp.dagger.module

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tecocraft.sportbuffapp.common.CommonUtils
import com.tecocraft.sportbuffapp.common.SharedPref
import com.tecocraft.sportbuffapp.dagger.module.AppModule
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module(includes = arrayOf(AppModule::class))
open class NetModule(private var mBaseUrl: String) {


    @Provides
    @Singleton
    internal open fun provideSharedPreferences(application:Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }
    @Provides
    @Singleton
    internal open fun provideOkHttpCache(application:Application):Cache {

        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        return Cache(application.cacheDir, cacheSize.toLong())
    }
    @Provides
    @Singleton
    internal open fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }
    @Provides
    @Singleton
    open fun provideOkHttpClient(cache: Cache): OkHttpClient {
        val okClient = OkHttpClient.Builder()
        okClient.connectTimeout(30000, TimeUnit.MILLISECONDS)
        okClient.writeTimeout(30000, TimeUnit.MILLISECONDS)
        okClient.readTimeout(30000, TimeUnit.MILLISECONDS)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        okClient.interceptors().add(interceptor)

        okClient.interceptors().add(Interceptor { chain ->
            val response = chain.proceed(chain.request())
            response.newBuilder()
                    .header("Cache-Control", "only-if-cached")
                    .build()
            response
        })
        return okClient.build()
    }

    @Provides
    @Singleton
    open fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build()
    }


    @Provides
    @Singleton
    internal open fun provideCommonUtils(application: Application): CommonUtils {
        return CommonUtils(application)
    }

    @Provides
    @Singleton
    internal open fun providePref(application:Application): SharedPref {
        return SharedPref(application)
    }

}