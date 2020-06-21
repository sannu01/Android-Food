package com.internshala.assignment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.internshala.assignment.R
import com.internshala.assignment.model.Order
import com.internshala.assignment.model.Order_Food_Item
import kotlinx.android.synthetic.main.order_single_row.view.*

class OrderRecyclerAdapter(val context:Context, val itemList:ArrayList<Order>,val food_item:ArrayList<ArrayList<Order_Food_Item>>):RecyclerView.Adapter<OrderRecyclerAdapter.OrderRecyclerHolder>() {


    class OrderRecyclerHolder(view: View):RecyclerView.ViewHolder(view){
        val txtRestaurant_name:TextView=view.findViewById(R.id.order_restaurant_name)
        val txtorder_id:TextView=view.findViewById(R.id.order_id)
        val txtoredr_time:TextView=view.findViewById(R.id.ordertime)
        val total_cost:TextView=view.findViewById(R.id.order_total_cost)
        val food_item:LinearLayout=view.findViewById(R.id.food_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderRecyclerHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.order_single_row,parent,false)

        return OrderRecyclerAdapter.OrderRecyclerHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: OrderRecyclerHolder, position: Int) {
        val order=itemList[position]
        var food=food_item[position]
        holder.txtRestaurant_name.text=order.restaurant_name
        holder.txtorder_id.text=("Order#"+order.order_id)
        holder.txtoredr_time.text=order.order_placed_at
        holder.total_cost.text=("Rs."+order.total_cost)
        holder.food_item.removeAllViews()
        for (i in 0 until food.size){
            val foods=food[i]

            val textcount=TextView(context)
            var countparam=LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            )

            countparam.setMargins(25,5,5,10)

            textcount.layoutParams=countparam

            textcount.textSize=16f
            val relativeLayout=LinearLayout(context)
            val text=TextView(context)
            var layoutParams=LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                7f


            )
            layoutParams.setMargins(10,5,5,10)
            text.layoutParams=layoutParams
            text.setPadding(5,5,5,5)

            text.textSize=16f
            relativeLayout.weightSum= 10F
            relativeLayout.orientation=LinearLayout.HORIZONTAL
            val txtprice=TextView(context)
            var playoutParams=LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                2f
            )
            playoutParams.setMargins(10,5,5,10)
            txtprice.layoutParams=playoutParams
            text.setTextColor(R.color.black)
            textcount.setTextColor(R.color.black)
            txtprice.setTextColor(R.color.black)

            txtprice.textSize=16f
            text.text=foods.food_name
            txtprice.text=("Rs."+foods.cost)
            textcount.text= (i+1).toString()
            relativeLayout.addView(textcount)
            relativeLayout.addView(text)
            relativeLayout.addView(txtprice)
            holder.food_item.addView(relativeLayout)

        }



    }
}