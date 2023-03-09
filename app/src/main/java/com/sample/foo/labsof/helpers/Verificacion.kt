package com.sample.foo.labsof.helpers

import android.widget.EditText

class Verificacion {
    companion object {
        fun notVacio(editText: EditText): Boolean {
            return editText.text.toString().replace(" ", "") != ""
        }

        fun email(editText: EditText): Boolean {
            return editText.text.toString().contains("@", true) &&
                    editText.text.toString().contains(".com", true)
        }
        fun url(editText: EditText): Boolean {
            return editText.text.toString().contains("https://", true) &&
                    editText.text.toString().contains("htttp://", true)
        }
    }
}