package ru.joinrpg.android

import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import java.util.*
import kotlin.collections.HashMap

class Networking {

    val BASE_URL = "https://dev.joinrpg.ru/x-api"
    val LOGIN_URL = "$BASE_URL/token"
    val PROJECTS_URL = "$BASE_URL/me/projects/active"

    fun authenticate(login: String, password: String, callback: (Boolean, String?) -> Unit) {

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

    fun activeProjects(callback: (Boolean, Map<Int, String>?) -> Unit) {
        PROJECTS_URL.httpGet().header(Pair("Authorization", "Bearer " + Settings.shared.getToken())).responseJson { _, _, result ->
            when (result) {
                is Result.Success -> {
                    // парсим массив вида (из swagger'а):
                    /*
                    [
                      {
                        "ProjectId": 0,
                        "ProjectName": "string"
                      }
                    ]
                     */

                    result.fold(success = { json ->
                        val retMap = HashMap<Int, String>(json.array().length())
                        for (i in 0 until json.array().length()) {
                            val obj = json.array().getJSONObject(i)
                            val id = obj.getInt("ProjectId")
                            val name = obj.getString("ProjectName")

                            retMap[id] = name
                        }

                        callback(true, retMap)
                    },
                    failure = { _ ->
                        callback(false, null)
                    })
                }
                is Result.Failure -> {
                    callback(false, null)
                }
            }
        }
    }
}
