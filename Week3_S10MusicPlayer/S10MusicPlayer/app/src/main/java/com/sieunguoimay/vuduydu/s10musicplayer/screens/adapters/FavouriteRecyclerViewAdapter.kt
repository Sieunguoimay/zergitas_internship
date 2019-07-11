package com.sieunguoimay.vuduydu.s10musicplayer.screens.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.sieunguoimay.vuduydu.s10musicplayer.R
import com.sieunguoimay.vuduydu.s10musicplayer.models.data.Song
import com.sieunguoimay.vuduydu.s10musicplayer.utils.Utils

//https://www.androidhive.info/2016/01/android-working-with-recycler-view/

class FavouriteRecyclerViewAdapter(
        var listener:StandardSongViewHolder.ItemClickListener<Pair<Int,Song>>,
        var favouriteList:ArrayList<Song>,
        var context: Context

): RecyclerView.Adapter<StandardSongViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int):StandardSongViewHolder {
        return StandardSongViewHolder(
            LayoutInflater.from(p0.context).inflate(
                R.layout.standard_row,
                p0,
                false
            )
        )
    }
    override fun getItemCount(): Int {
        return favouriteList.size
    }
    override fun onBindViewHolder(p0: StandardSongViewHolder, p1: Int) {
        var song = favouriteList[p1]

        p0.bind(song)
        p0.itemView.setOnClickListener{
            listener.onItemClick(Pair(p1,song))
        }

    }
}