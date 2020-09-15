package com.tecocraft.sportbuffapp.common


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.view.Window
import android.view.WindowManager

class CommonUtils(internal var mContext: Context) {


    //check internet connection
    /* getting systems Service connectivity manager */// connected to the internet
    // connected to wifi
    // connected to the mobile provider's data plan
    // not connected to the internet
    val isNetworkAvailable: Boolean
        @SuppressLint("MissingPermission")
        get() {
            val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            if (activeNetwork != null) {
                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                    return true
                } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                    return true
                }
            } else {
                return false
            }
            return false
        }


    /*fun createCustomLoader(mContext: Context, isCancelable: Boolean): Dialog {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(isCancelable)
        dialog.setContentView(R.layout.loader_progress_dialog)


        //Grab the window of the dialog, and change the width
        val lp = WindowManager.LayoutParams()
        val window = dialog.window
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = lp
        return dialog
    }*/

}

