package com.tecocraft.sportbuffapp


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tecocraft.sportbuffapp.common.VIDEO_URL
import com.tecocraft.sportbuffapp.ui.MainActivityViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
/**
 * Created by Akshay Jariwala
 */
@RunWith(MockitoJUnitRunner::class)
class MainActivityBuffTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var observer: Observer<String>

    private lateinit var mainActivityViewModel: MainActivityViewModel

    @Before
    fun setUp() {
        mainActivityViewModel = MainActivityViewModel()
    }

    @Test
    fun `videoUrl has an observer`() {
        mainActivityViewModel.videoUrl.observeForever(observer)
        assert(mainActivityViewModel.videoUrl.hasObservers())
    }

    @Test
    fun `fetchVideoUrl sets correct value for videoUrl`() {
        mainActivityViewModel.fetchVideoUrl()
        assert(mainActivityViewModel.videoUrl.value == VIDEO_URL)
    }

    @Test
    fun `fetchVideoUrl should trigger the observer for videoUrl`() {
        mainActivityViewModel.videoUrl.observeForever(observer)
        mainActivityViewModel.fetchVideoUrl()
        Mockito.verify(observer).onChanged(VIDEO_URL)
    }


}