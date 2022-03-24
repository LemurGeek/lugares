package com.isc.lugares.utiles

import java.text.SimpleDateFormat
import java.util.*

class OtrosUtiles {
    companion object {
        fun getTempFile(prefijo:String) : String {
            var nombre=SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            return prefijo+nombre
        }
    }
}