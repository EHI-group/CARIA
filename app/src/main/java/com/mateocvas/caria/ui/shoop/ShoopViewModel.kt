package com.mateocvas.caria.ui.shoop

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.storage.FirebaseStorage
import com.mateocvas.caria.*
import com.mateocvas.caria.adapters.AdapterRecyclerShop
import com.mateocvas.caria.items.ItemProduct
import com.mateocvas.caria.ui.Comunication
import kotlinx.android.synthetic.main.activity_product_selected.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ShoopViewModel : ViewModel(),View.OnClickListener, TextWatcher, SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

            if (p0!!.id == R.id.aprosel_sb_slider1) {
                dialog.aprosel_tv_slider1.text = (tipo + " " + selected_item.array1[p1])
                val new_amounto =
                    (amount * funciones.desformato(selected_item.precio) * selected_item.porcentaje1[p1]).toLong()
                dialog.aprosel_tv_total.text = funciones.formato(total + new_amounto)
                dialog.aprosel_tv_preciunitario.text = funciones.formato(
                    (funciones.desformato(
                        selected_item.precio
                    ) * selected_item.porcentaje1[p1]).toLong()
                )
            } else {
                dialog.aprosel_tv_slider2.text = (selected_item.tipo2 + " " + selected_item.array2[p1])
                selected_item.ind2 = p1
                val new_amounto =
                    (amount * funciones.desformato(selected_item.precio) * selected_item.porcentaje1[dialog.aprosel_sb_slider1.progress] * selected_item.porcentaje2[p1]).toLong()
                dialog.aprosel_tv_total.text = funciones.formato(total + new_amounto)
                dialog.aprosel_tv_preciunitario.text = funciones.formato(
                    (funciones.desformato(
                        selected_item.precio
                    ) * selected_item.porcentaje2[p1]).toLong()
                )
            }

    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
    }

    private val funciones=Funciones()
    private val app_context= MyApp.instance.applicationContext!!
    private var tipo:String=""


    private lateinit var toast:Toast
    private val warnings=Warinings()
    private lateinit var dialog:Dialog
    var selected_tab=MyApp.instance.applicationContext.getString(R.string.tag_tab1)
    private lateinit var selected_item:ItemProduct
    private lateinit var comunication: Comunication
    private var total:Long=0
    private var amount:Int=1

    val com_edit_change=MutableLiveData<Boolean>()


    private val data_food=ArrayList<ItemProduct>()
    private val data_food_search=ArrayList<ItemProduct>()
    lateinit var adapter_food:AdapterRecyclerShop

    private val data_fruver=ArrayList<ItemProduct>()
    private val data_fruver_search=ArrayList<ItemProduct>()
    lateinit var adapter_fruver:AdapterRecyclerShop

    private val data_medicinal=ArrayList<ItemProduct>()
    private val data_medicinal_search=ArrayList<ItemProduct>()
    lateinit var adapter_medicinal:AdapterRecyclerShop
