package com.sieunguoimay.vuduydu.yamp.screens.playsong

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import android.widget.RemoteViews
import com.sieunguoimay.vuduydu.yamp.R
import com.sieunguoimay.vuduydu.yamp.data.model.Song
import com.sieunguoimay.vuduydu.yamp.screens.playsong.services.PlaySongService
import com.sieunguoimay.vuduydu.yamp.screens.playsong.services.SongAction.Companion.NEXT
import com.sieunguoimay.vuduydu.yamp.screens.playsong.services.SongAction.Companion.PLAY
import com.sieunguoimay.vuduydu.yamp.screens.playsong.services.SongAction.Companion.PREV
import com.sieunguoimay.vuduydu.yamp.utils.Constants

const val TAG = "PlaySongNotification"
class PlaySongNotification(
    var context:Context
){
    var notification: Notification?= null
    var remoteViews: RemoteViews? =null
    var pendingIntent: PendingIntent
    val CHANNEL_ID:String = "ID"
    val NOTIFICATION_ID = 100
    var foreground:Boolean = false
    init {
        Log.d(TAG,"notification created")

        val intent = Intent(context,PlaySongActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.putExtra(Constants.EXTRA_OPEN_FROM_NOTI,true)
        val uniqueInt = (System.currentTimeMillis() and 0xfffffff).toInt()
        pendingIntent=PendingIntent.getActivity(context,uniqueInt,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        createNotificationChannel()
    }
    fun startForeground(service:Service){
        if(notification!=null){
            service.startForeground(NOTIFICATION_ID,notification)
            foreground = true
        }
    }

    fun stopForeground(service: Service){
        service.stopForeground(false)
        foreground = false
    }

    fun update(context:Context,song:Song, state:Boolean, thumb: Bitmap?, position:Int){
        Log.d(TAG,"notification updated "+position+" state "+state)

        if(remoteViews==null) {
            remoteViews = RemoteViews(context.packageName, R.layout.playsong_notification_layout)
            remoteViews?.setOnClickPendingIntent(R.id.iv_noti_next,createPendingIntent(context,NEXT))
            remoteViews?.setOnClickPendingIntent(R.id.iv_noti_prev,createPendingIntent(context,PREV))
            remoteViews?.setOnClickPendingIntent(R.id.iv_noti_state,createPendingIntent(context, PLAY))
        }
        remoteViews?.setTextViewText(R.id.tv_noti_songName,song.name)
        remoteViews?.setImageViewResource(R.id.iv_noti_thumb,R.drawable.ic_song)


        if(state){
            remoteViews?.setImageViewResource(R.id.iv_noti_state,R.drawable.ic_pause)
        }else{
            remoteViews?.setImageViewResource(R.id.iv_noti_state,R.drawable.ic_play)
        }
        if(thumb!=null) remoteViews?.setImageViewBitmap(R.id.iv_noti_thumb,thumb)
        else remoteViews?.setImageViewResource(R.id.iv_noti_thumb,R.drawable.ic_song)

        val notificationBuilder = NotificationCompat.Builder(context,CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setCustomBigContentView(remoteViews)
            .setAutoCancel(false)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)

        notification=notificationBuilder.build()

        with(NotificationManagerCompat.from(context)){
            notify(NOTIFICATION_ID,notification!!)
        }
    }




    private fun createPendingIntent(context:Context,action:String):PendingIntent{
        val intent = Intent(context, PlaySongService::class.java)
        intent.action = action
        return PendingIntent.getService(context,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}