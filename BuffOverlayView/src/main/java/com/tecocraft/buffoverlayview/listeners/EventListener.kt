package com.tecocraft.buffoverlayview.listeners

import com.tecocraft.buffoverlayview.model.Buff

/**
 * Created by Akshay Jariwala
 */

/**
 * Listener of changes in the view. All methods have default implementations to allow
 * selective overrides.
 */
interface EventListener {

    /**
     * Called when a buff is displayed on the screen.
     *
     * @param buff The buff displayed.
     */
    fun onBuffDisplayed(buff: Buff) {}

    /**
     * Called when an answer is selected from the list of Buff Answers.
     *
     * @param answer The Buff Answer selected.
     */
    fun onBuffAnswerSelected(answer: Buff.Answer) {}

    /**
     * Called when an error is encountered while fetching Buff.
     *
     * @param t Throwable that contains the error thrown.
     */
    fun onBuffError(t: Throwable) {}

}


