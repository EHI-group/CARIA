package com.mateocvas.caria

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*
import kotlin.concurrent.timerTask

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val timer= Timer()
        timer.schedule(
            timerTask {
                goToLoginActivity()
            }, 1000
        )

    }


    private fun goToLoginActivity( ){
        val intent = Intent(this,RegisterActivity::class.java)
        startActivityForResult(intent,4)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        finish()
    }

}
