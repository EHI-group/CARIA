package com.mateocvas.caria.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.mateocvas.caria.items.ItemOrder

class RegisterViewModel : ViewModel() {

    val data=ArrayList<ItemOrder>()
    val data_com=MutableLiveData<java.util.ArrayList<ItemOrder>>()
    lateinit var selected_item:ItemOrder
    val selected_item_com= MutableLiveData<ItemOrder>()
    val send_data_= MutableLiveData<ItemOrder>()




    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }

    fun setData(data:ArrayList<ItemOrder>){
        this.data.clear()
        this.data.addAll(data)
        data_com.value=this.data
    }


    fun setItemOrder(item:ItemOrder){
        this.selected_item=item
        this.selected_item_com.value=selected_item
    }














}