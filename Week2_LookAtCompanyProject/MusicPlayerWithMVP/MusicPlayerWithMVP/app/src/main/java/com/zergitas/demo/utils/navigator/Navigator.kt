package com.zergitas.demo.utils.navigator

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.zergitas.demo.R

inline fun <reified T : Any> AppCompatActivity.launcherActivity(
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent)
    overridePendingTransition(R.anim.amin_right, R.anim.amin_left)
}

inline fun <reified T : Any> AppCompatActivity.launcherActivityWithResult(
    requestCode: Int,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivityForResult(intent, requestCode)
    overridePendingTransition(R.anim.amin_right, R.anim.amin_left)
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
    Intent(context, T::class.java)

inline fun AppCompatActivity.addFragment(id: Int, fragment: Fragment) {
    val manager = supportFragmentManager
    manager.beginTransaction()
        .replace(id, fragment)
        .addToBackStack(null)
        .commit()
}

inline fun AppCompatActivity.addFragmentNoBackStack(id: Int, fragment: Fragment) {
    val manager = supportFragmentManager
    manager.beginTransaction()
        .replace(id, fragment)
        .commit()
}

inline fun AppCompatActivity.removeFragment(id: Int, fragment: Fragment) {
    val manager = supportFragmentManager
    manager.beginTransaction()
        .remove(fragment)
        .commit()
    manager.popBackStack()
}