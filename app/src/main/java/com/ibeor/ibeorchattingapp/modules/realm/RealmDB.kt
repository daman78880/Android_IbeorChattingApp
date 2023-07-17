package com.ibeor.ibeorchattingapp.modules.realm

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration




class RealmDB :Application(){
    override fun onCreate() {
        super.onCreate()
        Realm.init(this@RealmDB)
        val config = RealmConfiguration.Builder()
            .allowWritesOnUiThread(true)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
    }
}