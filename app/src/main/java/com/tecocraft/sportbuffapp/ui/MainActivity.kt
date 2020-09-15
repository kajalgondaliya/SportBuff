package com.tecocraft.sportbuffapp.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.decoder.DecoderCounters
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.*
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.video.VideoRendererEventListener
import com.tecocraft.sportbuffapp.BaseActivity
import com.tecocraft.sportbuffapp.R
import com.tecocraft.sportbuffapp.adapter.AnsAdapter
import com.tecocraft.sportbuffapp.common.onSlide
import com.tecocraft.sportbuffapp.model.buffResponce.Answer
import com.tecocraft.sportbuffapp.model.buffResponce.BuffResponce
import com.tecocraft.sportbuffapp.presenterimpl.BuffPresenterImpl
import com.tecocraft.sportbuffapp.repositories.BuffRepository
import com.tecocraft.sportbuffapp.view.Buffview
import com.tecocraft.videooverlaylib.VidOverlayLayout
import java.util.*

class MainActivity : BaseActivity(), VideoRendererEventListener, Buffview {

    val fmManager = supportFragmentManager
    var TAG = "MainActivity"
    var videoUrl: String? =
        "https://buffup-public.s3.eu-west-2.amazonaws.com/video/toronto+nba+cut+3.mp4"

    private var simpleExoPlayerView: PlayerView? = null
    private var player: SimpleExoPlayer? = null
    lateinit var presenter: BuffPresenterImpl
    lateinit var videoOverlay: VidOverlayLayout
    var ansAdapter: AnsAdapter? = null
    var ansList: MutableList<Answer> = mutableListOf()
    var buffId: Int = 0
    val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        /* FullScreen Activity */
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        //Presenter Init
        presenter = BuffPresenterImpl()

        //Lib Init
        videoOverlay = fmManager.findFragmentById(R.id.fragOverlay) as VidOverlayLayout
        loadVideo(videoUrl!!)
        bindRecycleView()
        apiCall()
    }


    private fun apiCall(){
        if (commonUtils.isNetworkAvailable) {
            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    buffId++
                    if (buffId <= 5) {
                        presenter.buffField(BuffRepository(), this@MainActivity, buffId)
                    } else {
                        timer.cancel()
                    }
                }
            }, 30 * 1000, 30 * 1000)
        } else {
            Toast.makeText(
                this@MainActivity,
                "Please Check Your Internet Connection.",
                Toast.LENGTH_SHORT
            )
                .show()
        }

    }
    private fun loadVideo(videoUrl: String) {
        //Video Url parse
        val streamUrl =
            Uri.parse(videoUrl)

        //Player Bandwidth
        val bandwidthMeter = DefaultBandwidthMeter()

        val videoTrackSelectionFactory: TrackSelection.Factory =
            AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector: TrackSelector = DefaultTrackSelector(videoTrackSelectionFactory)


        //Create the player
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)
        simpleExoPlayerView = SimpleExoPlayerView(this)
        simpleExoPlayerView =
            findViewById<SimpleExoPlayerView>(R.id.player_view)


        //Set media controller
        (simpleExoPlayerView as SimpleExoPlayerView?)!!.useController =
            false //set to true or false to see controllers
        (simpleExoPlayerView as SimpleExoPlayerView?)!!.requestFocus()

        // Bind the player to the view.
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        val params = (simpleExoPlayerView as SimpleExoPlayerView?)!!.layoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        (simpleExoPlayerView as SimpleExoPlayerView?)!!.layoutParams = params

        //set player
        (simpleExoPlayerView as SimpleExoPlayerView?)!!.player = player

        // Measures bandwidth during playback. Can be null if not required.
        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, "SportBuff"),
            bandwidthMeter
        )

        // This is the MediaSource representing the media to be played.
        val videoSource: MediaSource =
            ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(streamUrl)

        // Prepare the player with the source.
        player!!.prepare(videoSource)

        // Event Listener
        player!!.addListener(object : Player.EventListener {
            override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {

            }

            override fun onTracksChanged(
                trackGroups: TrackGroupArray?,
                trackSelections: TrackSelectionArray?
            ) {
            }

            override fun onLoadingChanged(isLoading: Boolean) {
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playWhenReady && playbackState == Player.STATE_READY) {

                } else {
                    Log.e(TAG, "VIDEO NOT READY")
                }
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            }

            override fun onPlayerError(error: ExoPlaybackException?) {
                Log.e(TAG, "ERROR---> " + error!!.message + " " + error.cause)
            }

            override fun onPositionDiscontinuity(reason: Int) {
            }

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
            }

            override fun onSeekProcessed() {
            }

        })

        //run file/link when ready to play.
        player!!.playWhenReady = true

        player!!.setVideoDebugListener(this)
    }

    override fun onVideoEnabled(counters: DecoderCounters?) {

    }

    override fun onVideoDecoderInitialized(
        decoderName: String?,
        initializedTimestampMs: Long,
        initializationDurationMs: Long
    ) {
    }

    override fun onVideoInputFormatChanged(format: Format?) {
    }

    override fun onDroppedFrames(count: Int, elapsedMs: Long) {
    }

    override fun onVideoSizeChanged(
        width: Int,
        height: Int,
        unappliedRotationDegrees: Int,
        pixelWidthHeightRatio: Float
    ) {
    }

    override fun onRenderedFirstFrame(surface: Surface?) {
    }

    override fun onVideoDisabled(counters: DecoderCounters?) {
    }

    override fun showLoader() {
    }

    override fun hideLoader() {
    }

    override fun onDataSuccess(model: BuffResponce) {
        ansList.clear()

        videoOverlay.setQuestionBuffVisibility(View.VISIBLE)

        videoOverlay.setPlayerProfile(model.result.author.image)

        videoOverlay.setPlayerName(model.result.author.first_name+" "+model.result.author.last_name)

        videoOverlay.countDownTimer(model.result.time_to_show)

        videoOverlay.setQuestion(model.result.question.title)

        ansList.addAll(model.result.answers)
        ansAdapter!!.notifyDataSetChanged()

    }

    override fun onDataFailure(msg: String) {
    }

    override fun showError(msg: String) {
    }

    private fun bindRecycleView() {
        val linearLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        videoOverlay.rvAns.layoutManager = linearLayoutManager
        ansAdapter = AnsAdapter(this, ansList, object : onSlide {
            override fun getEvent(position: Int, text: String) {
               videoOverlay.onSlide()
            }

        })
        videoOverlay.rvAns.setHasFixedSize(true)
        videoOverlay.rvAns.adapter = ansAdapter
    }
}