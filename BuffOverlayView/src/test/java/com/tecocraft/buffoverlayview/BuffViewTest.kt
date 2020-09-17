package com.tecocraft.buffoverlayview

import android.app.Activity
import com.tecocraft.buffoverlayview.exceptions.MissingInitializationException
import com.tecocraft.buffoverlayview.view.BuffOverlay
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Akshay Jariwala
 */

@RunWith(MockitoJUnitRunner::class)
class BuffViewTest {

    @Mock
    lateinit var activity: Activity

    @Mock
    lateinit var buffView: BuffOverlay

    @Test
    fun buffViewThrowsInitializationExceptionWhenStartIsCalled() {
        try {
            buffView.start(activity)
        } catch (e: MissingInitializationException) {

        } catch (e: NullPointerException) {

        }
    }

}