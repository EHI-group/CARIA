package com.mateocvas.caria.ui

import android.content.Context
import com.google.firebase.database.*
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.mateocvas.caria.Funciones
import com.mateocvas.caria.MyApp
import com.mateocvas.caria.R
import com.mateocvas.caria.base.DBManager
import com.mateocvas.caria.items.ItemOrder
import com.mateocvas.caria.items.ItemProduct
import java.util.*
import kotlin.collections.ArrayList

class Comunication : ViewModel() {

    var text_fruver=""
    var text_food=""
    var text_medicinal=""

    private var item_number=0
    var item_number_com=MutableLiveData<Int>()

    val funciones = Funciones()
    private val base = DBManager(MyApp.instance.applicationContext)
    val app_context: Context = MyApp.instance.applicationContext
    private val author = FirebaseAuth.getInstance().currentUser!!.uid

    var tabind=0


    private val data_fruver = ArrayList<ItemProduct>()
    private val data_food = ArrayList<ItemProduct>()
    private val data_medicinal = ArrayList<ItemProduct>()


    val data_fruver_com = MutableLiveData<ArrayList<ItemProduct>>()
    val data_food_com = MutableLiveData<ArrayList<ItemProduct>>()
    val data_medicinal_com = MutableLiveData<ArrayList<ItemProduct>>()

    private val bascket_fruver = ArrayList<ItemProduct>()
    private val bascket_food = ArrayList<ItemProduct>()


    private val functiones = Funciones()
    val com_data_pedidos = MutableLiveData<ArrayList<ItemOrder>>()
    private val data_pedidos = ArrayList<ItemOrder>()

    val bascket_fruver_com = MutableLiveData<ArrayList<ItemProduct>>()

    val bascket_food_com = MutableLiveData<ArrayList<ItemProduct>>()

    private val bascket_medicinal = ArrayList<ItemProduct>()
    val bascket_medicinal_com = MutableLiveData<ArrayList<ItemProduct>>()

    private val all_basckets= arrayOf(bascket_fruver,bascket_food,bascket_medicinal)


    var total: Long = 0
    val com_total = MutableLiveData<String>()


    private val com_clear_bascket = MutableLiveData<Boolean>()


    val cloud_data = FirebaseDatabase.getInstance().reference

    val tipo_productos: Array<String> = app_context.resources.getStringArray(R.array.tipo_productos)




