package ru.joinrpg.android

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import java.util.*

class Settings {
    companion object {
        private val KEY_USER_EMAIL = "USER_EMAIL"
        private val KEY_USER_EMAIL_SOURCE = "USER_EMAIL_SOURCE"
        private val KEY_TOKEN = "USER_TOKEN"
        private val KEY_TOKEN_EXPIRATION = "USER_TOKEN_EXPIRATION"

        val shared = Settings()

        private val map = EmailSource.values().associateBy(EmailSource::value);
        fun fromInt(type: Int) = map[type]!!
    }

    enum class EmailSource(val value: Int) {
        UNKNOWN(-1),
        NATIVE(0),
        GOOGLE(1),
        VK(2)
    }

    private var _preferences: SharedPreferences? = null

    fun setCurrentActivity(activity: Activity) {
        _preferences = activity.getSharedPreferences(activity.packageName, Context.MODE_PRIVATE)
    }

    fun getUserEmail(): String? {
        assert(_preferences != null)

        return _preferences!!.getString(KEY_USER_EMAIL, null)
    }

    fun getUserEmailSource(): EmailSource {
        assert(_preferences != null)

        return fromInt(_preferences!!.getInt(KEY_USER_EMAIL_SOURCE, -1))
    }

    fun setUserEmail(email: String?, source: EmailSource) {
        assert(_preferences != null)

        if (email == null) {
            _preferences!!.edit()
                    .remove(KEY_USER_EMAIL)
                    .remove(KEY_USER_EMAIL_SOURCE)
                    .apply()
        }
        else {
            _preferences!!.edit()
                    .putString(KEY_USER_EMAIL, email)
                    .putInt(KEY_USER_EMAIL_SOURCE, source.value)
                    .apply()
        }
    }

    fun setToken(token: String?, expires: Date?) {
        assert(_preferences != null)

        if (token == null || expires == null) {
            _preferences!!.edit()
                    .remove(KEY_TOKEN)
                    .remove(KEY_TOKEN_EXPIRATION)
                    .apply()
        }
        else {
            _preferences!!.edit()
                    .putString(KEY_TOKEN, token)
                    .putLong(KEY_TOKEN_EXPIRATION, expires.time)
                    .apply()
        }
    }

    fun getToken(): String? {
        assert(_preferences != null)

        return _preferences!!.getString(KEY_TOKEN, null)
    }

    fun getTokenExpires(): Date? {
        assert(_preferences != null)

        return Date(_preferences!!.getLong(KEY_TOKEN_EXPIRATION, 0))
    }
}
