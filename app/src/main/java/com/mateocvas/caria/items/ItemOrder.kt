package com.mateocvas.caria.items

data class ItemOrder(
    val total: Long,
    val fecha: String,
    var numero: Int,
    val fruver: ArrayList<ItemProduct>,
    val food: ArrayList<ItemProduct>,
    val medicinal: ArrayList<ItemProduct>
    )
