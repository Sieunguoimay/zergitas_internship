package com.zergitas.demo.screens.main

import android.content.Context
import com.zergitas.demo.data.model.Song
import com.zergitas.demo.data.repository.SongRepository
import com.zergitas.demo.data.source.SongDataSource

class MainPresenter(context: Context) : MainContract.Presenter {
    private var view: MainContract.View? = null
    private var songRepository: SongRepository

    init {
        songRepository = SongRepository(context)
    }

    override fun getSongs() {
        view?.showProgressBar()
        songRepository.getSongs(object : SongDataSource.Callback<Song> {
            override fun onUpdateData(data: Pair<Int, Song>) {
                view?.updateSongs(data)
            }

            override fun onGetDataSuccess(data: List<Song>) {
                view?.hiddenProgressBar()
                view?.showSongs(data)
            }

            override fun onFail(message: String) {
                view?.hiddenProgressBar()
                view?.showErrorSongs()
            }

        })
    }

    override fun setView(view: MainContract.View) {
        this.view = view
    }

    override fun start() {
    }

    override fun stop() {
    }

}