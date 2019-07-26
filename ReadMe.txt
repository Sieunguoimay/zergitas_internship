	 	_______________||________________
	 ______/				 \______
	|	      Zergitas Internsship 		|
	|______		   Log File		 _______|
	       \________________________________/

<26/07/2019> Fri
	+ One more bug remains. but still I cannot figure it out what behind the scene. Today I'll finish it. And go on for the next and last project. The Floating Back Button.


<15/07/2019> Mon (Week 6)
	+ What I am going to do today:

	after 5 weeks of intership. my product of S10 Music Player is about to finish. This week, I will fix some remaining bugs. adjust some unreasonable buttons. and then take the screenshots of the entire app. for every details. and write some comments on what seem to be confusing so that the designer team can understand and design it for a better looking. 

	Tomorrow, the app will be handed on to the tester for further bug testing/finding.

<11/07/2019> Thu
	+ What I'm gonna do today:
		+ fix the bugs: on restart the main activity while the service is still running pressing the play button always leads to playing of new song not the current song at the current moment. that means the song always pass the condition of playing new song even though it should be resumed only. Let's take a look at the condition that makes the song play new.

<10/09/2019> Wed
	+ Duong's Birthday. :))))

<09/07/2019> Tue
	+ What I am gonna do today:
		current playing song; Queue moving still has problem with position of the song in the service's queue; add the delete on drop down for queue song rows; for skin theme, create 2 theme mode: dark and light; Text moving on the player screen title; Adjust layout for the Album and Artist Song Display screen; scroll the queue to current position of playing song; Equalizer and Volumn control using earphone.

<08/07/2019> Mon
	+ What I am gonna do today:
		- fix remainning bugs: on queue changed - index problem; playlist selecting activity problem; Drawer width; Search sreen - open the keyboard on starting the screen; the playlist song display screen -> animation
		- select all
		- change some icon
		- drag and drop queue
		- sleep timer
		- setting
		- skin
<05/07/2019> Fri
	+ Album, Artist, ShowSongListListener, ShowSongListFragment

<04/07/2019> Thu
	+ Let's design the Playlist database:
		playlist_table: stores all the songs that belongs to any playlist. and each song keeps the playlist id of its own.
		

<03/07/2019> Wed
	+ only 3 days left for the project. S10 Music Player. It's time to make it right. Today: finish all the yesterday tasks. and 
		- load the thumbnail again on update view for the player screen so that the bitmap is bigger
		- shuffle feature: save the last played song. ->shared preference
		- Let me tell you a story: The service play a song based on the position index given to it and the available songlist kept from the beginning. now when a new kind of list such as favourite list is created. it keeps the song ids of the songs in the original list. and on one song gets selected from that new list. it will drive the index to the position in the songList by the means of songMap. At the same time, when a new list is played. the queue of playing will become that list. and that queue on display will show that list only. Moreover the queue current state will tell how the position index will move in the songList. while the shuffle feature has no effect on the list. it just give a random number of index. for the queue. that queue index will then be converted to songList index.
	-> Now how do we invent the playing queue. and the next song function.
<02/07/2019> Tue
	+ What we gonna do today:
		- finishing the favourite song feature: when loading a song. the liked attribute will be provided by the SDLite query with each and every loaded song. What about the list of loved song? when will you load it? at the beginning and then adding along with the like or unlike events??? or will you load it every time the user open the favourite song tab??? what would you choose????
however in the former one, to unlike take the searching in the list would be time comsumming.
one alternative option is to load the favourite song list again for every like / unlike event.-> i think this one is good.
		-> what about onload how do we know a song is liked or not??? how do we run the query for it in side the MusicLoadingModel???
		-> one solution for this is combine the two model into one. so that they have only one class. and share the usage of databaseHelper. -> that means one model. one presenter



		- some bugs: on restart the app from notification the player screen cannot be cliked with any button. at the same time. the song list is not showing up; The second bug is the swiping up event while the tablayout is at the top due to scrolling of the screen.

		- the thumbnail image on the drawer needs tobe shown
		- the icons of navigation items needs tobe changed.
		- the background color needs to be changed.
			#333745 #E63462 #FE5F55 #C7EFCF #EEF5DB

		- after all this, come to filters: albums, playlist, artist.

		- how do we suppose to design the playlist and how to save it to SQL
		
- save the last played song to the shared preference and make sure that the first song on first play button click is the song at position of zero.
		- love from notification
		- notification over lock screen
		- round corner thumbails
		 
