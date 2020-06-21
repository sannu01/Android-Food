package com.internshala.assignment.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import com.internshala.assignment.R
import com.internshala.assignment.adapter.Global

class OrderPlacedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_placed)

        val btnhome:Button=findViewById(R.id.btngohome)
        btnhome.setOnClickListener {
            val home= Intent(this,MainActivity::class.java)
            Global.main_flag=0
            startActivity(home)
            finishAffinity()

        }
    }

    override fun onBackPressed() {

    }

}
