package com.ibeor.ibeorchattingapp.modules.welcome

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.LocationSettingsStates
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val states = LocationSettingsStates.fromIntent(data!!)
        when (requestCode) {
            123 -> when (resultCode) {
                RESULT_OK ->      {
                    MyUtils.checkStatus=true
                    Toast.makeText(this, "On result match", Toast.LENGTH_SHORT).show()
                    Log.i("sajdfsdgdjkaf","auto done without change in mainactivity")
                Toast.makeText(this, states!!.isLocationPresent.toString() + "", Toast.LENGTH_SHORT).show()
                }
                Activity.RESULT_CANCELED ->                         // The user was asked to change settings, but chose not to
                    Toast.makeText(this, "Canceled on result", Toast.LENGTH_SHORT).show()
                else -> {
                    Toast.makeText(this, "Else on result", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}