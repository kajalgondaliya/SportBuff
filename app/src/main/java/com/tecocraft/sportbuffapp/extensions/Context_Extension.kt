package com.tecocraft.sportbuffapp.extensions

import android.content.Context
import android.widget.Toast

/**
 * Created by Akshay Jariwala
 */

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}