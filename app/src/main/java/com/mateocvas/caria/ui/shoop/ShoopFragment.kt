package com.mateocvas.caria.ui.shoop


import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TabHost
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mateocvas.caria.R
import com.mateocvas.caria.ui.Comunication

import kotlinx.android.synthetic.main.fragment_shop.view.*
import org.w3c.dom.Text
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class ShoopFragment : Fragment(),TabHost.OnTabChangeListener{


     lateinit var root:View
     lateinit var model:ShoopViewModel
     lateinit var comunication:Comunication

     private val tabs=ArrayList<TextView>()
     private val tabs_back=ArrayList<View>()





    override fun onTabChanged(p0: String?) {

        model.selected_tab=p0!!
        var t0=0
        var t1=1
        var t2=2

        if(p0.equals(root.context.getString(R.string.tag_tab2))){
             t0=1
             t1=0
             t2=2

        }
         else if(p0.equals(root.context.getString(R.string.tag_tab3)))   {
            t0=2
            t1=0
            t2=1

        }


        tabs[t0].setTextColor(root.context.getColor(R.color.color_withe))
        tabs_back[t0].setBackground(root.context.getDrawable(R.drawable.tab_rounded))
        tabs[t1].setTextColor(root.context.getColor(R.color.color_purple))
        tabs_back[t1].setBackgroundColor(root.context.getColor(R.color.color_withe))
        tabs[t2].setTextColor(root.context.getColor(R.color.color_purple))
        tabs_back[t2].setBackgroundColor(root.context.getColor(R.color.color_withe))

    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        model=activity?.run {
            ViewModelProviders.of(this).get(ShoopViewModel::class.java)
        }?:throw Exception("Invalid Activity")

        comunication=activity?.run {
            ViewModelProviders.of(this).get(Comunication::class.java)
        }?:throw Exception("Invalid Activity")





        model.begin(this)

         root = inflater.inflate(R.layout.fragment_shop, container, false)
        setTab(root)
        setAdapters()
        setListeners()
        setObservers()
        return root
    }


fun setTab(view: View){
    view.fshoop_rv_fruver.setHasFixedSize(true)
    view.fshoop_rv_fruver.layoutManager = LinearLayoutManager(view.context)

    val tabHost=view.findViewById<TabHost>(R.id.tabhost)
    tabHost.setup()
    val tab1 = tabHost.newTabSpec(getString(R.string.tag_tab1))
    tab1.setIndicator("Frutas")
    tab1.setContent(R.id.fshoop_tb_tab1)
    tabHost.addTab(tab1)

    val tab2 = tabHost.newTabSpec(getString(R.string.tag_tab2))
    tab2.setIndicator("Verduras")
    tab2.setContent(R.id.fshoop_tb_tab2)
    tabHost.addTab(tab2)

    val tab3 = tabHost.newTabSpec(getString(R.string.tag_tab3))
    tab3.setIndicator("Otros")
    tab3.setContent(R.id.fshoop_tb_tab3)
    tabHost.addTab(tab3)


    for (i in 0 until tabHost.getTabWidget().getChildCount()) {
        val tv = tabHost.getTabWidget().getChildAt(i).findViewById<TextView>(android.R.id.title)
       tabs_back.add(tabHost.getTabWidget().getChildAt(i))
        tv.setAllCaps(false)
        tabs.add(tv)
    }
    tabs[0].setTextColor(root.context.getColor(R.color.color_withe))
    tabs_back[0].setBackground(root.context.getDrawable(R.drawable.tab_rounded))
    tabs[1].setTextColor(root.context.getColor(R.color.color_purple))
    tabs_back[1].setBackgroundColor(root.context.getColor(R.color.color_withe))
    tabs[2].setTextColor(root.context.getColor(R.color.color_purple))
    tabs_back[2].setBackgroundColor(root.context.getColor(R.color.color_withe))


}

    fun setAdapters(){

        root.fshoop_rv_fruver.setHasFixedSize(false)
        root.fshoop_rv_fruver.layoutManager = LinearLayoutManager(root.context)
        root.fshoop_rv_fruver.adapter=model.adapter_fruver
        root.fshoop_rv_fruver.addOnScrollListener (ListenerRecycler())







        model.adapter_fruver.notifyDataSetChanged()

        root.fshoop_rv_food.setHasFixedSize(false)
        root.fshoop_rv_food.layoutManager = LinearLayoutManager(root.context)
        root.fshoop_rv_food.adapter=model.adapter_food
        model.adapter_fruver.notifyDataSetChanged()


        root.fshoop_rv_medicinal.setHasFixedSize(false)
        root.fshoop_rv_medicinal.layoutManager = LinearLayoutManager(root.context)
        root.fshoop_rv_medicinal.adapter=model.adapter_medicinal
        model.adapter_fruver.notifyDataSetChanged()

    }

    fun setListeners(){
        root.tabhost.setOnTabChangedListener(this)
        root.fshoop_et_search.addTextChangedListener(model)
    }



    fun setObservers(){


        comunication.data_fruver_com.observe(this.activity!!, Observer {
          model.refreshDataFruver(it)
        })

        comunication.data_food_com.observe(this.activity!!, Observer {
            model.refreshDataFood(it)
        })

        comunication.data_food_com.observe(this.activity!!, Observer {
            model.refreshDataMedicinal(it)
        })




    }

    private inner class ListenerRecycler:RecyclerView.OnScrollListener () {
        var arriba:Long=0
        var abajo:Long=0
        val pararam1=root.fshoop_et_search.layoutParams as ConstraintLayout.LayoutParams
        val tam1=root.fshoop_et_search.height
        val tam2=root.textView.height
        val pararam2=root.textView.layoutParams as ConstraintLayout.LayoutParams




        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            
            if(dy>8){
                arriba+=dy
                    if(arriba>=10){
                        arriba=0
                        if(!(root.textView.height-4<=0)) {
                             pararam2.height=root.textView.height-6
                             root.textView.setLayoutParams(pararam2)
                        }

                        if(!(root.fshoop_et_search.height-4<=0)) {
                            pararam1.height= root.fshoop_et_search.height-6
                            root.fshoop_et_search.setLayoutParams(pararam1)
                        }
                        }
            }
            else if(dy<-8){
                abajo+=-1*(dy)
                if(abajo>=10){
                    abajo=0
                    if(root.textView.height+7>=tam2){
                        pararam2.height=tam2
                        root.textView.setLayoutParams(pararam2)
                    }
                    else {
                        pararam2.height=root.textView.height + 11
                        root.textView.setLayoutParams(pararam2)
                    }
                    if(root.fshoop_et_search.height+7>=tam1){
                        pararam1.height=tam1
                        root.fshoop_et_search.setLayoutParams(pararam1)
                    }
                    else {
                        pararam1.height=root.textView.height + 11
                        root.fshoop_et_search.setLayoutParams(pararam1)
                    }

                }


                    }

            }


        }
    }




