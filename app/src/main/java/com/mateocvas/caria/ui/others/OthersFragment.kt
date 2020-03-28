package com.mateocvas.caria.ui.others

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mateocvas.caria.R
import kotlinx.android.synthetic.main.fragment_others.view.*
import android.view.KeyEvent
import com.mateocvas.caria.UpdateUserData
import kotlinx.android.synthetic.main.ventana_confirmar.*
import java.util.*


class OthersFragment : Fragment(),View.OnClickListener {

    private lateinit var  dialog:Dialog
    lateinit var root:View


    override fun onResume() {

        super.onResume()

        view!!.isFocusableInTouchMode = true
        view!!.requestFocus()
        view!!.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                val dialog=Dialog(root.context)
                dialog.setContentView(R.layout.ventana_confirmar)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
                dialog.vconfirm_bt_cancelar.setOnClickListener {
                    dialog.dismiss() }
                dialog.vconfirm_bt_aceptar.setOnClickListener {
                    activity!!.finish()
                }
                dialog.show()

                true

            } else false
        }
    }


    override fun onClick(p0: View?) {
        when (p0!!.id){
            R.id.fothers_bt_actualizar->{
                startActivity(Intent(root.context,UpdateUserData::class.java))
             }

            R.id.fothers_bt_pago->{
                dialog.setContentView(R.layout.ventana_info_pago)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            }

            R.id.fothers_bt_aclaraciones->{
                dialog.setContentView(R.layout.ventana_info_aclaraciones)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            }

            R.id.fothers_bt_contacto->{
                dialog.setContentView(R.layout.ventana_info_contacto)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            }

            R.id.fothers_bt_acercade->{
                dialog.setContentView(R.layout.ventana_info_acercade)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()

            }

            R.id.fothers_bt_agradecimientos->{
                dialog.setContentView(R.layout.ventana_info_agradecimientos)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            }


            R.id.fothers_bt_politicas->{
            }


        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

         root = inflater.inflate(R.layout.fragment_others, container, false)


begin()
        return root
    }

    fun begin(){
        dialog= Dialog(root.context)
        root.fothers_bt_actualizar.setOnClickListener(this)
        root.fothers_bt_pago.setOnClickListener(this)
        root.fothers_bt_aclaraciones.setOnClickListener(this)
        root.fothers_bt_contacto.setOnClickListener(this)
        root.fothers_bt_acercade.setOnClickListener(this)
        root.fothers_bt_agradecimientos.setOnClickListener(this)
        root.fothers_bt_politicas.setOnClickListener(this)

        val pref = root.context.getSharedPreferences("user", Context.MODE_PRIVATE)
        val nombre=pref.getString("name","")

        root.fothers_tv_title.text = ("hola "+ StringTokenizer(nombre," ").nextToken())

    }
}