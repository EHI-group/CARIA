package com.mateocvas.caria.ui.bascket

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mateocvas.caria.Funciones
import com.mateocvas.caria.GlideApp
import com.mateocvas.caria.MyApp
import com.mateocvas.caria.R
import com.mateocvas.caria.adapters.AdapterRecyclerMarket
import com.mateocvas.caria.items.ItemProduct
import com.mateocvas.caria.ui.Comunication
import kotlinx.android.synthetic.main.activity_basket.view.*
import kotlinx.android.synthetic.main.activity_product_selected.*
import kotlinx.android.synthetic.main.ventana_confirmar_envio.*
import kotlinx.android.synthetic.main.ventana_producto_modificar_fruver.*
import java.lang.Exception
import kotlin.collections.ArrayList

class BascketFragment: Fragment(),View.OnClickListener,SeekBar.OnSeekBarChangeListener{

    lateinit var root:View
    lateinit var comunication: Comunication
    lateinit var dialog:Dialog
    private var total:Long=0
    private var cant_ini=0
    private val cloud_storage=FirebaseStorage.getInstance()

   private val funciones=Funciones()

   private val data_fruver=ArrayList<ItemProduct>()
   private val data_food=ArrayList<ItemProduct>()
   private val data_medicinal=ArrayList<ItemProduct>()

   private lateinit var data_fruver_adapter:AdapterRecyclerMarket
   private lateinit var  data_food_adapter:AdapterRecyclerMarket
   private lateinit var  data_medicinal_adapter:AdapterRecyclerMarket

    private lateinit var selected_item:ItemProduct

    val app_context= MyApp.instance.applicationContext

