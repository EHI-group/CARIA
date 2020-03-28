package com.mateocvas.caria

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.mateocvas.caria.adapters.AdapterRecyclerMarket
import com.mateocvas.caria.items.ItemProduct


class BascketActivity:AppCompatActivity() {

    private lateinit var fruver:ArrayList<ItemProduct>
    private lateinit var food:ArrayList<ItemProduct>
    private lateinit var medicinal:ArrayList<ItemProduct>


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_basket)

        begin()
    }


    fun begin(){
        fruver = intent.extras?.getParcelableArrayList<ItemProduct>("Object_list") as ArrayList<ItemProduct>
        food = intent.extras?.getParcelableArrayList<ItemProduct>("Object_list") as ArrayList<ItemProduct>
        medicinal = intent.extras?.getParcelableArrayList<ItemProduct>("Object_list") as ArrayList<ItemProduct>

        /*
        Adapterfruver=AdapterRecyclerMarket(this.applicationContext,fruver)
        Adapterfood=AdapterRecyclerMarket(this,food)
        Adaptermedicinal=AdapterRecyclerMarket(this,medicinal)

        this.abask_rv_fruver.adapter=Adapterfruver
        this.abask_rv_food.adapter=Adapterfood
        this.abask_rv_medicinal.adapter=Adaptermedicinal

        Adapterfruver.notifyDataSetChanged()
        Adapterfood.notifyDataSetChanged()
        Adaptermedicinal.notifyDataSetChanged()

    */
    }









}