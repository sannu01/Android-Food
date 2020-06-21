package com.internshala.assignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.internshala.assignment.R
import com.internshala.assignment.model.Cart

class CartRecyclerAdapter(val context:Context,val itemList:ArrayList<Cart>):RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder>() {

    class CartViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtcounter:TextView=view.findViewById(R.id.cartcounter)
        val txtfoodname:TextView=view.findViewById(R.id.cartfoodname)
        val txtxfoodprice:TextView=view.findViewById(R.id.cartfoodprice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {

        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_single_row,parent,false)

        return CartRecyclerAdapter.CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val food=itemList[position]
        holder.txtcounter.text=(position+1).toString()
        holder.txtfoodname.text=food.food_item_name
        holder.txtxfoodprice.text=food.food_cost
    }
}