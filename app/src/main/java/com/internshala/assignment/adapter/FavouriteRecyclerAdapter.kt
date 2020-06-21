package com.internshala.assignment.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.internshala.assignment.R
import com.internshala.assignment.activity.LoginActivity
import com.internshala.assignment.activity.MenuActivity
import com.internshala.assignment.database.RestaurantEntity
import com.internshala.assignment.model.Restaurant
import com.squareup.picasso.Picasso
import android.os.Handler

class FavouriteRecyclerAdapter(val context: Context, val itemList:ArrayList<Restaurant>):RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>(){

    class FavouriteViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtRestaurantName: TextView =view.findViewById(R.id.favnamerestaurant)
        val txtprice: TextView =view.findViewById(R.id.favcostrestaurant)
        val txtRating: TextView =view.findViewById(R.id.favrestsurantrating)
        val imgRestaurant: ImageView =view.findViewById(R.id.favimgrestaurant)
        val llcontent: LinearLayout =view.findViewById(R.id.llcotentfavourite)
        val imgfav:ImageView=view.findViewById(R.id.favrestaurantfavicon)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.favourite_single_row,parent,false)

        return FavouriteRecyclerAdapter.FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val restaurant=itemList[position]
        holder.txtRestaurantName.text=restaurant.name
        holder.txtRating.text=restaurant.rating
        holder.txtprice.text=(restaurant.costPerPerson+" /Person")
        Picasso.get().load(restaurant.image_url).error(R.mipmap.ic_launcher).into(holder.imgRestaurant)

        holder.llcontent.setOnClickListener {
            Global.fav_flag=1
            val intent= Intent(context, MenuActivity::class.java)
            intent.putExtra("id",restaurant.id)
            intent.putExtra("name",restaurant.name)
            context.startActivity(intent)

        }

        val restaurantEntity = RestaurantEntity(
            restaurant.id.toInt(),
            restaurant.name
        )

        holder.imgfav.setOnClickListener {
            val async = MenuActivity.DBAsyncTask(context, restaurantEntity, 3).execute()
                val result = async.get()
                if (result) {
                    itemList.removeAt(position)
                    holder.llcontent.animate().alpha(0f).setDuration(500)

                    Handler().postDelayed({

                        holder.llcontent.animate().alpha(1f)
                        notifyDataSetChanged()
                    }, 500)


                }
            }



    }
}