package com.mateocvas.caria.base

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

internal class DBHelper(  contexto: Context) :
    SQLiteOpenHelper(contexto, "BASE", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
         db.execSQL("CREATE TABLE $table1 ($c1 INTEGER PRIMARY KEY AUTOINCREMENT, $c2 TEXT UNIQUE NOT NULL, $c3 INTEGER, $c4 INTEGER, $c5 INTEGER, $c6 TEXT NOT NULL, $c7 TEXT NOT NULL,$c8 TEXT NOT NULL)")
         db.execSQL("CREATE TABLE $table2 ($c1 INTEGER PRIMARY KEY AUTOINCREMENT, $c2 TEXT  NOT NULL, $c3 INTEGER, $c4 INTEGER, $c5 INTEGER, $c6 TEXT NOT NULL, $c7 TEXT NOT NULL, $c8 TEXT NOT NULL,$c9 TEXT NOT NULL, $c10 INTEGER)")
         db.execSQL("CREATE TABLE $table3 ($c1 INTEGER PRIMARY KEY AUTOINCREMENT, $c2 TEXT  NOT NULL, $c4 INTEGER, $c5 INTEGER)")

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

     companion object {
        private const val table1 = "bascket"
        private const val table2 = "pedidos"
        private const val table3 = "caracteristicas"
        private const val c1 = "Id"
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


