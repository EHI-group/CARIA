package com.mateocvas.caria

import android.app.Dialog
import android.content.Context
import android.content.Intent

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.mateocvas.caria.items.ItemProduct
import kotlinx.android.synthetic.main.ventana_confirmar.*
import kotlinx.android.synthetic.main.ventana_confirmar_envio.*

class Principal : AppCompatActivity(){


    lateinit var dialog:Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)



        begin()

    }







    fun  begin () {


        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.ventana_confirmar)
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.vconfirm_bt_cancelar.setOnClickListener {
            dialog.dismiss() }
        dialog.vconfirm_bt_aceptar.setOnClickListener {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

    }



}
