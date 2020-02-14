package com.mateocvas.caria

import android.app.Application
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class MyApp : Application() {


    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MyApp
            private set
    }


}