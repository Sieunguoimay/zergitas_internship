package com.sieunguoimay.vuduydu.s10musicplayer.screens.HomeScreenActivity.AllSongsScreenFragment.adapters

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sieunguoimay.vuduydu.s10musicplayer.R
import com.sieunguoimay.vuduydu.s10musicplayer.screens.HomeScreenActivity.HomeScreenActivity
import com.sieunguoimay.vuduydu.s10musicplayer.screens.adapters.StandardRecyclerViewAdapter

class PlaylistFragment: Fragment() {
    private lateinit var adapter: StandardRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return initView(inflater.inflate(R.layout.fragment_playlist, container, false))
    }

    private fun initView(view :View):View{
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_playlist_fragment)
        adapter = StandardRecyclerViewAdapter(null)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter

        prepareData()

        return view
    }

    private fun prepareData(){

        //data goes here

        adapter.infoList.clear()
        adapter.infoList.add(StandardRecyclerViewAdapter.RowInfo("Title","sub",null))
        adapter.infoList.add(StandardRecyclerViewAdapter.RowInfo("Title","sub",null))
        adapter.infoList.add(StandardRecyclerViewAdapter.RowInfo("Title","sub",null))
        adapter.infoList.add(StandardRecyclerViewAdapter.RowInfo("Title","sub",null))
        adapter.infoList.add(StandardRecyclerViewAdapter.RowInfo("Title","sub",null))
        adapter.infoList.add(StandardRecyclerViewAdapter.RowInfo("Title","sub",null))
        adapter.notifyDataSetChanged()
    }

}