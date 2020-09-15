package com.tecocraft.sportbuffapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.romychab.slidetounlock.ISlideChangeListener
import com.github.romychab.slidetounlock.SlideLayout

import com.tecocraft.sportbuffapp.R
import com.tecocraft.sportbuffapp.model.buffResponce.Answer
import com.tecocraft.sportbuffapp.common.onSlide


class AnsAdapter(val context: Context, var ansList: MutableList<Answer>, var onSlider: onSlide) :
    RecyclerView.Adapter<AnsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_ans, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ansModel = ansList[position]

        holder.tvSlider.text = ansModel.title

        Glide.with(context)
            .load(ansModel.image.`2`.url)
            .into(holder.ivSlider)


        holder.ansSlider.addSlideChangeListener(object : ISlideChangeListener {
            override fun onSlideStart(slider: SlideLayout?) {

            }

            override fun onSlideChanged(slider: SlideLayout?, percentage: Float) {
                if (percentage >= 0.5) {
                    slider!!.background =
                        context.resources.getDrawable(R.drawable.rounded_corner_blue)
                    holder.tvSlider.setTextColor(context.resources.getColor(R.color.colorWhite))
                } else {
                    slider!!.background =
                        context.resources.getDrawable(R.drawable.rounded_corner_grey_trans)
                    holder.tvSlider.setTextColor(context.resources.getColor(R.color.colorBlack))
                }
            }

            override fun onSlideFinished(slider: SlideLayout?, done: Boolean) {
                if (done) {
                    onSlider.getEvent(position, ansModel.title)
                    slider!!.reset()
                }
            }

        })


    }


    override fun getItemCount(): Int {
        return ansList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ansSlider = itemView.findViewById<SlideLayout>(R.id.slider)
        val ivSlider = itemView.findViewById<ImageView>(R.id.ivSlider)
        val tvSlider = itemView.findViewById<TextView>(R.id.tvSlider)

    }

}

