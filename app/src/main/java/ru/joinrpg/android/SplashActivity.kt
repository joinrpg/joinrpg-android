package ru.joinrpg.android

import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()

        val h = Handler()
        h.postDelayed({
            app().onSplashStart()
        }, 500)
    }
}
