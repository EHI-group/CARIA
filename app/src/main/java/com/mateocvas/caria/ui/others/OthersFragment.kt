package com.mateocvas.caria.ui.others

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.mateocvas.caria.R
import com.mateocvas.caria.RegisterActivity
import kotlinx.android.synthetic.main.fragment_others.view.*

class OthersFragment : Fragment(),View.OnClickListener {

    lateinit var  dialog:Dialog
    lateinit var root:View

    override fun onClick(p0: View?) {
        when (p0!!.id){
            R.id.fothers_bt_actualizar->{
                startActivity(Intent(root.context,RegisterActivity::class.java))
             }

            R.id.fothers_bt_agradecimientos->{
                dialog.setContentView(R.layout.ventana_agradecimientos)
                dialog.show()
            }

            R.id.fothers_bt_aclaraciones->{
                dialog.setContentView(R.layout.ventana_aclaraciones)
                dialog.show()
            }

            R.id.fothers_bt_contactos->{
                dialog.setContentView(R.layout.ventana_contacto)
                dialog.show()
            }


            R.id.fothers_bt_nosotros->{
                dialog.setContentView(R.layout.ventana_nosotros)
                dialog.show()

            }


            R.id.fothers_bt_politicas->{
            }


        }
    }

    private lateinit var galleryViewModel: OthersViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProviders.of(this).get(OthersViewModel::class.java)
         root = inflater.inflate(R.layout.fragment_others, container, false)


begin()
        return root
    }

    fun begin(){
        dialog= Dialog(root.context)
        root.fothers_bt_aclaraciones.setOnClickListener(this)
        root.fothers_bt_actualizar.setOnClickListener(this)
        root.fothers_bt_agradecimientos.setOnClickListener(this)
        root.fothers_bt_contactos.setOnClickListener(this)
        root.fothers_bt_nosotros.setOnClickListener(this)
        root.fothers_bt_politicas.setOnClickListener(this)

    }
}