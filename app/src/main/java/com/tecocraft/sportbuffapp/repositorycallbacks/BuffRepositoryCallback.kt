package com.tecocraft.sportbuffapp.repositorycallbacks

import com.tecocraft.sportbuffapp.model.buffResponce.BuffResponce


interface BuffRepositoryCallback {
    fun onDataSuccess(model: BuffResponce)
    fun onDataFailure(msg: String)
    fun showError(msg: String)
}