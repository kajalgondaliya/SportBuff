package com.tecocraft.buffoverlayview

import com.tecocraft.buffoverlayview.remote.BuffApiService
import com.tecocraft.buffoverlayview.remote.BuffResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by Akshay Jariwala
 */
@RunWith(JUnit4::class)
class BuffApiServiceTest {

    @Test
    fun `fetchBuff returns Success response`() = runBlocking {
        val result = withContext(Dispatchers.Default) {
            BuffApiService.getBuff("1")
        }
        assert(result is BuffResult.Success)
    }

    @Test
    fun `ensure buffId param falls between 1 to 5`() = runBlocking {
        val result = withContext(Dispatchers.Default) {
            BuffApiService.getBuff("6")
        }
        assert(result is BuffResult.Error)
    }

}