package com.mateocvas.caria.base

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

internal class DBHelper(private val contexto: Context) :
    SQLiteOpenHelper(contexto, "BASE", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
         db.execSQL("CREATE TABLE $table1 ($c1 INTEGER PRIMARY KEY AUTOINCREMENT, $c2 TEXT UNIQUE NOT NULL, $c3 INTEGER, $c4 INTEGER, $c5 INTEGER, $c6 TEXT NOT NULL, $c7 TEXT NOT NULL,$c8 TEXT NOT NULL)")
         db.execSQL("CREATE TABLE $table2 ($c1 INTEGER PRIMARY KEY AUTOINCREMENT, $c2 TEXT  NOT NULL, $c3 INTEGER, $c4 INTEGER, $c5 INTEGER, $c6 TEXT NOT NULL, $c7 TEXT NOT NULL, $c8 TEXT NOT NULL,$c9 TEXT NOT NULL, $c10 INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

     companion object {

        private val table1 = "bascket"
        private val table2 = "pedidos"
        private val c1 = "Id"
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


