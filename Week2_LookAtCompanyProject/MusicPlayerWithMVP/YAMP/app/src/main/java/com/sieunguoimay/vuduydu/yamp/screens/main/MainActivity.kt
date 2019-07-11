package com.sieunguoimay.vuduydu.yamp.screens.main

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Parcel
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.RemoteViews
import com.sieunguoimay.vuduydu.yamp.R
import com.sieunguoimay.vuduydu.yamp.data.model.Song
import com.sieunguoimay.vuduydu.yamp.screens.main.adapters.ProgressBarThread
import com.sieunguoimay.vuduydu.yamp.screens.main.adapters.SongAdapter
import com.sieunguoimay.vuduydu.yamp.screens.playsong.PlaySongActivity
import com.sieunguoimay.vuduydu.yamp.screens.playsong.services.PlaySongService
import com.sieunguoimay.vuduydu.yamp.screens.playsong.services.SongAction
import com.sieunguoimay.vuduydu.yamp.utils.Constants
import com.sieunguoimay.vuduydu.yamp.utils.Utils
import com.sieunguoimay.vuduydu.yamp.widgets.ItemDecoration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_play_song.*

class MainActivity : AppCompatActivity(), MainContract.View  {
    val PERMISSION = arrayOf(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )
    val REQUEST_CODE_PERMISSION = 100
    val PLAY_SONG_REQUEST_CODE = 101

    private lateinit var presenter: MainPresenter

    private var songs: ArrayList<Song>?=null
    private lateinit var adapter: SongAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(baseContext)
        presenter.setView(this)
        checkPermission()
    }

    private fun checkPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val p = Utils.checkPermission(baseContext,PERMISSION)
            if(p!=null){
                requestPermissions(p, REQUEST_CODE_PERMISSION)
            }else{
                setupView()
            }
        }else
            setupView()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == REQUEST_CODE_PERMISSION){
            if(grantResults.isEmpty()||grantResults[0]!= PackageManager.PERMISSION_GRANTED){
                finish();
            }else{
                setupView()
            }
        }
    }
    private fun setupView(){
        initRecyclerView()
        presenter.getSongs()
        bar_playing.visibility = android.view.View.GONE

        bar_playing.setOnClickListener{
            val intent = Intent(this,PlaySongActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.putExtra(Constants.EXTRA_OPEN_FROM_NOTI,true)
            startActivityForResult(intent,PLAY_SONG_REQUEST_CODE)
        }
    }

    private fun initRecyclerView(){
        rc_songs.layoutManager = LinearLayoutManager(baseContext,LinearLayoutManager.VERTICAL,false)
        rc_songs.addItemDecoration(
            ItemDecoration(
                0,
                0,
                resources.getDimensionPixelSize(R.dimen._8sdp),
                resources.getDimensionPixelSize(R.dimen._8sdp)
            )
        )
        adapter = SongAdapter(baseContext,object: SongAdapter.ItemSongListener{
            override fun onClick(pos: Int) {
                if(pos == 0){

                }else{
                    val intent = Intent(this@MainActivity.baseContext,PlaySongActivity::class.java)
                    intent.putParcelableArrayListExtra("songs",songs)
                    intent.putExtra("position",pos+1)
                    startActivityForResult(intent,PLAY_SONG_REQUEST_CODE)
                }
            }
        })
        rc_songs.adapter = adapter
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode ==  PLAY_SONG_REQUEST_CODE&&resultCode== Activity.RESULT_OK){
            bar_playing.visibility = android.view.View.VISIBLE
            tv_bar_songName.setText(data?.getParcelableExtra<Song>("song")?.name)
        }
    }




    override fun showSongs(songs: List<Song>) {
        this.songs = ArrayList()
        this.songs!!.add(Song(1,1,"","","",null,1,1,1))
        for(song in songs){
            this.songs!!.add(song.copyObject())
        }
        adapter.updateSongs(songs)
    }

    override fun updateSongs(pair: Pair<Int,Song>) {
        adapter.updateSongs(pair.first,pair.second)
    }



    override fun showProgressBar() {
    }

    override fun hideProgressBar() {
    }



    override fun showErrorSongs() {
    }





}
