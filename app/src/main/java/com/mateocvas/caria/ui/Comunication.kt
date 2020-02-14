package com.mateocvas.caria.ui

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mateocvas.caria.Funciones
import com.mateocvas.caria.MyApp
import com.mateocvas.caria.R
import com.mateocvas.caria.base.DBManager
import com.mateocvas.caria.items.ItemOrder
import com.mateocvas.caria.items.ItemProduct
import java.util.*
import kotlin.collections.ArrayList

class Comunication:ViewModel() {

    var cond= arrayOf(false,true,true)
    val db= FirebaseFirestore.getInstance()
    val funciones=Funciones()
    val base=DBManager(MyApp.instance.applicationContext)
    val app_context=MyApp.instance.applicationContext




    private val data_fruver=ArrayList<ItemProduct>()
    private val data_food=ArrayList<ItemProduct>()
    private val data_medicinal=ArrayList<ItemProduct>()
    private val author=FirebaseAuth.getInstance()



    val data_fruver_com=MutableLiveData<ArrayList<ItemProduct>>()
    val data_food_com=MutableLiveData<ArrayList<ItemProduct>>()
    val data_medicinal_com=MutableLiveData<ArrayList<ItemProduct>>()

    val bascket_fruver=ArrayList<ItemProduct>()
    val bascket_food=ArrayList<ItemProduct>()


    val functiones=Funciones()
    val com_data_pedidos=MutableLiveData<ArrayList<ItemOrder>>()
    val data_pedidos=ArrayList<ItemOrder>()

    val bascket_fruver_com= MutableLiveData<ArrayList<ItemProduct>>()

    val bascket_food_com= MutableLiveData<ArrayList<ItemProduct>>()

    val bascket_medicinal=ArrayList<ItemProduct>()
    val bascket_medicinal_com= MutableLiveData<ArrayList<ItemProduct>>()

    var total:Long=0
    val com_total=MutableLiveData<String>()


    val com_clear_bascket=MutableLiveData<Boolean>()




