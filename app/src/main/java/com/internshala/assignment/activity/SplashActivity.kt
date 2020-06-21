package com.internshala.assignment.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.LinearLayout
import com.internshala.assignment.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash);
        supportActionBar?.hide();
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val login= Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(login)
            finish()
        },1000)
        val splash:LinearLayout=findViewById(R.id.splash)
        splash.animate().alpha(0f).setDuration(1500)

    }
}
