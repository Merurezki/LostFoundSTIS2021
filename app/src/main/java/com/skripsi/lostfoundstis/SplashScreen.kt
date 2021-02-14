package com.skripsi.lostfoundstis

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    private var sessionManager: SessionManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        sessionManager = SessionManager(this)

        /*handler untuk menahan activity sementara*/
        val handler = Handler()
        handler.postDelayed({
            sessionManager?.checkLogin()
            finish()
        }, 1000)
    }

    override fun onBackPressed() {
        return
    }
}