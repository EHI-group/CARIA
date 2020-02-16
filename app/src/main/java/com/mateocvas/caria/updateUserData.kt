package com.mateocvas.caria

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mateocvas.caria.adapters.AdapterSimpleItem
import kotlinx.android.synthetic.main.activity_register.*

class updateUserData: AppCompatActivity(),View.OnClickListener{

    override fun onClick(p0: View?) {
        val temp = arrayOf(
            this.aregister_et_name.text.toString(),
            this.aregister_et_numero.text.toString(),
            this.aregister_et_aldress.text.toString()
        )

        if (temp[0].equals(""))
            this.aregister_et_name.setError(this.getString(R.string.error_not_fill))
        else if (temp[1].equals(""))
            this.aregister_et_numero.setError(this.getString(R.string.error_not_fill))
        else if (temp[2].equals(""))
            this.aregister_et_aldress.setError(this.getString(R.string.error_not_fill))
        else if (temp[1].length != 10 && temp[1].length != 7)
            this.aregister_et_numero.setError(this.getString(R.string.error_digit));
        else {

                    val editor = this.getSharedPreferences("user", Context.MODE_PRIVATE).edit()
                    editor.putString("name", temp[0])
                    editor.putString("number1", temp[1])
                    editor.putString("number2", temp[2])
                    editor.putString("city", this.aregister_sp_spinner.selectedItem as String)
                    editor.putString("aldres", temp[3])
                    editor.putBoolean("first", true)
                    editor.apply()

                    finish()



                // ...
            }



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        begin()
        super.onCreate(savedInstanceState)
    }


    fun begin(){
        setContentView(R.layout.activity_register)
        this.aregister_est_register.text=getString(R.string.titulo_actualice)
        this.aregister_sp_spinner.adapter= AdapterSimpleItem(arrayListOf("Bello","Envigado","Itagui","Medellin"),this)
        this.aregister_bt_done.setOnClickListener(this)
    }

}