package com.mateocvas.caria.ui.bascket

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.storage.FirebaseStorage
import com.mateocvas.caria.Funciones
import com.mateocvas.caria.GlideApp
import com.mateocvas.caria.R
import com.mateocvas.caria.Warinings
import com.mateocvas.caria.adapters.AdapterRecyclerMarket
import com.mateocvas.caria.items.ItemProduct
import com.mateocvas.caria.ui.Comunication
import kotlinx.android.synthetic.main.activity_basket.view.*
import kotlinx.android.synthetic.main.ventana_confirmar.*
import kotlinx.android.synthetic.main.ventana_confirmar_envio.*
import kotlinx.android.synthetic.main.ventana_producto_modificar_fruver.*


class BascketFragment : Fragment(), View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    lateinit var root: View

    private lateinit var comunication: Comunication
    private lateinit var dialog: Dialog
    private var total: Long = 0
    private var cant_ini = 0
    private var amount = 0
    private var ready = false
    private val cloud_storage = FirebaseStorage.getInstance()

    private val funciones = Funciones()

    private val data_fruver = ArrayList<ItemProduct>()
    private val data_food = ArrayList<ItemProduct>()
    private val data_medicinal = ArrayList<ItemProduct>()

    private val warnings = Warinings()


    private var tipo: String = ""
    private lateinit var data_fruver_adapter: AdapterRecyclerMarket
    private lateinit var data_food_adapter: AdapterRecyclerMarket
    private lateinit var data_medicinal_adapter: AdapterRecyclerMarket

    private lateinit var selected_item: ItemProduct


    override fun onResume() {

        super.onResume()

        view!!.isFocusableInTouchMode = true
        view!!.requestFocus()
        view!!.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {


                val dialog = Dialog(root.context)
                dialog.setContentView(R.layout.ventana_confirmar)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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


    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

        if (ready) {
            if (p0!!.id == R.id.aprosel_sb_slider1) {
                dialog.aprosel_tv_slider1.text = (tipo + " " + selected_item.array1[p1])
                var new_amounto = ( funciones.desformato(selected_item.precio) * selected_item.porcentaje1[p1]).toLong()
                if (selected_item.ind2!=-1)
                    new_amounto=((new_amounto)*selected_item.porcentaje2[dialog.aprosel_sb_slider2.progress]).toLong()
                dialog.aprosel_tv_total.text = funciones.formato(total + (amount*new_amounto))
                dialog.aprosel_tv_preciunitario.text = funciones.formato(new_amounto)
            } else {
                dialog.aprosel_tv_slider2.text = (selected_item.tipo2 + " " + selected_item.array2[p1])
                val new_amounto = (  funciones.desformato(selected_item.precio) * selected_item.porcentaje1[dialog.aprosel_sb_slider1.progress] * selected_item.porcentaje2[p1]).toLong()
                dialog.aprosel_tv_total.text = funciones.formato(total + (amount*new_amounto))
                dialog.aprosel_tv_preciunitario.text = funciones.formato(new_amounto)
            }

        }

    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        root = inflater.inflate(R.layout.activity_basket, container, false)

        begin()
        setObservers()
        setListeners()

        return root
    }

    // here we set the recyclers configuration and we instance the vm
    fun begin() {
        dialog = Dialog(root.context)
        data_fruver_adapter = AdapterRecyclerMarket(this, data_fruver)
        data_food_adapter = AdapterRecyclerMarket(this, data_food)
        data_medicinal_adapter = AdapterRecyclerMarket(this, data_medicinal)


        comunication = activity?.run {
            ViewModelProviders.of(this).get(Comunication::class.java)
        } ?: throw Exception("Invalid Activity")




        root.abask_rv_fruver.setHasFixedSize(false)
        root.abask_rv_fruver.layoutManager = LinearLayoutManager(root.context)
        root.abask_rv_fruver.adapter = data_fruver_adapter

        root.abask_rv_food.setHasFixedSize(false)
        root.abask_rv_food.layoutManager = LinearLayoutManager(root.context)
        root.abask_rv_food.adapter = data_food_adapter

        root.abask_rv_medicinal.setHasFixedSize(false)
        root.abask_rv_medicinal.layoutManager = LinearLayoutManager(root.context)
        root.abask_rv_medicinal.adapter = data_medicinal_adapter
    }

    //here we set the listners to the buttons envuar and limpiar
    private fun setListeners() {
        root.abask_bt_enviar.setOnClickListener(this)
        root.abask_bt_limpiar.setOnClickListener(this)
    }

    private fun setObservers() {

        //  comunication.bascket_fruver_com.removeObservers(activity!!)
        comunication.bascket_fruver_com.observe(activity!!, Observer {
            data_fruver.clear()
            data_fruver.addAll(it)
            data_fruver_adapter.notifyDataSetChanged()
        })
        comunication.bascket_food_com.observe(this, Observer {
            data_food.clear()
            data_food.addAll(it)
            data_food_adapter.notifyDataSetChanged()
        })

        comunication.bascket_medicinal_com.observe(this, Observer {
            data_medicinal.clear()
            data_medicinal.addAll(it)
            data_fruver_adapter.notifyDataSetChanged()
        })


        comunication.com_total.observe(this, Observer {
            root.abask_tv_total.text = it
        })


    }


    private fun clickClearBascket() {
        comunication.deleteAllBascket()
        Toast.makeText(
            root.context,
            this.getString(R.string.toast_carrito_vaciado),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun clickMinusDialog() {
        if (amount == 1)
            warnings.prosel_minus(dialog.context)
        else {
            amount--
            dialog.aprosel_tv_unidad.text = amount.toString()
            var price: Double = (funciones.desformato(selected_item.precio) * amount).toDouble()
            if (selected_item.ventanan.toInt() == 2 || selected_item.ventanan.toInt() == 3) {
                price*= (selected_item.porcentaje1[dialog.aprosel_sb_slider1.progress] )
                if (selected_item.ventanan.toInt() == 3)
                    price *= (selected_item.porcentaje2[dialog.aprosel_sb_slider2.progress])
            }
            dialog.aprosel_tv_total.text = funciones.formato(price.toLong() + total)
        }
    }

    private fun clickPlusDialog() {
        amount++
        dialog.aprosel_tv_unidad.text = amount.toString()
        var price:Double = (funciones.desformato(selected_item.precio) * amount).toDouble()
        if (selected_item.ventanan.toInt() == 2||selected_item.ventanan.toInt() == 3) {
            price *= (selected_item.porcentaje1[dialog.aprosel_sb_slider1.progress] )
            if (selected_item.ventanan.toInt() == 3)
            price *= (selected_item.porcentaje2[dialog.aprosel_sb_slider2.progress] )
        }
        dialog.aprosel_tv_total.text = funciones.formato(price.toLong() + total)
    }

    private fun clickOpenConfirm() {
        dialog.setContentView(R.layout.ventana_confirmar_envio)
        dialog.vconfirmar_bt_confirmar.setOnClickListener(this)
        val pref = root.context.getSharedPreferences("user", Context.MODE_PRIVATE)
        val nombre=pref.getString("aldres","")
        val city=pref.getString("city","")


        dialog.vconfirmar_et_direccion.setText(("$nombre - $city"))
        dialog.vconfirmar_tv_evio.text = funciones.formato(3200)
        dialog.vconfirmar_tv_totalcompra.text = funciones.formato(comunication.total)
        dialog.vconfirmar_tv_total.text = funciones.formato(comunication.total+3200)
        dialog.show()
    }

    private fun clickCloseConfirm() {
        dialog.dismiss()
    }

    private fun clickSend() {
        if (!verifyAvailableNetwork(this.activity!!))
            Toast.makeText(
                root.context,
                root.context.getString(R.string.toast_error_conexion),
                Toast.LENGTH_LONG
            ).show()
        else {
            comunication.saveOrder()
            dialog.dismiss()
            Toast.makeText(
                root.context,
                this.context!!.getString(R.string.toast_pedido_enviado_exitosamente),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun clickAccept() { selected_item.cantidad = dialog.aprosel_tv_unidad.text.toString().toInt()
        if(selected_item.ventanan>1){
            selected_item.ind1 = dialog.aprosel_sb_slider1.progress
            if(selected_item.ventanan.toInt()==3){
                selected_item.ind2 = dialog.aprosel_sb_slider2.progress
            }
        }
        selected_item.mensaje=dialog.aprosel_et_mensaje.text.toString()
        comunication.updatedBascket(selected_item)
        dialog.dismiss()
        ready = false

        comunication.setTotal2(funciones.desformato(dialog.aprosel_tv_total.text.toString()))
        Toast.makeText(
            root.context,
            this.getString(R.string.toast_correccion_exitosa),
            Toast.LENGTH_LONG
        ).show()
    }


    fun showWindow(item: ItemProduct) {
        ready=false
        selected_item = item
        amount = item.cantidad
        dialog.setContentView(R.layout.ventana_producto_modificar_fruver)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.aprosel_sb_slider1.setOnSeekBarChangeListener(this)
        dialog.aprosel_sb_slider2.setOnSeekBarChangeListener(this)
        dialog.show()
        cant_ini = item.cantidad
        GlideApp.with(dialog.context)
            .load(cloud_storage.reference.child(item.path + "/" + item.nombre + ".png"))
            .into(dialog.aprosel_iv_icono)
        var aux=funciones.desformato(item.precio) * item.cantidad
        if (item.ventanan.toInt()>1){
            aux=(aux*item.porcentaje1[item.ind1]).toLong()
            if(item.ventanan.toInt()==3)
                aux=(aux*item.porcentaje2[item.ind2]).toLong()
        }
        total=comunication.total-aux

        dialog.aprosel_tv_nombre.text = item.nombre
        dialog.aprosel_tv_unidad.text = item.cantidad.toString()
        dialog.aprosel_tv_preciunitario.text = item.precio
        dialog.aprosel_tv_total.text = funciones.formato(comunication.total)
        dialog.aprosel_et_mensaje.setText(selected_item.mensaje)
        if(item.unidad == "1"){
            dialog.aprosel_tv_descripcion.text = ("1 ${item.nombreMostrar} (a granel).")
            dialog.aprosel_est_precioxunidad.text = ("Precio x unidad: ")
            dialog.aprosel_tv_precioxunidad.text = (item.precio)
        }
        else{
            dialog.aprosel_tv_descripcion.text = ("${item.unidad} de ${item.nombreMostrar}.")
            dialog.aprosel_est_precioxunidad.text = ("Precio x gramo: ")
            val numero = item.unidad.replace("[^0-9]".toRegex(), "").toInt()
            if(item.unidad.contains("libra"))
                dialog.aprosel_tv_precioxunidad.text = funciones.formato(funciones.desformato(item.precio)/(numero*500))
            else
                dialog.aprosel_tv_precioxunidad.text = funciones.formato(funciones.desformato(item.precio)/(numero))
        }


        if (item.ventanan.toInt() == 1) {
            dialog.aprosel_sb_slider1.visibility = View.GONE
            dialog.aprosel_tv_slider1.visibility = View.GONE
            dialog.aprosel_sb_slider2.visibility = View.GONE
            dialog.aprosel_tv_slider2.visibility = View.GONE
        }

        else if (item.ventanan.toInt() == 2) {


            dialog.aprosel_sb_slider1.max = item.porcentaje1.size - 1
            dialog.aprosel_sb_slider1.progress = selected_item.ind1
            dialog.aprosel_tv_preciunitario.text = funciones.formato((funciones.desformato(item.precio) * item.porcentaje1[item.ind1]).toLong())
            dialog.aprosel_sb_slider1.progress = (item.ind1)
            dialog.aprosel_tv_slider2.visibility = View.GONE
            dialog.aprosel_sb_slider2.visibility = View.GONE
        } else {

            dialog.aprosel_sb_slider2.max = (selected_item.array2.size) - 1
            dialog.aprosel_sb_slider2.progress = selected_item.ind2
            dialog.aprosel_sb_slider1.max = (selected_item.array1.size) - 1
            dialog.aprosel_tv_preciunitario.text = funciones.formato((funciones.desformato(item.precio) * item.porcentaje1[item.ind1]*item.porcentaje2[item.ind2]).toLong())
            dialog.aprosel_tv_slider1.text=((item.tipo1 + ": " + item.array1[item.ind1]))
            dialog.aprosel_sb_slider1.progress = (item.ind1)
            dialog.aprosel_tv_slider2.text = (item.tipo2 + ": " + item.array2[item.ind2])
            dialog.aprosel_sb_slider2.progress = (item.ind2)

        }


        dialog.aprosel_bt_ingresar.setOnClickListener(this)
        dialog.aprosel_ib_plus.setOnClickListener(this)
        dialog.aprosel_ib_minus.setOnClickListener(this)

        ready = true

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {

            //dialog modify*************************************************************************
            R.id.aprosel_ib_plus ->
                clickPlusDialog()
            R.id.aprosel_ib_minus ->
                clickMinusDialog()
            R.id.aprosel_bt_ingresar ->
                clickAccept()

            //fragment bascket**********************************************************************
            R.id.abask_bt_limpiar ->
                clickClearBascket()
            R.id.abask_bt_enviar ->
                clickOpenConfirm()

            //dialog confirm************************************************************************
            R.id.vconfirmar_bt_atras ->
                clickCloseConfirm()
            R.id.vconfirmar_bt_confirmar ->
                clickSend()

        }

    }

    fun delete(item:ItemProduct){
        comunication.removeItemBascket(item)
    }

    private fun verifyAvailableNetwork(activity: FragmentActivity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }

}