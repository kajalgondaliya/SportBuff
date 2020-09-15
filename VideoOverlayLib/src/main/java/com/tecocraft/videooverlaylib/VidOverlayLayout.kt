package com.tecocraft.videooverlaylib

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.makeramen.roundedimageview.RoundedImageView
import douglasspgyn.com.github.circularcountdown.CircularCountdown
import douglasspgyn.com.github.circularcountdown.listener.CircularListener

open class VidOverlayLayout : Fragment() {

    lateinit var ivPlayerProfile:RoundedImageView
    lateinit var tvPlayerName:TextView
    lateinit var tvQue:TextView
    lateinit var llClose:LinearLayout
    lateinit var circularCountdown:CircularCountdown
    lateinit var rvAns:RecyclerView
    lateinit var svMain:ScrollView

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val overlayView = inflater.inflate(R.layout.vid_overlay_layout, container, false)
        bindView(overlayView)
        QueBuffClickListner()
        return overlayView
    }

    private fun bindView(view:View){
        ivPlayerProfile = view.findViewById(R.id.ivPlayerProfile)
        tvPlayerName = view.findViewById(R.id.tvPlayerName)
        tvQue = view.findViewById(R.id.tvQue)
        llClose = view.findViewById(R.id.llClose)
        circularCountdown = view.findViewById(R.id.circularCountdown)
        rvAns = view.findViewById(R.id.rvAns)
        svMain = view.findViewById(R.id.svMain)
    }

    open fun setPlayerProfile(profile:String){
        Glide.with(this)
            .load(profile)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(ivPlayerProfile)
    }

    open fun setPlayerName(name:String){
        tvPlayerName.text = name
    }

    open fun setQuestionBuffVisibility(visibility:Int){
        svMain.visibility = visibility
    }

    open fun QueBuffClickListner(){
        llClose.setOnClickListener {
            svMain.visibility = View.GONE
            if (circularCountdown.isRunning()) {
                circularCountdown.stop()
            }
        }
    }

    open fun setQuestion(que:String){
        tvQue.text = que
    }
    open fun countDownTimer(time: Int) {

        circularCountdown.create(0, time, CircularCountdown.TYPE_SECOND)
            .listener(object : CircularListener {
                override fun onFinish(newCycle: Boolean, cycleCount: Int) {
                    onSlide()
                }

                override fun onTick(progress: Int) {

                }

            })

        circularCountdown.start()
    }


    open fun onSlide(){
        svMain.visibility = View.GONE
        circularCountdown.stop()
    }
}