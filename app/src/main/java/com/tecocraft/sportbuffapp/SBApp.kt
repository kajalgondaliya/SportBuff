package com.tecocraft.sportbuffapp


import android.app.Application
import com.tecocraft.buffoverlayview.BuffOverlayView


open class SBApp : Application() {

    override fun onCreate() {
        super.onCreate()
        BuffOverlayView.initialize("")
    }


}
