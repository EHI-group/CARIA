package com.mateocvas.caria

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mateocvas.caria.items.ItemProduct
import kotlinx.android.synthetic.main.activity_product_selected.*


class SelectedProduct   :AppCompatActivity(), View.OnClickListener {

    private lateinit var utiles: Funciones
    private lateinit var product:ItemProduct
    private lateinit var base:StorageReference
    private lateinit var warnings: Warinings
    private var total:Long=0
    private var totalUnidad:Long=0
    private var amount=1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_selected)
        begin()
        set_listeners()
    }

    fun begin(){
        warnings= Warinings()
        utiles= Funciones()
        base=FirebaseStorage.getInstance().reference
        product=intent.getParcelableExtra("product") as ItemProduct
        GlideApp.with(this)
            .load(base.child(product.path +"/"+product.nombre+".png"))
            .into( this.aprosel_iv_icono  )

        total=utiles.desformato(product.precio)
        totalUnidad=total


        if(product.unidad == "1")
            this.aprosel_tv_descripcion.text = ("1 "+product.nombreMostrar+" (a granel).")
        else
            this.aprosel_tv_descripcion.text = ("1 "+product.unidad+" de "+product.nombreMostrar+".")

        //this.aprosel_tv_beneficios.setText(product.beneficios)
        this.aprosel_tv_nombre.text = product.nombreMostrar
        this.aprosel_tv_total.text = product.precio

    }

    private fun set_listeners() {
            this.aprosel_ib_minus.setOnClickListener(this)
            this.aprosel_ib_plus.setOnClickListener(this)
            this.aprosel_bt_ingresar.setOnClickListener(this)

    }



    override fun onClick(view: View) {
        when (view.id) {
            R.id.aprosel_ib_plus -> {
                total+=totalUnidad
                amount++
                this.aprosel_tv_unidad.text = amount.toString()
                this.aprosel_tv_total.text = utiles.formato(total)
            }

            R.id.aprosel_ib_minus -> {
                if(amount==1)
                    warnings.prosel_minus(this)
                else{
                        total -= totalUnidad
                        amount--
                    this.aprosel_tv_unidad.text = amount.toString()
                    this.aprosel_tv_total.text = utiles.formato(total)
                }
            }

            R.id.aprosel_bt_ingresar -> {
                val intent = intent
                intent.putExtra("amount", amount)
                intent.putExtra("name", product.nombre)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }




}