    init {
        loadData()

        Thread(Runnable {
            while (true)
                if (cond[0]&&cond[1]&&cond[2])
                    break
            loadregister()
        }).start()



        val docRef = db.collection("control").document(FirebaseAuth.getInstance().currentUser!!.uid)



        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                if(!(snapshot.data!!["estado"] as String)!!.equals("0")) {
                    val pref=app_context.getSharedPreferences("user", Context.MODE_PRIVATE)
                    val editor=pref.edit()
                    editor.putInt("numero",pref.getInt("numero",0)+1)
                    editor.apply()
                    docRef.set(hashMapOf("estado" to "0"))

                }
            } else {
            }
        }


    }


    fun updatedBascket(item: ItemProduct){

        if(item.path.equals(app_context.getString(R.string.tag_food))) {
            val index=bascket_food.indexOfFirst { it.equals(item.nombre) }
            total-=(bascket_food[index].cantidad)*funciones.desformato(bascket_food[index].precio)
            bascket_food.set(index, item)
            bascket_food_com.value=bascket_food
        }
        else if(item.path.equals(app_context.getString(R.string.tag_medicinal))) {
            val index=bascket_medicinal.indexOfFirst { it.equals(item.nombre) }
            total-=(bascket_medicinal[index].cantidad)*funciones.desformato(bascket_medicinal[index].precio)
            bascket_medicinal.set(index, item)
            bascket_medicinal_com.value=bascket_medicinal
        }
        else {
            val index=bascket_fruver.indexOfFirst { it.nombre.equals(item.nombre) }
            total-=(bascket_fruver[index].cantidad)*funciones.desformato(bascket_fruver[index].precio)
            bascket_fruver.set(bascket_fruver.indexOfFirst { it.nombre.equals(item.nombre) }, item)
            bascket_fruver_com.value=bascket_fruver

        }


        total=0

        for(it in bascket_fruver )
            total+= it.cantidad*(funciones.desformato(it.precio))
        for(it in bascket_medicinal )
            total+= it.cantidad*(funciones.desformato(it.precio))
        for(it in bascket_food )
            total+= it.cantidad*(funciones.desformato(it.precio))





        com_total.value=funciones.formato(total)
        base.canastaUpdate(item.nombre,item.cantidad,item.tamano,item.madure,item.tipo,item.mensaje)
    }

    // remove one n item from de bascket list and make it unlocked in the shoop
    fun removeItemBascket(item: ItemProduct){
        if(item.path.equals(app_context.getString(R.string.tag_food))){
            bascket_food.removeIf { it.nombre.equals(item.nombre) }
            (data_food.find {it.nombre.equals(item.nombre) })!!.alreadyBougtth=false
            bascket_food_com.value=bascket_food
            data_food_com.value=data_food
            total-= item.cantidad*funciones.desformato(item.precio)
        }
        else if(item.path.equals(app_context.getString(R.string.tag_medicinal))){
            bascket_medicinal.removeIf { it.nombre.equals(item.nombre) }
            (data_medicinal.find {it.nombre.equals(item.nombre) })!!.alreadyBougtth=false
            bascket_medicinal_com.value=bascket_fruver
            data_medicinal_com.value=data_fruver
            total-= item.cantidad*funciones.desformato(item.precio)

        }
        else{
            bascket_fruver.removeIf { it.nombre.equals(item.nombre) }
            (data_fruver.find {it.nombre.equals(item.nombre) })!!.alreadyBougtth=false
            bascket_fruver_com.value=bascket_fruver
            data_fruver_com.value=data_fruver
            total-= item.cantidad*funciones.desformato(item.precio)
        }
        com_total.value=funciones.formato(total)
        base.canastaDelete(item.nombre)
    }

    // add item to its respective bascket and save it into the local database
    fun addItemBascket(item:ItemProduct){
        if(item.path.equals(app_context.getString(R.string.tag_food))){
            val temp=data_food.find { it.nombre.equals(item.nombre)}
            temp!!.alreadyBougtth=true
            bascket_food.add(temp)
            bascket_food.sortBy { it.nombre }
            bascket_food_com.value=bascket_food
            data_food_com.value=data_food

        }


        else if(item.path.equals(app_context.getString(R.string.tag_medicinal))){
            val temp=data_medicinal.find { it.nombre.equals(item.nombre)}
            temp!!.alreadyBougtth=true
            bascket_medicinal.add(temp)
            bascket_medicinal.sortBy { it.nombre }
            bascket_medicinal_com.value=bascket_medicinal
            data_medicinal_com.value=data_medicinal

        }

        else{
            val temp=data_fruver.find { it.nombre.equals(item.nombre)}
            temp!!.alreadyBougtth=true
            bascket_fruver.add(temp)
            bascket_fruver.sortBy { it.nombre }
            bascket_fruver_com.value=bascket_fruver
            data_fruver_com.value=data_fruver
        }

        total+=funciones.desformato(item.precio)*item.cantidad
        com_total.value=funciones.formato(total)
        base.canastaAdd(item.nombre,item.cantidad,item.tamano,item.madure,item.tipo,item.mensaje,item.path)



    }

    // delete de data from bascket and unlock the data
    fun deleteAllBascket(){
        base.canastaDeleteAll()
        total=0
        com_total.value=functiones.formato(total)
        bascket_medicinal.clear()
        bascket_fruver.clear()
        bascket_food.clear()

        com_clear_bascket.value=true

        data_fruver.forEach {
            it.alreadyBougtth=false
            it.cantidad=1
        }

        data_food.forEach {
            it.alreadyBougtth=false
            it.cantidad=1
        }

        data_medicinal.forEach {
            it.alreadyBougtth=false
            it.cantidad=1
        }

        data_fruver_com.value=data_fruver
        data_food_com.value=data_food
        data_medicinal_com.value=data_medicinal

        bascket_food_com.value=bascket_food
        bascket_medicinal_com.value=bascket_medicinal
        bascket_fruver_com.value=bascket_fruver

        com_total.value=funciones.formato(0)
    }


     fun saveOrder(){


         val preferences = app_context!!.getSharedPreferences("user", Context.MODE_PRIVATE)
         val c = Calendar.getInstance()
         val date = c.get(Calendar.YEAR).toString() + "/" + c.get(Calendar.MONTH).toString() + "/" + c.get(Calendar.DAY_OF_MONTH).toString()
         val number=preferences.getInt("numero",0)



         base.pedidosDelete(number)
         val order=ItemOrder(total,date,number, ArrayList(),ArrayList(),ArrayList())


         bascket_fruver.forEach {
             base.pedidosAdd(it.nombre,it.cantidad,it.tamano,it.madure,it.tipo,it.mensaje,it.path,date,number)
             order.fruver.add(it.copy())
         }
         bascket_food.forEach {
             base.pedidosAdd(it.nombre,it.cantidad,it.tamano,it.madure,it.tipo,it.mensaje,it.path,date,number)
             order.food.add(it.copy())
         }
         bascket_medicinal.forEach {
             base.pedidosAdd(it.nombre,it.cantidad,it.tamano,it.madure,it.tipo,it.mensaje,it.path,date,number)
             order.medicinal.add(it.copy())
         }




         deleteAllBascket()
         data_pedidos.removeIf { it.numero==order.numero }
         data_pedidos.add(order)
         com_data_pedidos.value=data_pedidos


         db.collection("pedidos/${author.currentUser!!.uid}/food")
             .get()
             .addOnCompleteListener { task ->
                 if (task.isSuccessful && task.result!!.size()!=0)
                     for (producto in task.result!!)
                         producto.reference.delete()

                 for (item in order.fruver) {
                     val product = hashMapOf(
                         "name" to item.nombreMostrar,
                         "price" to item.precio,
                         "amount" to item.cantidad,
                         "mature" to item.madure,
                         "size" to item.tamano,
                         "message" to item.mensaje,
                         "tipo" to item.tipoEnviar
                     )

                     db.collection("pedidos").document(author.currentUser!!.uid)
                         .collection("fruver").document(item.nombre).set(product)
                 } }


         db.collection("pedidos/${author.currentUser!!.uid}/fruver")
             .get()
             .addOnCompleteListener { task ->
                 if (task.isSuccessful && task.result!!.size()!=0)
                     for (producto in task.result!!)
                         producto.reference.delete()

                 for (item in order.food) {
                     val product = hashMapOf(
                         "name" to item.nombreMostrar,
                         "price" to item.precio,
                         "amount" to item.cantidad,
                         "mature" to item.madure,
                         "size" to item.tamano,
                         "message" to item.mensaje,
                         "tipo" to item.tipoEnviar
                     )

                     db.collection("pedidos").document(author.currentUser!!.uid).collection("food").document(item.nombre).set(product)


                 }
             }

         db.collection("pedidos/${author.currentUser!!.uid}/medicinal")
             .get()
             .addOnCompleteListener { task ->
                 if (task.isSuccessful && task.result!!.size()!=0)
                     for (producto in task.result!!)
                         producto.reference.delete()

                 for (item in order.medicinal) {
                     val product = hashMapOf(
                         "name" to item.nombreMostrar,
                         "price" to item.precio,
                         "amount" to item.cantidad,
                         "mature" to item.madure,
                         "size" to item.tamano,
                         "message" to item.mensaje,
                         "tipo" to item.tipoEnviar
                     )
                     db.collection("pedidos").document(author.currentUser!!.uid).collection("medicinal").document(item.nombre).set(product)

                 }


             }

         db.collection("pedidos/${author.currentUser!!.uid}/user data").document("data").delete()

         db.collection("pedidos").document(author.currentUser!!.uid).delete()



         val user = hashMapOf(
             "date" to date,
             "name" to preferences.getString("name", ""),
             "number" to preferences.getString(
                 "number1",
                 ""
             ) + "/" + preferences.getString("number2", ""),
             "city" to preferences.getString("city", ""),
             "aldres" to preferences.getString("aldres", "")
         )


         db.collection("pedidos").document(author.currentUser!!.uid).collection("user data").document("data").set(user)




    }

    private fun loadregister() {



        val cursor=base.queryAllPedidos()
        cursor.moveToFirst()
        if(cursor.count>0) {
            var order = ItemOrder(0, cursor.getString(8), cursor.getInt(9), ArrayList(), ArrayList(), ArrayList())
            for (i in 0 until cursor.count) {

                if (cursor.getInt(9) != order.numero) {
                    data_pedidos.add(order)
                    order = ItemOrder(0, cursor.getString(8), cursor.getInt(9), ArrayList(), ArrayList(), ArrayList())
                }

                if (cursor.getString(7).equals(app_context.getString(R.string.tag_food))) {
                    val temp = data_food.find { it.nombre.equals(cursor.getString(1)) }!!
                    temp.cantidad = cursor.getInt(2)
                    temp.tamano = cursor.getInt(3)
                    temp.madure = cursor.getInt(4)
                    temp.tipo = cursor.getString(5)
                    temp.mensaje = cursor.getString(6)
                    order.food.add(temp)
                }

                else if (cursor.getString(7).equals(app_context.getString(R.string.tag_medicinal))) {
                    val temp = data_medicinal.find { it.nombre.equals(cursor.getString(1)) }!!
                    temp.cantidad = cursor.getInt(2)
                    temp.tamano = cursor.getInt(3)
                    temp.madure = cursor.getInt(4)
                    temp.tipo = cursor.getString(5)
                    temp.mensaje = cursor.getString(6)
                    order.medicinal.add(temp)
                }

                else {
                    val temp = data_fruver.find { it.nombre.equals(cursor.getString(1)) }!!
                    temp.cantidad = cursor.getInt(2)
                    temp.tamano = cursor.getInt(3)
                    temp.madure = cursor.getInt(4)
                    temp.tipo = cursor.getString(5)
                    temp.mensaje = cursor.getString(6)
                    order.fruver.add(temp)
                }
                cursor.moveToNext()
            }
            data_pedidos.add(order)
            com_data_pedidos.postValue ( data_pedidos)
        }
    }

    //||||||||||||||||||||||||||||||||||||||FRAGMENT REGISTER ||||||||||||||||||||||||||||||||||||||

     fun setOrder(itemOrder: ItemOrder){
         base.canastaDeleteAll()

         for (item in bascket_fruver)
            data_fruver.find { item.nombre.equals(it.nombre)}!!.alreadyBougtth=false

         for (item in bascket_food)
             data_food.find { item.nombre.equals(it.nombre)}!!.alreadyBougtth=false

         for (item in bascket_medicinal)
             data_medicinal.find { item.nombre.equals(it.nombre)}!!.alreadyBougtth=false


        bascket_fruver.clear()
        bascket_food.clear()
        bascket_medicinal.clear()


         total=0

         for (item in itemOrder.fruver) {
               item.precio=data_fruver.find { it.nombre.equals(item.nombre) }!!.precio
               addItemBascket(item)
         }

         for (item in itemOrder.food) {
             item.precio=data_food.find { it.nombre.equals(item.nombre) }!!.precio
             addItemBascket(item)
         }

         for (item in itemOrder.medicinal) {
             item.precio=data_medicinal.find { it.nombre.equals(item.nombre) }!!.precio
             addItemBascket(item)
         }


    }


    //||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||




    // Load the start data into the variables
    private fun loadData(){
        db.collection("products/"+app_context.getString(R.string.tag_fruver)+"/items")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (producto in task.result!!) {
                        Log.d("errorres",producto["nombre"] as String)
                        data_fruver.add(
                            ItemProduct(
                                producto["nombre"] as String,
                                producto["nombreMostrar"] as String,
                                funciones.formato(44),
                                1,
                                false,
                                producto["unidad"] as String,
                                "",
                                app_context.getString(R.string.tag_fruver),
                                1, 1,
                                "",
                                "",
                                "",
                                "",
                                producto["ventana"] as Long

                            )
                        )
                    }
                    data_fruver_com.value=data_fruver
                    reloadBadcketFruver()
                    cond[0]=true

                }

            }


        db.collection("products/"+app_context.getString(R.string.tag_food)+"/items")
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
                                app_context.getString(R.string.tag_food),
                                1, 1,
                                "",
                                "","",    "",
                                producto["ventana"] as Long
                            )
                        )
                    }
                    data_food_com.value=data_food
                    reloadBadcketFood()
                    cond[1]=true
                }

            }


        db.collection("products/"+app_context.getString(R.string.tag_medicinal)+"/items")
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
                                app_context.getString(R.string.tag_medicinal),
                                1, 1,
                                "",
                                "","",    "",
                                producto["ventana"] as Long

                            )
                        )
                    }
                    data_medicinal_com.value=data_medicinal
                    reloadBadcketMedicinal()
                    cond[2]=true

                }
            }


    }

    // reload the last saved items in the basckert**************************************************


    private fun reloadBadcketFruver(){
        val cursor = base.queryAllCanasta(app_context.getString(R.string.tag_fruver))
        cursor.moveToFirst()
        for (i in 0 until cursor.count) {
            val item = data_fruver.find { it.nombre.equals(cursor.getString(1)) }!!
            item.cantidad = cursor.getInt(2)
            item.tamano = cursor.getInt(3)
            item.madure = cursor.getInt(4)
            item.tipo = cursor.getString(5)
            item.mensaje = cursor.getString(6)
            item.alreadyBougtth = true
            bascket_fruver.add(item)
            total+=funciones.desformato(item.precio)*item.cantidad
            cursor.moveToNext()
        }

        com_total.value=funciones.formato(total)
        bascket_fruver_com.value = bascket_fruver
        data_fruver_com.value = data_fruver
        cursor.close()
    }


    private fun reloadBadcketFood(){
        val cursor = base.queryAllCanasta(app_context.getString(R.string.tag_food))
        cursor.moveToFirst()
        for (i in 0 until cursor.count) {
            Log.e("erro raro",cursor.getString(1)+" "+"  "+cursor.getString(7)+"     ")
            val item = data_food.find { it.nombre.equals(cursor.getString(1)) }!!
            item.cantidad = cursor.getInt(2)
            item.tamano = cursor.getInt(3)
            item.madure = cursor.getInt(4)
            item.tipo = cursor.getString(5)
            item.mensaje = cursor.getString(6)
            item.alreadyBougtth = true
            bascket_food.add(item)
            cursor.moveToNext()
            total+=funciones.desformato(item.precio)*item.cantidad
        }
        com_total.value=funciones.formato(total)
        bascket_food_com.value = bascket_food
        data_food_com.value = data_food
        cursor.close()
    }

    private fun reloadBadcketMedicinal(){
        val cursor = base.queryAllCanasta(app_context.getString(R.string.tag_medicinal))
        cursor.moveToFirst()
        for (i in 0 until cursor.count) {
            Log.e("erro raro",cursor.getString(1)+" "+"  "+cursor.getString(7)+"     ")
            val item = data_food.find { it.nombre.equals(cursor.getString(1)) }!!
            item.cantidad = cursor.getInt(2)
            item.tamano = cursor.getInt(3)
            item.madure = cursor.getInt(4)
            item.tipo = cursor.getString(5)
            item.mensaje = cursor.getString(6)
            item.alreadyBougtth = true
            bascket_medicinal.add(item)
            total+=funciones.desformato(item.precio)*item.cantidad
            cursor.moveToNext()
        }
        com_total.value=funciones.formato(total)
        bascket_medicinal_com.value = bascket_medicinal
        data_medicinal_com.value = data_medicinal
        cursor.close()
    }


    //**********************************************************************************************

}