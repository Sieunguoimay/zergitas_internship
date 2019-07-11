package com.sieunguoimay.vuduydu.yamp.screens.main.adapters

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sieunguoimay.vuduydu.yamp.R
import com.sieunguoimay.vuduydu.yamp.data.model.Song
import kotlinx.android.synthetic.main.item_song.view.*


class SongAdapter (
    private val context: Context,
    private val listener:ItemSongListener
):
    RecyclerView.Adapter< RecyclerView.ViewHolder>()  {

    private var songs:List<Song>
    init{
        songs = ArrayList()
    }

    override fun getItemViewType(position: Int): Int {
        if(position == 0)
            return 0
        return 1
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        if(p1 == 0)
            return SomethingCoolViewHolder(LayoutInflater.from(context).inflate(R.layout.something_cool,p0,false))
        else
            return SongViewHolder(LayoutInflater.from(context).inflate(R.layout.item_song,p0,false))
    }

    override fun getItemCount(): Int = songs.size
    override fun onBindViewHolder(p0:  RecyclerView.ViewHolder, p1: Int) {
        if(p1==0)
            (p0 as SomethingCoolViewHolder).bindData()
        else
            (p0 as SongViewHolder).bindData()
    }
    override fun onBindViewHolder(holder:RecyclerView.ViewHolder, position:Int, payloads:MutableList<Any>){

        if(payloads.isNotEmpty()&&payloads[0]is Bitmap){
            (holder as SongViewHolder).bindData(payloads[0] as Bitmap)
        }else{
            super.onBindViewHolder(holder, position, payloads)
        }

    }


    fun updateSongs(songs: List<Song>){
        this.songs = songs
        notifyDataSetChanged()
    }
    fun updateSongs(index:Int, song:Song){
        this.songs[index].thumb =song.thumb
        notifyItemChanged(index,song.thumb)
    }


    fun getItemSong(pos: Int) = songs[pos]

    interface ItemSongListener{
        fun onClick(pos:Int)
    }

    inner class SongViewHolder(v: View):RecyclerView.ViewHolder(v){

        fun bindData(){
            val song = getItemSong(adapterPosition)
            if(song.thumb!=null) bindData(song.thumb!!)
            itemView.tv_name.text = song.name
            itemView.tv_singer.text = song.singer
            itemView.item_song.setOnClickListener{
                listener.onClick(adapterPosition)
            }
       }
        fun bindData(thumb:Bitmap){
            itemView.img_thumb.setImageBitmap(thumb)
        }

    }

    inner class SomethingCoolViewHolder(v: View):RecyclerView.ViewHolder(v){
        fun bindData(){



        }

    }
}