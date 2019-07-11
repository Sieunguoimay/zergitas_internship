package com.sieunguoimay.vuduydu.s10musicplayer.screens.HomeScreenActivity
import com.sieunguoimay.vuduydu.yamp.data.model.Song

class FavouriteSongContract {
    interface Model{
        fun insertFavouriteSong(songId:Long, title:String)
        fun deleteFavouriteSong(songId:Long)
    }
    interface Presenter{
        fun like(song: Song)
        fun unlike(song:Song)
    }
    interface View{
        fun updateIconOnLike()
        fun updateIconOnUnlike()
    }
}

//steps:
//first, do the action like and dislike, which insert and delete the song into and from the database
//second, is the current song liked. by checking from the favorite list every time a new song is played
//third, display the entire favorite list