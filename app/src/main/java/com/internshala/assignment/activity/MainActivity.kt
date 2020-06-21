package com.internshala.assignment.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.google.android.material.navigation.NavigationView
import com.internshala.assignment.R
import com.internshala.assignment.database.RestaurantDatabase
import com.internshala.assignment.database.RestaurantEntity
import com.internshala.assignment.fragment.*

class MainActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var drawerlayout: DrawerLayout
    lateinit var toolbar: Toolbar
    lateinit var navigationview: NavigationView
    var previousMenuItem:MenuItem?=null
    var secondMenu:MenuItem?=null
    lateinit var appBarConfiguration:AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences=getSharedPreferences(getString(R.string.shared_preference),Context.MODE_PRIVATE)
        val username=sharedPreferences.getString("name","User")
        val usermobile=sharedPreferences.getString("phone","Mobile")
        val head="+91 "
        val finalmobile=head+usermobile

        drawerlayout=findViewById(R.id.drawerlayout)

        toolbar=findViewById(R.id.toolbar)
        navigationview=findViewById(R.id.navigationview)
        val header=navigationview.getHeaderView(0)
        val txtdrawerName:TextView=header.findViewById(R.id.drawerUsername)
        val txtDrawerMobile:TextView=header.findViewById(R.id.drawerUserMobile)
        val llheader:LinearLayout=header.findViewById(R.id.llheader)
        llheader.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.nav_host_fragment,
                    ProfileFragment()
                )
                .commit()
            supportActionBar?.title ="My Profile"
            drawerlayout.closeDrawers()
        }
        txtdrawerName.text=username
        txtDrawerMobile.text=finalmobile

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        home()
        val actionBar=ActionBarDrawerToggle(this@MainActivity,drawerlayout,R.string.open_drawer,R.string.close_drawer)
        drawerlayout.addDrawerListener(actionBar)
        actionBar.syncState()

        navigationview.setNavigationItemSelectedListener {
            if (previousMenuItem !=null){
                secondMenu=previousMenuItem
                previousMenuItem?.isChecked =false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it
            when(it.itemId){
                R.id.home->{
                    home()

                    drawerlayout.closeDrawers()
                }
                R.id.profile->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment,
                        ProfileFragment())
                        .commit()
                    supportActionBar?.title ="My Profile"
                    drawerlayout.closeDrawers()
                }
                R.id.favourite->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.nav_host_fragment,
                            FavouriteFragment()
                        )
                        .commit()
                    supportActionBar?.title="Favourite Restaurant"
                    drawerlayout.closeDrawers()
                }
                R.id.order->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.nav_host_fragment,
                            OrderFragment()
                        )
                        .commit()
                    supportActionBar?.title="Previous Orders"
                    drawerlayout.closeDrawers()
                }
                R.id.faq->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.nav_host_fragment,
                            FaqFragment()
                        )
                        .commit()
                    supportActionBar?.title="Frequently Asked Questions"
                    drawerlayout.closeDrawers()
                }
                R.id.logout->{
                    drawerlayout.closeDrawers()
                    val dialog=AlertDialog.Builder(this)
                    dialog.setMessage("Are you Sure want to Exit?")
                    dialog.setTitle("Alert")
                    dialog.setPositiveButton("Logout"){ _, _ ->
                        val login=Intent(this,LoginActivity::class.java)
                        clearDatbase(this).execute()
                        sharedPreferences.edit().clear().apply()

                        startActivity(login)
                        finishAffinity()

                    }
                    dialog.setNegativeButton("Cancel"){ _, _ ->
                        secondMenu?.isChecked=true
                    }
                    dialog.create()
                    dialog.show()

                }
            }

            return@setNavigationItemSelectedListener true
        }


    }
    fun home(){
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.nav_host_fragment,
                HomeFragment()
            )
            .commit()
        supportActionBar?.title="Home"
        navigationview.setCheckedItem(R.id.home)
    }

    override fun onBackPressed() {
        if (drawerlayout.isDrawerOpen(GravityCompat.START)){
            drawerlayout.closeDrawers()
        }else {
            val frag = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            println(frag)
            when (frag) {
                !is HomeFragment -> home()

                else -> super.onBackPressed()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id== android.R.id.home){
            drawerlayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    class clearDatbase(val context:Context):AsyncTask<Void,Void,Boolean>(){
        override fun doInBackground(vararg params: Void?):Boolean {

            val db = Room.databaseBuilder(context,RestaurantDatabase::class.java,"restaurant-db").build()
            db.restaurantDao().deleteAll()
            return true

        }

    }




}

