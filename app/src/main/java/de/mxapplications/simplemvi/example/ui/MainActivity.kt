package de.mxapplications.simplemvi.example.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.mxapplications.simplemvi.example.repository.RedditRepository
import de.mxapplications.simplemvi.example.ui.home.HomeFragment
import de.mxapplications.simplemvi.example.ui.login.LoginActivity
import org.koin.android.ext.android.inject

private const val LOGIN_REQUEST = 1001

class MainActivity: AppCompatActivity() {
    val repository: RedditRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            startActivityForResult(LoginActivity.createIntent(this), LOGIN_REQUEST)
        } else {
            showHome()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOGIN_REQUEST && resultCode == Activity.RESULT_OK) {
            showHome()
        }
    }

    private fun showHome() {
        if (repository.loggedIn) {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, HomeFragment.newInstance())
                .commit()
        }
    }
}