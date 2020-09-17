package com.tecocraft.sportbuffapp


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.tecocraft.sportbuffapp.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Akshay Jariwala
 */

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun videoPlayerViewIsDisplayed() {
        val playerView = onView(withId(R.id.playerView))
        playerView.check(matches(isDisplayed()))
    }

    @Test
    fun buffViewIsAttached() {
        val buffView = onView(withId(R.id.buffView))
        buffView.check(matches(isEnabled()))
    }

}
