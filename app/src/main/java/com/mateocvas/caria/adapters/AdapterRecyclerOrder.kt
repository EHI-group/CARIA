package com.mateocvas.caria.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.mateocvas.caria.R
import com.mateocvas.caria.items.ItemOrder
import com.mateocvas.caria.ui.register.RegisterFragment
import kotlinx.android.synthetic.main.item_order.view.*

class AdapterRecyclerOrder(val context: RegisterFragment, val items: ArrayList<ItemOrder>, val numero:Int) : RecyclerView.Adapter<AdapterRecyclerOrder.ViewHolder>() {






    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val slected_order= items.get(position)
        holder.bind(slected_order)
        holder.view.setOnClickListener {
            context.model.setItemOrder(slected_order)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_order, parent, false),numero )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(val view: View,val numero:Int) : RecyclerView.ViewHolder(view) {
        val number = view.iorder_tv_number as TextView
        val date = view.iorder_tv_date as TextView
        val estado=view.iorder_tv_state as TextView

        fun bind(item: ItemOrder) {
            number.text = item.numero.toString()
            date.text = item.fecha
            if(item.numero<numero)
                estado.text =view.context.getString(R.string.estado_entregado)
            else
                estado.text =view.context.getString(R.string.estado_espera)

        }
    }
}