package com.mateocvas.caria.adapters

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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mateocvas.caria.GlideApp
import com.mateocvas.caria.ui.shoop.ShoopFragment
import com.mateocvas.caria.ui.shoop.ShoopViewModel
import kotlinx.android.synthetic.main.item_shoop.view.*


class AdapterRecyclerShop(val context: ShoopFragment, val items: ArrayList<ItemProduct>, val model:ShoopViewModel) : RecyclerView.Adapter<AdapterRecyclerShop.ViewHolder>() {
    val base=FirebaseStorage.getInstance().reference

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)
        holder.bind(item)
        holder.view.setOnClickListener {
            context.model.itemSlected(item)

        }
    }





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_shoop, parent, false),base )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(val view: View,val base:StorageReference) : RecyclerView.ViewHolder(view) {
        val nombreMostrar = view.ishoop_tv_nombre as TextView
        val precio = view.ishoop_tv_precio as TextView
        val icono = view.ishoop_iv_icono as ImageView
        val card=view.ishoop_cv_card as CardView
        val context=view.context

        fun bind(item: ItemProduct) {
            nombreMostrar.text = item.nombreMostrar
            precio.text = item.precio


            if(item.path.equals(view.context.getString(R.string.tag_food))){
                if(item.alreadyBougtth==false)
                    card.setCardBackgroundColor(context.getColor(R.color.color_withe))
                else
                    card.setCardBackgroundColor(context.getColor(R.color.selected_food))
            }

            else if(item.path.equals(view.context.getString(R.string.tag_medicinal))){

                if(item.alreadyBougtth==false)
                    card.setCardBackgroundColor(context.getColor(R.color.color_withe))
                else
                    card.setCardBackgroundColor(context.getColor(R.color.selected_food))
            }
            else{
                if(item.alreadyBougtth==false)
                    card.setCardBackgroundColor(context.getColor(R.color.fruver))
                else
                    card.setCardBackgroundColor(context.getColor(R.color.slected_fruver))

            }

            GlideApp.with(view.context)
                .load(base.child(item.path +"/"+item.nombre+".png"))
                .into( icono  )
        }
    }
}