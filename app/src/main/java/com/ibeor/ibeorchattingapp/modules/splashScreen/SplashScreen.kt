package com.ibeor.ibeorchattingapp.modules.splashScreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.ibeor.ibeorchattingapp.modules.welcome.MainActivity

import com.ibeor.ibeorchattingapp.R

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
       Handler(Looper.myLooper()!!).postDelayed({
           startActivity(Intent(this@SplashScreen, MainActivity::class.java))
           finish() }, 1500)
    }
}