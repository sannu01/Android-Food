package com.internshala.assignment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.internshala.assignment.R
import com.internshala.assignment.model.Cart
import com.internshala.assignment.model.Menu
import kotlinx.android.synthetic.main.menu_activity_single_row.view.*

class MenuRecyclerAdapter(val context: Context, val itemList: ArrayList<Menu>, val menuClickListener: MenuClickListener):RecyclerView.Adapter<MenuRecyclerAdapter.MenuViewHolder>() {

    class MenuViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtcount:TextView=view.findViewById(R.id.txtcounter)
        val txtmenuName:TextView=view.findViewById(R.id.txtmenuname)
        val txtMenuPrice:TextView=view.findViewById(R.id.txtmenuprice)
        val btnaddcart:Button=view.findViewById(R.id.btnaddcart)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.menu_activity_single_row,parent,false)

        return MenuRecyclerAdapter.MenuViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu=itemList[position]
        holder.txtmenuName.text=menu.name
        holder.txtMenuPrice.text=menu.cost_for_one
        holder.txtcount.text= (position+1).toString()
        holder.btnaddcart.text="Add"
        var add=0
        var index=-1
        holder.btnaddcart.setOnClickListener {

            for(i in 0 until Global.cartList.size){
                if (menu.id==Global.cartList[i].food_item_id){
                   add=1
                    index=i

                }
            }
            if (add==1){
                Global.cartList.removeAt(index)
                holder.btnaddcart.text="Add"
                holder.btnaddcart.setBackgroundResource(R.color.colorPrimary)
                index=-1
                add=0

            }else{
                val cart=Cart(
                    menu.id,
                    menu.name,
                    menu.cost_for_one
                )
                Global.cartList.add(cart)
                holder.btnaddcart.text="Remove"
                holder.btnaddcart.setBackgroundResource(R.color.colorAccent)
            }
            menuClickListener.checkCartStatus()
        }

    }
}