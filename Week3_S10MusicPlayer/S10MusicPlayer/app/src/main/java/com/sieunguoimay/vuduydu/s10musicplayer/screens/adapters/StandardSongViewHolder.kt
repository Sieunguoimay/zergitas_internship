package com.sieunguoimay.vuduydu.s10musicplayer.screens.adapters

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sieunguoimay.vuduydu.s10musicplayer.R
import com.sieunguoimay.vuduydu.s10musicplayer.models.data.Song

class StandardSongViewHolder(view: View):RecyclerView.ViewHolder(view){
    var title: TextView
    var sub: TextView
    var thumbail: ImageView
    var moreOption:CardView
    init{
        title = view.findViewById(R.id.tv_standard_row_title)
        sub= view.findViewById(R.id.tv_standard_row_sub_text)
        thumbail = view.findViewById(R.id.iv_standard_row_icon)
        moreOption = view.findViewById(R.id.cv_standard_row_options)
    }

    fun bind(song: Song){
        title.text = song.title
        sub.text = song.artist
        if(song.thumb!=null)
            thumbail.setImageBitmap(song.thumb)
        else
            thumbail.setImageResource(R.drawable.ic_library_music_24dp)
    }

    interface ItemClickListener<T>{
        fun onItemClick(item:T)
    }
}