package com.sieunguoimay.vuduydu.s10musicplayer.screens.HomeScreenActivity

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.IBinder
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.view.GravityCompat
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.sieunguoimay.vuduydu.s10musicplayer.R
import kotlinx.android.synthetic.main.activity_home_screen.*
import kotlinx.android.synthetic.main.app_bar_home_screen.*
import android.view.*
import com.sieunguoimay.vuduydu.s10musicplayer.screens.HomeScreenActivity.OptionsScreenFragment.HomeScreenFragment
import com.sieunguoimay.vuduydu.s10musicplayer.services.MusicPlayerService
import com.sieunguoimay.vuduydu.yamp.data.model.Song
import kotlinx.android.synthetic.main.player_bar_home_screen.*
import android.widget.FrameLayout
import android.widget.SeekBar
import com.sieunguoimay.vuduydu.s10musicplayer.screens.HomeScreenActivity.AllSongsScreenFragment.AllSongsFragment
import com.sieunguoimay.vuduydu.s10musicplayer.screens.adapters.SongRecyclerViewAdapter
import com.sieunguoimay.vuduydu.s10musicplayer.screens.adapters.StandardRecyclerViewAdapter
import com.sieunguoimay.vuduydu.s10musicplayer.tasks.ProgressBarThread


private const val TAG = "HOME_SCREEN_ACTIVITY"
private const val REQUEST_PERMISSION_CODE = 1

