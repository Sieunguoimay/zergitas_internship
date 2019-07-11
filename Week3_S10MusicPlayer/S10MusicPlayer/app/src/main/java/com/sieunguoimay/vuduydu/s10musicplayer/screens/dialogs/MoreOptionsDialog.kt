package com.sieunguoimay.vuduydu.s10musicplayer.screens.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window

private const val TAG = "MORE_OPTIONS_DIALOG"
object MoreOptionsDialog {

    fun showDialog(context: Context,view: View){
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        dialog.setButton(1,"Add to next", clicklistener )
        dialog.setButton(2,"Push to queue", clicklistener )
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val wmlp = dialog.window!!.attributes
        wmlp.gravity = Gravity.TOP;Gravity.START
        wmlp.x = view.x.toInt()
        wmlp.y = view.y.toInt()
        dialog.show()
    }
    val clicklistener = object:DialogInterface.OnClickListener{
        override fun onClick(dialog: DialogInterface?, which: Int) {
            when(which){
                1->{
                    Log.d(TAG,"Button 1 on dialog clicked")
                }
            }
        }
    }
}