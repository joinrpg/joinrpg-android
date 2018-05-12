package ru.joinrpg.android

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.auth.api.signin.*
import kotlinx.android.synthetic.main.activity_login.*


/**
 * Окно логина. Заголовок, емейл, пароль, кнопка "логин". Присутствуют зачатки логина через соцсети Гугл и ВК
 */
class LoginActivity : BaseActivity(), View.OnClickListener {

    private var RC_SIGN_IN: Int = 0x8000
    private var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /*
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        google_sign_in_button.setOnClickListener(this)
        vk_sign_in_button.setOnClickListener(this)
        */

        native_sign_in_button.setOnClickListener(this)

        //(vkSdkListener, appId, VKAccessToken.tokenFromSharedPreferences(this, vkTokenKey));
    }

    override fun onStart() {
        super.onStart()
        // source is from here: https://developers.google.com/identity/sign-in/android/sign-in
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)

        if (account != null) {
            // do something? TODO
        }
    }

    override fun onClick(v: View?) {
        if (v == null)
            return

        when (v.id) {
/* TODO: логин через соцсети
            R.id.google_sign_in_button -> googleSignIn()
            R.id.vk_sign_in_button -> vkSignIn() */
            R.id.native_sign_in_button -> nativeSignIn()
        }
    }

    private fun nativeSignIn() {
        val login = findViewById<EditText>(R.id.native_login).text.trim().toString()
        val password = findViewById<EditText>(R.id.native_password).text.toString()

        if (login.isEmpty()) {
            showError(resources.getString(R.string.login_error_login_empty))
            return
        }

        if (password.isEmpty()) {
            showError(resources.getString(R.string.login_error_password_empty))
            return
        }

        val dialog = MaterialDialog.Builder(this)
                .progress(true, 0)
                .show()

        app().networking.authenticate(login, password, { result, error ->  
            if (result) {
                Handler(this.mainLooper).post {
                    dialog.hide()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
            else {
                Handler(this.mainLooper).post {
                    dialog.hide()
                    showError(error!!)
                }
            }
        })
    }

    private fun showError(error: String) {
        MaterialDialog.Builder(this)
                .title(R.string.login_error_messagebox_title)
                .content(error)
                .positiveText(R.string.login_error_messagebox_dismiss_button)
                .show()
    }

    /* TODO: логин через соцсети

    private fun googleSignIn() {
        if (mGoogleSignInClient != null) {
            startActivityForResult(mGoogleSignInClient!!.signInIntent, RC_SIGN_IN)
        }
    }

    private fun vkSignIn() {
        VKSdk.login(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (VKSdk.onActivityResult(requestCode, resultCode, data, object: VKCallback<VKAccessToken> {
                    override fun onResult(res: VKAccessToken) {
                        // Пользователь успешно авторизовался
                        Log.d("onActivityResult", "VK success!")

                        Settings.shared.setUserEmail(res.email, Settings.EmailSource.VK)
                    }
                    override fun onError(error: VKError) {
                        // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
                        Log.d("onActivityResult", "VK fail!")
                        Settings.shared.setUserEmail(null, Settings.EmailSource.UNKNOWN)
                    }
                })) {
            // do nothing
        }
        else {
            if (requestCode == RC_SIGN_IN) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleGoogleSignInResult(task)
            }
        }
    }

    private fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            // woo-hoo
            Log.d("Login", "Google Success")

            Settings.shared.setUserEmail(task.result.email, Settings.EmailSource.GOOGLE)
        }
        else {
            // not woo-hoo
            Log.d("Login", "Google Fail")
            Settings.shared.setUserEmail(null, Settings.EmailSource.UNKNOWN)
        }
    }
    */
}
