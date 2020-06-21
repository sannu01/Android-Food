package com.internshala.assignment.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.internshala.assignment.R

class PasswordChangedActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_changed)
        toolbar=findViewById(R.id.toolbar)
        setupactiobar()
    }
    fun setupactiobar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Reset Password"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed();
        return true;
    }
}
