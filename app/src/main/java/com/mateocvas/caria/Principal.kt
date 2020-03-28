package com.mateocvas.caria

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.aurelhubert.ahbottomnavigation.notification.AHNotification
import com.mateocvas.caria.adapters.ViewPagerAdapter
import com.mateocvas.caria.ui.Comunication
import com.mateocvas.caria.ui.bascket.BascketFragment
import com.mateocvas.caria.ui.others.OthersFragment
import com.mateocvas.caria.ui.register.RegisterFragment
import com.mateocvas.caria.ui.shoop.ShoopFragment
import kotlinx.android.synthetic.main.ventana_confirmar.*
import kotlin.system.exitProcess


class Principal : AppCompatActivity(){


    private lateinit var dialog:Dialog
    lateinit var bottomNavigation:AHBottomNavigation
    private lateinit var pager:ViewPager
    private val notification =  AHNotification.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
         bottomNavigation = findViewById<View>(R.id.nav_view) as AHBottomNavigation
         pager=findViewById(R.id.pager)



        begin()

    }






    private fun settingsNav(){

        // Create items
        val item1 = AHBottomNavigationItem(R.string.botom1, R.drawable.ic_menu_send, R.color.colorPrimary)
        val item2 = AHBottomNavigationItem(R.string.botom2, R.drawable.ic_menu_share, R.color.colorPrimary)
        val item3 = AHBottomNavigationItem(R.string.botom3, R.drawable.ic_menu_gallery,R.color.colorPrimary)
        val item4 = AHBottomNavigationItem(R.string.botom4, R.drawable.ic_menu_camera, R.color.colorPrimary)

        // Add items
        bottomNavigation.addItem(item1)
        bottomNavigation.addItem(item2)
        bottomNavigation.addItem(item3)
        bottomNavigation.addItem(item4)

        bottomNavigation.accentColor=R.color.colorPrimary

        bottomNavigation.isBehaviorTranslationEnabled = false

        bottomNavigation.inactiveColor = Color.parseColor("#747474")


        bottomNavigation.setOnTabSelectedListener { position, wasSelected ->

            when (position) {
                0 -> {
                    pager.currentItem = 0
                }
                1 -> {
                    pager.currentItem = 1
                }
                2-> {
                    pager.currentItem = 2
                }
                3 -> {
                    pager.currentItem = 3
                }


            }
            true
        }


    }

    private lateinit var model: Comunication

    fun  begin () {


       // val navView: BottomNavigationView = findViewById(R.id.nav_view)
        //val navController = findNavController(R.id.nav_host_fragment)
        //navView.setupWithNavController(navController)

        pager.offscreenPageLimit=4

        notification.setTextColor(this.getColor(R.color.colorOrange))
        notification.setBackgroundColor(this.getColor(R.color.color_withe))
        settingsNav()

        val fragmentos=ArrayList<Fragment>()
        fragmentos.add(ShoopFragment())
        fragmentos.add(BascketFragment())
        fragmentos.add(RegisterFragment())
        fragmentos.add(OthersFragment())


        this.pager.adapter=ViewPagerAdapter(supportFragmentManager,fragmentos)




         model = ViewModelProviders.of(this)[Comunication::class.java]
         model.item_number_com.observe(this, Observer {

             bottomNavigation.setNotificationBackgroundColor(getColor((R.color.colorOrange)))
             bottomNavigation.setNotificationTextColor(getColor((R.color.color_withe)))
             if(it!=0)
                bottomNavigation.setNotification(it.toString() ,1)
             else
                bottomNavigation.setNotification( "",1)


         })



        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> bottomNavigation.currentItem = 0
                    1 -> bottomNavigation.currentItem = 1
                    2 -> bottomNavigation.currentItem = 2
                    3 -> bottomNavigation.currentItem = 3
                }
            }

        })

        dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.ventana_confirmar)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.vconfirm_bt_cancelar.setOnClickListener {
            dialog.dismiss() }
        dialog.vconfirm_bt_aceptar.setOnClickListener {
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(1)
        }

    }



}
