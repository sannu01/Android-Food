package com.internshala.assignment.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.internshala.assignment.R
import com.internshala.assignment.adapter.OrderRecyclerAdapter
import com.internshala.assignment.model.Order
import com.internshala.assignment.model.Order_Food_Item
import com.internshala.assignment.util.GlobalVars
import java.util.HashMap

/**
 * A simple [Fragment] subclass.
 */
class OrderFragment : Fragment() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var recyclerOrder: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerAdapter: OrderRecyclerAdapter

    var orderList= arrayListOf<Order>()
    var foodList= arrayListOf<ArrayList<Order_Food_Item>>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_order, container, false)
        sharedPreferences= activity?.getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE)!!
        val userId=sharedPreferences.getString("id",null)


        recyclerOrder=view.findViewById(R.id.recyclerOrder)
        layoutManager= LinearLayoutManager(activity)

        val queue= Volley.newRequestQueue(activity)
        val url= "http://13.235.250.119/v2/orders/fetch_result/"


        if(GlobalVars.isNetworkConnected){
            val jsnoRequest =
                object: JsonObjectRequest(Method.GET,"$url$userId", null,Response.Listener {

                    try {
                        val dataObject=it.getJSONObject("data")
                        var success=dataObject.getBoolean("success")
                        println(success)
                        if(success){
                            val data=dataObject.getJSONArray("data")
                            for(i in 0 until data.length()){
                                val foods= arrayListOf<Order_Food_Item>()
                                val orderObject=data.getJSONObject(i)
                                val food=orderObject.getJSONArray("food_items")
                                for(j in 0 until food.length()){
                                    val foodObject=food.getJSONObject(j)
                                    val foodItem=Order_Food_Item(
                                        foodObject.getString("food_item_id"),
                                        foodObject.getString("name"),
                                        foodObject.getString("cost")

                                    )

                                    foods.add(foodItem)


                                }
                                foodList.add(foods)



                                val order=Order(
                                    orderObject.getString("order_id"),
                                    orderObject.getString("restaurant_name"),
                                    orderObject.getString("total_cost"),
                                    orderObject.getString("order_placed_at").substring(0..7)
                                )
                                orderList.add(order)

                            }





                            recyclerAdapter= OrderRecyclerAdapter(activity as Context,orderList,foodList)
                            recyclerOrder.adapter=recyclerAdapter
                            recyclerOrder.layoutManager=layoutManager

                        }else{
                            var message=dataObject.getString("errorMessage")
                            Toast.makeText(activity,message, Toast.LENGTH_SHORT).show()
                        }

                    }catch (e:Exception){

                        Toast.makeText(activity,"Some Error Occurred!!", Toast.LENGTH_SHORT).show()

                    }

                }, Response.ErrorListener {

                    Toast.makeText(activity,"Some Error Occurred!!", Toast.LENGTH_SHORT).show()


                }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["context type"] = "application/json"
                        headers["token"] = "72c79d10f45224"
                        return headers
                    }
                }
            queue.add(jsnoRequest)
        }else{
            Toast.makeText(activity,"Couldn't Connect to Internet", Toast.LENGTH_SHORT).show()
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet connection is not Found")
            dialog.setPositiveButton("Open Settings"){ _, _ ->
                val settingsIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){ _, _ ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }








        return view
    }

}
