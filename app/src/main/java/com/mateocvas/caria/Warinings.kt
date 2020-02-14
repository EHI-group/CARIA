package com.mateocvas.caria

import android.content.Context
import android.widget.Toast

class Warinings {
    fun prosel_minus(context: Context){
        Toast.makeText(context,context.getString(R.string.toast_cantidad_positiva),Toast.LENGTH_LONG).show()
    }


}