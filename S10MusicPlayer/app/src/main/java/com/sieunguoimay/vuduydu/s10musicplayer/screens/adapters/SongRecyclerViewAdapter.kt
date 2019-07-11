package com.sieunguoimay.vuduydu.s10musicplayer.screens.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sieunguoimay.vuduydu.s10musicplayer.R
import com.sieunguoimay.vuduydu.yamp.data.model.Song

//https://www.androidhive.info/2016/01/android-working-with-recycler-view/

class SongRecyclerViewAdapter(
    var listener:StandardRecyclerViewAdapter.ItemClickListener<Pair<Int,Song>>,
    var songList:ArrayList<Song>
): RecyclerView.Adapter<StandardRecyclerViewAdapter.SongViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): StandardRecyclerViewAdapter.SongViewHolder {
        return StandardRecyclerViewAdapter.SongViewHolder(
            LayoutInflater.from(p0.context).inflate(
                R.layout.standard_row,
                p0,
                false
            )
        )
    }
    override fun getItemCount(): Int {
        return songList.size
    }
    override fun onBindViewHolder(p0: StandardRecyclerViewAdapter.SongViewHolder, p1: Int) {
        var song = songList[p1]
        p0.title.setText(song.title)
        p0.sub.setText(song.artist)
        if(song.thumb!=null)
            p0.thumbail.setImageBitmap(song.thumb)
        p0.itemView.setOnClickListener {
            listener.onItemClick(Pair(p1,song))
        }
    }
}