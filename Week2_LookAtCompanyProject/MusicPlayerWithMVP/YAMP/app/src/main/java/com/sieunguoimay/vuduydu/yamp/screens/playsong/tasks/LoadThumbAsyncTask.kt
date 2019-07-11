package com.sieunguoimay.vuduydu.yamp.screens.playsong.tasks

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import com.sieunguoimay.vuduydu.yamp.data.source.song.provider.ContentProvider
import com.sieunguoimay.vuduydu.yamp.screens.playsong.interfaces.LoadingThumbCallback

class LoadThumbAsyncTask
    (
    private var context:Context,
    private var callback:LoadingThumbCallback
): AsyncTask<Long, Void, Bitmap> (){

    override fun doInBackground(vararg params: Long?): Bitmap? {
        try{
            return params[0]!!.let{
                ContentProvider.getThumbSong(context,it)
                //what is it? it is a params[0] of type Long
            }
        }catch(e:Exception){
            e.printStackTrace()
            return null
        }
    }

    override fun onPostExecute(result: Bitmap?) {
        if(result!=null){
            callback.onSuccess(result)
        }else{
            callback.onFail()
        }
    }
}