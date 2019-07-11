package com.sieunguoimay.vuduydu.s10musicplayer.screens

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.sieunguoimay.vuduydu.s10musicplayer.R
import com.sieunguoimay.vuduydu.s10musicplayer.models.data.Song
import com.sieunguoimay.vuduydu.s10musicplayer.screens.adapters.SongSelectRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_song_select.*

private const val TAG = "SONG_SELECT_ACTIVITY"
class SongSelectActivity : AppCompatActivity()
    , SongSelectRecyclerViewAdapter.SongSelectListener{

    val selectedSongs = ArrayList<Song>()
    val selectedMap = LinkedHashMap<Int, Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_select)


        val songList = intent.getParcelableArrayListExtra<Song>("songList")

        rv_song_select.layoutManager = LinearLayoutManager(this)
        rv_song_select.itemAnimator = DefaultItemAnimator()
        rv_song_select.adapter = SongSelectRecyclerViewAdapter(this,songList, this)

        cv_song_select_ok.setOnClickListener(listner)
        cv_song_select_ok.setOnClickListener(listner)
    }

    override fun onItemClick(holder: SongSelectRecyclerViewAdapter.SongSelectViewHolder, item:Int, song:Song) {
        Log.d(TAG,"Song selected "+song.title)
        //press one time we add it to the list
        // press the second time we remove it from the list.
        if(selectedMap[item]==null){
            selectedMap[item] = -1
        }

        if(selectedMap[item]==-1){
            selectedSongs.add(song)
            selectedMap[item] = selectedSongs.size-1
            holder.background.setBackgroundColor(ContextCompat.getColor(this,R.color.colorAccent))
            holder.selectState.setImageResource(R.drawable.ic_check_box_24dp)
        }else{

            selectedSongs.removeAt(selectedMap[item]!!)
            selectedMap[item] = -1
            holder.background.setBackgroundColor(ContextCompat.getColor(this,R.color.colorAbsoluteTransparency))
            holder.selectState.setImageResource(R.drawable.ic_crop_din_24dp)
        }
    }



    val listner = object: View.OnClickListener{
        override fun onClick(v: View?) {
            when(v!!.id){
                R.id.cv_song_select_cancel->{
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
                R.id.cv_song_select_ok->{
                    val intent = Intent()
                    intent.putParcelableArrayListExtra("selectedSongs",selectedSongs)
                    setResult(Activity.RESULT_OK,intent)
                    finish()
                }
            }
        }
    }

}


