package com.mateocvas.caria

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mateocvas.caria.adapters.AdapterSimpleItem
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity :AppCompatActivity(),View.OnClickListener {
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
        else if (temp[3].equals(""))
            this.aregister_et_aldress.setError(this.getString(R.string.error_not_fill))
        else if (temp[1].length != 10 && temp[1].length != 7)
            this.aregister_et_numero.setError(this.getString(R.string.error_digit));
        else {
            FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val editor = this.getSharedPreferences("user", Context.MODE_PRIVATE).edit()
                    editor.putString("name", temp[0])
                    editor.putString("number1", temp[1])
                    editor.putString("city", this.aregister_sp_spinner.selectedItem as String)
                    editor.putString("aldres", temp[3])
                    editor.putBoolean("first", true)
                    editor.apply()
                    FirebaseFirestore.getInstance().collection("control").document(FirebaseAuth.getInstance().currentUser!!.uid).set( hashMapOf(
                        "estado" to "0"))
                    val intent2= Intent(this,Principal::class.java)

                    startActivity(intent2)



                } else
                    Toast.makeText(
                        this,
                        getString(R.string.toast_error_registro_incompleto),
                        Toast.LENGTH_LONG
                    ).show()
                // ...
            }


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firstInit()
        setContentView(R.layout.activity_register)

        Log.d("hola","Entre a register")
        begin()

    }

    fun begin(){
        this.aregister_sp_spinner.adapter=AdapterSimpleItem(arrayListOf(getString(R.string.city1),getString(R.string.city2),getString(R.string.city3),getString(R.string.city4)),this)
        this.aregister_bt_done.setOnClickListener(this)

        val pref = this.getSharedPreferences("user", Context.MODE_PRIVATE)
        this.aregister_et_name.setText(pref.getString("name", ""))
        this.aregister_et_numero.setText(pref.getString("number1",""))
        //this.aregister_et_number2.setText(pref.getString("number2",""))
        this.aregister_et_aldress.setText(pref.getString("aldres",""))

        val city=pref.getString("city","")

        if(city.equals(getString(R.string.city1)))
            this.aregister_sp_spinner.setSelection(0)
        else if(city.equals(getString(R.string.city2)))
            this.aregister_sp_spinner.setSelection(1)
        else if(city.equals(getString(R.string.city3)))
            this.aregister_sp_spinner.setSelection(2)
        else if(city.equals(getString(R.string.city4)))
            this.aregister_sp_spinner.setSelection(3)




    }

    fun firstInit(){


        val preference=this.getSharedPreferences("user", Context.MODE_PRIVATE)
        val cond=preference.getBoolean("first",false)

        if(cond){
            val intent2= Intent(this,Principal::class.java)
            startActivity(intent2)
        }



    }


}