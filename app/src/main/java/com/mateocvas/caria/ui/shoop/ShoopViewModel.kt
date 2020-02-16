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
import kotlin.collections.ArrayList

class ShoopViewModel : ViewModel(),View.OnClickListener, TextWatcher, SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {


        if(selected_item.path.equals(app_context.getString(R.string.tag_fruver)))
            if(p0!!.id==R.id.aprosel_sb_slider1)
                if(selected_item.ventanan.toInt()==2 ||selected_item.ventanan.toInt()==4){
                    selected_item.tamano=p1
                    dialog.aprosel_tv_slider1.setText(("Tamaño: "+array_size[p1]))
                    //if(p1==2 && aux==1 )
                      //  selected_item.precio=funciones.formato((funciones.desformato(selected_item.precio)*1.25).toLong())

                }
                else{
                    selected_item.madure=p1
                    dialog.aprosel_tv_slider1.setText(("Maduracion: "+array_maduration[p1]))
                }

        else {
            dialog.aprosel_tv_slider2.setText(("Maduracion: "+array_maduration[p1]))
            selected_item.madure = p1
        }

        aux=p1
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
    }

    val db=FirebaseFirestore.getInstance()
    val funciones=Funciones()
    val app_context=MyApp.instance.applicationContext
    val array_size =arrayOf(app_context.getString(R.string.seekbar_tam0),app_context.getString(R.string.seekbar_tam1),app_context.getString(R.string.seekbar_tam2))
    val array_maduration=arrayOf(app_context.getString(R.string.seekbar_mad0), app_context.getString(R.string.seekbar_mad1),app_context.getString(R.string.seekbar_mad2))

    var aux=0
    lateinit var toast:Toast
    val warnings=Warinings()
    lateinit var dialog:Dialog
    var selected_tab=MyApp.instance.applicationContext.getString(R.string.tag_tab1)
    lateinit var selected_item:ItemProduct
    lateinit var comunication: Comunication
    var total:Long=0
    var amount:Int=1
    private lateinit var dependencia: ShoopFragment


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


            toast= Toast.makeText(fragment.context, app_context.getString(R.string.toast_error_agregado_previamente),Toast.LENGTH_LONG)
            toast.duration=Toast.LENGTH_LONG
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
                if (fruver.nombre.contains(text)) {
                    data_fruver_search.add(fruver)
                }
            adapter_fruver.notifyDataSetChanged()
        }
        else if(selected_tab.equals(app_context.getString(R.string.tag_tab2))){
                data_food_search.clear()
                for(food in data_food)
                    if (food.nombre.contains(text)){
                        data_food_search.add(food)
                    }
                 adapter_food.notifyDataSetChanged()
        }

        else {
            data_medicinal_search.clear()
            for (medicinal in data_food)
                if (medicinal.nombre.contains(text)) {
                    data_food_search.add(medicinal)
                }
            adapter_medicinal.notifyDataSetChanged()
        }
    }

    //unlocks the slected item in its respective lis and send it to the market
    fun clickAddItem(){

        if(selected_item.path.equals(app_context.getString(R.string.tag_food)))
            data_food_search.find { it.nombre.equals(selected_item.nombre)}!!.alreadyBougtth=true
        else if(selected_item.path.equals(app_context.getString(R.string.tag_medicinal)))
            data_medicinal_search.find { it.nombre.equals(selected_item.nombre)}!!.alreadyBougtth=true
        else
            data_fruver_search.find { it.nombre.equals(selected_item.nombre)}!!.alreadyBougtth=true

        comunication.addItemBascket(selected_item)
        dialog.dismiss()
    }

    fun itemSlected(item:ItemProduct){
        selected_item=item

        if(item.alreadyBougtth)
            toast.show()
        else {
            GlideApp.with(dialog.context)
                .load(FirebaseStorage.getInstance().reference.child(item.path + "/" + item.nombre + ".png"))
                .into(dialog.aprosel_iv_icono)
            dialog.aprosel_tv_total.setText(item.precio)
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
            else if(item.ventanan.toInt()==2 ||item.ventanan.toInt()==3 ){
                dialog.aprosel_sb_slider1.progress=1
                dialog.aprosel_tv_slider2.visibility=View.GONE
                dialog.aprosel_sb_slider2.visibility=View.GONE
                if(item.ventanan.toInt()==2)
                    dialog.aprosel_tv_slider1.setText(array_size[1])
                else if(item.ventanan.toInt()==3)
                    dialog.aprosel_tv_slider1.setText(array_maduration[1])
            }





            dialog.show()
        }
    }

    //button plus
    fun clickPlus(){
        total += funciones.desformato(selected_item.precio)
        amount++
        dialog.aprosel_tv_unidad.setText(amount.toString())
        dialog.aprosel_tv_total.setText(funciones.formato(funciones.desformato(selected_item.precio)*amount))
    }

    //button minus
    fun clickMinus(){
        if(amount==1)
            warnings.prosel_minus(dialog.context)
        else{
            total -= funciones.desformato(selected_item.precio)
            amount--
            dialog.aprosel_tv_unidad.setText(amount.toString())
            dialog.aprosel_tv_total.setText( funciones.formato(funciones.desformato(selected_item.precio)*amount))
        }

    }

    // ******************************LISTENERS****************************************
    override fun afterTextChanged(p0: Editable?) {
        autoComplete(p0.toString())
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











