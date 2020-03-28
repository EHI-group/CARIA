package com.mateocvas.caria

class Funciones {

    fun formato(numero: Long): String {

        val entrada = numero.toString()
        var cont = 1
        val builder = StringBuilder("")
        for (i in entrada.indices) {
            builder.append(entrada[entrada.length - 1 - i])
            if (cont % 3 == 0 && i != entrada.length - 1)
                builder.append(".")
            cont++
        }
        builder.append("$")

        return builder.reverse().toString()
    }

    fun desformato(texto: String): Long {
        return(texto.substring(1, texto.length).replace("\\.".toRegex(), "")).toLong()
    }

}