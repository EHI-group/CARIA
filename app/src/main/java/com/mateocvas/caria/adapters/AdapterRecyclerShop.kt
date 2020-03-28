package com.mateocvas.caria.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.mateocvas.caria.ui.shoop.ShoopFragment
import kotlinx.android.synthetic.main.item_shoop.view.*


class AdapterRecyclerShop(
    val context: ShoopFragment,
    val items: ArrayList<ItemProduct>
) : RecyclerView.Adapter<AdapterRecyclerShop.ViewHolder>() {
    private val base=FirebaseStorage.getInstance().reference

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
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

    class ViewHolder(val view: View, private val base:StorageReference) : RecyclerView.ViewHolder(view) {
        private val nombreMostrar = view.ishoop_tv_nombre as TextView
        private val precio = view.ishoop_tv_precio as TextView
        private val icono = view.ishoop_iv_icono as ImageView
        private val description = view.ishoop_tv_descripcion as TextView
        private val precioxunidad = view.ishoop_tv_precoxunidad as TextView
        private val card=view.ishoop_cv_card as CardView
        val context= view.context!!
        private val funciones=Funciones()

        fun bind(item: ItemProduct) {
            nombreMostrar.text = item.nombreMostrar
            precio.text = item.precio
            if(item.unidad == "1"){
                description.text = ("1 ${item.nombreMostrar} (a granel).")
                precioxunidad.text = ("Precio x unidad: ${item.precio}")
            }
            else{
                description.text = ("${item.unidad} de ${item.nombreMostrar}.")
                val numero = item.unidad.replace("[^0-9]".toRegex(), "").toInt()
                if(item.unidad.contains("libra"))
                    precioxunidad.text = ("Precio x gramo : "+funciones.formato(funciones.desformato(item.precio)/(numero*500)))
                else
                    precioxunidad.text = ("Precio x gramo: "+funciones.formato(funciones.desformato(item.precio)/(numero)))
            }


            if(item.path == view.context.getString(R.string.tag_food)){
                if(!item.alreadyBougtth)
                    card.setCardBackgroundColor(context.getColor(R.color.color_withe))
                else
                    card.setCardBackgroundColor(context.getColor(R.color.selected_food))
            }

            else if(item.path == view.context.getString(R.string.tag_medicinal)){

                if(!item.alreadyBougtth)
                    card.setCardBackgroundColor(context.getColor(R.color.color_withe))
                else
                    card.setCardBackgroundColor(context.getColor(R.color.selected_food))
            }
            else{
                if(!item.alreadyBougtth)
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