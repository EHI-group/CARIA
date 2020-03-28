package com.mateocvas.caria.base

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class DBManager (context: Context){

    private val base: SQLiteDatabase
    private val helper:DBHelper = DBHelper(context)

    init{
        base = helper.writableDatabase
    }


    fun queryAllCanasta(tipo:String):Cursor {
        return base.rawQuery("SELECT * FROM $table1 WHERE $c8 = '$tipo';", null)
    }

    fun canastaAdd(nombre: String, cantidad: Int, tamano: Int,maduracion:Int,tipo:String,mensaje:String,clasificacion:String) {
        base.execSQL("INSERT OR IGNORE INTO $table1 ($c2,$c3,$c4,$c5,$c6,$c7,$c8) VALUES ('$nombre',$cantidad,$tamano,$maduracion,'$tipo','$mensaje','$clasificacion');")
        Log.wtf("ronaldo","$nombre   $tamano   $maduracion")
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


    fun setCaracteristicas(nombre: String,ind1:Int,ind2:Int) {
        if (getCaracteristicas(nombre).count==0)
            base.execSQL("INSERT INTO $table3( $c2, $c4, $c5) VALUES('$nombre', $ind1, $ind2)")
        else
            base.execSQL("UPDATE $table3 SET $c4=$ind1, $c5=$ind2 WHERE $c2 ='$nombre';")
    }

    fun getCaracteristicas(nombre: String):Cursor {
        return base.rawQuery("SELECT * FROM $table3 WHERE $c2 ='$nombre';", null)
    }


    fun pedidosDelete(num: Int) {
        base.execSQL("DELETE FROM $table2 WHERE $c10 =$num;")
    }

    fun pedidosAdd(nombre: String, cantidad: Int, tamano: Int,maduracion:Int,tipo:String,mensaje:String,clasificacoion:String,fecha:String,num:Int) {
        val c=queryAllPedidos()
        base.execSQL("INSERT INTO $table2 ($c2,$c3,$c4,$c5,$c6,$c7,$c8,$c9,$c10) VALUES ('$nombre',$cantidad,$tamano,$maduracion,'$tipo','$mensaje','$clasificacoion','$fecha',$num)")
    }

    fun queryAllPedidos():Cursor {
        return base.rawQuery("SELECT * FROM $table2", null)
    }


    companion object {
        private const val table1 = "bascket"
        private const val table2 = "pedidos"
        private const val table3 = "caracteristicas"
        private const val c2 = "nombre"
        private const val c3 = "cantidad"
        private const val c4 = "ind1"
        private const val c5 = "ind2"
        private const val c6 = "tipo"
        private const val c7 = "mensaje"
        private const val c8 = "clasificacion"
        private const val c9 = "fecha"
        private const val c10="numero"
    }


}