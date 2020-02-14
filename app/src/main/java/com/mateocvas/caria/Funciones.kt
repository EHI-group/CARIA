package com.mateocvas.caria

class Funciones {

    fun formato(numero: Long): String {

        val entrada = numero.toString()
        var cont = 1
        val builder = StringBuilder("POC ")
        for (i in 0 until entrada.length) {
            builder.append(entrada[entrada.length - 1 - i])
            if (cont % 3 == 0 && i != entrada.length - 1)
                builder.append(".")
            cont++
        }

        return builder.reverse().toString()
    }

    fun desformato(texto: String): Long {
        return(texto.substring(0, texto.length - 4).replace("\\.".toRegex(), "")).toLong()
    }

}