<01/07/2019> Mon
	+ what we gonna do today:
		- remove the first screen fragment. and replace it by the allSongs fragment which has the tablayout. so. what will be the problem in doing this task: the song list will be displayed right after it is loaded. and hence require some kind of progress bar. however our adapter was put in the main activity it will receive no effect on changing of the fragments.
		- after we have remove that first screen fragment. we can add one more tab to the tabLayout say Playlists. And also. we have to replace navigation icon on the action bar.
		- Bottomsheet handles on back event to close itself
		- after that we jump to the tasks that require SDLite and shared preference.
		

		- shuffle, songs in queue
		- pointing to currently playing song in the list
		

		-> we have a problem with how to save the favourite songs????? if we save the entire song info into the Database, then we have to load two time for one song and they will be treated separatedly??? is that how we work???
	if we save the song id. that we had fetch from the memory is that id unique or is that changing after one or more songs is added or deleted. to answer for this question we should do some experiment-> we choose to believe the song id is unique. 
	-> we have two diffent things to do with Favourite song: first. when we click on the love button the main activity will send the song down to the favourite model for saving. next when we press the love button again, the song is removed from the database all using the song id which is unique for that song. and the second case is when wee load the favourite list for displaying. here we simply display them all. what about the situation when we play to the song that has been loved. ? shall we have to look for it in the database or shall we look for it in the list that we have from the begining. (maybe that list will be updated every time something changes with the database) and this list serves for the purpose of favourite song fragment

<28/06/2019> Fri
	+ what we gonna do today:best book on coco
		- thumbail loading and displaying in Fragments: recyclerView Lists, player screen; in Notification
		-> the thumbails will be loaded when the fetching of songs is occupied??? or where? one more place to load is on the binding of the recyclerView. at that point we have to access each song and we will call for loading. for the former case, I don't see it in the sample project. I think it because that helps the user experience in loading fast recyclerview. and display that list as soon as possible while the thumbails can be loaded latter -> done. let's follow this solution. No.
		-> one more problem arises. Since our app structure is different from the sample project. starting screen is not a list of songs having thumbails, but other thing (say, a list of playlists). and the songList is loaded on the initializing of the main activity. here we don't have any recyclerview to create at the first time. so the only loop throught the song list we have is at the loading point. So the former solution is considered on this situation. the loading loop will trigger the thumbail image loading thread (asynctask) for each and every thumbail it encounters. and the callback should be give to the presenter or someone who keeps the songlist for updating the data of the songs.
		-> in fact, it is not like that. the entire song list will be walk through one more time for loading the thumbail image in background. and it should be occupied after the songs are loaded.
		-> who will handle the event when the asynctask finishes? on that event the list will be triggered to refresh.-> the list is lying in the fragment of the main activity. what happens if we create the list during the running of asynctask??? i.e. open the song list fragment ???
		- working with progressbar and seekTo proper position.
		-> how does the progressbar work with media player: here one thread is running in parallel with the main ui thread. so that the view will change frequently. we have no place in the activity that loop again and again to look for the mediaplayer to update??->Ok, what about the position to put that implementation of the callback? if we put it in the mainactivity means we have to access the media player from there. and also we have to check if it exists or not at the moment. so why dont we put it in the service. where the mediaplayer is created we put start the thread. and when it is dismissed. we stop it altogether. and the callback will one more time call back to the main activity for usage.
this layout of callback make sure that the mediaplayer exist any times it calls back
		-> for controlling -> oh it is SeekBar
		
		- still the problem of coloring the toolbar. and shadow.
		- the player screen has not updated on musicplayer state changes yet
		- noitification is not fit in the display area

		SharedPreference required features:
		- working with loop button -> shared preference
		- working with loved song list. how do we suppose to generate a list of loved songs and display it permanently during the installed time of the app. it will stay there whenever the app is remaining in user's phone. -> Shared preference
		- playlist creating process: as described on <26/06/2019>
			-> shared prefercence


<27/06/2019> Thu
	+ what we have done:
		- create a concrete service that runs and stops with the right expectation. a notification that can be wiped away anytime that the music player is paused. 
	+ what we gonna do today:
		//- employ the mediaPlayer in the service. allow it to play the music with the existing interface. in the service.
		- customize the notification for having the player bar displayed on it. and it should be able to send action message back to the service by using intent.
		- create the callbacks for the activity to update info when the song states are changed. the same is for the notification.
			+ for the notification whenever something changed in the state, the entire notification is recreated
			+ for the activity: when state change then we change it. when the song info change then we change it.
		-> so how do we design the interface for common used
		there is two type of changes: song change and state change. where state includes: play or pause, love or not love. and current song position (this progress maybe in the callback from somewhere).
		-> callback is for the activity only. the notification only needs to be updated accordingly

		-> after this day. we have a proper music player that can play songs.
		-> what about thumbail.? when should it be loaded?
		-> where should it be used		