    init {
        loadData()


        cloud_data.child("control/$author").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.child("estado").value!! != "0" && p0.child("estado").value!! != "1") {
                    val pref = app_context.getSharedPreferences("user", Context.MODE_PRIVATE)
                    val editor = pref.edit()
                    editor.putInt("numero", pref.getInt("numero", 0) + 1)
                    editor.apply()
                    cloud_data.child("control/$author/estado").setValue("0")
                }
            }

        })
    }


    fun setTotal2(total:Long){
        this.total=total
        this.com_total.value=funciones.formato(total)
    }

    fun updatedBascket(item: ItemProduct) {

        if (item.path == app_context.getString(R.string.tag_food)) {
            val index = bascket_food.indexOfFirst { it.nombre == item.nombre }
            total -= (bascket_food[index].cantidad) * funciones.desformato(bascket_food[index].precio)
            bascket_food[index] = item
            bascket_food_com.value = bascket_food
        } else if (item.path == app_context.getString(R.string.tag_medicinal)) {
            val index = bascket_medicinal.indexOfFirst { it.nombre == item.nombre }
            total -= (bascket_medicinal[index].cantidad) * funciones.desformato(bascket_medicinal[index].precio)
            bascket_medicinal[index] = item
            bascket_medicinal_com.value = bascket_medicinal
        } else {
            val index = bascket_fruver.indexOfFirst { it.nombre == item.nombre }
            total -= (bascket_fruver[index].cantidad) * funciones.desformato(bascket_fruver[index].precio)
            bascket_fruver[bascket_fruver.indexOfFirst { it.nombre == item.nombre }] = item
            bascket_fruver_com.value = bascket_fruver
        }


        total = 0

        for (it in bascket_fruver)
            total += it.cantidad * (funciones.desformato(it.precio))
        for (it in bascket_medicinal)
            total += it.cantidad * (funciones.desformato(it.precio))
        for (it in bascket_food)
            total += it.cantidad * (funciones.desformato(it.precio))





        com_total.value = funciones.formato(total)
        base.canastaUpdate(
            item.nombre,
            item.cantidad,
            item.ind1,
            item.ind2,
            "",
            item.mensaje
        )
    }

    // remove one n item from de bascket list and make it unlocked in the shoop
    fun removeItemBascket(item: ItemProduct) {
        setNumber(-1)
        if (item.path == app_context.getString(R.string.tag_food)) {
            bascket_food.removeIf { it.nombre == item.nombre }
            (data_food.find { it.nombre == item.nombre })!!.alreadyBougtth = false
            bascket_food_com.value = bascket_food
            data_food_com.value = data_food
            var aux=item.cantidad * (funciones.desformato(item.precio))
            if(item.ind1!=-1) {
                aux=(aux*item.porcentaje1[item.ind1]).toLong()
                if(item.ind2!=-1)
                    aux=(aux*item.porcentaje2[item.ind2]).toLong()
            }
            total -= aux
        } else if (item.path == app_context.getString(R.string.tag_medicinal)) {
            bascket_medicinal.removeIf { it.nombre == item.nombre }
            (data_medicinal.find { it.nombre == item.nombre })!!.alreadyBougtth = false
            bascket_medicinal_com.value = bascket_fruver
            data_medicinal_com.value = data_fruver
            var aux=item.cantidad * (funciones.desformato(item.precio))
            if(item.ind1!=-1) {
                aux=(aux*item.porcentaje1[item.ind1]).toLong()
                if(item.ind2!=-1)
                    aux=(aux*item.porcentaje2[item.ind2]).toLong()
            }
            total -= aux
        } else {
            bascket_fruver.removeIf { it.nombre == item.nombre }
            (data_fruver.find { it.nombre == item.nombre })!!.alreadyBougtth = false
            bascket_fruver_com.value = bascket_fruver
            data_fruver_com.value = data_fruver
            var aux=item.cantidad * (funciones.desformato(item.precio))
            if(item.ind1!=-1) {
                aux=(aux*item.porcentaje1[item.ind1]).toLong()
                if(item.ind2!=-1)
                    aux=(aux*item.porcentaje2[item.ind2]).toLong()
            }
            total -= aux
        }
        com_total.value = funciones.formato(total)
        base.canastaDelete(item.nombre)
    }

    private fun setNumber(num:Int){
        item_number+=num
        item_number_com.value=item_number
    }

    // add item to its respective bascket and save it into the local database
    fun addItemBascket(item: ItemProduct) {

        setNumber(1)
        val all_data= arrayOf(data_fruver,data_food,data_medicinal)
        val all_bascket= arrayOf(bascket_fruver,bascket_food,bascket_medicinal)
        val all_data_com= arrayOf(data_fruver_com,data_food_com,data_medicinal_com)
        val all_bascket_com= arrayOf(bascket_fruver_com,bascket_food_com,bascket_medicinal_com)
        var ind=0
        if(item.path == tipo_productos[1])
            ind=1
        else if(item.path == tipo_productos[2])
            ind=2

        val temp = all_data[ind].find { it.nombre == item.nombre }
        temp!!.alreadyBougtth = true
        all_bascket[ind].add(temp)
        all_bascket[ind].sortBy { it.nombre }
        all_bascket_com[ind].value =all_bascket[ind]
        all_data_com[ind].value = all_data[ind]


        total += funciones.desformato(item.precio) * item.cantidad
        com_total.value = funciones.formato(total)
        base.canastaAdd(
            item.nombre,
            item.cantidad,
            item.ind1,
            item.ind2,
            "",
            item.mensaje,
            item.path
        )


    }

    // delete de data from bascket and unlock the data
    fun deleteAllBascket() {
        setNumber(-item_number)

        base.canastaDeleteAll()
        total = 0
        com_total.value = functiones.formato(total)
        bascket_medicinal.clear()
        bascket_fruver.clear()
        bascket_food.clear()

        com_clear_bascket.value = true

        data_fruver.forEach {
            it.alreadyBougtth = false
            it.cantidad = 1
            base.setCaracteristicas(it.nombre,it.ind1,it.ind2)
        }

        data_food.forEach {
            it.alreadyBougtth = false
            it.cantidad = 1
            base.setCaracteristicas(it.nombre,it.ind1,it.ind2)
        }

        data_medicinal.forEach {
            it.alreadyBougtth = false
            it.cantidad = 1
            base.setCaracteristicas(it.nombre,it.ind1,it.ind2)
        }

        data_fruver_com.value = data_fruver
        data_food_com.value = data_food
        data_medicinal_com.value = data_medicinal

        bascket_food_com.value = bascket_food
        bascket_medicinal_com.value = bascket_medicinal
        bascket_fruver_com.value = bascket_fruver

        com_total.value = funciones.formato(0)
    }


    fun saveOrder() {


        val preferences = app_context!!.getSharedPreferences("user", Context.MODE_PRIVATE)
        val c = Calendar.getInstance()
        val date =
            c.get(Calendar.YEAR).toString() + "/" + c.get(Calendar.MONTH).toString() + "/" + c.get(
                Calendar.DAY_OF_MONTH
            ).toString()
        val number = preferences.getInt("numero", 0)



        base.pedidosDelete(number)
        val order = ItemOrder(total, date, number, ArrayList(), ArrayList(), ArrayList())

    for(i in 0 until 3)
        all_basckets[i].forEach {
            base.pedidosAdd(
                it.nombre,
                it.cantidad,
                it.ind1,
                it.ind2,
                "",
                it.mensaje,
                it.path,
                date,
                number
            )
            if(i==0)
                order.fruver.add(it.copy())
            else if(i==1)
                order.food.add(it.copy())
            else if(i==2)
                order.medicinal.add(it.copy())
        }
        deleteAllBascket()
        data_pedidos.removeIf { it.numero == order.numero }
        data_pedidos.add(order)
        com_data_pedidos.value = data_pedidos


        cloud_data.child("pedidos/$author+/pedido").removeValue()
        for (i in 0 until 2) {
            val temp: ArrayList<ItemProduct>
            temp = if (i == 0)
                order.fruver
            else if (i == 1)
                order.food
            else
                order.medicinal

            for (item in temp) {
                val product = hashMapOf(
                    "nombre" to item.nombreMostrar,
                    "precio" to funciones.desformato(item.precio),
                    "cantidad" to item.cantidad,
                    "unidad" to item.unidad,
                    "mensaje" to item.mensaje
                )

                if (item.ind1 != -1) {
                    product["tipo1"] = item.array1[item.ind1]
                    var aux = (funciones.desformato(item.precio) * item.porcentaje1[item.ind1])
                    if (item.ind2 != -1) {
                        aux *= item.porcentaje2[item.ind2]
                        product["tipo2"] = item.porcentaje2[item.ind2]
                    }
                    product["precio"] = aux
                }

                cloud_data.child("pedidos/${author}/pedido/"+tipo_productos[i]).child(item.nombre)
                    .setValue(product)
            }


        }





        val user = hashMapOf(
            "fecha" to date,
            "nombre" to preferences.getString("name", ""),
            "numero" to preferences.getString("number1", ""),
            "direccion" to preferences.getString(
                "city",
                ""
            ) + " - " + preferences.getString("aldres", "")
        )

        cloud_data.child("pedidos/$author/informacion").setValue(user)
        cloud_data.child("control/$author/estado").setValue("1")


    }

    private fun loadregister() {

        val cursor = base.queryAllPedidos()
        cursor.moveToFirst()
        if (cursor.count > 0) {
            var order = ItemOrder(
                0,
                cursor.getString(8),
                cursor.getInt(9),
                ArrayList(),
                ArrayList(),
                ArrayList()
            )
            for (i in 0 until cursor.count) {

                if (cursor.getInt(9) != order.numero) {
                    data_pedidos.add(order)
                    order = ItemOrder(
                        0,
                        cursor.getString(8),
                        cursor.getInt(9),
                        ArrayList(),
                        ArrayList(),
                        ArrayList()
                    )
                }

                val all_data= arrayListOf(data_fruver,data_food,data_medicinal)


                for(k in 0 until 3)
                if (cursor.getString(7) == tipo_productos[k]) {
                    val temp = all_data[k].find { it.nombre == cursor.getString(1) }!!.copy()
                    temp.cantidad = cursor.getInt(2)
                    temp.ind1 = cursor.getInt(3)
                    temp.ind2 = cursor.getInt(4)
                    temp.mensaje = cursor.getString(6)
                   if(k==0)
                    order.fruver.add(temp)
                   else if(k==1)
                       order.food.add(temp)
                   else if(k==2)
                       order.medicinal.add(temp)


                }
                cursor.moveToNext()
            }
            data_pedidos.add(order)
            com_data_pedidos.postValue(data_pedidos)
        }
    }

    //||||||||||||||||||||||||||||||||||||||FRAGMENT REGISTER ||||||||||||||||||||||||||||||||||||||

    fun setOrder(itemOrder: ItemOrder) {
        base.canastaDeleteAll()

        for (item in bascket_fruver)
            data_fruver.find { item.nombre == it.nombre }!!.alreadyBougtth = false

        for (item in bascket_food)
            data_food.find { item.nombre == it.nombre }!!.alreadyBougtth = false

        for (item in bascket_medicinal)
            data_medicinal.find { item.nombre == it.nombre }!!.alreadyBougtth = false


        bascket_fruver.clear()
        bascket_food.clear()
        bascket_medicinal.clear()


        total = 0

        for (item in itemOrder.fruver) {
            item.precio = data_fruver.find { it.nombre == item.nombre }!!.precio
            addItemBascket(item)
        }

        for (item in itemOrder.food) {
            item.precio = data_food.find { it.nombre == item.nombre }!!.precio
            addItemBascket(item)
        }

        for (item in itemOrder.medicinal) {
            item.precio = data_medicinal.find { it.nombre == item.nombre }!!.precio
            addItemBascket(item)
        }


    }


    //||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||


    // Load the start data into the variables
    private fun loadData() {

        cloud_data.child("productos").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (tipo in tipo_productos) {
                    val temp: ArrayList<ItemProduct>
                    temp = if (tipo == tipo_productos[0])
                        data_fruver
                    else if (tipo == tipo_productos[1])
                        data_food
                    else
                        data_medicinal

                    for (item in p0.child(tipo).children) {
                        Log.wtf("mateo",      item.child("nombre").value as String)

                        temp.add(
                            ItemProduct(
                                item.child("nombre").value as String,
                                item.child( "nombreMostrar").value as String,
                                funciones.formato(item.child("precio").value as Long),
                                1,
                                false,
                                item.child("unidad").value as String,
                                item.child("descripcion").value as String,
                                tipo,
                                "",
                                "",
                                item.child("ventana").value as Long,
                                -1,
                                -1,
                                "",
                                "",
                                ArrayList(),
                                ArrayList(),
                                ArrayList(),
                                ArrayList()
                            )
                        )

                        if (item.child("array1").exists()) {
                            temp.last()
                                .array1.addAll(item.child("array1").value as ArrayList<String>)
                            temp.last()
                                .porcentaje1.addAll(item.child("porcentaje1").value as ArrayList<Double>)
                            temp.last().ind1 = 0
                            temp.last().tipo1= item.child("tipo1").value as String
                        }
                        if (item.child("array2").exists()) {
                            temp.last()
                                .array2.addAll(item.child("array2").value as ArrayList<String>)
                            temp.last()
                                .porcentaje2.addAll(item.child( "porcentaje2").value as ArrayList<Double>)
                            temp.last().ind2 = 0
                            temp.last().tipo2= item.child("tipo2").value as String
                        }
                    }
                }

                data_fruver_com.value=data_fruver
                data_food_com.value=data_food
                data_medicinal_com.value=data_medicinal
                loadCharacteristics()
                reloadBascket()
                loadregister()
            }

        })

    }

    // reload the last saved items in the basckert**************************************************


    private fun loadCharacteristics(){
        for(cate in arrayOf(data_fruver,data_food,data_medicinal))
          for(item in cate){
              val c=base.getCaracteristicas(item.nombre)
              c.moveToFirst()
              if(c.count==0)
                  continue
              if(item.ind1!=-1 )
                  item.ind1=c.getInt(2)
              if(item.ind2!=-1 )
                  item.ind2=c.getInt(3)
          }
 }


    private fun reloadBascket() {

        setNumber(-item_number)
        val all_data= arrayOf(data_fruver,data_food,data_medicinal)
        val all_bascket= arrayOf(bascket_fruver,bascket_food,bascket_medicinal)
        for (j in 0 until 3) {
            val cursor = base.queryAllCanasta(tipo_productos[j])
            cursor.moveToFirst()
            for (i in 0 until cursor.count) {
                setNumber(1)
                val item = all_data[j].find { it.nombre == cursor.getString(1) }!!
                item.cantidad = cursor.getInt(2)
                item.ind1 = cursor.getInt(3)
                item.ind2 = cursor.getInt(4)
                item.mensaje = cursor.getString(6)
                item.alreadyBougtth = true
                all_bascket[j].add(item)


                var aux= funciones.desformato(item.precio) * item.cantidad
                if(item.ind1!=-1)
                    aux=(aux*item.porcentaje1[item.ind1]).toLong()
                if(item.ind2!=-1)
                    aux=(aux*item.porcentaje2[item.ind2]).toLong()
                total += aux
                cursor.moveToNext()
            }

            cursor.close()
        }

        com_total.value = funciones.formato(total)

        bascket_fruver_com.value = bascket_fruver
        data_fruver_com.value = data_fruver

        bascket_food_com.value = bascket_food
        data_food_com.value = data_food

        bascket_medicinal_com.value = bascket_medicinal
        data_medicinal_com.value = data_medicinal
    }
    //**********************************************************************************************




}