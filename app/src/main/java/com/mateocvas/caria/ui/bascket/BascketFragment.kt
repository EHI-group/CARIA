package com.mateocvas.caria.ui.bascket

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.firebase.storage.FirebaseStorage
import com.mateocvas.caria.*
import com.mateocvas.caria.adapters.AdapterRecyclerMarket
import com.mateocvas.caria.items.ItemProduct
import com.mateocvas.caria.ui.Comunication
import kotlinx.android.synthetic.main.activity_basket.view.*
import kotlinx.android.synthetic.main.activity_product_selected.*
import kotlinx.android.synthetic.main.ventana_confirmar.*
import kotlinx.android.synthetic.main.ventana_confirmar_envio.*

import java.lang.Exception
import kotlin.collections.ArrayList


class BascketFragment: Fragment(),View.OnClickListener,SeekBar.OnSeekBarChangeListener{

    lateinit var root:View
    var tam=0
    var tip=0
    lateinit var comunication: Comunication
    lateinit var dialog:Dialog
    private var total:Long=0
    private var cant_ini=0
    private var amount=0
    var ready=false
    private val cloud_storage=FirebaseStorage.getInstance()

   private val funciones=Funciones()

   private val data_fruver=ArrayList<ItemProduct>()
   private val data_food=ArrayList<ItemProduct>()
   private val data_medicinal=ArrayList<ItemProduct>()

    private val warnings=Warinings()


   private  var tipo:String=""
   private lateinit var data_fruver_adapter:AdapterRecyclerMarket
   private lateinit var  data_food_adapter:AdapterRecyclerMarket
   private lateinit var  data_medicinal_adapter:AdapterRecyclerMarket

    private lateinit var selected_item:ItemProduct




