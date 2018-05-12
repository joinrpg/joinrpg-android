package ru.joinrpg.android

import android.app.Activity

/**
 * Базовый класс для всех активити. Полезен тем, что сообщает о себе в объект класса JoinRpgApplication
 */
open class BaseActivity: Activity() {

    override fun onStart() {
        super.onStart()
        // расскажем объекту приложения, кто себйчас на экране
        app().setCurrentActivity(this)
    }

    fun app(): JoinRpgApplication {
        // получить приложение из любого активити
        return application as JoinRpgApplication
    }
}
