package com.tecocraft.buffoverlayview.remote

import com.tecocraft.buffoverlayview.remote.response.BuffApiResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by Akshay Jariwala
 */
class BuffApiService {

    companion object {

        private const val BASE_URL = "http://demo2373134.mockable.io/"
        private const val GET_BUFF_WITH_ID_ENDPOINT = "buffs/%s"
        private const val READ_TIMEOUT = 30000
        private const val CONNECTION_TIMEOUT = 30000

        /**
         * Fetches Buffs from the Buff API using their buff ids
         *
         * @param buffId The ID of the buff to be fetched
         * @return BuffResult
         */
        internal suspend fun getBuff(buffId: String): BuffResult {
            var response: BuffApiResponse? = null
            withContext(Dispatchers.IO) {
                try {
                    val url = URL(String.format(BASE_URL + GET_BUFF_WITH_ID_ENDPOINT, buffId))

                    val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
                    urlConnection.readTimeout = READ_TIMEOUT
                    urlConnection.connectTimeout = CONNECTION_TIMEOUT
                    urlConnection.setRequestProperty("Content-Type", "application/json")
                    urlConnection.requestMethod = "GET"

                    val statusCode = urlConnection.responseCode

                    if (statusCode == 200) {

                        val mIn = BufferedReader(
                            InputStreamReader(
                                urlConnection.inputStream
                            )
                        )

                        val sb = StringBuffer("")
                        var line = mIn.readLine()

                        while (line != null) {
                            sb.append(line)
                            line = mIn.readLine()
                        }

                        mIn.close()
                        response = Gson().fromJson<BuffApiResponse>(sb.toString(), BuffApiResponse::class.java)
                    } else {
                        return@withContext BuffResult.Error(
                            Throwable(BuffApiError.UNABLE_TO_FETCH_BUFF)
                        )
                    }
                } catch (e: Exception) {
                    return@withContext BuffResult.Error(
                        Throwable(BuffApiError.UNABLE_TO_FETCH_BUFF)
                    )
                }
            }

            val buff = response?.result
            return if (buff != null) {
                BuffResult.Success(buff)
            } else {
                BuffResult.Error(
                    Throwable(BuffApiError.NO_BUFF)
                )
            }
        }
    }
}