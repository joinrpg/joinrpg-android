package ru.joinrpg.android

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.afollestad.materialdialogs.MaterialDialog

/**
 * Пока это называется "главным активити", но потом жизнь может заставить всё переделать
 */
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // при создании меню надо создать меню
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // это когда пользователь на кнопку в меню нажал
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == null)
            return super.onOptionsItemSelected(item)

        when (item.itemId) {
            R.id.main_menu_about -> {
                onMenuAbout()
                return true
            }
            R.id.main_menu_logout -> {
                onMenuLogout()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    // обработка нажатия на меню About
    private fun onMenuAbout() {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

    // обработка нажатия на меню логаут
    private fun onMenuLogout() {
        MaterialDialog.Builder(this)
                .title(R.string.main_menu_logout_confirm_title)
                .content(R.string.main_menu_logout_confirm_content)
                .positiveText(R.string.messagebox_yes)
                .negativeText(R.string.messagebox_no).onPositive { _, _ ->
                    app().networking.deauthenticate()
                    val intent = Intent(this, LoginActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .show()
    }
}
