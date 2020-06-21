package com.internshala.assignment.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.internshala.assignment.R
import com.internshala.assignment.adapter.FavouriteRecyclerAdapter
import com.internshala.assignment.adapter.Global
import com.internshala.assignment.adapter.HomeRecyclerAdapter
import com.internshala.assignment.database.RestaurantDatabase
import com.internshala.assignment.database.RestaurantEntity
import com.internshala.assignment.model.Restaurant
import com.internshala.assignment.util.GlobalVars
import java.util.HashMap

/**
 * A simple [Fragment] subclass.
 */
class FavouriteFragment : Fragment() {

    lateinit var recyclerFavourite: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavouriteRecyclerAdapter
    lateinit var nofavlayout:RelativeLayout
    lateinit var favProgress:RelativeLayout
    val restaurantList= arrayListOf<Restaurant>()
    var dbrestaurantList= arrayListOf<RestaurantEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_favourite, container, false)
        dbrestaurantList.clear()
        restaurantList.clear()
        recyclerFavourite=view.findViewById(R.id.recyclerfavourite)
        layoutManager= GridLayoutManager(activity as Context,2)
        nofavlayout=view.findViewById(R.id.nofavlayout)
        favProgress=view.findViewById(R.id.favouriteProgress)
        nofavlayout.visibility=View.GONE
        dataLoad()

        return view
    }

    class RetrieveFavourite(val context:Context):AsyncTask<Void,Void,List<RestaurantEntity>>(){
        override fun doInBackground(vararg params: Void?): List<RestaurantEntity> {

            val db = Room.databaseBuilder(context,RestaurantDatabase::class.java,"restaurant-db").build()
            return db.restaurantDao().getAllRestaurant()

        }

    }
    fun dataLoad(){

        dbrestaurantList.clear()
        restaurantList.clear()
        dbrestaurantList= RetrieveFavourite(activity as Context).execute().get() as ArrayList<RestaurantEntity>
        if (dbrestaurantList.size==0){
            recyclerFavourite.visibility=View.GONE
            favProgress.visibility=View.GONE
            nofavlayout.visibility=View.VISIBLE
        }
        val queue= Volley.newRequestQueue(activity as Context)
        val url= "http://13.235.250.119/v2/restaurants/fetch_result/"
        if(GlobalVars.isNetworkConnected){
            val jsnoRequest =
                object: JsonObjectRequest(Method.GET,url, null, Response.Listener {

                    try {
                        val dataObject=it.getJSONObject("data")
                        var success=dataObject.getBoolean("success")
                        println(success)
                        if(success){
                            var k=0
                            val data=dataObject.getJSONArray("data")
                            for (j in 0 until dbrestaurantList.size) {
                                val dbrestaurant = dbrestaurantList[j]

                                loop@ for (i in k until data.length()) {
                                    val restaurantObject = data.getJSONObject(i)
                                    if (restaurantObject.getString("id")==dbrestaurant.restaurant_id.toString()) {
                                        val restaurant = Restaurant(
                                            restaurantObject.getString("id"),
                                            restaurantObject.getString("name"),
                                            restaurantObject.getString("rating"),
                                            restaurantObject.getString("cost_for_one"),
                                            restaurantObject.getString("image_url")
                                        )
                                        restaurantList.add(restaurant)
                                        k=i+1
                                        break@loop
                                    }
                                }
                            }
                            favProgress.visibility=View.GONE
                            recyclerAdapter=
                                FavouriteRecyclerAdapter(activity as Context,restaurantList)
                            recyclerFavourite.adapter=recyclerAdapter
                            recyclerFavourite.layoutManager=layoutManager

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
            favProgress.visibility=View.GONE
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

    }


    override fun onResume() {
        super.onResume()
        if (Global.fav_flag==1){
            Global.fav_flag=0
            dataLoad()

        }

    }


}