class HomeScreenActivity : AppCompatActivity()
    , NavigationView.OnNavigationItemSelectedListener
    , MusicLoadingContract.View
    , MusicPlayerService.UpdateViewCallback
    , StandardRecyclerViewAdapter.ItemClickListener<Pair<Int,Song>>
    , SeekBar.OnSeekBarChangeListener
    , FavouriteSongContract.View
{


    private var fragmentCount:Int = 0
    private var presenter: MusicLoadingContract.Presenter
    private var favouriteSongPresenter = FavouriteSongPresenter(this)
    private var service:MusicPlayerService? =null
    var songList = ArrayList<Song>()//the one and only songlist

    //this songAdapter is not used in this activity, but in other fragment.
    //we put it here so that it will be created once. not twice...
    //and the listviews that are on other fragments want to display songs , just set their adapter to this
    //and booom!! it is there. :))))
    var adapter: SongRecyclerViewAdapter


    private var songListGivenByService:Boolean = false
    private lateinit var bottomSheet:BottomSheetBehavior<FrameLayout>

    private var aCopyOfCurrentSongIndexToCarryWithinThisClass:Int = 0
    init{
        presenter = MusicLoadingPresenter(this,this)
        adapter = SongRecyclerViewAdapter(this, songList)
        favouriteSongPresenter.view = this
    }




    //here we initialize everything once and for all

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)


        //why do we call this function in onResume?
        //because the activity may had not stopped??? -> that means we should make sure that
        //we unbind it to the service only when we are destroyed. sothat the next time this activity is triggered tot start
        //it will encounter the onCreate event. and that is where we should call this initServiceIfExist()
        songListGivenByService = initServiceIfExist()

        checkPermissions()

        initView()

    }

    //now we want to provide something for debugging purpose
    //we want to bind to the service on resume and unbind it on stop
    /// which is parallel with the same stuff in onCreate and onDestroy
    private var stopped:Boolean = false

    override fun onResume() {
        super.onResume()
        if(stopped)
            initServiceIfExist()

    }
    override fun onStop() {
        //checking for binding already inside this function
        unbindServiceIfExist()
        stopped = true
        super.onStop()
    }


    override fun onDestroy() {
        unbindServiceIfExist()
        super.onDestroy()
    }




    private fun checkPermissions(){
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            !=PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(this, Array(1){android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CODE)
            }else{
                ActivityCompat.requestPermissions(this, Array(1){android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CODE)
            }
        }else{
            onPermissionGranted()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            REQUEST_PERMISSION_CODE->{
                if(grantResults.size>0&&grantResults[0]==PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED){
                        Log.d(TAG,"Permission of reading storage granted")

                        onPermissionGranted()
                    }
                }else{
                    Log.d(TAG,"Permission not granted")
                    finish()
                }
            }
        }

    }

    private fun onPermissionGranted(){
        //after the view is ready we run this. be cause the presenter will call this view for displaying something
        //which require the content view to be not null
        if(!songListGivenByService)
            presenter.loadSong(songList)

    }


    private fun initView(){
        setSupportActionBar(toolbar)
        nav_view.setNavigationItemSelectedListener(this)

        //init song adapter

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu_24dp)

        addHomeScreenFragment()

        cv_player_bar_play.setOnClickListener(clickListener)
        bt_state.setOnClickListener(clickListener)
        bt_next.setOnClickListener(clickListener)
        bt_prev.setOnClickListener(clickListener)
        bt_loop.setOnClickListener(clickListener)
        bt_equalizer.setOnClickListener(clickListener)
        cv_love.setOnClickListener(clickListener)
        cv_player_screen_playing_queue.setOnClickListener(clickListener)
        iv_hide_player_screen.setOnClickListener(clickListener)
        bottom_sheet_peek.setOnClickListener(clickListener)
        sb_player_screen_seek_bar.setOnSeekBarChangeListener(this)

        drawer_layout.setScrimColor(Color.TRANSPARENT)
        drawer_layout.addDrawerListener(object:ActionBarDrawerToggle(this, drawer_layout,R.string.open,R.string.close){
            override fun onDrawerSlide(drawerView:View, slideOffset:Float){
                super.onDrawerSlide(drawerView, slideOffset)
                val slideX =drawerView.width*slideOffset
                cl_home_screen.translationX = slideX
                cl_home_screen.scaleX = 1.0f - slideOffset/4
                cl_home_screen.scaleY = 1.0f - slideOffset/4
            }
        })

        bottomSheet = BottomSheetBehavior.from(fl_bottom_sheet)
        bottomSheet.setBottomSheetCallback(bottomSheetCallback)

    }


    private val bottomSheetCallback = object:BottomSheetBehavior.BottomSheetCallback(){
        override fun onSlide(p0: View, p1: Float) {
            Log.d(TAG,"BottomSheet: sliding up "+p1)
            ll_home_screen_fragment.visibility = View.VISIBLE

            abl_action_bar.y = -p1*abl_action_bar.height.toFloat()

            if(p1==0.0f) abl_action_bar.y = 0.0f

            bottom_sheet_peek.alpha = 1.0f-p1
            iv_hide_player_screen.alpha = p1

        }

        override fun onStateChanged(p0: View, p1: Int) {
            when(p1){
                BottomSheetBehavior.STATE_HIDDEN->
                    Log.d(TAG,"BottomSheet: state hidden")
                BottomSheetBehavior.STATE_COLLAPSED->{
                    Log.d(TAG,"BottomSheet: state collapsed")
                }
                BottomSheetBehavior.STATE_EXPANDED->{
                    Log.d(TAG,"BottomSheet: state expanded")
                    ll_home_screen_fragment.visibility = View.GONE

                }
            }
        }
    }

    //common use for all fragment having recyclerview
    val recyclerViewScrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val actionBar = abl_action_bar
            val llHomeScreenFragment = ll_home_screen_fragment

            if(dy>0){
                //scroll up
                if(actionBar.y>-actionBar.height) {
                    actionBar.y = actionBar.y-dy.toFloat()*.5f
                    llHomeScreenFragment.translationY = actionBar.y
                }else{
                    actionBar.y = -actionBar.height.toFloat()
                    llHomeScreenFragment.y = 0.0f
                }
            }else{
                //scroll down
                if(actionBar.y<0){
                    actionBar.y = actionBar.y-dy.toFloat()*.5f
                    llHomeScreenFragment.y = llHomeScreenFragment.y-dy.toFloat()*.5f
                }else{
                    actionBar.y = 0.0f
                    llHomeScreenFragment.y = actionBar.height.toFloat()
                }
            }
        }

    }


    private val clickListener = object:View.OnClickListener {
        override fun onClick(v: View?) {
            if(MusicPlayerService.serviceBound){
                if(v?.id==cv_player_bar_play.id||v?.id==bt_state.id) {
                    if(service?.musicPlayer!!.isPlaying)
                        service?.pause()
                    else {
                        //what if this button is clicked for the fist time?
                        //should I make sure that the first song will be zero and always displayed? everywhere?->No
                        //we should implicitly assume that the first song is zero
                        if (service?.currentSongIndex == -1)
                            playMusicInService(0)
                        else
                            playMusicInService(service?.currentSongIndex!!)
                    }
                }else {
                    when(v?.id) {
                        bt_next.id -> {
                            nextSongInService()
                        }
                        bt_prev.id -> {
                            prevSongInService()
                        }
                    }
                }
            }
            when(v?.id) {
                iv_hide_player_screen.id->{
                    bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
                }
                bottom_sheet_peek.id->{
                    bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                }
                cv_love.id->{
                    favouriteSongPresenter.like(songList[aCopyOfCurrentSongIndexToCarryWithinThisClass])
                }
            }
        }
    }

    //this is seekbar listener//////////////////////////////////////////////////////////
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if(MusicPlayerService.serviceBound&&fromUser)
            service?.setProgress(progress.toFloat()/sb_player_screen_seek_bar.max.toFloat())
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }
    //end of seek bar listener//////////////////////////////////////////////////////////

    override fun onItemClick(pair: Pair<Int,Song>) {
        playMusicInService(pair.first)
    }


    private fun addHomeScreenFragment(){
        val fragment = AllSongsFragment()//HomeScreenFragment()ll_home_screen_fragment
        //fragment.fragmentChangeListener = this
        supportFragmentManager.beginTransaction().add(R.id.ll_home_screen_fragment,fragment,"tag_home_screen_fragment").commit()
        fragmentCount = 1
    }



    //events that occur during application at loading only
    override fun onLoadedSongList() {
        Log.d(TAG,"Loaded all songs")
        adapter.notifyDataSetChanged()
    }

    override fun showErrorMessage() {
        Log.d(TAG,"Something went wrong with loading music")
    }

    override fun onLoadedThumbails() {
        Log.d(TAG,"Thumbail loaded")
        adapter.notifyDataSetChanged()
    }



    //override from service to update views

    override fun updateViewOnNewSong(song: Song) {
        super.updateViewOnNewSong(song)
        tv_player_peek_title.text = song.title
        tv_player_screen_title.text = song.title
        tv_player_screen_artist.text = song.artist
        if(song.thumb!=null){
            iv_player_bar_thumb.setImageBitmap(song.thumb)
            iv_player_screen_photo.setImageBitmap(song.thumb)
        }
    }

    override fun updateViewOnStateChange(state: Boolean) {
        if(state)
            iv_state.setImageResource(R.drawable.ic_pause)
        else
            iv_state.setImageResource(R.drawable.ic_play)

    }

    override fun updateProgressBar(progress: Float, maxTime:Int) {
        val p = progress*sb_player_screen_seek_bar.max

        pb_player_screen_progress_bar.progress = p.toInt()
        sb_player_screen_seek_bar.progress = p.toInt()

        val currentTime = (progress*maxTime.toFloat()).toInt()
        var m = currentTime/60000
        var s = (currentTime/1000)%60
        var time:String = ""+if(m<10){"0"+m}else{m}+":"+if(s<10){"0"+s}else{s}
        tv_time.text = time

        m = maxTime/60000
        s = (maxTime/1000)%60
        time = ""+if(m<10){"0"+m}else{m}+":"+if(s<10){"0"+s}else{s}
        tv_duration.text = time
    }




    //events that occur during application running

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_equalizer-> {

            }
            R.id.nav_sleep_timer-> {

            }
            R.id.nav_skin_theme-> {

            }
            R.id.nav_settings-> {

            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    //from fragments other than HomeScreenFragment, the call to this funtion is triggered
    //that means want to goback
    override fun onBackPressed(){

        if(bottomSheet.state == BottomSheetBehavior.STATE_EXPANDED){
            bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
            return
        }
        //these are fragments other than the HomeScreenFragment
        if(!popBackUptoHomeScreenFragment())
            super.onBackPressed()

    }

    //from fragment usage


    //from the HomeScreenFragment on the home OptionsItem is selected this function is called
    fun openDrawer(){
        if (drawer_layout.isDrawerOpen(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START)
        else
            drawer_layout.openDrawer(GravityCompat.START)
    }


    //favourite song implementation
    override fun updateIconOnLike() {

    }

    override fun updateIconOnUnlike() {
    }




    fun replaceFragment(fragment: Fragment) {
        var transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.ll_home_screen_fragment,fragment,fragment.toString())
        transaction.addToBackStack(fragment.toString())
        transaction.commit()

        fragmentCount++

        if (fragmentCount>1){
            //these are fragments other than the HomeScreenFragment
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp)
            Log.d(TAG,"More than one fragment "+fragmentCount)
        }
    }

    fun popBackUptoHomeScreenFragment():Boolean{
        if(fragmentCount>1){
            supportFragmentManager.popBackStack()
            fragmentCount--
            Log.d(TAG,"pop fragment "+fragmentCount)
            if(fragmentCount==1){
                //this is homeScreenFragment
                supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu_24dp)
                Log.d(TAG,"Only one fragment left in the stack")
            }
            return true
        }
        return false
    }





    //call this function on:
        //1. first time clicked on the song
        //2. click on play button on pause????
    //don't worry this function can only be called when the recyclerView was initialized. i.e songList was loaded
    fun playMusicInService(songIndex:Int){
        //how do we suppose to carry the songIndex to the boundEvent
        aCopyOfCurrentSongIndexToCarryWithinThisClass = songIndex

        Log.d(TAG,"playing music babe")
        if(!MusicPlayerService.serviceRunning){
            //here we start the service . after this point that service exists
            val serviceIntent = Intent(this,MusicPlayerService::class.java)
            startService(serviceIntent)
            //we immediately bind to that service
            bindService(serviceIntent,serviceConnection,BIND_AUTO_CREATE)

        }else{
            service?.play(songIndex)


            //here we can check for if the notification has been dismissed or not
            if(!MusicPlayerService.serviceStarted) {
                service?.initServiceObject(baseContext,songList!!)
                val serviceIntent = Intent(this, MusicPlayerService::class.java)
                startService(serviceIntent)
            }else{
                //otherwise, notification is still there, we have to start the foreground again only
            }
        }


    }

    fun nextSongInService(){
        var nextSongIndex = aCopyOfCurrentSongIndexToCarryWithinThisClass+1
        if(nextSongIndex>=songList!!.size)
            nextSongIndex -= songList!!.size
        playMusicInService(nextSongIndex)
    }

    fun prevSongInService(){
        var prevSongIndex = aCopyOfCurrentSongIndexToCarryWithinThisClass-1
        if(prevSongIndex <=-1)
            prevSongIndex  += songList!!.size
        playMusicInService(prevSongIndex )

    }
    //if the service is existing.. we don't need to load the songList from model again
    //just take a copy from the service's songList
    //and if the onServiceConnected arrived from here, dont initialize it again any more
    private fun initServiceIfExist():Boolean{
        //first instance is created here
        if(MusicPlayerService.serviceRunning){
            //bind to that service
            bindService(Intent(this,MusicPlayerService::class.java),serviceConnection,BIND_AUTO_CREATE)
            return true
        }
        return false
    }


    private val serviceConnection = object:ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG,"Service disconnected")
        }

        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            Log.d(TAG,"Start passing data from activity to service")

            //now we ready to communicate and use the public functions of Service
            //at this point service must be null, always
            if(service == null){
                service = (binder as MusicPlayerService.MusicPlayerBinder).getServiceInstance()
                service?.callback = this@HomeScreenActivity
            }

            if(!MusicPlayerService.serviceRunning){
                //come from playMusicInService()
                MusicPlayerService.serviceRunning = true
                //means we have to play music
                service?.initServiceObject(baseContext,songList!!)
                service?.play(aCopyOfCurrentSongIndexToCarryWithinThisClass)
            }else{
                //come from initServiceIfExist()
                //ask for the existing songList here
                songList = service?.songList!!
                adapter = SongRecyclerViewAdapter(this@HomeScreenActivity, songList)
            }

            MusicPlayerService.serviceBound = true
        }
    }


    //we unbind the service on Stop
    private fun unbindServiceIfExist(){
        //if any song has been played since this activity started
        //that mean we had bound to the music player service
        //now we have to check if the singleton of that service say it was started
        if(MusicPlayerService.serviceRunning&&MusicPlayerService.serviceBound){
            Log.d(TAG,"Trying to disconnect with the service")

            //if the service has paused the mediaplayer then
//            if(service?.isPaused()!!) service?.stopForeground()

            unbindService(serviceConnection)
            MusicPlayerService.serviceBound = false
            //null so that next time init you will create new service instance again
            service = null
        }
    }

}
//                        toolbar.animate()
//                            .translationY(-toolbar.height.toFloat())
//                            .setListener(object : AnimatorListenerAdapter() {
//                                override fun onAnimationEnd(animation: Animator) {
//                                    super.onAnimationEnd(animation)
////                                    toolbar.visibility = View.GONE
//                                    toolbar.tag = "GONE"
//                                }
//                            })