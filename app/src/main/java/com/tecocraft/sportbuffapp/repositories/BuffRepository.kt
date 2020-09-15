package com.tecocraft.sportbuffapp.repositories


import android.util.Log
import com.tecocraft.sportbuffapp.model.buffResponce.BuffResponce
import com.tecocraft.sportbuffapp.repositorycallbacks.BuffRepositoryCallback
import com.tecocraft.sportbuffapp.rest.ApiClient
import com.tecocraft.sportbuffapp.rest.SBAppInterface
import retrofit2.Call
import retrofit2.Response

class BuffRepository {

    private lateinit var responseData: Call<BuffResponce>
    private lateinit var apiService: SBAppInterface


    fun getBuffData(callback: BuffRepositoryCallback, buffId: Int) {
        apiService = ApiClient.getSbAppInterface()

        responseData = apiService.getBuff(buffId)

        responseData.enqueue(object : retrofit2.Callback<BuffResponce> {
            override fun onResponse(call: Call<BuffResponce>, response: Response<BuffResponce>) {

                if (response.isSuccessful){
                    if (response.body()!!.result!=null){
                        callback.onDataSuccess(response.body()!!)
                    }
                }
            }

            override fun onFailure(call: Call<BuffResponce>, t: Throwable) {
                Log.e("Buff Fail", "In onFailure : " + t.message.toString())
                callback.onDataFailure(t.message.toString())

            }

        })
    }


}

