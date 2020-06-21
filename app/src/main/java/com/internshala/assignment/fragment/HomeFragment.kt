package com.internshala.assignment.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.internshala.assignment.R
import com.internshala.assignment.activity.MainActivity
import com.internshala.assignment.adapter.Global
import com.internshala.assignment.adapter.HomeRecyclerAdapter
import com.internshala.assignment.model.Restaurant
import com.internshala.assignment.util.GlobalVars
import java.util.*
import kotlin.Comparator

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    lateinit var recyclerHome:RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: HomeRecyclerAdapter
    var position:Int=0
    var top:Int=0
    lateinit var progressBar:RelativeLayout
    val restaurantList= arrayListOf<Restaurant>()
    var ratingComparator = Comparator<Restaurant>{restaurant1,restaurant2 ->

        if (restaurant1.rating.compareTo(restaurant2.rating,true)==0){
            restaurant1.name.compareTo(restaurant2.name,true)
        }else{
            restaurant1.rating.compareTo(restaurant2.rating,true)
        }
    }
    var priceComparator = Comparator<Restaurant>{restaurant1,restaurant2 ->

        if (restaurant1.costPerPerson.compareTo(restaurant2.costPerPerson,true)==0){
            restaurant1.name.compareTo(restaurant2.name,true)
        }else{
            restaurant1.costPerPerson.compareTo(restaurant2.costPerPerson,true)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_home, container, false)
        setHasOptionsMenu(true)

        restaurantList.clear()
        recyclerHome=view.findViewById(R.id.recyclerhome)
        layoutManager=LinearLayoutManager(activity)
        progressBar=view.findViewById(R.id.homeprogress)

        val queue= Volley.newRequestQueue(activity as Context)
        val url= "http://13.235.250.119/v2/restaurants/fetch_result/"


        if(GlobalVars.isNetworkConnected){
            val jsnoRequest =
                object: JsonObjectRequest(Method.GET,url, null,Response.Listener {

                    try {
                        val dataObject=it.getJSONObject("data")
                        var success=dataObject.getBoolean("success")
                        println(success)
                        if(success){
                            val data=dataObject.getJSONArray("data")
                            for (i in 0 until data.length()){
                                val restaurantObject=data.getJSONObject(i)
                                val restaurant=Restaurant(
                                    restaurantObject.getString("id"),
                                    restaurantObject.getString("name"),
                                    restaurantObject.getString("rating"),
                                    restaurantObject.getString("cost_for_one"),
                                    restaurantObject.getString("image_url")
                                )
                                restaurantList.add(restaurant)
                            }
                            progressBar.visibility=View.GONE
                            recyclerAdapter=HomeRecyclerAdapter(activity as Context,restaurantList)
                            recyclerHome.adapter=recyclerAdapter
                            recyclerHome.layoutManager=layoutManager

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
            progressBar.visibility=View.GONE
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

    override fun onPause() {
        super.onPause()
        position= (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        val v:View?=layoutManager.getChildAt(0)
        if (v==null) {
            top = 0
        }
        else
        {
            top=(v.top - layoutManager.paddingTop)
        }

    }

    override fun onResume() {
        super.onResume()
        if (Global.main_flag==1){
            Global.main_flag=0
            recyclerHome.adapter=recyclerAdapter
        }

        (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position,top)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_home,menu)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id =item?.itemId
        if(id==R.id.sortrating){
            item.isChecked=true
            Collections.sort(restaurantList,ratingComparator)
            restaurantList.reverse()
            recyclerAdapter.notifyDataSetChanged()
        }
        if(id==R.id.priceLtoH){
            item.isChecked=true
            Collections.sort(restaurantList,priceComparator)
            recyclerAdapter.notifyDataSetChanged()
        }
        if(id==R.id.priceHtoL){
            item.isChecked=true
            Collections.sort(restaurantList,priceComparator)
            restaurantList.reverse()
            recyclerAdapter.notifyDataSetChanged()
        }

        return super.onOptionsItemSelected(item)
    }

}
