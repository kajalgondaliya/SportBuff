package com.tecocraft.buffoverlayview.remote

import com.tecocraft.buffoverlayview.model.Buff

/**
 * Created by Akshay Jariwala
 */

internal sealed class BuffResult {
    data class Success(val buff: Buff) : BuffResult()
    data class Error(val error: Throwable) : BuffResult()
}