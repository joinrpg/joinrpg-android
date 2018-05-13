package ru.joinrpg.android

import android.os.Bundle
import android.widget.TextView

class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val versionView = findViewById<TextView>(R.id.version)
        if (versionView != null) {
            val version = resources.getString(R.string.about_version) + " " + app().version!!
            versionView.text = version
        }
    }
}
