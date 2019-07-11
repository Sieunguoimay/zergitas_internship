package com.sieunguoimay.vuduydu.yamp.screens.playsong

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.sieunguoimay.vuduydu.yamp.data.model.Song
import com.sieunguoimay.vuduydu.yamp.screens.main.adapters.ProgressBarThread
import com.sieunguoimay.vuduydu.yamp.screens.playsong.interfaces.SongInfoCallback
import com.sieunguoimay.vuduydu.yamp.screens.playsong.services.PlaySongService
import com.sieunguoimay.vuduydu.yamp.utils.Constants
import com.sieunguoimay.vuduydu.yamp.utils.helpers.scaleAndCropCenter
import com.sieunguoimay.vuduydu.yamp.widgets.CanvasView
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_play_song.*
import kotlin.collections.ArrayList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.audiofx.Visualizer
import android.os.*
import com.sieunguoimay.vuduydu.yamp.R
import com.sieunguoimay.vuduydu.yamp.utils.Utils
import com.sieunguoimay.vuduydu.yamp.widgets.WaveformView
import java.lang.IllegalStateException


class PlaySongActivity : AppCompatActivity(), PlaySongContract.View, SongInfoCallback<Song>
    , ProgressBarThread.ProgressCallback
    , WaveformView.WaveformRenderer


{

    val REQUEST_CODE_PERMISSION = 10011
    val PERMISSIONS = arrayOf(
        android.Manifest.permission.RECORD_AUDIO,
        android.Manifest.permission.MODIFY_AUDIO_SETTINGS
    )
    val CAPTURE_SIZE = 256

    private var service:PlaySongService? = null
    private var bound:Boolean = false
    private var stopped:Boolean = false
    var progressBarThread: ProgressBarThread

    lateinit var canvas: CanvasView
    var canvasReady:Boolean = false

    private var visualizer: Visualizer? = null
    init{
        progressBarThread = ProgressBarThread()
        progressBarThread.callback = this
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_song)
        initView()
    }
    private fun initView() {
        val clickListener = View.OnClickListener {
            if(bound){
                when(it?.id){
                    bt_state.id-> {
                        service?.changeSongPlayingState()
                    }
                    bt_loop.id->{
                        Log.d("PlaySongActivity", "Change loop")
                    }
                    bt_next.id->{
                        Log.d("PlaySongActivity", "Next Song")
                        service?.playNextSong()
                    }
                    bt_prev.id-> {
                        Log.d("PlaySongActivity", "Prev Song")
                        service?.playPrevSong()
                    }
                    bt_back.id->{
                        var intent = Intent()

                        intent.putExtra("song",service?.getCurrentSong())
                        intent.putExtra("state",service?.isPlaying())
                        setResult(Activity.RESULT_OK,intent)
                        finish()
                    }
                }
            }
        }
        bt_state.setOnClickListener(clickListener)
        bt_loop.setOnClickListener(clickListener)
        bt_next.setOnClickListener(clickListener)
        bt_prev.setOnClickListener(clickListener)
        bt_back.setOnClickListener(clickListener)
//        canvas_playsong.setRenderer(this)
//        setupAudioVisualizer()
        checkPermission()
    }


    override fun onStart(){
        super.onStart()
        // call this function one time here only. never call it anywhere else
        startPlaySongService()
    }

    private fun startPlaySongService(){
        var intent = Intent(this, PlaySongService::class.java)
        startService(intent)
        bindService(intent, serviceConnection , Context.BIND_AUTO_CREATE)
    }


    //created when the service is bind to the activity
    private var serviceConnection = object: ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
        }

        override fun onServiceConnected(name: ComponentName?, iBinder: IBinder?) {
            val binder = iBinder as PlaySongService.ServiceBinder
            service = binder.getService()
            service!!.songInfoCallback = this@PlaySongActivity


            if(stopped||intent.getBooleanExtra(Constants.EXTRA_OPEN_FROM_NOTI,false)){
                updateSongInfo(service!!.getCurrentSong()!!)
                updateSongState(service!!.isPlaying()!!)
                updateSongThumb(service!!.thumb)
            }else{
                service!!.songs = getSongs()
                service!!.position = getCurrentIndex()
                service!!.playSong()
            }

            bound= true
            stopped =false;
            Log.d("PlaySongActivity","Service bound. Now we can communicate")
        }
    }

    override fun onResume() {
        super.onResume()
        progressBarThread = ProgressBarThread()
        progressBarThread.callback = this
        progressBarThread.start()
//        audioVisualization.onResume()
    }

    override fun onPause() {
        progressBarThread.interrupt()
//        audioVisualization.onPause()
        visualizer?.release()

        super.onPause()
    }

    //when pause, we stop the service for safety and for debugging
    //after everything works fine. we then allow the service to run in the foreground with notification
    //at that time we don't stop it here anymore.
    override fun onStop(){
        super.onStop()
        if(bound) {
            unbindService(serviceConnection)
            bound = false
        }
        stopped = true
//        audioVisualization.release()
    }

    //these function is called from somewhere I dont even know
    override fun getSongs(): ArrayList<Song> = intent.getParcelableArrayListExtra("songs")
    override fun getCurrentIndex(): Int = intent.getIntExtra("position", 0)
    override fun isOpenedFromNotification()= intent.getBooleanExtra("from_where",false)
    override fun displayLoopingStatus(isLoop: Boolean) {
        if(isLoop)
            iv_loop.setImageResource(R.drawable.ic_loop_grey)
        else
            iv_loop.setImageResource(R.drawable.loop)
    }


    //these following functions are called from the playSongService
    //whenever you click a button, that button event trigger the function within the PlaySongService
    //and PlaySongService will decide when to call back these...
    override fun updateSongInfo(data: Song) {
        tv_songName.setText(data.name)
//        tv_singer.setText(SimpleDateFormat("yyy.MM.dd HH:mm").format(Date(data.date)))
//        tv_path.setText(data.path)
        val m = data.duration/60000
        val s = (data.duration/1000)%60
        tv_maxTime.setText(""+if(m<10){"0"+m}else{m}+":"+if(s<10){"0"+s}else{s})

        //when the mediaplayer is started to play
//        if(visualizer==null&&visualizerOk)
//            visualizer?.release()
//            startVisualizer()
//        else{
        startVisualizer()
    }

    override fun updateSongState(status: Boolean) {
        if(status) {
            iv_state.setImageResource(R.drawable.ic_pause)
        }else
            iv_state.setImageResource(R.drawable.ic_play)
    }

    override fun updateSongThumb(thumb: Bitmap?) {
        if(thumb==null) {
            iv_thumb.setImageResource(R.drawable.ic_song)
            iv_blur_thumb_background.setImageResource(R.drawable.bg_main)
        }else {
            iv_thumb.setImageBitmap(thumb)
            Blurry.with(this).from(thumb.scaleAndCropCenter(iv_blur_thumb_background.width,iv_blur_thumb_background.height)).into(iv_blur_thumb_background)
        }
    }
    override fun updateProgress():Float{
        var progress = 0.0f
        if(service!=null&&service?.songs!=null) {
            val maxTime = service?.getCurrentSong()?.duration
            val currentTime = service?.getCurrentTimePosition()
            var progress_a = (currentTime!!.toFloat()) / (maxTime!!.toFloat())
            progress = progress_a*100.0f
            pb_song_progress.setProgress(progress.toInt())
            val m = currentTime/60000
            val s = (currentTime/1000)%60
            tv_currentTime.setText(""+if(m<10){"0"+m}else{m}+":"+if(s<10){"0"+s}else{s})

        }
        return progress
    }


    fun checkPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val p = Utils.checkPermission(baseContext,PERMISSIONS)
            if(p!=null){
                requestPermissions(p, REQUEST_CODE_PERMISSION)
            }else{
                visualizerOk = true
            }
        }else
            visualizerOk = true

