package com.zergitas.demo.screens.playsong

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.view.View
import com.zergitas.demo.R
import com.zergitas.demo.data.model.Song
import com.zergitas.demo.screens.BaseActivity
import com.zergitas.demo.screens.playsong.interfaces.CallbackSong
import com.zergitas.demo.screens.playsong.service.ActionSong.Companion.CREAT_NOTI
import com.zergitas.demo.screens.playsong.service.SongService
import com.zergitas.demo.screens.playsong.service.SongService.BinderService
import com.zergitas.demo.utils.Contants
import com.zergitas.demo.utils.helper.formatTime
import kotlinx.android.synthetic.main.activity_play_song.*


class PlaySongActivity : BaseActivity(), PlaySongContract.View {
    private val TIME_DELAY:Long=10
    private var service: SongService? = null
    private var isBound = false

    private var songHandler: Handler? = null
    private var songRunnable: Runnable? = null
    private var onStop = false
    private lateinit var presenter: PlaySongPresenter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_song)
        presenter = PlaySongPresenter(this)
        presenter.setView(this)
        setupView()
    }
    /**
     * setupview
     */
    private fun setupView() {
        actionClick()
    }

    /**
     * action click
     */
    private fun actionClick() {
        img_play.setOnClickListener(onClick)
        img_next.setOnClickListener(onClick)
        img_previous.setOnClickListener(onClick)
        img_back.setOnClickListener(onClick)
        img_loop.setOnClickListener(onClick)

    }

    private val onClick = View.OnClickListener { v ->
        when (v?.id) {
            img_play.id -> {
                service?.changeStatusSong()
            }
            img_next.id -> {
                service?.playNextSong()
            }
            img_previous.id -> {
                service?.playPreviousSong()
            }
            img_back.id -> {
                finish()
            }img_loop.id -> {
                presenter.onUpdateLoopSong()
            }
        }
    }














    override fun onStart() {
        super.onStart()
        startService()
    }



    /**
     *start bound Service
     */
    private fun startService() {
        val intent = Intent(this, SongService::class.java)
        startService(intent)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            val binder = iBinder as BinderService
            service = binder.getService()
            service!!.callback = callbackSong
            presenter.start()
            if (!isOpenFromNoti() && !onStop) {
                service!!.position = getIndexCurrent()
                service!!.songs = getSongs()
                service!!.playSong()
                isBound = true
            } else {
                callbackSong.updateInfoSong(service!!.getCurrentSong()!!)
                callbackSong.updateThumbSong(service!!.thumb)
                callbackSong.updateStatusSong(service!!.isSongPlaying()!!)
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            isBound = false
        }
    }

    override fun getSongs(): ArrayList<Song> = intent.getParcelableArrayListExtra(Contants.EXTRA_SONGS)
    override fun getIndexCurrent() = intent.getIntExtra(Contants.EXTRA_POSITION, 0)
    override fun isOpenFromNoti() = intent.getBooleanExtra(Contants.EXTRA_OPEN_FROM_NOTI, false)
    override fun showLoopSetting(isLoop: Boolean) {
        if (isLoop) {
            img_loop.setImageResource(R.drawable.loop)
        } else {
            img_loop.setImageResource(R.drawable.ic_loop_grey)
        }
        service!!.updateSettingLoopSong(isLoop)
    }




    private val callbackSong = object : CallbackSong<Song> {
        override fun updateInfoSong(data: Song) {
            tv_name_song.text = data.name
            tv_name_singer.text = data.singer
            tv_time.text = data.duration.formatTime()
            sb_time.max = data.duration.toInt()
            sb_time.progress = 0
        }

        override fun updateStatusSong(status: Boolean) {
            if (status) {
                img_play.setImageResource(R.drawable.ic_pause)
            } else {
                img_play.setImageResource(R.drawable.ic_play)
            }
        }

        override fun updateThumbSong(thumb: Bitmap?) {
            if (thumb == null) {
                img_thumb.setImageResource(R.drawable.ic_song)
            } else {
                img_thumb.setImageBitmap(thumb)
            }
        }
    }









    override fun onResume() {
        super.onResume()
        handlerSong()
    }

    /**
     * handler song
     */
    private fun handlerSong() {

        //this is a thread required for updating song's info. since the song is running on background
        //it requires a new thread to be used for getting data
        songHandler = Handler()
        songRunnable = object : Runnable {
            override fun run() {
                try {
                    if (service != null && service!!.getCurrentPosition() != null) {
                        sb_time.progress = service!!.getCurrentPosition()!!
                        tv_current.text = sb_time.progress.toLong().formatTime()
                    }
                } catch (e: Exception) {
                }
                songHandler?.postDelayed(this, TIME_DELAY)
            }
        }

        songHandler!!.postDelayed(songRunnable, TIME_DELAY)
    }










    //on suddenly change of the state
    //we destroy the songHandler if running
    override fun onPause() {
        super.onPause()
        songHandler?.removeCallbacksAndMessages(null)
    }



    override fun onStop() {
        super.onStop()
        this.onStop = true
        val intentStart = Intent(this, SongService::class.java)
        intentStart.action = CREAT_NOTI
        startService(intentStart)
        if (isBound) {
            unbindService(serviceConnection)
        }
    }




}
