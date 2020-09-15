package com.tecocraft.sportbuffapp.rest


import com.tecocraft.sportbuffapp.model.buffResponce.BuffResponce
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.GET

interface SBAppInterface {

    //Get Buff
    @GET("buffs/{id}")
    fun getBuff(@Path("id") buffId: Int): Call<BuffResponce>

}