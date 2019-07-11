package com.sieunguoimay.vuduydu.yamp.screens.playsong.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import android.widget.Toast
import com.sieunguoimay.vuduydu.yamp.R
import com.sieunguoimay.vuduydu.yamp.data.model.Song
import com.sieunguoimay.vuduydu.yamp.screens.playsong.PlaySongNotification
import com.sieunguoimay.vuduydu.yamp.screens.playsong.interfaces.LoadingThumbCallback
import com.sieunguoimay.vuduydu.yamp.screens.playsong.interfaces.SongInfoCallback
import com.sieunguoimay.vuduydu.yamp.screens.playsong.services.SongAction.Companion.CREATE_NOTIFICATION
import com.sieunguoimay.vuduydu.yamp.screens.playsong.services.SongAction.Companion.NEXT
import com.sieunguoimay.vuduydu.yamp.screens.playsong.services.SongAction.Companion.PLAY
import com.sieunguoimay.vuduydu.yamp.screens.playsong.services.SongAction.Companion.PREV
import com.sieunguoimay.vuduydu.yamp.screens.playsong.tasks.LoadThumbAsyncTask
import java.io.File

const val TAG = "PlaySongService"

class PlaySongService: Service(), MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener,
    LoadingThumbCallback{

    var position:Int = 0
    var songs:ArrayList<Song>? = null
    var songInfoCallback:SongInfoCallback<Song>? = null
    var thumb: Bitmap? = null

     var mediaPlayer: MediaPlayer? = null
    var captureSize:Int?=null
    private var iBinder = ServiceBinder()

    var notification: PlaySongNotification? = null

    override fun onCreate(){
        Log.i("SERVICE", "Service created")
        notification = PlaySongNotification(this)
    }

//    //implementing onStartCommand means that you have to call stopSelf or stopService() to stop it
//    //not implementing this method means the service is distroyed when nothing is bound to it
    @SuppressLint("ByteOrderMark")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("SERVICE", "Service onStartCommand")
        if(intent!=null)
            receiveIntent(intent)
        return START_STICKY
    }

    //implement this function to communicate with components
    override fun onBind(intent: Intent?): IBinder? {
        Log.i("SERVICE", "Service OnBind")
        return iBinder
    }


    override fun onDestroy(){
        Toast.makeText(baseContext,"Service onDestroy",Toast.LENGTH_SHORT).show()
        Log.i("SERVICE","Service onDestroy")
        stopSong()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mp?.start()
        updateNotification()
        startPlaySongForegroundService()
    }

    override fun onCompletion(mp: MediaPlayer?) {
        playNextSong()
        //updateNotification()
    }

    inner class ServiceBinder: Binder(){
        fun getService(): PlaySongService = this@PlaySongService
    }



    fun changeSongPlayingState(){
        if(mediaPlayer!!.isPlaying) {
            mediaPlayer?.pause()
            if(notification?.foreground!!)
                notification?.stopForeground(this)
        }else {
            mediaPlayer?.start()
            startPlaySongForegroundService()
        }
        songInfoCallback?.updateSongState(mediaPlayer!!.isPlaying)
        updateNotification()
    }

    //usage functions
    fun playSong(){

        LoadThumbAsyncTask(baseContext,this@PlaySongService).execute(songs!![position].albumId)
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            stopSong()
        }


        val songUri: Uri = Uri.fromFile(File(songs!![position]?.path))
        mediaPlayer = MediaPlayer().apply{
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setDataSource(applicationContext, songUri)
            prepareAsync()
            setOnPreparedListener(this@PlaySongService)
            setOnCompletionListener(this@PlaySongService)
        }
        songInfoCallback?.updateSongInfo(songs!![position])
        songInfoCallback?.updateSongState(true)
    }
    fun stopSong(){
        mediaPlayer?.stop()
    }
    fun playNextSong(){
        position++
        if(position==songs?.size)
            position = 0
        stopSong()
        playSong()
    }
    fun playPrevSong(){
        position --
        if(position==-1)
            position = songs?.size!!-1
        stopSong()
        playSong()
    }


    //some funtions that serve for the need of the activity onCreate
    fun getCurrentSong(): Song?{
        return songs!![position]
    }
    fun isPlaying() = mediaPlayer?.isPlaying
    fun getCurrentTimePosition() = mediaPlayer?.currentPosition


    //load thumb art
    override fun onSuccess(bitmap: Bitmap) {
        thumb = bitmap
        songInfoCallback?.updateSongThumb(bitmap)
    }

    override fun onFail() {
        songInfoCallback?.updateSongThumb(null)
    }





    private fun receiveIntent(intent:Intent){
        when(intent.action){
//            CREATE_NOTIFICATION ->
//                startForegroundService()
            PLAY ->{
                changeSongPlayingState()
            }
            NEXT->playNextSong()
            PREV->playPrevSong()
        }
    }





    fun startPlaySongForegroundService(){
        if(!notification?.foreground!!)
            notification?.startForeground(this)
    }

    private fun updateNotification(){
        notification?.update(this,songs!![position],mediaPlayer?.isPlaying!!,thumb,position)
    }

}
