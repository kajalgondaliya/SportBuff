package com.tecocraft.buffoverlayview

import androidx.annotation.NonNull

/**
 * Created by Akshay Jariwala
 */
class BuffOverlayView {

    internal var authKey: String? = null

    companion object {

        private var mInstance: BuffOverlayView? = null

        /**
         * Initializes SDK
         *
         * @param authKey The authentication key that is require by the SDK from the hosting application
         */
        fun initialize(@NonNull authKey: String) {
            mInstance = BuffOverlayView()
            mInstance?.authKey = authKey
        }

        /**
         * Returns the instance of the class
         * @return mInstance
         */
        internal fun getInstance(): BuffOverlayView? = mInstance
    }

}