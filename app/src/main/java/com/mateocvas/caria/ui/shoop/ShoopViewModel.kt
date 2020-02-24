package com.mateocvas.caria.ui.shoop

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.SeekBar
import android.widget.TabHost
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
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



            if(p0!!.id==R.id.aprosel_sb_slider1){
                    selected_item.tamano=p1
                    dialog.aprosel_tv_slider1.setText(( tipo+" "+array_size[p1]))
                    val new_amounto=(amount*funciones.desformato(selected_item.precio)*selected_item.porcentaje[p1]).toLong()
                    dialog.aprosel_tv_total.setText(funciones.formato(total+new_amounto))
                    dialog.aprosel_tv_preciunitario.setText(funciones.formato(new_amounto))
                }
                else{
                    selected_item.madure=p1
                    dialog.aprosel_tv_slider2.setText((selected_item.tipo2+" "+array_maduration[p1]))
                }


    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
    }

    val db=FirebaseFirestore.getInstance()
    val funciones=Funciones()
    val app_context=MyApp.instance.applicationContext
    val array_size =ArrayList<String>()
    val array_maduration=ArrayList<String>()
    var tipo:String=""


    lateinit var toast:Toast
    val warnings=Warinings()
    lateinit var dialog:Dialog
    var selected_tab=MyApp.instance.applicationContext.getString(R.string.tag_tab1)
    lateinit var selected_item:ItemProduct
    lateinit var comunication: Comunication
    var total:Long=0
    var amount:Int=1
    private lateinit var dependencia: ShoopFragment

    val com_edit_change=MutableLiveData<Boolean>()


    val data_food=ArrayList<ItemProduct>()
    private val data_food_search=ArrayList<ItemProduct>()
    lateinit var adapter_food:AdapterRecyclerShop

    val data_fruver=ArrayList<ItemProduct>()
    private val data_fruver_search=ArrayList<ItemProduct>()
    lateinit var adapter_fruver:AdapterRecyclerShop

    val data_medicinal=ArrayList<ItemProduct>()
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


            adapter_fruver = AdapterRecyclerShop(fragment, data_fruver_search, this)
            adapter_food = AdapterRecyclerShop(fragment, data_food_search, this)
            adapter_medicinal = AdapterRecyclerShop(fragment, data_medicinal_search, this)

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
                    if(data_fruver_search[i].nombre.equals(item.nombre)){
                        data_fruver_search.set(i,item)
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
                    if(data_food_search[i].nombre.equals(item.nombre)){
                        data_food_search.set(i,item)
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
                    if(data_medicinal_search[i].nombre.equals(item.nombre)){
                        data_medicinal_search.set(i,item)
                        break
                    }
        adapter_medicinal.notifyDataSetChanged()
    }




    // Listener autocompletado
    fun autoComplete(text:String){


        if(selected_tab.equals(app_context.getString(R.string.tag_tab1))) {
            data_fruver_search.clear()

            for (fruver in data_fruver)
                if (fruver.nombreMostrar.toLowerCase(Locale.getDefault()).contains(text)) {
                    data_fruver_search.add(fruver)
                }
            adapter_fruver.notifyDataSetChanged()
        }
        else if(selected_tab.equals(app_context.getString(R.string.tag_tab2))){
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
    fun clickAddItem(){

        if(selected_item.path.equals(app_context.getString(R.string.tag_food)))
            data_food_search.find { it.nombre.equals(selected_item.nombre)}!!.alreadyBougtth=true
        else if(selected_item.path.equals(app_context.getString(R.string.tag_medicinal)))
            data_medicinal_search.find { it.nombre.equals(selected_item.nombre)}!!.alreadyBougtth=true
        else
            data_fruver_search.find { it.nombre.equals(selected_item.nombre)}!!.alreadyBougtth=true

        selected_item.cantidad=amount
        comunication.addItemBascket(selected_item)
        comunication.set_total(funciones.desformato(dialog.aprosel_tv_total.text.toString()))
        dialog.dismiss()
    }

    fun itemSlected(item:ItemProduct){
        selected_item=item
        amount=1
        total=comunication.total

        if(item.alreadyBougtth)
            toast.show()
        else {
            GlideApp.with(dialog.context)
                .load(FirebaseStorage.getInstance().reference.child(item.path + "/" + item.nombre + ".png"))
                .into(dialog.aprosel_iv_icono)
            dialog.aprosel_tv_unidad.setText("1")
            dialog.aprosel_tv_total.setText(funciones.formato(total+funciones.desformato(item.precio)))
            dialog.aprosel_tv_preciunitario.setText(item.precio)
            dialog.aprosel_tv_total.setText(funciones.formato(total+funciones.desformato(item.precio)))
            if(item.unidad.equals("1"))
                dialog.aprosel_tv_descripcion.setText(("1 ${item.nombreMostrar} (a granel)."))
            else
                dialog.aprosel_tv_descripcion.setText(("${item.unidad} de ${item.nombreMostrar}."))

            dialog.aprosel_tv_nombre.setText(item.nombreMostrar)
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
                array_size.clear()
                array_size.addAll(item.array)
                dialog.aprosel_sb_slider1.max=array_size.size-1
                dialog.aprosel_sb_slider1.progress=0
                dialog.aprosel_tv_preciunitario.setText(funciones.formato(funciones.desformato(item.precio)*item.porcentaje[0].toLong()))
                tipo=item.tipo
                dialog.aprosel_tv_slider2.visibility=View.GONE
                dialog.aprosel_sb_slider2.visibility=View.GONE
            }


            else if(item.ventanan.toInt()==3 ){
                array_size.clear()
                array_size.addAll(item.array)
                array_maduration.clear()
                array_maduration.addAll(item.array2)
                dialog.aprosel_sb_slider1.max=array_size.size-1
                dialog.aprosel_sb_slider1.progress=0
                dialog.aprosel_tv_preciunitario.setText(funciones.formato(funciones.desformato(item.precio)*item.porcentaje[0].toLong()))
                tipo=item.tipo

            }


            dialog.show()
        }
    }

    //button plus
    fun clickPlus(){
        amount++
        dialog.aprosel_tv_unidad.setText(amount.toString())
        var price=funciones.desformato(selected_item.precio)*amount
        if(selected_item.ventanan.toInt()==2 || selected_item.ventanan.toInt()==4 )
            price=(selected_item.porcentaje[dialog.aprosel_sb_slider1.progress]*price).toLong()
        dialog.aprosel_tv_total.setText(funciones.formato(price+total))
    }

    //button minus
    fun clickMinus(){
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

    // ******************************LISTENERS****************************************
    override fun afterTextChanged(p0: Editable?) {
        com_edit_change.value=true
        autoComplete(p0.toString().toLowerCase(Locale.getDefault()))
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











