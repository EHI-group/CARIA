package com.mateocvas.caria

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.mateocvas.caria.adapters.AdapterSimpleItem
import kotlinx.android.synthetic.main.activity_register.*

class UpdateUserData: AppCompatActivity(),View.OnClickListener{

    override fun onClick(p0: View?) {
        val temp = arrayOf(
            this.aregister_et_name.text.toString(),
            this.aregister_et_numero.text.toString(),
            this.aregister_et_aldress.text.toString()
        )

        if (temp[0] == "")
            this.aregister_et_name.error = this.getString(R.string.error_not_fill)
        else if (temp[1] == "")
            this.aregister_et_numero.error = this.getString(R.string.error_not_fill)
        else if (temp[2] == "")
            this.aregister_et_aldress.error = this.getString(R.string.error_not_fill)
        else if (temp[1].length != 10 && temp[1].length != 7)
            this.aregister_et_numero.error = this.getString(R.string.error_digit)
        else {

                    val editor = this.getSharedPreferences("user", Context.MODE_PRIVATE).edit()
                    editor.putString("name", temp[0])
                    editor.putString("number1", temp[1])
                    editor.putString("city", this.aregister_sp_spinner.selectedItem as String)
                    editor.putString("aldres", temp[3])
                    FirebaseDatabase.getInstance().reference.child("control/${FirebaseAuth.getInstance().currentUser!!.uid}/nombre").setValue(temp[0])

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
        this.aregister_sp_spinner.adapter=AdapterSimpleItem(arrayListOf(getString(R.string.city1),getString(R.string.city2),getString(R.string.city3),getString(R.string.city4)),this)
        this.aregister_bt_done.setOnClickListener(this)


        val pref = this.getSharedPreferences("user", Context.MODE_PRIVATE)
        this.aregister_et_name.setText(pref.getString("name",""))
        this.aregister_et_numero.setText(pref.getString("number1",""))
        this.aregister_et_aldress.setText(pref.getString("city",""))

       val city=pref.getString("city","")
        if(city.equals(getString(R.string.city1)))
            this.aregister_sp_spinner.setSelection(0)
        else if(city.equals(getString(R.string.city2)))
            this.aregister_sp_spinner.setSelection(1)
        else if(city.equals(getString(R.string.city3)))
            this.aregister_sp_spinner.setSelection(2)
        else if(city.equals(getString(R.string.city4)))
            this.aregister_sp_spinner.setSelection(3)


        aregister_sp_spinner


        finish()

    }

}