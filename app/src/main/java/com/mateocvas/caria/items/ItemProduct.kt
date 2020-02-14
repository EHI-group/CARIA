package com.mateocvas.caria.items

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@SuppressLint("ParcelCreator")
@Parcelize
data class ItemProduct(val nombre:String,
                  val nombreMostrar:String,
                  var precio:String,
                  var cantidad :Int=0,
                  var alreadyBougtth:Boolean=false,
                  val unidad:String,
                  val beneficios:String,
                  var path:String,
                  var tamano:Int,
                  var madure:Int,
                  var palabrasClave:String,
                  var tipo:String,
                  var mensaje:String,
                  var tipoEnviar:String="",
                  var ventanan:Long
                  ):Parcelable
