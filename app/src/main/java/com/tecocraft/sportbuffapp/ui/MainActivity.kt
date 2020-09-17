package com.tecocraft.sportbuffapp.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProviders
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource

import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.tecocraft.buffoverlayview.listeners.EventListener
import com.tecocraft.buffoverlayview.model.Buff
import com.tecocraft.sportbuffapp.R
import com.tecocraft.sportbuffapp.extensions.showToast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var TAG = "MainActivity"
    private var player: SimpleExoPlayer? = null
    private val mainActivityViewModel: MainActivityViewModel by lazy {
        ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
    }


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

        mainActivityViewModel.fetchVideoUrl()

        mainActivityViewModel.videoUrl.observe(this, androidx.lifecycle.Observer {
            if (it.isNotEmpty()) {
                prepareVideoPlayer(it)
                initializeBuffView()
            }
        })

    }

    private fun prepareVideoPlayer(videoUrl: String) {
        val player = SimpleExoPlayer.Builder(this).build()
        playerView.player = player

        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory = DefaultDataSourceFactory(this,
            Util.getUserAgent(this, getString(R.string.app_name))
        )

        // This is the MediaSource representing the media to be played.
        val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(videoUrl.toUri())

        // Prepare the player with the source.
        player.prepare(videoSource)
        player.playWhenReady = true

    }

    private fun initializeBuffView() {
        buffView.setupWithStream("STREAM_ID")

        buffView.addListener(object : EventListener {
            override fun onBuffDisplayed(buff: Buff) {
                super.onBuffDisplayed(buff)
            }

            override fun onBuffAnswerSelected(answer: Buff.Answer) {
                super.onBuffAnswerSelected(answer)
                showToast("Your Answer is : ${answer.title}")
            }

            override fun onBuffError(t: Throwable) {
                super.onBuffError(t)
                showToast("Error : ${t.message}")
            }
        })

        buffView.start(this)

    }

    override fun onStop() {
        player?.stop()
        player?.release()

        //Stop BuffView
        buffView.stop()
        super.onStop()
    }



}