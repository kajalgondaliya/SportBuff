package com.tecocraft.buffoverlayview.view

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.tecocraft.buffoverlayview.BuffOverlayView
import com.tecocraft.buffoverlayview.exceptions.MissingInitializationException
import com.tecocraft.buffoverlayview.exceptions.UnsupportedActivityViewGroupException
import com.tecocraft.buffoverlayview.listeners.EventListener
import com.tecocraft.buffoverlayview.model.Buff
import com.tecocraft.buffoverlayview.remote.BuffApiService
import com.tecocraft.buffoverlayview.remote.BuffResult
import com.squareup.picasso.Picasso
import com.tecocraft.buffoverlayview.R
import de.hdodenhof.circleimageview.CircleImageView
import douglasspgyn.com.github.circularcountdown.CircularCountdown
import douglasspgyn.com.github.circularcountdown.listener.CircularListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by Akshay Jariwala
 */
class BuffOverlay(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {

    /** View Attributes */
    var authorInfoBackground: Drawable? = ContextCompat.getDrawable(context, R.drawable.buff_author_layout_bg)
        set(value) {
            if (value != null) {
                field = value
                refreshView()
            }
        }

    var authorInfoTextColor: Int = ContextCompat.getColor(context, DEFAULT_DARK_TEXT_COLOR)
        set(value) {
            field = value
            refreshView()
        }

    var closeIcon: Drawable? = ContextCompat.getDrawable(context, R.drawable.buff_ic_close)
        set(value) {
            if (value != null) {
                field = value
                refreshView()
            }
        }

    var questionTextColor: Int = ContextCompat.getColor(context, DEFAULT_LIGHT_TEXT_COLOR)
        set(value) {
            field = value
            refreshView()
        }

    var questionBackground: Drawable? = ContextCompat.getDrawable(context, R.drawable.buff_question_layout_bg)
        set(value) {
            if (value != null) {
                field = value
                refreshView()
            }
        }

    var answerRowBackground: Drawable? = ContextCompat.getDrawable(context, R.drawable.buff_question_answer_layout_bg)
        set(value) {
            if (value != null) {
                field = value
                refreshView()
            }
        }

    var answerRowTextColor: Int = ContextCompat.getColor(context, DEFAULT_DARK_TEXT_COLOR)
        set(value) {
            field = value
            refreshView()
        }

    var selectedAnswerRowBackground: Drawable? = ContextCompat.getDrawable(context, R.drawable.buff_selected_answer_layout_bg)
        set(value) {
            if (value != null) {
                field = value
                refreshView()
            }
        }

    var selectedAnswerRowTextColor: Int = ContextCompat.getColor(context, DEFAULT_LIGHT_TEXT_COLOR)
        set(value) {
            field = value
            refreshView()
        }



    /** Event Listener to be implemented by the host app */
    private var listener: EventListener? = null

    /** Whether Buffs should be fetched and displayed or not */
    private var shouldFetchAndDisplayBuffs = false

    /** The ID of the present stream from the host application */
    private var streamId: String? = null

    /** Instance of the BuffView */
    private val buffView: LinearLayout?

    /** Current ID of a buff to be fetched */
    private var currentBuffId: Int = 1

    /** LayoutParams for the answer row view */
    private lateinit var answerRowLayoutParams: LayoutParams

    /** Handler to set intervals */
    private val mHandler = Handler()

    /** Instance of the CircularCountdown Timer */
    private var timerView: CircularCountdown? = null

    /**
     * Initialize view and get view attributes
     */
    init {
        buffView = View.inflate(context, R.layout.buff_view, this) as LinearLayout

        context.theme.obtainStyledAttributes(attrs, R.styleable.BuffOverlay,
            0, 0).apply {

            try {
                //Header Items
                authorInfoBackground = getDrawable(R.styleable.BuffOverlay_buff_author_info_background)
                authorInfoTextColor = getColor(
                    R.styleable.BuffOverlay_buff_author_info_text_color,
                    ContextCompat.getColor(context, DEFAULT_DARK_TEXT_COLOR)
                )
                closeIcon = getDrawable(R.styleable.BuffOverlay_buff_close_icon)

                //Question Items
                questionTextColor = getColor(
                    R.styleable.BuffOverlay_buff_author_info_text_color,
                    ContextCompat.getColor(context, DEFAULT_LIGHT_TEXT_COLOR)
                )
                questionBackground = getDrawable(R.styleable.BuffOverlay_buff_question_background)

                //Answer Items
                answerRowBackground = getDrawable(R.styleable.BuffOverlay_buff_answer_row_background)
                answerRowTextColor = getColor(
                    R.styleable.BuffOverlay_buff_answer_row_text_color,
                    ContextCompat.getColor(context, DEFAULT_DARK_TEXT_COLOR)
                )
                selectedAnswerRowBackground = getDrawable(R.styleable.BuffOverlay_buff_selected_answer_row_background)
                selectedAnswerRowTextColor = getColor(
                    R.styleable.BuffOverlay_buff_selected_answer_row_text_color,
                    ContextCompat.getColor(context, DEFAULT_LIGHT_TEXT_COLOR)
                )
            } finally {
                recycle()
            }
        }

    }

    /**
     * Redraws the view
     */
    private fun refreshView() {
        invalidate()
        requestLayout()
    }

    /**
     * Connects the BuffView to the present Stream in the host application
     *
     * <p>For the actual use case of the SDK, Buffs are fetched using the ID of the stream currently
     * playing and are then displayed at interval if the host app has the start method called.
     *
     * @param streamId The ID of the present stream playing
     */
    fun setupWithStream(@NonNull streamId: String) {
        this.streamId = streamId
    }

    /**
     * Starts fetching of Buffs and displaying them
     *
     * @param activity Activity on which BuffView is hosted on
     * @throws MissingInitializationException if BuffView is not initialized
     * @throws UnsupportedActivityViewGroupException if an unsupported parent ViewGroup is used
     */
    @Throws(MissingInitializationException::class, UnsupportedActivityViewGroupException::class)
    fun start(@NonNull activity: Activity) {
        shouldFetchAndDisplayBuffs = true
        fetchBuffWithId(currentBuffId, activity)
    }

    /**
     * Fetches a buff using its ID
     *
     * @param buffId ID of the Buff to be fetched
     * @param activity Activity on which BuffView is hosted on
     */
    private fun fetchBuffWithId(@NonNull buffId: Int,@NonNull activity: Activity) {
        /* Check if Buffup is initialized */
        checkBuffupInitialization()

        /* Check if user wants Buffs to be fetched */
        if (!shouldFetchAndDisplayBuffs) return

        /* Stop fetching buffs when maximum Buff ID is reached */
        if (buffId > MAXIMUM_BUFF_ID) return

        GlobalScope.launch(Dispatchers.Main) {
            when(val fetchBuffResult = BuffApiService.getBuff(buffId.toString())) {
                is BuffResult.Success -> {
                    displayBuffView(fetchBuffResult.buff, activity)
                }
                is BuffResult.Error -> {
                    listener?.onBuffError(fetchBuffResult.error)
                }
            }

            mHandler.postDelayed({
                fetchBuffWithId(++currentBuffId, activity)
            }, TIME_BETWEEN_BUFFS)
        }

    }

    /**
     * Checks if Buffup has been initialized
     *
     * @throws MissingInitializationException if BuffView is not initialized
     */
    private fun checkBuffupInitialization() {
        if (BuffOverlayView.getInstance()?.authKey == null) {
            throw MissingInitializationException(
                Throwable(context.getString(R.string.buff_missing_initialization_exception))
            )
        }
    }

    /**
     * Displays BuffView after fetching
     *
     * @param buff The present Buff
     * @param activity Activity on which BuffView is hosted on
     */
    private fun displayBuffView(@NonNull buff: Buff, @NonNull activity: Activity) {

        /* Check if parent ViewGroup is supported by BuffView */
        checkViewGroupSupport(activity)

        /* Check if all Buff data is present */
        if (!isValidBuff(buff)) return

        /* Apply alignment configuration*/
        applyAlignmentConfigurationToBuffView()

        /* Set Buff Header */
        setHeaderView(buff.author!!)

        /* Set Question Details */
        setQuestionView(buff.question!!)

        /* Set CountDown Timer View */
        setCountDownTimerView(buff.timeToShow ?: 0)

        /* Set Answer Details */
        setAnswersView(buff.answers!!)

        /* Enable the BuffView if disabled */
        enableBuffView()

        /* Set BuffView to Visible */
        buffView?.visibility = View.VISIBLE

    }

    /**
     * Checks if parent ViewGroup is supported by BuffView
     *
     * @param activity Activity on which BuffView is hosted on
     * @throws UnsupportedActivityViewGroupException if an unsupported parent ViewGroup is used
     */
    private fun checkViewGroupSupport(@NonNull activity: Activity) {
        val viewGroup = activity
            .findViewById<ViewGroup>(android.R.id.content).getChildAt(0) as ViewGroup
        if (viewGroup !is RelativeLayout) {
            throw UnsupportedActivityViewGroupException(
                Throwable(context.getString(R.string.buff_unsupported_activity_viewgroup_exception))
            )
        }
    }

    /**
     * Checks if all the necessary Buff data is present
     *
     * @param buff The present Buff to be displayed
     * @return true or false
     */
    private fun isValidBuff(buff: Buff): Boolean {
        if (buff.author == null) {
            listener?.onBuffError(
                Throwable(context.getString(R.string.buff_invalid_author_data))
            )
            return false
        }
        if (buff.question == null) {
            listener?.onBuffError(
                Throwable(context.getString(R.string.buff_invalid_question_data))
            )
            return false
        }
        if (buff.answers == null || buff.answers.isEmpty()) {
            listener?.onBuffError(
                Throwable(context.getString(R.string.buff_invalid_answers_data))
            )
            return false
        }
        if (buff.timeToShow == null) {
            listener?.onBuffError(
                Throwable(context.getString(R.string.buff_invalid_time_to_show_data))
            )
            return false
        }

        return true
    }

    /**
     * Set constraints and alignments for the buffview and make it show proportionately on every screen
     */
    private fun applyAlignmentConfigurationToBuffView() {
        /* Set BuffView width to be 40% of the screen width */
        val width = (Resources.getSystem().displayMetrics.widthPixels * 0.4).toInt()
        val viewParams = RelativeLayout.LayoutParams(
            width, RelativeLayout.LayoutParams.WRAP_CONTENT
        )

        /* Add margins */
        val margin = resources.getDimension(R.dimen.buff_buffview_layout_margin).toInt()
        viewParams.setMargins(margin, margin, margin, margin)

        viewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1)
        buffView?.layoutParams = viewParams
    }

    /**
     * Configures the BuffView's header which contains author info and the close button
     * @param author The present Buff author
     */
    private fun setHeaderView(author: Buff.Author) {
        val headerView = buffView?.findViewById<RelativeLayout>(R.id.headerView)

        // Close Button
        val closeButton =
            headerView?.findViewById<ImageButton>(R.id.closeBuffIcon)
        closeButton?.setImageDrawable(closeIcon)
        closeButton?.setOnClickListener {
            stopTimer()
            closeBuffView()
        }

        /* Set Author Details */
        setAuthorView(author)

        headerView?.visibility = View.VISIBLE
        val animation = AnimationUtils.loadAnimation(buffView?.context, R.anim.buff_slide_up)
        headerView?.startAnimation(animation)
    }

    /**
     * Configures the Author View and add it with animation into the BuffView
     * @param author The present Buff author
     */
    private fun setAuthorView(author: Buff.Author) {

        val authorFirstName = author.firstName ?: ""
        val authorLastName = author.lastName ?: ""
        val authorName = "$authorFirstName $authorLastName"

        val authorTextView = buffView?.findViewById<TextView>(R.id.authorText)
        authorTextView?.text = authorName
        authorTextView?.setTextColor(authorInfoTextColor)

        // Author Image
        val authorImageView = buffView?.findViewById<CircleImageView>(R.id.authorImage)
        Picasso.get().load(author.image).into(authorImageView)

        // Set Author View Background
        buffView?.findViewById<LinearLayout>(R.id.authorInfoContainer)?.background = authorInfoBackground
    }

    /**
     * Configures the Question View and add it with animation into BuffView
     * @param question The present Buff Question
     * */
    private fun setQuestionView(question: Buff.Question) {
        val questionTextView =
            buffView?.findViewById<TextView>(R.id.questionText)
        questionTextView?.text = question.title
        questionTextView?.setTextColor(questionTextColor)

        val questionLayout =
            buffView?.findViewById<RelativeLayout>(R.id.questionLayout)
        questionLayout?.background = questionBackground
        questionLayout?.visibility = View.VISIBLE

        val animation = AnimationUtils.loadAnimation(buffView?.context, R.anim.buff_slide_right)
        questionLayout?.startAnimation(animation)
    }

    /**
     * Sets the circular countdown timer view for a buff. It closes the BuffView once the timer is
     * over, call #closeBuffView.
     * */
    private fun setCountDownTimerView(seconds: Int) {
        timerView = buffView?.findViewById(R.id.circularCountDown)

        // Disable repeated iterations
        timerView?.disableLoop()

        timerView?.create(1, seconds, CircularCountdown.TYPE_SECOND)?.listener(object : CircularListener {
                override fun onTick(progress: Int) {
                }

                override fun onFinish(newCycle: Boolean, cycleCount: Int) {
                    closeBuffView()
                }
            })?.start()
    }

    /**
     * Generates the answers layout by generating views from #inflateAnswerRowView() and then
     * adding them to pre-defined answersLayout. Number of views generated is ensured to be
     * equal to the size of answers list
     *
     * @param answers List of Buff.Answer model obtained from the Buff API
     * */
    private fun setAnswersView(answers: List<Buff.Answer>) {

        // Add answer rows to container
        val answersLayout =
            buffView?.findViewById<LinearLayout>(R.id.answersLayout)

        /* Remove previously added views */
        answersLayout?.removeAllViews()

        for (answer in answers) {
            val answerRowView = inflateAnswerRowView(answersLayout) as? MotionLayout

            // Set answer index image
            val indexImageView = answerRowView?.findViewById<CircleImageView>(R.id.answerImage)
            Picasso.get().load(answer.imageModel.image2?.url).into(indexImageView)

            // Set answer title
            val answerTextView =
                answerRowView?.findViewById<TextView>(R.id.answerText)
            answerTextView?.text = answer.title
            answerTextView?.setTextColor(answerRowTextColor)

            // Set transition listener for the answer swipe
            answerRowView?.setTransitionListener(object : MotionLayout.TransitionListener {
                override fun onTransitionTrigger(
                    motionLayout: MotionLayout?,
                    idStart: Int,
                    idEnd: Boolean,
                    progess: Float
                ) {
                }

                override fun onTransitionStarted(motionLayout: MotionLayout?, idStart: Int, idEnd: Int) {
                }

                override fun onTransitionChange(motionLayout: MotionLayout?, idStart: Int, idEnd: Int, progress: Float) {
                }

                override fun onTransitionCompleted(motionLayout: MotionLayout?, idCurrent: Int) {
                    /* Check if user completely transitioned to the right */
                    if (idCurrent == motionLayout?.endState) {
                        answerRowView.background = selectedAnswerRowBackground
                        answerTextView?.setTextColor(selectedAnswerRowTextColor)

                        stopTimer()
                        disableBuffView()

                        // Close BuffView after 2 seconds
                        mHandler.postDelayed({
                            closeBuffView()
                        }, POST_ANSWER_BUFF_DISMISS_DURATION)

                        // Fire callback event implemented in the host activity
                        listener?.onBuffAnswerSelected(answer)
                    }
                }

            })

            // Add row to the layout
            answersLayout?.addView(answerRowView)
            val animation = AnimationUtils.loadAnimation(buffView?.context, R.anim.buff_slide_right)
            answerRowView?.startAnimation(animation)
        }
    }

    /**
     * Inflates answer row and apply layout params set in #getAnswerRowLayoutParams()
     *
     * @return Answer View
     * */
    private fun inflateAnswerRowView(answersLayout: LinearLayout?): View? {
        val view = LayoutInflater.from(answersLayout?.context)
            .inflate(R.layout.buff_question_answer_item, answersLayout, false)
        view.layoutParams = getAnswerRowLayoutParams()
        view.background = answerRowBackground
        return view
    }

    /**
     * LayoutParams for the dynamically generated answer view row generated in
     * #inflateAnswerRowView()
     *
     * @return LayoutParams for row view
     * */
    private fun getAnswerRowLayoutParams(): LayoutParams {
        if (!::answerRowLayoutParams.isInitialized) {
            answerRowLayoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            answerRowLayoutParams.bottomMargin =
                resources.getDimension(R.dimen.buff_answer_row_margin_bottom).toInt()
        }
        return answerRowLayoutParams
    }

    /**
     * Stops the countdown timer
     */
    private fun stopTimer() {
        timerView?.stop()
    }

    /**
     * Called when the #closeButton is tapped or when timer has finished.
     * Hide the buffView
     */
    private fun closeBuffView() {
        disableBuffView()
        buffView?.visibility = View.GONE
        val animation = AnimationUtils.loadAnimation(buffView?.context, R.anim.buff_slide_left)
        buffView?.startAnimation(animation)
    }

    /**
     * Disables BuffView once called
     */
    private fun View.disableBuffView() {
        isEnabled = false
        if (this is ViewGroup) {
            //Disable further motion transition
            if (this is MotionLayout) {
                this.getTransition(R.id.answerImageTransition)?.setEnable(false)
            }
            children.forEach { child -> child.disableBuffView() }
        }
    }

    /**
     * Enables BuffView once called
     */
    private fun View.enableBuffView() {
        isEnabled = true
        if (this is ViewGroup) children.forEach { child -> child.enableBuffView() }
    }

    /**
     * Stops fetching and displaying of Buffs. Also removes listener automatically
     */
    fun stop() {
        shouldFetchAndDisplayBuffs = false
        removeListener()
        handler.removeCallbacksAndMessages(null)
    }

    /**
     * Adds an event listener to the view to notify the host app of events happening within the view
     *
     * @param listener The EventListener interface that is to be implemented
     */
    fun addListener(@NonNull listener: EventListener) {
        this.listener = listener
    }

    /**
     * Removes the event listener from the view to stop notifying the host app of events happening
     * within the vie
     */
    fun removeListener() {
        this.listener?.let {
            this.listener == null
        }
    }

    companion object {
        private val DEFAULT_DARK_TEXT_COLOR = R.color.buff_text_dark
        private const val DEFAULT_LIGHT_TEXT_COLOR = android.R.color.white

        private const val TIME_BETWEEN_BUFFS = 30000L
        private const val POST_ANSWER_BUFF_DISMISS_DURATION = 2000L
        private const val MAXIMUM_BUFF_ID = 5
    }
}