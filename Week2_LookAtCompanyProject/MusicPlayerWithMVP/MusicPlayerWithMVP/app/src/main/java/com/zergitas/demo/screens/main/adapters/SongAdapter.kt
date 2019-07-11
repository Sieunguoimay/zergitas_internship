package com.zergitas.demo.screens.main.adapters

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zergitas.demo.R
import com.zergitas.demo.data.model.Song
import kotlinx.android.synthetic.main.item_song.view.*

class SongAdapter(
    private val context: Context,
    private val listener: ItemSongListener
) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder>() {
     var songs: List<Song>

    init {
        songs = ArrayList()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SongViewHolder {
        return SongViewHolder(LayoutInflater.from(context).inflate(R.layout.item_song, p0, false))
    }

    override fun getItemCount(): Int = songs.size

    override fun onBindViewHolder(p0: SongViewHolder, p1: Int) {
        p0.bindData()
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty() && payloads[0] is Bitmap) {
            holder.bindData(payloads[0] as Bitmap)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    fun getItemSong(pos: Int) = songs[pos]

    fun updateSongs(songs: List<Song>) {
        this.songs = songs
        notifyDataSetChanged()
    }

    fun updateSongs(index: Int, song: Song) {
        this.songs[index].thumb = song.thumb
        notifyItemChanged(index, song.thumb)
    }


    inner class SongViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        fun bindData() {
            val song = getItemSong(adapterPosition)
            if (song.thumb != null) bindData(song.thumb!!)
            itemView.tv_name.text = song.name
            itemView.tv_singer.text = song.singer
            itemView.item_song.setOnClickListener {
                listener.onClick(adapterPosition)
            }

        }

        fun bindData(thumb: Bitmap) {
            itemView.img_thumb.setImageBitmap(thumb)
        }
    }

    interface ItemSongListener {
        fun onClick(pos: Int)
    }
}