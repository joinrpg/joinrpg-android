package ru.joinrpg.android

import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import java.util.*

class Networking {

    val BASE_URL = "https://dev.joinrpg.ru/x-api"
    val LOGIN_URL = "$BASE_URL/token"

    fun authenticate(login: String, password: String, callback: (Boolean, String?) -> Boolean) {

        LOGIN_URL.httpPost(listOf("grant_type" to "password", "username" to login, "password" to password)).responseJson { _, _, result ->
            when (result) {
                is Result.Failure -> {
                    callback(false, result.error.localizedMessage)
                }
                is Result.Success -> {
                    result.fold(success = { json ->
                        val token = json.obj().getString("access_token")
                        val expireDate = json.obj().getInt("expires_in")
                        val date = Date()
                        date.time += expireDate*1000
                        Settings.shared.setToken(token, date)
                        callback(true, null)
                    },
                    failure = { _ ->
                        callback(false, "error parsing JSON")
                    })
                }
            }
        }
    }

    fun deauthenticate() {
        Settings.shared.setToken(null, null)
    }
}