    override fun onResume() {

        super.onResume()

        view!!.isFocusableInTouchMode = true
        view!!.requestFocus()
        view!!.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {

                return if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    val dialog=Dialog(root.context)
                    dialog.setContentView(R.layout.ventana_confirmar)
                    dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.vconfirm_bt_cancelar.setOnClickListener {
                        dialog.dismiss() }
                    dialog.vconfirm_bt_aceptar.setOnClickListener {
                        activity!!.finish()
                    }
                    dialog.show()

                    true

                } else false

            }
        })
    }




    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

        if(ready)
        {

        if (p0!!.id == R.id.aprosel_sb_slider1){
             tam = p1
                dialog.aprosel_tv_slider1.setText((tipo + " " + selected_item.array[p1]))
                val new_amounto = (amount * funciones.desformato(selected_item.precio) * selected_item.porcentaje[p1]).toLong()
                dialog.aprosel_tv_total.setText(funciones.formato(total + new_amounto))
                dialog.aprosel_tv_preciunitario.setText(
                    funciones.formato(
                        (funciones.desformato(
                            selected_item.precio
                        ) * selected_item.porcentaje[p1]).toLong()
                    )
                )

            }
        else {
            dialog.aprosel_tv_slider2.setText((selected_item.tipo2 +" "+ selected_item.array2[p1]))
            selected_item.madure = p1
            tip=p1
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
    fun begin(){
        dialog= Dialog(root.context)
        data_fruver_adapter= AdapterRecyclerMarket(this,data_fruver)
        data_food_adapter=AdapterRecyclerMarket(this,data_food)
        data_medicinal_adapter=AdapterRecyclerMarket(this,data_medicinal)


        comunication=activity?.run {
            ViewModelProviders.of(this).get(Comunication::class.java)
        }?:throw Exception("Invalid Activity")




        root.abask_rv_fruver.setHasFixedSize(false)
        root.abask_rv_fruver.layoutManager = LinearLayoutManager(root.context)
        root.abask_rv_fruver.adapter=data_fruver_adapter

        root.abask_rv_food.setHasFixedSize(false)
        root.abask_rv_food.layoutManager = LinearLayoutManager(root.context)
        root.abask_rv_food.adapter=data_food_adapter

        root.abask_rv_medicinal.setHasFixedSize(false)
        root.abask_rv_medicinal.layoutManager = LinearLayoutManager(root.context)
        root.abask_rv_medicinal.adapter=data_medicinal_adapter
    }

    //here we set the listners to the buttons envuar and limpiar
    fun setListeners(){
        root.abask_bt_enviar.setOnClickListener(this)
        root.abask_bt_limpiar.setOnClickListener(this)
    }

    fun setObservers(){

      //  comunication.bascket_fruver_com.removeObservers(activity!!)
        comunication.bascket_fruver_com.observe(activity!! , Observer {
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
            data_fruver_adapter.notifyDataSetChanged()        })


        comunication.com_total.observe(this, Observer {
            root.abask_tv_total.text=it
        })



    }


    fun clickClearBascket(){
        comunication.deleteAllBascket()
        Toast.makeText(root.context,this.getString(R.string.toast_carrito_vaciado),Toast.LENGTH_LONG).show()
    }
    fun clickMinusDialog(){
        if(amount==1)
            warnings.prosel_minus(dialog.context)
        else{
            amount--
            dialog.aprosel_tv_unidad.setText(amount.toString())
            var price=funciones.desformato(selected_item.precio)*amount
            if(selected_item.ventanan.toInt()==2 || selected_item.ventanan.toInt()==3 )
                price=(selected_item.porcentaje[dialog.aprosel_sb_slider1.progress]*price).toLong()
            dialog.aprosel_tv_total.setText(funciones.formato(total+price))
        }

    }

    fun clickPlusDialog(){
        amount++
        dialog.aprosel_tv_unidad.setText(amount.toString())
        var price=funciones.desformato(selected_item.precio)*amount
        if(selected_item.ventanan.toInt()==2 || selected_item.ventanan.toInt()==4 )
            price=(selected_item.porcentaje[dialog.aprosel_sb_slider1.progress]*price).toLong()
        dialog.aprosel_tv_total.setText(funciones.formato(price+total))
    }

    fun clickOpenConfirm(){
        dialog.setContentView(R.layout.ventana_confirmar_envio)
        dialog.vconfirmar_bt_confirmar.setOnClickListener(this)
        dialog.show()
    }

    fun clickCloseConfirm(){
        dialog.dismiss()
    }

    fun clickSend() {
        comunication.saveOrder()
        dialog.dismiss()
        Toast.makeText(root.context,this.context!!.getString(R.string.toast_pedido_enviado_exitosamente),Toast.LENGTH_LONG).show()
    }



    fun clickAccept(){
        selected_item.cantidad=dialog.aprosel_tv_unidad.text.toString().toInt()
        selected_item.tamano=tam
        selected_item.madure=tip
        comunication.updatedBascket(selected_item)
        dialog.dismiss()
        ready=false
        Toast.makeText(root.context,this.getString(R.string.toast_correccion_exitosa),Toast.LENGTH_LONG).show()
    }


    fun showWindow(item:ItemProduct){
        selected_item=item
        amount=item.cantidad
        tam=selected_item.tamano
        tipo=item.tipo
        dialog.setContentView(R.layout.ventana_producto_modificar_fruver)
        dialog.getWindow()!!.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
        dialog.aprosel_sb_slider1.setOnSeekBarChangeListener(this)
        dialog.aprosel_sb_slider2.setOnSeekBarChangeListener(this)
        dialog.show()
        cant_ini=item.cantidad
        GlideApp.with(dialog.context)
            .load(cloud_storage.reference.child(item.path +"/"+item.nombre+".png"))
            .into( dialog.aprosel_iv_icono )
        if(item.array.size==0)
            total=comunication.total-funciones.desformato(item.precio)*item.cantidad
        else
            total=comunication.total-(funciones.desformato(item.precio)*item.porcentaje[item.tamano]*item.cantidad).toLong()



        dialog.aprosel_tv_nombre.setText(item.nombre)
        dialog.aprosel_tv_unidad.setText(item.cantidad.toString())
        dialog.aprosel_tv_preciunitario.setText(item.precio)
        dialog.aprosel_tv_total.setText(funciones.formato(comunication.total))


        if(item.ventanan.toInt()==1){
            dialog.aprosel_sb_slider1.visibility=View.GONE
            dialog.aprosel_tv_slider1.visibility=View.GONE
            dialog.aprosel_sb_slider2.visibility=View.GONE
            dialog.aprosel_tv_slider2.visibility=View.GONE
        }

        if(item.ventanan.toInt()==2)
        {

          //dialog.aprosel_sb_slider1.max=(selected_item.array.size-1)


            val temp=item.porcentaje.size-1
            dialog.aprosel_sb_slider1.max=(temp)
            dialog.aprosel_sb_slider1.setProgress(tam)

          dialog.aprosel_tv_preciunitario.setText(funciones.formato((funciones.desformato(item.precio)*item.porcentaje[item.tamano]).toLong()))
          tipo=item.tipo

          dialog.aprosel_sb_slider1.progress=(item.tamano)
          dialog.aprosel_tv_slider2.visibility=View.GONE
          dialog.aprosel_sb_slider2.visibility=View.GONE
        }

        else{

            dialog.aprosel_sb_slider2.max=(selected_item.array2.size)-1
            dialog.aprosel_sb_slider2.setProgress(selected_item.madure)
            dialog.aprosel_sb_slider1.max=(selected_item.array.size)-1

            dialog.aprosel_tv_preciunitario.setText(funciones.formato((funciones.desformato(item.precio)*item.porcentaje[item.tamano]).toLong()))


            dialog.aprosel_tv_slider1.setText((item.tipo+": "+item.array[item.tamano]))
            dialog.aprosel_sb_slider1.progress=(item.tamano)
            dialog.aprosel_tv_slider2.text=item.array2[(item.madure)]
        }


        dialog.aprosel_bt_ingresar.setOnClickListener(this)
        dialog.aprosel_ib_plus.setOnClickListener(this)
        dialog.aprosel_ib_minus.setOnClickListener(this)

        ready=true

    }

    override fun onClick(p0: View?) {
        when (p0!!.id){

            //dialog modify*************************************************************************
            R.id.aprosel_ib_plus->
                clickPlusDialog()
            R.id.aprosel_ib_minus->
                clickMinusDialog()
            R.id.aprosel_bt_ingresar->
                clickAccept()

            //fragment bascket**********************************************************************
            R.id.abask_bt_limpiar->
                clickClearBascket()
            R.id.abask_bt_enviar->
                clickOpenConfirm()

            //dialog confirm************************************************************************
            R.id.vconfirmar_bt_atras->
                clickCloseConfirm()
            R.id.vconfirmar_bt_confirmar->
                clickSend()

        }

    }




}