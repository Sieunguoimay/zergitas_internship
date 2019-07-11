package com.sieunguoimay.vuduydu.yamp.screens.main

import android.content.Context
import com.sieunguoimay.vuduydu.yamp.data.model.Song
import com.sieunguoimay.vuduydu.yamp.data.repository.SongRepository
import com.sieunguoimay.vuduydu.yamp.data.source.song.SongLocalDataContract

class MainPresenter(context:Context): MainContract.Presenter {
    private var view: MainContract.View? = null
    private var songRepository: SongRepository

    init{
        songRepository = SongRepository(context)
    }


    override fun getSongs() {

        view?.showProgressBar()

        songRepository.getSongs(object: SongLocalDataContract.Callback<Song>{

            override fun onGetDataSuccess(data: List<Song>) {
                view?.hideProgressBar()
                view?.showSongs(data)
            }

            override fun onUpdateData(data: Pair<Int,Song>) {
                view?.updateSongs(data)
            }

            override fun onFailed(message: String) {
                view?.hideProgressBar()
                view?.showErrorSongs()
            }

        })
    }

    override fun setView(view: MainContract.View) {
        this.view = view;
    }
}