<26/06/2019> Wed
	+ today: swipe up to open the player at the bottom
		or touch to open the full screen player 
		this after noon, load all songs and show up at the
		song fragment's songLists
		the song list will be kept by the main activity
		
	+ what else I have to do:
		- search screen
		- play music service running in background and 
			the notification sends intents to control
			that service, while the main activity binds
			to that service for controlling media 
			and updating the displayed information
		- in the folder fragment, we display all the folders that contains songs and on clicked will open that folder showing the songs belonging to that folder only. same for albums and artists
		- for the playlist in the main screen fragment, when the button of add new is clicked, a dialog is popup to enter playlist name. on return of that dialog, we enter a new fragment where all songs are listed and there are tick box for selecting songs out of the list. one button on the top right to agree with the choice. and after that return the list of songs for that playlist to keep. and that playlist will be store in the shared preference. for future usage
		- it's time to create the music player service. On first song selected, we turn on the service. what does the service do? 1. it keeps the mediaPlayer alive in the background even the activity is distroyed. 2. it updates the media playing state to the players at player_bar, player_screen and player_notification and concurrently allows them to send back the control signals to the media player. 3. Moreover, it also work with some models that provide storing user information related to the mediaplayer such as the loop info.
		- songs in the queue
		- liked songs
		- playlist of songs

	+ let's design the interface of the music player in order to allow the View to access it.
what the user want to do

	play
	pause
	next
	prev
	change position

what the mediaplayer provide:
	start
	pause
	stop

-> how to map them together?
	play for the first time -> loadMusic + start
	pause -> pause
	play for the second time -> start
	next&prev -> stop + loadMusic + start
	change position -> pause -> set position-> start

<25/06/2019> Tue
	+ Fragment with actionBar Up button problem:
		At first, I assign the ActionBarDrawerToogle
		directly to the DrawerLayout, which then captures
		the entire up button in the activity across fragment
		-> the solution is to not use that actionBarDrawerToggle, but use the customize up button instead. then handle the onOptionsItemSelected by the android.R.id.home id.
however one problem arise. customizing the icon effects on the activity and hence, not changed from other fragment.
-> now what I'm gonna do is find out when the top fragment is not the homeScreenFragment. At the points where new fragment is pushed in and current fragment is popped out. By counting the current number of fragment in stack. we can know what is what. and change the button icon accordingly

	+ one more problem is that when the second fragment is entered for the second time. The fragment with in it is not loaded.
i.e one fragment contains some more fragments. when that parent fragment is attached to the container. it's component views can be shown, but no child fragments are loaded. when the event of changing fragment within that parent fragment is triggered, only then the child fragments are refreshed.
	->solution: getChildFragmentManager()
	because we are creating the adapter of the view pager inside the fragment not in the activity so we cannot use the supportFragmentManager. use getChildFragmentManager() instead.


<24/06/2019> Mon
	+ What to do:
		We have two weeks to finish the job. First week, we
		will be focusing on making the UI screens where we 
		can move among them and experience the real app having no
		content or media to play yet.

		From the second week. We start to create all the services
		to run the loaded song and all other kind of animation
		as well as other kind of controlling buttons.
		
		- Screens:
			- Home Screen Activity -> DrawerLayout
				- Fragment Options Screen	
				- Fragment All Songs Screen
					-> Songs fragment
					-> Folders fragment
					-> Albums fragment
					-> Artists fragment
				- Fragment Recent Played Screen
				- Fragment Playing Queue Screen
			- Search Screen	Activity
			- Play Screen Activity

		- Questions:
			- how to add drawer layout button on toolbar
			- how to create a layout that stick on every screen
			- how to create a dialog that swifts up from screen bottom
<22/06/2019> Sat
	+ Start to work with the S10 Music player.


<21/06/2019> Fri

	+ Continue with Audio visualizer
	+ Progressbar controls song position

<20/06/2019> Thu
	+ Today: 
		//Progressbar: playing music and loading song
		Loop button
		Show notification on lock screen
		Fix bug - play next continuously after clicking
			- service auto destroyed
		//Displaying Waveform 

