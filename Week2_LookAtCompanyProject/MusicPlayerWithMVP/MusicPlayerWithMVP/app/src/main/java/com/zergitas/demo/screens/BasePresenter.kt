package com.zergitas.demo.screens

interface BasePresenter<T> {
    fun setView(view: T)
    fun start()
    fun stop()
}