/*
    init {
        loadData()
    }

    //load the data from firebase
    fun loadData(){
        db.collection(app_context.getString(R.string.tag_fruver))
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (producto in task.result!!) {
                        data_fruver.add(
                            ItemProduct(
                                producto["nombre"] as String,
                                producto["nombreMostrar"] as String,
                                funciones.formato(44),
                                1,
                                false,
                                producto["unidad"] as String,
                                "",
                                "ProductosFruve",
                                0, 0,
                                "",
                                "",""
                            )
                        )
                    }
                    data_fruver_search.addAll(data_fruver)
                }

            }


        db.collection(app_context.getString(R.string.tag_food))
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (producto in task.result!!) {
                        data_food.add(
                            ItemProduct(
                                producto["nombre"] as String,
                                producto["nombreMostrar"] as String,
                                funciones.formato(44),
                                1,
                                false,
                                producto["unidad"] as String,
                                "",
                                "ProductosFruve",
                                0, 0,
                                "",
                                "",""
                            )
                        )
                    }
                    data_food_search.addAll(data_food)
                }

            }


        db.collection(app_context.getString(R.string.tag_medicinal))
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (producto in task.result!!) {
                        data_fruver.add(
                            ItemProduct(
                                producto["nombre"] as String,
                                producto["nombreMostrar"] as String,
                                funciones.formato(44),
                                1,
                                false,
                                producto["unidad"] as String,
                                "",
                                "ProductosFruve",
                                0, 0,
                                "",
                                "",""
                            )
                        )




                    }
                    data_medicinal_search.addAll(data_medicinal)
                }
            }



    }
*/
    //Constructor
    fun begin(fragment:ShoopFragment){


            toast= Toast(fragment.context)


            dialog = Dialog(fragment.context!!)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.activity_product_selected)
            dialog.aprosel_ib_minus.setOnClickListener(this)
            dialog.aprosel_ib_plus.setOnClickListener(this)
            dialog.aprosel_bt_ingresar.setOnClickListener(this)
            dialog.aprosel_sb_slider1.setOnSeekBarChangeListener(this)
            dialog.aprosel_sb_slider2.setOnSeekBarChangeListener(this)

            comunication = fragment.activity?.run {
                ViewModelProviders.of(this).get(Comunication::class.java)
            } ?: throw Exception("Invalid Activity")


            adapter_fruver = AdapterRecyclerShop(fragment, data_fruver_search)
            adapter_food = AdapterRecyclerShop(fragment, data_food_search)
            adapter_medicinal = AdapterRecyclerShop(fragment, data_medicinal_search)

            adapter_fruver.notifyDataSetChanged()
            adapter_food.notifyDataSetChanged()
            adapter_medicinal.notifyDataSetChanged()


    }


    fun refreshDataFruver(array:ArrayList<ItemProduct>){

        data_fruver.clear()
        data_fruver.addAll(array)
        if(data_fruver_search.size==0)
            data_fruver_search.addAll(data_fruver)
        else
            for (i in 0 until data_fruver_search.size-1)
                for (item in data_fruver){
                    if(data_fruver_search[i].nombre == item.nombre){
                        data_fruver_search[i] = item
                        break
                    }
            }
        adapter_fruver.notifyDataSetChanged()
    }

    fun refreshDataFood(array:ArrayList<ItemProduct>){
        data_food.clear()
        data_food.addAll(array)
        if(data_food_search.size==0)
            data_food_search.addAll(data_food)
        else
            for (i in 0 until data_food_search.size-1)
                for (item in data_food)
                    if(data_food_search[i].nombre == item.nombre){
                        data_food_search[i] = item
                        break
                    }
        adapter_food.notifyDataSetChanged()
    }

    fun refreshDataMedicinal(array:ArrayList<ItemProduct>){
        data_medicinal.clear()
        data_medicinal.addAll(array)
        if(data_medicinal_search.size==0)
            data_medicinal_search.addAll(data_medicinal)
        else
            for (i in 0 until data_medicinal_search.size-1)
                for (item in data_medicinal)
                    if(data_medicinal_search[i].nombre == item.nombre){
                        data_medicinal_search[i] = item
                        break
                    }
        adapter_medicinal.notifyDataSetChanged()
    }




    // Listener autocompletado
    private fun autoComplete(text:String){


        if(selected_tab == app_context.getString(R.string.tag_tab1)) {
            data_fruver_search.clear()
            for (fruver in data_fruver)
                if (fruver.nombreMostrar.toLowerCase(Locale.getDefault()).contains(text)) {
                    data_fruver_search.add(fruver)
                }
            adapter_fruver.notifyDataSetChanged()
        }
        else if(selected_tab == app_context.getString(R.string.tag_tab2)){
                data_food_search.clear()
            for(food in data_food)
                    if (food.nombreMostrar.toLowerCase(Locale.getDefault()).contains(text)){
                        data_food_search.add(food)
                    }
                 adapter_food.notifyDataSetChanged()
        }

        else {

            data_medicinal_search.clear()
            for (medicinal in data_medicinal)
                if (medicinal.nombreMostrar.toLowerCase(Locale.getDefault()).contains(text)) {
                    data_medicinal_search.add(medicinal)
                }
            adapter_medicinal.notifyDataSetChanged()
        }
        com_edit_change.value=false

    }

    //unlocks the slected item in its respective lis and send it to the market
    private fun clickAddItem(){

        if(selected_item.path == app_context.getString(R.string.tag_food))
            data_food_search.find { it.nombre == selected_item.nombre }!!.alreadyBougtth=true
        else if(selected_item.path == app_context.getString(R.string.tag_medicinal))
            data_medicinal_search.find { it.nombre == selected_item.nombre }!!.alreadyBougtth=true
        else
            data_fruver_search.find { it.nombre == selected_item.nombre }!!.alreadyBougtth=true

        selected_item.cantidad=amount
        if(selected_item.ventanan>1) {
            selected_item.ind1 = dialog.aprosel_sb_slider1.progress
            if(selected_item.ventanan.toInt()==3)
                selected_item.ind2 = dialog.aprosel_sb_slider2.progress
        }
        comunication.addItemBascket(selected_item)
        comunication.setTotal2(funciones.desformato(dialog.aprosel_tv_total.text.toString()))
        dialog.dismiss()
    }

    fun itemSlected(item:ItemProduct){
        selected_item=item
        amount=1
        total=comunication.total

        if(item.alreadyBougtth) {
            toast.setText(R.string.toast_error_agregado_previamente)
            toast.duration=Toast.LENGTH_LONG
            toast.show()
        }
        else {
            GlideApp.with(dialog.context)
                .load(FirebaseStorage.getInstance().reference.child(item.path + "/" + item.nombre + ".png"))
                .into(dialog.aprosel_iv_icono)
            dialog.aprosel_tv_unidad.text = "1"
            dialog.aprosel_tv_total.text = funciones.formato(total+funciones.desformato(item.precio))
            dialog.aprosel_tv_preciunitario.text = item.precio
            dialog.aprosel_tv_total.text = funciones.formato(total+funciones.desformato(item.precio))
            dialog.aprosel_tv_descripcion.text =
                selected_item.beneficios.replace("\\n","\n").replace("\\","")
            if(item.unidad == "1"){
                dialog.aprosel_tv_equivale.text = ("1 ${item.nombreMostrar} (a granel).")
                dialog.aprosel_est_precioxunidad.text = ("Precio x unidad: ")
                dialog.aprosel_tv_precioxunidad.text = (item.precio)
            }
            else{
                dialog.aprosel_tv_equivale.text = ("${item.unidad} de ${item.nombreMostrar}.")
                dialog.aprosel_est_precioxunidad.text = ("Precio x gramo: ")
                val numero = item.unidad.replace("[^0-9]".toRegex(), "").toInt()
                if(item.unidad.contains("libra"))
                    dialog.aprosel_tv_precioxunidad.text = funciones.formato(funciones.desformato(item.precio)/(numero*500))
                else
                    dialog.aprosel_tv_precioxunidad.text = funciones.formato(funciones.desformato(item.precio)/(numero))
            }



            dialog.aprosel_tv_nombre.text = item.nombreMostrar
            dialog.aprosel_tv_slider1.visibility=View.VISIBLE
            dialog.aprosel_sb_slider1.visibility=View.VISIBLE
            dialog.aprosel_tv_slider2.visibility=View.VISIBLE
            dialog.aprosel_sb_slider2.visibility=View.VISIBLE

            if(item.ventanan.toInt()==1 ){
                dialog.aprosel_tv_slider1.visibility=View.GONE
                dialog.aprosel_sb_slider1.visibility=View.GONE
                dialog.aprosel_tv_slider2.visibility=View.GONE
                dialog.aprosel_sb_slider2.visibility=View.GONE}

            else if(item.ventanan.toInt()==2  ){
                dialog.aprosel_sb_slider1.max = item.porcentaje1.size - 1
                dialog.aprosel_sb_slider1.progress = selected_item.ind1
                dialog.aprosel_tv_preciunitario.text = funciones.formato((funciones.desformato(item.precio) * item.porcentaje1[item.ind1]).toLong())
                dialog.aprosel_sb_slider1.progress = (item.ind1)
                dialog.aprosel_tv_slider2.visibility = View.GONE
                dialog.aprosel_sb_slider2.visibility = View.GONE
            }

            else if(item.ventanan.toInt()==3 ){
                dialog.aprosel_sb_slider2.max = (selected_item.array2.size) - 1
                dialog.aprosel_sb_slider2.progress = selected_item.ind2
                dialog.aprosel_sb_slider1.max = (selected_item.array1.size) - 1
                dialog.aprosel_tv_preciunitario.text = funciones.formato((funciones.desformato(item.precio) * item.porcentaje1[item.ind1]*item.porcentaje1[item.ind2]).toLong())
                dialog.aprosel_tv_slider1.text=((item.tipo1 + ": " + item.array1[item.ind1]))
                dialog.aprosel_sb_slider1.progress = (item.ind1)
                dialog.aprosel_tv_slider2.text = (item.tipo2 + ": " + item.array2[item.ind2])
                dialog.aprosel_sb_slider2.progress = (item.ind2)

            }


            dialog.show()
        }
    }

    //button plus
    private fun clickPlus(){
        amount++
        dialog.aprosel_tv_unidad.text = amount.toString()
        var price:Double = (funciones.desformato(selected_item.precio) * amount).toDouble()
        if (selected_item.ventanan.toInt() == 2||selected_item.ventanan.toInt() == 3) {
            price = (selected_item.porcentaje1[dialog.aprosel_sb_slider1.progress] * price)
            if (selected_item.ventanan.toInt() == 3)
                price *= (selected_item.porcentaje2[dialog.aprosel_sb_slider2.progress])
        }
        dialog.aprosel_tv_total.text = funciones.formato(price.toLong() + total)
    }

    //button minus
    private fun clickMinus(){
        if (amount == 1)
            warnings.prosel_minus(dialog.context)
        else {
            amount--
            dialog.aprosel_tv_unidad.text = amount.toString()
            var price: Double = (funciones.desformato(selected_item.precio) * amount).toDouble()
            if (selected_item.ventanan.toInt() == 2 || selected_item.ventanan.toInt() == 3) {
                price = (selected_item.porcentaje1[dialog.aprosel_sb_slider1.progress] * price)
                if (selected_item.ventanan.toInt() == 3)
                    price *= (selected_item.porcentaje2[dialog.aprosel_sb_slider2.progress])
            }
            dialog.aprosel_tv_total.text = funciones.formato(price.toLong() + total)
        }

    }

    private var acu=""

    // ******************************LISTENERS****************************************
    override fun afterTextChanged(p0: Editable?) {
        com_edit_change.value=true
        autoComplete(p0.toString().toLowerCase(Locale.getDefault()))
        if(selected_tab == app_context.getString(R.string.tag_tab1) && acu != p0.toString())
            comunication.text_fruver=p0.toString()
        else if(selected_tab == app_context.getString(R.string.tag_tab2) && acu != p0.toString())
            comunication.text_food=p0.toString()
        else if(selected_tab == app_context.getString(R.string.tag_tab3) && acu != p0.toString())
            comunication.text_medicinal=p0.toString()

        acu=p0.toString()

    }
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.aprosel_ib_plus ->
                clickPlus()
            R.id.aprosel_ib_minus ->
                clickMinus()
            R.id.aprosel_bt_ingresar ->
                clickAddItem()
        }
    }



    }











