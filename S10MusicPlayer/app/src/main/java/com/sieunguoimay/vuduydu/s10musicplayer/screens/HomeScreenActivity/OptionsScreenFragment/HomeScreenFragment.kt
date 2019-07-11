package com.sieunguoimay.vuduydu.s10musicplayer.screens.HomeScreenActivity.OptionsScreenFragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*

import com.sieunguoimay.vuduydu.s10musicplayer.R
import com.sieunguoimay.vuduydu.s10musicplayer.screens.HomeScreenActivity.AllSongsScreenFragment.AllSongsFragment
import com.sieunguoimay.vuduydu.s10musicplayer.screens.HomeScreenActivity.HomeScreenActivity
import com.sieunguoimay.vuduydu.s10musicplayer.screens.adapters.StandardRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_home_screen.*

private const val TAG = "HOME_SCREEN_FRAGMENT"
class HomeScreenFragment : Fragment()
{

//    var fragmentChangeListener: FragmentChangeListener? = null
    lateinit var adapter: StandardRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG,"fragment created")
        // Inflate the layout for this fragment
        var v= inflater.inflate(R.layout.fragment_home_screen, container, false)
        initView(v)
        return v
    }

    private fun initView(v:View){
        v.findViewById<CardView>(R.id.cv_allSongs).setOnClickListener(clickListener)
        v.findViewById<CardView>(R.id.cv_folders).setOnClickListener(clickListener)
        val playlistView = v.findViewById<RecyclerView>(R.id.rv_playlist)

        setHasOptionsMenu(true)



        adapter = StandardRecyclerViewAdapter()
        playlistView.layoutManager = LinearLayoutManager(context)
        playlistView.itemAnimator = DefaultItemAnimator()
        playlistView.adapter = adapter


        adapter.infoList.clear()
        adapter.infoList.add(StandardRecyclerViewAdapter.RowInfo("Title","sub",null))
        adapter.infoList.add(StandardRecyclerViewAdapter.RowInfo("Title","sub",null))
        adapter.infoList.add(StandardRecyclerViewAdapter.RowInfo("Title","sub",null))
        adapter.infoList.add(StandardRecyclerViewAdapter.RowInfo("Title","sub",null))
        adapter.infoList.add(StandardRecyclerViewAdapter.RowInfo("Title","sub",null))
        adapter.infoList.add(StandardRecyclerViewAdapter.RowInfo("Title","sub",null))
        adapter.notifyDataSetChanged()


    }

    private val clickListener = View.OnClickListener{
        when(it.id){
            cv_allSongs.id->{
                (activity as HomeScreenActivity).replaceFragment(AllSongsFragment())
            }
            cv_folders.id->{
                (activity as HomeScreenActivity).replaceFragment(AllSongsFragment())
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater): Unit {
        inflater.inflate(R.menu.home_screen, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.action_search->{
                //search for it babe
                return true
            }
            android.R.id.home->{
                (activity as HomeScreenActivity).openDrawer()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
