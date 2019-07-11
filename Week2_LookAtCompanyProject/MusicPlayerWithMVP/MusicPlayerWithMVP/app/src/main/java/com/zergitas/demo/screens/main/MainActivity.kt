package com.zergitas.demo.screens.main

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import com.zergitas.demo.data.model.Song
import com.zergitas.demo.dialogs.ProgressDialog
import com.zergitas.demo.screens.BaseActivity
import com.zergitas.demo.screens.main.adapters.SongAdapter
import com.zergitas.demo.screens.playsong.PlaySongActivity
import com.zergitas.demo.utils.Contants
import com.zergitas.demo.utils.Utils
import com.zergitas.demo.utils.navigator.launcherActivity
import com.zergitas.demo.widgets.ItemDecoration
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : BaseActivity(), MainContract.View {
    val PERMISSION = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val REQUEST_CODE_PERMISSION = 100

    private lateinit var adapter: SongAdapter

    private lateinit var presenter: MainPresenter
    private lateinit var dialog: ProgressDialog
    private var songs: ArrayList<Song>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.zergitas.demo.R.layout.activity_main)
        presenter = MainPresenter(baseContext)
        presenter.setView(this)
        checkPermission()
    }

    /**
     * check permission
     */
    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val p = Utils.checkPermission(baseContext, PERMISSION)
            if (p != null) {
                requestPermissions(p, REQUEST_CODE_PERMISSION)
            } else setupView()
        } else setupView()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                finish()
            } else {
                setupView()
            }
        }
    }


    /**
     * setupview
     */
    private fun setupView() {
        dialog = ProgressDialog(this)
        initRecyclerViewSongs()
        presenter.getSongs()
    }

    /**
     * init recyclerview list songs
     */
    private fun initRecyclerViewSongs() {

        rc_songs.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        rc_songs.addItemDecoration(
            ItemDecoration(
                resources.getDimensionPixelSize(com.zergitas.demo.R.dimen._5sdp),
                0,
                resources.getDimensionPixelSize(com.zergitas.demo.R.dimen._8sdp),
                resources.getDimensionPixelSize(com.zergitas.demo.R.dimen._8sdp)

            )
        )
        adapter = SongAdapter(baseContext, object : SongAdapter.ItemSongListener {
            override fun onClick(pos: Int) {
                launcherActivity<PlaySongActivity> {
                    putParcelableArrayListExtra(
                        Contants.EXTRA_SONGS,
                        songs as ArrayList<Song>
                    )
                    putExtra(Contants.EXTRA_POSITION, pos)
                }
            }
        })
        rc_songs.adapter = adapter

    }

    /**
     *
     */






    //these functions will be called by the presenter for the purpose of getSongs()
    override fun showProgressBar() {
        dialog.show()
    }

    override fun hiddenProgressBar() {
        dialog.dismiss()
    }

    override fun showSongs(songs: List<Song>) {
        this.songs = ArrayList()
        for (song in songs) {
            this.songs!!.add(song.copyObject())
        }
        adapter.updateSongs(songs)
    }

    override fun updateSongs(pair: Pair<Int, Song>) {
        adapter.updateSongs(pair.first, pair.second)
    }

    override fun showErrorSongs() {
    }





}
