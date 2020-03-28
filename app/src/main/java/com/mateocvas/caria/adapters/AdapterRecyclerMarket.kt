package com.mateocvas.caria.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mateocvas.caria.Funciones
import com.mateocvas.caria.GlideApp
import com.mateocvas.caria.R
import com.mateocvas.caria.items.ItemProduct
import com.mateocvas.caria.ui.bascket.BascketFragment
import kotlinx.android.synthetic.main.item_market.view.*


class AdapterRecyclerMarket(val context:BascketFragment?, val items: ArrayList<ItemProduct>) : RecyclerView.Adapter<AdapterRecyclerMarket.ViewHolder>() {
    private val base=FirebaseStorage.getInstance().reference
    private val funcion= Funciones()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        if (context!=null)
        holder.view.setOnClickListener {
            context.showWindow(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if(context==null)
            ViewHolder(layoutInflater.inflate(R.layout.item_market, parent, false),base ,funcion,context,true)
        else
            ViewHolder(layoutInflater.inflate(R.layout.item_market, parent, false),base ,funcion,context,false)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(val view: View,
                     private val base:StorageReference, funciones: Funciones, val context:BascketFragment?,
                     private val con:Boolean) : RecyclerView.ViewHolder(view) {
        private val nombreMostrar = view.imarket_tv_nombre as TextView
        private val precio = view.imarket_tv_total as TextView
        private val cantidad = view.imarket_tv_cantidad as TextView
        private val icono = view.imarket_iv_icono as ImageView
        private val elimine = view.imarket_ib_elimine as ImageButton
        private val card=view.imarket_cv_card as CardView
        private val funciones=funciones

        fun bind(item: ItemProduct) {
            nombreMostrar.text = item.nombreMostrar
            cantidad.text=item.cantidad.toString()



            if(item.array1.size==0)
                precio.text = item.precio
            else
                precio.text=funciones.formato((funciones.desformato(item.precio)*item.porcentaje1[item.ind1]).toLong())

            if(item.path == view.context.getString(R.string.tag_food))
                card.setCardBackgroundColor(Color.GREEN)
            else if(item.path == view.context.getString(R.string.tag_medicinal)){
                card.setCardBackgroundColor(Color.GREEN)
            }

            if(con)
                elimine.visibility=View.GONE
             else
            elimine.setOnClickListener {
                context!!.delete(item)
            }

                    GlideApp.with(view.context)
                .load(base.child(item.path +"/"+item.nombre+".png"))
                .into( icono )

        }
    }
}