<19/06/2019> Wed
	+ Today: Create a Notification for the YAMP 
		(Yet Another Music Player) that handles the
		view for controlling the service and to distroy
		the service. this notification requires to
		use RemoteViews. which is created programmatically
		whenever a song is played
	

<18/06/2019> Tue
	+ continue the project of the second week
	now come to the second activity to play the song
	and use the running in background service.
	also, communicate between service and activity
	as well as between service and notification.
	
	+ tomorrow we will think about the further ideas
	
	+ Parcelable: allows to pass data in form of an object
	between activities

	+ Sevice(): has two types bound Service and started service 
		bound service: bindService(serviceConnection)
		 		unbindService(serviceConnection)
		started service: startService(intent)
				service.stopSelf() or stopService(intent)

		Note: this two pairs of functions must be called whenever you use
		any type of the service. you cannot stop a service of one type
		by calling only the stopself() or unbindService()
		call them both if you want to use such a combination of the two

<17/6/2019> Mon (Week 2)
	+ Now we look at a given project of the company
	namely MusicPlayerWithMVP, understand it. And eventually
	implement your own Music player based on what you have
	learned from that given project.
	

	---> Project description:
	- MainActivity: 
		+ load songs from storage into 
		a recycleView list for displaying purpose only
		the presenter will call the getSongs() function
		in the onCreate(). It fetch the songs' info only.

		+ On one item is selected from the list. second 
		activity will be triggered for display.
		(using laucherActivity)

	- PlaySongActivity
		in this Activity, we use boundService and unboundService
		Where boundService to connect between the background thread



<14/06/2019> Fri

	+ Done with Peer 2 Peer connection. ready for sending
	data among users. (However I dont have an extra device
	for testing this communication feature yet)
	
	+ Sqlite is simple and It saves data in the local 
	text file only. nothing interesting here.
	
	+ Http connection is ok. Just give one URL and send
	the request. the response will be sent back as a result.
	
	+ JSONObject is nothing more than a parser for
	Json data. allow to read out all the values by keys.

	+ WifiPeer2Peer connection is ok now. It provides
		discovering other devices on the same network
		we can obtain the IP address for TCP connection

	+ WifiManager supports wifi scanning to see which wifi
		is available.
	+ Two balls on one screen is now ok.

<13/06/2019> Thurs

	+ using pitch, roll, yaw to control a canvas image (a ball) to
	move arround the screen.

	+ Vibration: when the ball hits the walls, a vibation is emitted.

	+ WifiScanner: require the permision ACCESS_COARSE_LOCATION
		to be granted at runtime from Android version
		>= 6. (Perhaps Storage permission faces the
		same problem)

	+ TCP socket message sending.

	+ Peer-2-Peer direct wifi communication.

	+ then work with internet: http json object.
	and finally 
	
	+ tomorrow, continue with the 2 last topic in the list
	in the afternoon we will have a argenda (meeting).
	we'll talk about what we have done during the week.
	And we will have to be prepare.

<12/06/2019>
	+ Working with Accelerometer and MagneticMeter
	to calculate orientation
	+ Learning asynchTask, thread, looper, handler,
	lock, unlock to run the task in background
	for drawing to canvas (update UI)

<11/06/2019>
	+ Understood MVP architecture
		https://androidwave.com/android-mvp-architecture-for-beginners-demo-app/
	+ Now, TabLayout:
		TabLayout usually goes with ViewPager for
		switching tabs by swifting the screen.
		Each Tab has one id, based on that the
		corresponding fragment is selected.
		The PageAdapter would create a new Fragment
		and give it to the ViewPager ??? through 
		getItem(). 
	+ FileSystem:
		+ sharedPreference
		+ file system:	private application files
		 		external public files: root or getExternalStoragePublicDirectory
				FileInputStream and FileOutputStream
		+ SQLite

<10/06/2019>
-----------> Start the internship
	What to do in this internship:
		Week 1: review all the basis of android:
			//- TabLayout Viewpage
			//- File system
			- SQLite content provider
			- Http url connection, Json object
			//- MVP architecture

		Week 2: Break through a real project of the
 			company. 
			Analysis to see how it is designed.

		Week 3 and 4: Hands on a personal project
		based on given requirements of company.
		
		From the second month: review the finished
		project and work with the real project of
		the company.
		

---------> What to remember:
		+ every last day of a week, send an email
		to the team Mornitor about what have been
		done during that week.
		