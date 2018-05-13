package ru.joinrpg.android

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.AsyncTask
import com.vk.sdk.VKSdk
import java.util.*

/**
 * Класс приложения. В него хочется складывать разнообразную "бизнес-логику" работы приложения.
 */
class JoinRpgApplication() : Application() {

    val networking = Networking()
    var _currentActivity: Activity? = null
    var version: String? = null

    override fun onCreate() {
        super.onCreate()

        VKSdk.initialize(this)

        val info = packageManager.getPackageInfo(packageName, 0)
        version = info.versionName
    }

    fun setCurrentActivity(activity: Activity) {
        _currentActivity = activity
        Settings.shared.setCurrentActivity(activity)
    }

    fun onSplashStart() {
        // это запускается из окна "сплэша". Надо понять, залогинены мы или нет — если залогинены, то показываем сразу главное окно, а нет — окно логина
        AsyncTask.execute {
            if (!Settings.shared.getToken().isNullOrEmpty() && Date().after(Settings.shared.getTokenExpires())) {
                // мы были залогинены, вроде всё ок
                val intent = Intent(_currentActivity, MainActivity::class.java)
                _currentActivity!!.startActivity(intent)
            }
            else {
                val intent = Intent(_currentActivity, LoginActivity::class.java)
                _currentActivity!!.startActivity(intent)
                _currentActivity!!.overridePendingTransition(0, android.R.anim.fade_out)
            }
        }
    }
}
