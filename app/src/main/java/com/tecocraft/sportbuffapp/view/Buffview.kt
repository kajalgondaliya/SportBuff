package com.tecocraft.sportbuffapp.view

import com.tecocraft.sportbuffapp.repositorycallbacks.BuffRepositoryCallback


interface Buffview : BuffRepositoryCallback {
    fun showLoader()
    fun hideLoader()
}
