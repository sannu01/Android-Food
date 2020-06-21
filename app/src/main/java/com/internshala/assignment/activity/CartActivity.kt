package com.internshala.assignment.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.assignment.R
import com.internshala.assignment.adapter.CartRecyclerAdapter
import com.internshala.assignment.adapter.Global
import com.internshala.assignment.adapter.MenuRecyclerAdapter
import com.internshala.assignment.model.Menu
import com.internshala.assignment.util.GlobalVars
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

class CartActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var toolbar: Toolbar
    lateinit var recyclerCart: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerAdapter: CartRecyclerAdapter
    lateinit var btnplaceorder: Button
    var restaurant_id:String="1000"
    var restaurant_name:String="Name"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title ="Cart"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        sharedPreferences=getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE)
        val user_id=sharedPreferences.getString("id","1000")
        restaurant_id=intent.getStringExtra("restaurant_id")
        restaurant_name=intent.getStringExtra("restaurant_name")
        recyclerCart=findViewById(R.id.recyclerCart)
        btnplaceorder=findViewById(R.id.btnplaceorder)
        layoutManager=LinearLayoutManager(this)

        recyclerAdapter = CartRecyclerAdapter(this, Global.cartList)

        recyclerCart.adapter = recyclerAdapter
        recyclerCart.layoutManager = layoutManager

        if (user_id=="1000"){
            onBackPressed()
            finish()
        }
        val fromrestaurant:TextView=findViewById(R.id.fromrestaurant)
        fromrestaurant.text=("Ordering From : $restaurant_name")

        var totalCost=0
        val food=JSONArray()
        val order=JSONObject()
        order.put("user_id",user_id)
        order.put("restaurant_id",restaurant_id)
        for(i in 0 until Global.cartList.size){
            val food_id=JSONObject()
            food_id.put("food_item_id",Global.cartList[i].food_item_id)
            totalCost+=Global.cartList[i].food_cost.toInt()
            food.put(i,food_id)
        }
        btnplaceorder.text="Place Order (Total Cost $totalCost)"
        order.put("total_cost",totalCost.toString())
        order.put("food",food)
        println(order)
        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/place_order/fetch_result/"

        btnplaceorder.setOnClickListener {


            if (GlobalVars.isNetworkConnected) {
                val jsnoRequest =
                    object :
                        JsonObjectRequest(Method.POST, url, order, Response.Listener {

                            try {
                                val dataObject = it.getJSONObject("data")
                                var success = dataObject.getBoolean("success")
                                println(success)
                                if (success) {

                                    val success = Intent(this, OrderPlacedActivity::class.java)
                                    startActivity(success)



                                } else {
                                    var message = dataObject.getString("errorMessage")
                                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                                }

                            } catch (e: Exception) {

                                Toast.makeText(this, "Some Error Occurred!!", Toast.LENGTH_SHORT)
                                    .show()

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



    }
}
