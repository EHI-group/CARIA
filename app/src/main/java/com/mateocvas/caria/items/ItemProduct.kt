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
                       val path:String,
                       val palabrasClave:String,
                       var mensaje:String,
                       val ventanan:Long,
                       var ind1:Int,
                       var ind2:Int,
                       var tipo1:String,
                       var tipo2:String,
                       val array1:ArrayList<String>,
                       val array2:ArrayList<String>,
                       val porcentaje1:ArrayList<Double>,
                       val porcentaje2:ArrayList<Double>
                  ):Parcelable
