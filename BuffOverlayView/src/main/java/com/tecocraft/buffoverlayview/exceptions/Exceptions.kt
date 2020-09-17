package com.tecocraft.buffoverlayview.exceptions

/**
 * Created by Akshay Jariwala
 */

/**
 * Thrown when Buffup is not initialized
 */
class MissingInitializationException(throwable: Throwable): Exception(throwable)

/**
 * Thrown when parent view is not supported
 */
class UnsupportedActivityViewGroupException(throwable: Throwable): Exception(throwable)