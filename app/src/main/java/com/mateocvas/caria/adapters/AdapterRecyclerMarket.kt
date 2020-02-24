package com.mateocvas.caria.adapters

import android.app.Dialog
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mateocvas.caria.R
import com.mateocvas.caria.items.ItemProduct
import kotlinx.android.synthetic.main.item_market.view.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mateocvas.caria.Funciones
import com.mateocvas.caria.GlideApp
import com.mateocvas.caria.ui.bascket.BascketFragment


class AdapterRecyclerMarket(val context:BascketFragment?, val items: ArrayList<ItemProduct>) : RecyclerView.Adapter<AdapterRecyclerMarket.ViewHolder>() {
    val base=FirebaseStorage.getInstance().reference
    val funcion= Funciones()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)
        holder.bind(item)

        if (context!=null)
        holder.view.setOnClickListener {
            context.showWindow(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_market, parent, false),base ,funcion)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(val view: View,val base:StorageReference,funciones: Funciones) : RecyclerView.ViewHolder(view) {
        val nombreMostrar = view.imarket_tv_nombre as TextView
        val precio = view.imarket_tv_total as TextView
        val cantidad = view.imarket_tv_cantidad as TextView
        val icono = view.imarket_iv_icono as ImageView
        val card=view.imarket_cv_card as CardView
        val funciones=funciones

        fun bind(item: ItemProduct) {
            nombreMostrar.text = item.nombreMostrar
            cantidad.text=item.cantidad.toString()

            if(item.array.size==0)
                precio.text = item.precio
            else
                precio.text=funciones.formato((funciones.desformato(item.precio)*item.porcentaje[item.tamano]).toLong())

            if(item.path.equals(view.context.getString(R.string.tag_food)))
                card.setCardBackgroundColor(Color.GREEN)
            else if(item.path.equals(view.context.getString(R.string.tag_medicinal))){
                card.setCardBackgroundColor(Color.GREEN)
            }



                    GlideApp.with(view.context)
                .load(base.child(item.path +"/"+item.nombre+".png"))
                .into( icono )

        }
    }
}