    private val array_size =arrayOf(app_context.getString(R.string.seekbar_tam0),app_context.getString(R.string.seekbar_tam1),app_context.getString(R.string.seekbar_tam2))
    private val array_maduration=arrayOf(app_context.getString(R.string.seekbar_mad0), app_context.getString(R.string.seekbar_mad1),app_context.getString(R.string.seekbar_mad2))


    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {


        if(selected_item.path.equals(this.getString(R.string.tag_fruver)))
            if(p0!!.id==R.id.vmodfru_sb_seekbar1)
                if(selected_item.ventanan.toInt()==2 ||selected_item.ventanan.toInt()==4){
                    selected_item.tamano=p1
                    dialog.vmodfru_tv_seekbar1.setText((array_size[p1]))
                }
                else{
                    selected_item.madure=p1
                    dialog.vmodfru_tv_seekbar1.setText((array_maduration[p1]))
                }

            else {
                dialog.vmodfru_tv_seekbar2.setText((array_maduration[p1]))
                selected_item.madure = p1
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

        comunication.bascket_food_com.removeObservers(activity!!)
        comunication.bascket_food_com.observe(this, Observer {
            data_food.clear()
            data_food.addAll(it)
            data_food_adapter.notifyDataSetChanged()
        })

        comunication.bascket_medicinal_com.removeObservers(activity!!)
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
    fun clickMinusDialog() {
        var aux1 = dialog.vmodfru_tv_cantidad.text.toString().toInt()
        aux1--
        if (aux1 == 0)
            Toast.makeText(context, context!!.getString(R.string.toast_cantidad_positiva), Toast.LENGTH_LONG).show()
        else {
            dialog.vmodfru_tv_cantidad.setText(aux1.toString())
            dialog.vmodfru_tv_totalunidad.setText(funciones.formato((aux1 * funciones.desformato(selected_item.precio))))
            dialog.vmodfru_tv_totalcompra.setText(funciones.formato((total + (aux1-cant_ini)*funciones.desformato(selected_item.precio))))
        }
    }

    fun clickPlusDialog(){
        var aux2=dialog.vmodfru_tv_cantidad.text.toString().toInt()
        aux2++
        dialog.vmodfru_tv_cantidad.setText(aux2.toString())
        dialog.vmodfru_tv_totalunidad.setText(funciones.formato((aux2*funciones.desformato(selected_item.precio))))
        dialog.vmodfru_tv_totalcompra.setText(funciones.formato((total+(aux2-cant_ini)*funciones.desformato(selected_item.precio))))

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




    fun clickRemove(){
        comunication.removeItemBascket(selected_item)
        dialog.dismiss()
        Toast.makeText(root.context,this.getString(R.string.toast_producto_eliminado),Toast.LENGTH_LONG).show()
    }

    fun clickAccept(){
        selected_item.cantidad=dialog.vmodfru_tv_cantidad.text.toString().toInt()
        comunication.updatedBascket(selected_item)
        dialog.dismiss()
        Toast.makeText(root.context,this.getString(R.string.toast_correccion_exitosa),Toast.LENGTH_LONG).show()
    }


    fun showWindow(item:ItemProduct){
        selected_item=item
        dialog.setContentView(R.layout.ventana_producto_modificar_fruver)
        dialog.getWindow()!!.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
        dialog.vmodfru_sb_seekbar1.setOnSeekBarChangeListener(this)
        dialog.vmodfru_sb_seekbar2.setOnSeekBarChangeListener(this)
        dialog.show()
        cant_ini=item.cantidad
        GlideApp.with(dialog.context)
            .load(cloud_storage.reference.child(item.path +"/"+item.nombre+".png"))
            .into( dialog.vmodfru_iv_icono )
        total=comunication.total
        dialog.vmodfru_tv_nombre.setText(item.nombre)
        dialog.vmodfru_et_comentario.setText(item.mensaje)
        dialog.vmodfru_tv_cantidad.setText(item.cantidad.toString())
        dialog.vmodfru_tv_totalunidad.setText(funciones.formato(item.cantidad*funciones.desformato(item.precio)))
        dialog.vmodfru_tv_totalcompra.setText(funciones.formato(total))


        if(item.ventanan.toInt()==1){
            dialog.vmodfru_tv_seekbar1.visibility=View.GONE
            dialog.vmodfru_sb_seekbar1.visibility=View.GONE
            dialog.vmodfru_sb_seekbar2.visibility=View.GONE
            dialog.vmodfru_tv_seekbar2.visibility=View.GONE
        }

        if(item.ventanan.toInt()==2)
        { dialog.vmodfru_tv_seekbar1.setText(array_size[(item.tamano)])
          dialog.vmodfru_sb_seekbar1.progress=(item.tamano)
          dialog.vmodfru_tv_seekbar2.visibility=View.GONE
          dialog.vmodfru_sb_seekbar2.visibility=View.GONE
        }
        else if(item.ventanan.toInt()==3){
            dialog.vmodfru_tv_seekbar1.setText(array_maduration[(item.madure)])
            dialog.vmodfru_sb_seekbar1.progress=(item.madure)
            dialog.vmodfru_sb_seekbar2.visibility=View.GONE
            dialog.vmodfru_tv_seekbar2.visibility=View.GONE
        }
        else{
            dialog.vmodfru_sb_seekbar1.progress=(item.tamano)
            dialog.vmodfru_tv_seekbar1.text=array_size[(item.tamano)]
            dialog.vmodfru_sb_seekbar2.progress=(item.madure)
            dialog.vmodfru_tv_seekbar2.text=array_maduration[(item.madure)]
        }


        dialog.vmodfru_tv_nombre.setText(item.nombre)
        dialog.vmodfru_bt_minus.setOnClickListener(this)
        dialog.vmodfru_bt_plus.setOnClickListener(this)
        dialog.vmodfru_bt_remover.setOnClickListener(this)
        dialog.vmodfru_bt_aceptar.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when (p0!!.id){

            //dialog modify*************************************************************************
            R.id.vmodfru_bt_plus->
                clickPlusDialog()
            R.id.vmodfru_bt_minus->
                clickMinusDialog()
            R.id.vmodfru_bt_remover->
                clickRemove()
            R.id.vmodfru_bt_aceptar->
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