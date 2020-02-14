package com.mateocvas.caria.ui.register

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.ventana_pedido_seleccionado.*
import java.lang.Exception

class RegisterFragment : Fragment() {

    lateinit var model: RegisterViewModel
    private lateinit var comunication: Comunication
    private lateinit var root:View
    private lateinit var adapter:AdapterRecyclerOrder
    private lateinit var dialog: Dialog
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

        model = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        adapter=AdapterRecyclerOrder(this,model.data,preferences.getInt("numero",0))

        dialog= Dialog(root.context)
        dialog.setContentView(R.layout.ventana_pedido_seleccionado)
    }



    // settings for the recycler view
    fun viewsSettings(){


        root.fregistro_rv_lista.setHasFixedSize(false)
        root.fregistro_rv_lista.layoutManager = LinearLayoutManager(root.context)
        root.fregistro_rv_lista.adapter=adapter
        adapter.notifyDataSetChanged()
    }

    // comunication (data pedidos) --- model(data) --- model (selected item)
    fun setObservers(){
        comunication.com_data_pedidos.observe(this, Observer {
        model.setData(it)
        })

        model.data_com.observe(this, Observer {
        adapter.notifyDataSetChanged()
        })

        model.selected_item_com.observe(this, Observer {
            openWindow(it)
        })

    }

    // starts dialog for a click event
    fun openWindow(item:ItemOrder){

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
            comunication.setOrder(model.selected_item)
        }

        dialog.show()

    }
}