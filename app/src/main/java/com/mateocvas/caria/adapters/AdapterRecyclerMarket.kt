package com.mateocvas.caria.adapters

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.mateocvas.caria.R
import com.mateocvas.caria.items.ItemProduct
import kotlinx.android.synthetic.main.item_market.view.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mateocvas.caria.GlideApp
import com.mateocvas.caria.ui.bascket.BascketFragment
import com.mateocvas.caria.ui.Comunication
import java.lang.Exception


class AdapterRecyclerMarket(val context:BascketFragment?, val items: ArrayList<ItemProduct>, val comunication: Comunication) : RecyclerView.Adapter<AdapterRecyclerMarket.ViewHolder>() {
    val base=FirebaseStorage.getInstance().reference

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)
        holder.bind(item)



        if (context!=null)
        holder.view.setOnClickListener {
            context.showWindow(item)
        }

        holder.eliminar.setOnClickListener {

            removeItems(position)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_market, parent, false),base )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(val view: View,val base:StorageReference) : RecyclerView.ViewHolder(view) {
        val nombreMostrar = view.imarket_tv_nombre as TextView
        val precio = view.imarket_tv_total as TextView
        val cantidad = view.imarket_tv_cantidad as TextView
        val icono = view.imarket_iv_icono as ImageView
        val card=view.imarket_cv_card as CardView
        val eliminar= view.iv_cart_delete as ImageView



        fun bind(item: ItemProduct) {
            nombreMostrar.text = item.nombreMostrar
            precio.text = item.precio
            cantidad.text=item.cantidad.toString()



            if(item.path.equals(view.context.getString(R.string.tag_food)))
                card.setCardBackgroundColor(Color.GREEN)
            else if(item.path.equals(view.context.getString(R.string.tag_medicinal))){
                card.setCardBackgroundColor(Color.GREEN)
            }



                    GlideApp.with(view.context)
                .load(base.child(item.path +"/"+item.nombre+".png"))
                .into( icono  )

        }
    }

    fun removeItems(position: Int) {
        //items.removeAt(position)
        comunication.removeItemBascket(items.get(position))


        notifyItemRemoved(position)
        notifyDataSetChanged()
        Toast.makeText(context?.context,R.string.toast_producto_eliminado,Toast.LENGTH_SHORT).show()
    }
}