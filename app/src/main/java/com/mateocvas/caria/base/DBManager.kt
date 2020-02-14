package com.mateocvas.caria.base

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class DBManager (context: Context){

    private val base: SQLiteDatabase
    private val helper:DBHelper

    init{
        helper = DBHelper(context)
        base = helper.getWritableDatabase()
    }


    fun queryAllCanasta(tipo:String):Cursor {
        return base.rawQuery("SELECT * FROM $table1 WHERE $c8 = '$tipo';", null)
    }

    fun canastaAdd(nombre: String, cantidad: Int, tamano: Int,maduracion:Int,tipo:String,mensaje:String,clasificacion:String) {
        base.execSQL("INSERT OR IGNORE INTO $table1 ($c2,$c3,$c4,$c5,$c6,$c7,$c8) VALUES ('$nombre',$cantidad,$tamano,$maduracion,'$tipo','$mensaje','$clasificacion');")
    }

    fun canastaDeleteAll() {
        base.execSQL("DELETE FROM $table1;")
    }

    fun canastaDelete(nombre: String) {
        base.execSQL("DELETE FROM $table1 WHERE $c2 ='$nombre';")


    }

    fun canastaUpdate(nombre: String, cantidad: Int, tamano: Int,maduracion:Int,tipo:String,mensaje:String) {
        base.execSQL("UPDATE $table1 SET $c3=$cantidad, $c4=$tamano, $c5=$maduracion, $c6='$tipo', $c7='$mensaje' WHERE $c2='$nombre'")
    }




    fun pedidosDelete(num: Int) {
        base.execSQL("DELETE FROM $table2 WHERE $c10 =$num;")


    }

    fun pedidosAdd(nombre: String, cantidad: Int, tamano: Int,maduracion:Int,tipo:String,mensaje:String,clasificacoion:String,fecha:String,num:Int) {
        val c=queryAllPedidos()
        base.execSQL("INSERT INTO $table2 ($c2,$c3,$c4,$c5,$c6,$c7,$c8,$c9,$c10) VALUES ('$nombre',$cantidad,$tamano,$maduracion,'$tipo','$mensaje','$clasificacoion','$fecha',$num)")
        val f=queryAllPedidos()
        val a=5
    }

    fun queryAllPedidos():Cursor {
        return base.rawQuery("SELECT * FROM $table2", null)
    }

    fun pedidosUpdate(number:Int, nombre: String, cantidad: Int, tamano: Int,maduracion:Int,tipo:String,mensaje:String,clasificacion:String,fecha:String, num:Int) {
        base.execSQL("DELETE FROM $table2 WHERE $c10 = $number;")
        base.execSQL("INSERT OR IGNORE INTO $table2 ($c2,$c3,$c4,$c5,$c6,$c7,$c8,$c9,$c10) VALUES ('$nombre',$cantidad,$tamano,$maduracion,'$tipo','$mensaje','$clasificacion','$fecha',$num)")

    }

    companion object {
        private val table1 = "bascket"
        private val table2 = "pedidos"
        private val c2 = "nombre"
        private val c3 = "cantidad"
        private val c4 = "tamano"
        private val c5 = "maduracion"
        private val c6 = "tipo"
        private val c7 = "mensaje"
        private val c8 = "clasificacion"
        private val c9 = "fecha"
        private val c10="numero"
    }


}