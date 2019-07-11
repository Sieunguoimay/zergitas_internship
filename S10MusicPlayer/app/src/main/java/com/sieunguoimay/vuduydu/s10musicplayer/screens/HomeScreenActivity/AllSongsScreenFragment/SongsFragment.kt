package com.sieunguoimay.vuduydu.s10musicplayer.screens.HomeScreenActivity.AllSongsScreenFragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView

import com.sieunguoimay.vuduydu.s10musicplayer.R
import com.sieunguoimay.vuduydu.s10musicplayer.screens.HomeScreenActivity.HomeScreenActivity
import kotlinx.android.synthetic.main.app_bar_home_screen.*
import kotlinx.android.synthetic.main.fragment_all_songs.*


class SongsFragment : Fragment()
//    , StandardRecyclerViewAdapter.ItemClickListener<Pair<Int,Song>>
{

//    private lateinit var adapter: SongRecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return initView(inflater.inflate(R.layout.fragment_songs, container, false))
    }

    private fun initView(view :View):View{
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_songs_fragment)

//        adapter = SongRecyclerViewAdapter(this, (activity as HomeScreenActivity).songList!!)
//        adapter.notifyDataSetChanged()

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = (activity as HomeScreenActivity).adapter
        recyclerView.addOnScrollListener((activity as HomeScreenActivity).recyclerViewScrollListener)
        return view
    }



//    override fun onItemClick(pair: Pair<Int,Song>) {
//        (activity as HomeScreenActivity).playMusicInService(pair.first)
//    }

}



//
//private fun prepareData(){
//
//    //data goes here
//    adapter.infoList.clear()
//    val songList = (activity as HomeScreenActivity).songList
//    if(songList!=null){
//        for(a in songList){
//            adapter.infoList.add(StandardRecyclerViewAdapter.RowInfo(a.title,a.artist,null))
//        }
//    }
//
//}
//
