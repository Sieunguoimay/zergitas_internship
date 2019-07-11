package com.sieunguoimay.vuduydu.s10musicplayer.screens.adapters

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import com.sieunguoimay.vuduydu.s10musicplayer.R

//https://www.androidhive.info/2016/01/android-working-with-recycler-view/

class StandardRecyclerViewAdapter(
    var listener:ItemClickListener<Int>?=null
): RecyclerView.Adapter<StandardRecyclerViewAdapter.SongViewHolder>() {
    var infoList = ArrayList<RowInfo>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SongViewHolder {
        return SongViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.standard_row,p0,false))
    }

    override fun getItemCount(): Int {
        return infoList.size
    }

    override fun onBindViewHolder(p0: SongViewHolder, p1: Int) {
        var info = infoList[p1]
        p0.title.setText(info.title)
        p0.sub.setText(info.sub)
        if(info.icon!=null)
            p0.thumbail.setImageBitmap(info.icon)
        p0.itemView.setOnClickListener {
            listener?.onItemClick(p1)
        }
    }

    class SongViewHolder(view: View):RecyclerView.ViewHolder(view){
        var title: TextView
        var sub: TextView
        var thumbail: ImageView
        init{
            title = view.findViewById(R.id.tv_standard_row_title)
            sub= view.findViewById(R.id.tv_standard_row_sub_text)
            thumbail = view.findViewById(R.id.iv_standard_row_icon)
        }
    }
    class RowInfo(
        var title:String,
        var sub: String,
        var icon: Bitmap?
    ){}
    interface ItemClickListener<T>{
        fun onItemClick(item:T)
    }
}