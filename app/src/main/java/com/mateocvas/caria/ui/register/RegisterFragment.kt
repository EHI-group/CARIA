package com.mateocvas.caria.ui.register

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mateocvas.caria.R
import com.mateocvas.caria.adapters.AdapterRecyclerMarket
import com.mateocvas.caria.adapters.AdapterRecyclerOrder
import com.mateocvas.caria.items.ItemOrder
import com.mateocvas.caria.ui.Comunication
import kotlinx.android.synthetic.main.fragment_historial.view.*
import kotlinx.android.synthetic.main.ventana_confirmar.*
import kotlinx.android.synthetic.main.ventana_pedido_seleccionado.*
import java.lang.Exception

class RegisterFragment : Fragment() {


    private val data=ArrayList<ItemOrder>()
    private lateinit var comunication: Comunication
    private lateinit var root:View
    private lateinit var order:ItemOrder
    private lateinit var adapter:AdapterRecyclerOrder

    override fun onResume() {

        super.onResume()


        view!!.isFocusableInTouchMode = true
        view!!.requestFocus()
        view!!.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                val dialog=Dialog(root.context)
                dialog.setContentView(R.layout.ventana_pedido_seleccionado)
                dialog.setContentView(R.layout.ventana_confirmar)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
                dialog.vconfirm_bt_cancelar.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.vconfirm_bt_aceptar.setOnClickListener {
                    activity!!.finish()

                }
                dialog.show()

                true

            } else false
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.fragment_historial, container, false)
        begin()
        viewsSettings()
        setObservers()
        return root
    }




    fun begin(){
        val preferences = root.context.getSharedPreferences("user", Context.MODE_PRIVATE)

        comunication=activity?.run {
            ViewModelProviders.of(this).get(Comunication::class.java)
        }?:throw Exception("Invalid Activity")

        adapter=AdapterRecyclerOrder(this,data,preferences.getInt("numero",0))

    }


    // settings for the recycler view
    private fun viewsSettings(){


        root.fregistro_rv_lista.setHasFixedSize(false)
        root.fregistro_rv_lista.layoutManager = LinearLayoutManager(root.context)
        root.fregistro_rv_lista.adapter=adapter
        adapter.notifyDataSetChanged()
    }

    // comunication (data pedidos) --- model(data) --- model (selected item)
    private fun setObservers(){
        comunication.com_data_pedidos.observe(this, Observer {
            data.clear()
            data.addAll(it)
            adapter.notifyDataSetChanged()
        })




    }

    fun setItemOrder(item:ItemOrder){
        order=item
        openWindow(item)

    }

    // starts dialog for a click event
    private fun openWindow(item:ItemOrder){


        val dialog=Dialog(root.context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.ventana_pedido_seleccionado)
        var adapter=AdapterRecyclerMarket(null,item.fruver )
        dialog.vpedsel_rv_fruver.adapter=adapter
        dialog.vpedsel_rv_fruver.setHasFixedSize(false)
        dialog.vpedsel_rv_fruver.layoutManager = LinearLayoutManager(root.context)
        adapter.notifyDataSetChanged()

        adapter=AdapterRecyclerMarket(null,item.food )
        dialog.vpedsel_rv_food.adapter=adapter
        dialog.vpedsel_rv_food.setHasFixedSize(false)
        dialog.vpedsel_rv_food.layoutManager = LinearLayoutManager(root.context)
        adapter.notifyDataSetChanged()

        adapter=AdapterRecyclerMarket(null,item.medicinal )
        dialog.vpedsel_rv_medicinal.adapter=adapter
        dialog.vpedsel_rv_medicinal.setHasFixedSize(false)
        dialog.vpedsel_rv_medicinal.layoutManager = LinearLayoutManager(root.context)
        adapter.notifyDataSetChanged()

        dialog.abask_bt_enviar.setOnClickListener {
            comunication.setOrder(order)
            dialog.dismiss()
            Toast.makeText(root.context,root.context.getString(R.string.toast_cargado_en_carrito),Toast.LENGTH_LONG).show()
        }

        dialog.show()

    }
}