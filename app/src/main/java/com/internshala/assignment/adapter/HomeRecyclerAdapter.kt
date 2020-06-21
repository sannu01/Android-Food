package com.internshala.assignment.adapter

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
import com.internshala.assignment.activity.MenuActivity
import com.internshala.assignment.database.RestaurantEntity
import com.internshala.assignment.model.Menu
import com.internshala.assignment.model.Restaurant
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_fragment_single_row.view.*
import android.os.Handler

class HomeRecyclerAdapter(val context:Context,val itemList:ArrayList<Restaurant>) :RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>(){


    class HomeViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtRestaurantName:TextView=view.findViewById(R.id.nameRestaurant)
        val txtprice:TextView=view.findViewById(R.id.price)
        val txtRating:TextView=view.findViewById(R.id.restaurantRating)
        val imgfav:ImageView=view.findViewById(R.id.imgfavicon)
        val imgRestaurant:ImageView=view.findViewById(R.id.imgrestaurant)
        val llcontent:LinearLayout=view.findViewById(R.id.llcontenthome)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view=LayoutInflater.from(parent.context)
            .inflate(R.layout.home_fragment_single_row,parent,false)

        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val restaurant=itemList[position]
        holder.txtRestaurantName.text=restaurant.name
        holder.txtRating.text=restaurant.rating
        holder.txtprice.text=(restaurant.costPerPerson+" /Person")
        Picasso.get().load(restaurant.image_url).error(R.mipmap.ic_launcher).into(holder.imgRestaurant)
        holder.imgfav.setImageResource(R.drawable.ic_unfav)


        val restaurantEntity =RestaurantEntity(
            restaurant.id.toInt(),
            restaurant.name
        )

        val checkfav = MenuActivity.DBAsyncTask(context,restaurantEntity,1).execute()

        val isfav= checkfav.get()
        if (isfav){
            holder.imgfav.setImageResource(R.drawable.ic_fav)
        }

        holder.llcontent.setOnClickListener {
            Global.main_flag=1
            val intent= Intent(context,MenuActivity::class.java)
            intent.putExtra("id",restaurant.id)
            intent.putExtra("name",restaurant.name)
            context.startActivity(intent)

        }




    }

}