//            startVisualizer()
    }

    var visualizerOk = false
    fun startVisualizer(){
//        if(visualizer!=null) {
//            visualizer?.release()
//            if(visualizer?.enabled!!)
//                visualizer?.setEnabled(false)
//        }
        visualizer = Visualizer(service?.mediaPlayer?.audioSessionId!!)

        visualizer?.setDataCaptureListener(object:Visualizer.OnDataCaptureListener{
            override fun onFftDataCapture(visualizer: Visualizer?, fft: ByteArray?, samplingRate: Int) {

            }

            override fun onWaveFormDataCapture(visualizer: Visualizer?, waveform: ByteArray?, samplingRate: Int) {
                if(waveform!=null){
                    waveform_view.updateWaveform(waveform)
                }
            }

        },Visualizer.getMaxCaptureRate(),true, false)

        visualizer?.captureSize = Visualizer.getCaptureSizeRange()[1]

        visualizer?.setEnabled(true)

        waveform_view.setRenderer(this)
    }

    override fun render(canvas:Canvas, waveform: ByteArray){
        val paint = Paint()
        paint.color = Color.RED
        canvas.restore()
        canvas.drawCircle(100.0f,100.0f,100.0f,paint)
    }






//    lateinit var audioVisualization:AudioVisualization
//
//    fun startVisualizer(){
//        audioVisualization = (visualizer_view as AudioVisualization)
//        audioVisualization.linkTo(DbmHandler.Factory.newSpeechRecognizerHandler(this))
//    }
}
