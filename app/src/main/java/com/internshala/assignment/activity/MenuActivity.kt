package com.internshala.assignment.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.assignment.R
import com.internshala.assignment.adapter.Global
import com.internshala.assignment.adapter.MenuClickListener
import com.internshala.assignment.adapter.MenuRecyclerAdapter
import com.internshala.assignment.database.RestaurantDatabase
import com.internshala.assignment.database.RestaurantEntity
import com.internshala.assignment.model.Menu
import com.internshala.assignment.util.GlobalVars
import java.util.HashMap

class MenuActivity : AppCompatActivity(),MenuClickListener {
    lateinit var recyclerMenu: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerAdapter: MenuRecyclerAdapter
    lateinit var toolbar: Toolbar
    lateinit var btnproceed:Button
    var restaurant_id:String="1000"
    var restaurant_name:String="Name"
    val menuList = arrayListOf<Menu>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        btnproceed=findViewById(R.id.btnproceed)
        btnproceed.visibility= View.GONE
        Global.cartList.clear()

        toolbar = findViewById(R.id.toolbar)

        recyclerMenu = findViewById(R.id.recyclerMenu)
        layoutManager = LinearLayoutManager(this)
        if (intent != null) {
            restaurant_id = intent.getStringExtra("id")
            restaurant_name = intent.getStringExtra("name")

        }
        if (restaurant_id=="1000"){
            finish()
        }
        setupactiobar(restaurant_name)
        val favrestaurant:ImageView=findViewById(R.id.favrestautant)
        favrestaurant.setImageResource(R.drawable.ic_unfav)
        val restaurantEntity =RestaurantEntity(
            restaurant_id.toInt(),
            restaurant_name
        )
        val checkfav = MenuActivity.DBAsyncTask(this,restaurantEntity,1).execute()

        val isfav= checkfav.get()
        if (isfav){
            favrestaurant.setImageResource(R.drawable.ic_fav)
        }


        favrestaurant.setOnClickListener {
            if(!MenuActivity.DBAsyncTask(this,restaurantEntity,1).execute().get()){

                val async=MenuActivity.DBAsyncTask(this,restaurantEntity,2).execute()
                val result=async.get()
                if (result){

                    favrestaurant.animate().scaleY(1.5f).scaleX(1.5f).setDuration(200)
                    Handler().postDelayed( {
                        favrestaurant.animate().scaleY(1f).scaleX(1f)
                        favrestaurant.setImageResource(R.drawable.ic_fav)
                    },300)


                }
            }else{
                val async = MenuActivity.DBAsyncTask(this,restaurantEntity,3).execute()

                val result=async.get()
                if (result){

                    favrestaurant.animate().scaleY(.5f).scaleX(.5f).setDuration(200)
                    Handler().postDelayed( {
                        favrestaurant.animate().scaleY(1f).scaleX(1f)
                        favrestaurant.setImageResource(R.drawable.ic_unfav)
                    },300)
                }
            }
        }

        btnproceed=findViewById(R.id.btnproceed)
        btnproceed.setOnClickListener {
            val proceed=Intent(this,CartActivity::class.java)
            proceed.putExtra("restaurant_id",restaurant_id)
            proceed.putExtra("restaurant_name",restaurant_name)
            startActivity(proceed)
        }



        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"


        if (GlobalVars.isNetworkConnected) {
            val jsnoRequest =
                object :
                    JsonObjectRequest(Method.GET, "$url$restaurant_id", null, Response.Listener {

                        try {
                            val dataObject = it.getJSONObject("data")
                            var success = dataObject.getBoolean("success")
                            println(success)
                            if (success) {
                                val data = dataObject.getJSONArray("data")
                                for (i in 0 until data.length()) {
                                    val restaurantObject = data.getJSONObject(i)
                                    val restaurant = Menu(
                                        restaurantObject.getString("id"),
                                        restaurantObject.getString("name"),
                                        restaurantObject.getString("cost_for_one"),
                                        restaurantObject.getString("restaurant_id")
                                    )
                                    menuList.add(restaurant)
                                }
                                recyclerAdapter = MenuRecyclerAdapter(this, menuList,this)

                                recyclerMenu.adapter = recyclerAdapter
                                recyclerMenu.layoutManager = layoutManager


                            } else {
                                var message = dataObject.getString("errorMessage")
                                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            }

                        } catch (e: Exception) {

                            Toast.makeText(this, "Some Error Occurred!!", Toast.LENGTH_SHORT).show()

                        }

                    }, Response.ErrorListener {

                        Toast.makeText(this, "Some Error Occurred!!", Toast.LENGTH_SHORT).show()


                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["context type"] = "application/json"
                        headers["token"] = "72c79d10f45224"
                        return headers
                    }
                }
            queue.add(jsnoRequest)
        } else {
            Toast.makeText(this, "Couldn't Connect to Internet", Toast.LENGTH_SHORT).show()

        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed();
        return true;
    }

    fun setupactiobar(name: String?) {
        setSupportActionBar(toolbar)
        supportActionBar?.title = name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }


    class DBAsyncTask(val context: Context, val restaurantEntity: RestaurantEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        val db =
            Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurant-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {

                1 -> {
                    val restaurant: RestaurantEntity? =
                        db.restaurantDao()
                            .getRestaurantById(restaurantEntity.restaurant_id.toString())
                    db.close()
                    return restaurant != null
                }

                2 -> {

                    db.restaurantDao().insertRestaurant(restaurantEntity)
                    db.close()
                    return true
                }
                3 -> {
                    db.restaurantDao().deleteRestaurant(restaurantEntity)
                    db.close()
                    return true
                }
            }
            return false
        }

    }

    override fun checkCartStatus() {
        if (Global.cartList.size>0){
            btnproceed.visibility=View.VISIBLE
        }
        if(Global.cartList.size==0){
            btnproceed.visibility=View.GONE
        }
    }


    override fun onBackPressed() {

        if(Global.cartList.size>0){
            val dialog=AlertDialog.Builder(this)
            dialog.setMessage("Going back will reset the Cart. Do you want to go back?")
            dialog.setTitle("Confirmation")
            dialog.setPositiveButton("YES"){_,_->
                super.onBackPressed()
            }
            dialog.setNegativeButton("NO"){_,_->

            }
            dialog.create()
            dialog.show()
        }
        else{
            super.onBackPressed()
        }
    }

}