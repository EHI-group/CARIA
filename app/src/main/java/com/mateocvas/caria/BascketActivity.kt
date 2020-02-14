package com.mateocvas.caria

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.mateocvas.caria.adapters.AdapterRecyclerMarket
import com.mateocvas.caria.items.ItemProduct
import kotlinx.android.synthetic.main.activity_basket.*


class BascketActivity:AppCompatActivity() {

    lateinit var fruver:ArrayList<ItemProduct>
    lateinit var food:ArrayList<ItemProduct>
    lateinit var medicinal:ArrayList<ItemProduct>

    lateinit var Adapterfruver:AdapterRecyclerMarket
    lateinit var Adapterfood:AdapterRecyclerMarket
    lateinit var Adaptermedicinal:AdapterRecyclerMarket




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